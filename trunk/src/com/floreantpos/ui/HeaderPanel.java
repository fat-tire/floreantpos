package com.floreantpos.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
import com.floreantpos.actions.ShutDownAction;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;

public class HeaderPanel extends JPanel {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss aaa"); //$NON-NLS-1$

	private JLabel statusLabel;
	private Timer timer;

	private String userString = Messages.getString("PosMessage.70"); //$NON-NLS-1$
	private String terminalString = Messages.getString("TERMINAL_LABEL"); //$NON-NLS-1$
	
	private String name;
	
	public HeaderPanel() {
		this("");
	}
	
	public HeaderPanel(String name) {
		super(new MigLayout("ins 2 2 0 2", "[][fill, grow][]", "")); //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
		
		this.name = name;

		setOpaque(true);
		setBackground(Color.white);

		JLabel logoLabel = new JLabel(IconFactory.getIcon("header-logo.png")); //$NON-NLS-1$
		add(logoLabel);

		statusLabel = new JLabel();
		statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
		statusLabel.setHorizontalAlignment(JLabel.CENTER);
		add(statusLabel, "grow"); //$NON-NLS-1$

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

		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isShowing()) {
					timer.stop();
					return;
				}
				
				showHeader();
			}
		});

		add(new JSeparator(JSeparator.HORIZONTAL), "newline, span 6, grow, gap 0");
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (timer.isRunning()) {
			return;
		}

		System.out.println("Starting timer for: " + HeaderPanel.this.name);
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
	
	public void startTimer() {
		timer.start();
	}
	
	public void stopTimer() {
		timer.stop();
	}
}
