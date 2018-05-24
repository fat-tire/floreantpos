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
package com.floreantpos.services;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.main.Application;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.GiftCertificateTransaction;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.RefundTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TransactionType;
import com.floreantpos.model.User;
import com.floreantpos.model.VoidTransaction;
import com.floreantpos.model.dao.ActionHistoryDAO;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.util.NumberUtil;

public class PosTransactionService {
	private static PosTransactionService paymentService = new PosTransactionService();

	public void settleTicket(Ticket ticket, PosTransaction transaction) throws Exception {
		Application application = Application.getInstance();
		User currentUser = Application.getCurrentUser();
		if (currentUser == null) {
			currentUser = ticket.getOwner();
		}
		Terminal terminal = application.refreshAndGetTerminal();

		Session session = null;
		Transaction tx = null;

		GenericDAO dao = new GenericDAO();

		try {
			Date currentDate = new Date();

			session = dao.createNewSession();
			tx = session.beginTransaction();

			ticket.setVoided(false);
			ticket.setDrawerResetted(false);
			ticket.setTerminal(terminal);
			ticket.setPaidAmount(ticket.getPaidAmount() + transaction.getAmount());

			ticket.calculatePrice();

			if (ticket.getDueAmount() == 0.0) {
				ticket.setPaid(true);
				closeTicketIfApplicable(ticket, currentDate);
			}
			else {
				ticket.setPaid(false);
				ticket.setClosed(false);
			}

			transaction.setTransactionType(TransactionType.CREDIT.name());
			transaction.setPaymentType(transaction.getPaymentType());
			transaction.setTerminal(terminal);
			transaction.setUser(currentUser);
			transaction.setTransactionTime(currentDate);

			ticket.addTotransactions(transaction);

			if (ticket.getOrderType().getName() == OrderType.BAR_TAB) {
				ticket.removeProperty(Ticket.PROPERTY_PAYMENT_METHOD);
				ticket.removeProperty(Ticket.PROPERTY_CARD_NAME);
				ticket.removeProperty(Ticket.PROPERTY_CARD_TRANSACTION_ID);
				ticket.removeProperty(Ticket.PROPERTY_CARD_TRACKS);
				ticket.removeProperty(Ticket.PROPERTY_CARD_READER);
				ticket.removeProperty(Ticket.PROPERTY_ADVANCE_PAYMENT);
				ticket.removeProperty(Ticket.PROPERTY_CARD_NUMBER);
				ticket.removeProperty(Ticket.PROPERTY_CARD_EXP_YEAR);
				ticket.removeProperty(Ticket.PROPERTY_CARD_EXP_MONTH);
				ticket.removeProperty(Ticket.PROPERTY_CARD_AUTH_CODE);
			}

			adjustTerminalBalance(transaction);

			session.update(terminal);
			//session.saveOrUpdate(ticket);
			TicketDAO.getInstance().saveOrUpdate(ticket, session);

			//				User assignedDriver = ticket.getAssignedDriver();
			//				if(assignedDriver != null) {
			//					assignedDriver.setAvailableForDelivery(true);
			//					UserDAO.getInstance().saveOrUpdate(assignedDriver, session);
			//				}

			tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception x) {
			}
			throw e;
		} finally {
			dao.closeSession(session);
		}

