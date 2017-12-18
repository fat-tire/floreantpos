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
package com.floreantpos.model.dao;

import java.sql.Connection;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import com.floreantpos.Database;
import com.floreantpos.config.AppConfig;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.CashDrawer;
import com.floreantpos.model.CashDrawerResetHistory;
import com.floreantpos.model.CookingInstruction;
import com.floreantpos.model.Currency;
import com.floreantpos.model.CurrencyBalance;
import com.floreantpos.model.CustomPayment;
import com.floreantpos.model.Customer;
import com.floreantpos.model.DataUpdateInfo;
import com.floreantpos.model.DeliveryAddress;
import com.floreantpos.model.DeliveryCharge;
import com.floreantpos.model.DeliveryConfiguration;
import com.floreantpos.model.DeliveryInstruction;
import com.floreantpos.model.Discount;
import com.floreantpos.model.DrawerAssignedHistory;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.EmployeeInOutHistory;
import com.floreantpos.model.GlobalConfig;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.GuestCheckPrint;
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
import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.ModifierGroup;
import com.floreantpos.model.ModifierMultiplierPrice;
import com.floreantpos.model.Multiplier;
import com.floreantpos.model.PackagingUnit;
import com.floreantpos.model.PayoutReason;
import com.floreantpos.model.PayoutRecepient;
import com.floreantpos.model.PizzaCrust;
import com.floreantpos.model.PizzaModifierPrice;
import com.floreantpos.model.PizzaPrice;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.PrinterConfiguration;
import com.floreantpos.model.PrinterGroup;
import com.floreantpos.model.PurchaseOrder;
import com.floreantpos.model.Recepie;
import com.floreantpos.model.RecepieItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Shift;
import com.floreantpos.model.ShopFloor;
import com.floreantpos.model.ShopFloorTemplate;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.ShopTableStatus;
import com.floreantpos.model.ShopTableType;
import com.floreantpos.model.TableBookingInfo;
import com.floreantpos.model.Tax;
import com.floreantpos.model.TaxGroup;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.TerminalPrinters;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketDiscount;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemDiscount;
import com.floreantpos.model.TicketItemModifier;
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
		Configuration configuration = new Configuration();
		configuration.addClass(ActionHistory.class);
		configuration.addClass(AttendenceHistory.class);
		configuration.addClass(CashDrawerResetHistory.class);
		configuration.addClass(CookingInstruction.class);
		configuration.addClass(Discount.class);
		configuration.addClass(Gratuity.class);
		configuration.addClass(MenuCategory.class);
		configuration.addClass(MenuGroup.class);
		configuration.addClass(MenuItem.class);
		configuration.addClass(MenuItemModifierGroup.class);
		configuration.addClass(MenuItemShift.class);
		configuration.addClass(MenuModifier.class);
		configuration.addClass(ModifierGroup.class);
		configuration.addClass(PayoutReason.class);
		configuration.addClass(PayoutRecepient.class);
		configuration.addClass(Restaurant.class);
		configuration.addClass(Shift.class);
		configuration.addClass(Tax.class);
		configuration.addClass(Terminal.class);
		configuration.addClass(Ticket.class);
		configuration.addClass(KitchenTicket.class);
		configuration.addClass(TicketDiscount.class);
		configuration.addClass(TicketItem.class);
		configuration.addClass(TicketItemModifier.class);
		//configuration.addClass(TicketItemModifierGroup.class);
		configuration.addClass(TicketItemDiscount.class);
		configuration.addClass(KitchenTicketItem.class);
		configuration.addClass(PosTransaction.class);
		configuration.addClass(User.class);
		configuration.addClass(VirtualPrinter.class);
		configuration.addClass(TerminalPrinters.class);
		configuration.addClass(VoidReason.class);
		configuration.addClass(DrawerPullReport.class);
		configuration.addClass(PrinterConfiguration.class);
		configuration.addClass(UserPermission.class);
		configuration.addClass(UserType.class);
		configuration.addClass(Customer.class);
		configuration.addClass(PurchaseOrder.class);
		configuration.addClass(ZipCodeVsDeliveryCharge.class);
		configuration.addClass(ShopFloor.class);
		configuration.addClass(ShopFloorTemplate.class);
		configuration.addClass(ShopTable.class);
		configuration.addClass(ShopTableStatus.class);
		configuration.addClass(ShopTableType.class);
		configuration.addClass(PrinterGroup.class);
		configuration.addClass(DrawerAssignedHistory.class);
		configuration.addClass(DataUpdateInfo.class);
		configuration.addClass(TableBookingInfo.class);
		configuration.addClass(CustomPayment.class);
		configuration.addClass(com.floreantpos.model.OrderType.class);
		configuration.addClass(DeliveryAddress.class);
		configuration.addClass(DeliveryInstruction.class);
		configuration.addClass(DeliveryCharge.class);
		configuration.addClass(DeliveryConfiguration.class);
		configuration.addClass(EmployeeInOutHistory.class);
		configuration.addClass(Currency.class);
		configuration.addClass(CashDrawer.class);
		configuration.addClass(CurrencyBalance.class);
		configuration.addClass(GlobalConfig.class);
		configuration.addClass(MenuItemSize.class);
		configuration.addClass(PizzaCrust.class);
		configuration.addClass(PizzaPrice.class);
		configuration.addClass(PizzaModifierPrice.class);
		configuration.addClass(Multiplier.class);
		configuration.addClass(ModifierMultiplierPrice.class);
		configuration.addClass(TaxGroup.class);
		configuration.addClass(GuestCheckPrint.class);

		configureInventoryClasses(configuration);

		Database defaultDatabase = AppConfig.getDefaultDatabase();

		configuration.setProperty("hibernate.dialect", defaultDatabase.getHibernateDialect()); //$NON-NLS-1$
		configuration.setProperty("hibernate.connection.driver_class", defaultDatabase.getHibernateConnectionDriverClass()); //$NON-NLS-1$

		configuration.setProperty("hibernate.connection.url", AppConfig.getConnectString()); //$NON-NLS-1$
		configuration.setProperty("hibernate.connection.username", AppConfig.getDatabaseUser()); //$NON-NLS-1$
		configuration.setProperty("hibernate.connection.password", AppConfig.getDatabasePassword()); //$NON-NLS-1$
		configuration.setProperty("hibernate.hbm2ddl.auto", "update"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration.setProperty("hibernate.connection.autocommit", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration.setProperty("hibernate.max_fetch_depth", "3"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration.setProperty("hibernate.show_sql", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration.setProperty("hibernate.connection.isolation", String.valueOf(Connection.TRANSACTION_READ_COMMITTED)); //$NON-NLS-1$

		configureC3p0ConnectionPool(configuration);

		return configuration;
	}

	private static void configureC3p0ConnectionPool(Configuration configuration) {
		//min pool size
		configuration.setProperty("hibernate.c3p0.min_size", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		//max pool size
		configuration.setProperty("hibernate.c3p0.max_size", "5"); //$NON-NLS-1$ //$NON-NLS-2$
		// When an idle connection is removed from the pool (in second)
		configuration.setProperty("hibernate.c3p0.timeout", "300"); //$NON-NLS-1$ //$NON-NLS-2$
		//Number of prepared statements will be cached
		configuration.setProperty("hibernate.c3p0.max_statements", "50"); //$NON-NLS-1$ //$NON-NLS-2$
		//The number of milliseconds a client calling getConnection() will wait for a Connection to be 
		//checked-in or acquired when the pool is exhausted. Zero means wait indefinitely.
		//Setting any positive value will cause the getConnection() call to time-out and break 
		//with an SQLException after the specified number of milliseconds. 
		configuration.setProperty("hibernate.c3p0.checkoutTimeout", "10000"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration.setProperty("hibernate.c3p0.acquireRetryAttempts", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration.setProperty("hibernate.c3p0.acquireIncrement", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration.setProperty("hibernate.c3p0.maxIdleTime", "3000"); //$NON-NLS-1$ //$NON-NLS-2$
		//idle time in seconds before a connection is automatically validated
		configuration.setProperty("hibernate.c3p0.idle_test_period", "3000"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration.setProperty("hibernate.c3p0.breakAfterAcquireFailure", "false"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static Configuration configureInventoryClasses(Configuration configuration) {
		configuration.addClass(InventoryGroup.class);
		configuration.addClass(InventoryItem.class);
		configuration.addClass(InventoryLocation.class);
		configuration.addClass(InventoryMetaCode.class);
		configuration.addClass(InventoryTransaction.class);
		configuration.addClass(InventoryUnit.class);
		configuration.addClass(InventoryVendor.class);
		configuration.addClass(InventoryWarehouse.class);
		configuration.addClass(Recepie.class);
		configuration.addClass(RecepieItem.class);
		configuration.addClass(PackagingUnit.class);

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