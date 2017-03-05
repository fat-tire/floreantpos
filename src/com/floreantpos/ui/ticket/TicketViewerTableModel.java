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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.Messages;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemCookingInstruction;
import com.floreantpos.model.TicketItemModifier;

public class TicketViewerTableModel extends AbstractTableModel {
	private JTable table;
	protected Ticket ticket;
	private double previousFractionalItemQuantity;
	protected final HashMap<String, ITicketItem> tableRows = new LinkedHashMap<String, ITicketItem>();

	private boolean priceIncludesTax = false;

	protected String[] columnNames = {
			Messages.getString("TicketViewerTableModel.2"), Messages.getString("TicketViewerTableModel.0"), /*Messages.getString("TicketViewerTableModel.1"),*/Messages.getString("TicketViewerTableModel.3") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	private boolean forReciptPrint;
	private boolean printCookingInstructions;

	public TicketViewerTableModel(JTable table) {
		this(table, null);
	}

	public TicketViewerTableModel(JTable table, Ticket ticket) {
		this.table = table;
		setTicket(ticket);
	}

	public int getItemCount() {
		return tableRows.size();
	}

	public int getRowCount() {
		int size = tableRows.size();

		return size;
	}

	public int getActualRowCount() {
		return tableRows.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		ITicketItem ticketItem = tableRows.get(String.valueOf(rowIndex));

		if (ticketItem == null) {
			return null;
		}

		switch (columnIndex) {
			case 0:
				return ticketItem.getItemQuantityDisplay();

				/*case 1:
					return ticketItem.getUnitPriceDisplay();*/

			case 1:
				return ticketItem.getNameDisplay();

			case 2:
				return ticketItem.getSubTotalAmountDisplay();
		}

		return null;
	}

	private void calculateRows() {
		TicketItemRowCreator.calculateTicketRows(ticket, tableRows);
	}

	public int addTicketItem(TicketItem ticketItem) {
		if (ticketItem.isHasModifiers()) {
			return addTicketItemToTicket(ticketItem);
		}

		Object[] values = tableRows.values().toArray();
		if (values == null || values.length == 0) {
			previousFractionalItemQuantity = ticketItem.getItemQuantity();
			return addTicketItemToTicket(ticketItem);
		}

		Object object = values[values.length - 1];
		if (object instanceof TicketItem) {
			TicketItem item = (TicketItem) object;

			if (ticketItem.getItemId() == 0) {// MISC item
				return addTicketItemToTicket(ticketItem);
			}

			if (ticketItem.getItemId().equals(item.getItemId()) && !item.isPrintedToKitchen() && !item.isInventoryHandled()) {
				if (ticketItem.isFractionalUnit()) {
					item.setItemQuantity(previousFractionalItemQuantity + ticketItem.getItemQuantity());
					previousFractionalItemQuantity = item.getItemQuantity();
				}
				else {
					item.setItemCount(item.getItemCount() + 1);
				}
				return values.length - 1;
			}
		}
		previousFractionalItemQuantity = ticketItem.getItemQuantity();
		return addTicketItemToTicket(ticketItem);
	}

	private int addTicketItemToTicket(TicketItem ticketItem) {
		ticket.addToticketItems(ticketItem);
		calculateRows();
		fireTableDataChanged();

		return tableRows.size() - 1;
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
		if (index < 0 || index >= tableRows.size())
			return null;

		Object object = tableRows.get(String.valueOf(index));
		if (object instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) object;
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
		else if (object instanceof TicketItemModifier) {
//			TicketItemModifier itemModifier = (TicketItemModifier) object;
//			TicketItemModifierGroup ticketItemModifierGroup = itemModifier.getParent();
//			List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
//
//			if (ticketItemModifiers != null) {
//				for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
//					TicketItemModifier element = (TicketItemModifier) iterator.next();
//					if (itemModifier.getTableRowNum() == element.getTableRowNum()) {
//						iterator.remove();
//
//						if (element.isPrintedToKitchen()) {
//							ticket.addDeletedItems(element);
//						}
//					}
//				}
//			}
		}
		else if (object instanceof TicketItemCookingInstruction) {
			TicketItemCookingInstruction cookingInstruction = (TicketItemCookingInstruction) object;
			int tableRowNum = cookingInstruction.getTableRowNum();

			TicketItem ticketItem = null;
			while (tableRowNum > 0) {
				Object object2 = tableRows.get(String.valueOf(--tableRowNum));
				if (object2 instanceof TicketItem) {
					ticketItem = (TicketItem) object2;
					break;
				}
			}

			if (ticketItem != null) {
				ticketItem.removeCookingInstruction(cookingInstruction);
			}
		}

		calculateRows();
		fireTableDataChanged();
		return object;
	}

	public Object get(int index) {
		if (index < 0 || index >= tableRows.size())
			return null;

		return tableRows.get(String.valueOf(index));
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

	public boolean isPriceIncludesTax() {
		return priceIncludesTax;
	}

	public void setPriceIncludesTax(boolean priceIncludesTax) {
		this.priceIncludesTax = priceIncludesTax;
	}
}
