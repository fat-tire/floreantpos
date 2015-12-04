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
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.Shift;


public class ShiftDAO extends BaseShiftDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ShiftDAO () {}

	public boolean exists(String shiftName) throws PosException {
		Session session = null;
		
		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Shift.PROP_NAME, shiftName));
			List list = criteria.list();
			if (list != null && list.size() > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new PosException("An error occured while trying to check Shift duplicacy", e); //$NON-NLS-1$
		} finally {
			if(session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	public void refresh(Shift shift) throws PosException {
		Session session = null;
		
		try {
			session = createNewSession();
			session.refresh(shift);
		} catch (Exception e) {
			throw new PosException("An error occured while refreshing Shift state.", e); //$NON-NLS-1$
		} finally {
			if(session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
	}
}