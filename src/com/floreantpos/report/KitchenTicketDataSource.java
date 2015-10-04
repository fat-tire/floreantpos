package com.floreantpos.report;

import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.KitchenTicketItem;

public class KitchenTicketDataSource extends AbstractReportDataSource {

	public KitchenTicketDataSource() {
		super(new String[] { "itemNo", "itemName", "itemQty" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public KitchenTicketDataSource(KitchenTicket ticket) {
		super(new String[] { "itemNo", "itemName", "itemQty" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		setTicket(ticket);
	}

	private void setTicket(KitchenTicket ticket) {
		setRows(ticket.getTicketItems());
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		KitchenTicketItem item = (KitchenTicketItem) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return item.getMenuItemCode();

			case 1:
				return item.getMenuItemName();

			case 2:
				Integer itemCountDisplay = item.getQuantity();

				if (itemCountDisplay == null) {
					return null;
				}

				return String.valueOf(itemCountDisplay);
		}

		return null;
	}
}
