package com.floreantpos.print;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.main.Application;
import com.floreantpos.model.CashDropTransaction;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.DebitCardTransaction;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.GiftCertificateTransaction;
import com.floreantpos.model.PayOutTransaction;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.CashDropTransactionDAO;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.PayOutTransactionDAO;
import com.floreantpos.model.dao.RefundTransactionDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.util.RefundSummary;
import com.floreantpos.model.util.TransactionSummary;

public class DrawerpullReportService {

	public static DrawerPullReport buildDrawerPullReport() throws Exception {
		Session session = null;
		try {
			Terminal terminal = Application.getInstance().getTerminal();
			DrawerPullReport report = new DrawerPullReport();
			report.setReportTime(new Date());

			GenericDAO dao = new GenericDAO();
			session = dao.createNewSession();

			populateNetSales(session, terminal, report);

//			populateGiftSection(session, terminal, report);

			//populateCashTax(session, terminal, report);

			RefundSummary refundSummary = new RefundTransactionDAO().getTotalRefundForTerminal(terminal);
			report.setCashBack(report.getCashBack() + refundSummary.getAmount());

			report.setTipsPaid(TicketDAO.getInstance().getPaidGratuityAmount(terminal));

			double totalPayout = 0;
			List<PayOutTransaction> payoutTransactions = new PayOutTransactionDAO().getUnsettled(terminal);
			for (PayOutTransaction transaction : payoutTransactions) {
				totalPayout += transaction.getAmount();
			}
			report.setPayOutNumber(payoutTransactions.size());
			report.setPayOutAmount(totalPayout);

			double drawerBleedAmount = 0;
			List<CashDropTransaction> cashDrops = new CashDropTransactionDAO().findUnsettled(terminal);
			for (CashDropTransaction transaction : cashDrops) {
				drawerBleedAmount += transaction.getAmount();
			}
			report.setDrawerBleedNumber(cashDrops.size());
			report.setDrawerBleedAmount(drawerBleedAmount);

			report.setBeginCash(terminal.getOpeningBalance());
			report.setCashToDeposit(terminal.getCurrentBalance());

			//void
//			criteria = session.createCriteria(Ticket.class, "t");
//			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.TRUE));
//			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
//			list = criteria.list();
//
//			for (Iterator iter = list.iterator(); iter.hasNext();) {
//				Ticket ticket = (Ticket) iter.next();
//				DrawerPullVoidTicketEntry entry = new DrawerPullVoidTicketEntry();
//				entry.setCode(ticket.getId());
//				entry.setAmount(ticket.getSubtotalAmount());
//				//entry.setQuantity()
//				entry.setReason(ticket.getVoidReason());
//
//				report.addVoidTicketEntry(entry);
//			}
//
//			//gift cert
//			criteria = session.createCriteria(GiftCertificateTransaction.class);
//			criteria.add(Restrictions.eq(GiftCertificateTransaction.PROP_DRAWER_RESETTED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(GiftCertificateTransaction.PROP_TERMINAL, terminal));
//			projectionList = Projections.projectionList();
//			projectionList.add(Projections.sum(GiftCertificateTransaction.PROP_FACE_VALUE));
//			projectionList.add(Projections.sum(GiftCertificateTransaction.PROP_CASH_BACK_AMOUNT));
//			criteria.setProjection(projectionList);
//			Object[] o = (Object[]) criteria.uniqueResult();
//			if (o.length > 0 && o[0] instanceof Number) {
//				double amount = ((Number) o[0]).doubleValue();
//				report.setGiftCertReturnAmount(amount);
//			}
//			if (o.length > 1 && o[1] instanceof Number) {
//				double amount = ((Number) o[1]).doubleValue();
//				report.setGiftCertChangeAmount(amount);
//			}

			//discount, coupon
			int totalDiscountCount = 0;
			double totalDiscountAmount = 0;
			double totalDiscountSales = 0;
			int totalDiscountGuest = 0;
			int totalDiscountPartySize = 0;
			int totalDiscountCheckSize = 0;
			double totalDiscountPercentage = 0;
			double totalDiscountRatio = 0;

//			criteria = session.createCriteria(Ticket.class, "t");
//			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
//			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
//			list = criteria.list();
//
//			for (Iterator iter = list.iterator(); iter.hasNext();) {
//				Ticket ticket = (Ticket) iter.next();
//				if (ticket.getCouponAndDiscounts() != null) {
//					List<TicketCouponAndDiscount> discounts = ticket.getCouponAndDiscounts();
//					for (TicketCouponAndDiscount discount2 : discounts) {
//						++totalDiscountCount;
//						totalDiscountAmount += discount2.getValue();
//						totalDiscountGuest += ticket.getNumberOfGuests();
//						totalDiscountSales += ticket.getTotalAmount();
//						totalDiscountCheckSize++;
//					}
//				}
//			}

			totalDiscountPartySize = totalDiscountGuest;

			report.setTotalDiscountCount(totalDiscountCount);
			report.setTotalDiscountAmount(totalDiscountAmount);
			report.setTotalDiscountCheckSize(totalDiscountCheckSize);
			report.setTotalDiscountSales(totalDiscountSales);
			report.setTotalDiscountGuest(totalDiscountGuest);
			report.setTotalDiscountPartySize(totalDiscountPartySize);
			report.setTotalDiscountPercentage(totalDiscountPercentage);
			report.setTotalDiscountRatio(totalDiscountRatio);

			report.setTerminal(terminal);

			report.calculate();

			return report;
		} finally {
			if (session != null)
				session.close();
		}
	}

//	private static void populateCashTax(Session session, Terminal terminal, DrawerPullReport report) {
//		Criteria criteria;
//		ProjectionList projectionList;
//		List list;
//		//find cash tax amount
//		criteria = session.createCriteria(Ticket.class, "t");
//		criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
//		criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//		criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
//		//FIXME: TRANSACTION
//		//		criteria.add(Restrictions.eq(Ticket.PROP_TRANSACTION_TYPE, TransactionType.CASH.name()));
//		criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
//
//		projectionList = Projections.projectionList();
//		projectionList.add(Projections.rowCount());
//		projectionList.add(Projections.sum(Ticket.PROP_TAX_AMOUNT));
//
//		criteria.setProjection(projectionList);
//		list = criteria.list();
//
//		double cashTax = 0;
//		if (list != null && list.size() > 0) {
//			Object[] objects = (Object[]) list.get(0);
//
//			if (objects.length > 1 && objects[1] != null) {
//				cashTax = ((Number) objects[1]).doubleValue();
//			}
//		}
//		report.setCashTax(cashTax);
//	}

//	//FIXME: FIX GIFT CERT SECTION
//	private static void populateGiftSection(Session session, Terminal terminal, DrawerPullReport report) {
//		//gift cert receipt
//		Criteria criteria = session.createCriteria(PosTransaction.class);
//		//criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
//		//criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//		//criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
//		criteria.add(Restrictions.eq(PosTransaction.PROP_TERMINAL, terminal));
//
//		//FIXME: TRANSACTION
//		//		criteria.add(Restrictions.eq(Ticket.PROP_TRANSACTION_TYPE, TransactionType.GIFT_CERT.name()));
//
//		//		ProjectionList projectionList = Projections.projectionList();
//		//		projectionList.add(Projections.rowCount());
//		//		projectionList.add(Projections.sum(Ticket.PROP_SUBTOTAL_AMOUNT));
//		//
//		//		criteria.setProjection(projectionList);
//		//
//		//		List list = criteria.list();
//		//
//		//		if (list.size() > 0) {
//		//			Object[] datas = (Object[]) list.get(0);
//		//			if (datas.length > 0 && datas[0] instanceof Number) {
//		//				report.setGiftCertReturnCount(((Number) datas[0]).intValue());
//		//			}
//		//			if (datas.length > 1 && datas[1] instanceof Number) {
//		//				report.setGiftCertReturnAmount(((Number) datas[1]).doubleValue());
//		//			}
//		//		}
//	}

