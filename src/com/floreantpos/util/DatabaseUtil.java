package com.floreantpos.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import com.floreantpos.bo.actions.DataImportAction;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Tax;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.ShiftDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.model.dao.UserTypeDAO;
import com.floreantpos.model.dao._RootDAO;

public class DatabaseUtil {
	private static Log logger = LogFactory.getLog(DatabaseUtil.class);

	public static void checkConnection(String connectionString, String hibernateDialect, String hibernateConnectionDriverClass, String user, String password)
			throws DatabaseConnectionException {
		Configuration configuration = _RootDAO.getNewConfiguration(null);

		configuration = configuration.setProperty("hibernate.dialect", hibernateDialect);
		configuration = configuration.setProperty("hibernate.connection.driver_class", hibernateConnectionDriverClass);

		configuration = configuration.setProperty("hibernate.connection.url", connectionString);
		configuration = configuration.setProperty("hibernate.connection.username", user);
		configuration = configuration.setProperty("hibernate.connection.password", password);

		checkConnection(configuration);
	}

	public static void checkConnection() throws DatabaseConnectionException {
		Configuration configuration = _RootDAO.getNewConfiguration(null);
		checkConnection(configuration);
	}

	public static void checkConnection(Configuration configuration) throws DatabaseConnectionException {
		try {
			SessionFactory sessionFactory = configuration.buildSessionFactory();
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			session.close();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	public static boolean createDatabase(String connectionString, String hibernateDialect, String hibernateConnectionDriverClass, String user, String password, boolean exportSampleData) {
		try {
			Configuration configuration = _RootDAO.getNewConfiguration(null);

			configuration = configuration.setProperty("hibernate.dialect", hibernateDialect);
			configuration = configuration.setProperty("hibernate.connection.driver_class", hibernateConnectionDriverClass);

			configuration = configuration.setProperty("hibernate.connection.url", connectionString);
			configuration = configuration.setProperty("hibernate.connection.username", user);
			configuration = configuration.setProperty("hibernate.connection.password", password);
			configuration = configuration.setProperty("hibernate.hbm2ddl.auto", "create");

			SchemaExport schemaExport = new SchemaExport(configuration);
			schemaExport.create(true, true);

			_RootDAO.initialize();

			Restaurant restaurant = new Restaurant();
			restaurant.setId(Integer.valueOf(1));
			restaurant.setName("Sample Restaurant");
			restaurant.setAddressLine1("Somewhere");
			restaurant.setTelephone("+0123456789");
			RestaurantDAO.getInstance().saveOrUpdate(restaurant);

			Tax tax = new Tax();
			tax.setName("US");
			tax.setRate(Double.valueOf(6));
			TaxDAO.getInstance().saveOrUpdate(tax);

			Shift shift = new Shift();
			shift.setName(com.floreantpos.POSConstants.GENERAL);
			java.util.Date shiftStartTime = ShiftUtil.buildShiftStartTime(0, 0, 0, 11, 59, 1);
			java.util.Date shiftEndTime = ShiftUtil.buildShiftEndTime(0, 0, 0, 11, 59, 1);

			shift.setStartTime(shiftStartTime);
			shift.setEndTime(shiftEndTime);
			long length = Math.abs(shiftStartTime.getTime() - shiftEndTime.getTime());

			shift.setShiftLength(Long.valueOf(length));
			ShiftDAO.getInstance().saveOrUpdate(shift);

			UserType type = new UserType();
			type.setName(com.floreantpos.POSConstants.ADMINISTRATOR);
			type.setPermissions(new HashSet<UserPermission>(Arrays.asList(UserPermission.permissions)));
			UserTypeDAO.getInstance().saveOrUpdate(type);

			User u = new User();
			u.setUserId(123);
			u.setSsn("123");
			u.setPassword("1111");
			u.setFirstName("Administrator");
			u.setLastName(com.floreantpos.POSConstants.USER);
			u.setType(type);

			UserDAO dao = new UserDAO();
			dao.saveOrUpdate(u);
			
			if(!exportSampleData) {
				return true;
			}
			
			DataImportAction.importMenuItems(DatabaseUtil.class.getResourceAsStream("/floreantpos-menu-items.xml"));

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return false;
		}
	}
	
	public static boolean updateDatabase(String connectionString, String hibernateDialect, String hibernateConnectionDriverClass, String user, String password) {
		try {
			Configuration configuration = _RootDAO.getNewConfiguration(null);

			configuration = configuration.setProperty("hibernate.dialect", hibernateDialect);
			configuration = configuration.setProperty("hibernate.connection.driver_class", hibernateConnectionDriverClass);

			configuration = configuration.setProperty("hibernate.connection.url", connectionString);
			configuration = configuration.setProperty("hibernate.connection.username", user);
			configuration = configuration.setProperty("hibernate.connection.password", password);
			configuration = configuration.setProperty("hibernate.hbm2ddl.auto", "update");

			SchemaUpdate schemaUpdate = new SchemaUpdate(configuration);
			schemaUpdate.execute(true, true);

			_RootDAO.initialize();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return false;
		}
	}

	public static Configuration initialize() throws DatabaseConnectionException {
		try {

			return _RootDAO.reInitialize();

		} catch (Exception e) {
			logger.error(e);
			throw new DatabaseConnectionException(e);
		}

	}

	public static void main(String[] args) throws Exception {
		initialize();

		List<PosTransaction> findAll = PosTransactionDAO.getInstance().findAll();
		for (PosTransaction posTransaction : findAll) {
			PosTransactionDAO.getInstance().delete(posTransaction);
		}

		List<Ticket> list = TicketDAO.getInstance().findAll();
		for (Ticket ticket : list) {
			TicketDAO.getInstance().delete(ticket);
		}
	}
}
