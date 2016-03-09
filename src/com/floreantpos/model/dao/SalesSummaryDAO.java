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
package com.floreantpos.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.User;
import com.floreantpos.model.UserType;
import com.floreantpos.report.SalesAnalysisReportModel.SalesAnalysisData;
import com.floreantpos.report.SalesStatistics;
import com.floreantpos.report.SalesStatistics.ShiftwiseSalesTableData;

public class SalesSummaryDAO extends _RootDAO {

	public SalesSummaryDAO() {
		super();
	}

	@Override
	protected Class getReferenceClass() {
		return null;
	}

	@Override
	public Serializable save(Object obj) {
		return super.save(obj);
	}

	@Override
	public void saveOrUpdate(Object obj) {
		super.saveOrUpdate(obj);
	}

	public List<SalesAnalysisData> findSalesAnalysis(Date start, Date end, UserType userType, Terminal terminal) {
		Session session = null;

		try {
			ArrayList<SalesAnalysisData> list = new ArrayList<SalesAnalysisData>();

			session = getSession();

			Criteria criteria = session.createCriteria(Shift.class);
			List<Shift> shifts = criteria.list();

			criteria = session.createCriteria(MenuCategory.class);
			List<MenuCategory> categories = criteria.list();
			MenuCategory miscCategory = new MenuCategory();
			miscCategory.setName(Messages.getString("SalesSummaryDAO.0")); //$NON-NLS-1$
			categories.add(miscCategory);

			//find food sales
			criteria = session.createCriteria(TicketItem.class, "item"); //$NON-NLS-1$
			criteria.createCriteria("ticket", "t"); //$NON-NLS-1$ //$NON-NLS-2$
			criteria.createCriteria("t.owner", "u"); //$NON-NLS-1$ //$NON-NLS-2$
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
			projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
			projectionList.add(Projections.sum(TicketItem.PROP_DISCOUNT_AMOUNT));
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("item." + TicketItem.PROP_BEVERAGE, Boolean.FALSE)); //$NON-NLS-1$
			criteria.add(Restrictions.ge("t." + Ticket.PROP_ACTIVE_DATE, start)); //$NON-NLS-1$
			criteria.add(Restrictions.le("t." + Ticket.PROP_ACTIVE_DATE, end)); //$NON-NLS-1$

			if (userType != null) {
				criteria.add(Restrictions.eq("u." + User.PROP_TYPE, userType)); //$NON-NLS-1$
			}
			if (terminal != null) {
				criteria.add(Restrictions.eq("t." + Ticket.PROP_TERMINAL, terminal)); //$NON-NLS-1$
			}
			List datas = criteria.list();
			if (datas.size() > 0) {
				Object[] objects = (Object[]) datas.get(0);

				SalesAnalysisData data = new SalesAnalysisData();
				data.setShiftName(""); //$NON-NLS-1$
				data.setCategoryName(Messages.getString("SalesSummaryDAO.1")); //$NON-NLS-1$

				if (objects.length > 0 && objects[0] != null)
					data.setCount(((Number) objects[0]).intValue());

				if (objects.length > 1 && objects[1] != null)
					data.setGross(((Number) objects[1]).doubleValue());

				if (objects.length > 2 && objects[2] != null)
					data.setDiscount(((Number) objects[2]).doubleValue());

				data.calculate();
				list.add(data);
			}

			//find non food sales
			criteria = session.createCriteria(TicketItem.class, "item"); //$NON-NLS-1$
			criteria.createCriteria("ticket", "t"); //$NON-NLS-1$ //$NON-NLS-2$
			criteria.createCriteria("t.owner", "u"); //$NON-NLS-1$ //$NON-NLS-2$
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
			projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
			projectionList.add(Projections.sum(TicketItem.PROP_DISCOUNT_AMOUNT));
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("item." + TicketItem.PROP_BEVERAGE, Boolean.TRUE)); //$NON-NLS-1$
			criteria.add(Restrictions.ge("t." + Ticket.PROP_ACTIVE_DATE, start)); //$NON-NLS-1$
			criteria.add(Restrictions.le("t." + Ticket.PROP_ACTIVE_DATE, end)); //$NON-NLS-1$

			if (userType != null) {
				criteria.add(Restrictions.eq("u." + User.PROP_TYPE, userType)); //$NON-NLS-1$
			}
			if (terminal != null) {
				criteria.add(Restrictions.eq("t." + Ticket.PROP_TERMINAL, terminal)); //$NON-NLS-1$
			}
			datas = criteria.list();
			if (datas.size() > 0) {
				Object[] objects = (Object[]) datas.get(0);

				SalesAnalysisData data = new SalesAnalysisData();
				data.setShiftName(""); //$NON-NLS-1$
				data.setCategoryName(Messages.getString("SalesSummaryDAO.2")); //$NON-NLS-1$

				if (objects.length > 0 && objects[0] != null)
					data.setCount(((Number) objects[0]).intValue());

				if (objects.length > 1 && objects[1] != null)
					data.setGross(((Number) objects[1]).doubleValue());

				if (objects.length > 2 && objects[2] != null)
					data.setDiscount(((Number) objects[2]).doubleValue());

				data.calculate();
				list.add(data);
			}

