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

import javax.swing.JTextField;
import javax.swing.text.Document;

import org.apache.commons.lang.StringUtils;

public class FocusedTextField extends JTextField implements FocusListener {

	public FocusedTextField() {
		init();
	}

//	public FocusedTextField(boolean numeric) {
//		this.numeric = numeric;
//
//		init();
//	}

	public FocusedTextField(String text) {
		super(text);

		init();
	}

	public FocusedTextField(int columns) {
		super(columns);

		init();
	}

	public FocusedTextField(String text, int columns) {
		super(text, columns);

		init();
	}

	public FocusedTextField(Document doc, String text, int columns) {
		super(doc, text, columns);

		init();
	}

	private void init() {
		installFocusHandler();
	}
	
	public boolean isEmpty() {
		return StringUtils.isEmpty(getText());
	}

	private void installFocusHandler() {
		addFocusListener(this);
	}

	public void focusGained(FocusEvent e) {
//		setBackground(Color.YELLOW);
		
		selectAll();
	}

	public void focusLost(FocusEvent e) {
//		setBackground(Color.WHITE);
	}

}
