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
/*
 * BackOfficeWindow.java
 *
 * Created on August 16, 2006, 12:43 PM
 */

package com.floreantpos.bo.ui;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.floreantpos.Messages;
import com.floreantpos.actions.AboutAction;
import com.floreantpos.actions.UpdateAction;
import com.floreantpos.bo.actions.AttendanceHistoryAction;
import com.floreantpos.bo.actions.CategoryExplorerAction;
import com.floreantpos.bo.actions.ConfigureRestaurantAction;
import com.floreantpos.bo.actions.CookingInstructionExplorerAction;
import com.floreantpos.bo.actions.CouponExplorerAction;
import com.floreantpos.bo.actions.CreditCardReportAction;
import com.floreantpos.bo.actions.CurrencyExplorerAction;
import com.floreantpos.bo.actions.CustomPaymentReportAction;
import com.floreantpos.bo.actions.DataExportAction;
import com.floreantpos.bo.actions.DataImportAction;
import com.floreantpos.bo.actions.DrawerPullReportExplorerAction;
import com.floreantpos.bo.actions.EmployeeAttendanceAction;
import com.floreantpos.bo.actions.GroupExplorerAction;
import com.floreantpos.bo.actions.HourlyLaborReportAction;
import com.floreantpos.bo.actions.ItemExplorerAction;
import com.floreantpos.bo.actions.JournalReportAction;
import com.floreantpos.bo.actions.KeyStatisticsSalesReportAction;
import com.floreantpos.bo.actions.LanguageSelectionAction;
import com.floreantpos.bo.actions.MenuItemSizeExplorerAction;
import com.floreantpos.bo.actions.MenuUsageReportAction;
import com.floreantpos.bo.actions.ModifierExplorerAction;
import com.floreantpos.bo.actions.ModifierGroupExplorerAction;
import com.floreantpos.bo.actions.MultiplierExplorerAction;
import com.floreantpos.bo.actions.OpenTicketSummaryReportAction;
import com.floreantpos.bo.actions.OrdersTypeExplorerAction;
import com.floreantpos.bo.actions.PayrollReportAction;
import com.floreantpos.bo.actions.PizzaCrustExplorerAction;
import com.floreantpos.bo.actions.PizzaExplorerAction;
import com.floreantpos.bo.actions.PizzaItemExplorerAction;
import com.floreantpos.bo.actions.PizzaModifierExplorerAction;
import com.floreantpos.bo.actions.SalesAnalysisReportAction;
import com.floreantpos.bo.actions.SalesBalanceReportAction;
import com.floreantpos.bo.actions.SalesDetailReportAction;
import com.floreantpos.bo.actions.SalesExceptionReportAction;
import com.floreantpos.bo.actions.SalesReportAction;
import com.floreantpos.bo.actions.ServerProductivityReportAction;
import com.floreantpos.bo.actions.ShiftExplorerAction;
import com.floreantpos.bo.actions.TaxExplorerAction;
import com.floreantpos.bo.actions.TicketExplorerAction;
import com.floreantpos.bo.actions.UserExplorerAction;
import com.floreantpos.bo.actions.UserTypeExplorerAction;
import com.floreantpos.bo.actions.ViewGratuitiesAction;
import com.floreantpos.config.AppConfig;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.customPayment.CustomPaymentBrowserAction;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.FloreantPlugin;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.table.ShowTableBrowserAction;
import com.jidesoft.swing.JideTabbedPane;

/**
 *
 * @author  MShahriar
 */
public class BackOfficeWindow extends javax.swing.JFrame {

	private static final String POSY = "bwy";//$NON-NLS-1$
	private static final String POSX = "bwx";//$NON-NLS-1$
	private static final String WINDOW_HEIGHT = "bwheight";//$NON-NLS-1$
	private static final String WINDOW_WIDTH = "bwwidth";//$NON-NLS-1$
	private JMenu floorPlanMenu;
	private static BackOfficeWindow instance;
	private JMenuBar menuBar;

