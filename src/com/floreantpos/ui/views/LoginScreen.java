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
import com.floreantpos.swing.ImageComponent;

/**
 *
 * @author  MShahriar
 */
public class LoginScreen extends JPanel {
	public final static String VIEW_NAME = "LOGIN_VIEW";

	private PasswordScreen passwordScreen;

	/** Creates new form LoginScreen */
	public LoginScreen() {
		//setLayout(new MigLayout("ins 20 10 20 10, fill","[fill,growprio 100,grow][]",""));
		setLayout(new BorderLayout(5, 5));

		JLabel titleLabel = new JLabel(IconFactory.getIcon("title.png"));
		titleLabel.setOpaque(true);
		titleLabel.setBackground(Color.WHITE);
		
		add(titleLabel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(20, 20, 20, 20)));
		ImageIcon icon = IconFactory.getIcon("/", "logo.png");
		
		if(icon == null) {
			icon = IconFactory.getIcon("floreant-pos.png");
		}
		
		centerPanel.add(new ImageComponent(icon.getImage()));

		add(centerPanel);

		passwordScreen = new PasswordScreen();
		passwordScreen.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(5, 5, 12, 5)));
		
		add(passwordScreen, BorderLayout.EAST);
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
}
