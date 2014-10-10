package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.PosTransaction;


public class PosTransactionDAO extends BasePosTransactionDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public PosTransactionDAO () {}

	public List<PosTransaction> findUnauthorizedTransactions() {
		Session session = null;

		try {
			session = getSession();
			
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(PosTransaction.PROP_CAPTURED, Boolean.FALSE));
			criteria.add(Restrictions.isNotNull(PosTransaction.PROP_TICKET));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
}