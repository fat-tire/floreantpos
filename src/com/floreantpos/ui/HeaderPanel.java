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
package com.floreantpos.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.Timer;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.actions.ClockInOutAction;
import com.floreantpos.actions.HomeScreenViewAction;
import com.floreantpos.actions.LogoutAction;
import com.floreantpos.actions.ShowOtherFunctionsAction;
import com.floreantpos.actions.ShutDownAction;
import com.floreantpos.actions.SwithboardViewAction;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.util.PosGuiUtil;

public class HeaderPanel extends JPanel {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss aaa"); //$NON-NLS-1$

	private JLabel statusLabel;

	private Timer clockTimer = new Timer(1000, new ClockTimerHandler());
	private Timer autoLogoffTimer;

	private String userString = Messages.getString("PosMessage.70"); //$NON-NLS-1$
	private String terminalString = Messages.getString("TERMINAL_LABEL"); //$NON-NLS-1$

	private JLabel logoffLabel;
	private PosButton btnHomeScreen;
	private PosButton btnOthers;
	private PosButton btnSwithboardView;
	private PosButton btnLogout;
	private PosButton btnClockOUt;
	private PosButton btnShutdown;
	private JPanel buttonPanel;

	private int btnSize;

	public HeaderPanel() {
		//	super(new MigLayout("ins 2 2 0 2,hidemode 3", "[][fill, grow][]", "")); //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
		super(new BorderLayout());
		setOpaque(true);
		setBackground(Color.white);

		buttonPanel = new JPanel(new MigLayout("hidemode 3", "sg,fill", ""));
		buttonPanel.setBackground(Color.white);

		JLabel logoLabel = new JLabel(IconFactory.getIcon("/ui_icons/", "header-logo.png")); //$NON-NLS-1$ //$NON-NLS-2$
		//	add(logoLabel);
		add(logoLabel, BorderLayout.WEST);

		TransparentPanel statusPanel = new TransparentPanel(new MigLayout("hidemode 3, fill, ins 0, gap 0")); //$NON-NLS-1$
		statusLabel = new JLabel();
		statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
		statusLabel.setHorizontalAlignment(JLabel.CENTER);
		statusLabel.setVerticalAlignment(JLabel.BOTTOM);
		statusPanel.add(statusLabel, "grow"); //$NON-NLS-1$
		logoffLabel = new JLabel();
		logoffLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
		logoffLabel.setHorizontalAlignment(JLabel.CENTER);
		logoffLabel.setVerticalAlignment(JLabel.TOP);
		statusPanel.add(logoffLabel, "newline, growx"); //$NON-NLS-1$

		//add(statusPanel, "grow"); //$NON-NLS-1$
		add(statusPanel, BorderLayout.CENTER);

		btnSize = PosUIManager.getSize(60);

		btnHomeScreen = new PosButton(new HomeScreenViewAction(false, true));
		buttonPanel.add(btnHomeScreen, "w " + btnSize + "!, h " + btnSize + "!"); //$NON-NLS-1$

		btnSwithboardView = new PosButton(new SwithboardViewAction(false, true));
		buttonPanel.add(btnSwithboardView, "w " + btnSize + "!, h " + btnSize + "!"); //$NON-NLS-1$

		btnOthers = new PosButton(new ShowOtherFunctionsAction(false, true));
		buttonPanel.add(btnOthers, "w " + btnSize + "!, h " + btnSize + "!"); //$NON-NLS-1$

		btnClockOUt = new PosButton(new ClockInOutAction(false, true));
		buttonPanel.add(btnClockOUt, "w " + btnSize + "!, h " + btnSize + "!"); //$NON-NLS-1$

		btnLogout = new PosButton(new LogoutAction(false, true));
		btnLogout.setToolTipText(Messages.getString("Logout")); //$NON-NLS-1$
		buttonPanel.add(btnLogout, "w " + btnSize + "!, h " + btnSize + "!"); //$NON-NLS-1$

		btnShutdown = new PosButton(new ShutDownAction(false, true));

		btnShutdown.setIcon(IconFactory.getIcon("/ui_icons/", "shutdown.png")); //$NON-NLS-1$ //$NON-NLS-2$
		btnShutdown.setToolTipText(Messages.getString("Shutdown")); //$NON-NLS-1$
		buttonPanel.add(btnShutdown, "w " + btnSize + "!, h " + btnSize + "!"); //$NON-NLS-1$

		add(buttonPanel, BorderLayout.EAST);

		add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);

