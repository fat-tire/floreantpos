package com.floreantpos.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.ui.ticket.TicketItemRowCreator;

public class KitchenTicketDataSource extends AbstractReportDataSource {

	public KitchenTicketDataSource() {
		super(new String[] { "itemNo", "itemName", "itemQty" });
	}

	public KitchenTicketDataSource(KitchenTicket ticket) {
		super(new String[] { "itemNo", "itemName", "itemQty" });

		setTicket(ticket);
	}

	private void setTicket(KitchenTicket ticket) {
		ArrayList<ITicketItem> rows = new ArrayList<ITicketItem>();

		LinkedHashMap<String, ITicketItem> tableRows = new LinkedHashMap<String, ITicketItem>();
		TicketItemRowCreator.calculateKitchenTicketRows(ticket, tableRows);

		rows.addAll(tableRows.values());
		setRows(rows);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		ITicketItem item = (ITicketItem) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return item.getItemCode();

			case 1:
				return item.getNameDisplay();

			case 2:
				Integer itemCountDisplay = item.getItemCountDisplay();

				if (itemCountDisplay == null) {
					return null;
				}

				return String.valueOf(itemCountDisplay);
		}

		return null;
	}
}
