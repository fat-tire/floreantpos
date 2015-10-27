package com.floreantpos.model.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.TableBookingInfo;

public class TableBookingInfoDAO extends BaseTableBookingInfoDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public TableBookingInfoDAO() {
	}

	public List<TableBookingInfo> getIdByDate(Date startDate, Date endDate) {

		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			
			long durationInMillis = (endDate.getTime() - startDate.getTime()) / 2;
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(startDate.getTime() - durationInMillis);
			startDate = calendar.getTime();
			
			calendar.setTimeInMillis(endDate.getTime() + durationInMillis);
			endDate = calendar.getTime();
			
			
			System.out.println("time: " + startDate+":"+endDate);
			
	
		
			criteria.add(Restrictions.ge(TableBookingInfo.PROP_FROM_DATE, startDate));
			criteria.add(Restrictions.le(TableBookingInfo.PROP_TO_DATE, endDate));
			
			List list = criteria.list();
			return list;
		} catch (Exception e) {

		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
		return null;
	}
}