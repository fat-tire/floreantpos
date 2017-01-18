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

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.CashDrawerResetHistory;
import com.floreantpos.model.DrawerAssignedHistory;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;

public class TerminalDAO extends BaseTerminalDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public TerminalDAO() {
	}

	public void refresh(Terminal terminal) {
		Session session = null;

		try {
			session = getSession();
			session.refresh(terminal);
		} finally {
			closeSession(session);
		}
	}
	
	/*
	 * Save a list of objects in a batch operation
	 */
	public void performBatchSave(Object... objects) {
		Session session = null;
		Transaction  tx = null;
		
		try {
			session = createNewSession();
			tx = session.beginTransaction();
			for (Object object : objects) {
				if (object != null) {
					session.saveOrUpdate(object);
				}
			}
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

	/**
	 * Get terminal by terminal key
	 * 
	 * @param terminalKey
	 * @return
	 */
	public Terminal getByTerminalKey(String terminalKey) {
		Session session = null;
		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Terminal.PROP_TERMINAL_KEY, terminalKey));
			List<Terminal> list = criteria.list();
			if (list.size() > 0) {
				return list.get(0);
			}
			return null;
		} finally {
			closeSession(session);
		}
	}

	public void resetCashDrawer(DrawerPullReport report, Terminal terminal, User user, double balance) throws Exception {
		Session session = null;
		Transaction tx = null;

		CashDrawerResetHistory history = new CashDrawerResetHistory();
		history.setDrawerPullReport(report);
		history.setResetedBy(user);
		history.setResetTime(new Date());

		try {
			session = createNewSession();
			tx = session.beginTransaction();

			String hql = "update Ticket t set t.drawerResetted=true where t." + Ticket.PROP_CLOSED + "=true and t.drawerResetted=false and t.terminal=:terminal"; //$NON-NLS-1$ //$NON-NLS-2$
			Query query = session.createQuery(hql);
			query.setEntity("terminal", terminal); //$NON-NLS-1$
			query.executeUpdate();

			hql = "update PosTransaction t set t.drawerResetted=true where t.drawerResetted=false and t.terminal=:terminal"; //$NON-NLS-1$
			query = session.createQuery(hql);
			query.setEntity("terminal", terminal); //$NON-NLS-1$
			query.executeUpdate();

			terminal.setAssignedUser(null);
			terminal.setOpeningBalance(balance);
			terminal.setCurrentBalance(balance);
			update(terminal, session);
			save(report, session);
			save(history, session);

			DrawerAssignedHistory history2 = new DrawerAssignedHistory();
			history2.setTime(new Date());
			history2.setOperation(DrawerAssignedHistory.CLOSE_OPERATION);
			history2.setUser(user);
			save(history2, session);

			tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception x) {
			}
			throw e;
		} finally {
			closeSession(session);
		}
	}
}