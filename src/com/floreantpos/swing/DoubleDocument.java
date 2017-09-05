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
/**
 * 
 */
package com.floreantpos.swing;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DoubleDocument extends PlainDocument {

	/**
	 * @param field
	 */
	private Integer decimalPlaces;

	public DoubleDocument() {
	}

	public DoubleDocument(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		String value = getText(0, getLength());
		value = value + str;
		if (decimalPlaces != null) {
			if (value.contains(".")) {
				int currentLength = value.substring(value.indexOf("."), value.length()).length() - 1;

				if (currentLength > this.decimalPlaces) {
					Toolkit.getDefaultToolkit().beep();
					return;
				}
			}
		}
		try {
			Double.parseDouble(value);
		} catch (Exception x) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}

		super.insertString(offs, str, a);
	}
}