package com.floreantpos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.ColumnControlButton;
import org.jdesktop.swingx.table.TableColumnModelExt;

import com.floreantpos.ITicketList;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.DataUpdateInfo;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.DataUpdateInfoDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PaginatedTableModel;
import com.floreantpos.swing.PosBlinkButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.PosGuiUtil;

public class TicketListView extends JPanel implements ITicketList {
	private OrderFilterPanel orderFiltersPanel;
	private JXTable table;
	private TicketListTableModel tableModel;
	private PosBlinkButton btnRefresh;
	private PosButton btnPrevious;
	private PosButton btnNext;

	private ArrayList<TicketListUpdateListener> ticketUpdateListenerList = new ArrayList();

	private Date lastUpdateTime;
	private Timer lastUpateCheckTimer = new Timer(5 * 1000, new TaskLastUpdateCheck());

	private POSToggleButton btnOrderFilters;

	public TicketListView() {
		setLayout(new BorderLayout());

		orderFiltersPanel = new OrderFilterPanel(this);
		add(orderFiltersPanel, BorderLayout.NORTH);

		createTicketTable();

		updateTicketList();

		updateButtonStatus();

	}

	private void createTicketTable() {
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

		createScrollPane();
	}

	private void createScrollPane() {
		btnOrderFilters = new POSToggleButton();
		btnOrderFilters.setText("<html>" + Messages.getString("SwitchboardView.2") + "</html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		btnRefresh = new PosBlinkButton(Messages.getString("TicketListView.3")); //$NON-NLS-1$
		btnPrevious = new PosButton(Messages.getString("TicketListView.4")); //$NON-NLS-1$
		btnNext = new PosButton(Messages.getString("TicketListView.5")); //$NON-NLS-1$

		createActionHandlers();

		PosScrollPane scrollPane = new PosScrollPane(table, PosScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, PosScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel topButtonPanel = new JPanel(new MigLayout("ins 0", "grow", ""));
		ColumnControlButton controlButton = new ColumnControlButton(table);
		topButtonPanel.add(controlButton, "h 40!, grow, wrap");
		topButtonPanel.add(btnRefresh, "h 40!, grow, wrap");
		topButtonPanel.add(btnPrevious, "h 40!, grow, wrap");

		JPanel downButtonPanel = new JPanel(new MigLayout("ins 0", "grow", ""));
		downButtonPanel.add(btnNext, "h 40!, grow, wrap");
		downButtonPanel.add(btnOrderFilters, "h 40!, grow, wrap");

		JPanel tableButtonPanel = new JPanel(new BorderLayout());
		tableButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
		tableButtonPanel.setPreferredSize(new Dimension(80, 0));
		tableButtonPanel.add(topButtonPanel, BorderLayout.NORTH);
		tableButtonPanel.add(downButtonPanel, BorderLayout.SOUTH);
		tableButtonPanel.add(scrollPane.getVerticalScrollBar());

		add(scrollPane);
		add(tableButtonPanel, BorderLayout.EAST);

	}

	public void createActionHandlers() {
		btnPrevious.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableModel.hasPrevious()) {
					List<Ticket> tickets = TicketDAO.getInstance().findPreviousTickets(tableModel);
					tableModel.setRows(tickets);
				}
				updateButtonStatus();

			}
		});

		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableModel.hasNext()) {
					List<Ticket> tickets = TicketDAO.getInstance().findNextTickets(tableModel);
					tableModel.setRows(tickets);
				}
				updateButtonStatus();
			}
		});

		btnRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getTableModel().setCurrentRowIndex(0);
				updateTicketList();
				updateButtonStatus();

			}
		});

		btnOrderFilters.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				orderFiltersPanel.setCollapsed(!orderFiltersPanel.isCollapsed());
			}
		});

	}

	public void updateButtonStatus() {
		btnNext.setEnabled(tableModel.hasNext());
		btnPrevious.setEnabled(tableModel.hasPrevious());
	}

	public synchronized void updateTicketList() {
		lastUpateCheckTimer.stop();

		try {
			Application.getPosWindow().setGlassPaneVisible(true);

			TicketListTableModel ticketListTableModel = getTableModel();

			List<Ticket> tickets = TicketDAO.getInstance().findTickets(ticketListTableModel);

			setTickets(tickets);

			btnRefresh.setBlinking(false);

			for (int i = 0; i < ticketUpdateListenerList.size(); i++) {
				TicketListUpdateListener listener = ticketUpdateListenerList.get(i);
				listener.ticketListUpdated();
			}

		} catch (Exception e) {
			POSMessageDialog.showError(this, Messages.getString("SwitchboardView.19"), e); //$NON-NLS-1$
		} finally {
			Application.getPosWindow().setGlassPaneVisible(false);
		}

		try {

			DataUpdateInfo lastUpdateInfo = DataUpdateInfoDAO.getLastUpdateInfo();
			this.lastUpdateTime = new Date(lastUpdateInfo.getLastUpdateTime().getTime());

		} catch (Exception e) {
			POSMessageDialog.showError(this, Messages.getString("SwitchboardView.20"), e); //$NON-NLS-1$
		}

		lastUpateCheckTimer.restart();
	}

	public void addTicketListUpateListener(TicketListUpdateListener l) {
		ticketUpdateListenerList.add(l);
	}

	//
	private class TaskLastUpdateCheck implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				if (PosGuiUtil.isModalDialogShowing()) {
					return;
				}

				lastUpateCheckTimer.stop();

				DataUpdateInfo lastUpdateInfo = DataUpdateInfoDAO.getLastUpdateInfo();

				if (lastUpdateInfo.getLastUpdateTime().after(lastUpdateTime)) {
					btnRefresh.setBlinking(true);
				}
			} finally {
				lastUpateCheckTimer.restart();
			}
		}
	}

	//

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

	private class TicketListTableModel extends PaginatedTableModel {
		public TicketListTableModel() {
			super(new String[] { POSConstants.TICKET_LIST_COLUMN_ID, POSConstants.TICKET_LIST_COLUMN_TABLE, POSConstants.TICKET_LIST_COLUMN_SERVER,
					POSConstants.TICKET_LIST_COLUMN_CREATE_DATE, POSConstants.TICKET_LIST_COLUMN_CUSTOMER, POSConstants.TICKET_LIST_COLUMN_DELIVERY_DATE,
					POSConstants.TICKET_LIST_COLUMN_TICKET_TYPE, POSConstants.TICKET_LIST_COLUMN_STATUS, POSConstants.TICKET_LIST_COLUMN_TOTAL,
					POSConstants.TICKET_LIST_COLUMN_DUE });

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

					return Messages.getString("TicketListView.6"); //$NON-NLS-1$

				case 5:
					return ticket.getDeliveryDate();

				case 6:
					return ticket.getType();

				case 7:
					String status = ""; //$NON-NLS-1$
					if (ticket.isPaid()) {
						status = Messages.getString("TicketListView.8"); //$NON-NLS-1$
					}
					else {
						status = Messages.getString("TicketListView.9"); //$NON-NLS-1$
					}

					if (ticket.getType() == OrderType.HOME_DELIVERY) {
						if (ticket.getAssignedDriver() == null) {
							status += Messages.getString("TicketListView.10"); //$NON-NLS-1$
						}

						status += Messages.getString("TicketListView.11"); //$NON-NLS-1$
					}

					if (ticket.isVoided()) {
						status = Messages.getString("TicketListView.12"); //$NON-NLS-1$
					}
					else if (ticket.isClosed()) {
						status += Messages.getString("TicketListView.13"); //$NON-NLS-1$
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
			POSMessageDialog.showMessage(Messages.getString("TicketListView.14")); //$NON-NLS-1$
			return null;
		}

		Ticket ticket = selectedTickets.get(0);

		return ticket;
	}

	public int getFirstSelectedTicketId() {
		Ticket ticket = getFirstSelectedTicket();
		if (ticket == null) {
			return -1;
		}

		return ticket.getId();
	}

	public JXTable getTable() {
		return table;
	}

	public TicketListTableModel getTableModel() {
		return tableModel;
	}

	public void setCurrentRowIndexZero() {
		getTableModel().setCurrentRowIndex(0);

	}

	public void setAutoUpdateCheck(boolean check) {
		if (check) {
			lastUpateCheckTimer.restart();
		}
		else {
			lastUpateCheckTimer.stop();
		}
	}
}