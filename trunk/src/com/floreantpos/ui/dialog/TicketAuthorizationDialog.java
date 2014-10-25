package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.actions.ActionCommand;
import com.floreantpos.actions.CloseDialogAction;
import com.floreantpos.main.Application;
import com.floreantpos.model.CardReader;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.TransactionListView;
import com.floreantpos.ui.views.payment.AuthorizeDotNetProcessor;
import com.floreantpos.ui.views.payment.PaymentProcessWaitDialog;

public class TicketAuthorizationDialog extends POSDialog {
	private TransactionListView listView = new TransactionListView();

	public TicketAuthorizationDialog(JDialog parent) {
		super(parent, true);

		init();
	}
	
	public TicketAuthorizationDialog(JFrame parent) {
		super(parent, true);
		
		init();
	}

	private void init() {
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
		listView.setTransactions(PosTransactionDAO.getInstance().findUnauthorizedTransactions());
	}

	class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ActionCommand command = ActionCommand.valueOf(e.getActionCommand());

			try {
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
			} catch (Exception e2) {
				POSMessageDialog.showError(TicketAuthorizationDialog.this, e2.getMessage(), e2);
			}
		}
		
		private boolean confirmAuthorize(PosTransaction transaction) {
			Double amount = transaction.getAmount();
			String message = "Transaction with amount " + Application.getCurrencySymbol() +	amount + " will be authorized.";
			int option = JOptionPane.showConfirmDialog(TicketAuthorizationDialog.this, message, "Confirm", JOptionPane.OK_CANCEL_OPTION);
			if(option == JOptionPane.OK_OPTION) {
				return true;
			}
			
			return false;
		}

		private void doAuthorize() {
			PosTransaction transaction = listView.getFirstSelectedTransaction();

			if (transaction == null) {
				return;
			}

			String cardEntryType = transaction.getCardReader();
			if (StringUtils.isEmpty(cardEntryType)) {
				POSMessageDialog.showError("No input information found for card. The record may be broken.");
				return;
			}
			
			if(!confirmAuthorize(transaction)) {
				return;
			}

			CardReader cardReader = CardReader.valueOf(cardEntryType);

			switch (cardReader) {
				case SWIPE:
				case MANUAL:
					authorizeSwipeCard(transaction);
					break;

				case EXTERNAL_TERMINAL:
					transaction.setCaptured(true);
					PosTransactionDAO.getInstance().saveOrUpdate(transaction);
					POSMessageDialog.showMessage("Authorized.");
					updateTransactiontList();
					break;

				default:
					break;
			}
		}

		private void authorizeSwipeCard(PosTransaction transaction) {
			PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(TicketAuthorizationDialog.this);
			waitDialog.setVisible(true);

			try {
				double authorizedAmount = transaction.calculateAuthorizeAmount();
				double totalAmount = transaction.getAmount();

				if (totalAmount > authorizedAmount) {
					AuthorizeDotNetProcessor.voidAmount(transaction);
					AuthorizeDotNetProcessor.captureNewAmount(transaction);

					transaction.setCaptured(true);

					PosTransactionDAO.getInstance().saveOrUpdate(transaction);

					waitDialog.setVisible(false);
				}
				else {
					AuthorizeDotNetProcessor.captureAuthorizedAmount(transaction);

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

			Ticket ticket = TicketDAO.getInstance().loadFullTicket(transaction.getTicket().getId());
			Set<PosTransaction> transactions = ticket.getTransactions();
			for (PosTransaction posTransaction : transactions) {
				if (transaction.getId().equals(posTransaction.getId())) {
					transaction = posTransaction;
					break;
				}
			}

			final double oldTipsAmount = transaction.getTipsAmount();
			final double newTipsAmount = NumberSelectionDialog2.show(TicketAuthorizationDialog.this, "Enter tips amount", oldTipsAmount);

			if (Double.isNaN(newTipsAmount))
				return;

			transaction.setTipsAmount(newTipsAmount);
			transaction.setAmount(transaction.getAmount() - oldTipsAmount + newTipsAmount);

			if (ticket.hasGratuity()) {
				double ticketTipsAmount = ticket.getGratuity().getAmount();
				double ticketPaidAmount = ticket.getPaidAmount();

				double newTicketTipsAmount = ticketTipsAmount - oldTipsAmount + newTipsAmount;
				double newTicketPaidAmount = ticketPaidAmount - oldTipsAmount + newTipsAmount;

				ticket.setGratuityAmount(newTicketTipsAmount);
				ticket.setPaidAmount(newTicketPaidAmount);
			}
			else {
				ticket.setGratuityAmount(newTipsAmount);
				ticket.setPaidAmount(ticket.getPaidAmount() + newTipsAmount);
			}

			ticket.calculatePrice();

			TicketDAO.getInstance().saveOrUpdate(ticket);
			updateTransactiontList();
		}

	}
}
