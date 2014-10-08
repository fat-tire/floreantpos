package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.actions.AuthorizeTicketAction;
import com.floreantpos.actions.CloseDialogAction;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TicketListView;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.TransactionListView;
import com.floreantpos.util.TicketAuthorizer;

public class TicketAuthorizationDialog extends POSDialog {
	private TransactionListView listView = new TransactionListView();
	
	public TicketAuthorizationDialog(JFrame parent) {
		super(parent, true);
		
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Authorize tickets");
		add(titlePanel, BorderLayout.NORTH);
		
		listView.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		add(listView);
		
		JPanel buttonPanel = new JPanel();
//		PosButton authorizeButton = new PosButton(new AuthorizeTicketAction(this));
//		buttonPanel.add(authorizeButton);
		
		PosButton closeButton = new PosButton(new CloseDialogAction(this));
		buttonPanel.add(closeButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		updateTicketList();
	}

	public void updateTicketList() {
		TicketDAO dao = TicketDAO.getInstance();
		List<Ticket> tickets = dao.findHoldTickets();

		List<PosTransaction> transactions = new ArrayList<PosTransaction>();
		
		for (Ticket ticket : tickets) {
			Ticket fullTicket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
			List<PosTransaction> transactionList = fullTicket.getTransactions();
			if(transactionList == null) continue;
			
			for (PosTransaction posTransaction : transactionList) {
				if(!posTransaction.isCaptured()) {
					transactions.add(posTransaction);
				}
			}
		}
		
		listView.setTransactions(transactions);
	}

}
