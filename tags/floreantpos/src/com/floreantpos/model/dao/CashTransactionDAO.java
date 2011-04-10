package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.util.TransactionSummary;



public class CashTransactionDAO extends BaseCashTransactionDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public CashTransactionDAO () {}
	
	public TransactionSummary getTransactionSummary(Terminal terminal) {
		Session session = null;
		TransactionSummary summary = new TransactionSummary();
		try {
//			String hql = "select count(*), sum(t.amount), sum(t.taxAmount) from CashTransaction t where t.transactionDate>=:startDate and t.transactionDate<:endDate";
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(PosTransaction.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(PosTransaction.PROP_TERMINAL, terminal));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.count(CashTransaction.PROP_ID));
			projectionList.add(Projections.sum(CashTransaction.PROP_SUBTOTAL_AMOUNT));
			projectionList.add(Projections.sum(CashTransaction.PROP_TAX_AMOUNT));
			projectionList.add(Projections.sum(CashTransaction.PROP_DISCOUNT_AMOUNT));
			projectionList.add(Projections.sum(CashTransaction.PROP_TOTAL_AMOUNT));
			projectionList.add(Projections.sum(CashTransaction.PROP_GRATUITY_AMOUNT));
			criteria.setProjection(projectionList);
			
			List list = criteria.list();
			if (list.size() > 0) {
				Object[] o = (Object[]) list.get(0);
				summary.setTotalNumber(((Integer) o[0]).intValue());
				summary.setSubtotalAmount(o[1] == null ? 0 : ((Double) o[1]).doubleValue());
				summary.setTotalTax(o[2] == null ? 0 : ((Double) o[2]).doubleValue());
				summary.setTotalDiscount(o[3] == null ? 0 : ((Double) o[3]).doubleValue());
				summary.setTotalAmount(o[4] == null ? 0 : ((Double) o[4]).doubleValue());
				summary.setGratuityAmount(o[5] == null ? 0 : ((Double) o[5]).doubleValue());
			}
			return summary;
		} finally {
			closeSession(session);
		}
	}

}