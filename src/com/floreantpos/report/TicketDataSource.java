package com.floreantpos.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.ticket.TicketItemRowCreator;
import com.floreantpos.util.NumberUtil;

public class TicketDataSource extends AbstractReportDataSource {
	
	public TicketDataSource() {
		super(new String[] { "itemName", "itemQty", "itemSubtotal" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public TicketDataSource(Ticket ticket) {
		super(new String[] { "itemName", "itemQty", "itemSubtotal" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		setTicket(ticket);
	}
	
	private void setTicket(Ticket ticket) {
		ArrayList<ITicketItem> rows = new ArrayList<ITicketItem>();

		LinkedHashMap<String, ITicketItem> tableRows = new LinkedHashMap<String, ITicketItem>();
		TicketItemRowCreator.calculateTicketRows(ticket, tableRows);

		rows.addAll(tableRows.values());
		setRows(rows);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		ITicketItem item = (ITicketItem) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return item.getNameDisplay();

			case 1:
				Integer itemCountDisplay = item.getItemCountDisplay();
				
				if(itemCountDisplay == null) {
					return null;
				}
				
				return String.valueOf(itemCountDisplay);

			case 2:
				Double total = item.getSubTotalAmountWithoutModifiersDisplay();
				if(total == null) {
					return null;
				}
				
				return NumberUtil.formatNumber(total);
		}

		return null;
	}
}
