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

import com.floreantpos.IconFactory;
import com.floreantpos.swing.IntroPage;
import com.floreantpos.ui.views.order.ViewPanel;

/**
 *
 * @author  MShahriar
 */
public class LoginView extends ViewPanel {
	public final static String VIEW_NAME = "LOGIN_VIEW"; //$NON-NLS-1$

	private LoginPasswordEntryView passwordScreen;

	/** Creates new form LoginScreen */
	public LoginView() {
		//setLayout(new MigLayout("ins 20 10 20 10, fill","[fill,growprio 100,grow][]",""));
		setLayout(new BorderLayout(5, 5));

		JLabel titleLabel = new JLabel(IconFactory.getIcon("/ui_icons/", "title.png")); //$NON-NLS-1$ //$NON-NLS-2$
		titleLabel.setOpaque(true);
		titleLabel.setBackground(Color.WHITE);
		
		add(titleLabel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(20, 20, 8, 20)));
		ImageIcon icon = IconFactory.getIcon("/", "logo.png"); //$NON-NLS-1$ //$NON-NLS-2$
		
		if(icon == null) {
			icon = IconFactory.getIcon("/ui_icons/", "floreant-pos.png"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		centerPanel.add(new IntroPage(icon.getImage()));

		add(centerPanel);

		passwordScreen = new LoginPasswordEntryView();
		passwordScreen.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(5, 5, 12, 5)));
		
		add(passwordScreen, BorderLayout.EAST);
	}

	public void setTerminalId(int terminalId) {
		passwordScreen.setTerminalId(terminalId);
	}
	
	@Override
	public String getViewName() {
		return VIEW_NAME;
	}
}
