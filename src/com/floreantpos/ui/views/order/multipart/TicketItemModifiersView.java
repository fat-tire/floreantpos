package com.floreantpos.ui.views.order.multipart;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.report.AbstractReportDataSource;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.util.NumberUtil;

public class TicketItemModifiersView extends TransparentPanel {

	private TicketItem ticketItem;
	private TicketItemViewerModel ticketItemViewerModel;
	private JTable table;

	public TicketItemModifiersView() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		ticketItemViewerModel = new TicketItemViewerModel();
		table = new JTable();
		table.setModel(ticketItemViewerModel);
		table.setRowHeight(30);

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
	}

	public TicketItemViewerModel getTicketItemViewerModel() {
		return ticketItemViewerModel;
	}

	class TicketItemViewerModel extends AbstractReportDataSource {

		public TicketItemViewerModel() {
			super(new String[] { "Item", "Price" });
		}

		@Override
		public List getRows() {
			List list = new ArrayList<>();
			list.add(ticketItem);
			ticketItem.calculatePrice();
			if (ticketItem.getTicketItemModifiers() != null) {
				list.addAll(ticketItem.getTicketItemModifiers());
			}

			return list;
		}

		/*public void addItem(ITicketItem ticketItem) {

			getRows().add(ticketItem);

			for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
				ITicketItem item = (ITicketItem) iterator.next();
				if (item instanceof TicketItem) {
					((TicketItem) item).calculatePrice();
				}

			}

			fireTableDataChanged();
		}

		public void updateItem(ITicketItem ticketItem) {

			for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
				ITicketItem item = (ITicketItem) iterator.next();
				if (item instanceof TicketItem) {
					if (item == ticketItem) {
						item = ticketItem;
						((TicketItem) item).calculatePrice();
					}
				}

			}

			fireTableDataChanged();
		}

		public void updateModifier(ITicketItem ticketItem) {

			for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
				ITicketItem item = (ITicketItem) iterator.next();
				if (item instanceof TicketItemModifier) {
					if (item == ticketItem) {
						item = ticketItem;
					}
				}

			}

			fireTableDataChanged();
		}

		public void removeModifier(ITicketItem ticketItem) {

			getRows().add(ticketItem);

			for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
				ITicketItem item = (ITicketItem) iterator.next();
				if (item instanceof TicketItem) {
					((TicketItem) item).calculatePrice();
				}

			}

			fireTableDataChanged();
		}*/

		public void updateView() {
			fireTableDataChanged();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			ITicketItem item = (ITicketItem) getRows().get(rowIndex);

			switch (columnIndex) {
				case 0:
					if (item instanceof TicketItem) {
						return ((TicketItem) item).getName();
					}
					return item.getNameDisplay();

				case 1:
					Double total = null;
					if (item instanceof TicketItem) {
						total = item.getSubTotalAmountDisplay();
						return NumberUtil.formatNumber(total);
					}
					return null;

			}

			return null;
		}

	}

	public TicketItem getTicketItem() {
		return ticketItem;
	}

	public void setTicketItem(TicketItem ticketItem) {
		this.ticketItem = ticketItem;
		ticketItemViewerModel.setRows(ticketItem.getTicketItemModifiers());
	}

	public JTable getTable() {
		return table;
	}

}
