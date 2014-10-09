package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.actions.ActionCommand;
import com.floreantpos.actions.CloseDialogAction;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.TransactionListView;
import com.floreantpos.ui.views.payment.AuthorizeDoNetProcessor;
import com.floreantpos.ui.views.payment.CardInput;
import com.floreantpos.ui.views.payment.PaymentProcessWaitDialog;

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
		ActionHandler actionHandler = new ActionHandler();

		buttonPanel.add(new PosButton(ActionCommand.EDIT_TIPS, actionHandler));
		buttonPanel.add(new PosButton(ActionCommand.AUTHORIZE, actionHandler));

		buttonPanel.add(new PosButton(new CloseDialogAction(this)));

		add(buttonPanel, BorderLayout.SOUTH);

		updateTransactiontList();
	}

	public void updateTransactiontList() {
		TicketDAO dao = TicketDAO.getInstance();
		List<Ticket> tickets = dao.findHoldTickets();

		List<PosTransaction> transactions = new ArrayList<PosTransaction>();

		for (Ticket ticket : tickets) {
			Ticket fullTicket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
			List<PosTransaction> transactionList = fullTicket.getTransactions();
			if (transactionList == null)
				continue;

			for (PosTransaction posTransaction : transactionList) {
				if (!posTransaction.isCaptured()) {
					transactions.add(posTransaction);
				}
			}
		}

		listView.setTransactions(transactions);
	}

	class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ActionCommand command = ActionCommand.valueOf(e.getActionCommand());

			switch (command) {
				case EDIT_TIPS:

					doEditTips();
					break;
					
				case AUTHORIZE:
					doAuthorize();
					break;

				default:
					break;
			}
		}

		private void doAuthorize() {
			PosTransaction transaction = listView.getFirstSelectedTransaction();

			if (transaction == null) {
				return;
			}

			String cardEntryType = transaction.getCardEntryType();
			if(StringUtils.isEmpty(cardEntryType)) {
				POSMessageDialog.showError("No input information found for card. The record may be broken.");
				return;
			}
			
			CardInput cardInput = CardInput.valueOf(cardEntryType);
			
			switch (cardInput) {
				case SWIPE:
					authorizeSwipeCard(transaction);
					break;

				default:
					break;
			}
		}
		
		private void authorizeSwipeCard(PosTransaction transaction) {
			PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(TicketAuthorizationDialog.this);
			waitDialog.setVisible(true);

			try {

				String transactionId = transaction.getCardTransactionId();

				double authorizedAmount = transaction.calculateAuthorizeAmount();
				double totalAmount = transaction.calculateTotalAmount();
				
				if (totalAmount > authorizedAmount) {
					AuthorizeDoNetProcessor.voidAmount(transactionId, authorizedAmount);

					String authCode = AuthorizeDoNetProcessor.captureAmount(transactionId, totalAmount);
					
					transaction.setAuthorizationCode(authCode);
					transaction.setCardTransactionId(null);
					transaction.setCaptured(true);
					
					PosTransactionDAO.getInstance().saveOrUpdate(transaction);

					waitDialog.setVisible(false);
					
					
				}
				else {
					String authCode = AuthorizeDoNetProcessor.captureAmount(transactionId, totalAmount);
					
					transaction.setAuthorizationCode(authCode);
					transaction.setCardTransactionId(null);
					transaction.setCaptured(true);
					
					PosTransactionDAO.getInstance().saveOrUpdate(transaction);

					waitDialog.setVisible(false);
				}

				POSMessageDialog.showMessage("Authorized.");
				updateTransactiontList();
			} catch (Exception e) {
				POSMessageDialog.showError(e.getMessage(), e);
			} finally {
				waitDialog.setVisible(false);
			}
		}

		private void doEditTips() {
			PosTransaction transaction = listView.getFirstSelectedTransaction();

			if (transaction == null) {
				return;
			}

			Ticket ticket = transaction.getTicket();

			double tipsAmount = transaction.getTipsAmount();
			tipsAmount = NumberSelectionDialog2.show(TicketAuthorizationDialog.this, "Enter tips amount", tipsAmount);

			if(tipsAmount == Double.NaN) return;
			
			ticket.subtractFromGratuity(transaction.getTipsAmount());
			ticket.addGratuityAmount(tipsAmount);
			ticket.setPaidAmount(ticket.getPaidAmount() + tipsAmount);
			transaction.setTipsAmount(tipsAmount);

			ticket.calculatePrice();

			TicketDAO.getInstance().saveOrUpdate(ticket);
			updateTransactiontList();
		}

	}
}
