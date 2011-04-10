package com.floreantpos.swing;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.UIManager;

public class POSTitleLabel extends JLabel {
	private static Font font = UIManager.getFont("Label.font").deriveFont(Font.BOLD, 12);
	private static Color forground = Color.black;
	
	
	public POSTitleLabel() {
		setFont(font);
		setForeground(forground);
	}

}
