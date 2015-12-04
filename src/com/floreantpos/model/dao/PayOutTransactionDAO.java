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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.PayOutTransaction;
import com.floreantpos.model.Terminal;


public class PayOutTransactionDAO extends BasePayOutTransactionDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public PayOutTransactionDAO () {}

	public void saveTransaction(PayOutTransaction t, Terminal terminal) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = createNewSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(t);
			session.saveOrUpdate(terminal);
			tx.commit();
			
		} catch (Exception e) {
			try {
				tx.rollback();
			}catch(Exception x) {}
			throw e;
		} finally {
			closeSession(session);
		}
	}
	
	public List<PayOutTransaction> getUnsettled(Terminal terminal) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(PayOutTransaction.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(PayOutTransaction.PROP_TERMINAL, terminal));
			
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
}