package com.floreantpos.model.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.PizzaCrust;
import com.floreantpos.model.PizzaPrice;

public class PizzaPriceDAO extends BasePizzaPriceDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public PizzaPriceDAO() {
	}

	public PizzaPrice findBySizeAndCrust(MenuItemSize menuItemSize, PizzaCrust pizzaCrust) {

		Session session = null;
		Criteria criteria = null;
		try {
			session = getSession();
			criteria = session.createCriteria(PizzaPrice.class);

			criteria.add(Restrictions.eq(PizzaPrice.PROP_SIZE, menuItemSize));
			if (pizzaCrust != null) {
				criteria.add(Restrictions.eq(PizzaPrice.PROP_CRUST, pizzaCrust));
			}
			return (PizzaPrice) criteria.list().get(0);

		} catch (Exception e) {
		}
		return null;
	}

}