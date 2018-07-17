/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.ui.views.payment;

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
import javax.swing.JTabbedPane;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.actions.ActionCommand;
import com.floreantpos.actions.CloseDialogAction;
import com.floreantpos.config.CardConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.TransactionListView;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

import net.miginfocom.swing.MigLayout;

public class AuthorizableTicketBrowser extends POSDialog {
	private TransactionListView authClosedListView = new TransactionListView();
	private TransactionListView authWaitingListView = new TransactionListView();
	private JTabbedPane tabbedPane;

	public AuthorizableTicketBrowser(JDialog parent) {
		super();

		init();
	}

	public AuthorizableTicketBrowser(JFrame parent) {
		super();

		init();
	}

	private void init() {
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("TicketAuthorizationDialog.0")); //$NON-NLS-1$
		add(titlePanel, BorderLayout.NORTH);

		tabbedPane = new JTabbedPane();
		JPanel authWaitingTab = new JPanel(new BorderLayout());

		authWaitingListView.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		authClosedListView.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		authWaitingTab.add(authWaitingListView);

		JPanel buttonPanel = new JPanel(new MigLayout("al center", "sg, fill", ""));
		ActionHandler actionHandler = new ActionHandler();
		buttonPanel.add(new PosButton(ActionCommand.EDIT_TIPS, actionHandler), "grow");
		buttonPanel.add(new PosButton(ActionCommand.SETTLE, actionHandler), "grow");
		buttonPanel.add(new PosButton(ActionCommand.SETTLE_ALL, actionHandler), "grow");
		buttonPanel.add(new PosButton(ActionCommand.VOID, actionHandler), "grow");
		buttonPanel.add(new PosButton(new CloseDialogAction(this)));

		authWaitingTab.add(buttonPanel, BorderLayout.SOUTH);

		JPanel authClosedTab = new JPanel(new BorderLayout());
		JPanel buttonPanel2 = new JPanel(new MigLayout("al center", "sg, fill", ""));
		buttonPanel2.add(new PosButton(ActionCommand.TIP_ADJUST, actionHandler), "grow");
		buttonPanel2.add(new PosButton(ActionCommand.VOID, actionHandler), "grow");
		buttonPanel2.add(new PosButton(new CloseDialogAction(this)));
		authClosedTab.add(authClosedListView);
		authClosedTab.add(buttonPanel2, BorderLayout.SOUTH);

		tabbedPane.addTab("Unsettled", authWaitingTab);
		tabbedPane.addTab("Settled", authClosedTab);

		add(tabbedPane);

