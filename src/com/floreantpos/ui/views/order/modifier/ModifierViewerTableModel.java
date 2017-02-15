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

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.Messages;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;

public class ModifierViewerTableModel extends AbstractTableModel {
	protected TicketItem ticketItem;

	private final List<ITicketItem> tableRows = new ArrayList<ITicketItem>();
	private boolean priceIncludesTax = false;

	protected String[] columnNames = {
			Messages.getString("TicketViewerTableModel.0"),/* Messages.getString("TicketViewerTableModel.1"), Messages.getString("TicketViewerTableModel.2"),*/Messages.getString("TicketViewerTableModel.3") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	private boolean forReciptPrint;
	private boolean printCookingInstructions;

	public ModifierViewerTableModel(TicketItem ticketItem) {
		setTicketItem(ticketItem);
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
		ITicketItem ticketItem = tableRows.get(rowIndex);

		if (ticketItem == null) {
			return null;
		}

		switch (columnIndex) {
			case 0:
				return ticketItem.getNameDisplay();

				/*case 1:
					return ticketItem.getUnitPriceDisplay();

				case 2:
					return ticketItem.getItemQuantityDisplay();*/

			case 1:
				return ticketItem.getSubTotalAmountWithoutModifiersDisplay();
		}

		return null;
	}

	private void calculateRows() {
		//TicketItemRowCreator.calculateTicketRows(ticket, tableRows);
		tableRows.clear();

		calculateRowsForModifiers();
		//if (addOnMode) {
		calculateRowsForAddOns();
		//}
		//else {

		//}
	}

	private void calculateRowsForAddOns() {
		List<TicketItemModifier> addOns = ticketItem.getAddOns();
		if (addOns == null) {
			return;
		}

		for (TicketItemModifier ticketItemModifier : addOns) {
			tableRows.add(ticketItemModifier);
		}
	}

	private void calculateRowsForModifiers() {
		//		List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
		//		if (ticketItemModifierGroups == null)
		//			return;
		//
		//		for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
		//			List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
		//			if (ticketItemModifiers != null) {
		//				for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
		//					tableRows.add(ticketItemModifier);
		//				}
		//			}
		//		}
		List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
		if (ticketItemModifiers == null) {
			return;
		}
		for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
			tableRows.add(ticketItemModifier);
		}
	}

	public void removeModifier(TicketItem parent, TicketItemModifier modifierToDelete) {
		//		TicketItemModifierGroup ticketItemModifierGroup = modifierToDelete.getParent();
		//		List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
		//
		//		for (Iterator iter = ticketItemModifiers.iterator(); iter.hasNext();) {
		//			TicketItemModifier modifier = (TicketItemModifier) iter.next();
		//			if (modifier.getItemId() == modifierToDelete.getItemId()) {
		//				iter.remove();
		//
		//				if (modifier.isPrintedToKitchen()) {
		//					ticket.addDeletedItems(modifier);
		//				}
		//
		//				calculateRows();
		//				fireTableDataChanged();
		//				return;
		//			}
		//		}
	}

	public Object delete(int index) {
		if (index < 0 || index >= tableRows.size())
			return null;

		TicketItemModifier ticketItemModifier = (TicketItemModifier) tableRows.remove(index);
		if (ticketItemModifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
			ticketItemModifier.getTicketItem().removeAddOn(ticketItemModifier);
		}
		else {
			//TicketItemModifierGroup ticketItemModifierGroup = ticketItemModifier.getParent();
			//ticketItemModifierGroup.removeTicketItemModifier(ticketItemModifier);
			ticketItem.removeTicketItemModifier(ticketItemModifier);
		}
		fireTableRowsDeleted(index, index);
		return ticketItemModifier;
	}

	public Object get(int index) {
		if (index < 0 || index >= tableRows.size())
			return null;

		return tableRows.get(index);
	}

	public TicketItem getTicketItem() {
		return ticketItem;
	}

	public void setTicketItem(TicketItem ticketItem) {
		this.ticketItem = ticketItem;

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
