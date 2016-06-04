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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.UserType;
import com.floreantpos.report.AttendanceReportData;
import com.floreantpos.report.PayrollReportData;

public class AttendenceHistoryDAO extends BaseAttendenceHistoryDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public AttendenceHistoryDAO() {
	}

	public List<User> findNumberOfClockedInUserAtHour(Date fromDay, Date toDay, int hour, UserType userType, Terminal terminal) {
		Session session = null;

		ArrayList<User> users = new ArrayList<User>();

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(AttendenceHistory.PROP_CLOCK_IN_TIME, fromDay));
			criteria.add(Restrictions.le(AttendenceHistory.PROP_CLOCK_IN_TIME, toDay));
			criteria.add(Restrictions.le(AttendenceHistory.PROP_CLOCK_IN_HOUR, new Short((short) hour)));

			if (userType != null) {
				criteria.createAlias(AttendenceHistory.PROP_USER, "u"); //$NON-NLS-1$
				criteria.add(Restrictions.eq("u.type", userType)); //$NON-NLS-1$
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
			throw new PosException(Messages.getString("AttendenceHistoryDAO.2"), e); //$NON-NLS-1$
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public List<User> findNumberOfClockedInUserAtShift(Date fromDay, Date toDay, Shift shift, UserType userType, Terminal terminal) {
		Session session = null;

		ArrayList<User> users = new ArrayList<User>();

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(AttendenceHistory.PROP_CLOCK_IN_TIME, fromDay));
			criteria.add(Restrictions.le(AttendenceHistory.PROP_CLOCK_IN_TIME, toDay));
			criteria.add(Restrictions.le(AttendenceHistory.PROP_SHIFT, shift));

			if (userType != null) {
				criteria.createAlias(AttendenceHistory.PROP_USER, "u"); //$NON-NLS-1$
				criteria.add(Restrictions.eq("u.type", userType)); //$NON-NLS-1$
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
			throw new PosException(Messages.getString("AttendenceHistoryDAO.5"), e); //$NON-NLS-1$
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
			throw new PosException(Messages.getString("AttendenceHistoryDAO.6"), e); //$NON-NLS-1$
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public List<AttendanceReportData> findAttendance(Date from, Date to, User user) {
		Session session = null;

		ArrayList<AttendanceReportData> list = new ArrayList<AttendanceReportData>();

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(AttendenceHistory.class);
			criteria.add(Restrictions.ge(AttendenceHistory.PROP_CLOCK_IN_TIME, from));
			criteria.add(Restrictions.le(AttendenceHistory.PROP_CLOCK_OUT_TIME, to));
			criteria.addOrder(Order.asc(AttendenceHistory.PROP_USER));

			if (user != null) {
				criteria.add(Restrictions.eq(AttendenceHistory.PROP_USER, user));
			}

			List list2 = criteria.list();

			for (Iterator iterator = list2.iterator(); iterator.hasNext();) {
				AttendenceHistory history = (AttendenceHistory) iterator.next();
				AttendanceReportData data = new AttendanceReportData();
				data.setClockIn(history.getClockInTime());
				data.setClockOut(history.getClockOutTime());
				data.setUser(history.getUser());
				data.setName(history.getUser().getFirstName());
				data.calculate();

				list.add(data);
			}

			return list;
		} catch (Exception e) {
			throw new PosException("Unable to find Attendance", e); //$NON-NLS-1$
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public List<AttendenceHistory> findHistory(Date from, Date to, User user) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(AttendenceHistory.class);
			criteria.add(Restrictions.ge(AttendenceHistory.PROP_CLOCK_IN_TIME, from));
			criteria.add(Restrictions.le(AttendenceHistory.PROP_CLOCK_OUT_TIME, to));
			criteria.addOrder(Order.asc(AttendenceHistory.PROP_ID));
			if (user != null) {
				criteria.add(Restrictions.eq(AttendenceHistory.PROP_USER, user));
			}

			List list2 = criteria.list();

			return list2;
		} catch (Exception e) {
			throw new PosException("Unable to find History", e); //$NON-NLS-1$
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}