package com.floreantpos.bo.ui.explorer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColorCellRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		if (value instanceof Color) {
			JLabel backgroundLabel = new JLabel();
			backgroundLabel.setOpaque(true);
			backgroundLabel.setBackground((Color) value);

			return backgroundLabel;
		}
		
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

}
