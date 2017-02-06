package com.floreantpos.model.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import com.floreantpos.PosLog;
import com.floreantpos.model.Multiplier;

public class MultiplierDAO extends BaseMultiplierDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public MultiplierDAO() {
	}

	@Override
	public List<Multiplier> findAll() {
		try {
			Session session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.addOrder(Order.asc(Multiplier.PROP_SORT_ORDER));
			return criteria.list();
		} catch (Exception e) {
			PosLog.error(getClass(), e);
		}
		return null;
	}

	public void saveOrUpdateMultipliers(List<Multiplier> items) {
		Session session = null;
		Transaction tx = null;
		try {
			session = createNewSession();
			tx = session.beginTransaction();
			for (Iterator iterator = items.iterator(); iterator.hasNext();) {
				Multiplier multiplier = (Multiplier) iterator.next();
				session.saveOrUpdate(multiplier);
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			PosLog.error(getClass(), e);
		} finally {
			session.close();
		}
	}

}