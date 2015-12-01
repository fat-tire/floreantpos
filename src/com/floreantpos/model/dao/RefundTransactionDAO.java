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
			projectionList.add(Projections.sum(RefundTransaction.PROP_AMOUNT));
			criteria.setProjection(projectionList);
			
			criteria.add(Restrictions.eq(RefundTransaction.PROP_TERMINAL, terminal));
			criteria.add(Restrictions.eq(RefundTransaction.PROP_DRAWER_RESETTED, Boolean.FALSE));
			
			List list = criteria.list();
			if (list.size() > 0) {
				Object[] objects = (Object[]) list.get(0);
				
				if (objects.length > 0 && objects[0] != null) {
					refundSummary.setCount(((Number) objects[0]).intValue());
				}

				if (objects.length > 1 && objects[1] != null) {
					refundSummary.setAmount(((Number) objects[1]).doubleValue());
				}
			}
			return refundSummary;
		} finally {
			closeSession(session);
		}
		
	}
}