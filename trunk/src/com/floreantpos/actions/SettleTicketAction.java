package com.floreantpos.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.payment.SettleTicketDialog;

public class SettleTicketAction extends AbstractAction {

	private int ticketId;

	public SettleTicketAction(int ticketId) {
		this.ticketId = ticketId;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public boolean execute() {
		Ticket ticket = TicketDAO.getInstance().loadFullTicket(ticketId);

		if (ticket.isPaid()) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("SettleTicketAction.0")); //$NON-NLS-1$
			return false;
		}

		SettleTicketDialog posDialog = new SettleTicketDialog();
		posDialog.setTicket(ticket);
		posDialog.setSize(900, 700);
		posDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		posDialog.open();
		
		return !posDialog.isCanceled();
	}

}
