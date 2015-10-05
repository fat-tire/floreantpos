package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.actions.ActionCommand;
import com.floreantpos.actions.CloseDialogAction;
import com.floreantpos.config.CardConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.CardReader;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.TransactionListView;
import com.floreantpos.ui.views.payment.CardProcessor;
import com.floreantpos.ui.views.payment.PaymentProcessWaitDialog;

public class TicketAuthorizationDialog extends POSDialog {
	private TransactionListView listView = new TransactionListView();

	public TicketAuthorizationDialog(JDialog parent) {
		super();

		init();
	}

	public TicketAuthorizationDialog(JFrame parent) {
		super();

		init();
	}

	private void init() {
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("TicketAuthorizationDialog.0")); //$NON-NLS-1$
		add(titlePanel, BorderLayout.NORTH);

		listView.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(listView);

		JPanel buttonPanel = new JPanel();
		ActionHandler actionHandler = new ActionHandler();

		buttonPanel.add(new PosButton(ActionCommand.EDIT_TIPS, actionHandler));
		buttonPanel.add(new PosButton(ActionCommand.AUTHORIZE, actionHandler));
		buttonPanel.add(new PosButton(ActionCommand.AUTHORIZE_ALL, actionHandler));

		buttonPanel.add(new PosButton(new CloseDialogAction(this)));

		add(buttonPanel, BorderLayout.SOUTH);

		updateTransactiontList();
	}

	public void updateTransactiontList() {
		User owner = null;
		User currentUser = Application.getCurrentUser();
		if(!currentUser.hasPermission(UserPermission.VIEW_ALL_OPEN_TICKETS)) {
			owner = currentUser;
		}
		
		listView.setTransactions(PosTransactionDAO.getInstance().findUnauthorizedTransactions(owner));
	}

	private boolean confirmAuthorize(String message) {
		int option = JOptionPane.showConfirmDialog(TicketAuthorizationDialog.this, message, Messages.getString("TicketAuthorizationDialog.1"), JOptionPane.OK_CANCEL_OPTION); //$NON-NLS-1$
		if (option == JOptionPane.OK_OPTION) {
			return true;
		}

		return false;
	}

	private void doAuthorize() {
		List<PosTransaction> transactions = listView.getSelectedTransactions();

		if (transactions == null || transactions.size() == 0) {
			POSMessageDialog.showMessage(TicketAuthorizationDialog.this, Messages.getString("TicketAuthorizationDialog.2")); //$NON-NLS-1$
			return;
		}

		if (!confirmAuthorize(Messages.getString("TicketAuthorizationDialog.3"))) { //$NON-NLS-1$
			return;
		}

		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(TicketAuthorizationDialog.this);
		waitDialog.setVisible(true);

		try {

			for (PosTransaction transaction : transactions) {
				authorizeTransaction(transaction);
			}

			POSMessageDialog.showMessage(Application.getPosWindow(), Messages.getString("TicketAuthorizationDialog.4")); //$NON-NLS-1$
			updateTransactiontList();

		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		} finally {
			waitDialog.setVisible(false);
		}
	}

	public void doAuthorizeAll() {
		List<PosTransaction> transactions = listView.getAllTransactions();

		if (transactions == null || transactions.size() == 0) {
			POSMessageDialog.showMessage(TicketAuthorizationDialog.this, Messages.getString("TicketAuthorizationDialog.5")); //$NON-NLS-1$
			return;
		}

		if (!confirmAuthorize(Messages.getString("TicketAuthorizationDialog.6"))) { //$NON-NLS-1$
			return;
		}

		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(TicketAuthorizationDialog.this);
		waitDialog.setVisible(true);

		try {

			for (PosTransaction transaction : transactions) {
				authorizeTransaction(transaction);
			}

			POSMessageDialog.showMessage(Application.getPosWindow(), Messages.getString("TicketAuthorizationDialog.7")); //$NON-NLS-1$
			updateTransactiontList();

		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		} finally {
			waitDialog.setVisible(false);
		}
	}

	private void authorizeSwipeCard(PosTransaction transaction) throws Exception {
		double authorizedAmount = transaction.calculateAuthorizeAmount();
		double totalAmount = transaction.getAmount();

		CardProcessor cardProcessor = CardConfig.getMerchantGateway().getProcessor();
		
		if (totalAmount > authorizedAmount) {
			cardProcessor.voidAmount(transaction);
			cardProcessor.captureNewAmount(transaction);

			transaction.setCaptured(true);

			PosTransactionDAO.getInstance().saveOrUpdate(transaction);
		}
		else {
			cardProcessor.captureAuthorizedAmount(transaction);

			transaction.setCaptured(true);

			PosTransactionDAO.getInstance().saveOrUpdate(transaction);
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
		final double newTipsAmount = NumberSelectionDialog2.show(TicketAuthorizationDialog.this, Messages.getString("TicketAuthorizationDialog.8"), oldTipsAmount); //$NON-NLS-1$

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

	private void authorizeTransaction(PosTransaction transaction) throws Exception {
		String cardEntryType = transaction.getCardReader();
		if (StringUtils.isEmpty(cardEntryType)) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TicketAuthorizationDialog.9") + transaction.getId() + Messages.getString("TicketAuthorizationDialog.10")); //$NON-NLS-1$ //$NON-NLS-2$
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
				break;

			default:
				break;
		}
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

					case AUTHORIZE_ALL:
						doAuthorizeAll();

					default:
						break;
				}
			} catch (Exception e2) {
				POSMessageDialog.showError(TicketAuthorizationDialog.this, e2.getMessage(), e2);
			}
		}
	}
}
