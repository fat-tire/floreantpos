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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.Messages;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemCookingInstruction;
import com.floreantpos.model.TicketItemModifier;

public class TodoTicketViewerTableModel extends AbstractTableModel {
	private JTable table;
	protected Ticket ticket;

	private List<ITicketItem> items = new ArrayList<ITicketItem>();

	protected String[] columnNames = {
			Messages.getString("TodoTicketViewerTableModel.0"), Messages.getString("TodoTicketViewerTableModel.1"), Messages.getString("TodoTicketViewerTableModel.2"), Messages.getString("TodoTicketViewerTableModel.3"), Messages.getString("TodoTicketViewerTableModel.4") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	private boolean forReciptPrint;
	private boolean printCookingInstructions;

	public TodoTicketViewerTableModel() {
	}

	public TodoTicketViewerTableModel(Ticket ticket) {
		setTicket(ticket);
	}

	public int getItemCount() {
		return items.size();
	}

	public int getRowCount() {
		return items.size();
	}

	public int getActualRowCount() {
		return items.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		ITicketItem ticketItem = items.get(rowIndex);

		if (ticketItem == null) {
			return null;
		}

		switch (columnIndex) {
			case 0:
				return ticketItem.getNameDisplay();

			case 1:
				return ticketItem.getUnitPriceDisplay();

			case 2:
				return ticketItem.getItemQuantityDisplay();

			case 3:
				return ticketItem.getTaxAmountWithoutModifiersDisplay();

			case 4:
				return ticketItem.getTotalAmountWithoutModifiersDisplay();
		}

		return null;
	}

	private void calculateRows() {
		items.clear();

		if (ticket == null || ticket.getTicketItems() == null)
			return;

		List<TicketItem> ticketItems = ticket.getTicketItems();
		for (TicketItem ticketItem : ticketItems) {

			items.add(ticketItem);

			//			List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
			//			if (ticketItemModifierGroups != null) {
			//				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
			List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
			if (ticketItemModifiers != null) {
				for (TicketItemModifier itemModifier : ticketItemModifiers) {
					items.add(itemModifier);
				}
			}

			List<TicketItemCookingInstruction> cookingInstructions = ticketItem.getCookingInstructions();
			if (cookingInstructions != null) {
				for (TicketItemCookingInstruction ticketItemCookingInstruction : cookingInstructions) {
					items.add(ticketItemCookingInstruction);
				}
			}
		}
	}

	public int addTicketItem(TicketItem ticketItem) {

		if (ticketItem.isHasModifiers()) {
			return addTicketItemToTicket(ticketItem);
		}

		for (int row = 0; row < items.size(); row++) {
			ITicketItem iTicketItem = items.get(row);

			if (!(iTicketItem instanceof TicketItem)) {
				continue;
			}

			TicketItem t = (TicketItem) iTicketItem;

			if (ticketItem.getName().equals(t.getName()) && !t.isPrintedToKitchen()) {
				t.setItemCount(t.getItemCount() + 1);

				table.repaint();

				return Integer.valueOf(row);
			}
		}

		return addTicketItemToTicket(ticketItem);
	}

	private int addTicketItemToTicket(TicketItem ticketItem) {
		ticket.addToticketItems(ticketItem);
		calculateRows();
		fireTableDataChanged();

		return items.size() - 1;
	}

	public void addAllTicketItem(TicketItem ticketItem) {
		if (ticketItem.isHasModifiers()) {
			List<TicketItem> ticketItems = ticket.getTicketItems();
			ticketItems.add(ticketItem);

			calculateRows();
			fireTableDataChanged();
		}
		else {
			List<TicketItem> ticketItems = ticket.getTicketItems();
			boolean exists = false;
			for (TicketItem item : ticketItems) {
				if (item.getName().equals(ticketItem.getName())) {
					int itemCount = item.getItemCount();
					itemCount += ticketItem.getItemCount();
					item.setItemCount(itemCount);
					exists = true;
					table.repaint();
					return;
				}
			}
			if (!exists) {
				ticket.addToticketItems(ticketItem);
				calculateRows();
				fireTableDataChanged();
			}
		}
	}

	public boolean containsTicketItem(TicketItem ticketItem) {
		if (ticketItem.isHasModifiers())
			return false;

		List<TicketItem> ticketItems = ticket.getTicketItems();
		for (TicketItem item : ticketItems) {
			if (item.getName().equals(ticketItem.getName())) {
				return true;
			}
		}
		return false;
	}

	public void removeModifier(TicketItem parent, TicketItemModifier modifierToDelete) {
		//TicketItemModifierGroup ticketItemModifierGroup = modifierToDelete.getParent();
		List<TicketItemModifier> ticketItemModifiers = parent.getTicketItemModifiers();

		for (Iterator iter = ticketItemModifiers.iterator(); iter.hasNext();) {
			TicketItemModifier modifier = (TicketItemModifier) iter.next();
			if (modifier.getModifierId() == modifierToDelete.getModifierId()) {
				iter.remove();

				if (modifier.isPrintedToKitchen()) {
					ticket.addDeletedItems(modifier);
				}

				calculateRows();
				fireTableDataChanged();
				return;
			}
		}
	}

	public Object delete(int index) {
		if (index < 0 || index >= items.size())
			return null;

		ITicketItem iTicketItem = items.get(index);

		if (iTicketItem instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) iTicketItem;
			int rowNum = ticketItem.getTableRowNum();

			List<TicketItem> ticketItems = ticket.getTicketItems();
			for (Iterator iter = ticketItems.iterator(); iter.hasNext();) {
				TicketItem item = (TicketItem) iter.next();
				if (item.getTableRowNum() == rowNum) {
					iter.remove();

					if (item.isPrintedToKitchen() || item.isInventoryHandled()) {
						ticket.addDeletedItems(item);
					}

					break;
				}
			}
		}
		else if (iTicketItem instanceof TicketItemModifier) {
			TicketItemModifier itemModifier = (TicketItemModifier) iTicketItem;
			//TicketItemModifierGroup ticketItemModifierGroup = itemModifier.getParent();
			List<TicketItemModifier> ticketItemModifiers = itemModifier.getTicketItem().getTicketItemModifiers();

			if (ticketItemModifiers != null) {
				for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
					TicketItemModifier element = (TicketItemModifier) iterator.next();
					if (itemModifier.getTableRowNum() == element.getTableRowNum()) {
						iterator.remove();

						if (element.isPrintedToKitchen()) {
							ticket.addDeletedItems(element);
						}
					}
				}
			}
		}
		//		else if (iTicketItem instanceof TicketItemCookingInstruction) {
		//			TicketItemCookingInstruction cookingInstruction = (TicketItemCookingInstruction) iTicketItem;
		//			int tableRowNum = cookingInstruction.getTableRowNum();
		//
		//			TicketItem ticketItem = null;
		//			while (tableRowNum > 0) {
		//				Object object2 = tableRows.get(String.valueOf(--tableRowNum));
		//				if (object2 instanceof TicketItem) {
		//					ticketItem = (TicketItem) object2;
		//					break;
		//				}
		//			}
		//
		//			if (ticketItem != null) {
		//				ticketItem.removeCookingInstruction(cookingInstruction);
		//			}
		//		}

		calculateRows();
		fireTableDataChanged();
		return iTicketItem;
	}

	public Object get(int index) {
		//		if (index < 0 || index >= tableRows.size())
		//			return null;
		//
		//		return tableRows.get(String.valueOf(index));

		return null;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;

		update();
	}

	public void update() {
		calculateRows();
		fireTableDataChanged();
	}

	public boolean isForReciptPrint() {
		return forReciptPrint;
	}

	public void setForReciptPrint(boolean forReciptPrint) {
		this.forReciptPrint = forReciptPrint;
	}

	public boolean isPrintCookingInstructions() {
		return printCookingInstructions;
	}

	public void setPrintCookingInstructions(boolean printCookingInstructions) {
		this.printCookingInstructions = printCookingInstructions;
	}
}
