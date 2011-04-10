package com.floreantpos.model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.report.PayrollReportData;

public class AttendenceHistoryDAO extends BaseAttendenceHistoryDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public AttendenceHistoryDAO() {
	}

	public List<User> findNumberOfClockedInUserAtHour(Date fromDay, Date toDay, int hour, String userType, Terminal terminal) {
		Session session = null;

		ArrayList<User> users = new ArrayList<User>();

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(AttendenceHistory.PROP_CLOCK_IN_TIME, fromDay));
			criteria.add(Restrictions.le(AttendenceHistory.PROP_CLOCK_IN_TIME, toDay));
			criteria.add(Restrictions.le(AttendenceHistory.PROP_CLOCK_IN_HOUR, new Short((short) hour)));

			if (userType != null) {
				criteria.createAlias(AttendenceHistory.PROP_USER, "u");
				criteria.add(Restrictions.eq("u.newUserType", userType));
			}
			if (terminal != null) {
				criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			}

			List list = criteria.list();
			for (Object object : list) {
				AttendenceHistory history = (AttendenceHistory) object;

				if (!history.isClockedOut()) {
					users.add(history.getUser());
				}
				else if (history.getClockOutHour() >= hour) {
					users.add(history.getUser());
				}
			}
			return users;
		} catch (Exception e) {
			throw new PosException("Error while calculating number of clocked in user", e);
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}
	public List<User> findNumberOfClockedInUserAtShift(Date fromDay, Date toDay, Shift shift, String userType, Terminal terminal) {
		Session session = null;
		
		ArrayList<User> users = new ArrayList<User>();
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(AttendenceHistory.PROP_CLOCK_IN_TIME, fromDay));
			criteria.add(Restrictions.le(AttendenceHistory.PROP_CLOCK_IN_TIME, toDay));
			criteria.add(Restrictions.le(AttendenceHistory.PROP_SHIFT, shift));
			
			if (userType != null) {
				criteria.createAlias(AttendenceHistory.PROP_USER, "u");
				criteria.add(Restrictions.eq("u.newUserType", userType));
			}
			if (terminal != null) {
				criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			}
			
			List list = criteria.list();
			for (Object object : list) {
				AttendenceHistory history = (AttendenceHistory) object;
				
//				if (!history.isClockedOut()) {
//					users.add(history.getUser());
//				}
//				else if (history.getClockOutHour() >= hour) {
//					users.add(history.getUser());
//				}
				users.add(history.getUser());
			}
			return users;
		} catch (Exception e) {
			throw new PosException("Error while calculating number of clocked in user", e);
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public AttendenceHistory findHistoryByClockedInTime(User user) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(AttendenceHistory.class);
			criteria.add(Restrictions.eq(AttendenceHistory.PROP_CLOCK_IN_TIME, user.getLastClockInTime()));
			criteria.add(Restrictions.eq(AttendenceHistory.PROP_USER, user));

			return (AttendenceHistory) criteria.uniqueResult();
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}
	
	public List<PayrollReportData> findPayroll(Date from, Date to) {
		Session session = null;
		
		ArrayList<PayrollReportData> list = new ArrayList<PayrollReportData>();
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(AttendenceHistory.class);
			criteria.add(Restrictions.ge(AttendenceHistory.PROP_CLOCK_IN_TIME, from));
			criteria.add(Restrictions.le(AttendenceHistory.PROP_CLOCK_OUT_TIME, to));
			criteria.addOrder(Order.asc(AttendenceHistory.PROP_USER));
			List list2 = criteria.list();
			
			for (Iterator iterator = list2.iterator(); iterator.hasNext();) {
				AttendenceHistory history = (AttendenceHistory) iterator.next();
				PayrollReportData data = new PayrollReportData();
				data.setFrom(history.getClockInTime());
				data.setTo(history.getClockOutTime());
				data.setDate(history.getClockInTime());
				data.setUser(history.getUser());
				data.calculate();
				
				list.add(data);
			}
			
			return list;
		} catch (Exception e) {
			throw new PosException("Unnable to payroll", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}