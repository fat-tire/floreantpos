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
 * LoginScreen.java
 *
 * Created on August 14, 2006, 10:57 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.apache.commons.logging.LogFactory;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.actions.ClockInOutAction;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.config.ui.DatabaseConfigurationDialog;
import com.floreantpos.demo.KitchenDisplayView;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.User;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PasswordEntryDialog;
import com.floreantpos.ui.views.order.ViewPanel;
import com.floreantpos.util.ShiftException;
import com.floreantpos.util.UserNotFoundException;

/**
 *
 * @author  MShahriar
 */
public class LoginView extends ViewPanel {
	public final static String VIEW_NAME = "LOGIN_VIEW"; //$NON-NLS-1$

	private com.floreantpos.swing.PosButton btnDineIn;
	private com.floreantpos.swing.PosButton btnTakeOut;
	private com.floreantpos.swing.PosButton btnPickUp;
	private com.floreantpos.swing.PosButton btnHomeDelivery;
	private com.floreantpos.swing.PosButton btnDriveThru;
	private com.floreantpos.swing.PosButton btnBarTab;
	private com.floreantpos.swing.PosButton btnForHere;
	private com.floreantpos.swing.PosButton btnRetail;
	private com.floreantpos.swing.PosButton btnSwitchBoard;
	private com.floreantpos.swing.PosButton btnKitchenDisplay;

	private com.floreantpos.swing.PosButton btnConfigureDatabase;
	private com.floreantpos.swing.PosButton btnShutdown;
	private com.floreantpos.swing.PosButton btnClockOUt;
	private JLabel lblTerminalId;
	private JPanel centerPanel = new JPanel(new MigLayout("wrap 4,al center center", "sg", "100")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	private static LoginView instance;
	private JPanel mainPanel;

	/** Creates new form LoginScreen */
	private LoginView() {
		setLayout(new BorderLayout(5, 5));
		JLabel titleLabel = new JLabel(IconFactory.getIcon("/ui_icons/", "title.png")); //$NON-NLS-1$ //$NON-NLS-2$
		titleLabel.setOpaque(true);
		titleLabel.setBackground(Color.WHITE);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(titleLabel, BorderLayout.CENTER);
		panel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);

		add(panel, BorderLayout.NORTH);
		add(createCenterPanel(), BorderLayout.CENTER);
	}

	private JPanel createCenterPanel() {

		lblTerminalId = new JLabel(Messages.getString("LoginView.0")); //$NON-NLS-1$
		lblTerminalId.setForeground(Color.BLACK);
		lblTerminalId.setFont(new Font(Messages.getString("LoginView.1"), Font.BOLD, 18)); //$NON-NLS-1$
		lblTerminalId.setHorizontalAlignment(SwingConstants.CENTER);

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(lblTerminalId, BorderLayout.NORTH);

		btnDineIn = new PosButton(POSConstants.DINE_IN_BUTTON_TEXT);
		btnTakeOut = new PosButton(POSConstants.TAKE_OUT_BUTTON_TEXT);
		btnPickUp = new PosButton(POSConstants.PICKUP_BUTTON_TEXT);
		btnHomeDelivery = new PosButton(POSConstants.HOME_DELIVERY_BUTTON_TEXT);

		btnBarTab = new PosButton(POSConstants.BAR_TAB_BUTTON_TEXT);
		btnBarTab.setEnabled(false);
		btnDriveThru = new PosButton(POSConstants.DRIVE_THRU_BUTTON_TEXT);
		btnForHere = new PosButton(POSConstants.FOR_HERE_BUTTON_TEXT);
		btnRetail = new PosButton(POSConstants.RETAIL_BUTTON_TEXT);

		btnSwitchBoard = new PosButton(POSConstants.SWITCHBOARD);
		btnKitchenDisplay = new PosButton(POSConstants.KITCHEN_DISPLAY_BUTTON_TEXT);

		btnShutdown = new PosButton(POSConstants.SHUTDOWN);
		btnClockOUt = new PosButton(new ClockInOutAction(false, true));

		OrderServiceExtension orderServiceExtension = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);

