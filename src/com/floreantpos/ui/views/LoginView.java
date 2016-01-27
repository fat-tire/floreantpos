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
public class LoginView extends ViewPanel implements ActionListener {
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

	/** Creates new form LoginScreen */
	public LoginView() {
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

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(lblTerminalId, BorderLayout.NORTH);

		btnDineIn = new PosButton(POSConstants.DINE_IN_BUTTON_TEXT);
		btnDineIn.addActionListener(this);
		btnTakeOut = new PosButton(POSConstants.TAKE_OUT_BUTTON_TEXT);
		btnTakeOut.addActionListener(this);
		btnPickUp = new PosButton(POSConstants.PICKUP_BUTTON_TEXT);
		btnPickUp.addActionListener(this);
		btnHomeDelivery = new PosButton(POSConstants.HOME_DELIVERY_BUTTON_TEXT);
		btnHomeDelivery.addActionListener(this);

		btnBarTab = new PosButton(POSConstants.BAR_TAB_BUTTON_TEXT);
		btnBarTab.addActionListener(this);
		btnBarTab.setEnabled(false);
		btnDriveThru = new PosButton(POSConstants.DRIVE_THRU_BUTTON_TEXT);
		btnDriveThru.addActionListener(this);
		btnForHere = new PosButton(POSConstants.FOR_HERE_BUTTON_TEXT);
		btnForHere.addActionListener(this);
		btnRetail = new PosButton(POSConstants.RETAIL_BUTTON_TEXT);
		btnRetail.addActionListener(this);

		btnSwitchBoard = new PosButton(POSConstants.SWITCHBOARD);
		btnSwitchBoard.addActionListener(this);
		btnKitchenDisplay = new PosButton(POSConstants.KITCHEN_DISPLAY_BUTTON_TEXT);
		btnKitchenDisplay.addActionListener(this);

		btnShutdown = new PosButton(POSConstants.SHUTDOWN);
		btnShutdown.addActionListener(this);
		btnClockOUt = new PosButton(new ClockInOutAction(false, true));

		JPanel centerPanel = new JPanel(new MigLayout("hidemode 3,wrap 4,al center center", "sg", "100")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		centerPanel.add(btnDineIn, "grow"); //$NON-NLS-1$
		centerPanel.add(btnTakeOut, "grow"); //$NON-NLS-1$
		centerPanel.add(btnPickUp, "grow"); //$NON-NLS-1$
		centerPanel.add(btnHomeDelivery, "grow"); //$NON-NLS-1$
		centerPanel.add(btnBarTab, "grow");//$NON-NLS-1$
		centerPanel.add(btnDriveThru, "grow"); //$NON-NLS-1$
		centerPanel.add(btnForHere, "grow"); //$NON-NLS-1$
		centerPanel.add(btnRetail, "grow"); //$NON-NLS-1$
		centerPanel.add(btnSwitchBoard, "span 2,grow"); //$NON-NLS-1$
		centerPanel.add(btnKitchenDisplay, "span 2,grow"); //$NON-NLS-1$
		centerPanel.add(btnClockOUt, "grow"); //$NON-NLS-1$

		if (TerminalConfig.isShowDbConfigureButton()) {
			btnConfigureDatabase = new PosButton(POSConstants.CONFIGURE_DATABASE);
			btnConfigureDatabase.addActionListener(this);
			centerPanel.add(btnConfigureDatabase, "span 2,grow"); //$NON-NLS-1$
		}
		centerPanel.add(btnShutdown, "grow"); //$NON-NLS-1$

		if (TerminalConfig.isFullscreenMode()) {
			if (btnConfigureDatabase != null) {
				btnConfigureDatabase.setVisible(false);
			}
			if (btnShutdown != null) {
				btnShutdown.setVisible(false);
			}
		}

		mainPanel.add(centerPanel, BorderLayout.CENTER);
		return mainPanel;
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

	@Override
	public void actionPerformed(ActionEvent e) {

		if (POSConstants.CONFIGURE_DATABASE.equals(e.getActionCommand())) {
			DatabaseConfigurationDialog.show(Application.getPosWindow());
		}
		else if (POSConstants.SHUTDOWN.equals(e.getActionCommand())) {
			Application.getInstance().shutdownPOS();
		}
		else if (POSConstants.DINE_IN_BUTTON_TEXT.equals(e.getActionCommand())) {
			TerminalConfig.setDefaultView(OrderType.DINE_IN.toString());
			doLogin();
		}
		else if (POSConstants.TAKE_OUT_BUTTON_TEXT.equals(e.getActionCommand())) {
			TerminalConfig.setDefaultView(OrderType.TAKE_OUT.toString());
			doLogin();
		}
		else if (POSConstants.BAR_TAB_BUTTON_TEXT.equals(e.getActionCommand())) {
			TerminalConfig.setDefaultView(OrderType.BAR_TAB.toString());
			doLogin();
		}
		else if (POSConstants.DRIVE_THRU_BUTTON_TEXT.equals(e.getActionCommand())) {
			/*TerminalConfig.setDefaultView(OrderType.DRIVE_THRU.toString());
			doLogin();*/
			POSMessageDialog.showMessage(Messages.getString("LoginView.18")); //$NON-NLS-1$
		}
		else if (POSConstants.PICKUP_BUTTON_TEXT.equals(e.getActionCommand())) {
			/*TerminalConfig.setDefaultView(OrderType.PICKUP.toString());
			doLogin();*/
			POSMessageDialog.showMessage(Messages.getString("LoginView.19")); //$NON-NLS-1$
		}
		else if (POSConstants.HOME_DELIVERY_BUTTON_TEXT.equals(e.getActionCommand())) {
			/*TerminalConfig.setDefaultView(OrderType.HOME_DELIVERY.toString());
			doLogin();*/
			POSMessageDialog.showMessage(Messages.getString("LoginView.20")); //$NON-NLS-1$
		}
		else if (POSConstants.RETAIL_BUTTON_TEXT.equals(e.getActionCommand())) {
			POSMessageDialog.showMessage(Messages.getString("LoginView.21")); //$NON-NLS-1$
		}
		else if (POSConstants.FOR_HERE_BUTTON_TEXT.equals(e.getActionCommand())) {
			POSMessageDialog.showMessage(Messages.getString("LoginView.22")); //$NON-NLS-1$
		}
		else if (POSConstants.KITCHEN_DISPLAY_BUTTON_TEXT.equals(e.getActionCommand())) {
			TerminalConfig.setDefaultView(KitchenDisplayView.VIEW_NAME);
			doLogin();
		}
		else if (POSConstants.SWITCHBOARD.equals(e.getActionCommand())) {
			TerminalConfig.setDefaultView(SwitchboardView.VIEW_NAME);
			doLogin();
		}
	}
}