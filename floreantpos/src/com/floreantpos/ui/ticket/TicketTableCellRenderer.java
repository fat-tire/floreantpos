package com.floreantpos.ui.ticket;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;

public class TicketTableCellRenderer extends DefaultTableCellRenderer {
	private static final DecimalFormat numberFormat = new DecimalFormat("0.00");
	private boolean inTicketScreen = false;
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
		
		TicketTableModel model = (TicketTableModel) table.getModel();
		Object object = model.get(row);
		
		if(!inTicketScreen || isSelected) {
			return rendererComponent;
		}
		
		rendererComponent.setBackground(Color.WHITE);
		
		if(object instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) object;
			if(ticketItem.isPrintedToKitchen()) {
				rendererComponent.setBackground(Color.YELLOW);
			}
		}
		if(object instanceof TicketItemModifier) {
			TicketItemModifier modifier = (TicketItemModifier) object;
			if(modifier.isPrintedToKitchen()) {
				rendererComponent.setBackground(Color.YELLOW);
			}
		}
		
		return rendererComponent;
	}
	
	@Override
	protected void setValue(Object value) {
		if(value == null) {
			setText("");
			return;
		}
		
		String text = value.toString();
		
		if(value instanceof Double || value instanceof Float) {
			text = numberFormat.format(((java.lang.Number) value).doubleValue());
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
		else if(value instanceof Integer) {
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
		else {
			setHorizontalAlignment(SwingConstants.LEFT);
		}
		
		setText(" " + text + " ");
	}

	public boolean isInTicketScreen() {
		return inTicketScreen;
	}

	public void setInTicketScreen(boolean inTicketScreen) {
		this.inTicketScreen = inTicketScreen;
	}
}
