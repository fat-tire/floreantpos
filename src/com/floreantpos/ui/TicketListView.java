package com.floreantpos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.Customer;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketStatus;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class TicketListView extends JPanel {
	private JXTable table;
	private TicketListTableModel tableModel;

	public TicketListView() {
		table = new TicketListTable();
		table.setSortable(false);
		table.setModel(tableModel = new TicketListTableModel());
		table.setRowHeight(40);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setGridColor(Color.LIGHT_GRAY);
		
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(20);
		columnModel.getColumn(1).setPreferredWidth(20);
		columnModel.getColumn(2).setPreferredWidth(200);
		columnModel.getColumn(3).setPreferredWidth(100);

		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setPreferredSize(new Dimension(30, 60));

		setLayout(new BorderLayout());

		add(scrollPane);
	}

	public void setTickets(List<Ticket> tickets) {
		tableModel.setRows(tickets);
	}

	public void addTicket(Ticket ticket) {
		tableModel.addItem(ticket);
	}

	public Ticket getSelectedTicket() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow < 0) {
			return null;
		}

		return (Ticket) tableModel.getRowData(selectedRow);
	}

	public List<Ticket> getSelectedTickets() {
		int[] selectedRows = table.getSelectedRows();

		ArrayList<Ticket> tickets = new ArrayList<Ticket>(selectedRows.length);

		for (int i = 0; i < selectedRows.length; i++) {
			Ticket ticket = (Ticket) tableModel.getRowData(selectedRows[i]);
			tickets.add(ticket);
		}
		
		return tickets;
	}

	// public void removeTicket(Ticket ticket) {
	// tableModel.
	// }

	private class TicketListTable extends JXTable {
		
		public TicketListTable() {
			setColumnControlVisible(true);
		}
		
		@Override
		public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
			ListSelectionModel selectionModel = getSelectionModel();
			boolean selected = selectionModel.isSelectedIndex(rowIndex);
			if (selected) {
				selectionModel.removeSelectionInterval(rowIndex, rowIndex);
			}
			else {
				selectionModel.addSelectionInterval(rowIndex, rowIndex);
			}
		}
	}

	private class TicketListTableModel extends ListTableModel {
		public TicketListTableModel() {
			super(new String[] { POSConstants.ID, "TBL", POSConstants.SERVER, POSConstants.CREATED, POSConstants.CUSTOMER,
					POSConstants.TICKET_DELIVERY_DATE, POSConstants.TICKET_TYPE, "STATUS", POSConstants.TOTAL, POSConstants.DUE });
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			Ticket ticket = (Ticket) rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return Integer.valueOf(ticket.getId());

			case 1:
				return ticket.getTableNumber();

			case 2:
					User owner = ticket.getOwner();
					return owner.getFirstName();

			case 3:
				return ticket.getCreateDate();

			case 4:
				Customer customer = ticket.getCustomer();
				if (customer != null) {
					return ticket.getCustomer().getTelephoneNo();
				}

				return "Guest";

			case 5:
				return ticket.getDeliveryDate();

			case 6:
				return ticket.getType();
				
			case 7:
				if(ticket.getType() == TicketType.PICKUP) {
					return "Will pickup";
				}
				else if(ticket.getType() == TicketType.HOME_DELIVERY) {
					if(ticket.getAssignedDriver() == null) {
						return "Driver not assigned";
					}
					return "Driver assigned";
				}
				else if(ticket.getType() == TicketType.DRIVE_THRU) {
					return "Not delivered";
				}
				
				if(ticket.isPaid()) {
					if(ticket.getStatus() != null) {
						return TicketStatus.valueOf(ticket.getStatus()).toString();
					}
					return "PAID";
				}
				
				return "OPEN";

			case 8:
				return ticket.getTotalAmount();

			case 9:
				return ticket.getDueAmount();

			}

			return null;
		}

	}
	
	public Ticket getFirstSelectedTicket() {
		List<Ticket> selectedTickets = getSelectedTickets();

		if (selectedTickets.size() == 0 || selectedTickets.size() > 1) {
			POSMessageDialog.showMessage("Please select a ticket");
			return null;
		}

		Ticket ticket = selectedTickets.get(0);

		return ticket;
	}
	
	public int getFirstSelectedTicketId() {
		Ticket ticket = getFirstSelectedTicket();
		if(ticket == null) {
			return -1;
		}
		
		return ticket.getId();
	}

	public JXTable getTable() {
		return table;
	}
}
