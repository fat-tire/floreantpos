package com.floreantpos.model.dao;

import java.sql.Connection;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import com.floreantpos.Database;
import com.floreantpos.config.AppConfig;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.CashDrawerResetHistory;
import com.floreantpos.model.CookingInstruction;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.Customer;
import com.floreantpos.model.DataUpdateInfo;
import com.floreantpos.model.DrawerAssignedHistory;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.InventoryGroup;
import com.floreantpos.model.InventoryItem;
import com.floreantpos.model.InventoryLocation;
import com.floreantpos.model.InventoryMetaCode;
import com.floreantpos.model.InventoryTransaction;
import com.floreantpos.model.InventoryUnit;
import com.floreantpos.model.InventoryVendor;
import com.floreantpos.model.InventoryWarehouse;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.KitchenTicketItem;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuItemShift;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.OrderTypeProperties;
import com.floreantpos.model.PackagingUnit;
import com.floreantpos.model.PayoutReason;
import com.floreantpos.model.PayoutRecepient;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.PrinterConfiguration;
import com.floreantpos.model.PrinterGroup;
import com.floreantpos.model.PurchaseOrder;
import com.floreantpos.model.Recepie;
import com.floreantpos.model.RecepieItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Shift;
import com.floreantpos.model.ShopFloor;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.ShopTableType;
import com.floreantpos.model.TableBookingInfo;
import com.floreantpos.model.Tax;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.model.VoidReason;
import com.floreantpos.model.ZipCodeVsDeliveryCharge;

public abstract class _RootDAO extends com.floreantpos.model.dao._BaseRootDAO {

	/*
	 * If you are using lazy loading, uncomment this Somewhere, you should call
	 * RootDAO.closeCurrentThreadSessions(); public void closeSession (Session
	 * session) { // do nothing here because the session will be closed later }
	 */

	/*
	 * If you are pulling the SessionFactory from a JNDI tree, uncomment this
	 * protected SessionFactory getSessionFactory(String configFile) { // If you
	 * have a single session factory, ignore the configFile parameter //
	 * Otherwise, you can set a meta attribute under the class node called
	 * "config-file" which // will be passed in here so you can tell what
	 * session factory an individual mapping file // belongs to return
	 * (SessionFactory) new
	 * InitialContext().lookup("java:/{SessionFactoryName}"); }
	 */
	
	public static void initialize(String configFileName, Configuration configuration) {
		com.floreantpos.model.dao._RootDAO.setSessionFactory(configuration.buildSessionFactory());
	}

