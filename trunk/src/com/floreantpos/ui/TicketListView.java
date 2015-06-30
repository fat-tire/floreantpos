package com.floreantpos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnModelExt;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class TicketListView extends JPanel {
	private JXTable table;
	private TicketListTableModel tableModel;

	public TicketListView() {
		table = new TicketListTable();
		table.setSortable(true);
		table.setColumnControlVisible(true);
		table.setModel(tableModel = new TicketListTableModel());
		table.setRowHeight(60);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setGridColor(Color.LIGHT_GRAY);
		table.getTableHeader().setPreferredSize(new Dimension(100, 40));
		
		TableColumnModelExt columnModel = (TableColumnModelExt) table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(30);
		columnModel.getColumn(1).setPreferredWidth(20);
		columnModel.getColumn(2).setPreferredWidth(100);
		columnModel.getColumn(3).setPreferredWidth(100);
		
		columnModel.getColumnExt(5).setVisible(false);
		columnModel.getColumnExt(4).setVisible(false);
		columnModel.getColumnExt(3).setVisible(false);
		columnModel.getColumnExt(2).setVisible(false);
		columnModel.getColumnExt(1).setVisible(false);
		

		PosScrollPane scrollPane = new PosScrollPane(table, PosScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, PosScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		setLayout(new BorderLayout());

		add(scrollPane);
		
//		JPanel statusPanel = new JPanel(new MigLayout("ins 5 2 5 0", "[fill, grow][]10[]10[]", ""));
//		
//		JLabel lblStatus = new JLabel("Fount 1000 ticket, showing 1 to 100");
//		statusPanel.add(lblStatus, "grow");
//		
//		PosButton btnRefresh = new PosButton("REFRESH");
//		statusPanel.add(btnRefresh, "align trailing, h 40!");
//
//		PosButton btnPrev = new PosButton("PREV");
//		statusPanel.add(btnPrev, "align trailing, h 40!");
//		
//		PosButton btnNext = new PosButton("NEXT");
//		statusPanel.add(btnNext, "align trailing, h 40!");
//		
//		statusPanel.add(new JSeparator(JSeparator.HORIZONTAL), "newline, grow, span");
//		
//		add(statusPanel, BorderLayout.SOUTH);
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

		return (Ticket) tableModel.getRowData(table.convertRowIndexToModel(selectedRow));
	}

	public List<Ticket> getSelectedTickets() {
		int[] selectedRows = table.getSelectedRows();

		ArrayList<Ticket> tickets = new ArrayList<Ticket>(selectedRows.length);

		for (int i = 0; i < selectedRows.length; i++) {
			Ticket ticket = (Ticket) tableModel.getRowData(table.convertRowIndexToModel(selectedRows[i]));
			tickets.add(ticket);
		}
		
		return tickets;
	}

	private class TicketListTable extends JXTable {
		
		public TicketListTable() {
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
			super(new String[] { POSConstants.TICKET_LIST_COLUMN_ID, POSConstants.TICKET_LIST_COLUMN_TABLE,
					POSConstants.TICKET_LIST_COLUMN_SERVER, POSConstants.TICKET_LIST_COLUMN_CREATE_DATE, 
					POSConstants.TICKET_LIST_COLUMN_CUSTOMER, POSConstants.TICKET_LIST_COLUMN_DELIVERY_DATE,
					POSConstants.TICKET_LIST_COLUMN_TICKET_TYPE, POSConstants.TICKET_LIST_COLUMN_STATUS, 
					POSConstants.TICKET_LIST_COLUMN_TOTAL, POSConstants.TICKET_LIST_COLUMN_DUE });
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			Ticket ticket = (Ticket) rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return Integer.valueOf(ticket.getId());

			case 1:
				return ticket.getTableNumbers();

			case 2:
					User owner = ticket.getOwner();
					return owner.getFirstName();

			case 3:
				return ticket.getCreateDate();

			case 4:
				String customerPhone = ticket.getProperty(Ticket.CUSTOMER_PHONE);
				
				if (customerPhone != null) {
					return customerPhone;
				}

				return "Guest";

			case 5:
				return ticket.getDeliveryDate();

			case 6:
				return ticket.getType();
				
			case 7:
				String status = "";
				if(ticket.isPaid()) {
					status = "PAID";
				}
				else {
					status = "OPEN";
				}
				
				if(ticket.getType() == OrderType.HOME_DELIVERY) {
					if(ticket.getAssignedDriver() == null) {
						status += " (DRIVER NOT ASSIGNED)";
					}
					
					status += " (DRIVER ASSIGNED)";
				}
				
				if(ticket.isVoided()) {
					status = "VOID";
				}
				else if(ticket.isClosed()) {
					status += " (CLOSED)";
				}
				
				return status;

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
