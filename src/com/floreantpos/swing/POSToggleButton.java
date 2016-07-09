/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.swing;

import java.awt.Dimension;

import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.floreantpos.POSConstants;


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

//		setFont(UIConfig.getButtonFont());
		setFocusPainted(false);
		setFocusable(false);
		//setMargin(margin);
	}

	@Override
	public String getUIClassID() {
		return "POSToggleButtonUI"; //$NON-NLS-1$
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension size = null;

		if (isPreferredSizeSet()) {
			return super.getPreferredSize();
		}
		else if (ui != null) {
			size = ui.getPreferredSize(this);
		}

		if (size == null) {
			size = new Dimension(PosUIManager.getSize(POSConstants.BUTTON_DEFAULT_WIDTH, POSConstants.BUTTON_DEFAULT_HEIGHT));
		}
		else {
			int width = size.width < POSConstants.BUTTON_DEFAULT_WIDTH ? POSConstants.BUTTON_DEFAULT_WIDTH : size.width;
			int height = size.height < POSConstants.BUTTON_DEFAULT_HEIGHT ? POSConstants.BUTTON_DEFAULT_HEIGHT : size.height;
			size.setSize(PosUIManager.getSize(width, height));
		}

		return size;
	}
}