	public static Configuration getNewConfiguration(String configFileName) {
		AnnotationConfiguration configuration = new AnnotationConfiguration();
		configuration = configuration.addClass(ActionHistory.class);
		configuration = configuration.addClass(AttendenceHistory.class);
		configuration = configuration.addClass(CashDrawerResetHistory.class);
		configuration = configuration.addClass(CookingInstruction.class);
		configuration = configuration.addClass(CouponAndDiscount.class);
		configuration = configuration.addClass(Gratuity.class);
		configuration = configuration.addClass(MenuCategory.class);
		configuration = configuration.addClass(MenuGroup.class);
		configuration = configuration.addClass(MenuItem.class);
		configuration = configuration.addClass(MenuItemModifierGroup.class);
		configuration = configuration.addClass(MenuItemShift.class);
		configuration = configuration.addClass(MenuModifier.class);
		configuration = configuration.addClass(MenuModifierGroup.class);
		configuration = configuration.addClass(PayoutReason.class);
		configuration = configuration.addClass(PayoutRecepient.class);
		configuration = configuration.addClass(Restaurant.class);
		configuration = configuration.addClass(Shift.class);
		configuration = configuration.addClass(Tax.class);
		configuration = configuration.addClass(Terminal.class);
		configuration = configuration.addClass(Ticket.class);
		configuration = configuration.addClass(KitchenTicket.class);
		configuration = configuration.addClass(TicketCouponAndDiscount.class);
		configuration = configuration.addClass(TicketItem.class);
		configuration = configuration.addClass(TicketItemModifier.class);
		configuration = configuration.addClass(TicketItemModifierGroup.class);
		configuration = configuration.addClass(KitchenTicketItem.class);
		configuration = configuration.addClass(PosTransaction.class);
		configuration = configuration.addClass(User.class);
		configuration = configuration.addClass(VirtualPrinter.class);
		configuration = configuration.addClass(VoidReason.class);
		configuration = configuration.addClass(DrawerPullReport.class);
		configuration = configuration.addClass(PrinterConfiguration.class);
		configuration = configuration.addClass(UserPermission.class);
		configuration = configuration.addClass(UserType.class);
		configuration = configuration.addClass(Customer.class);
		configuration = configuration.addClass(PurchaseOrder.class);
		configuration = configuration.addClass(ZipCodeVsDeliveryCharge.class);
		configuration = configuration.addClass(ShopFloor.class);
		configuration = configuration.addClass(ShopTable.class);
		configuration = configuration.addClass(ShopTableType.class);
		configuration = configuration.addClass(OrderTypeProperties.class);
		configuration = configuration.addClass(PrinterGroup.class);
		configuration = configuration.addClass(DrawerAssignedHistory.class);
		configuration = configuration.addClass(DataUpdateInfo.class);
		configuration = configuration.addClass(TableBookingInfo.class);
		
		configuration = configureInventoryClasses(configuration);
		
		Database defaultDatabase = AppConfig.getDefaultDatabase();

		configuration = configuration.setProperty("hibernate.dialect", defaultDatabase.getHibernateDialect()); //$NON-NLS-1$
		configuration = configuration.setProperty("hibernate.connection.driver_class", defaultDatabase.getHibernateConnectionDriverClass()); //$NON-NLS-1$
		
		configuration = configuration.setProperty("hibernate.connection.url", AppConfig.getConnectString()); //$NON-NLS-1$
		configuration = configuration.setProperty("hibernate.connection.username", AppConfig.getDatabaseUser()); //$NON-NLS-1$
		configuration = configuration.setProperty("hibernate.connection.password", AppConfig.getDatabasePassword()); //$NON-NLS-1$
		configuration = configuration.setProperty("hibernate.hbm2ddl.auto", "update"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration = configuration.setProperty("hibernate.connection.autocommit", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration = configuration.setProperty("hibernate.max_fetch_depth", "3"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration = configuration.setProperty("hibernate.show_sql", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration = configuration.setProperty("hibernate.connection.isolation", String.valueOf(Connection.TRANSACTION_READ_COMMITTED)); //$NON-NLS-1$

		configuration = configuration.setProperty("hibernate.c3p0.min_size", "2"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration = configuration.setProperty("hibernate.c3p0.max_size", "10"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration = configuration.setProperty("hibernate.c3p0.timeout", "300"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration = configuration.setProperty("hibernate.c3p0.acquireRetryAttempts", "3"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration = configuration.setProperty("hibernate.c3p0.max_statements", "50"); //$NON-NLS-1$ //$NON-NLS-2$
		//configuration = configuration.setProperty("hibernate.c3p0.idle_test_period", "3000");
		configuration = configuration.setProperty("hibernate.c3p0.validate", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration = configuration.setProperty("hibernate.c3p0.breakAfterAcquireFailure", "true"); //$NON-NLS-1$ //$NON-NLS-2$

		return configuration;
	}
	
	private static AnnotationConfiguration configureInventoryClasses(AnnotationConfiguration configuration) {
		configuration = configuration.addClass(InventoryGroup.class);
		configuration = configuration.addClass(InventoryItem.class);
		configuration = configuration.addClass(InventoryLocation.class);
		configuration = configuration.addClass(InventoryMetaCode.class);
		configuration = configuration.addClass(InventoryTransaction.class);
		configuration = configuration.addClass(InventoryUnit.class);
		configuration = configuration.addClass(InventoryVendor.class);
		configuration = configuration.addClass(InventoryWarehouse.class);
		configuration = configuration.addClass(Recepie.class);
		configuration = configuration.addClass(RecepieItem.class);
		configuration = configuration.addClass(PackagingUnit.class);
		
		return configuration;
	}
	
	public static Configuration reInitialize() {
		Configuration configuration = getNewConfiguration(null);
		com.floreantpos.model.dao._RootDAO.setSessionFactory(configuration.buildSessionFactory());
		
		return configuration;
	}
	
	public void refresh(Object obj) {
		Session session = createNewSession();
		super.refresh(obj, session);
		session.close();
	}
}