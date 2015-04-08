/*
 * BackOfficeWindow.java
 *
 * Created on August 16, 2006, 12:43 PM
 */

package com.floreantpos.bo.ui;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.floreantpos.actions.AboutAction;
import com.floreantpos.bo.actions.CategoryExplorerAction;
import com.floreantpos.bo.actions.ConfigureRestaurantAction;
import com.floreantpos.bo.actions.CookingInstructionExplorerAction;
import com.floreantpos.bo.actions.CouponExplorerAction;
import com.floreantpos.bo.actions.CreditCardReportAction;
import com.floreantpos.bo.actions.DataExportAction;
import com.floreantpos.bo.actions.DataImportAction;
import com.floreantpos.bo.actions.DrawerPullReportExplorerAction;
import com.floreantpos.bo.actions.GroupExplorerAction;
import com.floreantpos.bo.actions.HourlyLaborReportAction;
import com.floreantpos.bo.actions.ItemExplorerAction;
import com.floreantpos.bo.actions.JournalReportAction;
import com.floreantpos.bo.actions.KeyStatisticsSalesReportAction;
import com.floreantpos.bo.actions.MenuUsageReportAction;
import com.floreantpos.bo.actions.ModifierExplorerAction;
import com.floreantpos.bo.actions.ModifierGroupExplorerAction;
import com.floreantpos.bo.actions.OpenTicketSummaryReportAction;
import com.floreantpos.bo.actions.PayrollReportAction;
import com.floreantpos.bo.actions.SalesAnalysisReportAction;
import com.floreantpos.bo.actions.SalesBalanceReportAction;
import com.floreantpos.bo.actions.SalesDetailReportAction;
import com.floreantpos.bo.actions.SalesExceptionReportAction;
import com.floreantpos.bo.actions.SalesReportAction;
import com.floreantpos.bo.actions.ServerProductivityReportAction;
import com.floreantpos.bo.actions.ShiftExplorerAction;
import com.floreantpos.bo.actions.TaxExplorerAction;
import com.floreantpos.bo.actions.UserExplorerAction;
import com.floreantpos.bo.actions.UserTypeExplorerAction;
import com.floreantpos.bo.actions.ViewGratuitiesAction;
import com.floreantpos.config.AppConfig;
import com.floreantpos.extension.InventoryPlugin;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
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

		JMenuBar menuBar = new JMenuBar();

		if (newUserType == null) {
			createAdminMenu(menuBar);
			createExplorerMenu(menuBar);
			createReportMenu(menuBar);
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

		createInventoryMenus(menuBar);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(new AboutAction());
		menuBar.add(helpMenu);

		setJMenuBar(menuBar);
	}

	private void createInventoryMenus(JMenuBar menuBar) {
		InventoryPlugin plugin = Application.getPluginManager().getPlugin(InventoryPlugin.class);
		if (plugin == null) {
			return;
		}

		AbstractAction[] actions = plugin.getActions();
		if (actions == null) {
			return;
		}

		JMenu inventoryMenu = new JMenu("Inventory");
		for (AbstractAction abstractAction : actions) {
			inventoryMenu.add(abstractAction);
		}

		menuBar.add(inventoryMenu);
	}

	private void createReportMenu(JMenuBar menuBar) {
		JMenu reportMenu = new JMenu(com.floreantpos.POSConstants.REPORTS);
		reportMenu.add(new SalesReportAction());
		reportMenu.add(new OpenTicketSummaryReportAction());
		reportMenu.add(new HourlyLaborReportAction());
		reportMenu.add(new PayrollReportAction());
		reportMenu.add(new KeyStatisticsSalesReportAction());
		reportMenu.add(new SalesAnalysisReportAction());
		reportMenu.add(new CreditCardReportAction());
		reportMenu.add(new MenuUsageReportAction());
		reportMenu.add(new ServerProductivityReportAction());
		reportMenu.add(new JournalReportAction());
		reportMenu.add(new SalesBalanceReportAction());
		reportMenu.add(new SalesExceptionReportAction());
		reportMenu.add(new SalesDetailReportAction());
		menuBar.add(reportMenu);
	}

	private void createExplorerMenu(JMenuBar menuBar) {
		JMenu explorerMenu = new JMenu(com.floreantpos.POSConstants.EXPLORERS);
		menuBar.add(explorerMenu);
		
		explorerMenu.add(new CategoryExplorerAction());
		explorerMenu.add(new GroupExplorerAction());
		explorerMenu.add(new ItemExplorerAction());
		explorerMenu.add(new ModifierGroupExplorerAction());
		explorerMenu.add(new ModifierExplorerAction());
		explorerMenu.add(new ShiftExplorerAction());
		explorerMenu.add(new CouponExplorerAction());
		explorerMenu.add(new CookingInstructionExplorerAction());
		explorerMenu.add(new TaxExplorerAction());
		
		OrderServiceExtension plugin = Application.getPluginManager().getPlugin(OrderServiceExtension.class);
		if (plugin == null) {
			return;
		}
		
		plugin.createCustomerMenu(explorerMenu);
	}

	private void createAdminMenu(JMenuBar menuBar) {
		JMenu adminMenu = new JMenu(com.floreantpos.POSConstants.ADMIN);
		adminMenu.add(new ConfigureRestaurantAction());
		adminMenu.add(new UserExplorerAction());
		adminMenu.add(new UserTypeExplorerAction());
		adminMenu.add(new ViewGratuitiesAction());
		adminMenu.add(new DrawerPullReportExplorerAction());
		adminMenu.add(new DataExportAction());
		adminMenu.add(new DataImportAction());
		menuBar.add(adminMenu);
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

//	public static BackOfficeWindow getInstance() {
//		if (instance == null) {
//			instance = new BackOfficeWindow();
//			Application.getInstance().setBackOfficeWindow(instance);
//		}
//
//		return instance;
//	}

}
