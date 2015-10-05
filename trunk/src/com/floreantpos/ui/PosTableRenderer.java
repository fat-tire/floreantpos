/**
 * 
 */
package com.floreantpos.ui;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.floreantpos.util.NumberUtil;

public class PosTableRenderer extends DefaultTableCellRenderer {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, h:m a"); //$NON-NLS-1$
	/**
	 * 
	 */
	private JCheckBox checkBox = new JCheckBox();
	private JLabel lblColor = new JLabel();
	
	public PosTableRenderer(){
		checkBox.setHorizontalAlignment(SwingConstants.CENTER);
		lblColor.setOpaque(true);
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
		if(value instanceof Color) {
			Color color = (Color) value;
			String rgb = Integer.toHexString(color.getRGB()).toUpperCase();
			rgb = rgb.substring(2, rgb.length());
			
			lblColor.setText(rgb);
			lblColor.setBackground(color);
			
			return lblColor;
		}
		
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
		
		return label;
	}
	
	@Override
	protected void setValue(Object value) {
		if(value == null) {
			setText(""); //$NON-NLS-1$
			return;
		}
		
		String text = value.toString();
		
		if(value instanceof Double || value instanceof Float) {
			text = NumberUtil.formatNumber(((java.lang.Number) value).doubleValue());
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
		
		setText(" " + text + " "); //$NON-NLS-1$ //$NON-NLS-2$
	}
}