		if (orderServiceExtension != null) {
			centerPanel.add(btnDineIn, "grow");//$NON-NLS-1$
			centerPanel.add(btnTakeOut, "grow");//$NON-NLS-1$
			centerPanel.add(btnForHere, "grow");//$NON-NLS-1$
			centerPanel.add(btnRetail, "grow");//$NON-NLS-1$
			centerPanel.add(btnPickUp, "grow");//$NON-NLS-1$
			centerPanel.add(btnHomeDelivery, "grow");//$NON-NLS-1$
			centerPanel.add(btnDriveThru, "grow");//$NON-NLS-1$
			centerPanel.add(btnBarTab, "grow");//$NON-NLS-1$
			centerPanel.add(btnSwitchBoard, "span 2,grow"); //$NON-NLS-1$
			centerPanel.add(btnKitchenDisplay, "span 2,grow"); //$NON-NLS-1$
		}
		else {
			centerPanel.add(btnDineIn, "grow"); //$NON-NLS-1$
			centerPanel.add(btnForHere, "grow"); //$NON-NLS-1$
			centerPanel.add(btnTakeOut, "grow"); //$NON-NLS-1$
			centerPanel.add(btnRetail, "grow"); //$NON-NLS-1$
			centerPanel.add(btnSwitchBoard, "span 2,grow"); //$NON-NLS-1$
			centerPanel.add(btnKitchenDisplay, "span 2,grow"); //$NON-NLS-1$
		}

		btnConfigureDatabase = new PosButton(POSConstants.CONFIGURE_DATABASE);

		if (TerminalConfig.isFullscreenMode()) {
			if (btnConfigureDatabase != null) {
				btnConfigureDatabase.setVisible(false);
			}
			if (btnShutdown != null) {
				btnShutdown.setVisible(false);
			}
			centerPanel.add(btnClockOUt, "span 4,grow"); //$NON-NLS-1$
		}
		else {

			if (TerminalConfig.isShowDbConfigureButton()) {
				centerPanel.add(btnClockOUt, "grow"); //$NON-NLS-1$
				centerPanel.add(btnConfigureDatabase, "span 2,grow"); //$NON-NLS-1$
				centerPanel.add(btnShutdown, "grow"); //$NON-NLS-1$
			}
			else {
				centerPanel.add(btnClockOUt, "span 2,grow"); //$NON-NLS-1$
				centerPanel.add(btnShutdown, "span 2,grow"); //$NON-NLS-1$
			}
		}

		initActionHandlers();

