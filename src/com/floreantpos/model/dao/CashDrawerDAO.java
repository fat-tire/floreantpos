package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.CashDrawer;
import com.floreantpos.model.Terminal;

public class CashDrawerDAO extends BaseCashDrawerDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public CashDrawerDAO() {
	}

	public List<CashDrawer> findByTerminal(Terminal terminal) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(CashDrawer.PROP_TERMINAL, terminal));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
}