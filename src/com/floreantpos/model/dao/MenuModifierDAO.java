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

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.PosLog;
import com.floreantpos.model.MenuModifier;

public class MenuModifierDAO extends BaseMenuModifierDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public MenuModifierDAO() {
	}

//	public MenuModifier getMenuModifierFromTicketItemModifier(TicketItemModifier ticketItemModifier) {
//		MenuModifier menuModifier = get(ticketItemModifier.getMenuItemId());
//		menuModifier.setMenuItemModifierGroup(ticketItemModifier.getParent().getMenuItemModifierGroup());
//		return menuModifier;
//	}

	public void saveAll(List<MenuModifier> menuModifiers) {
		if (menuModifiers == null) {
			return;
		}
		Session session = null;
		Transaction tx = null;

		try {
			session = createNewSession();
			tx = session.beginTransaction();
			for (MenuModifier menuModifier : menuModifiers) {
				session.saveOrUpdate(menuModifier);
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			PosLog.error(getClass(), e);
		} finally {
			closeSession(session);
		}
	}
}