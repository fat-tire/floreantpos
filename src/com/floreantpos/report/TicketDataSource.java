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
package com.floreantpos.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
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

		Collection<ITicketItem> items = tableRows.values();
		for (ITicketItem item : items) {
			if (item instanceof TicketItem && ((TicketItem) item).isTreatAsSeat())
				continue;
			rows.add(item);
		}
		setRows(rows);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		ITicketItem item = (ITicketItem) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return item.getNameDisplay();

			case 1:
				return item.getItemQuantityDisplay();

			case 2:
				Double total = item.getSubTotalAmountDisplay();
				if (total == null) {
					return null;
				}

				return NumberUtil.formatNumber(total);
		}

		return null;
	}
}
