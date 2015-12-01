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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;
import javax.swing.text.Document;

public class POSPasswordField extends JPasswordField implements FocusListener {

	public POSPasswordField() {
		addFocusListener(this);
	}

	public POSPasswordField(String text) {
		super(text);
		addFocusListener(this);
	}

	public POSPasswordField(int columns) {
		super(columns);
		addFocusListener(this);
	}

	public POSPasswordField(String text, int columns) {
		super(text, columns);
		addFocusListener(this);
	}

	public POSPasswordField(Document doc, String txt, int columns) {
		super(doc, txt, columns);
		addFocusListener(this);
	}

	public void focusGained(FocusEvent e) {
		selectAll();
	}

	public void focusLost(FocusEvent e) {
	}

}
