package com.floreantpos.model.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.ShopTable;


public class ShopTableDAO extends BaseShopTableDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ShopTableDAO () {}

	public ShopTable getByNumber(String tableNumber) {
		Session session = createNewSession();
		Criteria criteria = session.createCriteria(getReferenceClass());
		criteria.add(Restrictions.eq(ShopTable.PROP_TABLE_NUMBER, tableNumber));
		
		return (ShopTable) criteria.uniqueResult();
	}
}