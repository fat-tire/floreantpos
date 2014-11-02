package com.floreantpos.model.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.TransactionType;
import com.floreantpos.model.User;
import com.floreantpos.model.util.TransactionSummary;


public class PosTransactionDAO extends BasePosTransactionDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public PosTransactionDAO () {}

	public List<PosTransaction> findUnauthorizedTransactions() {
		return findUnauthorizedTransactions(null);
	}
	
	public List<PosTransaction> findUnauthorizedTransactions(User owner) {
		Session session = null;
		
		try {
			session = getSession();
			
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(PosTransaction.PROP_CAPTURED, Boolean.FALSE));
			criteria.add(Restrictions.eq(PosTransaction.PROP_AUTHORIZABLE, Boolean.TRUE));
			criteria.add(Restrictions.eq(PosTransaction.PROP_TRANSACTION_TYPE, TransactionType.CREDIT.name()));
			criteria.add(Restrictions.isNotNull(PosTransaction.PROP_TICKET));
			
			if(owner != null) {
				criteria.add(Restrictions.eq(PosTransaction.PROP_USER, owner));
			}
			
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public List<? extends PosTransaction> findTransactions(Terminal terminal, Class transactionClass, Date from, Date to ) {
		Session session = null;

		try {
			session = getSession();
			
			Criteria criteria = session.createCriteria(transactionClass);
			criteria.add(Restrictions.isNotNull(PosTransaction.PROP_TICKET));
			if(terminal != null) {
				criteria.add(Restrictions.eq(PosTransaction.PROP_TERMINAL, terminal));
			}
			
			if(from != null && to != null) {
				//criteria.add(Restrictions.ge(PosTransaction.PROP_TRANSACTION_TIME, from));
				//criteria.add(Restrictions.le(PosTransaction.PROP_TRANSACTION_TIME, to));
			}

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public TransactionSummary getTransactionSummary(Terminal terminal, Class transactionClass, Date from, Date to) {
		Session session = null;
		TransactionSummary summary = new TransactionSummary();
		try {
			session = getSession();

			Criteria criteria = session.createCriteria(transactionClass);
			criteria.add(Restrictions.eq(PosTransaction.PROP_DRAWER_RESETTED, Boolean.FALSE));
			
			if(terminal != null) {
				criteria.add(Restrictions.eq(PosTransaction.PROP_TERMINAL, terminal));
			}
			
			if(from != null && to != null) {
				criteria.add(Restrictions.ge(PosTransaction.PROP_TRANSACTION_TIME, from));
				criteria.add(Restrictions.le(PosTransaction.PROP_TRANSACTION_TIME, to));
			}

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.count(PosTransaction.PROP_ID));
			projectionList.add(Projections.sum(PosTransaction.PROP_AMOUNT));
			projectionList.add(Projections.sum(PosTransaction.PROP_TIPS_AMOUNT));

			criteria.setProjection(projectionList);

			List list = criteria.list();

			if (list == null || list.size() == 0)
				return summary;

			Object[] o = (Object[]) list.get(0);
			int index = 0;
			
			summary.setCount(HibernateProjectionsUtil.getInt(o, index++));
			summary.setAmount(HibernateProjectionsUtil.getDouble(o, index++));
			summary.setTipsAmount(HibernateProjectionsUtil.getDouble(o, index++));
			
			return summary;
		} finally {
			closeSession(session);
		}
	}
}