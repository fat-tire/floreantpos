package com.floreantpos.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.SettleTicketView;

public class SettleTicketAction extends AbstractAction {

	private Ticket ticket;

	public SettleTicketAction(Ticket ticket) {
		this.ticket = ticket;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public boolean execute() {
		ticket = TicketDAO.getInstance().initializeTicket(ticket);

		if (ticket.isPaid()) {
			POSMessageDialog.showError("Ticket is already settled");
			return false;
		}

		SettleTicketView posDialog = new SettleTicketView();
		posDialog.setCurrentTicket(ticket);
		posDialog.setSize(800, 600);
		posDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		posDialog.open();
		
		return !posDialog.isCanceled();
	}

}
