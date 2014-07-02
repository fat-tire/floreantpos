package com.floreantpos.ui.ticket;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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

	protected String[] columnNames = { "Item", "U/Price", "Unit", "Tax", "Value" };

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
				return ticketItem.getName();

			case 1:
				return ticketItem.getUnitPrice();

			case 2:
				return ticketItem.getItemCount();

			case 3:
				return ticketItem.getTaxAmountWithoutModifiers();

			case 4:
				// return ticketItem.getTotalAmountWithoutModifiers();
				return Double.valueOf(ticketItem.getSubtotalAmountWithoutModifiers() + ticketItem.getTaxAmountWithoutModifiers());
			}
		}

		if (value instanceof TicketItemModifier) {
			TicketItemModifier modifier = (TicketItemModifier) value;

			switch (columnIndex) {
			case 0:
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

			case 1:
				if (modifier.getModifierType() == TicketItemModifier.NO_MODIFIER) {
					return null;
				}
				if (modifier.getModifierType() == TicketItemModifier.NORMAL_MODIFIER) {
					return modifier.getUnitPrice();
				}
				if (modifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
					return modifier.getExtraUnitPrice();
				}

			case 2:
				if (modifier.getModifierType() == TicketItemModifier.NO_MODIFIER) {
					return null;
				}
				return Integer.valueOf(modifier.getItemCount());

			case 3:
				if (modifier.getModifierType() == TicketItemModifier.NO_MODIFIER) {
					return null;
				}
				
				return  modifier.getTotalAmount() * (modifier.getTaxRate() / 100);
//				if (modifier.getModifierType() == TicketItemModifier.NORMAL_MODIFIER) {
//					return (modifier.getUnitPrice() * modifier.getItemCount()) * (modifier.getTaxRate() / 100);
//				}
//				if (modifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
//					return (modifier.getExtraUnitPrice() * modifier.getItemCount()) * (modifier.getTaxRate() / 100);
//				}

			case 4:
				if (modifier.getModifierType() == TicketItemModifier.NO_MODIFIER) {
					return null;
				}

//				double taxAmount = 0;
//				if (modifier.getModifierType() == TicketItemModifier.NORMAL_MODIFIER) {
//					taxAmount = modifier.getTotalAmount() * (modifier.getTaxRate() / 100);
//				}
//				if (modifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
//					taxAmount = modifier.getTotalAmount() * (modifier.getTaxRate() / 100);
//				}

				return modifier.getTotalAmount() + (modifier.getTotalAmount() * (modifier.getTaxRate() / 100));
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

	public int addTicketItem(TicketItem ticketItem) {

		if (ticketItem.isHasModifiers()) {
			return addTicketItemToTicket(ticketItem);
		}

		Set<Entry<String, Object>> entries = tableRows.entrySet();
		for (Entry<String, Object> entry : entries) {

			if (!(entry.getValue() instanceof TicketItem)) {
				continue;
			}

			TicketItem t = (TicketItem) entry.getValue();

			if (ticketItem.getName().equals(t.getName())) {
				t.setItemCount(t.getItemCount() + 1);

				table.repaint();

				return Integer.parseInt(entry.getKey());
			}
		}

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
		TicketItemModifierGroup ticketItemModifierGroup = modifierToDelete.getParent();
		List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();

		for (Iterator iter = ticketItemModifiers.iterator(); iter.hasNext();) {
			TicketItemModifier modifier = (TicketItemModifier) iter.next();
			if (modifier.getItemId() == modifierToDelete.getItemId()) {
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

					if (item.isPrintedToKitchen()) {
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

						if (element.isPrintedToKitchen()) {
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
