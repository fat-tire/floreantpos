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
package com.floreantpos.report.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.DebitCardTransaction;
import com.floreantpos.model.Discount;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.GiftCertificateTransaction;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.PayOutTransaction;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TransactionType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.DiscountDAO;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.report.JournalReportModel;
import com.floreantpos.report.JournalReportModel.JournalReportData;
import com.floreantpos.report.MenuUsageReport;
import com.floreantpos.report.MenuUsageReport.MenuUsageReportData;
import com.floreantpos.report.SalesBalanceReport;
import com.floreantpos.report.SalesDetailedReport;
import com.floreantpos.report.SalesDetailedReport.DrawerPullData;
import com.floreantpos.report.SalesExceptionReport;
import com.floreantpos.report.ServerProductivityReport;
import com.floreantpos.report.ServerProductivityReport.ServerProductivityReportData;

public class ReportService {
	private static SimpleDateFormat fullDateFormatter = new SimpleDateFormat("MMM dd yyyy, hh:mm a"); //$NON-NLS-1$
	private static SimpleDateFormat shortDateFormatter = new SimpleDateFormat("MMM dd yyyy "); //$NON-NLS-1$

	public static String formatFullDate(Date date) {
		return fullDateFormatter.format(date);
	}

	public static String formatShortDate(Date date) {
		return shortDateFormatter.format(date);
	}

