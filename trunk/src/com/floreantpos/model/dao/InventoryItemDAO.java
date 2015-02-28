package com.floreantpos.model.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.MenuItem;


public class InventoryItemDAO extends BaseInventoryItemDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public InventoryItemDAO () {}
	
	public boolean hasInventoryItemByName(String name) {
		Session session = null;

		try {
			
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_NAME, name));

			return criteria.list().size() > 0;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding food items");
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

}