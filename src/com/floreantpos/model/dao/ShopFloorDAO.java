package com.floreantpos.model.dao;

import org.hibernate.criterion.Projections;


public class ShopFloorDAO extends BaseShopFloorDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ShopFloorDAO () {}

	public boolean hasFloor() {
		Number result = (Number) getSession().createCriteria(getReferenceClass()).setProjection(Projections.rowCount()).uniqueResult();
		return result.intValue() != 0;
	}
}