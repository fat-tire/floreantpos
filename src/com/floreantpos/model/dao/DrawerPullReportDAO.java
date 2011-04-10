package com.floreantpos.model.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.DrawerPullReport;



public class DrawerPullReportDAO extends BaseDrawerPullReportDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public DrawerPullReportDAO () {}
	
	public List<DrawerPullReport> findReports(Date start, Date end) {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(DrawerPullReport.PROP_REPORT_TIME, start));
			criteria.add(Restrictions.le(DrawerPullReport.PROP_REPORT_TIME, end));
			
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
}