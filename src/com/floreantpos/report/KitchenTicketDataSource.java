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

import java.util.Collections;
import java.util.Comparator;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.KitchenTicketItem;

public class KitchenTicketDataSource extends AbstractReportDataSource {

	public KitchenTicketDataSource() {
		super(new String[] { "groupName", "itemNo", "itemName", "itemQty" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public KitchenTicketDataSource(KitchenTicket ticket) {
		super(new String[] { "groupId", "groupName", "itemNo", "itemName", "itemQty" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		setTicket(ticket);
	}

	private void setTicket(KitchenTicket ticket) {
		if (!ticket.getType().isAllowSeatBasedOrder()) {
			if (TerminalConfig.isGroupKitchenReceiptItems()) {
				Collections.sort(ticket.getTicketItems(), new Comparator<KitchenTicketItem>() {
					public int compare(KitchenTicketItem o1, KitchenTicketItem o2) {
						return (o1.getMenuItemGroupId() - o2.getMenuItemGroupId());
					}
				});

				Collections.sort(ticket.getTicketItems(), new Comparator<KitchenTicketItem>() {
					public int compare(KitchenTicketItem o1, KitchenTicketItem o2) {
						return (o1.getSortOrder() - o2.getSortOrder());
					}
				});
			}
		}
		setRows(ticket.getTicketItems());
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		KitchenTicketItem item = (KitchenTicketItem) rows.get(rowIndex);

		switch (columnIndex) {

			case 0:
				return String.valueOf(item.getMenuItemGroupId());

			case 1:
				return item.getMenuItemGroupName();

			case 2:
				return item.getMenuItemCode();

			case 3:
				return item.getMenuItemName();

			case 4:
				if (item.isFractionalUnit()) {

					double itemQuantity = item.getFractionalQuantity();

					if (itemQuantity % 1 == 0) {
						return String.valueOf((int) itemQuantity) + item.getUnitName();
					}
					return String.valueOf(itemQuantity) + item.getUnitName();
				}
				if (item.getQuantity() == 0) {
					return "";
				}
				return String.valueOf(item.getQuantity());
		}
		return null;
	}
}