			//find shift wise salse
			for (Shift shift : shifts) {

				for (MenuCategory category : categories) {

					criteria = session.createCriteria(TicketItem.class, "item"); //$NON-NLS-1$
					criteria.createCriteria("ticket", "t"); //$NON-NLS-1$ //$NON-NLS-2$
					criteria.createCriteria("t.owner", "u"); //$NON-NLS-1$ //$NON-NLS-2$
					projectionList = Projections.projectionList();
					projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
					projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
					projectionList.add(Projections.sum(TicketItem.PROP_DISCOUNT_AMOUNT));
					criteria.setProjection(projectionList);
					criteria.add(Restrictions.eq("item." + TicketItem.PROP_CATEGORY_NAME, category.getName())); //$NON-NLS-1$
					criteria.add(Restrictions.eq("t." + Ticket.PROP_SHIFT, shift)); //$NON-NLS-1$
					criteria.add(Restrictions.ge("t." + Ticket.PROP_ACTIVE_DATE, start)); //$NON-NLS-1$
					criteria.add(Restrictions.le("t." + Ticket.PROP_ACTIVE_DATE, end)); //$NON-NLS-1$

					if (userType != null) {
						criteria.add(Restrictions.eq("u." + User.PROP_TYPE, userType)); //$NON-NLS-1$
					}
					if (terminal != null) {
						criteria.add(Restrictions.eq("t." + Ticket.PROP_TERMINAL, terminal)); //$NON-NLS-1$
					}
					datas = criteria.list();
					if (datas.size() > 0) {
						Object[] objects = (Object[]) datas.get(0);

						SalesAnalysisData data = new SalesAnalysisData();
						data.setShiftName(shift.getName());
						data.setCategoryName(category.getName());

						if (objects.length > 0 && objects[0] != null)
							data.setCount(((Number) objects[0]).intValue());

						if (objects.length > 1 && objects[1] != null)
							data.setGross(((Number) objects[1]).doubleValue());

						if (objects.length > 2 && objects[2] != null)
							data.setDiscount(((Number) objects[2]).doubleValue());

						data.calculate();
						list.add(data);
					}
				}
			}

