package com.floreantpos.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketStatus;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.TicketListView;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.TicketAuthorizer;

public class AuthorizeTicketAction extends AbstractAction {
	private TicketAuthorizer authorizer;
	
	public AuthorizeTicketAction(TicketAuthorizer authorizer) {
		super("AUTHORIZE");
		
		this.authorizer = authorizer;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		TicketListView ticketListView = authorizer.getTicketListView();
		
		try {
			int ticketId = ticketListView.getFirstSelectedTicketId();
			if (ticketId == -1) {
				return;
			}

			TicketDAO dao = TicketDAO.getInstance();
			Ticket ticket = dao.loadFullTicket(ticketId);
			Double totalAmount = ticket.getTotalAmount();

			ticket.setPaidAmount(totalAmount);
			ticket.calculatePrice();
			ticket.setStatus(TicketStatus.PAID.name());
			ticket.setPaid(true);
			ticket.setClosed(true);

			dao.saveOrUpdate(ticket);
			
			POSMessageDialog.showMessage("Done");
			authorizer.updateTicketList();
		} catch (Exception e2) {
			POSMessageDialog.showError(ticketListView, e2.getMessage(), e2);
		}
	}

}