	private static void populateNetSales(Session session, Terminal terminal, DrawerPullReport report) {
		//find net sale, tax
		Criteria criteria = session.createCriteria(Ticket.class);
		criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.TRUE));
		criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
		criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
		criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));

		List<Ticket> list = criteria.list();

		if (list == null || list.size() == 0) {
			return;
		}

		int ticketCount = 0;
		double subtotal = 0;
		double discount = 0;
		double salesTax = 0;
		double tips = 0;
		
		TicketDAO dao = TicketDAO.getInstance();
		
		for (Ticket ticket : list) {
			ticket = dao.loadCouponsAndTransactions(ticket.getId());
			
			++ticketCount;
			subtotal += ticket.getSubtotalAmount();
			discount += ticket.getDiscountAmount();
			salesTax += ticket.getTaxAmount();
			if(ticket.getGratuity() != null) {
				tips += ticket.getGratuity().getAmount();
			}
			
			TransactionSummary ts = calculateTransactionSummary(ticket, CashTransaction.class);
			report.setCashReceiptNumber(report.getCashReceiptNumber() + ts.getTotalNumber());
			report.setCashReceiptAmount(report.getCashReceiptAmount() + ts.getTotalAmount());
			
			ts = calculateTransactionSummary(ticket, CreditCardTransaction.class);
			report.setCreditCardReceiptNumber(report.getCreditCardReceiptNumber() + ts.getTotalNumber());
			report.setCreditCardReceiptAmount(report.getCreditCardReceiptAmount() + ts.getTotalAmount());
			
			ts = calculateTransactionSummary(ticket, DebitCardTransaction.class);
			report.setDebitCardReceiptNumber(report.getDebitCardReceiptNumber() + ts.getTotalNumber());
			report.setDebitCardReceiptAmount(report.getDebitCardReceiptAmount() + ts.getTotalAmount());
			
			ts = calculateTransactionSummary(ticket, GiftCertificateTransaction.class);
			report.setGiftCertReturnCount(report.getGiftCertReturnCount() + ts.getTotalNumber());
			report.setGiftCertReturnAmount(report.getGiftCertReturnAmount() + ts.getTotalAmount());
			report.setGiftCertChangeAmount(report.getGiftCertChangeAmount() + ts.getChangeAmount());
			report.setCashBack(report.getCashBack() + ts.getChangeAmount());
		}

		report.setTicketCount(ticketCount);
		report.setNetSales(subtotal - discount);
		report.setSalesTax(salesTax);
		report.setChargedTips(tips);
	}
	
	private static TransactionSummary calculateTransactionSummary(Ticket ticket, Class transactionClass) {
		int count = 0;
		double total = 0;
		double changeAmount = 0;
		
		TransactionSummary summary = new TransactionSummary();
		
		List<PosTransaction> transactions = ticket.getTransactions();
		if(transactions == null) {
			return summary;
		}
		
		for (PosTransaction posTransaction : transactions) {
			if(posTransaction.getClass().equals(transactionClass)) {
				++count;
				total += posTransaction.getAmount();
				changeAmount += posTransaction.getGiftCertCashBackAmount();
			}
		}
		
		summary.setTotalNumber(count);
		summary.setTotalAmount(total);
		summary.setChangeAmount(changeAmount);
		
		return summary;
	}

}
