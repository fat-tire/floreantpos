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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.KitchenTicket.KitchenTicketStatus;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PrinterGroup;
import com.floreantpos.model.Ticket;
import com.floreantpos.swing.PaginatedListModel;
import com.floreantpos.swing.PaginatedTableModel;

public class KitchenTicketDAO extends BaseKitchenTicketDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public KitchenTicketDAO() {
	}

	public List<KitchenTicket> findAllOpen() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(KitchenTicket.PROP_STATUS, KitchenTicketStatus.WAITING.name()));
			List list = criteria.list();

			return list;
		} finally {
			closeSession(session);
		}
	}

	public List<KitchenTicket> findByParentId(Integer ticketId) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(KitchenTicket.PROP_TICKET_ID, ticketId));
			List list = criteria.list();

			return list;
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findNextKitchenTickets(PaginatedTableModel tableModel) {
		Session session = null;
		Criteria criteria = null;

		try {
			int nextIndex = tableModel.getNextRowIndex();

			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());

			criteria.setFirstResult(nextIndex);
			criteria.setMaxResults(tableModel.getPageSize());

			List kitchenTicketList = criteria.list();

			criteria.setProjection(Projections.rowCount());
			Integer rowCount = (Integer) criteria.uniqueResult();
			if (rowCount != null) {
				tableModel.setNumRows(rowCount);

			}
			tableModel.setCurrentRowIndex(nextIndex);

			return kitchenTicketList;

		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findPreviousKitchenTickets(PaginatedTableModel tableModel) {
		Session session = null;
		Criteria criteria = null;
		try {

			int previousIndex = tableModel.getPreviousRowIndex();

			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());

			criteria.setFirstResult(previousIndex);
			criteria.setMaxResults(tableModel.getPageSize());

			List kitchenTicketList = criteria.list();

			criteria.setProjection(Projections.rowCount());
			Integer rowCount = (Integer) criteria.uniqueResult();
			if (rowCount != null) {
				tableModel.setNumRows(rowCount);

			}

			tableModel.setCurrentRowIndex(previousIndex);

			return kitchenTicketList;

		} finally {
			closeSession(session);
		}
	}

	public int getRowCount(String selectedKDSPrinter, OrderType orderType) {
		Session session = null;
		Criteria criteria = null;
		try {
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(KitchenTicket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(KitchenTicket.PROP_STATUS, KitchenTicketStatus.WAITING.name()));
			if (orderType != null) {
				criteria.add(Restrictions.eq(KitchenTicket.PROP_TICKET_TYPE, orderType.getName()));
			}
			criteria.setProjection(Projections.rowCount());
			Number rowCount = (Number) criteria.uniqueResult();
			if (rowCount != null) {
				return rowCount.intValue();
			}
		} finally {
			closeSession(session);
		}
		return 0;
	}

	public void loadKitchenTickets(String selectedKDSPrinter, OrderType orderType, PaginatedListModel listModel) {
		Session session = null;
		Criteria criteria = null;
		try {
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(KitchenTicket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(KitchenTicket.PROP_STATUS, KitchenTicketStatus.WAITING.name()));
			if (orderType != null) {
				criteria.add(Restrictions.eq(KitchenTicket.PROP_TICKET_TYPE, orderType.getName()));
			}
			criteria.setFirstResult(listModel.getCurrentRowIndex());
			criteria.setMaxResults(listModel.getPageSize());
			criteria.addOrder(Order.desc(KitchenTicket.PROP_CREATE_DATE));

			List<KitchenTicket> tickets = criteria.list();
			if (selectedKDSPrinter != null) {
				for (Iterator iterator = tickets.iterator(); iterator.hasNext();) {
					KitchenTicket kitchenTicket = (KitchenTicket) iterator.next();
					PrinterGroup printerGroup = kitchenTicket.getPrinterGroup();
					if (printerGroup != null && printerGroup.getPrinterNames() != null) {
						if (!printerGroup.getPrinterNames().contains(selectedKDSPrinter)) {
							iterator.remove();
							listModel.setNumRows(listModel.getNumRows() - 1);
						}
					}
				}
			}
			if (tickets == null) {
				tickets = new ArrayList<>();
			}
			listModel.setData(tickets);
		} finally {
			closeSession(session);
		}
	}
}