	/** Creates new form BackOfficeWindow */
	public BackOfficeWindow() {
		setIconImage(Application.getApplicationIcon().getImage());

		initComponents();

		createMenus();
		positionWindow();

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});

		setTitle(Application.getTitle() + "- " + com.floreantpos.POSConstants.BACK_OFFICE); //$NON-NLS-1$
		applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

		//call plugin's initBackoffice
	}

	private void positionWindow() {
		int width = AppConfig.getInt(WINDOW_WIDTH, 900); //$NON-NLS-1$
		int height = AppConfig.getInt(WINDOW_HEIGHT, 650); //$NON-NLS-1$
		setSize(width, height);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - width) >> 1;
		int y = (screenSize.height - height) >> 1;

		x = AppConfig.getInt(POSX, x); //$NON-NLS-1$
		y = AppConfig.getInt(POSY, y); //$NON-NLS-1$

		setLocation(x, y);
	}

	private void createMenus() {
		User user = Application.getCurrentUser();
		UserType newUserType = user.getType();

		Set<UserPermission> permissions = null;

		if (newUserType != null) {
			permissions = newUserType.getPermissions();
		}

		menuBar = new JMenuBar();

		if (newUserType == null) {
			createAdminMenu(menuBar);
			createExplorerMenu(menuBar);
			createReportMenu(menuBar);
			createFloorMenu(menuBar);
		}
		else {
			if (permissions != null && permissions.contains(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
				createAdminMenu(menuBar);
			}
			if (permissions != null && permissions.contains(UserPermission.VIEW_EXPLORERS)) {
				createExplorerMenu(menuBar);
			}
			if (permissions != null && permissions.contains(UserPermission.VIEW_REPORTS)) {
				createReportMenu(menuBar);
			}
		}
		createFloorMenu(menuBar);

		for (FloreantPlugin plugin : ExtensionManager.getPlugins()) {
			plugin.initBackoffice();
		}

		JMenu helpMenu = new JMenu(Messages.getString("BackOfficeWindow.0")); //$NON-NLS-1$
		helpMenu.add(new UpdateAction());
		helpMenu.add(new AboutAction());
		menuBar.add(helpMenu);

		setJMenuBar(menuBar);
	}

	private void createReportMenu(JMenuBar menuBar) {
		JMenu reportMenu = new JMenu(com.floreantpos.POSConstants.REPORTS);
		reportMenu.add(new SalesReportAction());
		reportMenu.add(new OpenTicketSummaryReportAction());
		reportMenu.add(new HourlyLaborReportAction());
		reportMenu.add(new PayrollReportAction());
		reportMenu.add(new EmployeeAttendanceAction());
		reportMenu.add(new KeyStatisticsSalesReportAction());
		reportMenu.add(new SalesAnalysisReportAction());
		reportMenu.add(new CreditCardReportAction());
		reportMenu.add(new CustomPaymentReportAction());
		reportMenu.add(new MenuUsageReportAction());
		reportMenu.add(new ServerProductivityReportAction());
		reportMenu.add(new JournalReportAction());
		reportMenu.add(new SalesBalanceReportAction());
		reportMenu.add(new SalesExceptionReportAction());
		reportMenu.add(new SalesDetailReportAction());
		//reportMenu.add(new PurchaseReportAction());
		//reportMenu.add(new InventoryOnHandReportAction());
		menuBar.add(reportMenu);
	}

	private void createExplorerMenu(JMenuBar menuBar) {
		JMenu explorerMenu = new JMenu(com.floreantpos.POSConstants.EXPLORERS);
		menuBar.add(explorerMenu);
		JMenu subMenuPizza = new JMenu(Messages.getString("BackOfficeWindow.1")); //$NON-NLS-1$

		if (TerminalConfig.isMultipleOrderSupported()) {
			explorerMenu.add(new OrdersTypeExplorerAction());
		}
		explorerMenu.add(new CategoryExplorerAction());
		explorerMenu.add(new GroupExplorerAction());
		explorerMenu.add(new ItemExplorerAction());
		explorerMenu.add(new ModifierGroupExplorerAction());
		explorerMenu.add(new ModifierExplorerAction());
		explorerMenu.add(new ShiftExplorerAction());
		explorerMenu.add(new CouponExplorerAction());
		explorerMenu.add(new CookingInstructionExplorerAction());
		explorerMenu.add(new TaxExplorerAction());
		explorerMenu.add(new CustomPaymentBrowserAction());
		explorerMenu.add(new DrawerPullReportExplorerAction());
		explorerMenu.add(new TicketExplorerAction());
		explorerMenu.add(new AttendanceHistoryAction());
		explorerMenu.add(new PizzaExplorerAction());
		//explorerMenu.add(subMenuPizza);

		subMenuPizza.add(new MenuItemSizeExplorerAction());
		subMenuPizza.add(new PizzaCrustExplorerAction());
		subMenuPizza.add(new PizzaItemExplorerAction());
		subMenuPizza.add(new PizzaModifierExplorerAction());
		explorerMenu.add(new MultiplierExplorerAction());

		OrderServiceExtension plugin = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);
		if (plugin == null) {
			return;
		}

		plugin.createCustomerMenu(explorerMenu);
	}

	private void createAdminMenu(JMenuBar menuBar) {
		JMenu adminMenu = new JMenu(com.floreantpos.POSConstants.ADMIN);
		adminMenu.add(new ConfigureRestaurantAction());
		adminMenu.add(new CurrencyExplorerAction());
		adminMenu.add(new UserExplorerAction());
		adminMenu.add(new UserTypeExplorerAction());
		adminMenu.add(new ViewGratuitiesAction());
		adminMenu.add(new DataExportAction());
		adminMenu.add(new DataImportAction());
		adminMenu.add(new LanguageSelectionAction());
		menuBar.add(adminMenu);
	}

	private void createFloorMenu(JMenuBar menuBar) {

		floorPlanMenu = new JMenu(Messages.getString("BackOfficeWindow.2")); //$NON-NLS-1$
		floorPlanMenu.add(new ShowTableBrowserAction());

		menuBar.add(floorPlanMenu);

	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() {
		jPanel1 = new javax.swing.JPanel();
		tabbedPane = new JideTabbedPane();
		tabbedPane.setTabShape(JideTabbedPane.SHAPE_WINDOWS);
		tabbedPane.setShowCloseButtonOnTab(true);
		tabbedPane.setTabInsets(new Insets(5, 5, 5, 5));
		Font font = new Font(tabbedPane.getFont().getName(), Font.PLAIN, PosUIManager.getDefaultFontSize());
		tabbedPane.setFont(font);

		getContentPane().setLayout(new java.awt.BorderLayout(5, 0));

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jPanel1.setLayout(new java.awt.BorderLayout(5, 0));

		jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		jPanel1.add(tabbedPane, java.awt.BorderLayout.CENTER);

		getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new BackOfficeWindow().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private JideTabbedPane tabbedPane;

	// End of variables declaration//GEN-END:variables

	public javax.swing.JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	private void saveSizeAndLocation() {
		AppConfig.putInt(WINDOW_WIDTH, BackOfficeWindow.this.getWidth());
		AppConfig.putInt(WINDOW_HEIGHT, BackOfficeWindow.this.getHeight()); //$NON-NLS-1$
		AppConfig.putInt(POSX, BackOfficeWindow.this.getX()); //$NON-NLS-1$
		AppConfig.putInt(POSY, BackOfficeWindow.this.getY()); //$NON-NLS-1$
	}

	public void close() {
		saveSizeAndLocation();
		//		instance = null;
		dispose();
	}

	public static BackOfficeWindow getInstance() {
		if (instance == null) {
			instance = new BackOfficeWindow();
		}
		return instance;
	}

	public JMenuBar getBackOfficeMenuBar() {
		return menuBar;
	}

	/**
	 * @return the floorPlanMenu
	 */
	public JMenu getFloorPlanMenu() {
		return floorPlanMenu;
	}

	/**
	 * @param floorPlanMenu the floorPlanMenu to set
	 */
	public void setFloorPlanMenu(JMenu floorPlanMenu) {
		this.floorPlanMenu = floorPlanMenu;
	}

	//	public static BackOfficeWindow getInstance() {
	//		if (instance == null) {
	//			instance = new BackOfficeWindow();
	//			Application.getInstance().setBackOfficeWindow(instance);
	//		}
	//
	//		return instance;
	//	}

}
