package com.floreantpos.ui.views.order.multipart;

import java.util.ArrayList;
import java.util.List;

import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.TicketItem;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.util.NumberUtil;

public class PizzaTicketItemTableModel extends ListTableModel<ITicketItem> {
	private TicketItem ticketItem;

	public PizzaTicketItemTableModel() {
		super(new String[] { "Item", "Price" });
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ITicketItem item = (ITicketItem) rows.get(rowIndex);
		if (item instanceof TicketItem) {
			((TicketItem) item).calculatePrice();
		}

		switch (columnIndex) {
			case 0:
				if (item instanceof TicketItem) {
					return ((TicketItem) item).getName();
				}
				return " " + item.getNameDisplay();

			case 1:
				Double total = null;
				if (item instanceof TicketItem) {
					total = item.getSubTotalAmountDisplay();
					return NumberUtil.roundToTwoDigit(total);
				}
				return null;

		}

		return null;
	}

	public void setTicketItem(TicketItem ticketItem) {
		this.ticketItem = ticketItem;
	}

	public void updateView() {
		List<ITicketItem> list = new ArrayList<ITicketItem>();
		list.add(ticketItem);
		ticketItem.calculatePrice();
		if (ticketItem.getTicketItemModifiers() != null) {
			list.addAll(ticketItem.getTicketItemModifiers());
		}
		setRows(list);
		fireTableDataChanged();
	}
}
