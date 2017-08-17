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

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.ui.views.order.actions.TicketEditListener;

public class TicketViewerTable extends JTable {

	private TicketViewerTableModel model;
	private DefaultListSelectionModel selectionModel;
	private TicketViewerTableCellRenderer cellRenderer;
	private List<TicketEditListener> ticketEditListenerList;

	public TicketViewerTable() {
		this(null);
	}

	public TicketViewerTable(Ticket ticket) {
		model = new TicketViewerTableModel(this);
		setModel(model);

		selectionModel = new DefaultListSelectionModel();
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		cellRenderer = new TicketViewerTableCellRenderer();
		ticketEditListenerList = new ArrayList<>();

		//getColumnModel().setColumnMargin(0);
		setGridColor(Color.LIGHT_GRAY);
		setSelectionModel(selectionModel);
		setAutoscrolls(true);
		setShowGrid(true);
		setBorder(null);

		setFocusable(false);

		setRowHeight(50);
		resizeTableColumns();

		setTicket(ticket);
	}

	private void resizeTableColumns() {
		setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
		//setColumnWidth(1, PosUIManager.getSize(50));
		setColumnWidth(0, PosUIManager.getSize(50));
		setColumnWidth(2, PosUIManager.getSize(60));
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

		if (selectedRow > (rowCount - 1)) {
			return;
		}

		--selectedRow;
		if (selectedRow < 0) {
			selectedRow = 0;
		}
		//		while (isModifierOrOther(selectedRow)) {
		//			--selectedRow;
		//			if (selectedRow > (rowCount - 1)) {
		//				return;
		//			}
		//		}

		selectionModel.addSelectionInterval(selectedRow, selectedRow);
		Rectangle cellRect = getCellRect(selectedRow, 0, false);
		scrollRectToVisible(cellRect);
	}

	public void scrollDown() {
		if (isTicketNull())
			return;

		int selectedRow = getSelectedRow();
		if (selectedRow >= model.getItemCount() - 1) {
			return;
		}

		++selectedRow;
		//		while (isModifierOrOther(selectedRow)) {
		//			++selectedRow;
		//			if (selectedRow >= model.getItemCount() - 1) {
		//				return;
		//			}
		//		}

		selectionModel.addSelectionInterval(selectedRow, selectedRow);
		Rectangle cellRect = getCellRect(selectedRow, 0, false);
		scrollRectToVisible(cellRect);
	}

	private boolean isModifierOrOther(int selectedRow) {
		ITicketItem selectedItem = (ITicketItem) get(selectedRow);
		if (selectedItem instanceof TicketItem) {
			return false;
		}
		else {
			return true;
		}
	}

	public void increaseItemAmount(TicketItem ticketItem) {
		int itemCount = ticketItem.getItemCount();
		ticketItem.setItemCount(++itemCount);
		repaint();
	}

	public boolean increaseFractionalUnit(double selectedQuantity) {
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
			ticketItem.setItemQuantity(selectedQuantity);
			return true;
		}
		return false;
	}

	public boolean increaseItemAmount() {
		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			return false;
		}
		else if (selectedRow >= model.getItemCount()) {
			return false;
		}

		ITicketItem iTicketItem = (ITicketItem) model.get(selectedRow);
		if (iTicketItem.isPrintedToKitchen()) {
			return false;
		}

		if (iTicketItem instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) iTicketItem;
			int itemCount = ticketItem.getItemCount();
			ticketItem.setItemCount(++itemCount);
			fireTicketItemUpdated(getTicket(), ticketItem);
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

		ITicketItem iTicketItem = (ITicketItem) model.get(selectedRow);
		if (iTicketItem.isPrintedToKitchen()) {
			return false;
		}
		if (iTicketItem instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) iTicketItem;
			int itemCount = ticketItem.getItemCount();
			if (itemCount == 1) {
				model.delete(selectedRow);
				fireTicketItemUpdated(getTicket(), ticketItem);
				return true;
			}

			ticketItem.setItemCount(--itemCount);
			fireTicketItemUpdated(getTicket(), ticketItem);
			return true;
		}
		return false;
	}

	public void setTicket(Ticket ticket) {
		model.setTicket(ticket);
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
		fireTicketItemUpdated(getTicket(), ticketItem);
	}

	public Object deleteSelectedItem() {
		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			return null;
		}
		Object delete = model.delete(selectedRow);
		if (delete instanceof TicketItem) {
			fireTicketItemUpdated(getTicket(), (TicketItem) delete);
		}
		return delete;
	}

	public boolean containsTicketItem(TicketItem ticketItem) {
		return model.containsTicketItem(ticketItem);
	}

	public void delete(int index) {
		model.delete(index);
	}

	public ITicketItem get(int index) {
		return (ITicketItem) model.get(index);
	}

	public ITicketItem getSelected() {
		int index = getSelectedRow();

		return (ITicketItem) model.get(index);
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
		} catch (Exception e) {
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
		if (index < 0 || index >= getActualRowCount()) {
			index = 0;
		}
		selectionModel.addSelectionInterval(index, index);
		Rectangle cellRect = getCellRect(index, 0, false);
		scrollRectToVisible(cellRect);
	}

	public TicketViewerTableModel getModel() {
		return model;
	}

	private List<TicketItem> getRowByValue(TicketViewerTableModel model) {

		List<TicketItem> ticketItems = new ArrayList();
		for (int i = 0; i <= model.getRowCount(); i++) {
			Object value = model.get(i);
			if (value instanceof TicketItem) {
				TicketItem ticketItem = (TicketItem) value;
				ticketItems.add(ticketItem);
			}

		}
		return ticketItems;
	}

	public List<TicketItem> getTicketItems() {
		return getRowByValue(model);
	}

	public TicketItem getTicketItem() {
		return (TicketItem) getSelected();
	}

	public void addTicketUpdateListener(TicketEditListener l) {
		ticketEditListenerList.add(l);
	}

	public void removeTicketUpdateListener(TicketEditListener l) {
		ticketEditListenerList.remove(l);
	}

	public void fireTicketItemUpdated(Ticket ticket, TicketItem ticketItem) {
		for (TicketEditListener listener : this.ticketEditListenerList) {
			listener.itemAdded(ticket, ticketItem);
		}
	}
}
