package com.floreantpos.services;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.main.Application;
import com.floreantpos.model.CashDropTransaction;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.DrawerPullVoidTicketEntry;
import com.floreantpos.model.GiftCertificateTransaction;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.PayOutTransaction;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.dao.CashDropTransactionDAO;
import com.floreantpos.model.dao.CashTransactionDAO;
import com.floreantpos.model.dao.CreditCardTransactionDAO;
import com.floreantpos.model.dao.DebitCardTransactionDAO;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.PayOutTransactionDAO;
import com.floreantpos.model.dao.RefundTransactionDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.util.RefundSummary;
import com.floreantpos.model.util.TransactionSummary;

public class DrawerPullService {
	public DrawerPullReport getDrawerPullReport() throws Exception {
		Session session = null;
		try {
			Terminal terminal = Application.getInstance().getTerminal();
			DrawerPullReport report = new DrawerPullReport();
			report.setReportTime(new Date());

			GenericDAO dao = new GenericDAO();
			session = dao.createNewSession();

			//find net sale, tax
			Criteria criteria = session.createCriteria(Ticket.class, "t");
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum(Ticket.PROP_SUBTOTAL_AMOUNT));
			projectionList.add(Projections.sum(Ticket.PROP_DISCOUNT_AMOUNT));
			projectionList.add(Projections.sum(Ticket.PROP_TAX_AMOUNT));

			criteria.setProjection(projectionList);
			List list = criteria.list();

			double subtotal = 0;
			double discount = 0;
			double salesTax = 0;
			if (list != null && list.size() > 0) {
				Object[] objects = (Object[]) list.get(0);

				if (objects.length > 0 && objects[0] != null) {
					int count = ((Number) objects[0]).intValue();
					report.setTicketCount(count);
				}
				if (objects.length > 1 && objects[1] != null) {
					subtotal = ((Number) objects[1]).doubleValue();
				}
				if (objects.length > 2 && objects[2] != null) {
					discount = ((Number) objects[2]).doubleValue();
				}
				if (objects.length > 3 && objects[3] != null) {
					salesTax = ((Number) objects[3]).doubleValue();
				}
			}
			report.setNetSales(subtotal - discount);
			report.setSalesTax(salesTax);
			
			//gift cert receipt
			criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			criteria.add(Restrictions.eq(Ticket.PROP_TRANSACTION_TYPE, PosTransaction.TYPE_GIFT_CERT));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum(Ticket.PROP_SUBTOTAL_AMOUNT));
			criteria.setProjection(projectionList);
			list = criteria.list();
			if(list.size() > 0) {
				Object[] datas = (Object[]) list.get(0);
				if(datas.length > 0 && datas[0] instanceof Number) {
					report.setGiftCertReturnCount(((Number) datas[0]).intValue());
				}
				if(datas.length > 1 && datas[1] instanceof Number) {
					report.setGiftCertReturnAmount(((Number) datas[1]).doubleValue());
				}
			}

