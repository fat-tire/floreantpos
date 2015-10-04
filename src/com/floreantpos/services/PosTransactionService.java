package com.floreantpos.services;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.main.Application;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.GiftCertificateTransaction;
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
			//transaction.setPaymentType(transaction.getPaymentType());
			transaction.setTerminal(terminal);
			transaction.setUser(currentUser);
			transaction.setTransactionTime(currentDate);

			ticket.addTotransactions(transaction);

			if (ticket.getType() == OrderType.BAR_TAB) {
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
		else if(transaction instanceof VoidTransaction) {
			
			double currentBalance = terminal.getCurrentBalance();
			double newBalance = currentBalance - transaction.getAmount();

			terminal.setCurrentBalance(newBalance);
			
		}
	}

	private void closeTicketIfApplicable(Ticket ticket, Date currentDate) {
		OrderType ticketType = ticket.getType();
		
		switch (ticketType) {
			case DINE_IN:
			case BAR_TAB:
			case TAKE_OUT:
				ticket.setClosed(true);
				ticket.setClosingDate(currentDate);
				break;

			default:
				break;
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
			Double totalPrice = ticket.getTotalAmount();
			double newBalance = currentBalance - totalPrice;
			terminal.setCurrentBalance(newBalance);
			
//			double refundAmount = ticket.getPaidAmount();
//			if(ticket.getGratuity() != null) {
//				refundAmount -= ticket.getGratuity().getAmount();
//			}

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

			session = dao.getSession();
			tx = session.beginTransaction();

			dao.saveOrUpdate(ticket, session);

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