		updateTransactiontList();
	}

	public void updateTransactiontList() {
		User owner = null;
		User currentUser = Application.getCurrentUser();
		if (!currentUser.hasPermission(UserPermission.VIEW_ALL_OPEN_TICKETS)) {
			owner = currentUser;
		}
		authWaitingListView.setTransactions(PosTransactionDAO.getInstance().findUnauthorizedTransactions(owner));
		authClosedListView.setTransactions(PosTransactionDAO.getInstance().findAuthorizedTransactions(owner));

	}

	private boolean confirmAuthorize(String message) {
		int option = JOptionPane.showConfirmDialog(AuthorizableTicketBrowser.this, message, Messages.getString("TicketAuthorizationDialog.1"), //$NON-NLS-1$
				JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			return true;
		}

		return false;
	}

	private void doAuthorize() {
		List<PosTransaction> transactions = authWaitingListView.getSelectedTransactions();

		if (transactions == null || transactions.size() == 0) {
			POSMessageDialog.showMessage(AuthorizableTicketBrowser.this, Messages.getString("TicketAuthorizationDialog.2")); //$NON-NLS-1$
			return;
		}

		if (!confirmAuthorize(Messages.getString("TicketAuthorizationDialog.3"))) { //$NON-NLS-1$
			return;
		}

		AuthorizationDialog authorizingDialog = new AuthorizationDialog(AuthorizableTicketBrowser.this, transactions);
		authorizingDialog.setVisible(true);

		updateTransactiontList();
	}

	public void doAuthorizeAll() {
		final List<PosTransaction> transactions = authWaitingListView.getAllTransactions();

		if (transactions == null || transactions.size() == 0) {
			POSMessageDialog.showMessage(AuthorizableTicketBrowser.this, Messages.getString("TicketAuthorizationDialog.5")); //$NON-NLS-1$
			return;
		}

		if (!confirmAuthorize(Messages.getString("TicketAuthorizationDialog.6"))) { //$NON-NLS-1$
			return;
		}

		AuthorizationDialog authorizingDialog = new AuthorizationDialog(AuthorizableTicketBrowser.this, transactions);
		authorizingDialog.setVisible(true);

		updateTransactiontList();
	}

	private void doEditTips() {
		PosTransaction transaction = authWaitingListView.getFirstSelectedTransaction();

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
		double newTipsAmount = NumberSelectionDialog2.show(AuthorizableTicketBrowser.this, Messages.getString("TicketAuthorizationDialog.8"), oldTipsAmount); //$NON-NLS-1$

		if (Double.isNaN(newTipsAmount))
			return;

		//double advanceTipsPercentage = CardConfig.getAdvanceTipsPercentage();

		//double acceptableTipsAmount = NumberUtil.roundToTwoDigit(transaction.getTenderAmount() * (advanceTipsPercentage / 100));

		//		if (newTipsAmount > acceptableTipsAmount) {
		//			//POSMessageDialog.showMessage(Messages.getString("AuthorizableTicketBrowser.0") + " :" + CurrencyUtil.getCurrencySymbol() + acceptableTipsAmount); //$NON-NLS-1$ //$NON-NLS-2$
		//			double tipsExceedAmount = newTipsAmount - acceptableTipsAmount;
		//			newTipsAmount = acceptableTipsAmount;
		//			transaction.setTipsExceedAmount(tipsExceedAmount);
		//		}
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

	private void doVoidTransaction() {
		PosTransaction transaction = authWaitingListView.getSelectedTransaction();
		if (tabbedPane.getSelectedIndex() == 1) {
			transaction = authClosedListView.getSelectedTransaction();
		}

		if (transaction == null) {
			POSMessageDialog.showMessage(AuthorizableTicketBrowser.this, Messages.getString("TicketAuthorizationDialog.2")); //$NON-NLS-1$
			return;
		}

		int option = POSMessageDialog.showYesNoQuestionDialog(AuthorizableTicketBrowser.this, "Selected transaction will be voided, proceed?", "Confirm");
		if (option != JOptionPane.YES_OPTION) { //$NON-NLS-1$
			return;
		}
		CardProcessor cardProcessor = CardConfig.getPaymentGateway().getProcessor();
		try {
			cardProcessor.voidTransaction(transaction);
		} catch (Exception e) {
			POSMessageDialog.showError(this, e.getMessage(), e);
		}
		POSMessageDialog.showMessage(this, "Voided!");
		updateTransactiontList();
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

					case SETTLE:
						doAuthorize();
						break;

					case SETTLE_ALL:
						doAuthorizeAll();
						break;

					case VOID:
						doVoidTransaction();
						break;

					case TIP_ADJUST:
						doTicketTipAdjust();
						break;

					default:
						break;
				}
			} catch (Exception e2) {
				POSMessageDialog.showError(AuthorizableTicketBrowser.this, e2.getMessage(), e2);
			}
		}

		private void doTicketTipAdjust() throws Exception {
			PosTransaction transaction = authClosedListView.getFirstSelectedTransaction();
			if (transaction == null) {
				return;
			}

			final double oldTipsAmount = transaction.getTipsAmount();
			double newTipsAmount = NumberSelectionDialog2.show(AuthorizableTicketBrowser.this, Messages.getString("TicketAuthorizationDialog.8"), //$NON-NLS-1$
					oldTipsAmount);

			if (Double.isNaN(newTipsAmount))
				return;

			transaction.getTicket().setGratuityAmount(newTipsAmount);
			transaction.setTipsAmount(newTipsAmount);

			CardProcessor cardProcessor = CardConfig.getPaymentGateway().getProcessor();
			if (cardProcessor.supportTipsAdjustMent()) {
				cardProcessor.adjustTips(transaction);
			}
			else {
				throw new PosException("Payment Gateway can not process Tip Adjustment!!!");
			}
			updateTransactiontList();
			POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Success!");
		}
	}
}
