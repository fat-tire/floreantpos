package com.floreantpos.model.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.CustomPayment;

public class CustomPaymentDAO extends BaseCustomPaymentDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public CustomPaymentDAO() {
	}

	@Override
	public Order getDefaultOrder() {
		return Order.asc(CustomPayment.PROP_ID);
	}

	public CustomPayment getByName(String name) {
		Session session = null;
		try {
			session = createNewSession();

			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.like(CustomPayment.PROP_NAME, name));

			return (CustomPayment) criteria.uniqueResult();
		} finally {
			closeSession(session);
		}
	}

}