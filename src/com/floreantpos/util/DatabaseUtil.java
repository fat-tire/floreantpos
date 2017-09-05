/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import com.floreantpos.PosLog;
import com.floreantpos.bo.actions.DataImportAction;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.CashDrawer;
import com.floreantpos.model.Currency;
import com.floreantpos.model.Discount;
import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.Multiplier;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PizzaCrust;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Tax;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.model.dao.CurrencyDAO;
import com.floreantpos.model.dao.DiscountDAO;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuItemSizeDAO;
import com.floreantpos.model.dao.MultiplierDAO;
import com.floreantpos.model.dao.OrderTypeDAO;
import com.floreantpos.model.dao.PizzaCrustDAO;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.ShiftDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.TerminalDAO;
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
			Transaction transaction = session.beginTransaction();
			transaction.rollback();
			session.close();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	public static void updateLegacyDatabase() {
		try {
			dropModifiedTimeColumn();
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	private static void dropModifiedTimeColumn() throws SQLException {
		GenericDAO dao = new GenericDAO();
		Session session = null;

		try {
			session = dao.createNewSession();
			Connection connection = session.connection();
			String[] tables = { "CUSTOMER", "GRATUITY", "INVENTORY_GROUP", "INVENTORY_ITEM", "INVENTORY_LOCATION", "INVENTORY_META_CODE",
					"INVENTORY_TRANSACTION", "INVENTORY_TRANSACTION_TYPE", "INVENTORY_UNIT", "INVENTORY_VENDOR", "INVENTORY_WAREHOUSE", "KITCHEN_TICKET",
					"KITCHEN_TICKET_ITEM", "MENUITEM_MODIFIERGROUP", "MENU_CATEGORY", "MENU_GROUP", "MENU_ITEM", "MENU_MODIFIER", "MENU_MODIFIER_GROUP",
					"PURCHASE_ORDER", "TAX", "TERMINAL", "TICKET", "TICKETITEM_MODIFIERGROUP", "TICKET_ITEM", "TICKETITEM_DISCOUNT", "TRANSACTIONS", "USERS",
					"ZIP_CODE_VS_DELIVERY_CHARGE" };

			for (String table : tables) {
				try {
					Statement statement = connection.createStatement();
					statement.execute("ALTER TABLE " + table + " DROP COLUMN MODIFIED_TIME");
				} catch (Exception e) {
					//logger.error(e);
				}
			}
			connection.commit();
		} finally {
			dao.closeSession(session);
		}
	}

	public static boolean createDatabase(String connectionString, String hibernateDialect, String hibernateConnectionDriverClass, String user, String password,
			boolean exportSampleData) {
		try {
			Configuration configuration = _RootDAO.getNewConfiguration(null);

			configuration = configuration.setProperty("hibernate.dialect", hibernateDialect);
			configuration = configuration.setProperty("hibernate.connection.driver_class", hibernateConnectionDriverClass);

			configuration = configuration.setProperty("hibernate.connection.url", connectionString);
			configuration = configuration.setProperty("hibernate.connection.username", user);
			configuration = configuration.setProperty("hibernate.connection.password", password);
			configuration = configuration.setProperty("hibernate.hbm2ddl.auto", "create");
			configuration = configuration.setProperty("hibernate.c3p0.checkoutTimeout", "0"); //$NON-NLS-1$ //$NON-NLS-2$

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

			UserType administrator = new UserType();
			administrator.setName(com.floreantpos.POSConstants.ADMINISTRATOR);
			administrator.setPermissions(new HashSet<UserPermission>(Arrays.asList(UserPermission.permissions)));
			UserTypeDAO.getInstance().saveOrUpdate(administrator);

			UserType manager = new UserType();
			manager.setName(com.floreantpos.POSConstants.MANAGER);
			manager.setPermissions(new HashSet<UserPermission>(Arrays.asList(UserPermission.permissions)));
			UserTypeDAO.getInstance().saveOrUpdate(manager);

			UserType cashier = new UserType();
			cashier.setName(com.floreantpos.POSConstants.CASHIER);
			cashier.setPermissions(new HashSet<UserPermission>(Arrays.asList(UserPermission.CREATE_TICKET, UserPermission.SETTLE_TICKET,
					UserPermission.SPLIT_TICKET, UserPermission.VIEW_ALL_OPEN_TICKETS)));
			UserTypeDAO.getInstance().saveOrUpdate(cashier);

			UserType server = new UserType();
			server.setName("SR. CASHIER");
			server.setPermissions(new HashSet<UserPermission>(Arrays.asList(UserPermission.CREATE_TICKET, UserPermission.SETTLE_TICKET,
					UserPermission.SPLIT_TICKET)));
			//server.setTest(Arrays.asList(OrderType.BAR_TAB));
			UserTypeDAO.getInstance().saveOrUpdate(server);

			User administratorUser = new User();
			administratorUser.setUserId(123);
			administratorUser.setSsn("123");
			administratorUser.setPassword("1111");
			administratorUser.setFirstName("Admin");
			administratorUser.setLastName("System");
			administratorUser.setType(administrator);
			administratorUser.setActive(true);

			UserDAO dao = new UserDAO();
			dao.saveOrUpdate(administratorUser);

			User managerUser = new User();
			managerUser.setUserId(124);
			managerUser.setSsn("124");
			managerUser.setPassword("2222");
			managerUser.setFirstName("Lisa");
			managerUser.setLastName("Carol");
			managerUser.setType(manager);
			managerUser.setActive(true);

			dao.saveOrUpdate(managerUser);

			User cashierUser = new User();
			cashierUser.setUserId(125);
			cashierUser.setSsn("125");
			cashierUser.setPassword("3333");
			cashierUser.setFirstName("Janet");
			cashierUser.setLastName("Ann");
			cashierUser.setType(cashier);
			cashierUser.setActive(true);

			dao.saveOrUpdate(cashierUser);

			User serverUser = new User();
			serverUser.setUserId(126);
			serverUser.setSsn("126");
			serverUser.setPassword("7777");
			serverUser.setFirstName("John");
			serverUser.setLastName("Doe");
			serverUser.setType(server);
			serverUser.setActive(true);

			dao.saveOrUpdate(serverUser);

			User driverUser = new User();
			driverUser.setUserId(127);
			driverUser.setSsn("127");
			driverUser.setPassword("8888");
			driverUser.setFirstName("Poll");
			driverUser.setLastName("Brien");
			driverUser.setType(server);
			driverUser.setDriver(true);
			driverUser.setActive(true);

			dao.saveOrUpdate(driverUser);

			OrderTypeDAO orderTypeDAO = new OrderTypeDAO();
			OrderType orderType = new OrderType();
			orderType.setName("DINE IN");
			orderType.setShowTableSelection(true);
			orderType.setCloseOnPaid(true);
			orderType.setEnabled(true);
			orderType.setShouldPrintToKitchen(true);
			orderType.setShowInLoginScreen(true);
			orderTypeDAO.save(orderType);

			orderType = new OrderType();
			orderType.setName("TAKE OUT");
			orderType.setShowTableSelection(false);
			orderType.setCloseOnPaid(true);
			orderType.setEnabled(true);
			orderType.setPrepaid(true);
			orderType.setShouldPrintToKitchen(true);
			orderType.setShowInLoginScreen(true);
			orderTypeDAO.save(orderType);

			orderType = new OrderType();
			orderType.setName("RETAIL");
			orderType.setShowTableSelection(false);
			orderType.setCloseOnPaid(true);
			orderType.setEnabled(true);
			orderType.setShouldPrintToKitchen(false);
			orderType.setShowInLoginScreen(true);
			orderTypeDAO.save(orderType);

			orderType = new OrderType();
			orderType.setName("HOME DELIVERY");
			orderType.setShowTableSelection(false);
			orderType.setCloseOnPaid(false);
			orderType.setEnabled(true);
			orderType.setShouldPrintToKitchen(true);
			orderType.setShowInLoginScreen(true);
			orderType.setRequiredCustomerData(true);
			orderType.setDelivery(true);
			orderTypeDAO.save(orderType);

			DiscountDAO discountDao = new DiscountDAO();

			Discount discount1 = new Discount();
			discount1.setName("Buy 1 and get 1 free");
			discount1.setType(1);
			discount1.setValue(100.0);
			discount1.setAutoApply(false);
			discount1.setMinimunBuy(2);
			discount1.setQualificationType(0);
			discount1.setApplyToAll(true);
			discount1.setNeverExpire(true);
			discount1.setEnabled(true);

			discountDao.saveOrUpdate(discount1);

			Discount discount2 = new Discount();
			discount2.setName("Buy 2 and get 1 free");
			discount2.setType(1);
			discount2.setValue(100.0);
			discount2.setAutoApply(true);
			discount2.setMinimunBuy(3);
			discount2.setQualificationType(0);
			discount2.setApplyToAll(true);
			discount2.setNeverExpire(true);
			discount2.setEnabled(true);

			discountDao.saveOrUpdate(discount2);

			Discount discount3 = new Discount();
			discount3.setName("10% Off");
			discount3.setType(1);
			discount3.setValue(10.0);
			discount3.setAutoApply(false);
			discount3.setMinimunBuy(1);
			discount3.setQualificationType(0);
			discount3.setApplyToAll(true);
			discount3.setNeverExpire(true);
			discount3.setEnabled(true);
			discountDao.saveOrUpdate(discount3);

			int terminalId = TerminalConfig.getTerminalId();

			if (terminalId == -1) {
				Random random = new Random();
				terminalId = random.nextInt(10000) + 1;
			}
			Terminal terminal = new Terminal();
			terminal.setId(terminalId);
			terminal.setName(String.valueOf(terminalId)); //$NON-NLS-1$

			TerminalDAO.getInstance().saveOrUpdate(terminal);

			CashDrawer cashDrawer = new CashDrawer();
			cashDrawer.setTerminal(terminal);

			Currency currency = new Currency();
			currency.setName("USD");
			currency.setCode("USD");
			currency.setSymbol("$");
			currency.setExchangeRate(1.0);
			currency.setMain(true);
			CurrencyDAO.getInstance().save(currency);

			currency = new Currency();
			currency.setName("EUR");
			currency.setCode("EUR");
			currency.setSymbol("E");
			currency.setExchangeRate(0.8);
			CurrencyDAO.getInstance().save(currency);

			currency = new Currency();
			currency.setName("BRL");
			currency.setCode("BRL");
			currency.setSymbol("B");
			currency.setExchangeRate(3.47);
			CurrencyDAO.getInstance().save(currency);

			currency = new Currency();
			currency.setName("ARS");
			currency.setCode("ARS");
			currency.setSymbol("P");
			currency.setExchangeRate(13.89);
			CurrencyDAO.getInstance().save(currency);

			currency = new Currency();
			currency.setName("PYG");
			currency.setCode("PYG");
			currency.setSymbol("P");
			currency.setExchangeRate(5639.78);
			currency.setDecimalPlaces(0);
			CurrencyDAO.getInstance().save(currency);

			MenuItemSize menuItemSize = new MenuItemSize();
			menuItemSize.setName("SMALL");
			menuItemSize.setSortOrder(0);
			MenuItemSizeDAO.getInstance().save(menuItemSize);

			menuItemSize = new MenuItemSize();
			menuItemSize.setName("MEDIUM");
			menuItemSize.setSortOrder(1);
			MenuItemSizeDAO.getInstance().save(menuItemSize);

			menuItemSize = new MenuItemSize();
			menuItemSize.setName("LARGE");
			menuItemSize.setSortOrder(2);
			MenuItemSizeDAO.getInstance().save(menuItemSize);

			PizzaCrust crust = new PizzaCrust();
			crust.setName("PAN");
			crust.setSortOrder(0);
			PizzaCrustDAO.getInstance().save(crust);

			crust = new PizzaCrust();
			crust.setName("HAND TOSSED");
			crust.setSortOrder(1);
			PizzaCrustDAO.getInstance().save(crust);

			Multiplier multiplier = new Multiplier("Regular");
			multiplier.setRate(0.0);
			multiplier.setSortOrder(0);
			multiplier.setTicketPrefix("");
			multiplier.setDefaultMultiplier(true);
			multiplier.setMain(true);
			MultiplierDAO.getInstance().save(multiplier);

			multiplier = new Multiplier("No");
			multiplier.setRate(0.0);
			multiplier.setSortOrder(1);
			multiplier.setTicketPrefix("No");
			multiplier.setDefaultMultiplier(false);
			MultiplierDAO.getInstance().save(multiplier);

			multiplier = new Multiplier("Half");
			multiplier.setRate(50.0);
			multiplier.setSortOrder(2);
			multiplier.setTicketPrefix("Half");
			multiplier.setDefaultMultiplier(false);
			MultiplierDAO.getInstance().save(multiplier);

			multiplier = new Multiplier("Quarter");
			multiplier.setRate(25.0);
			multiplier.setSortOrder(3);
			multiplier.setTicketPrefix("Quarter");
			multiplier.setDefaultMultiplier(false);
			MultiplierDAO.getInstance().save(multiplier);

			multiplier = new Multiplier("Extra");
			multiplier.setRate(200.0);
			multiplier.setSortOrder(4);
			multiplier.setTicketPrefix("Extra");
			multiplier.setDefaultMultiplier(false);
			MultiplierDAO.getInstance().save(multiplier);

			multiplier = new Multiplier("Triple");
			multiplier.setRate(300.0);
			multiplier.setSortOrder(5);
			multiplier.setTicketPrefix("Triple");
			multiplier.setDefaultMultiplier(false);
			MultiplierDAO.getInstance().save(multiplier);

			multiplier = new Multiplier("Sub");
			multiplier.setRate(100.0);
			multiplier.setSortOrder(6);
			multiplier.setTicketPrefix("Sub");
			multiplier.setDefaultMultiplier(false);
			MultiplierDAO.getInstance().save(multiplier);

			if (!exportSampleData) {
				return true;
			}

			DataImportAction.importMenuItems(DatabaseUtil.class.getResourceAsStream("/floreantpos-menu-items.xml"));

			return true;
		} catch (Exception e) {
			PosLog.error(DatabaseUtil.class, e.getMessage());
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
			PosLog.error(DatabaseUtil.class, e.getMessage());
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
