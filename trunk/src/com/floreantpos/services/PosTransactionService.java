package com.floreantpos.services;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.main.Application;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.GiftCertificateTransaction;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.RefundTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.ActionHistoryDAO;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.util.NumberUtil;

public class PosTransactionService {
	private static PosTransactionService paymentService = new PosTransactionService();

	public void settleTicket(Ticket ticket, PosTransaction transaction) throws Exception {
		Application application = Application.getInstance();
		User currentUser = Application.getCurrentUser();
		Terminal terminal = application.getTerminal();

		Session session = null;
		Transaction tx = null;

		GenericDAO dao = new GenericDAO();

		try {
			Date currentDate = new Date();

			session = dao.getSession();
			tx = session.beginTransaction();

			ticket.setVoided(false);
			ticket.setDrawerResetted(false);
			ticket.setTerminal(terminal);

			double transactionAmount = 0;
			double dueAmount = 0;
			final double tenderAmount = transaction.getTenderAmount(); 
			
			if(tenderAmount >= ticket.getDueAmount()) {
				transactionAmount = ticket.getDueAmount();
				dueAmount = 0;
			}
			else {
				transactionAmount = tenderAmount;
				dueAmount = ticket.getDueAmount() - tenderAmount;
			}

			ticket.setPaidAmount(ticket.getPaidAmount() + transactionAmount);
			ticket.setDueAmount(dueAmount);

			if (dueAmount == 0) {
				ticket.setPaid(true);
				closeTicketIfApplicable(ticket, currentDate);
			}
			else {
				ticket.setPaid(false);
				ticket.setClosed(false);
			}
			
			adjustTerminalBalance(ticket, transaction, terminal);

			transaction.setAmount(transactionAmount);
			transaction.setTenderAmount(tenderAmount);
			transaction.setDebit(false);
			transaction.setTicket(ticket);
			transaction.setTerminal(terminal);
			transaction.setUser(currentUser);
			transaction.setTransactionTime(currentDate);

			ticket.addTotransactions(transaction);
			
			if(ticket.getType() == TicketType.BAR_TAB) {
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

			dao.saveOrUpdate(ticket, session);
			//dao.saveOrUpdate(transaction, session);
			dao.saveOrUpdate(terminal, session);

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
		String actionMessage = com.floreantpos.POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ":" + ticket.getId();
		actionMessage += ";" + com.floreantpos.POSConstants.TOTAL + ":" + NumberUtil.formatNumber(ticket.getTotalAmount());
		ActionHistoryDAO.getInstance().saveHistory(Application.getCurrentUser(), ActionHistory.SETTLE_CHECK, actionMessage);
	}

	private void adjustTerminalBalance(Ticket ticket, PosTransaction transaction, Terminal terminal) {
		if(transaction instanceof CashTransaction) {
			double currentBalance = terminal.getCurrentBalance();
			double totalPrice = ticket.getTotalAmount();
			double newBalance = currentBalance + totalPrice;

			terminal.setCurrentBalance(newBalance);
		}
		else if(transaction instanceof GiftCertificateTransaction) {
			double currentBalance = terminal.getCurrentBalance();
			double newBalance = currentBalance - ((GiftCertificateTransaction) transaction).getCashBackAmount();
			terminal.setCurrentBalance(newBalance);
		}
	}

	private void closeTicketIfApplicable(Ticket ticket, Date currentDate) {
		//		String transactionType = ticket.getTransactionType();
		//		if (!"CASH".equalsIgnoreCase(transactionType)) {
		//			return;
		//		}
		//
		//		TicketType ticketType = ticket.getType();
		//
		//		if (ticketType == TicketType.DINE_IN || ticketType == TicketType.TAKE_OUT || ticketType == TicketType.BAR_TAB) {
		//			ticket.setClosed(true);
		//			ticket.setClosingDate(currentDate);
		//		}
	}

	public void refundTicket(Ticket ticket) throws Exception {
		Application application = Application.getInstance();
		User currentUser = Application.getCurrentUser();
		Terminal terminal = application.getTerminal();

		Session session = null;
		Transaction tx = null;

		GenericDAO dao = new GenericDAO();

		try {
			Double currentBalance = terminal.getCurrentBalance();
			Double totalPrice = ticket.getTotalAmount();
			double newBalance = currentBalance - totalPrice;
			terminal.setCurrentBalance(newBalance);

			ticket.setVoided(false);
			ticket.setPaid(false);
			ticket.setClosed(false);
			ticket.setDrawerResetted(false);
			ticket.setClosingDate(null);
			ticket.setReOpened(true);
			ticket.setTerminal(terminal);

			RefundTransaction posTransaction = new RefundTransaction();
			//posTransaction.setTicket(ticket);

			posTransaction.setAmount(ticket.getSubtotalAmount());

			posTransaction.setTerminal(terminal);
			posTransaction.setUser(currentUser);
			posTransaction.setTransactionTime(new Date());

			session = dao.getSession();
			tx = session.beginTransaction();

			dao.saveOrUpdate(ticket, session);
			dao.saveOrUpdate(posTransaction, session);
			dao.saveOrUpdate(terminal, session);

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

	}

	public static PosTransactionService getInstance() {
		return paymentService;
	}
}
