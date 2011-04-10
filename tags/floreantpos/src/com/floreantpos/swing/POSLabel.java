package com.floreantpos.swing;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.floreantpos.config.UIConfig;

public class POSLabel extends JLabel {

	public POSLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		
		setFont(UIConfig.largeFont);
	}

	public POSLabel(String text, int horizontalAlignment) {
		this(text,null, horizontalAlignment);
	}

	public POSLabel(String text) {
		this(text, null, JLabel.LEFT);
	}

	public POSLabel(Icon image, int horizontalAlignment) {
		this("", image, horizontalAlignment);
	}

	public POSLabel(Icon image) {
		this("", image, SwingConstants.LEFT);
	}

	public POSLabel() {
		this("", null, SwingConstants.LEFT);
	}

}
