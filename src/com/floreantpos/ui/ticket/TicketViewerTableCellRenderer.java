package com.floreantpos.ui.ticket;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.floreantpos.model.ITicketItem;
import com.floreantpos.util.NumberUtil;

public class TicketViewerTableCellRenderer extends DefaultTableCellRenderer {
	private boolean inTicketScreen = false;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

		TicketViewerTableModel model = (TicketViewerTableModel) table.getModel();
		Object object = model.get(row);

		if (!inTicketScreen || isSelected) {
			return rendererComponent;
		}

		rendererComponent.setBackground(Color.WHITE);

		if (object instanceof ITicketItem) {
			ITicketItem ticketItem = (ITicketItem) object;
			if (ticketItem.isPrintedToKitchen()) {
				rendererComponent.setBackground(Color.YELLOW);
			}
		}

		return rendererComponent;
	}

	@Override
	protected void setValue(Object value) {
		if (value == null) {
			setText(""); //$NON-NLS-1$
			return;
		}

		String text = value.toString();

		if (value instanceof Double || value instanceof Float) {
			text = NumberUtil.formatNumber(((java.lang.Number) value).doubleValue());
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
		else if (value instanceof Integer) {
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
		else {
			setHorizontalAlignment(SwingConstants.LEFT);
		}

		//setText(" " + text + " ");
		setText(text);
	}

	public boolean isInTicketScreen() {
		return inTicketScreen;
	}

	public void setInTicketScreen(boolean inTicketScreen) {
		this.inTicketScreen = inTicketScreen;
	}
}