	public MenuUsageReport getMenuUsageReport(Date fromDate, Date toDate) {
		GenericDAO dao = new GenericDAO();
		MenuUsageReport report = new MenuUsageReport();
		Session session = null;

		try {

			session = dao.getSession();

			Criteria criteria = session.createCriteria(MenuCategory.class);
			List<MenuCategory> categories = criteria.list();
			MenuCategory miscCategory = new MenuCategory();
			miscCategory.setName(com.floreantpos.POSConstants.MISC_BUTTON_TEXT);
			categories.add(miscCategory);

			for (MenuCategory category : categories) {
				criteria = session.createCriteria(TicketItem.class, "item"); //$NON-NLS-1$
				criteria.createCriteria("ticket", "t"); //$NON-NLS-1$ //$NON-NLS-2$
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
				projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
				projectionList.add(Projections.sum(TicketItem.PROP_DISCOUNT_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.eq("item." + TicketItem.PROP_CATEGORY_NAME, category.getName())); //$NON-NLS-1$
				criteria.add(Restrictions.ge("t." + Ticket.PROP_CREATE_DATE, fromDate)); //$NON-NLS-1$
				criteria.add(Restrictions.le("t." + Ticket.PROP_CREATE_DATE, toDate)); //$NON-NLS-1$
				criteria.add(Restrictions.eq("t." + Ticket.PROP_PAID, Boolean.TRUE)); //$NON-NLS-1$

				List datas = criteria.list();
				if (datas.size() > 0) {
					Object[] objects = (Object[]) datas.get(0);

					MenuUsageReportData data = new MenuUsageReportData();
					data.setCategoryName(category.getName());

					if (objects.length > 0 && objects[0] != null)
						data.setCount(((Number) objects[0]).intValue());

					if (objects.length > 1 && objects[1] != null)
						data.setGrossSales(((Number) objects[1]).doubleValue());

					if (objects.length > 2 && objects[2] != null)
						data.setDiscount(((Number) objects[2]).doubleValue());

					data.calculate();
					report.addReportData(data);
				}
			}

			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public ServerProductivityReport getServerProductivityReport(Date fromDate, Date toDate) {
		GenericDAO dao = new GenericDAO();
		ServerProductivityReport report = new ServerProductivityReport();
		Session session = null;

		try {

			session = dao.getSession();

			Criteria criteria = session.createCriteria(User.class);
			//criteria.add(Restrictions.eq(User.PROP_USER_TYPE, User.USER_TYPE_SERVER));
			List<User> servers = criteria.list();

			criteria = session.createCriteria(MenuCategory.class);
			List<MenuCategory> categories = criteria.list();
			MenuCategory miscCategory = new MenuCategory();
			miscCategory.setName(com.floreantpos.POSConstants.MISC_BUTTON_TEXT);
			categories.add(miscCategory);

			for (User server : servers) {
				ServerProductivityReportData data = new ServerProductivityReportData();
				data.setServerName(server.getUserId() + "/" + server.toString()); //$NON-NLS-1$
				criteria = session.createCriteria(Ticket.class);
				criteria.add(Restrictions.eq(Ticket.PROP_OWNER, server));
				criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.TRUE));
				criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE)); //$NON-NLS-1$
				criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE)); //$NON-NLS-1$
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));

				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_NUMBER_OF_GUESTS));
				projectionList.add(Projections.sum(TicketItem.PROP_TOTAL_AMOUNT));

				criteria.setProjection(projectionList);

				Object[] o = (Object[]) criteria.uniqueResult();
				int totalCheckCount = 0;
				double totalServerSale = 0;
				if (o != null) {
					if (o.length > 0 && o[0] != null) {
						int i = ((Number) o[0]).intValue();
						data.setTotalCheckCount(totalCheckCount = i);
					}
					if (o.length > 1 && o[1] != null) {
						int i = ((Number) o[1]).intValue();
						data.setTotalGuestCount(i);
					}
					if (o.length > 2 && o[2] != null) {
						totalServerSale = ((Number) o[2]).doubleValue();
						data.setTotalSales(totalServerSale);
					}
				}

				data.calculate();
				report.addReportData(data);

				for (MenuCategory category : categories) {
					data = new ServerProductivityReportData();
					data.setServerName(server.getUserId() + "/" + server.toString()); //$NON-NLS-1$

					criteria = session.createCriteria(TicketItem.class, "item"); //$NON-NLS-1$
					criteria.createCriteria(TicketItem.PROP_TICKET, "t"); //$NON-NLS-1$

					projectionList = Projections.projectionList();
					criteria.setProjection(projectionList);
					projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
					projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
					projectionList.add(Projections.sum("t." + Ticket.PROP_DISCOUNT_AMOUNT)); //$NON-NLS-1$
					projectionList.add(Projections.rowCount());

					criteria.add(Restrictions.eq("item." + TicketItem.PROP_CATEGORY_NAME, category.getName())); //$NON-NLS-1$
					criteria.add(Restrictions.ge("t." + Ticket.PROP_CREATE_DATE, fromDate)); //$NON-NLS-1$
					criteria.add(Restrictions.le("t." + Ticket.PROP_CREATE_DATE, toDate)); //$NON-NLS-1$
					criteria.add(Restrictions.eq("t." + Ticket.PROP_OWNER, server)); //$NON-NLS-1$
					criteria.add(Restrictions.eq("t." + Ticket.PROP_PAID, Boolean.TRUE)); //$NON-NLS-1$
					criteria.add(Restrictions.eq("t." + Ticket.PROP_VOIDED, Boolean.FALSE)); //$NON-NLS-1$
					criteria.add(Restrictions.eq("t." + Ticket.PROP_REFUNDED, Boolean.FALSE)); //$NON-NLS-1$

					List datas = criteria.list();
					if (datas.size() > 0) {
						Object[] objects = (Object[]) datas.get(0);

						data.setCategoryName(category.getName());
						data.setTotalCheckCount(totalCheckCount);
						if (objects.length > 0 && objects[0] != null) {
							int i = ((Number) objects[0]).intValue();
							data.setCheckCount(i);
						}

						if (objects.length > 1 && objects[1] != null) {
							double d = ((Number) objects[1]).doubleValue();
							data.setGrossSales(d);
						}

						if (objects.length > 2 && objects[2] != null) {
							double d = ((Number) objects[2]).doubleValue();
							if (d > 0)
								data.setSalesDiscount(d);
						}
						data.setAllocation((data.getGrossSales() / totalServerSale) * 100.0);
						data.calculate();
						report.addReportData(data);
					}
				}
			}
			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public JournalReportModel getJournalReport(Date fromDate, Date toDate) {
		GenericDAO dao = new GenericDAO();
		JournalReportModel report = new JournalReportModel();
		Session session = null;

		report.setFromDate(fromDate);
		report.setToDate(toDate);
		report.setReportTime(new Date());
		try {

			session = dao.getSession();
			Criteria criteria = session.createCriteria(ActionHistory.class);
			criteria.add(Restrictions.ge(ActionHistory.PROP_ACTION_TIME, fromDate));
			criteria.add(Restrictions.le(ActionHistory.PROP_ACTION_TIME, toDate));
			List<ActionHistory> list = criteria.list();

			for (ActionHistory history : list) {
				JournalReportData data = new JournalReportData();
				data.setRefId(history.getId());
				data.setAction(history.getActionName());
				data.setUserInfo(history.getPerformer().getUserId() + "/" + history.getPerformer()); //$NON-NLS-1$
				data.setTime(history.getActionTime());
				data.setComments(history.getDescription());
				report.addReportData(data);
			}

			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public SalesBalanceReport getSalesBalanceReport(Date fromDate, Date toDate, User user) {
		GenericDAO dao = new GenericDAO();
		SalesBalanceReport report = new SalesBalanceReport();
		Session session = null;

		report.setFromDate(fromDate);
		report.setToDate(toDate);
		report.setReportTime(new Date());
		try {

			session = dao.getSession();

			//gross taxable sales
			report.setGrossTaxableSalesAmount(calculateGrossSales(session, fromDate, toDate, user, true));
			//gross non-taxable sales
			report.setGrossNonTaxableSalesAmount(calculateGrossSales(session, fromDate, toDate, user, false));
			//discount
			report.setDiscountAmount(calculateDiscount(session, fromDate, toDate, user));
			//tax
			report.setSalesTaxAmount(calculateTax(session, fromDate, toDate, user));
			report.setChargedTipsAmount(calculateTips(session, fromDate, toDate, user));

			report.setCashReceiptsAmount(calculateCreditReceipt(session, CashTransaction.class, fromDate, toDate, user));
			report.setCreditCardReceiptsAmount(calculateCreditReceipt(session, CreditCardTransaction.class, fromDate, toDate, user));

			//report.setGiftCertSalesAmount(calculateGiftCertSoldAmount(session, fromDate, toDate));
			//report.setGiftCertReturnAmount(calculateCreditReceipt(session, GiftCertificateTransaction.class, fromDate, toDate));

			//			
			////			gift cert
			//			criteria = session.createCriteria(GiftCertificateTransaction.class);
			//			criteria.createAlias(GiftCertificateTransaction.PROP_TICKET, "t");
			//			criteria.add(Restrictions.ge("t." + Ticket.PROP_CREATE_DATE, fromDate));
			//			criteria.add(Restrictions.le("t." + Ticket.PROP_CREATE_DATE, toDate));
			//			criteria.add(Restrictions.eq("t." + Ticket.PROP_VOIDED, Boolean.FALSE));
			//			criteria.add(Restrictions.eq("t." + Ticket.PROP_REFUNDED, Boolean.FALSE));
			//			projectionList = Projections.projectionList();
			//			projectionList.add(Projections.sum(PosTransaction.PROP_GIFT_CERT_FACE_VALUE));
			//			projectionList.add(Projections.sum(PosTransaction.PROP_GIFT_CERT_CASH_BACK_AMOUNT));
			//			criteria.setProjection(projectionList);
			//			Object[] o = (Object[]) criteria.uniqueResult();
			//			if(o.length > 0 && o[0] instanceof Number) {
			//				double amount = ((Number) o[0]).doubleValue();
			//				report.setGiftCertReturnAmount(amount);
			//			}
			//			if(o.length > 1 && o[1] instanceof Number) {
			//				double amount = ((Number) o[1]).doubleValue();
			//				report.setGiftCertChangeAmount(amount);
			//			}
			//			
			//			tips paid
			report.setGrossTipsPaidAmount(calculateTipsPaid(session, fromDate, toDate, user));
			//			
			//cash payout
			report.setCashPayoutAmount(calculateCashPayout(session, fromDate, toDate, user));
			//			
			//drawer pulls
			calculateDrawerPullAmount(session, report, fromDate, toDate, user);

			// visa card summery
			report.setVisaCreditCardAmount(calculateVisaCreditCardSummery(session, CreditCardTransaction.class, fromDate, toDate, user));
			report.setMasterCardAmount(calculateMasterCardSummery(session, CreditCardTransaction.class, fromDate, toDate, user));
			report.setAmexAmount(calculateAmexSummery(session, CreditCardTransaction.class, fromDate, toDate, user));
			report.setDiscoveryAmount(calculateDiscoverySummery(session, CreditCardTransaction.class, fromDate, toDate, user));

			report.calculate();
			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private void calculateDrawerPullAmount(Session session, SalesBalanceReport report, Date fromDate, Date toDate, User user) {
		Criteria criteria = session.createCriteria(DrawerPullReport.class);
		criteria.add(Restrictions.ge(DrawerPullReport.PROP_REPORT_TIME, fromDate));
		criteria.add(Restrictions.le(DrawerPullReport.PROP_REPORT_TIME, toDate));

		if (user != null) {
			criteria.add(Restrictions.eq(DrawerPullReport.PROP_ASSIGNED_USER, user));
		}

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.sum(DrawerPullReport.PROP_DRAWER_ACCOUNTABLE));
		projectionList.add(Projections.sum(DrawerPullReport.PROP_BEGIN_CASH));
		criteria.setProjection(projectionList);

		Object[] o = (Object[]) criteria.uniqueResult();
		if (o.length > 0 && o[0] instanceof Number) {
			double amount = ((Number) o[0]).doubleValue();
			report.setDrawerPullsAmount(amount);
		}
		if (o.length > 1 && o[1] instanceof Number) {
			double amount = ((Number) o[1]).doubleValue();
			report.setDrawerPullsAmount(report.getDrawerPullsAmount() - amount);
		}
	}

	private double calculateCashPayout(Session session, Date fromDate, Date toDate, User user) {
		Criteria criteria = session.createCriteria(PayOutTransaction.class);
		criteria.add(Restrictions.ge(PayOutTransaction.PROP_TRANSACTION_TIME, fromDate));
		criteria.add(Restrictions.le(PayOutTransaction.PROP_TRANSACTION_TIME, toDate));

		if (user != null) {
			criteria.add(Restrictions.eq(PayOutTransaction.PROP_USER, user));
		}

		criteria.setProjection(Projections.sum(PayOutTransaction.PROP_AMOUNT));

		return getDoubleAmount(criteria.uniqueResult());
	}

	private double calculateTipsPaid(Session session, Date fromDate, Date toDate, User user) {
		Criteria criteria = session.createCriteria(Ticket.class);
		criteria.createAlias(Ticket.PROP_GRATUITY, "gratuity"); //$NON-NLS-1$
		criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
		criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));

		criteria.add(Restrictions.eq("gratuity." + Gratuity.PROP_PAID, Boolean.TRUE)); //$NON-NLS-1$

		if (user != null) {
			criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
		}

		criteria.setProjection(Projections.sum("gratuity." + Gratuity.PROP_AMOUNT)); //$NON-NLS-1$

		return getDoubleAmount(criteria.uniqueResult());
	}

	private double calculateCreditReceipt(Session session, Class transactionClass, Date fromDate, Date toDate, User user) {
		//cash receipt
		Criteria criteria = session.createCriteria(transactionClass);
		criteria.add(Restrictions.ge(PosTransaction.PROP_TRANSACTION_TIME, fromDate));
		criteria.add(Restrictions.le(PosTransaction.PROP_TRANSACTION_TIME, toDate));
		criteria.add(Restrictions.eq(PosTransaction.PROP_TRANSACTION_TYPE, TransactionType.CREDIT.name()));

		if (user != null) {
			criteria.add(Restrictions.eq(PosTransaction.PROP_USER, user));
		}

		criteria.setProjection(Projections.sum(CashTransaction.PROP_AMOUNT));

		return getDoubleAmount(criteria.uniqueResult());
	}

	private double calculateCashReceipt(Session session, Date fromDate, Date toDate) {
		//cash receipt
		Criteria criteria = session.createCriteria(CashTransaction.class);
		criteria.add(Restrictions.ge(CashTransaction.PROP_TRANSACTION_TIME, fromDate));
		criteria.add(Restrictions.le(CashTransaction.PROP_TRANSACTION_TIME, toDate));
		criteria.add(Restrictions.eq(CashTransaction.PROP_TRANSACTION_TYPE, TransactionType.CREDIT.name()));

		criteria.setProjection(Projections.sum(CashTransaction.PROP_AMOUNT));

		return getDoubleAmount(criteria.uniqueResult());
	}

	private double calculateCreditCardReceipt(Session session, Date fromDate, Date toDate) {
		//cash receipt
		Criteria criteria = session.createCriteria(CashTransaction.class);
		criteria.add(Restrictions.ge(CreditCardTransaction.PROP_TRANSACTION_TIME, fromDate));
		criteria.add(Restrictions.le(CreditCardTransaction.PROP_TRANSACTION_TIME, toDate));
		criteria.add(Restrictions.eq(CreditCardTransaction.PROP_TRANSACTION_TYPE, TransactionType.CREDIT.name()));

		criteria.setProjection(Projections.sum(CashTransaction.PROP_AMOUNT));

		return getDoubleAmount(criteria.uniqueResult());
	}

	private double calculateGiftCertSoldAmount(Session session, Date fromDate, Date toDate) {
		//cash receipt
		Criteria criteria = session.createCriteria(GiftCertificateTransaction.class);
		criteria.add(Restrictions.ge(GiftCertificateTransaction.PROP_TRANSACTION_TIME, fromDate));
		criteria.add(Restrictions.le(GiftCertificateTransaction.PROP_TRANSACTION_TIME, toDate));
		criteria.add(Restrictions.eq(GiftCertificateTransaction.PROP_TRANSACTION_TYPE, TransactionType.CREDIT.name()));

		criteria.setProjection(Projections.sum(GiftCertificateTransaction.PROP_GIFT_CERT_FACE_VALUE));

		return getDoubleAmount(criteria.uniqueResult());
	}

	private double calculateTips(Session session, Date fromDate, Date toDate, User user) {
		//tips
		Criteria criteria = session.createCriteria(Ticket.class);
		criteria.createAlias(Ticket.PROP_GRATUITY, "g"); //$NON-NLS-1$
		criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
		criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));

		if (user != null) {
			criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
		}
		//FIXME: HOW ABOUT TIPS ON VOID OR REFUNDED TICKET?

		//criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
		//criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));

		criteria.setProjection(Projections.sum("g." + Gratuity.PROP_AMOUNT)); //$NON-NLS-1$

		return getDoubleAmount(criteria.uniqueResult());
	}

	private double calculateDiscount(Session session, Date fromDate, Date toDate, User user) {
		//discounts
		Criteria criteria = session.createCriteria(Ticket.class);
		criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
		criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
		criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
		criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));

		if (user != null) {
			criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
		}

		criteria.setProjection(Projections.sum(Ticket.PROP_DISCOUNT_AMOUNT));

		return getDoubleAmount(criteria.uniqueResult());
	}

	private double getDoubleAmount(Object result) {
		if (result != null && result instanceof Number) {
			return ((Number) result).doubleValue();
		}
		return 0;
	}

	private double calculateTax(Session session, Date fromDate, Date toDate, User user) {
		//discounts
		Criteria criteria = session.createCriteria(Ticket.class);
		criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
		criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
		criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
		criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));

		if (user != null) {
			criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
		}

		criteria.setProjection(Projections.sum(Ticket.PROP_TAX_AMOUNT));

		return getDoubleAmount(criteria.uniqueResult());
	}

	private double calculateGrossSales(Session session, Date fromDate, Date toDate, User user, boolean taxableSales) {
		Criteria criteria = session.createCriteria(Ticket.class);
		criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
		criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
		criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
		criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));

		if (user != null) {
			criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
		}

		criteria.add(Restrictions.eq(Ticket.PROP_TAX_EXEMPT, Boolean.valueOf(!taxableSales)));

		criteria.setProjection(Projections.sum(Ticket.PROP_SUBTOTAL_AMOUNT));

		return getDoubleAmount(criteria.uniqueResult());
	}

	public SalesExceptionReport getSalesExceptionReport(Date fromDate, Date toDate) {
		GenericDAO dao = new GenericDAO();
		SalesExceptionReport report = new SalesExceptionReport();
		Session session = null;

		report.setFromDate(fromDate);
		report.setToDate(toDate);
		report.setReportTime(new Date());
		try {

			session = dao.getSession();

			//gross taxable sales

			//void tickets
			Criteria criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.TRUE));

			List list = criteria.list();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				report.addVoidToVoidData(ticket);
			}

			//discounts
			criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));

			list = criteria.list();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				report.addDiscountData(ticket);
			}

			//find all valid discounts
			DiscountDAO discountDAO = new DiscountDAO();
			List<Discount> availableCoupons = discountDAO.getValidCoupons();
			report.addEmptyDiscounts(availableCoupons);

			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public SalesDetailedReport getSalesDetailedReport(Date fromDate, Date toDate) {
		GenericDAO dao = new GenericDAO();
		SalesDetailedReport report = new SalesDetailedReport();
		Session session = null;

		report.setFromDate(fromDate);
		report.setToDate(toDate);
		report.setReportTime(new Date());
		try {

			session = dao.getSession();

			Criteria criteria = session.createCriteria(DrawerPullReport.class);
			criteria.add(Restrictions.ge(DrawerPullReport.PROP_REPORT_TIME, fromDate));
			criteria.add(Restrictions.le(DrawerPullReport.PROP_REPORT_TIME, toDate));
			List list = criteria.list();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				DrawerPullReport drawerPullReport = (DrawerPullReport) iter.next();
				DrawerPullData data = new DrawerPullData();
				data.setDrawerPullId(drawerPullReport.getId());
				data.setTicketCount(drawerPullReport.getTicketCount());
				data.setIdealAmount(drawerPullReport.getDrawerAccountable());
				data.setActualAmount(drawerPullReport.getCashToDeposit());
				data.setVarinceAmount(drawerPullReport.getDrawerAccountable() - drawerPullReport.getCashToDeposit());
				report.addDrawerPullData(data);
			}

			criteria = session.createCriteria(CreditCardTransaction.class);
			criteria.add(Restrictions.ge(CreditCardTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(CreditCardTransaction.PROP_TRANSACTION_TIME, toDate));
			list = criteria.list();

			for (Iterator iter = list.iterator(); iter.hasNext();) {
				CreditCardTransaction t = (CreditCardTransaction) iter.next();
				report.addCreditCardData(t);
			}

			criteria = session.createCriteria(DebitCardTransaction.class);
			criteria.add(Restrictions.ge(DebitCardTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(DebitCardTransaction.PROP_TRANSACTION_TIME, toDate));
			list = criteria.list();

			for (Iterator iter = list.iterator(); iter.hasNext();) {
				DebitCardTransaction t = (DebitCardTransaction) iter.next();
				report.addCreditCardData(t);
			}

			criteria = session.createCriteria(GiftCertificateTransaction.class);
			criteria.add(Restrictions.ge(GiftCertificateTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(GiftCertificateTransaction.PROP_TRANSACTION_TIME, toDate));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum(GiftCertificateTransaction.PROP_AMOUNT));
			criteria.setProjection(projectionList);
			Object[] object = (Object[]) criteria.uniqueResult();
			if (object != null && object.length > 0 && object[0] instanceof Number) {
				report.setGiftCertReturnCount(((Number) object[0]).intValue());
			}
			if (object != null && object.length > 1 && object[1] instanceof Number) {
				report.setGiftCertReturnAmount(((Number) object[1]).doubleValue());
			}

			criteria = session.createCriteria(GiftCertificateTransaction.class);
			criteria.add(Restrictions.ge(GiftCertificateTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(GiftCertificateTransaction.PROP_TRANSACTION_TIME, toDate));
			criteria.add(Restrictions.gt(GiftCertificateTransaction.PROP_GIFT_CERT_CASH_BACK_AMOUNT, Double.valueOf(0)));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum(GiftCertificateTransaction.PROP_GIFT_CERT_CASH_BACK_AMOUNT));
			criteria.setProjection(projectionList);
			object = (Object[]) criteria.uniqueResult();
			if (object != null && object.length > 0 && object[0] instanceof Number) {
				report.setGiftCertChangeCount(((Number) object[0]).intValue());
			}
			if (object != null && object.length > 1 && object[1] instanceof Number) {
				report.setGiftCertChangeAmount(((Number) object[1]).doubleValue());
			}

			criteria = session.createCriteria(Ticket.class);
			criteria.createAlias(Ticket.PROP_GRATUITY, "g"); //$NON-NLS-1$
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.gt("g." + Gratuity.PROP_AMOUNT, Double.valueOf(0))); //$NON-NLS-1$
			projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum("g." + Gratuity.PROP_AMOUNT)); //$NON-NLS-1$
			criteria.setProjection(projectionList);
			object = (Object[]) criteria.uniqueResult();
			if (object != null && object.length > 0 && object[0] instanceof Number) {
				report.setTipsCount(((Number) object[0]).intValue());
			}
			if (object != null && object.length > 1 && object[1] instanceof Number) {
				report.setChargedTips(((Number) object[1]).doubleValue());
			}

			criteria = session.createCriteria(Ticket.class);
			criteria.createAlias(Ticket.PROP_GRATUITY, "g"); //$NON-NLS-1$
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.gt("g." + Gratuity.PROP_AMOUNT, Double.valueOf(0))); //$NON-NLS-1$
			criteria.add(Restrictions.gt("g." + Gratuity.PROP_PAID, Boolean.TRUE)); //$NON-NLS-1$
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum("g." + Gratuity.PROP_AMOUNT)); //$NON-NLS-1$
			criteria.setProjection(projectionList);
			object = (Object[]) criteria.uniqueResult();
			if (object != null && object.length > 0 && object[0] instanceof Number) {
				report.setTipsPaid(((Number) object[0]).doubleValue());
			}

			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private double calculateVisaCreditCardSummery(Session session, Class transactionClass, Date fromDate, Date toDate, User user) {
		//cash receipt
		Criteria criteria = session.createCriteria(transactionClass);
		criteria.add(Restrictions.ge(PosTransaction.PROP_TRANSACTION_TIME, fromDate));
		criteria.add(Restrictions.le(PosTransaction.PROP_TRANSACTION_TIME, toDate));
		criteria.add(Restrictions.eq(PosTransaction.PROP_TRANSACTION_TYPE, TransactionType.CREDIT.name()));
		criteria.add(Restrictions.eq(PosTransaction.PROP_CARD_TYPE, PaymentType.CREDIT_VISA.getDisplayString()));

		if (user != null) {
			criteria.add(Restrictions.eq(PosTransaction.PROP_USER, user));
		}

		criteria.setProjection(Projections.sum(CashTransaction.PROP_AMOUNT));

		return getDoubleAmount(criteria.uniqueResult());
	}

	private double calculateMasterCardSummery(Session session, Class transactionClass, Date fromDate, Date toDate, User user) {
		//cash receipt
		Criteria criteria = session.createCriteria(transactionClass);
		criteria.add(Restrictions.ge(PosTransaction.PROP_TRANSACTION_TIME, fromDate));
		criteria.add(Restrictions.le(PosTransaction.PROP_TRANSACTION_TIME, toDate));
		criteria.add(Restrictions.eq(PosTransaction.PROP_TRANSACTION_TYPE, TransactionType.CREDIT.name()));
		criteria.add(Restrictions.eq(PosTransaction.PROP_CARD_TYPE, PaymentType.CREDIT_MASTER_CARD.getDisplayString()));

		if (user != null) {
			criteria.add(Restrictions.eq(PosTransaction.PROP_USER, user));
		}

		criteria.setProjection(Projections.sum(CashTransaction.PROP_AMOUNT));

		return getDoubleAmount(criteria.uniqueResult());
	}

	private double calculateAmexSummery(Session session, Class transactionClass, Date fromDate, Date toDate, User user) {
		//cash receipt
		Criteria criteria = session.createCriteria(transactionClass);
		criteria.add(Restrictions.ge(PosTransaction.PROP_TRANSACTION_TIME, fromDate));
		criteria.add(Restrictions.le(PosTransaction.PROP_TRANSACTION_TIME, toDate));
		criteria.add(Restrictions.eq(PosTransaction.PROP_TRANSACTION_TYPE, TransactionType.CREDIT.name()));
		criteria.add(Restrictions.eq(PosTransaction.PROP_CARD_TYPE, PaymentType.CREDIT_AMEX.getDisplayString()));

		if (user != null) {
			criteria.add(Restrictions.eq(PosTransaction.PROP_USER, user));
		}

		criteria.setProjection(Projections.sum(CashTransaction.PROP_AMOUNT));

		return getDoubleAmount(criteria.uniqueResult());
	}

	private double calculateDiscoverySummery(Session session, Class transactionClass, Date fromDate, Date toDate, User user) {
		//cash receipt
		Criteria criteria = session.createCriteria(transactionClass);
		criteria.add(Restrictions.ge(PosTransaction.PROP_TRANSACTION_TIME, fromDate));
		criteria.add(Restrictions.le(PosTransaction.PROP_TRANSACTION_TIME, toDate));
		criteria.add(Restrictions.eq(PosTransaction.PROP_TRANSACTION_TYPE, TransactionType.CREDIT.name()));
		criteria.add(Restrictions.eq(PosTransaction.PROP_CARD_TYPE, PaymentType.CREDIT_DISCOVERY.getDisplayString()));

		if (user != null) {
			criteria.add(Restrictions.eq(PosTransaction.PROP_USER, user));
		}

		criteria.setProjection(Projections.sum(CashTransaction.PROP_AMOUNT));

		return getDoubleAmount(criteria.uniqueResult());
	}
}
