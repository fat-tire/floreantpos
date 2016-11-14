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
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomCellRenderer extends DefaultTableCellRenderer {
	private Border unselectedBorder = null;
	private Border selectedBorder = null;

	public CustomCellRenderer() {
//		setLineWrap(true);
//		setWrapStyleWord(true);
//		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		if (selectedBorder == null) {
			selectedBorder = BorderFactory.createMatteBorder(5, 5, 5, 5, table.getSelectionBackground());
		}
		if (unselectedBorder == null) {
			unselectedBorder = BorderFactory.createMatteBorder(5, 5, 5, 5, table.getBackground());
		}

		if (value instanceof byte[]) {

			byte[] imageData = (byte[]) value;
			ImageIcon image = new ImageIcon(imageData);
			image = new ImageIcon(image.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
			if (imageData != null) {
				table.setRowHeight(row, 120);
			}

			JLabel l = new JLabel(image);
			if (isSelected) {
				l.setBorder(selectedBorder);
			}
			else {
				l.setBorder(unselectedBorder);
			}
			return l;
		}

		if (value instanceof Color) {
			JLabel l = new JLabel();

			Color newColor = (Color) value;
			l.setOpaque(true);
			l.setBackground(newColor);
			if (isSelected) {
				l.setBorder(selectedBorder);
			}
			else {
				l.setBorder(unselectedBorder);
			}
			return l;
		}

		if (value instanceof Date) {
			String pattern = "MM/dd hh:mm a"; //$NON-NLS-1$
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			value = format.format((Date) value);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
		
		if(value instanceof String) {
			value = "<html>" + value + "</html>"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
