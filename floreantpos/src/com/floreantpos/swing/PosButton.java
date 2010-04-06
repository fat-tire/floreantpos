package com.floreantpos.swing;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.floreantpos.config.UIConfig;

public class PosButton extends JButton {
	public static Border border = new LineBorder(Color.BLACK, 1);
	static Insets margin = new Insets(1, 1, 1, 1);

	static POSButtonUI ui = new POSButtonUI();

	static {
		UIManager.put("PosButtonUI", "com.floreantpos.swing.POSButtonUI");
	}

	public PosButton() {
		this(null);
	}

	public PosButton(String text) {
		super(text);
		setFont(UIConfig.getButtonFont());
		
		setFocusPainted(false);
		setMargin(margin);
	}

	@Override
	public String getUIClassID() {
		return "PosButtonUI";
	}
}
