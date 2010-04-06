package com.floreantpos.swing;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.floreantpos.config.UIConfig;

public class POSToggleButton extends JToggleButton {
	public static Border border = new LineBorder(Color.BLACK, 1);
	static Insets margin = new Insets(1, 1, 1, 1);

	static {
		UIManager.put("POSToggleButtonUI", "com.floreantpos.swing.POSToggleButtonUI");
	}
	
	public POSToggleButton() {
		this(null);
	}
	
	public POSToggleButton(String text) {
		super(text);
		
		setFont(UIConfig.getButtonFont());
		setFocusPainted(false);
		setMargin(margin);
	}
	
	@Override
	public String getUIClassID() {
		return "POSToggleButtonUI";
	}
}
