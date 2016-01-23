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

import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import com.floreantpos.model.ShopFloor;
import com.floreantpos.model.ShopTable;

public class ShopFloorDAO extends BaseShopFloorDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ShopFloorDAO() {
	}

	public boolean hasFloor() {
		Number result = (Number) getSession().createCriteria(getReferenceClass()).setProjection(Projections.rowCount()).uniqueResult();
		return result.intValue() != 0;
	}

	@Override
	public void delete(ShopFloor shopFloor) throws HibernateException {
		Session session = null;
		Transaction tx = null;

		try {
			session = createNewSession();
			tx = session.beginTransaction();

			Set<ShopTable> tables = shopFloor.getTables();

			if (tables != null && !tables.isEmpty()) {
				shopFloor.getTables().removeAll(tables);
				saveOrUpdate(shopFloor);
			}

			super.delete(shopFloor, session);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			LogFactory.getLog(ShopFloorDAO.class).error(e);

			throw new HibernateException(e);
		} finally {
			closeSession(session);
		}
	}
}