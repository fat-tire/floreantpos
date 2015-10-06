package com.floreantpos.ui.views;

import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.demo.KitchenDisplayWindow;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.extension.TicketImportPlugin;
import com.floreantpos.main.Application;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.ManagerDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.PayoutDialog;
import com.floreantpos.ui.dialog.TicketAuthorizationDialog;

public class SwitchboardOtherFunctionsDialog extends POSDialog implements ActionListener {
	private SwitchboardView switchboardView;
	
	private PosButton btnManager = new PosButton(POSConstants.MANAGER_BUTTON_TEXT);
	private PosButton btnAuthorize = new PosButton(POSConstants.AUTHORIZE_BUTTON_TEXT);
	private PosButton btnKitchenDisplay = new PosButton(POSConstants.KITCHEN_DISPLAY_BUTTON_TEXT);
	private PosButton btnPayout = new PosButton(POSConstants.PAYOUT_BUTTON_TEXT);
	private PosButton btnTableManage = new PosButton(POSConstants.TABLE_MANAGE_BUTTON_TEXT);
	private PosButton btnOnlineTickets = new PosButton(POSConstants.ONLINE_TICKET_BUTTON_TEXT);
	
	private FloorLayoutPlugin floorLayoutPlugin;
	private TicketImportPlugin ticketImportPlugin;
	
	public SwitchboardOtherFunctionsDialog(SwitchboardView switchboardView) {
		super();
		this.switchboardView = switchboardView;
		
		setTitle(Messages.getString("SwitchboardOtherFunctionsDialog.0")); //$NON-NLS-1$
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(800, 400);
		
		JPanel contentPane = new JPanel(new GridLayout(2, 0, 10, 10));
		contentPane.add(btnManager);
		contentPane.add(btnAuthorize);
		contentPane.add(btnKitchenDisplay);
		contentPane.add(btnPayout);
		
		floorLayoutPlugin = Application.getPluginManager().getPlugin(FloorLayoutPlugin.class);
		if (floorLayoutPlugin != null) {
			contentPane.add(btnTableManage);
		}

		ticketImportPlugin = Application.getPluginManager().getPlugin(TicketImportPlugin.class);
		if (ticketImportPlugin != null) {
			contentPane.add(btnOnlineTickets);
		}
		
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		
		btnManager.addActionListener(this);
		btnAuthorize.addActionListener(this);
		btnKitchenDisplay.addActionListener(this);
		btnPayout.addActionListener(this);
		btnTableManage.addActionListener(this);
		btnOnlineTickets.addActionListener(this);
		
		setupPermission();
	}

	private void setupPermission() {
		User user = Application.getCurrentUser();
		UserType userType = user.getType();
		if (userType != null) {
			Set<UserPermission> permissions = userType.getPermissions();
			if (permissions != null) {
				for (UserPermission permission : permissions) {
					if (permission.equals(UserPermission.PAY_OUT)) {
						btnPayout.setEnabled(true);
					}
					else if (permission.equals(UserPermission.PERFORM_MANAGER_TASK)) {
						btnManager.setEnabled(true);
					}
				}
			}
		}
	}
	
	private void doShowKitchenDisplay() {
		Window[] windows = Window.getWindows();
		for (Window window : windows) {
			if(window instanceof KitchenDisplayWindow) {
				window.toFront();
				return;
			}
		}
		
		KitchenDisplayWindow window = new KitchenDisplayWindow();
		window.setVisible(true);
	}
	
	private void doShowTicketImportDialog() {
		TicketImportPlugin ticketImportPlugin = Application.getPluginManager().getPlugin(TicketImportPlugin.class);
		if(ticketImportPlugin != null) {
			ticketImportPlugin.startService();
		}
	}
	
	private void doShowAuthorizeTicketDialog() {
		TicketAuthorizationDialog dialog = new TicketAuthorizationDialog(Application.getPosWindow());
    	dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    	dialog.setSize(800, 600);
    	dialog.setLocationRelativeTo(Application.getPosWindow());
    	dialog.setVisible(true);
	}
	
	private void doShowManagerWindow() {
		ManagerDialog dialog = new ManagerDialog();
		dialog.open();

		switchboardView.updateTicketList();
	}
	
	private void doPayout() {
		PayoutDialog dialog = new PayoutDialog();
		dialog.open();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		private PosButton btnBackOffice = new PosButton(POSConstants.BACK_OFFICE_BUTTON_TEXT);
//		private PosButton btnManager = new PosButton(POSConstants.MANAGER_BUTTON_TEXT);
//		private PosButton btnAuthorize = new PosButton(POSConstants.AUTHORIZE_BUTTON_TEXT);
//		private PosButton btnKitchenDisplay = new PosButton(POSConstants.KITCHEN_DISPLAY_BUTTON_TEXT);
//		private PosButton btnPayout = new PosButton(POSConstants.PAYOUT_BUTTON_TEXT);
//		private PosButton btnTableManage = new PosButton(POSConstants.TABLE_MANAGE_BUTTON_TEXT);
//		private PosButton btnOnlineTickets = new PosButton(POSConstants.ONLINE_TICKET_BUTTON_TEXT);
		
		Object source = e.getSource();
		dispose();
		
		if(source == btnManager) {
			doShowManagerWindow();
		}
		else if(source == btnAuthorize) {
			doShowAuthorizeTicketDialog();
		}
		else if(source == btnKitchenDisplay) {
			doShowKitchenDisplay();
		}
		else if(source == btnPayout) {
			doPayout();
		}
		else if(source == btnTableManage) {
			if(floorLayoutPlugin != null) {
				floorLayoutPlugin.openTicketsAndTablesDisplay();
			}
		}
		else if(source == btnOnlineTickets) {
			doShowTicketImportDialog();
		}
	}
}
