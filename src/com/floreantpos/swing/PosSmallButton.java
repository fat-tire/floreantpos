package com.floreantpos.swing;

import java.awt.Dimension;

public class PosSmallButton extends PosButton {

	public PosSmallButton() {
		this(null);
	}

	public PosSmallButton(String text) {
		super(text);
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
		
		if(size != null) {
			size.setSize(size.width + 20, 35);
		}
		
		
		return (size != null) ? size : super.getPreferredSize();
	}
}
