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
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.RefundTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.ActionHistoryDAO;
import com.floreantpos.model.dao.GenericDAO;

public class PosTransactionService {
	private static PosTransactionService paymentService = new PosTransactionService();

	public void settleTickets(List<Ticket> tickets, double tenderedAmount, double gratuityAmount, PosTransaction transaction, String cardType, String cardAuthorizationCode) throws Exception {
		Application application = Application.getInstance();
		User currentUser = Application.getCurrentUser();
		Terminal terminal = application.getTerminal();

		Session session = null;
		Transaction tx = null;

		GenericDAO dao = new GenericDAO();
		
		try {
			Date currentDate = new Date();
			boolean gratuityPaid = false;
			
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
				ticket.setClosingDate(currentDate);
				ticket.setTerminal(terminal);
				ticket.setCardNumber(cardAuthorizationCode);
				
				double paidAmount = Double.parseDouble(Application.formatNumber(ticket.getPaidAmount()));
				double dueAmount = Double.parseDouble(Application.formatNumber(ticket.getDueAmount()));
				
				if(tenderedAmount >= dueAmount) {
					paidAmount += dueAmount;
					tenderedAmount -= dueAmount;
					dueAmount = 0;
					
					ticket.setPaid(true);
					ticket.setClosed(true);
				}
				else {
					paidAmount += tenderedAmount;
					dueAmount -= tenderedAmount;
					
					ticket.setPaid(false);
					ticket.setClosed(false);
				}
				ticket.setPaidAmount(paidAmount);
				ticket.setDueAmount(dueAmount);
				
				if (!gratuityPaid && gratuityAmount > 0) {
					Gratuity gratuity = new Gratuity();
					gratuity.setAmount(gratuityAmount);
					gratuity.setOwner(ticket.getOwner());
					gratuity.setPaid(false);
					gratuity.setTicket(ticket);
					gratuity.setTerminal(ticket.getTerminal());
					
					ticket.setGratuity(gratuity);
					
					gratuityPaid = true;
				}

				PosTransaction posTransaction = null;
				if (transaction instanceof CashTransaction) {
					posTransaction = new CashTransaction();
					
					Double currentBalance = terminal.getCurrentBalance();
					Double totalPrice = ticket.getTotalAmount();
					double newBalance = currentBalance + totalPrice;

					terminal.setCurrentBalance(newBalance);
					
					ticket.setTransactionType(PosTransaction.TYPE_CASH);
				}
				else if (transaction instanceof CreditCardTransaction) {
					posTransaction = new CreditCardTransaction();
					((CreditCardTransaction) posTransaction).setCardNumber(cardAuthorizationCode);
					((CreditCardTransaction) posTransaction).setCardType(cardType);
					ticket.setCardType(cardType);
					ticket.setTransactionType(PosTransaction.TYPE_CREDIT_CARD);
				}
				else if (transaction instanceof DebitCardTransaction) {
					posTransaction = new DebitCardTransaction();
					((DebitCardTransaction) posTransaction).setCardNumber(cardAuthorizationCode);
					((DebitCardTransaction) posTransaction).setCardType(cardType);
					ticket.setCardType(cardType);
					ticket.setTransactionType(PosTransaction.TYPE_DEBIT_CARD);
				}
				else if (transaction instanceof GiftCertificateTransaction) {
					posTransaction = transaction;
					GiftCertificateTransaction giftCertificateTransaction = (GiftCertificateTransaction) posTransaction;
						
					Double currentBalance = terminal.getCurrentBalance();
					double newBalance = currentBalance - giftCertificateTransaction.getCashBackAmount();
					terminal.setCurrentBalance(newBalance);

					ticket.setTransactionType(PosTransaction.TYPE_GIFT_CERT);
				}
				
				posTransaction.setTicket(ticket);
				posTransaction.setSubtotalAmount(ticket.getSubtotalAmount());
				posTransaction.setDiscountAmount(ticket.getDiscountAmount());
				posTransaction.setTaxAmount(ticket.getTaxAmount());
				posTransaction.setTotalAmount(ticket.getTotalAmount());
				
				if(ticket.getGratuity() != null) {
					posTransaction.setGratuityAmount(ticket.getGratuity().getAmount());
				}

				posTransaction.setTerminal(terminal);
				posTransaction.setUser(currentUser);
				posTransaction.setTransactionTime(currentDate);
				
				dao.saveOrUpdate(ticket, session);
				dao.saveOrUpdate(posTransaction, session);
				dao.saveOrUpdate(terminal, session);
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
			String actionMessage = com.floreantpos.POSConstants.CHK_NO + ":" + ticket.getId();
			actionMessage += ";" +  com.floreantpos.POSConstants.TOTAL + ":" + Application.formatNumber(ticket.getTotalAmount());
			ActionHistoryDAO.getInstance().saveHistory(Application.getCurrentUser(), ActionHistory.SETTLE_CHECK, actionMessage);
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

			posTransaction.setSubtotalAmount(ticket.getSubtotalAmount());
			posTransaction.setDiscountAmount(ticket.getDiscountAmount());
			posTransaction.setTaxAmount(ticket.getTaxAmount());
			posTransaction.setTotalAmount(ticket.getTotalAmount());

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
