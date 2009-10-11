package com.mdss.pos.model.dao;

import org.hibernate.cfg.Configuration;

import com.mdss.pos.config.ApplicationConfig;
import com.mdss.pos.model.ActionHistory;
import com.mdss.pos.model.AttendenceHistory;
import com.mdss.pos.model.CashDrawerResetHistory;
import com.mdss.pos.model.CookingInstruction;
import com.mdss.pos.model.CouponAndDiscount;
import com.mdss.pos.model.DrawerPullReport;
import com.mdss.pos.model.Gratuity;
import com.mdss.pos.model.MenuCategory;
import com.mdss.pos.model.MenuGroup;
import com.mdss.pos.model.MenuItem;
import com.mdss.pos.model.MenuItemModifierGroup;
import com.mdss.pos.model.MenuItemShift;
import com.mdss.pos.model.MenuModifier;
import com.mdss.pos.model.MenuModifierGroup;
import com.mdss.pos.model.PayoutReason;
import com.mdss.pos.model.PayoutRecepient;
import com.mdss.pos.model.PosTransaction;
import com.mdss.pos.model.PrinterConfiguration;
import com.mdss.pos.model.Restaurant;
import com.mdss.pos.model.RestaurantTable;
import com.mdss.pos.model.Shift;
import com.mdss.pos.model.Tax;
import com.mdss.pos.model.Terminal;
import com.mdss.pos.model.Ticket;
import com.mdss.pos.model.TicketCouponAndDiscount;
import com.mdss.pos.model.TicketItem;
import com.mdss.pos.model.TicketItemModifier;
import com.mdss.pos.model.TicketItemModifierGroup;
import com.mdss.pos.model.User;
import com.mdss.pos.model.UserPermission;
import com.mdss.pos.model.UserType;
import com.mdss.pos.model.VoidReason;





public abstract class _RootDAO extends com.mdss.pos.model.dao._BaseRootDAO {

	static {
		initialize();
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
		configuration = configuration.setProperty("hibernate.connection.url", ApplicationConfig.getConnectionURL());
		configuration = configuration.setProperty("hibernate.connection.username", ApplicationConfig.getDatabaseUser());
		configuration = configuration.setProperty("hibernate.connection.password", ApplicationConfig.getDatabasePassword());
		configuration = configuration.setProperty("hibernate.hbm2ddl.auto", "create");
		configuration = configuration.setProperty("hibernate.connection.autocommit", "false");
		configuration = configuration.setProperty("hibernate.max_fetch_depth", "3");
		configuration = configuration.setProperty("hibernate.show_sql", "false");
		
		setSessionFactory(configuration.buildSessionFactory());
	}
}