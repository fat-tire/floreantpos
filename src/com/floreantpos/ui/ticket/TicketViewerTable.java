package com.floreantpos.ui.ticket;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;

public class TicketViewerTable extends JTable {
	
	private TicketViewerTableModel model;
	private DefaultListSelectionModel selectionModel;
	private TicketViewerTableCellRenderer cellRenderer;

	public TicketViewerTable() {
		this(null);
	}

	public TicketViewerTable(Ticket ticket) {
		setTicket(ticket);
		
		selectionModel = new DefaultListSelectionModel();
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		cellRenderer = new TicketViewerTableCellRenderer();
		
		setGridColor(Color.LIGHT_GRAY);
		setSelectionModel(selectionModel);
		setAutoscrolls(true);
		setShowGrid(true);
		setBorder(null);
		
		setRowHeight(20);
		resizeTableColumns();
	}

	private void resizeTableColumns() {
//		setColumnWidth(0, 180);
		setColumnWidth(1, 50);
		setColumnWidth(2, 30);
		setColumnWidth(3, 60);
		setColumnWidth(4, 75);
	}

	private void setColumnWidth(int columnNumber, int width) {
		TableColumn column = getColumnModel().getColumn(columnNumber);

		column.setPreferredWidth(width);
		column.setMaxWidth(width);
		column.setMinWidth(width);
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return cellRenderer;
	}
	
	public TicketViewerTableCellRenderer getRenderer() {
		return cellRenderer;
	}

	private boolean isTicketNull() {
		Ticket ticket = getTicket();
		if (ticket == null) {
			return true;
		}
		if (ticket.getTicketItems() == null) {
			return true;
		}
		return false;
	}

	public void scrollUp() {
		if (isTicketNull())
			return;

		int selectedRow = getSelectedRow();
		int rowCount = model.getItemCount();

		if (selectedRow <= 0) {
			selectedRow = rowCount - 1;
		}
		else if (selectedRow > (rowCount - 1)) {
			selectedRow = rowCount - 1;
		}
		else {
			--selectedRow;
		}

		selectionModel.addSelectionInterval(selectedRow, selectedRow);
		Rectangle cellRect = getCellRect(selectedRow, 0, false);
		scrollRectToVisible(cellRect);
	}

	public void scrollDown() {
		if (isTicketNull())
			return;

		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			selectedRow = 0;
		}
		else if (selectedRow >= model.getItemCount() - 1) {
			selectedRow = 0;
		}
		else {
			++selectedRow;
		}

		selectionModel.addSelectionInterval(selectedRow, selectedRow);
		Rectangle cellRect = getCellRect(selectedRow, 0, false);
		scrollRectToVisible(cellRect);
	}

	public void increaseItemAmount(TicketItem ticketItem) {
		int itemCount = ticketItem.getItemCount();
		ticketItem.setItemCount(++itemCount);
		repaint();
	}

	public boolean increaseItemAmount() {
		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			return false;
		}
		else if (selectedRow >= model.getItemCount()) {
			return false;
		}

		Object object = model.get(selectedRow);
		if (object instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) object;
			int itemCount = ticketItem.getItemCount();
			ticketItem.setItemCount(++itemCount);
			repaint();

			return true;
		}
		else if (object instanceof TicketItemModifier) {
			TicketItemModifier modifier = (TicketItemModifier) object;
			int itemCount = modifier.getItemCount();
			modifier.setItemCount(++itemCount);
			repaint();
			
			return true;
		}
		return false;
	}

	public boolean decreaseItemAmount() {
		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			return false;
		}
		else if (selectedRow >= model.getItemCount()) {
			return false;
		}

		Object object = model.get(selectedRow);
		if (object instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) object;
			int itemCount = ticketItem.getItemCount();
			if (itemCount == 1)
				return false;

			ticketItem.setItemCount(--itemCount);
			repaint();

			return true;
		}
		else if (object instanceof TicketItemModifier) {
			TicketItemModifier modifier = (TicketItemModifier) object;
			int itemCount = modifier.getItemCount();
			if (itemCount == 1)
				return false;

			modifier.setItemCount(--itemCount);
			repaint();
			
			return true;
		}
		return false;
	}

	public void setTicket(Ticket ticket) {
		model = new TicketViewerTableModel(ticket);
		model.setTable(this);
		
		Restaurant restaurant = Application.getInstance().getRestaurant();
		if(restaurant != null) {
			model.setPriceIncludesTax(restaurant.isItemPriceIncludesTax());
		}
		
		setModel(model);
	}

	public Ticket getTicket() {
		return model.getTicket();
	}

	public void addTicketItem(TicketItem ticketItem) {
		ticketItem.setTicket(getTicket());
		int addTicketItem = model.addTicketItem(ticketItem);
		
		int actualRowCount = addTicketItem;//getActualRowCount() - 1;
		selectionModel.addSelectionInterval(actualRowCount, actualRowCount);
		Rectangle cellRect = getCellRect(actualRowCount, 0, false);
		scrollRectToVisible(cellRect);
	}

	public Object deleteSelectedItem() {
		int selectedRow = getSelectedRow();
		return model.delete(selectedRow);
	}

	public boolean containsTicketItem(TicketItem ticketItem) {
		return model.containsTicketItem(ticketItem);
	}

	public void delete(int index) {
		model.delete(index);
	}

	public Object get(int index) {
		return model.get(index);
	}
	
	public Object getSelected() {
		int index = getSelectedRow();
		
		return model.get(index);
	}

	public void addAllTicketItem(TicketItem ticketItem) {
		model.addAllTicketItem(ticketItem);
	}

	public void removeModifier(TicketItem parent, TicketItemModifier modifier) {
		model.removeModifier(parent, modifier);
	}
	
	public void updateView() {
		int selectedRow = getSelectedRow();
		
		model.update();
		
		try {
			getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
		}catch (Exception e) {
			// do nothing
		}
	}
	
	public int getActualRowCount() {
		return model.getActualRowCount();
	}
	
	public void selectLast() {
		int actualRowCount = getActualRowCount() - 1;
		selectionModel.addSelectionInterval(actualRowCount, actualRowCount);
		Rectangle cellRect = getCellRect(actualRowCount, 0, false);
		scrollRectToVisible(cellRect);
	}
	
	public void selectRow(int index) {
		if(index < 0 || index >= getActualRowCount()) {
			index = 0;
		}
		selectionModel.addSelectionInterval(index, index);
		Rectangle cellRect = getCellRect(index, 0, false);
		scrollRectToVisible(cellRect);
	}
}
