package com.floreantpos.bo.ui;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class MyCellRenderer extends DefaultTableCellRenderer {
	
	

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(value instanceof Date) {
			DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
			String format = dateFormat.format(value);
			return super.getTableCellRendererComponent(table, format, isSelected, hasFocus, row, column);
		}
		
		
		
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	}
}
