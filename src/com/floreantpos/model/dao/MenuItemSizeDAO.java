package com.floreantpos.model.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.Multiplier;

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
}