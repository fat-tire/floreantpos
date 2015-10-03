package com.floreantpos.print;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.main.Application;
import com.floreantpos.model.CashDropTransaction;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.DebitCardTransaction;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.DrawerPullVoidTicketEntry;
import com.floreantpos.model.GiftCertificateTransaction;
import com.floreantpos.model.PayOutTransaction;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
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
			Terminal terminal = Application.getInstance().refreshAndGetTerminal();
			DrawerPullReport report = new DrawerPullReport();
			report.setReportTime(new Date());

			GenericDAO dao = new GenericDAO();
			session = dao.createNewSession();

			populateNetSales(session, terminal, report);
			populateReceiptDifferential(session, terminal, report);

			//			populateGiftSection(session, terminal, report);

			//populateCashTax(session, terminal, report);

			RefundSummary refundSummary = RefundTransactionDAO.getInstance().getTotalRefundForTerminal(terminal);
			report.setRefundReceiptCount(report.getRefundReceiptCount() + refundSummary.getCount());
			report.setRefundAmount(report.getRefundAmount() + refundSummary.getAmount());

			report.setTipsPaid(TicketDAO.getInstance().getPaidGratuityAmount(terminal));

			double totalPayout = 0;
			List<PayOutTransaction> payoutTransactions = new PayOutTransactionDAO().getUnsettled(terminal);
			for (PayOutTransaction transaction : payoutTransactions) {
				totalPayout += transaction.getAmount();
			}
			report.setPayOutCount(payoutTransactions.size());
			report.setPayOutAmount(totalPayout);

			double drawerBleedAmount = 0;
			List<CashDropTransaction> cashDrops = new CashDropTransactionDAO().findUnsettled(terminal);
			for (CashDropTransaction transaction : cashDrops) {
				drawerBleedAmount += transaction.getAmount();
			}
			report.setDrawerBleedCount(cashDrops.size());
			report.setDrawerBleedAmount(drawerBleedAmount);

			report.setBeginCash(terminal.getOpeningBalance());
			report.setCashToDeposit(terminal.getCurrentBalance());

			populateVoidSection(session, terminal, report);
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

			Criteria criteria = session.createCriteria(Ticket.class, "t"); //$NON-NLS-1$
			//criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			List list = criteria.list();

			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				if (ticket.getCouponAndDiscounts() != null) {
					List<TicketCouponAndDiscount> discounts = ticket.getCouponAndDiscounts();
					for (TicketCouponAndDiscount discount2 : discounts) {
						++totalDiscountCount;
						totalDiscountAmount += discount2.getValue();
						totalDiscountGuest += ticket.getNumberOfGuests();
						totalDiscountSales += ticket.getTotalAmount();
						totalDiscountCheckSize++;
					}
				}
			}

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

	private static void populateVoidSection(Session session, Terminal terminal, DrawerPullReport report) {
		//void
		Criteria criteria = session.createCriteria(Ticket.class, "t"); //$NON-NLS-1$
		criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.TRUE));
		criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
		criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
		List<Ticket> list = criteria.list();

		double totalWaste = 0;
		double totalVoid = 0;

		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Ticket ticket = (Ticket) iter.next();
			DrawerPullVoidTicketEntry entry = new DrawerPullVoidTicketEntry();
			entry.setCode(ticket.getId());
			entry.setAmount(ticket.getSubtotalAmount());
			entry.setReason(ticket.getVoidReason());
			if (ticket.isWasted()) {
				entry.setHast("Yes"); //$NON-NLS-1$
			}
			else {
				entry.setHast("No"); //$NON-NLS-1$
			}

			report.addVoidTicketEntry(entry);

			totalVoid += ticket.getSubtotalAmount();
			if (ticket.isWasted()) {
				totalWaste += ticket.getSubtotalAmount();
			}
		}

		report.setTotalVoid(totalVoid);
		report.setTotalVoidWst(totalWaste);
	}

	private static void populateNetSales(Session session, Terminal terminal, DrawerPullReport report) {
		//find net sale, tax
		Criteria criteria = session.createCriteria(Ticket.class);
		criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.TRUE));
		criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
		criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
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

		for (Ticket ticket : list) {
			++ticketCount;
			subtotal += ticket.getSubtotalAmount();
			discount += ticket.getDiscountAmount();
			salesTax += ticket.getTaxAmount();
			if (ticket.getGratuity() != null) {
				tips += ticket.getGratuity().getAmount();
			}
		}

		report.setTicketCount(ticketCount);
		report.setNetSales(subtotal - discount);
		report.setSalesTax(salesTax);
		report.setChargedTips(tips);
	}

	private static void populateReceiptDifferential(Session session, Terminal terminal, DrawerPullReport report) {
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

		TicketDAO dao = TicketDAO.getInstance();

		for (Ticket ticket : list) {
			ticket = dao.loadCouponsAndTransactions(ticket.getId());


			TransactionSummary ts = calculateTransactionSummary(ticket, CashTransaction.class);
			report.setCashReceiptCount(report.getCashReceiptCount() + ts.getCount());
			report.setCashReceiptAmount(report.getCashReceiptAmount() + ts.getAmount());

			ts = calculateTransactionSummary(ticket, CreditCardTransaction.class);
			report.setCreditCardReceiptCount(report.getCreditCardReceiptCount() + ts.getCount());
			report.setCreditCardReceiptAmount(report.getCreditCardReceiptAmount() + ts.getAmount());

			ts = calculateTransactionSummary(ticket, DebitCardTransaction.class);
			report.setDebitCardReceiptCount(report.getDebitCardReceiptCount() + ts.getCount());
			report.setDebitCardReceiptAmount(report.getDebitCardReceiptAmount() + ts.getAmount());

			ts = calculateTransactionSummary(ticket, GiftCertificateTransaction.class);
			report.setGiftCertReturnCount(report.getGiftCertReturnCount() + ts.getCount());
			report.setGiftCertReturnAmount(report.getGiftCertReturnAmount() + ts.getAmount());
			report.setGiftCertChangeAmount(report.getGiftCertChangeAmount() + ts.getChangeAmount());
			report.setCashBack(report.getCashBack() + ts.getChangeAmount());
		}
	}

	private static TransactionSummary calculateTransactionSummary(Ticket ticket, Class transactionClass) {
		int count = 0;
		double total = 0;
		double changeAmount = 0;

		TransactionSummary summary = new TransactionSummary();

		Set<PosTransaction> transactions = ticket.getTransactions();
		if (transactions == null) {
			return summary;
		}

		for (PosTransaction posTransaction : transactions) {
			if (posTransaction.getClass().equals(transactionClass)) {
				++count;
				total += posTransaction.getAmount();
				changeAmount += posTransaction.getGiftCertCashBackAmount();
			}
		}

		summary.setCount(count);
		summary.setAmount(total);
		summary.setChangeAmount(changeAmount);

		return summary;
	}

}
