package com.floreantpos.model.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.PizzaCrust;

public class PizzaCrustDAO extends BasePizzaCrustDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public PizzaCrustDAO() {
	}

	@Override
	public Order getDefaultOrder() {
		return Order.asc(PizzaCrust.PROP_SORT_ORDER);
	}

	public void setDefault(List<PizzaCrust> items) {
		Session session = null;
		Transaction tx = null;
		try {
			session = createNewSession();
			tx = session.beginTransaction();
			saveOrUpdateCrustList(items, session);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	public void saveOrUpdateCrustList(List<PizzaCrust> items, Session session) {
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			PizzaCrust pizzaCrust = (PizzaCrust) iterator.next();
			session.saveOrUpdate(pizzaCrust);
		}
	}
}