			//find all sales
			for (MenuCategory category : categories) {

				criteria = session.createCriteria(TicketItem.class, "item"); //$NON-NLS-1$
				criteria.createCriteria("ticket", "t"); //$NON-NLS-1$ //$NON-NLS-2$
				criteria.createCriteria("t.owner", "u"); //$NON-NLS-1$ //$NON-NLS-2$
				projectionList = Projections.projectionList();
				projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
				projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
				projectionList.add(Projections.sum(TicketItem.PROP_DISCOUNT_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.eq("item." + TicketItem.PROP_CATEGORY_NAME, category.getName())); //$NON-NLS-1$
				criteria.add(Restrictions.ge("t." + Ticket.PROP_ACTIVE_DATE, start)); //$NON-NLS-1$
				criteria.add(Restrictions.le("t." + Ticket.PROP_ACTIVE_DATE, end)); //$NON-NLS-1$

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_TYPE, userType)); //$NON-NLS-1$
				}
				if (terminal != null) {
					criteria.add(Restrictions.eq("t." + Ticket.PROP_TERMINAL, terminal)); //$NON-NLS-1$
				}
				datas = criteria.list();
				if (datas.size() > 0) {
					Object[] objects = (Object[]) datas.get(0);

					SalesAnalysisData data = new SalesAnalysisData();
					data.setShiftName("ALL DAY"); //$NON-NLS-1$
					data.setCategoryName(category.getName());

					if (objects.length > 0 && objects[0] != null)
						data.setCount(((Number) objects[0]).intValue());

					if (objects.length > 1 && objects[1] != null)
						data.setGross(((Number) objects[1]).doubleValue());

					if (objects.length > 2 && objects[2] != null)
						data.setDiscount(((Number) objects[2]).doubleValue());

					data.calculate();
					list.add(data);
				}
			}
			return list;
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public SalesStatistics findKeyStatistics(Date start, Date end, UserType userType, Terminal terminal) {
		Session session = null;

		try {
			SalesStatistics salesSummary = new SalesStatistics();

			session = getSession();

			//retrieve restaurant information and set it to sales summary
			Restaurant restaurant = (Restaurant) get(Restaurant.class, new Integer(1), session);
			if (restaurant != null) {
				salesSummary.setCapacity(restaurant.getCapacity() != null ? restaurant.getCapacity().intValue() : 0);
				salesSummary.setTables(restaurant.getTables() != null ? restaurant.getTables().intValue() : 0);
			}

			{
				//find gross sale, discount and tax. this excludes void tickets
				//net sale = gross_sale - discount
				Criteria criteria = session.createCriteria(Ticket.class, "ticket"); //$NON-NLS-1$
				criteria.createCriteria(Ticket.PROP_OWNER, "u"); //$NON-NLS-1$

				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_SUBTOTAL_AMOUNT));
				projectionList.add(Projections.sum(Ticket.PROP_DISCOUNT_AMOUNT));
				projectionList.add(Projections.sum(Ticket.PROP_TAX_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));

				//do not take into account void tickets
				criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
				criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_TYPE, userType)); //$NON-NLS-1$
				}

				if (terminal != null) {
					criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
				}
				List list = criteria.list();
				if (list.size() > 0) {
					Object[] objects = (Object[]) list.get(0);

					if (objects.length > 1 && objects[1] != null) {
						salesSummary.setGrossSale(((Number) objects[1]).doubleValue());
					}
					if (objects.length > 2 && objects[2] != null) {
						salesSummary.setDiscount(((Number) objects[2]).intValue());
					}
					if (objects.length > 3 && objects[3] != null) {
						salesSummary.setTax(((Number) objects[3]).intValue());
					}
				}
			}

			{
				//determine number of guests within the specified time. this includes void tickets, 
				Criteria criteria = session.createCriteria(Ticket.class, "ticket"); //$NON-NLS-1$
				criteria.createCriteria(Ticket.PROP_OWNER, "u"); //$NON-NLS-1$

				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_NUMBER_OF_GUESTS));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_TYPE, userType)); //$NON-NLS-1$
				}

				if (terminal != null) {
					criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
				}
				List list = criteria.list();
				if (list.size() > 0) {
					Object[] objects = (Object[]) list.get(0);
					salesSummary.setCheckCount(((Number) objects[0]).intValue());

					if (objects.length > 1 && objects[1] != null) {
						salesSummary.setGuestCount(((Number) objects[1]).intValue());
					}
				}
			}

			{
				//find number of open tickets and its total amount
				Criteria criteria = session.createCriteria(Ticket.class, "ticket"); //$NON-NLS-1$
				criteria.createCriteria(Ticket.PROP_OWNER, "u"); //$NON-NLS-1$

				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));
				criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_TYPE, userType)); //$NON-NLS-1$
				}
				if (terminal != null) {
					criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
				}
				List list = criteria.list();
				if (list.size() > 0) {
					Object[] objects = (Object[]) list.get(0);
					salesSummary.setOpenChecks(((Number) objects[0]).intValue());

					if (objects.length > 1 && objects[1] != null) {
						salesSummary.setOpenAmount(((Number) objects[1]).doubleValue());
					}
				}
			}

			{
				//find number of void tickets and its total amount
				Criteria criteria = session.createCriteria(Ticket.class, "ticket"); //$NON-NLS-1$
				criteria.createCriteria(Ticket.PROP_OWNER, "u"); //$NON-NLS-1$
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));
				criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
				criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.TRUE));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_TYPE, userType)); //$NON-NLS-1$
				}
				if (terminal != null) {
					criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
				}
				List list = criteria.list();
				if (list.size() > 0) {
					Object[] objects = (Object[]) list.get(0);
					salesSummary.setVoidChecks(((Number) objects[0]).intValue());

					if (objects.length > 1 && objects[1] != null) {
						salesSummary.setVoidAmount(((Number) objects[1]).doubleValue());
					}
				}
			}

			{
				//find non taxable sales
				Criteria criteria = session.createCriteria(Ticket.class, "ticket"); //$NON-NLS-1$
				criteria.createCriteria(Ticket.PROP_OWNER, "u"); //$NON-NLS-1$
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));
				criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
				criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
				criteria.add(Restrictions.eq(Ticket.PROP_TAX_EXEMPT, Boolean.TRUE));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_TYPE, userType)); //$NON-NLS-1$
				}
				if (terminal != null) {
					criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
				}
				List list = criteria.list();
				if (list.size() > 0) {
					Object[] objects = (Object[]) list.get(0);
					salesSummary.setNtaxChecks(((Number) objects[0]).intValue());

					if (objects.length > 1 && objects[1] != null) {
						salesSummary.setNtaxAmount(((Number) objects[1]).doubleValue());
					}
				}
			}
			{
				//find reopen statistics
				Criteria criteria = session.createCriteria(Ticket.class, "ticket"); //$NON-NLS-1$
				criteria.createCriteria(Ticket.PROP_OWNER, "u"); //$NON-NLS-1$
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));
				criteria.add(Restrictions.eq(Ticket.PROP_RE_OPENED, Boolean.TRUE));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_TYPE, userType)); //$NON-NLS-1$
				}
				if (terminal != null) {
					criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
				}
				List list = criteria.list();
				if (list.size() > 0) {
					Object[] objects = (Object[]) list.get(0);
					salesSummary.setRopnChecks(((Number) objects[0]).intValue());

					if (objects.length > 1 && objects[1] != null) {
						salesSummary.setRopnAmount(((Number) objects[1]).doubleValue());
					}
				}
			}

			{
				//calculate totalLaborHours in the specified period
				Criteria criteria = session.createCriteria(AttendenceHistory.class, "history"); //$NON-NLS-1$
				criteria.createCriteria(AttendenceHistory.PROP_USER, "u"); //$NON-NLS-1$
				criteria.add(Restrictions.ge(AttendenceHistory.PROP_CLOCK_IN_TIME, start));
				criteria.add(Restrictions.le(AttendenceHistory.PROP_CLOCK_IN_TIME, end));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_TYPE, userType)); //$NON-NLS-1$
				}
				if (terminal != null) {
					criteria.add(Restrictions.eq(AttendenceHistory.PROP_TERMINAL, terminal));
				}
				List list = criteria.list();

				double laborHours = 0;
				double laborCost = 0;
				for (Object object : list) {
					AttendenceHistory attendenceHistory = (AttendenceHistory) object;
					double laborHourInMillisecond = 0;
					if (!attendenceHistory.isClockedOut() || attendenceHistory.getClockOutTime() == null) {
						Shift attendenceShift = attendenceHistory.getShift();
						laborHourInMillisecond = Math.abs(end.getTime() - attendenceHistory.getClockInTime().getTime());
						if (laborHourInMillisecond > attendenceShift.getShiftLength()) {
							laborHourInMillisecond = attendenceShift.getShiftLength();
						}
					}
					else {
						laborHourInMillisecond = Math.abs(attendenceHistory.getClockInTime().getTime() - attendenceHistory.getClockInTime().getTime());
					}
					double hour = (laborHourInMillisecond * (2.77777778 * Math.pow(10, -7)));
					laborHours += hour;
					laborCost += hour * (attendenceHistory.getUser().getCostPerHour() == null ? 0 : attendenceHistory.getUser().getCostPerHour());
				}
				salesSummary.setLaborHour(laborHours);
				salesSummary.setLaborCost(laborCost);
			}

			{
				//find summary by shift
				Criteria criteria = session.createCriteria(Shift.class);
				List shifts = criteria.list();
				for (Object object : shifts) {
					Shift shift = (Shift) object;

					List<OrderType> values = Application.getInstance().getOrderTypes(); //change enum
					for (OrderType ticketType : values) {
						findRecordByProfitCenter(start, end, userType, terminal, session, salesSummary, shift, ticketType);
					}

				}
			}

			salesSummary.calculateOthers();
			return salesSummary;
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	private void findRecordByProfitCenter(Date start, Date end, UserType userType, Terminal terminal, Session session, SalesStatistics salesSummary,
			Shift shift, OrderType ticketType) {
		Criteria criteria;
		criteria = session.createCriteria(Ticket.class, "ticket"); //$NON-NLS-1$
		criteria.createCriteria(Ticket.PROP_OWNER, "u"); //$NON-NLS-1$
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.rowCount());
		projectionList.add(Projections.sum(Ticket.PROP_NUMBER_OF_GUESTS));
		projectionList.add(Projections.sum(Ticket.PROP_SUBTOTAL_AMOUNT));
		criteria.setProjection(projectionList);
		criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
		criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));
		criteria.add(Restrictions.eq(Ticket.PROP_SHIFT, shift));
		criteria.add(Restrictions.eq(Ticket.PROP_TICKET_TYPE, ticketType.name()));

		if (userType != null) {
			criteria.add(Restrictions.eq("u." + User.PROP_TYPE, userType)); //$NON-NLS-1$
		}
		if (terminal != null) {
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
		}
		List list = criteria.list();
		if (list.size() > 0) {
			ShiftwiseSalesTableData data = new ShiftwiseSalesTableData();
			data.setProfitCenter(ticketType.toString());
			Object[] objects = (Object[]) list.get(0);

			data.setShiftName(shift.getName());
			data.setCheckCount(((Number) objects[0]).intValue());

			if (objects.length > 1 && objects[1] != null) {
				data.setGuestCount(((Number) objects[1]).intValue());
			}
			if (objects.length > 2 && objects[2] != null) {
				data.setTotalSales(((Number) objects[2]).doubleValue());
			}
			data.setPercentage(data.getTotalSales() * 100 / salesSummary.getGrossSale());
			data.calculateOthers();
			salesSummary.addSalesTableData(data);
		}
	}
}
