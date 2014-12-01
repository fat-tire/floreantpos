package com.floreantpos.model.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.PackagingUnit;


public class PackagingUnitDAO extends BasePackagingUnitDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public PackagingUnitDAO () {}
	
	public boolean nameExists(String name) {
		Session session = null;
		
		try {
			
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(PackagingUnit.PROP_NAME, name).ignoreCase());
			
			criteria.setProjection(Projections.rowCount());
		    Integer rowCount = (Integer) criteria.uniqueResult();
		    
		    if(rowCount == null) return false;
		    
		    return (rowCount > 0) ? true : false;
		    
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}


}