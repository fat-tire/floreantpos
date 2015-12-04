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
package com.floreantpos.bo.ui;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomCellRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		if(value instanceof Color) {
			JLabel backgroundLabel = new JLabel();
			backgroundLabel.setOpaque(true);
			backgroundLabel.setBackground((Color) value);
			return backgroundLabel;
		}
		if(value instanceof Date) {
			value = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format((Date) value);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
