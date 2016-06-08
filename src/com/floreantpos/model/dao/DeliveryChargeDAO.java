package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.DeliveryCharge;

public class DeliveryChargeDAO extends BaseDeliveryChargeDAO {

	public List<DeliveryCharge> findByDistance(double distance) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.and(Restrictions.le(DeliveryCharge.PROP_START_RANGE, distance), Restrictions.ge(DeliveryCharge.PROP_END_RANGE, distance)));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<DeliveryCharge> findByZipCode(String zipCode) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(DeliveryCharge.PROP_ZIP_CODE, zipCode));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public double findMinRange() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.setProjection(Projections.min(DeliveryCharge.PROP_START_RANGE));

			return (Double) criteria.uniqueResult();
		} finally {
			closeSession(session);
		}
	}

	public Double findMaxRange() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.setProjection(Projections.max(DeliveryCharge.PROP_END_RANGE));

			return (Double) criteria.uniqueResult();
		} finally {
			closeSession(session);
		}
	}
}