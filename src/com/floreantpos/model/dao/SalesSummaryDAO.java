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

import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.User;
import com.floreantpos.model.UserType;
import com.floreantpos.report.SalesStatistics;
import com.floreantpos.report.SalesAnalysisReportModel.SalesAnalysisData;
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
			miscCategory.setName("MISC");
			categories.add(miscCategory);
			
			//find food sales
			criteria = session.createCriteria(TicketItem.class, "item");
			criteria.createCriteria("ticket", "t");
			criteria.createCriteria("t.owner", "u");
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
			projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
			projectionList.add(Projections.sum(TicketItem.PROP_DISCOUNT_AMOUNT));
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("item." + TicketItem.PROP_BEVERAGE, Boolean.FALSE));
			criteria.add(Restrictions.ge("t." + Ticket.PROP_ACTIVE_DATE, start));
			criteria.add(Restrictions.le("t." + Ticket.PROP_ACTIVE_DATE, end));

			if (userType != null) {
				criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
			}
			if (terminal != null) {
				criteria.add(Restrictions.eq("item." + TicketItem.PROP_TICKET + "." + Ticket.PROP_TERMINAL, terminal));
			}
			List datas = criteria.list();
			if (datas.size() > 0) {
				Object[] objects = (Object[]) datas.get(0);

				SalesAnalysisData data = new SalesAnalysisData();
				data.setShiftName("");
				data.setCategoryName("FOOD SALES");

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
			criteria = session.createCriteria(TicketItem.class, "item");
			criteria.createCriteria("ticket", "t");
			criteria.createCriteria("t.owner", "u");
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
			projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
			projectionList.add(Projections.sum(TicketItem.PROP_DISCOUNT_AMOUNT));
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("item." + TicketItem.PROP_BEVERAGE, Boolean.TRUE));
			criteria.add(Restrictions.ge("t." + Ticket.PROP_ACTIVE_DATE, start));
			criteria.add(Restrictions.le("t." + Ticket.PROP_ACTIVE_DATE, end));
			
			if (userType != null) {
				criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
			}
			if (terminal != null) {
				criteria.add(Restrictions.eq("item." + TicketItem.PROP_TICKET + "." + Ticket.PROP_TERMINAL, terminal));
			}
			datas = criteria.list();
			if (datas.size() > 0) {
				Object[] objects = (Object[]) datas.get(0);
				
				SalesAnalysisData data = new SalesAnalysisData();
				data.setShiftName("");
				data.setCategoryName("NON FOOD SALES");
				
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

					criteria = session.createCriteria(TicketItem.class, "item");
					criteria.createCriteria("ticket", "t");
					criteria.createCriteria("t.owner", "u");
					projectionList = Projections.projectionList();
					projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
					projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
					projectionList.add(Projections.sum(TicketItem.PROP_DISCOUNT_AMOUNT));
					criteria.setProjection(projectionList);
					criteria.add(Restrictions.eq("item." + TicketItem.PROP_CATEGORY_NAME, category.getName()));
					criteria.add(Restrictions.eq("t." + Ticket.PROP_SHIFT, shift));
					criteria.add(Restrictions.ge("t." + Ticket.PROP_ACTIVE_DATE, start));
					criteria.add(Restrictions.le("t." + Ticket.PROP_ACTIVE_DATE, end));

					if (userType != null) {
						criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
					}
					if (terminal != null) {
						criteria.add(Restrictions.eq("item." + TicketItem.PROP_TICKET + "." + Ticket.PROP_TERMINAL, terminal));
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

				criteria = session.createCriteria(TicketItem.class, "item");
				criteria.createCriteria("ticket", "t");
				criteria.createCriteria("t.owner", "u");
				projectionList = Projections.projectionList();
				projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
				projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
				projectionList.add(Projections.sum(TicketItem.PROP_DISCOUNT_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.eq("item." + TicketItem.PROP_CATEGORY_NAME, category.getName()));
				criteria.add(Restrictions.ge("t." + Ticket.PROP_ACTIVE_DATE, start));
				criteria.add(Restrictions.le("t." + Ticket.PROP_ACTIVE_DATE, end));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
				}
				if (terminal != null) {
					criteria.add(Restrictions.eq("item." + TicketItem.PROP_TICKET + "." + Ticket.PROP_TERMINAL, terminal));
				}
				datas = criteria.list();
				if (datas.size() > 0) {
					Object[] objects = (Object[]) datas.get(0);

					SalesAnalysisData data = new SalesAnalysisData();
					data.setShiftName("ALL DAY");
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
				Criteria criteria = session.createCriteria(Ticket.class, "ticket");
				criteria.createCriteria(Ticket.PROP_OWNER, "u");

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

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
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
				Criteria criteria = session.createCriteria(Ticket.class, "ticket");
				criteria.createCriteria(Ticket.PROP_OWNER, "u");

				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_NUMBER_OF_GUESTS));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
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
				Criteria criteria = session.createCriteria(Ticket.class, "ticket");
				criteria.createCriteria(Ticket.PROP_OWNER, "u");

				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));
				criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
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
				Criteria criteria = session.createCriteria(Ticket.class, "ticket");
				criteria.createCriteria(Ticket.PROP_OWNER, "u");
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));
				criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
				criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.TRUE));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
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
				Criteria criteria = session.createCriteria(Ticket.class, "ticket");
				criteria.createCriteria(Ticket.PROP_OWNER, "u");
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));
				criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
				criteria.add(Restrictions.eq(Ticket.PROP_TAX_EXEMPT, Boolean.TRUE));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
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
				Criteria criteria = session.createCriteria(Ticket.class, "ticket");
				criteria.createCriteria(Ticket.PROP_OWNER, "u");
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));
				criteria.add(Restrictions.eq(Ticket.PROP_RE_OPENED, Boolean.TRUE));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
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
				Criteria criteria = session.createCriteria(AttendenceHistory.class, "history");
				criteria.createCriteria(AttendenceHistory.PROP_USER, "u");
				criteria.add(Restrictions.ge(AttendenceHistory.PROP_CLOCK_IN_TIME, start));
				criteria.add(Restrictions.le(AttendenceHistory.PROP_CLOCK_IN_TIME, end));

				if (userType != null) {
					criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
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

					{
						//DINE IN PART
						criteria = session.createCriteria(Ticket.class, "ticket");
						criteria.createCriteria(Ticket.PROP_OWNER, "u");
						ProjectionList projectionList = Projections.projectionList();
						projectionList.add(Projections.rowCount());
						projectionList.add(Projections.sum(Ticket.PROP_NUMBER_OF_GUESTS));
						projectionList.add(Projections.sum(Ticket.PROP_SUBTOTAL_AMOUNT));
						criteria.setProjection(projectionList);
						criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
						criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));
						criteria.add(Restrictions.eq(Ticket.PROP_SHIFT, shift));

						if (userType != null) {
							criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
						}
						if (terminal != null) {
							criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
						}
						List list = criteria.list();
						if (list.size() > 0) {
							ShiftwiseSalesTableData data = new ShiftwiseSalesTableData();
							data.setProfitCenter("DINE IN");
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
					
					{
						//TAKE OUT PART
						criteria = session.createCriteria(Ticket.class, "ticket");
						criteria.createCriteria(Ticket.PROP_OWNER, "u");
						ProjectionList projectionList = Projections.projectionList();
						projectionList.add(Projections.rowCount());
						projectionList.add(Projections.sum(Ticket.PROP_NUMBER_OF_GUESTS));
						projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
						criteria.setProjection(projectionList);
						criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, start));
						criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, end));
						criteria.add(Restrictions.eq(Ticket.PROP_SHIFT, shift));
						criteria.add(Restrictions.eq(Ticket.PROP_TABLE_NUMBER, Ticket.TAKE_OUT));

						if (userType != null) {
							criteria.add(Restrictions.eq("u." + User.PROP_NEW_USER_TYPE, userType));
						}
						if (terminal != null) {
							criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
						}
						List list = criteria.list();
						if (list.size() > 0) {
							ShiftwiseSalesTableData data = new ShiftwiseSalesTableData();
							data.setProfitCenter("TAKE OUT");
							
							Object[] objects = (Object[]) list.get(0);

							data.setShiftName(shift.getName());
							data.setCheckCount(((Number) objects[0]).intValue());

							if (objects.length > 1 && objects[1] != null) {
								data.setGuestCount(((Number) objects[1]).intValue());
							}
							if (objects.length > 2 && objects[2] != null) {
								data.setTotalSales(((Number) objects[2]).doubleValue());
							}

							data.calculateOthers();
							salesSummary.addSalesTableData(data);
						}
					
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
}
