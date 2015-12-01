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