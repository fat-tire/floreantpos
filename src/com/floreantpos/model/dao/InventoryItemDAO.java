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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.PosLog;
import com.floreantpos.model.MenuItem;


public class InventoryItemDAO extends BaseInventoryItemDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public InventoryItemDAO () {}
	
	public boolean hasInventoryItemByName(String name) {
		Session session = null;

		try {
			
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_NAME, name));

			return criteria.list().size() > 0;
			
		} catch (Exception e) {
			PosLog.error(getClass(), e);
			throw new PosException(Messages.getString("InventoryItemDAO.0")); //$NON-NLS-1$
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}
}