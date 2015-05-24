/*
 * LoginScreen.java
 *
 * Created on August 14, 2006, 10:57 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.actions.ClockInOutAction;
import com.floreantpos.swing.ImageComponent;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.order.ViewPanel;

/**
 *
 * @author  MShahriar
 */
public class LoginView extends ViewPanel {
	public final static String VIEW_NAME = "LOGIN_VIEW";

	private LoginPasswordEntryView passwordScreen;

	/** Creates new form LoginScreen */
	public LoginView() {
		//setLayout(new MigLayout("ins 20 10 20 10, fill","[fill,growprio 100,grow][]",""));
		setLayout(new BorderLayout(5, 5));

		JLabel titleLabel = new JLabel(IconFactory.getIcon("/ui_icons/", "title.png"));
		titleLabel.setOpaque(true);
		titleLabel.setBackground(Color.WHITE);
		
		add(titleLabel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(20, 20, 8, 20)));
		ImageIcon icon = IconFactory.getIcon("/", "logo.png");
		
		if(icon == null) {
			icon = IconFactory.getIcon("/ui_icons/", "floreant-pos.png");
		}
		
		centerPanel.add(new ImageComponent(icon.getImage()));

		add(centerPanel);

		passwordScreen = new LoginPasswordEntryView();
		passwordScreen.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(5, 5, 12, 5)));
		
		add(passwordScreen, BorderLayout.EAST);
		
		JPanel buttonPanel = new JPanel(new MigLayout("align 50%"));
		
		PosButton btnClockOUt = new PosButton(new ClockInOutAction(false, true));
		buttonPanel.add(btnClockOUt, "w 100!, h 60!"); //$NON-NLS-1$

//		PosButton btnLogout = new PosButton(new LogoutAction(false, true));
//		btnLogout.setToolTipText(Messages.getString("Logout")); //$NON-NLS-1$
//		buttonPanel.add(btnLogout, "w 100!, h 60!"); //$NON-NLS-1$
//
//		PosButton btnShutdown = new PosButton(new ShutDownAction(false, true));
//		btnShutdown.setIcon(IconFactory.getIcon("/ui_icons/", "shutdown.png")); //$NON-NLS-1$
//		btnShutdown.setToolTipText(Messages.getString("Shutdown")); //$NON-NLS-1$
//		buttonPanel.add(btnShutdown, "w 100!, h 60!"); //$NON-NLS-1$
		
		centerPanel.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		if (aFlag) {
			passwordScreen.setFocus();
		}
	}

	public void setTerminalId(int terminalId) {
		passwordScreen.setTerminalId(terminalId);
	}
	
	@Override
	public String getViewName() {
		return VIEW_NAME;
	}
}
