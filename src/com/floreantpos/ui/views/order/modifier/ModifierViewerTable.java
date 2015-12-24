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
package com.floreantpos.ui.views.order.modifier;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.util.ModifierStateChangeListener;

public class ModifierViewerTable extends JTable {
	
	private ModifierViewerTableModel model;
	private DefaultListSelectionModel selectionModel;
	private ModifierViewerTableCellRenderer cellRenderer;
	
	private ModifierStateChangeListener modifierStateChangeListener;

	public ModifierViewerTable() {
		this(null);
	}

	public ModifierViewerTable(Ticket ticket) {
		model = new ModifierViewerTableModel(this);
		setModel(model);
		
		selectionModel = new DefaultListSelectionModel();
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		cellRenderer = new ModifierViewerTableCellRenderer();
		
		setGridColor(Color.LIGHT_GRAY);
		setSelectionModel(selectionModel);
		setAutoscrolls(true);
		setShowGrid(true);
		setBorder(null);
		
		setFocusable(false);
		
		setRowHeight(60);
		resizeTableColumns();
		
		setTicket(ticket);
	}

	private void resizeTableColumns() {
		setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
		setColumnWidth(1, 50);
		setColumnWidth(2, 30);
		setColumnWidth(3, 60);
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
	
	public ModifierViewerTableCellRenderer getRenderer() {
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
			int itemCount=modifier.getItemCount(); 
			
			int maxModifier = modifier.getParent().getMaxQuantity();
			if(modifier.getModifierType()==TicketItemModifier.NORMAL_MODIFIER && maxModifier<=maxModifier){
				return false; 
			}
			modifier.setItemCount(++itemCount);
			repaint();
			
			modifierStateChangeListener.modifierStateChanged();
			
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
			
			modifierStateChangeListener.modifierStateChanged();
			
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
		int addTicketItem = model.addTicketItem(ticketItem);
		
		int actualRowCount = addTicketItem;
		selectionModel.addSelectionInterval(actualRowCount, actualRowCount);
		Rectangle cellRect = getCellRect(actualRowCount, 0, false);
		scrollRectToVisible(cellRect);
	}
	
	public void addModifierItem(TicketItem ticketItem){
		int addModifierItem= model.addTicketItem(ticketItem); 
		
		int actualRowCount = addModifierItem;
		selectionModel.addSelectionInterval(actualRowCount, actualRowCount);
		Rectangle cellRect = getCellRect(actualRowCount, 0, false);
		scrollRectToVisible(cellRect);
	}

	public Object deleteSelectedItem() {
		int selectedRow = getSelectedRow();
		Object delete = model.delete(selectedRow);
		
		if(delete instanceof TicketItemModifier) {
			if(modifierStateChangeListener != null) {
				TicketItemModifier modifier = (TicketItemModifier) delete;
				modifier.setModifierType(TicketItemModifier.MODIFIER_NOT_INITIALIZED);
				modifier.setItemCount(0);
				modifierStateChangeListener.updateView(modifier);
			}
		}
		return delete;
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

	public ModifierStateChangeListener getModifierStateChangeListener() {
		return modifierStateChangeListener;
	}

	public void setModifierStateChangeListener(ModifierStateChangeListener modifierStateChangeListener) {
		this.modifierStateChangeListener = modifierStateChangeListener;
	}
}