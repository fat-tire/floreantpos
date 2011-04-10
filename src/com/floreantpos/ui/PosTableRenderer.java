/**
 * 
 */
package com.floreantpos.ui;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.floreantpos.main.Application;

public class PosTableRenderer extends DefaultTableCellRenderer {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, h:m a");
	//private static final DecimalFormat numberFormat = new DecimalFormat("0.00");
	/**
	 * 
	 */
	private JCheckBox checkBox = new JCheckBox();
	
	public PosTableRenderer(){
		checkBox.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(value instanceof Boolean) {
			checkBox.setSelected(((Boolean)value).booleanValue());
			if(isSelected) {
				checkBox.setBackground(table.getSelectionBackground());
			}
			else {
				checkBox.setBackground(table.getBackground());
			}
			return checkBox;
		}
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
		
		return label;
	}
	
	@Override
	protected void setValue(Object value) {
		if(value == null) {
			setText("");
			return;
		}
		
		String text = value.toString();
		
		if(value instanceof Double || value instanceof Float) {
			text = Application.formatNumber(((java.lang.Number) value).doubleValue());
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
		else if(value instanceof Integer) {
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
		else if(value instanceof Date) {
			text = dateFormat.format(value);
			setHorizontalAlignment(SwingConstants.LEFT);
		}
		else {
			setHorizontalAlignment(SwingConstants.LEFT);
		}
		
		setText(" " + text + " ");
	}
}