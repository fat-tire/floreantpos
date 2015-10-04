package com.floreantpos.swing;

import java.awt.Dimension;

import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.floreantpos.config.UIConfig;

public class POSToggleButton extends JToggleButton {
	//public static Border border = new LineBorder(Color.BLACK, 1);
	//static Insets margin = new Insets(0, 0, 0, 0);

	static {
		UIManager.put("POSToggleButtonUI", "com.floreantpos.swing.POSToggleButtonUI"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public POSToggleButton() {
		this(null);
	}

	public POSToggleButton(String text) {
		super(text);

		setFont(UIConfig.getButtonFont());
		setFocusPainted(false);
		//setMargin(margin);
	}

	@Override
	public String getUIClassID() {
		return "POSToggleButtonUI"; //$NON-NLS-1$
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension size = super.getPreferredSize();

		if (isPreferredSizeSet()) {
			return size;
		}

		if (ui != null) {
			size = ui.getPreferredSize(this);
		}

		if (size != null) {
			size.setSize(size.width + 20, 45);
		}

		return (size != null) ? size : super.getPreferredSize();
	}
}