		//			SETTLE ACTION
		String actionMessage = com.floreantpos.POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ":" + ticket.getId(); //$NON-NLS-1$
		actionMessage += ";" + com.floreantpos.POSConstants.TOTAL + ":" + NumberUtil.formatNumber(ticket.getTotalAmount()); //$NON-NLS-1$ //$NON-NLS-2$
		ActionHistoryDAO.getInstance().saveHistory(Application.getCurrentUser(), ActionHistory.SETTLE_CHECK, actionMessage);
	}

	public void bookBartabTicket(Ticket ticket, PosTransaction transaction, boolean closed) throws Exception {
		Application application = Application.getInstance();
		User currentUser = Application.getCurrentUser();
		Terminal terminal = application.refreshAndGetTerminal();

		Session session = null;
		Transaction tx = null;

		GenericDAO dao = new GenericDAO();

		try {
			Date currentDate = new Date();

			session = dao.createNewSession();
			tx = session.beginTransaction();

			ticket.setVoided(false);
			ticket.setDrawerResetted(false);
			ticket.setTerminal(terminal);
			ticket.calculatePrice();

			if (closed) {
				ticket.setPaid(true);
				closeTicketIfApplicable(ticket, currentDate);
			}
			else {
				ticket.setPaid(false);
				ticket.setClosed(false);
			}

			transaction.setTransactionType(TransactionType.CREDIT.name());
			transaction.setPaymentType(transaction.getPaymentType());
			transaction.setTerminal(terminal);
			transaction.setUser(currentUser);
			transaction.setTransactionTime(currentDate);

			ticket.addTotransactions(transaction);

			TicketDAO.getInstance().saveOrUpdate(ticket, session);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tx.rollback();
			} catch (Exception x) {
			}
			throw e;
		} finally {
			dao.closeSession(session);
		}

		String actionMessage = com.floreantpos.POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ":" + ticket.getId(); //$NON-NLS-1$
		actionMessage += ";" + com.floreantpos.POSConstants.TOTAL + ":" + NumberUtil.formatNumber(ticket.getTotalAmount()); //$NON-NLS-1$ //$NON-NLS-2$
		ActionHistoryDAO.getInstance().saveHistory(Application.getCurrentUser(), ActionHistory.SETTLE_CHECK, actionMessage);
	}

	public static void adjustTerminalBalance(PosTransaction transaction) {
		Terminal terminal = transaction.getTerminal();

		if (transaction instanceof CashTransaction) {

			double currentBalance = terminal.getCurrentBalance();
			double newBalance = currentBalance + transaction.getAmount();

			terminal.setCurrentBalance(newBalance);

		}
		else if (transaction instanceof GiftCertificateTransaction) {

			double currentBalance = terminal.getCurrentBalance();
			double newBalance = currentBalance - transaction.getGiftCertCashBackAmount();

			terminal.setCurrentBalance(newBalance);

		}
		else if (transaction instanceof VoidTransaction) {

			double currentBalance = terminal.getCurrentBalance();
			double newBalance = currentBalance - transaction.getAmount();

			terminal.setCurrentBalance(newBalance);

		}
	}

	private void closeTicketIfApplicable(Ticket ticket, Date currentDate) {
		OrderType ticketType = ticket.getOrderType();

		if (ticketType.isCloseOnPaid()) {//fix
			ticket.setClosed(true);
			ticket.setClosingDate(currentDate);
		}
	}

	public void refundTicket(Ticket ticket, final double refundAmount) throws Exception {
		User currentUser = Application.getCurrentUser();
		Terminal terminal = ticket.getTerminal();

		Session session = null;
		Transaction tx = null;

		GenericDAO dao = new GenericDAO();

		try {
			Double currentBalance = terminal.getCurrentBalance();
			double newBalance = currentBalance - refundAmount;
			terminal.setCurrentBalance(newBalance);

			Gratuity gratuity = ticket.getGratuity();
			if (gratuity != null) {
				double diff = ticket.getPaidAmount() - refundAmount;
				if (diff == 0 || diff > gratuity.getAmount()) {
					gratuity.setRefunded(true);
				}
			}

			RefundTransaction posTransaction = new RefundTransaction();
			posTransaction.setTicket(ticket);
			posTransaction.setPaymentType(PaymentType.CASH.name());
			posTransaction.setTransactionType(TransactionType.DEBIT.name());
			posTransaction.setAmount(refundAmount);
			posTransaction.setTerminal(terminal);
			posTransaction.setUser(currentUser);
			posTransaction.setTransactionTime(new Date());

			ticket.setVoided(false);
			ticket.setRefunded(true);
			ticket.setClosed(true);
			ticket.setDrawerResetted(false);
			ticket.setClosingDate(new Date());

			ticket.addTotransactions(posTransaction);

			session = dao.createNewSession();
			tx = session.beginTransaction();

			dao.saveOrUpdate(ticket, session);
			dao.saveOrUpdate(terminal, session);

			tx.commit();

			//String title = "- REFUND RECEIPT -";
			//String data = "Ticket #" + ticket.getId() + ", amount " + refundAmount + " was refunded.";

			ReceiptPrintService.printRefundTicket(ticket, posTransaction);

		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception x) {
			}

			throw e;
		} finally {
			dao.closeSession(session);
		}

	}

	public static PosTransactionService getInstance() {
		return paymentService;
	}
}