		mainPanel.add(centerPanel, BorderLayout.CENTER);
		return mainPanel;
	}

	void initActionHandlers() {
		btnDineIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TerminalConfig.setDefaultView(OrderType.DINE_IN.toString());
				doLogin();
			}
		});

		btnTakeOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TerminalConfig.setDefaultView(OrderType.TAKE_OUT.toString());
				doLogin();
			}
		});

		btnForHere.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TerminalConfig.setDefaultView(OrderType.FOR_HERE.toString());
				doLogin();
			}
		});

		btnRetail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TerminalConfig.setDefaultView(OrderType.RETAIL.toString());
				doLogin();
			}
		});

		btnPickUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*TerminalConfig.setDefaultView(OrderType.PICKUP.toString());
				doLogin();*/
				POSMessageDialog.showMessage(Messages.getString("LoginView.19")); //$NON-NLS-1$
			}
		});

		btnHomeDelivery.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*	TerminalConfig.setDefaultView(OrderType.HOME_DELIVERY.toString());
					doLogin();*/
				POSMessageDialog.showMessage(Messages.getString("LoginView.20")); //$NON-NLS-1$
			}
		});

		btnDriveThru.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*TerminalConfig.setDefaultView(OrderType.DRIVE_THRU.toString());
				doLogin();*/
				POSMessageDialog.showMessage(Messages.getString("LoginView.18")); //$NON-NLS-1$
			}
		});

		btnBarTab.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TerminalConfig.setDefaultView(OrderType.BAR_TAB.toString());
				doLogin();
			}
		});

		btnConfigureDatabase.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DatabaseConfigurationDialog.show(Application.getPosWindow());
			}
		});

		btnKitchenDisplay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TerminalConfig.setDefaultView(KitchenDisplayView.VIEW_NAME);
				doLogin();
			}
		});

		btnShutdown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Application.getInstance().shutdownPOS();
			}
		});

		btnSwitchBoard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TerminalConfig.setDefaultView(SwitchboardView.VIEW_NAME);
				doLogin();
			}
		});
	}

	public synchronized void doLogin() {
		try {
			final User user = PasswordEntryDialog.getUser(Application.getPosWindow(),
					Messages.getString("LoginPasswordEntryView.13"), Messages.getString("LoginPasswordEntryView.14")); //$NON-NLS-1$ //$NON-NLS-2$
			if (user == null) {
				return;
			}
			Application application = Application.getInstance();
			application.doLogin(user);

		} catch (UserNotFoundException e) {
			LogFactory.getLog(Application.class).error(e);
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("LoginPasswordEntryView.15")); //$NON-NLS-1$
		} catch (ShiftException e) {
			LogFactory.getLog(Application.class).error(e);
			MessageDialog.showError(e.getMessage());
		} catch (Exception e1) {
			LogFactory.getLog(Application.class).error(e1);
			String message = e1.getMessage();

			if (message != null && message.contains("Cannot open connection")) { //$NON-NLS-1$
				MessageDialog.showError(Messages.getString("LoginPasswordEntryView.17"), e1); //$NON-NLS-1$
				DatabaseConfigurationDialog.show(Application.getPosWindow());
			}
			else {
				MessageDialog.showError(Messages.getString("LoginPasswordEntryView.18"), e1); //$NON-NLS-1$
			}
		}
	}

	public void setTerminalId(int terminalId) {
		lblTerminalId.setText(Messages.getString("LoginView.17") + terminalId); //$NON-NLS-1$
	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}

	/**
	 * @return the btnDineIn
	 */
	public com.floreantpos.swing.PosButton getBtnDineIn() {
		return btnDineIn;
	}

	/**
	 * @return the btnTakeOut
	 */
	public com.floreantpos.swing.PosButton getBtnTakeOut() {
		return btnTakeOut;
	}

	/**
	 * @return the btnPickUp
	 */
	public com.floreantpos.swing.PosButton getBtnPickUp() {
		return btnPickUp;
	}

	/**
	 * @return the btnHomeDelivery
	 */
	public com.floreantpos.swing.PosButton getBtnHomeDelivery() {
		return btnHomeDelivery;
	}

	/**
	 * @return the btnDriveThru
	 */
	public com.floreantpos.swing.PosButton getBtnDriveThru() {
		return btnDriveThru;
	}

	/**
	 * @return the btnBarTab
	 */
	public com.floreantpos.swing.PosButton getBtnBarTab() {
		return btnBarTab;
	}

	/**
	 * @return the btnForHere
	 */
	public com.floreantpos.swing.PosButton getBtnForHere() {
		return btnForHere;
	}

	/**
	 * @return the btnRetail
	 */
	public com.floreantpos.swing.PosButton getBtnRetail() {
		return btnRetail;
	}

	/**
	 * @return the btnSwitchBoard
	 */
	public com.floreantpos.swing.PosButton getBtnSwitchBoard() {
		return btnSwitchBoard;
	}

	/**
	 * @return the btnKitchenDisplay
	 */
	public com.floreantpos.swing.PosButton getBtnKitchenDisplay() {
		return btnKitchenDisplay;
	}

	/**
	 * @return the btnConfigureDatabase
	 */
	public com.floreantpos.swing.PosButton getBtnConfigureDatabase() {
		return btnConfigureDatabase;
	}

	/**
	 * @return the btnShutdown
	 */
	public com.floreantpos.swing.PosButton getBtnShutdown() {
		return btnShutdown;
	}

	/**
	 * @return the btnClockOUt
	 */
	public com.floreantpos.swing.PosButton getBtnClockOUt() {
		return btnClockOUt;
	}

	/**
	 * @return the lblTerminalId
	 */
	public JLabel getLblTerminalId() {
		return lblTerminalId;
	}

	public static LoginView getInstance() {
		if (instance == null) {
			instance = new LoginView();
		}

		return instance;
	}

	/**
	 * @return the centerPanel
	 */
	public JPanel getCenterPanel() {
		return centerPanel;
	}

	/**
	 * @return the mainPanel
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}
}