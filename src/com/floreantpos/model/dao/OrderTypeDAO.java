package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

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

}