		//add(new JSeparator(JSeparator.HORIZONTAL), "newline, span 7, grow, gap 0"); //$NON-NLS-1$

		clockTimer.start();

		if (TerminalConfig.isAutoLogoffEnable()) {
			autoLogoffTimer = new Timer(1000, new AutoLogoffHandler());
		}
	}

	private void showHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append(userString + ": " + Application.getCurrentUser().getFirstName()); //$NON-NLS-1$
		sb.append(", "); //$NON-NLS-1$
		sb.append(terminalString + ": " + Application.getInstance().getTerminal().getName()); //$NON-NLS-1$
		sb.append(", "); //$NON-NLS-1$
		sb.append(dateFormat.format(Calendar.getInstance().getTime()));

		statusLabel.setText(sb.toString());
	}

	private void startTimer() {
		clockTimer.start();

		if (autoLogoffTimer != null) {
			autoLogoffTimer.start();
		}
	}

	private void stopTimer() {
		clockTimer.stop();

		if (autoLogoffTimer != null) {
			autoLogoffTimer.stop();
		}
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);

		if (aFlag) {
			startTimer();
		}
		else {
			stopTimer();
		}
	}

	private class ClockTimerHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isShowing()) {
				clockTimer.stop();
				return;
			}

			showHeader();
		}
	}

	class AutoLogoffHandler implements ActionListener, AWTEventListener {
		int countDown = TerminalConfig.getAutoLogoffTime();

		public AutoLogoffHandler() {
			Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
		}

		@Override
		public void eventDispatched(AWTEvent event) {
			reset();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isShowing()) {
				autoLogoffTimer.stop();
				return;
			}

			if (!TerminalConfig.isAutoLogoffEnable()) {
				return;
			}

			if (PosGuiUtil.isModalDialogShowing()) {
				reset();
				return;
			}

			--countDown;
			int min = countDown / 60;
			int sec = countDown % 60;

			logoffLabel.setText(Messages.getString("HeaderPanel.0") + min + ":" + sec); //$NON-NLS-1$ //$NON-NLS-2$

			if (countDown == 0) {
				//Application.getInstance().doLogout();
				Application.getInstance().doAutoLogout();
			}
		}

		public void reset() {
			logoffLabel.setText(""); //$NON-NLS-1$
			countDown = TerminalConfig.getAutoLogoffTime();

			autoLogoffTimer.setInitialDelay(5 * 1000);
			autoLogoffTimer.restart();
		}
	}

	public void updateOthersFunctionsView(boolean enable) {

		buttonPanel.removeAll();
		buttonPanel.add(btnHomeScreen, "w " + btnSize + "!, h " + btnSize + "!");
		buttonPanel.add(btnOthers, "w " + btnSize + "!, h " + btnSize + "!");
		buttonPanel.add(btnSwithboardView, "w " + btnSize + "!, h " + btnSize + "!");
		buttonPanel.add(btnClockOUt, "w " + btnSize + "!, h " + btnSize + "!");
		buttonPanel.add(btnLogout, "w " + btnSize + "!, h " + btnSize + "!");
		buttonPanel.add(btnShutdown, "w " + btnSize + "!, h " + btnSize + "!");
		btnOthers.setVisible(enable);
	}

	public void updateSwitchBoardView(boolean enable) {
		buttonPanel.removeAll();
		buttonPanel.add(btnHomeScreen, "w " + btnSize + "!, h " + btnSize + "!");
		buttonPanel.add(btnOthers, "w " + btnSize + "!, h " + btnSize + "!");
		buttonPanel.add(btnSwithboardView, "w " + btnSize + "!, h " + btnSize + "!");
		buttonPanel.add(btnClockOUt, "w " + btnSize + "!, h " + btnSize + "!");
		buttonPanel.add(btnLogout, "w " + btnSize + "!, h " + btnSize + "!");
		buttonPanel.add(btnShutdown, "w " + btnSize + "!, h " + btnSize + "!");
		btnSwithboardView.setVisible(enable);
	}

	/*public void updateOthersFunctionsView(String viewName) {
		btnOthers.setVisible(!viewName.equals(SwitchboardOtherFunctionsView.VIEW_NAME));
	}

	public void updateSwitchBoardView(String viewName) {
		btnSwithboardView.setVisible(!viewName.equals(SwitchboardView.VIEW_NAME));
	}*/

	public void updateHomeView(boolean enable) {
		btnHomeScreen.setVisible(enable);
	}
}
