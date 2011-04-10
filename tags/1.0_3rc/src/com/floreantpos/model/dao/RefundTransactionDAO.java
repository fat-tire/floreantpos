package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.RefundTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.util.RefundSummary;


public class RefundTransactionDAO extends BaseRefundTransactionDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public RefundTransactionDAO () {}

	public RefundSummary getTotalRefundForTerminal(Terminal terminal) {
		Session session = null;
		RefundSummary refundSummary = new RefundSummary();
		
		try {
			session = getSession();
			
			Criteria criteria = session.createCriteria(getReferenceClass());
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum(RefundTransaction.PROP_TAX_AMOUNT));
			projectionList.add(Projections.sum(RefundTransaction.PROP_TOTAL_AMOUNT));
			criteria.setProjection(projectionList);
			
			criteria.add(Restrictions.eq(RefundTransaction.PROP_TERMINAL, terminal));
			criteria.add(Restrictions.eq(RefundTransaction.PROP_DRAWER_RESETTED, Boolean.FALSE));
			
			List list = criteria.list();
			if (list.size() > 0) {
				Object[] objects = (Object[]) list.get(0);
				
				if (objects.length > 1 && objects[1] != null) {
					refundSummary.setTax(((Number) objects[1]).doubleValue());
				}
				if (objects.length > 2 && objects[2] != null) {
					refundSummary.setAmount(((Number) objects[2]).doubleValue());
				}
			}
			return refundSummary;
		} finally {
			closeSession(session);
		}
		
	}
}