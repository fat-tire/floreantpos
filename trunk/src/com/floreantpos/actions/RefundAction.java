package com.floreantpos.actions;

import com.floreantpos.ITicketList;
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
				if(ticketId == -1) return;
				
				ticket = TicketService.getTicket(ticketId);
			}
			
			if(!ticket.isPaid()) {
				POSMessageDialog.showError(Application.getPosWindow(), "Ticket is not paid.");
				return;
			}
			
			if(ticket.isRefunded()) {
				POSMessageDialog.showError(Application.getPosWindow(), "Ticket is already refunded.");
				return;
			}
			
			Double paidAmount = ticket.getPaidAmount();
			
			String message = Application.getCurrencySymbol() + paidAmount + " will be refunded.";
			
			ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
			
			message = "<html>" +
					"Ticket #" + ticket.getId() + "<br/>Total paid " + ticket.getPaidAmount();
			
			if(ticket.getGratuity() != null) {
				message += ", including tips " + ticket.getGratuity().getAmount();
			}
			
			message += "</html>";
			
			double refundAmount = NumberSelectionDialog2.takeDoubleInput(message, "Enter refund amount", paidAmount);
			if(Double.isNaN(refundAmount)) {
				return;
			}
			
			if(refundAmount > paidAmount) {
				POSMessageDialog.showError(Application.getPosWindow(), "Refund amount cannot be greater than paid amount");
				return;
			}

			PosTransactionService.getInstance().refundTicket(ticket, refundAmount);
			
			POSMessageDialog.showMessage("Refunded " + Application.getCurrencySymbol() + refundAmount);
			
			ticketList.updateTicketList();
			
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

}
