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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.DataUpdateInfo;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.InventoryItem;
import com.floreantpos.model.InventoryTransaction;
import com.floreantpos.model.InventoryTransactionType;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PaymentStatusFilter;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Recepie;
import com.floreantpos.model.RecepieItem;
import com.floreantpos.model.Shift;
import com.floreantpos.model.ShopTableStatus;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TransactionType;
import com.floreantpos.model.User;
import com.floreantpos.model.UserType;
import com.floreantpos.model.VoidTransaction;
import com.floreantpos.model.util.TicketSummary;
import com.floreantpos.swing.PaginatedTableModel;

public class TicketDAO extends BaseTicketDAO {
	private final static TicketDAO instance = new TicketDAO();

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public TicketDAO() {
	}

	@Override
	public Order getDefaultOrder() {
		return Order.desc(Ticket.PROP_CREATE_DATE);
	}

	@Override
	public synchronized void saveOrUpdate(Ticket ticket) {
		Session session = null;
		Transaction tx = null;

		try {

			session = createNewSession();
			tx = session.beginTransaction();

			saveOrUpdate(ticket, session);

			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}

			throwException(e);

		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public void saveOrUpdate(Ticket ticket, Session session) {
		//TODO: INVENTORY PLUGIN SUPPORT
		adjustInventoryItems(session, ticket);

		ticket.setActiveDate(Calendar.getInstance().getTime());

		adjustStockAmount(ticket, session);
		updateShopTableStatus(ticket, session);
		session.saveOrUpdate(ticket);

		ticket.clearDeletedItems();

		DataUpdateInfo lastUpdateInfo = DataUpdateInfoDAO.getLastUpdateInfo();
		lastUpdateInfo.setLastUpdateTime(new Date());
		session.update(lastUpdateInfo);
	}

	private void updateShopTableStatus(Ticket ticket, Session session) {
		List<Integer> tableNumbers = ticket.getTableNumbers();
		if (tableNumbers == null || tableNumbers.isEmpty()) {
			return;
		}
		if (ticket.isClosed()) {
			ShopTableStatusDAO.getInstance().removeTicketFromShopTableStatus(ticket, session);
		}
	}

	public void voidTicket(Ticket ticket) throws Exception {
		Session session = null;
		Transaction tx = null;
		Terminal terminal = Application.getInstance().getTerminal();

		try {
			session = createNewSession();
			tx = session.beginTransaction();

			ticket.setVoided(true);
			ticket.setClosed(true);
			ticket.setClosingDate(new Date());
			ticket.setTerminal(terminal);

			if (ticket.isPaid()) {
				VoidTransaction transaction = null;
				if (ticket.getTransactions() != null) {
					for (PosTransaction t : ticket.getTransactions()) {
						if (t instanceof VoidTransaction) {
							transaction = (VoidTransaction) t;
						}
					}
				}
				if (transaction == null)
					transaction = new VoidTransaction();

				transaction.setTicket(ticket);
				transaction.setTerminal(terminal);
				transaction.setTransactionTime(new Date());
				transaction.setTransactionType(TransactionType.DEBIT.name());
				transaction.setPaymentType(PaymentType.CASH.name());
				transaction.setAmount(ticket.getPaidAmount());
				transaction.setTerminal(Application.getInstance().getTerminal());
				transaction.setCaptured(true);

				ticket.addTotransactions(transaction);
			}

			session.update(ticket);
			session.update(terminal);

			session.flush();
			tx.commit();
		} catch (Exception x) {
			try {
				tx.rollback();
			} catch (Exception e) {
			}
			throw x;
		}

		finally {
			closeSession(session);
		}
	}

	public Ticket loadFullTicket(int id) {
		Session session = createNewSession();

		Ticket ticket = (Ticket) session.get(getReferenceClass(), id);

		if (ticket == null)
			return null;

		Hibernate.initialize(ticket.getTicketItems());
		Hibernate.initialize(ticket.getDiscounts());
		Hibernate.initialize(ticket.getTransactions());

		//		List<TicketItem> ticketItems = ticket.getTicketItems();
		//		if (ticketItems != null) {
		//			for (TicketItem ticketItem : ticketItems) {
		//				Hibernate.initialize(ticketItem.getTicketItemModifiers());
		//			}
		//		}

		session.close();

		return ticket;
	}

	public Ticket loadCouponsAndTransactions(int id) {
		Session session = createNewSession();

		Ticket ticket = (Ticket) session.get(getReferenceClass(), id);

		Hibernate.initialize(ticket.getDiscounts());
		Hibernate.initialize(ticket.getTransactions());

		session.close();

		return ticket;
	}

	public List<Gratuity> getServerGratuities(Terminal terminal, String transactionType) {
		Session session = null;
		ArrayList<Gratuity> gratuities = new ArrayList<Gratuity>();

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			criteria.createAlias(Ticket.PROP_GRATUITY, "gratuity"); //$NON-NLS-1$
			criteria.add(Restrictions.eq("gratuity.paid", Boolean.FALSE)); //$NON-NLS-1$

			List list = criteria.list();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				gratuities.add(ticket.getGratuity());
			}
			return gratuities;
		} finally {
			closeSession(session);
		}
	}

	public double getPaidGratuityAmount(Terminal terminal) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass(), "t"); //$NON-NLS-1$
			criteria = criteria.createAlias(Ticket.PROP_GRATUITY, "gratuity"); //$NON-NLS-1$
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			criteria.add(Restrictions.eq("gratuity.paid", Boolean.TRUE)); //$NON-NLS-1$

			criteria.setProjection(Projections.sum("gratuity.amount")); //$NON-NLS-1$

			List list = criteria.list();
			if (list.size() > 0 && list.get(0) instanceof Number) {
				return ((Number) list.get(0)).doubleValue();
			}
			return 0;
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findOpenTickets() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.addOrder(getDefaultOrder());

			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findOpenTicketsByOrderType(OrderType orderType) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			User user = Application.getCurrentUser();
			if (user != null && !user.canViewAllOpenTickets()) {
				criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
			}
			criteria.add(Restrictions.eq(Ticket.PROP_TICKET_TYPE, orderType.getName()));
			criteria.add(Restrictions.eq(Ticket.PROP_BAR_TAB, true));
			criteria.add(Restrictions.or(Restrictions.isEmpty("tableNumbers"), Restrictions.isNull("tableNumbers")));
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.addOrder(getDefaultOrder());

			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findOpenTickets(Integer customerId) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_CUSTOMER_ID, customerId));
			criteria.addOrder(getDefaultOrder());

			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findOpenTickets(Terminal terminal, UserType userType) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));

			if (userType != null) {
				criteria.createAlias(Ticket.PROP_OWNER, "u"); //$NON-NLS-1$
				criteria.add(Restrictions.eq("u.type", userType)); //$NON-NLS-1$
			}
			if (terminal != null) {
				criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			}
			criteria.addOrder(getDefaultOrder());

			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}

	public void loadTickets(PaginatedTableModel tableModel) {
		Session session = null;
		Criteria criteria = null;

		try {
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			updateCriteriaFilters(criteria);
			criteria.addOrder(getDefaultOrder());
			criteria.setFirstResult(tableModel.getCurrentRowIndex());
			criteria.setMaxResults(tableModel.getPageSize());
			tableModel.setRows(criteria.list());
			return;

		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findCustomerTickets(Integer customerId, PaginatedTableModel tableModel) {
		Session session = null;
		Criteria criteria = null;

		try {
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CUSTOMER_ID, customerId));

			/*if (filter.equals(PaymentStatusFilter.OPEN.toString())) {
				criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.FALSE));
				criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			}*/

			criteria.setFirstResult(0);
			criteria.setMaxResults(tableModel.getPageSize());

			List ticketList = criteria.list();

			criteria.setProjection(Projections.rowCount());
			Integer rowCount = (Integer) criteria.uniqueResult();
			if (rowCount != null) {
				tableModel.setNumRows(rowCount);

			}

			tableModel.setCurrentRowIndex(0);

			return ticketList;

		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findNextCustomerTickets(Integer customerId, PaginatedTableModel tableModel, String filter) {
		Session session = null;
		Criteria criteria = null;

		try {
			int nextIndex = tableModel.getNextRowIndex();

			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CUSTOMER_ID, customerId));

			if (filter.equals(PaymentStatusFilter.OPEN)) {
				criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.FALSE));
				criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			}

			criteria.setFirstResult(nextIndex);
			criteria.setMaxResults(tableModel.getPageSize());

			List ticketList = criteria.list();

			criteria.setProjection(Projections.rowCount());
			Integer rowCount = (Integer) criteria.uniqueResult();
			if (rowCount != null) {
				tableModel.setNumRows(rowCount);

			}

			tableModel.setCurrentRowIndex(nextIndex);

			return ticketList;

		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findPreviousCustomerTickets(Integer customerId, PaginatedTableModel tableModel, String filter) {
		Session session = null;
		Criteria criteria = null;
		try {

			int previousIndex = tableModel.getPreviousRowIndex();

			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CUSTOMER_ID, customerId));

			if (filter.equals(PaymentStatusFilter.OPEN)) {
				criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.FALSE));
				criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			}

			criteria.setFirstResult(previousIndex);
			criteria.setMaxResults(tableModel.getPageSize());

			List ticketList = criteria.list();

			criteria.setProjection(Projections.rowCount());
			Integer rowCount = (Integer) criteria.uniqueResult();
			if (rowCount != null) {
				tableModel.setNumRows(rowCount);

			}

			tableModel.setCurrentRowIndex(previousIndex);

			return ticketList;

		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findTicketByCustomer(Integer customerId) {
		Session session = null;
		Criteria criteria = null;

		try {
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CUSTOMER_ID, customerId));

			List ticketList = criteria.list();
			return ticketList;

		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findTickets(PaymentStatusFilter psFilter, String otFilter) {
		return findTicketsForUser(psFilter, otFilter, null);
	}

	public List<Ticket> findTicketsForUser(PaymentStatusFilter psFilter, String otFilter, User user) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());

			if (psFilter == PaymentStatusFilter.OPEN) {
				criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.FALSE));
				criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			}
			else if (psFilter == PaymentStatusFilter.PAID) {
				criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.TRUE));
				criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			}
			else if (psFilter == PaymentStatusFilter.CLOSED) {
				criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
				criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));

				Calendar currentTime = Calendar.getInstance();
				currentTime.add(Calendar.HOUR_OF_DAY, -24);

				criteria.add(Restrictions.ge(Ticket.PROP_CLOSING_DATE, currentTime.getTime()));
			}
			if (otFilter != POSConstants.ALL) {
				criteria.add(Restrictions.eq(Ticket.PROP_TICKET_TYPE, otFilter));
			}

			if (user != null) {
				criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
			}

			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findOpenTicketsForUser(User user) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			//criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			//criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findOpenTickets(Date startDate, Date endDate) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findClosedTickets(Date startDate, Date endDate) {
		Session session = null;

		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			if (startDate != null && endDate != null) {
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			}
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}

	public void closeOrder(Ticket ticket) {

		Session session = null;
		Transaction tx = null;

		try {
			session = createNewSession();
			tx = session.beginTransaction();

			saveOrUpdate(ticket);

			User driver = ticket.getAssignedDriver();
			if (driver != null) {
				driver.setAvailableForDelivery(true);
				UserDAO.getInstance().saveOrUpdate(driver);
			}

			ShopTableDAO.getInstance().releaseTables(ticket);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			LogFactory.getLog(TicketDAO.class).error(e);
			throw new RuntimeException(e);
		} finally {
			closeSession(session);
		}
	}

	public TicketSummary getOpenTicketSummary() {
		Session session = null;
		TicketSummary ticketSummary = new TicketSummary();
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.count(Ticket.PROP_ID));
			projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
			criteria.setProjection(projectionList);

			List list = criteria.list();
			if (list.size() > 0) {
				Object[] o = (Object[]) list.get(0);
				ticketSummary.setTotalTicket(((Integer) o[0]).intValue());
				ticketSummary.setTotalPrice(o[1] == null ? 0 : ((Double) o[1]).doubleValue());
			}
			return ticketSummary;
		} finally {
			closeSession(session);
		}
	}

	public TicketSummary getClosedTicketSummary(Terminal terminal) {

		Session session = null;
		TicketSummary ticketSummary = new TicketSummary();
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.count(Ticket.PROP_ID));
			projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
			criteria.setProjection(projectionList);

			List list = criteria.list();
			if (list.size() > 0) {
				Object[] o = (Object[]) list.get(0);
				ticketSummary.setTotalTicket(((Integer) o[0]).intValue());
				ticketSummary.setTotalPrice(o[1] == null ? 0 : ((Double) o[1]).doubleValue());
			}
			return ticketSummary;
		} finally {
			closeSession(session);
		}
	}

	//	public void saveTransaction(Ticket ticket, com.floreantpos.model.PosTransaction transaction, Terminal terminal, User user) throws Exception {
	//		Session session = null;
	//    	Transaction tx = null;
	//    	try {
	//    		if(transaction instanceof CashTransaction) {
	//    			terminal.setCurrentBalance(terminal.getCurrentBalance() + ticket.getTotalPrice());
	//    		}
	//			
	//			ticket.setVoided(false);
	//			ticket.setPaid(true);
	//			ticket.setClosed(true);
	//			ticket.setDrawerResetted(false);
	//			ticket.setClosingDate(new Date());
	//			ticket.setTerminal(terminal);
	//			
	//			transaction.setTicket(ticket);
	//			transaction.setAmount(ticket.getSubTotal());
	//			transaction.setTaxAmount(ticket.getTotalTax());
	//			transaction.setAppliedDiscount(ticket.getTotalDiscount());
	//			transaction.setTerminal(terminal);
	//			transaction.setUser(user);
	//			transaction.setTransactionTime(new Date());
	//			
	//			session = createNewSession();
	//			tx = session.beginTransaction();
	//			
	//			saveOrUpdate(ticket, session);
	//			saveOrUpdate(transaction, session);
	//			saveOrUpdate(terminal, session);
	//			
	//			tx.commit();
	//			
	//		} catch (Exception e) {
	//			try {
	//				tx.rollback();
	//			}catch (Exception x) {}
	//			
	//			throw e;
	//		} finally {
	//			closeSession(session);
	//		}
	//	}

	public List<Ticket> findTickets(Date startDate, Date endDate, boolean closed) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			//criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.valueOf(closed)));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findTickets(Date startDate, Date endDate, boolean closed, Terminal terminal) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.valueOf(closed)));

			if (terminal != null) {
				criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			}

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findTicketsForLaborHour(Date startDate, Date endDate, int hour, UserType userType, Terminal terminal) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_ACTIVE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_ACTIVE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_CREATION_HOUR, Integer.valueOf(hour)));
			//criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			//criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));

			if (userType != null) {
				criteria.createAlias(Ticket.PROP_OWNER, "u"); //$NON-NLS-1$
				criteria.add(Restrictions.eq("u.type", userType)); //$NON-NLS-1$
			}
			if (terminal != null) {
				criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			}

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findTicketsForShift(Date startDate, Date endDate, Shift shit, UserType userType, Terminal terminal) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_SHIFT, shit));
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));

			if (userType != null) {
				criteria.createAlias(Ticket.PROP_OWNER, "u"); //$NON-NLS-1$
				criteria.add(Restrictions.eq("u.type", userType)); //$NON-NLS-1$
			}
			if (terminal != null) {
				criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			}

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public static TicketDAO getInstance() {
		return instance;
	}

	private void adjustInventoryItems(Session session, Ticket ticket) {
		List<TicketItem> ticketItems = ticket.getTicketItems();
		if (ticketItems == null) {
			return;
		}

		for (TicketItem ticketItem : ticketItems) {
			if (ticketItem.isInventoryHandled()) {
				continue;
			}

			Integer menuItemId = ticketItem.getItemId();
			MenuItem menuItem = MenuItemDAO.getInstance().get(menuItemId);

			if (menuItem == null) {
				continue;
			}

			Recepie recepie = menuItem.getRecepie();

			if (recepie == null) {
				continue;
			}

			List<RecepieItem> recepieItems = recepie.getRecepieItems();
			for (RecepieItem recepieItem : recepieItems) {
				if (!recepieItem.isInventoryDeductable()) {
					continue;
				}

				Double percentage = recepieItem.getPercentage() / 100.0;

				InventoryItem inventoryItem = recepieItem.getInventoryItem();
				Double totalRecepieUnits = inventoryItem.getTotalRecepieUnits();

				Double itemQuantity = 0.0;
				if (ticketItem.isFractionalUnit()) {
					itemQuantity = ticketItem.getItemQuantity();
				}
				else {
					itemQuantity = (double) ticketItem.getItemCount();
				}
				inventoryItem.setTotalPackages(inventoryItem.getTotalPackages() - (itemQuantity / inventoryItem.getUnitPerPackage()));
				inventoryItem.setTotalRecepieUnits(totalRecepieUnits - (itemQuantity * percentage));

				session.saveOrUpdate(inventoryItem);

				InventoryTransaction transaction = new InventoryTransaction();
				transaction.setType(InventoryTransactionType.OUT);
				transaction.setUnitPrice(inventoryItem.getUnitSellingPrice());
				transaction.setInventoryItem(inventoryItem);
				transaction.setQuantity(itemQuantity);
				transaction.setRemark(Messages.getString("TicketDAO.0") + ticketItem.getName() + Messages.getString("TicketDAO.11") + ticket.getId()); //$NON-NLS-1$ //$NON-NLS-2$

				session.save(transaction);
			}

			ticketItem.setInventoryHandled(true);
		}

		List deletedItems = ticket.getDeletedItems();
		if (deletedItems == null) {
			return;
		}

		for (Object o : deletedItems) {
			if (!(o instanceof TicketItem)) {
				continue;
			}

			TicketItem ticketItem = (TicketItem) o;

			if (!ticketItem.isInventoryHandled()) {
				continue;
			}

			Integer menuItemId = ticketItem.getItemId();
			MenuItem menuItem = MenuItemDAO.getInstance().get(menuItemId);
			Recepie recepie = menuItem.getRecepie();

			if (recepie == null) {
				return;
			}

			List<RecepieItem> recepieItems = recepie.getRecepieItems();
			for (RecepieItem recepieItem : recepieItems) {
				if (!recepieItem.isInventoryDeductable()) {
					continue;
				}

				InventoryItem inventoryItem = recepieItem.getInventoryItem();

				Double itemQuantity = 0.0;
				if (ticketItem.isFractionalUnit()) {
					itemQuantity = ticketItem.getItemQuantity();
				}
				else {
					itemQuantity = (double) ticketItem.getItemCount();
				}
				inventoryItem.setTotalPackages(inventoryItem.getTotalPackages() + (itemQuantity / inventoryItem.getUnitPerPackage()));
				Double totalRecepieUnits = inventoryItem.getTotalRecepieUnits();
				inventoryItem.setTotalRecepieUnits(totalRecepieUnits + itemQuantity);

				session.saveOrUpdate(inventoryItem);

				InventoryTransaction transaction = new InventoryTransaction();
				transaction.setType(InventoryTransactionType.IN);
				transaction.setUnitPrice(inventoryItem.getUnitSellingPrice());
				transaction.setInventoryItem(inventoryItem);
				transaction.setQuantity(itemQuantity);
				transaction.setRemark(Messages.getString("TicketDAO.1") + ticketItem.getName() + " was canceled for ticket " + ticket.getId()); //$NON-NLS-1$ //$NON-NLS-2$

				session.save(transaction);
			}

			ticketItem.setInventoryHandled(true);
		}
	}

	//private methods

	private void updateCriteriaFilters(Criteria criteria) {
		User user = Application.getCurrentUser();
		PaymentStatusFilter paymentStatusFilter = com.floreantpos.config.TerminalConfig.getPaymentStatusFilter();
		String orderTypeFilter = com.floreantpos.config.TerminalConfig.getOrderTypeFilter();

		if (paymentStatusFilter == PaymentStatusFilter.OPEN) {
			criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			if (!user.canViewAllOpenTickets()) {
				criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
			}
		}
		else if (paymentStatusFilter == PaymentStatusFilter.PAID) {
			criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			if (!user.canViewAllOpenTickets() || !user.canViewAllCloseTickets()) {
				criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
			}
		}
		else if (paymentStatusFilter == PaymentStatusFilter.CLOSED) {
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			if (!user.canViewAllCloseTickets()) {
				criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
			}
		}

		if (!orderTypeFilter.equals(POSConstants.ALL)) {
			criteria.add(Restrictions.eq(Ticket.PROP_TICKET_TYPE, orderTypeFilter));
		}

	}

	public void deleteTickets(List<Ticket> tickets) {
		Session session = null;
		Transaction tx = null;

		try {
			session = createNewSession();
			tx = session.beginTransaction();

			for (Ticket ticket : tickets) {
				super.delete(ticket, session);
				//KitchenTicketDAO.getInstance().deleteKitchenTicket(ticket.getId());
			}

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			LogFactory.getLog(TicketDAO.class).error(e);
			throw new RuntimeException(e);
		} finally {
			closeSession(session);
		}
	}

	private void adjustStockAmount(Ticket ticket, Session session) {

		List<TicketItem> items = ticket.getTicketItems();

		HashMap<Integer, Double> itemMap = new LinkedHashMap<Integer, Double>();

		if (!getAdjustedMap(items, itemMap)) {
			return;
		}

		for (Integer ticketItemId : itemMap.keySet()) {

			MenuItem menuItem = MenuItemDAO.getInstance().get(ticketItemId);

			if (menuItem == null) {
				continue;
			}

			if (menuItem.getStockAmount() <= 0) {
				continue;
			}

			double availableStockAmount;
			double stockAmount = menuItem.getStockAmount();

			if (menuItem.isFractionalUnit()) {
				availableStockAmount = stockAmount - itemMap.get(ticketItemId);
			}
			else {
				availableStockAmount = stockAmount - itemMap.get(ticketItemId);
			}
			menuItem.setStockAmount(availableStockAmount);

			session.saveOrUpdate(menuItem);
		}
	}

	private boolean getAdjustedMap(List<TicketItem> items, HashMap<Integer, Double> itemMap) {

		for (TicketItem ticketItem : items) {

			if (ticketItem.isStockAmountAdjusted()) {
				return false;
			}

			Double previousValue = itemMap.get(ticketItem.getItemId());

			if (previousValue == null) {
				previousValue = 0.0;
			}

			if (ticketItem.isFractionalUnit()) {
				itemMap.put(ticketItem.getItemId(), ticketItem.getItemQuantity() + previousValue);
			}
			else {
				itemMap.put(ticketItem.getItemId(), ticketItem.getItemCount() + previousValue);
			}
			ticketItem.setStockAmountAdjusted(true);
		}
		return true;
	}

	public List<Ticket> findTickets(PaginatedTableModel tableModel, boolean filter) {
		return findTickets(tableModel, null, null, filter);
	}

	public List<Ticket> findTickets(PaginatedTableModel tableModel, Date start, Date end, boolean filter) {
		Session session = null;
		Criteria criteria = null;

		try {
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());

			if (filter) {
				updateCriteriaFilters(criteria);
			}

			criteria.setFirstResult(0);
			criteria.setMaxResults(tableModel.getPageSize());
			if (start != null)
				criteria.add(Restrictions.ge(Ticket.PROP_DELIVERY_DATE, start));

			if (end != null)
				criteria.add(Restrictions.le(Ticket.PROP_DELIVERY_DATE, end));

			List ticketList = criteria.list();

			criteria.setProjection(Projections.rowCount());
			Integer rowCount = (Integer) criteria.uniqueResult();
			if (rowCount != null) {
				tableModel.setNumRows(rowCount);

			}

			tableModel.setCurrentRowIndex(0);

			return ticketList;

		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findNextTickets(PaginatedTableModel tableModel, boolean filter) {
		return findNextTickets(tableModel, null, null, filter);
	}

	public List<Ticket> findNextTickets(PaginatedTableModel tableModel, Date start, Date end, boolean filter) {
		Session session = null;
		Criteria criteria = null;

		try {
			int nextIndex = tableModel.getNextRowIndex();

			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());

			if (filter) {
				updateCriteriaFilters(criteria);
			}

			criteria.setFirstResult(nextIndex);
			criteria.setMaxResults(tableModel.getPageSize());

			if (start != null)
				criteria.add(Restrictions.ge(Ticket.PROP_DELIVERY_DATE, start));

			if (end != null)
				criteria.add(Restrictions.le(Ticket.PROP_DELIVERY_DATE, end));

			List ticketList = criteria.list();

			criteria.setProjection(Projections.rowCount());
			Integer rowCount = (Integer) criteria.uniqueResult();
			if (rowCount != null) {
				tableModel.setNumRows(rowCount);

			}

			tableModel.setCurrentRowIndex(nextIndex);

			return ticketList;

		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findPreviousTickets(PaginatedTableModel tableModel, boolean filter) {
		return findPreviousTickets(tableModel, null, null, filter);
	}

	public List<Ticket> findPreviousTickets(PaginatedTableModel tableModel, Date start, Date end, boolean filter) {
		Session session = null;
		Criteria criteria = null;
		try {

			int previousIndex = tableModel.getPreviousRowIndex();

			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());

			if (filter) {
				updateCriteriaFilters(criteria);
			}

			criteria.setFirstResult(previousIndex);
			criteria.setMaxResults(tableModel.getPageSize());

			if (start != null)
				criteria.add(Restrictions.ge(Ticket.PROP_DELIVERY_DATE, start));

			if (end != null)
				criteria.add(Restrictions.le(Ticket.PROP_DELIVERY_DATE, end));

			List ticketList = criteria.list();

			criteria.setProjection(Projections.rowCount());
			Integer rowCount = (Integer) criteria.uniqueResult();
			if (rowCount != null) {
				tableModel.setNumRows(rowCount);

			}

			tableModel.setCurrentRowIndex(previousIndex);

			return ticketList;

		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> getTicketsWithSpecificFields(String... fields) {
		Session session = null;
		Criteria criteria = null;
		User currentUser = Application.getCurrentUser();
		boolean filterUser = !currentUser.isAdministrator() || !currentUser.isManager();
		try {
			session = createNewSession();
			criteria = session.createCriteria(Ticket.class);
			ProjectionList projectionList = Projections.projectionList();
			for (String field : fields) {
				projectionList.add(Projections.property(field));
			}
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			if (filterUser) {
				criteria.createAlias(Ticket.PROP_OWNER, "u");
				criteria.add(Restrictions.eq("u.userId", currentUser.getUserId()));
			}
			ResultTransformer transformer = new ResultTransformer() {

				public Object transformTuple(Object[] row, String[] arg1) {
					Ticket ticket = new Ticket();
					ticket.setId(Integer.valueOf("" + row[0]));
					ticket.setDueAmount(Double.valueOf("" + row[1]));
					ticket.setCreateDate((Date) row[2]);
					return ticket;
				}

				public List transformList(List arg0) {
					return arg0;
				}
			};
			criteria.setProjection(projectionList).setResultTransformer(transformer);
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public int getNumTickets() {
		Session session = null;
		Criteria criteria = null;
		try {
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			updateCriteriaFilters(criteria);

			criteria.setProjection(Projections.rowCount());
			Number rowCount = (Number) criteria.uniqueResult();
			if (rowCount != null) {
				return rowCount.intValue();

			}
			return 0;
		} finally {
			closeSession(session);
		}
	}

	public int getNumTickets(Date start, Date end) {
		Session session = null;
		Criteria criteria = null;
		try {
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			if (start != null)
				criteria.add(Restrictions.ge(Ticket.PROP_DELIVERY_DATE, start));

			if (end != null)
				criteria.add(Restrictions.le(Ticket.PROP_DELIVERY_DATE, end));

			criteria.add(Restrictions.isNotNull(Ticket.PROP_DELIVERY_DATE));
			criteria.setProjection(Projections.rowCount());
			Number rowCount = (Number) criteria.uniqueResult();
			if (rowCount != null) {
				return rowCount.intValue();
			}
			return 0;
		} finally {
			closeSession(session);
		}
	}

	public void loadTickets(PaginatedTableModel tableModel, Date start, Date end) {
		Session session = null;
		Criteria criteria = null;
		try {
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			if (start != null)
				criteria.add(Restrictions.ge(Ticket.PROP_DELIVERY_DATE, start));

			if (end != null)
				criteria.add(Restrictions.le(Ticket.PROP_DELIVERY_DATE, end));

			criteria.add(Restrictions.isNotNull(Ticket.PROP_DELIVERY_DATE));
			criteria.setFirstResult(tableModel.getCurrentRowIndex());
			criteria.setMaxResults(tableModel.getPageSize());
			tableModel.setRows(criteria.list());
			return;

		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findTicketsByTableNum(int tableNumber) {
		Session session = null;
		Criteria criteria = null;
		try {
			session = createNewSession();
			criteria = session.createCriteria(ShopTableStatus.class);
			criteria.add(Restrictions.eq(ShopTableStatus.PROP_ID, tableNumber));
			
			ShopTableStatus status = (ShopTableStatus) criteria.uniqueResult();
			List<Ticket> tickets = new ArrayList<>();
			if (status != null) {
				List<Integer> list = status.getListOfTicketNumbers();
				if (list != null && !list.isEmpty()) {
					for (Integer ticketId : list) {
						Ticket ticket = TicketDAO.getInstance().get(ticketId);
						if (ticket != null) {
							tickets.add(ticket);
						}
					}
				}
			}
			return tickets;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}