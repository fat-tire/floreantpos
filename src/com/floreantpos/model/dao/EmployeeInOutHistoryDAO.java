package com.floreantpos.model.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.EmployeeInOutHistory;
import com.floreantpos.model.User;

public class EmployeeInOutHistoryDAO extends BaseEmployeeInOutHistoryDAO {

	public EmployeeInOutHistory findDriverHistoryByClockedInTime(User user) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(EmployeeInOutHistory.class);
			criteria.add(Restrictions.eq(EmployeeInOutHistory.PROP_OUT_TIME, user.getLastClockOutTime()));
			criteria.add(Restrictions.eq(EmployeeInOutHistory.PROP_USER, user));

			return (EmployeeInOutHistory) criteria.uniqueResult();
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public List<EmployeeInOutHistory> findAttendanceHistory(Date from, Date to, User user) {

		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(EmployeeInOutHistory.class);
			criteria.add(Restrictions.le(EmployeeInOutHistory.PROP_OUT_TIME, from));
			criteria.add(Restrictions.ge(EmployeeInOutHistory.PROP_IN_TIME, to));
			criteria.addOrder(Order.asc(EmployeeInOutHistory.PROP_USER));

			if (user != null) {
				criteria.add(Restrictions.eq(AttendenceHistory.PROP_USER, user));
			}
			return criteria.list();
		} catch (Exception e) {
		} finally {
			session.close();
		}
		return null;
	}
}