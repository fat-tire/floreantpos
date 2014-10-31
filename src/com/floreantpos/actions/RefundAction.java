package com.floreantpos.actions;

import javax.swing.JOptionPane;

import com.floreantpos.ITicketList;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.services.PosTransactionService;
import com.floreantpos.services.TicketService;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class RefundAction extends PosAction {
	private ITicketList ticketList;

	public RefundAction(ITicketList ticketList) {
		super("REFUND", UserPermission.REFUND);

		this.ticketList = ticketList;
	}

	@Override
	public void execute() {
		try {
			Ticket ticket = ticketList.getSelectedTicket();

			if (ticket == null) {
				int ticketId = NumberSelectionDialog2.takeIntInput("Enter or scan ticket id");
				ticket = TicketService.getTicket(ticketId);
			}
			
			if(!ticket.isPaid()) {
				POSMessageDialog.showError("Ticket is not paid");
				return;
			}
			
			Double paidAmount = ticket.getPaidAmount();
			
			String message = Application.getCurrencySymbol() + paidAmount + " will be refunded.";
			
			int option = JOptionPane.showOptionDialog(Application.getPosWindow(), message, POSConstants.CONFIRM,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (option != JOptionPane.OK_OPTION) {
				return;
			}

			ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());

			PosTransactionService.getInstance().refundTicket(ticket);
			
			POSMessageDialog.showMessage("Done.");
			
			ticketList.updateTicketList();
			
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
		}
	}

}
