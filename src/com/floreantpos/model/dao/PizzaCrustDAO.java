package com.floreantpos.model.dao;

import org.hibernate.criterion.Order;

import com.floreantpos.model.PizzaCrust;
import com.floreantpos.model.dao.BasePizzaCrustDAO;


public class PizzaCrustDAO extends BasePizzaCrustDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public PizzaCrustDAO () {}

	@Override
	public Order getDefaultOrder() {
		return Order.asc(PizzaCrust.PROP_SORT_ORDER);
	}
}