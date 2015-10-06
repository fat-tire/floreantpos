package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.OrderTypeFilter;
import com.floreantpos.model.PaymentStatusFilter;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.POSDialog;

public class GroupSettleTicketSelectionWindow extends POSDialog {
	private JXTable ticketList;
	private BeanTableModel<Ticket> tableModel;
	
	public GroupSettleTicketSelectionWindow() {
		super(Application.getPosWindow(), true);
		setTitle(Messages.getString("GroupSettleTicketSelectionWindow.0")); //$NON-NLS-1$
		
		createTicketList();
		
		JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
		contentPanel.add(new PosScrollPane(ticketList, PosScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, PosScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		
		JPanel buttonPanel = new JPanel();
		
		PosButton btnConfirm = new PosButton(Messages.getString("GroupSettleTicketSelectionWindow.1")); //$NON-NLS-1$
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(false);
				dispose();
			}
		});
		buttonPanel.add(btnConfirm);
		
		PosButton psbtnCancel = new PosButton(Messages.getString("GroupSettleTicketSelectionWindow.2")); //$NON-NLS-1$
		psbtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		buttonPanel.add(psbtnCancel);
		
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		add(contentPanel);
		
		setSize(800, 600);
	}

	private void createTicketList() {
		tableModel = new BeanTableModel<Ticket>(Ticket.class);
		tableModel.addColumn(POSConstants.TICKET_LIST_COLUMN_ID, "id"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TICKET_LIST_COLUMN_TICKET_TYPE, "ticketType"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TICKET_LIST_COLUMN_SERVER, "owner"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TICKET_LIST_COLUMN_TOTAL, "totalAmount"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TICKET_LIST_COLUMN_DUE, "dueAmount"); //$NON-NLS-1$
		
		ticketList = new JXTable(tableModel);
		ticketList.setSortable(true);
		ticketList.setColumnControlVisible(true);
		ticketList.setRowHeight(60);
		ticketList.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		ticketList.setDefaultRenderer(Object.class, new PosTableRenderer());
		ticketList.setGridColor(Color.LIGHT_GRAY);
		ticketList.getTableHeader().setPreferredSize(new Dimension(100, 40));
		
		User user = Application.getCurrentUser();

		TicketDAO dao = TicketDAO.getInstance();
		List<Ticket> tickets = null;
		
		PaymentStatusFilter paymentStatusFilter = TerminalConfig.getPaymentStatusFilter();
		OrderTypeFilter orderTypeFilter = TerminalConfig.getOrderTypeFilter();

		if (user.canViewAllOpenTickets()) {
			tickets = dao.findTickets(paymentStatusFilter, orderTypeFilter);
		}
		else {
			tickets = dao.findTicketsForUser(paymentStatusFilter, orderTypeFilter, user);
		}
		
		tableModel.addRows(tickets);
	}
}
