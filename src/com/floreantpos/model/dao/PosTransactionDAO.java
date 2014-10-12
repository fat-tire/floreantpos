package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.util.TransactionSummary;


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
	
	public TransactionSummary getTransactionSummary(Terminal terminal, Class transactionClass) {
		Session session = null;
		TransactionSummary summary = new TransactionSummary();
		try {
			session = getSession();

			Criteria criteria = session.createCriteria(transactionClass);
			criteria.add(Restrictions.eq(PosTransaction.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(PosTransaction.PROP_TERMINAL, terminal));

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.count(PosTransaction.PROP_ID));
			projectionList.add(Projections.sum(PosTransaction.PROP_AMOUNT));

			criteria.setProjection(projectionList);

			List list = criteria.list();

			if (list == null || list.size() == 0)
				return summary;

			Object[] o = (Object[]) list.get(0);
			int index = 0;
			
			summary.setTotalNumber(HibernateProjectionsUtil.getInt(o, index++));
			summary.setTotalAmount(HibernateProjectionsUtil.getDouble(o, index++));
			
			return summary;
		} finally {
			closeSession(session);
		}
	}
}