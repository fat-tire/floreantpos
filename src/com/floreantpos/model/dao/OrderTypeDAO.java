package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosLog;
import com.floreantpos.model.OrderType;

public class OrderTypeDAO extends BaseOrderTypeDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public OrderTypeDAO() {
	}

	public List<OrderType> findEnabledOrderTypes() {
		Session session = null;
		try {
			session = createNewSession();

			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(OrderType.PROP_ENABLED, true));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<OrderType> findLoginScreenViewOrderTypes() {
		Session session = null;
		try {
			session = createNewSession();

			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(OrderType.PROP_ENABLED, true));
			criteria.add(Restrictions.eq(OrderType.PROP_SHOW_IN_LOGIN_SCREEN, true));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public OrderType findByName(String orderType) {
		Session session = null;
		try {
			session = createNewSession();

			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(OrderType.PROP_NAME, orderType));

			return (OrderType) criteria.uniqueResult();
		} finally {
			closeSession(session);
		}
	}

	/*
	 * 
	 * Checking menu item order types contain order type object or name.
	 * 
	 */
	public boolean containsOrderTypeObj() {
		Session session = null;
		try {
			session = createNewSession();
			Query query = session.createSQLQuery("select count(s.MENU_ITEM_ID), count(s.ORDER_TYPE_ID) from ITEM_ORDER_TYPE s");
			List result = query.list();
			Object[] object = (Object[]) result.get(0);
			Integer menuItemCount = getInt(object, 0);
			Integer orderTypeCount = getInt(object, 1);

			if (menuItemCount < 1) {
				return true;
			}
			return orderTypeCount > 0;
		} catch (Exception e) {
			PosLog.error(getClass(), e);
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
		return false;
	}

	/*
	 * This method is used for updating menu item order type name to order type object. 
	 * 
	 */
	public void updateMenuItemOrderType() {
		Session session = null;
		Transaction tx = null;
		try {
			session = createNewSession();
			tx = session.beginTransaction();
			Query query = session.createSQLQuery("Update ITEM_ORDER_TYPE t SET t.ORDER_TYPE_ID=(Select o.id from ORDER_TYPE o where o.NAME=t.ORDER_TYPE)");
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	private Integer getInt(Object[] array, int index) {
		if (array.length < (index + 1))
			return null;
		
		if (array[index] instanceof Number) {
			return ((Number) array[index]).intValue();
		}

		return null;
	}
}