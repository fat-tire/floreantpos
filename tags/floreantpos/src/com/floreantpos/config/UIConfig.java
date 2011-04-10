package com.floreantpos.config;

import java.awt.Font;

import javax.swing.UIManager;

public class UIConfig {
	public final static Font buttonFont = UIManager.getFont("Button.font").deriveFont(Font.BOLD, 12);
	public final static Font largeFont = UIManager.getFont("Button.font").deriveFont(Font.BOLD, 16);
	
	public UIConfig() {
		super();
	}

	public static Font getButtonFont() {
		return buttonFont;
	}
	
	
}
