package com.floreantpos.model.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.TableBookingInfo;
import com.floreantpos.model.dao.BaseTableBookingInfoDAO;

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
		/*	Criterion startInRange = Restrictions.between(TableBookingInfo.PROP_FROM_DATE, startDate, endDate);

			Criterion endInRange = Restrictions.between(TableBookingInfo.PROP_TO_DATE, startDate, endDate);

			Criterion thirdCondition = 
			    Restrictions.conjunction().add(Restrictions.le(TableBookingInfo.PROP_FROM_DATE, startDate))
			                              .add(Restrictions.ge(TableBookingInfo.PROP_TO_DATE, endDate));

			Criterion completeCondition = 
			    Restrictions.disjunction().add(startInRange)
			                              .add(endInRange)
			                              .add(thirdCondition);

			criteria.add(completeCondition);*/
			
			
			
			
			Criterion rest1= Restrictions.and(Restrictions.ge(TableBookingInfo.PROP_FROM_DATE, startDate), 
					Restrictions.le(TableBookingInfo.PROP_FROM_DATE,endDate));
			System.out.println("rest 1"+rest1.toString());
			
			Criterion rest2= Restrictions.and(Restrictions.ge(TableBookingInfo.PROP_TO_DATE,endDate), 
					Restrictions.le(TableBookingInfo.PROP_FROM_DATE, startDate));
			System.out.println("rest 2"+rest2.toString());
			criteria.add(Restrictions.or(rest1, rest2));
			
		
			return criteria.list();
		} catch (Exception e) {

		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
		return null;
	}
}