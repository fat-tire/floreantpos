package com.floreantpos.services;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.main.Application;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.DebitCardTransaction;
import com.floreantpos.model.GiftCertificateTransaction;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.RefundTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.TransactionType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.ActionHistoryDAO;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.util.NumberUtil;

public class PosTransactionService {
	private static PosTransactionService paymentService = new PosTransactionService();

	public void settleTickets(List<Ticket> tickets, final double tenderedAmount, PosTransaction transaction, String cardType, String cardAuthorizationCode) throws Exception {
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
			
			double totalDueAmount = 0;
			double cashBackAmount = 0;
			for (Ticket ticket : tickets) {
				totalDueAmount += ticket.getDueAmount();
			}
			cashBackAmount = tenderedAmount - totalDueAmount;
			if(cashBackAmount < 0) {
				cashBackAmount = 0;
			}
			
			for (Iterator it = tickets.iterator(); it.hasNext(); ) {
				Ticket ticket = (Ticket) it.next();
				
				ticket.setVoided(false);
				ticket.setDrawerResetted(false);
				ticket.setTerminal(terminal);
				ticket.setCardAuthCode(cardAuthorizationCode);
				
				double paidAmount = ticket.getPaidAmount();
				double dueAmount = ticket.getDueAmount();
				
				if(tenderedAmount >= dueAmount) {
					paidAmount += dueAmount;
					dueAmount = 0;
					
					ticket.setPaid(true);
					closeTicketIfApplicable(ticket, currentDate);
				}
				else {
					paidAmount += tenderedAmount;
					dueAmount -= tenderedAmount;
					
					ticket.setPaid(false);
					ticket.setClosed(false);
				}
				ticket.setPaidAmount(paidAmount);
				ticket.setDueAmount(dueAmount);
				
				PosTransaction posTransaction = null;
				if (transaction instanceof CashTransaction) {
					posTransaction = new CashTransaction();
					
					Double currentBalance = terminal.getCurrentBalance();
					Double totalPrice = ticket.getTotalAmount();
					double newBalance = currentBalance + totalPrice;

					terminal.setCurrentBalance(newBalance);
					
					ticket.setTransactionType(TransactionType.CASH.name());
				}
				else if (transaction instanceof CreditCardTransaction) {
					posTransaction = new CreditCardTransaction();
					((CreditCardTransaction) posTransaction).setCardNumber(cardAuthorizationCode);
					((CreditCardTransaction) posTransaction).setCardType(cardType);
					ticket.setCardType(cardType);
					ticket.setTransactionType(TransactionType.CARD.name());
				}
				else if (transaction instanceof DebitCardTransaction) {
					posTransaction = new DebitCardTransaction();
					((DebitCardTransaction) posTransaction).setCardNumber(cardAuthorizationCode);
					((DebitCardTransaction) posTransaction).setCardType(cardType);
					ticket.setCardType(cardType);
					ticket.setTransactionType(TransactionType.CARD.name());
				}
				else if (transaction instanceof GiftCertificateTransaction) {
					posTransaction = transaction;
					GiftCertificateTransaction giftCertificateTransaction = (GiftCertificateTransaction) posTransaction;
						
					Double currentBalance = terminal.getCurrentBalance();
					double newBalance = currentBalance - giftCertificateTransaction.getCashBackAmount();
					terminal.setCurrentBalance(newBalance);

					ticket.setTransactionType(TransactionType.GIFT_CERT.name());
				}
				
				posTransaction.setTicket(ticket);
				posTransaction.setAmount(tenderedAmount);

				posTransaction.setTerminal(terminal);
				posTransaction.setUser(currentUser);
				posTransaction.setTransactionTime(currentDate);
				
				dao.saveOrUpdate(ticket, session);
				dao.saveOrUpdate(posTransaction, session);
				dao.saveOrUpdate(terminal, session);
				
//				User assignedDriver = ticket.getAssignedDriver();
//				if(assignedDriver != null) {
//					assignedDriver.setAvailableForDelivery(true);
//					UserDAO.getInstance().saveOrUpdate(assignedDriver, session);
//				}
			}

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
		
		for (Ticket ticket : tickets) {
//			SETTLE ACTION
			String actionMessage = com.floreantpos.POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ":" + ticket.getId();
			actionMessage += ";" +  com.floreantpos.POSConstants.TOTAL + ":" + NumberUtil.formatNumber(ticket.getTotalAmount());
			ActionHistoryDAO.getInstance().saveHistory(Application.getCurrentUser(), ActionHistory.SETTLE_CHECK, actionMessage);
		}
	}

	private void closeTicketIfApplicable(Ticket ticket, Date currentDate) {
		String transactionType = ticket.getTransactionType();
		if(!"CASH".equalsIgnoreCase(transactionType)) {
			return;
		}
		
		TicketType ticketType = ticket.getType();
		
		if(ticketType == TicketType.DINE_IN || ticketType == TicketType.TAKE_OUT || ticketType == TicketType.BAR_TAB) {
			ticket.setClosed(true);
			ticket.setClosingDate(currentDate);
		}
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
			posTransaction.setTicket(ticket);

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