			//find tips
			double tips = 0;
			criteria = session.createCriteria(Gratuity.class);
			criteria.createAlias(Gratuity.PROP_TICKET, "ticket");
			criteria.add(Restrictions.eq(Gratuity.PROP_TERMINAL, terminal));
			criteria.add(Restrictions.eq("ticket." + Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.setProjection(Projections.sum(Gratuity.PROP_AMOUNT));
			list = criteria.list();
			if (list.size() > 0 && list.get(0) instanceof Number) {
				tips = ((Number) list.get(0)).doubleValue();
			}
			report.setChargedTips(tips);

			//find cash tax amount
			criteria = session.createCriteria(Ticket.class, "t");
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TRANSACTION_TYPE, PosTransaction.TYPE_CASH));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));

			projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum(Ticket.PROP_TAX_AMOUNT));

			criteria.setProjection(projectionList);
			list = criteria.list();

			double cashTax = 0;
			if (list != null && list.size() > 0) {
				Object[] objects = (Object[]) list.get(0);

				if (objects.length > 1 && objects[1] != null) {
					cashTax = ((Number) objects[1]).doubleValue();
				}
			}
			report.setCashTax(cashTax);

			TransactionSummary cashTransactionSummary = CashTransactionDAO.getInstance().getTransactionSummary(terminal);
			TransactionSummary creditCardTransactionSummary = CreditCardTransactionDAO.getInstance().getTransactionSummary(terminal);
			TransactionSummary debitCardTransactionSummary = DebitCardTransactionDAO.getInstance().getTransactionSummary(terminal);

			report.setCashReceiptNumber(cashTransactionSummary.getTotalNumber());
			report.setCashReceiptAmount(cashTransactionSummary.getTotalAmount() + cashTransactionSummary.getGratuityAmount());

			report.setCreditCardReceiptNumber(creditCardTransactionSummary.getTotalNumber());
			report.setCreditCardReceiptAmount(creditCardTransactionSummary.getTotalAmount() + creditCardTransactionSummary.getGratuityAmount());

			report.setDebitCardReceiptNumber(debitCardTransactionSummary.getTotalNumber());
			report.setDebitCardReceiptAmount(debitCardTransactionSummary.getTotalAmount() + debitCardTransactionSummary.getGratuityAmount());

			RefundSummary refundSummary = new RefundTransactionDAO().getTotalRefundForTerminal(terminal);
			report.setCashBack(refundSummary.getAmount());

			report.setTipsPaid(TicketDAO.getInstance().getPaidGratuityAmount(terminal));

			double totalPayout = 0;
			List<PayOutTransaction> payoutTransactions = new PayOutTransactionDAO().getUnsettled(terminal);
			for (PayOutTransaction transaction : payoutTransactions) {
				totalPayout += transaction.getTotalAmount();
			}
			report.setPayOutNumber(payoutTransactions.size());
			report.setPayOutAmount(totalPayout);

			double drawerBleedAmount = 0;
			List<CashDropTransaction> cashDrops = new CashDropTransactionDAO().findUnsettled(terminal);
			for (CashDropTransaction transaction : cashDrops) {
				drawerBleedAmount += transaction.getTotalAmount();
			}
			report.setDrawerBleedNumber(cashDrops.size());
			report.setDrawerBleedAmount(drawerBleedAmount);

			report.setBeginCash(terminal.getOpeningBalance());
			report.setCashToDeposit(terminal.getCurrentBalance());

			//void
			criteria = session.createCriteria(Ticket.class, "t");
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			list = criteria.list();

			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				DrawerPullVoidTicketEntry entry = new DrawerPullVoidTicketEntry();
				entry.setCode(ticket.getId());
				entry.setAmount(ticket.getSubtotalAmount());
				//entry.setQuantity()
				entry.setReason(ticket.getVoidReason());

				report.addVoidTicketEntry(entry);
			}
			
			//gift cert
			criteria = session.createCriteria(GiftCertificateTransaction.class);
			criteria.add(Restrictions.eq(GiftCertificateTransaction.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(GiftCertificateTransaction.PROP_TERMINAL, terminal));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(GiftCertificateTransaction.PROP_FACE_VALUE));
			projectionList.add(Projections.sum(GiftCertificateTransaction.PROP_CASH_BACK_AMOUNT));
			criteria.setProjection(projectionList);
			Object[] o = (Object[]) criteria.uniqueResult();
			if(o.length > 0 && o[0] instanceof Number) {
				double amount = ((Number) o[0]).doubleValue();
				report.setGiftCertReturnAmount(amount);
			}
			if(o.length > 1 && o[1] instanceof Number) {
				double amount = ((Number) o[1]).doubleValue();
				report.setGiftCertChangeAmount(amount);
			}

			//discount, coupon
			int totalDiscountCount = 0;
			double totalDiscountAmount = 0;
			double totalDiscountSales = 0;
			int totalDiscountGuest = 0;
			int totalDiscountPartySize = 0;
			int totalDiscountCheckSize = 0;
			double totalDiscountPercentage = 0;
			double totalDiscountRatio = 0;

			criteria = session.createCriteria(Ticket.class, "t");
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			list = criteria.list();

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
			if(session != null) session.close();
		}
	}
}
