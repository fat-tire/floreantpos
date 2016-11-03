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
package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;
import com.floreantpos.util.POSUtil;

public class TicketExplorer extends TransparentPanel {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, h:m a"); //$NON-NLS-1$

	private JXDatePicker fromDatePicker = UiUtil.getCurrentMonthStart();
	private JXDatePicker toDatePicker = UiUtil.getCurrentMonthEnd();
	private JButton btnGo = new JButton(com.floreantpos.POSConstants.GO);

	private JXTable table;
	private TicketExplorerTableModel tableModel;
	private List<Ticket> tickets;

	public TicketExplorer() {
		setLayout(new BorderLayout());

		table = new JXTable();
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		tableModel = new TicketExplorerTableModel();

		table.setModel(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setRowHeight(25);

		addTopPanel();
		add(new JScrollPane(table), BorderLayout.CENTER);
		addButtonPanel();

		refresh();
	}

	private void addTopPanel() {
		JPanel topPanel = new JPanel(new MigLayout());

		btnGo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					refresh();
				} catch (Exception e1) {
					BOMessageDialog.showError(TicketExplorer.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}

		});

		topPanel.add(new JLabel(com.floreantpos.POSConstants.FROM), "grow"); //$NON-NLS-1$
		topPanel.add(fromDatePicker, "gapright 10"); //$NON-NLS-1$
		topPanel.add(new JLabel(com.floreantpos.POSConstants.TO), "grow"); //$NON-NLS-1$
		topPanel.add(toDatePicker);
		topPanel.add(btnGo, "width 60!"); //$NON-NLS-1$
		add(topPanel, BorderLayout.NORTH);
	}

	private void addButtonPanel() {
		JButton btnVoid = new JButton(POSConstants.DELETE);
		btnVoid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0) {
						POSMessageDialog.showMessage(POSUtil.getBackOfficeWindow(), POSConstants.SELECT_ONE_TICKET_TO_VOID);
						return;
					}

					index = table.convertRowIndexToModel(index);
					List<Ticket> tickets = new ArrayList<Ticket>();
					Ticket ticket = tableModel.getRows().get(index);
					tickets.add(ticket);

					if (POSMessageDialog.showYesNoQuestionDialog(TicketExplorer.this, POSConstants.CONFIRM_DELETE, POSConstants.DELETE) != JOptionPane.YES_OPTION) {
						return;
					}

					TicketDAO.getInstance().deleteTickets(tickets);

					tableModel.deleteItem(index);
					table.repaint();
				} catch (Exception x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		JButton btnVoidAll = new JButton(POSConstants.DELETE_ALL);
		btnVoidAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<Ticket> tickets = tableModel.getRows();

					if (tickets.isEmpty()) {
						return;
					}

					if (POSMessageDialog.showYesNoQuestionDialog(TicketExplorer.this, POSConstants.CONFIRM_DELETE, POSConstants.DELETE_ALL) != JOptionPane.YES_OPTION) {
						return;
					}

					TicketDAO.getInstance().deleteTickets(tickets);
					refresh();

				} catch (Exception x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(btnVoid);
		panel.add(btnVoidAll);
		add(panel, BorderLayout.SOUTH);
	}

	class TicketExplorerTableModel extends ListTableModel<Ticket> {
		String[] columnNames = { POSConstants.ID, POSConstants.CREATED_BY.toUpperCase(), POSConstants.CREATE_TIME.toUpperCase(),
				POSConstants.SETTLE_TIME.toUpperCase(), POSConstants.SUBTOTAL.toUpperCase(), POSConstants.DISCOUNT.toUpperCase(),
				POSConstants.TAX.toUpperCase(), POSConstants.TOTAL, POSConstants.PAID, POSConstants.VOID };

		@Override
		public String[] getColumnNames() {
			return columnNames;
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		public Object getValueAt(int rowIndex, int columnIndex) {

			Ticket ticket = (Ticket) rows.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return String.valueOf(ticket.getId());

				case 1:
					return ticket.getOwner().toString();

				case 2:
					return dateFormat.format(ticket.getCreateDate());

				case 3:
					if (ticket.getClosingDate() != null) {
						return dateFormat.format(ticket.getClosingDate());
					}
					return ""; //$NON-NLS-1$

				case 4:
					return Double.valueOf(ticket.getSubtotalAmount());

				case 5:
					return Double.valueOf(ticket.getDiscountAmount());

				case 6:
					return Double.valueOf(ticket.getTaxAmount());

				case 7:
					return Double.valueOf(ticket.getTotalAmount());

				case 8:
					return Boolean.valueOf(ticket.isPaid());

				case 9:
					return Boolean.valueOf(ticket.isVoided());
			}
			return null;
		}
	}

	private void refresh() {
		if (tableModel.getRows() != null) {
			tableModel.getRows().clear();
		}

		Date fromDate = fromDatePicker.getDate();
		Date toDate = toDatePicker.getDate();

		fromDate = DateUtil.startOfDay(fromDate);
		toDate = DateUtil.endOfDay(toDate);

		TicketDAO dao = new TicketDAO();
		tickets = dao.findClosedTickets(fromDate, toDate);
		tableModel.setRows(tickets);
		table.repaint();
	}
}
