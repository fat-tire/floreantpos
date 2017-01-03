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
	MultiLineTableCellRenderer multiLineTableCellRenderer = new MultiLineTableCellRenderer();

	public TicketViewerTableCellRenderer() {
		super();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component rendererComponent = null;

		TicketViewerTableModel model = (TicketViewerTableModel) table.getModel();
		Object object = model.get(row);
		if (column == 1) {
			rendererComponent = multiLineTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
		else {
			rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
			if (column == 0) {
				setHorizontalAlignment(SwingConstants.CENTER);
			}
			else
				setHorizontalAlignment(SwingConstants.RIGHT);
		}

		if (!inTicketScreen || isSelected) {
			return rendererComponent;
		}

		rendererComponent.setBackground(table.getBackground());

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

		if (value instanceof String) {
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		if (value instanceof Double || value instanceof Float) {
			text = NumberUtil.formatNumber(((java.lang.Number) value).doubleValue());
		}
		setText(text);
	}

	public boolean isInTicketScreen() {
		return inTicketScreen;
	}

	public void setInTicketScreen(boolean inTicketScreen) {
		this.inTicketScreen = inTicketScreen;
	}

}
