package com.floreantpos.ui;

import java.awt.Color;
import java.awt.Font;
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
import com.floreantpos.actions.ClockoutAction;
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
	
	private final TimerHandler TIMER_HANDLER = new TimerHandler();
	private Timer timer = new Timer(1000, TIMER_HANDLER);

	private String userString = Messages.getString("PosMessage.70"); //$NON-NLS-1$
	private String terminalString = Messages.getString("TERMINAL_LABEL"); //$NON-NLS-1$
	
	private JLabel logoffLabel;
	
	public HeaderPanel() {
		super(new MigLayout("ins 2 2 0 2", "[][fill, grow][]", "")); //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
		
		setOpaque(true);
		setBackground(Color.white);

		JLabel logoLabel = new JLabel(IconFactory.getIcon("header-logo.png")); //$NON-NLS-1$
		add(logoLabel);
		
		TransparentPanel statusPanel = new TransparentPanel(new MigLayout("hidemode 3, fill, ins 0, gap 0"));
		statusLabel = new JLabel();
		statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
		statusLabel.setHorizontalAlignment(JLabel.CENTER);
		statusLabel.setVerticalAlignment(JLabel.BOTTOM);
		statusPanel.add(statusLabel, "grow");
		logoffLabel = new JLabel();
		logoffLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
		logoffLabel.setHorizontalAlignment(JLabel.CENTER);
		logoffLabel.setVerticalAlignment(JLabel.TOP);
		statusPanel.add(logoffLabel, "newline, growx");
		
		add(statusPanel, "grow"); //$NON-NLS-1$
		
		PosButton btnBackoffice = new PosButton(new ShowBackofficeAction(false, true));
		add(btnBackoffice, "w 60!, h 60!"); //$NON-NLS-1$

		PosButton btnClockOUt = new PosButton(new ClockoutAction(false, true));
		btnClockOUt.setToolTipText(Messages.getString("Clockout")); //$NON-NLS-1$
		add(btnClockOUt, "w 60!, h 60!"); //$NON-NLS-1$

		PosButton btnLogout = new PosButton(new LogoutAction(false, true));
		btnLogout.setToolTipText(Messages.getString("Logout")); //$NON-NLS-1$
		add(btnLogout, "w 60!, h 60!"); //$NON-NLS-1$

		PosButton btnShutdown = new PosButton(new ShutDownAction(false, true));
		btnShutdown.setIcon(IconFactory.getIcon("shutdown.png")); //$NON-NLS-1$
		btnShutdown.setToolTipText(Messages.getString("Shutdown")); //$NON-NLS-1$
		add(btnShutdown, "w 60!, h 60!"); //$NON-NLS-1$

		add(new JSeparator(JSeparator.HORIZONTAL), "newline, span 6, grow, gap 0");
		
		timer.start();
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
		timer.start();
	}
	
	private void stopTimer() {
		timer.stop();
	}
	
	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		
		if(aFlag) {
			TIMER_HANDLER.reset();
			startTimer();
		}
		else {
			stopTimer();
		}
	}
	
	private class TimerHandler implements ActionListener {
		int countDown = TerminalConfig.getAutoLogoffTime();
		boolean autoLogoffEnable = TerminalConfig.isAutoLogoffEnable();

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isShowing()) {
				timer.stop();
				return;
			}
			
			showHeader();
			
			checkLogoffOption();
		}

		private void checkLogoffOption() {
			if(!autoLogoffEnable) {
				return;
			}
			
			if (PosGuiUtil.isModalDialogShowing()) {
				reset();
				return;
			}

			--countDown;
			int min = countDown / 60;
			int sec = countDown % 60;

			logoffLabel.setText("Aoto logoff in " + min + ":" + sec);

			if (countDown == 0) {
				Application.getInstance().doLogout();
			}
		}

		public void reset() {
			logoffLabel.setText("");
			countDown = TerminalConfig.getAutoLogoffTime();
		}

	}
}
