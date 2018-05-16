/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.model.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.TransactionType;
import com.floreantpos.model.User;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.model.util.TransactionSummary;

public class PosTransactionDAO extends BasePosTransactionDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public PosTransactionDAO() {
	}

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
			criteria.add(Restrictions.or(Restrictions.isNull(PosTransaction.PROP_VOIDED), Restrictions.eq(PosTransaction.PROP_VOIDED, Boolean.FALSE)));
			criteria.add(Restrictions.eq(PosTransaction.PROP_TRANSACTION_TYPE, TransactionType.CREDIT.name()));
			criteria.add(Restrictions.isNotNull(PosTransaction.PROP_TICKET));
			
			if (owner != null) {
				criteria.add(Restrictions.eq(PosTransaction.PROP_USER, owner));
			}

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<PosTransaction> findAuthorizedTransactions(User owner) {
		Session session = null;

		try {
			session = getSession();

			Criteria criteria = session.createCriteria(CreditCardTransaction.class);
			criteria.add(Restrictions.eq(PosTransaction.PROP_CAPTURED, Boolean.TRUE));
			criteria.add(Restrictions.or(Restrictions.isNull(PosTransaction.PROP_VOIDED), Restrictions.eq(PosTransaction.PROP_VOIDED, Boolean.FALSE)));
			criteria.add(Restrictions.isNotNull(PosTransaction.PROP_TICKET));
			criteria.add(Restrictions.eq(PosTransaction.PROP_DRAWER_RESETTED, Boolean.FALSE));
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date startOfDay = DateUtil.startOfDay(calendar.getTime());
			
			calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			Date endOfDay = DateUtil.endOfDay(calendar.getTime());
			//show credit card transactions of last 2 days
			criteria.add(Restrictions.between(PosTransaction.PROP_TRANSACTION_TIME, startOfDay, endOfDay));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<? extends PosTransaction> findTransactions(Terminal terminal, Class transactionClass, Date from, Date to) {
		Session session = null;

		try {
			session = getSession();

			Criteria criteria = session.createCriteria(transactionClass);
			criteria.add(Restrictions.isNotNull(PosTransaction.PROP_TICKET));
			if (terminal != null) {
				criteria.add(Restrictions.eq(PosTransaction.PROP_TERMINAL, terminal));
			}

			if (from != null && to != null) {
				//criteria.add(Restrictions.ge(PosTransaction.PROP_TRANSACTION_TIME, from));
				//criteria.add(Restrictions.le(PosTransaction.PROP_TRANSACTION_TIME, to));
			}

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<? extends PosTransaction> findTransactions(Class transactionClass, Date from, Date to) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(transactionClass);
			criteria.add(Restrictions.isNotNull(PosTransaction.PROP_TICKET));
			if (from != null && to != null) {
				criteria.add(Restrictions.ge(PosTransaction.PROP_TRANSACTION_TIME, from));
				criteria.add(Restrictions.le(PosTransaction.PROP_TRANSACTION_TIME, to));
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

			if (terminal != null) {
				criteria.add(Restrictions.eq(PosTransaction.PROP_TERMINAL, terminal));
			}

			if (from != null && to != null) {
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