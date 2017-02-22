package com.floreantpos.model.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.MenuItemSize;

public class MenuItemSizeDAO extends BaseMenuItemSizeDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public MenuItemSizeDAO() {
	}

	@Override
	public Order getDefaultOrder() {
		return Order.asc(MenuItemSize.PROP_SORT_ORDER);
	}

	public void setDefault(List<MenuItemSize> items) {
		Session session = null;
		Transaction tx = null;
		try {
			session = createNewSession();
			tx = session.beginTransaction();
			saveOrUpdateSizeList(items, session);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	public void saveOrUpdateSizeList(List<MenuItemSize> items, Session session) {
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			MenuItemSize menuItemSize = (MenuItemSize) iterator.next();
			session.saveOrUpdate(menuItemSize);
		}
	}

	public MenuItemSize findByName(String sizeName) {
		Session session = null;
		Criteria criteria = null;
		try {
			session = getSession();
			criteria = session.createCriteria(MenuItemSize.class);

			criteria.add(Restrictions.eq(MenuItemSize.PROP_NAME, sizeName));

			return (MenuItemSize) criteria.list().get(0);

		} catch (Exception e) {
		}
		return null;
	}
}