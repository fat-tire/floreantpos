package com.floreantpos.model.dao;

import org.hibernate.cfg.Configuration;

import com.floreantpos.config.ApplicationConfig;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.CashDrawerResetHistory;
import com.floreantpos.model.CookingInstruction;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuItemShift;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.PayoutReason;
import com.floreantpos.model.PayoutRecepient;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.PrinterConfiguration;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.RestaurantTable;
import com.floreantpos.model.Shift;
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
import com.floreantpos.model.VoidReason;





public abstract class _RootDAO extends com.floreantpos.model.dao._BaseRootDAO {

	static {
		//initialize();
	}
/*
	If you are using lazy loading, uncomment this
	Somewhere, you should call RootDAO.closeCurrentThreadSessions();
	public void closeSession (Session session) {
		// do nothing here because the session will be closed later
	}
*/

/*
	If you are pulling the SessionFactory from a JNDI tree, uncomment this
	protected SessionFactory getSessionFactory(String configFile) {
		// If you have a single session factory, ignore the configFile parameter
		// Otherwise, you can set a meta attribute under the class node called "config-file" which
		// will be passed in here so you can tell what session factory an individual mapping file
		// belongs to
		return (SessionFactory) new InitialContext().lookup("java:/{SessionFactoryName}");
	}
*/
	
	public static void initialize() {
		Configuration configuration = new Configuration();
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
		configuration = configuration.addClass(RestaurantTable.class);
		configuration = configuration.addClass(Shift.class);
		configuration = configuration.addClass(Tax.class);
		configuration = configuration.addClass(Terminal.class);
		configuration = configuration.addClass(Ticket.class);
		configuration = configuration.addClass(TicketCouponAndDiscount.class);
		configuration = configuration.addClass(TicketItem.class);
		configuration = configuration.addClass(TicketItemModifier.class);
		configuration = configuration.addClass(TicketItemModifierGroup.class);
		configuration = configuration.addClass(PosTransaction.class);
		configuration = configuration.addClass(User.class);
		configuration = configuration.addClass(VoidReason.class);
		configuration = configuration.addClass(DrawerPullReport.class);
		configuration = configuration.addClass(PrinterConfiguration.class);
		configuration = configuration.addClass(UserPermission.class);
		configuration = configuration.addClass(UserType.class);
		
		configuration = configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
		configuration = configuration.setProperty("hibernate.connection.driver_class", "org.apache.derby.jdbc.ClientDriver");
		configuration = configuration.setProperty("hibernate.connection.url", ApplicationConfig.getConnectionURL());
		configuration = configuration.setProperty("hibernate.connection.username", ApplicationConfig.getDatabaseUser());
		configuration = configuration.setProperty("hibernate.connection.password", ApplicationConfig.getDatabasePassword());
		configuration = configuration.setProperty("hibernate.hbm2ddl.auto", "update");
		configuration = configuration.setProperty("hibernate.connection.autocommit", "false");
		configuration = configuration.setProperty("hibernate.max_fetch_depth", "3");
		configuration = configuration.setProperty("hibernate.show_sql", "false");
		
		setSessionFactory(configuration.buildSessionFactory());
	}
	
	public static void dbCleanerInitialize() {
		Configuration configuration = new Configuration();
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
		configuration = configuration.addClass(RestaurantTable.class);
		configuration = configuration.addClass(Shift.class);
		configuration = configuration.addClass(Tax.class);
		configuration = configuration.addClass(Terminal.class);
		configuration = configuration.addClass(Ticket.class);
		configuration = configuration.addClass(TicketCouponAndDiscount.class);
		configuration = configuration.addClass(TicketItem.class);
		configuration = configuration.addClass(TicketItemModifier.class);
		configuration = configuration.addClass(TicketItemModifierGroup.class);
		configuration = configuration.addClass(PosTransaction.class);
		configuration = configuration.addClass(User.class);
		configuration = configuration.addClass(VoidReason.class);
		configuration = configuration.addClass(DrawerPullReport.class);
		configuration = configuration.addClass(PrinterConfiguration.class);
		configuration = configuration.addClass(UserPermission.class);
		configuration = configuration.addClass(UserType.class);
		
		configuration = configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
		configuration = configuration.setProperty("hibernate.connection.driver_class", "org.apache.derby.jdbc.ClientDriver");
		configuration = configuration.setProperty("hibernate.connection.url", ApplicationConfig.getConnectionURL() + ";create=true");
		configuration = configuration.setProperty("hibernate.connection.username", ApplicationConfig.getDatabaseUser());
		configuration = configuration.setProperty("hibernate.connection.password", ApplicationConfig.getDatabasePassword());
		configuration = configuration.setProperty("hibernate.hbm2ddl.auto", "create");
		configuration = configuration.setProperty("hibernate.connection.autocommit", "false");
		configuration = configuration.setProperty("hibernate.max_fetch_depth", "3");
		configuration = configuration.setProperty("hibernate.show_sql", "false");
		
		setSessionFactory(configuration.buildSessionFactory());
	}
}