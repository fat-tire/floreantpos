package com.floreantpos.ui.ticket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;

public class TicketTableModel extends AbstractTableModel {
	private JTable table;
	protected Ticket ticket;
	protected final HashMap<String, Object> tableRows = new HashMap<String, Object>();

	protected String[] columnNames = { "itemQuantity", "itemName", "itemSubtotalPrice" };

	private boolean forReciptPrint;
	private boolean printCookingInstructions;

	public TicketTableModel() {
	}

	public TicketTableModel(Ticket ticket) {
		setTicket(ticket);
	}

	public int getItemCount() {
		return tableRows.size();
	}

	public int getRowCount() {
		int size = tableRows.size();
		if (forReciptPrint) {
			return size;
		}
		if (size < 30) {
			size = 30;
		}
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
		Object value = tableRows.get(String.valueOf(rowIndex));
		if (value instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) value;

			switch (columnIndex) {
				case 0:
					return Integer.valueOf(ticketItem.getItemCount());

				case 1:
					return ticketItem.getName();

				case 2:
					return Double.valueOf(ticketItem.getSubtotalAmountWithoutModifiers());
			}
		}

		if (value instanceof TicketItemModifier) {
			TicketItemModifier modifier = (TicketItemModifier) value;

			switch (columnIndex) {
				case 0:
					if (modifier.getModifierType() == TicketItemModifier.NO_MODIFIER) {
						return null;
					}
					return Integer.valueOf(modifier.getItemCount());

				case 1:
					String display = modifier.getName();
					if (modifier.getModifierType() == TicketItemModifier.NO_MODIFIER) {
						display = " - No " + display;
						return display;
					}
					else if (modifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
						display = " - Extra " + display;
						return display;
					}
					return " - " + display;

				case 2:
					if (modifier.getModifierType() == TicketItemModifier.NO_MODIFIER) {
						return null;
					}
					return modifier.getTotalAmount();
			}
		}

		return null;
	}

	private void calculateRows() {
		tableRows.clear();

		int rowNum = 0;

		if (ticket == null || ticket.getTicketItems() == null)
			return;

		List<TicketItem> ticketItems = ticket.getTicketItems();
		for (TicketItem ticketItem : ticketItems) {
			ticketItem.setTableRowNum(rowNum);
			tableRows.put(String.valueOf(rowNum), ticketItem);
			rowNum++;

			List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
					if (ticketItemModifiers != null) {
						for (TicketItemModifier itemModifier : ticketItemModifiers) {
							itemModifier.setTableRowNum(rowNum);
							tableRows.put(String.valueOf(rowNum), itemModifier);
							rowNum++;
						}
					}
				}
			}
		}
	}

	public void addTicketItem(TicketItem ticketItem) {
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
					item.setItemCount(++itemCount);
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
		TicketItemModifierGroup ticketItemModifierGroup = modifierToDelete.getParent();
		List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();

		for (Iterator iter = ticketItemModifiers.iterator(); iter.hasNext();) {
			TicketItemModifier modifier = (TicketItemModifier) iter.next();
			if (modifier.getItemId() == modifierToDelete.getItemId()) {
				iter.remove();
				
				if(modifier.isPrintedToKitchen()) {
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
					
					if(item.isPrintedToKitchen()) {
						ticket.addDeletedItems(item);
					}
					
					break;
				}
			}
		}
		else if (object instanceof TicketItemModifier) {
			TicketItemModifier itemModifier = (TicketItemModifier) object;
			TicketItemModifierGroup ticketItemModifierGroup = itemModifier.getParent();
			List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();

			if (ticketItemModifiers != null) {
				for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
					TicketItemModifier element = (TicketItemModifier) iterator.next();
					if (itemModifier.getTableRowNum() == element.getTableRowNum()) {
						iterator.remove();
						
						if(element.isPrintedToKitchen()) {
							ticket.addDeletedItems(element);
						}
					}
				}
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

	public int indexOf(Object o) {
		Collection collection = tableRows.values();
		int i = 0;
		for (Iterator iter = collection.iterator(); iter.hasNext();) {
			if (iter.next() == o) {
				//return i;
			}
			++i;
		}
		return 0;
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

		calculateRows();
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
