package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.DeliveryCharge;

public class DeliveryChargeDAO extends BaseDeliveryChargeDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public DeliveryChargeDAO() {
	}

	public double findMinCharge() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.setProjection(Projections.min(DeliveryCharge.PROP_CHARGE_AMOUNT));

			return (Double) criteria.uniqueResult();
		} finally {
			closeSession(session);
		}
	}

	public double findMaxCharge() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.setProjection(Projections.max(DeliveryCharge.PROP_CHARGE_AMOUNT));

			return (Double) criteria.uniqueResult();
		} finally {
			closeSession(session);
		}
	}
}