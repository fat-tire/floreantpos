package com.floreantpos.ui;

import java.awt.AWTEvent;
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
import com.floreantpos.actions.LogoutAction;
import com.floreantpos.actions.ShowBackofficeAction;
import com.floreantpos.actions.ShutDownAction;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
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
	
	public HeaderPanel() {
		super(new MigLayout("ins 2 2 0 2", "[][fill, grow][]", "")); //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
		
		setOpaque(true);
		setBackground(Color.white);

		JLabel logoLabel = new JLabel(IconFactory.getIcon("/ui_icons/", "header-logo.png")); //$NON-NLS-1$ //$NON-NLS-2$
		add(logoLabel);
		
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
		
		add(statusPanel, "grow"); //$NON-NLS-1$
		
		PosButton btnBackoffice = new PosButton(new ShowBackofficeAction(false, true));
		add(btnBackoffice, "w 60!, h 60!"); //$NON-NLS-1$

		PosButton btnClockOUt = new PosButton(new ClockInOutAction(false, true));
		add(btnClockOUt, "w 60!, h 60!"); //$NON-NLS-1$

		PosButton btnLogout = new PosButton(new LogoutAction(false, true));
		btnLogout.setToolTipText(Messages.getString("Logout")); //$NON-NLS-1$
		add(btnLogout, "w 60!, h 60!"); //$NON-NLS-1$

		PosButton btnShutdown = new PosButton(new ShutDownAction(false, true));
		btnShutdown.setIcon(IconFactory.getIcon("/ui_icons/", "shutdown.png")); //$NON-NLS-1$ //$NON-NLS-2$
		btnShutdown.setToolTipText(Messages.getString("Shutdown")); //$NON-NLS-1$
		add(btnShutdown, "w 60!, h 60!"); //$NON-NLS-1$

		add(new JSeparator(JSeparator.HORIZONTAL), "newline, span 6, grow, gap 0"); //$NON-NLS-1$
		
		clockTimer.start();
		
		if(TerminalConfig.isAutoLogoffEnable()) {
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
		
		if(autoLogoffTimer != null) {
			autoLogoffTimer.start();
		}
	}
	
	private void stopTimer() {
		clockTimer.stop();
		
		if(autoLogoffTimer != null) {
			autoLogoffTimer.stop();
		}
	}
	
	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		
		if(aFlag) {
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
			
			if(!TerminalConfig.isAutoLogoffEnable()) {
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
				Application.getInstance().doLogout();
			}
		}
		
		public void reset() {
			logoffLabel.setText(""); //$NON-NLS-1$
			countDown = TerminalConfig.getAutoLogoffTime();
			
			autoLogoffTimer.setInitialDelay(5 * 1000);
			autoLogoffTimer.restart();
		}
	}
}
