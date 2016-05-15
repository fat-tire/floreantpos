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

import java.awt.Font;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.ComboPopup;

public class POSComboBox extends JComboBox {
	Font font = new Font("Tahoma", Font.PLAIN, PosUIManager.getFontSize(18));

	public POSComboBox() {
		super();
		setHeight(40);
		setFont(font);
	}

	public POSComboBox(Object[] items) {
		super(items);
		setHeight(40);
		setFont(font);
	}

	public void setHeight(int height) {
		setMinimumSize(PosUIManager.getSize(60, 40));
		setPreferredSize(PosUIManager.getSize(0, height));
		Object popup = getUI().getAccessibleChild(this, 0);
		if (popup instanceof ComboPopup) {
			JList jlist = ((ComboPopup) popup).getList();
			jlist.setFixedCellHeight(PosUIManager.getSize(height));
		}
	}

	public POSComboBox(Vector items) {
		super(items);
		setHeight(40);
		setFont(font);
	}
}
