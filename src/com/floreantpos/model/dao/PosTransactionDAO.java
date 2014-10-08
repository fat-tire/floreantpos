package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;


public class PosTransactionDAO extends BasePosTransactionDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public PosTransactionDAO () {}

	public List<PosTransaction> findUnauthorizedTransactions(Terminal terminal) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(PosTransaction.PROP_CAPTURED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
}