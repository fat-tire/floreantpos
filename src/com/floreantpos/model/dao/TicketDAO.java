package com.floreantpos.model.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.main.Application;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.InventoryItem;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.Recepie;
import com.floreantpos.model.RecepieItem;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.TransactionType;
import com.floreantpos.model.User;
import com.floreantpos.model.VoidTransaction;
import com.floreantpos.model.util.TicketSummary;
import com.floreantpos.services.PosTransactionService;

public class TicketDAO extends BaseTicketDAO {
	private final static TicketDAO instance = new TicketDAO();

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public TicketDAO() {
	}
	
	@Override
	public void saveOrUpdate(Ticket ticket) {
		adjustInventoryItems(ticket);
		ticket.setActiveDate(Calendar.getInstance().getTime());
		super.saveOrUpdate(ticket);
		
		ticket.clearDeletedItems();
	}

	@Override
	public void saveOrUpdate(Ticket ticket, Session s) {
		adjustInventoryItems(ticket);
		ticket.setActiveDate(Calendar.getInstance().getTime());
		super.saveOrUpdate(ticket, s);
		
		ticket.clearDeletedItems();
	}
	
	public Ticket loadFullTicket(int id) {
		Session session = createNewSession();
		
		Ticket ticket = (Ticket) session.get(getReferenceClass(), id);
		
		if(ticket == null) return null;
		
		Hibernate.initialize(ticket.getTicketItems());
		Hibernate.initialize(ticket.getCouponAndDiscounts());
		Hibernate.initialize(ticket.getTransactions());

		List<TicketItem> ticketItems = ticket.getTicketItems();
		if (ticketItems != null) {
			for (TicketItem ticketItem : ticketItems) {
				List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
				Hibernate.initialize(ticketItemModifierGroups);
				if (ticketItemModifierGroups != null) {
					for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
						Hibernate.initialize(ticketItemModifierGroup.getTicketItemModifiers());
					}
				}
			}
		}
		
		session.close();
		
		return ticket;
	}
	
	public Ticket loadCouponsAndTransactions(int id) {
		Session session = createNewSession();
		
		Ticket ticket = (Ticket) session.get(getReferenceClass(), id);
		
		Hibernate.initialize(ticket.getCouponAndDiscounts());
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
			criteria.createAlias(Ticket.PROP_GRATUITY, "gratuity");
			criteria.add(Restrictions.eq("gratuity.paid", Boolean.FALSE));

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
			Criteria criteria = session.createCriteria(getReferenceClass(), "t");
			criteria = criteria.createAlias(Ticket.PROP_GRATUITY, "gratuity");
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			criteria.add(Restrictions.eq("gratuity.paid", Boolean.TRUE));
			criteria.setProjection(Projections.sum("gratuity.amount"));
			
			List list = criteria.list();
			if(list.size() > 0 && list.get(0) instanceof Number) {
				return ((Number) list.get(0)).doubleValue();
			}
			return 0;
		} finally {
			closeSession(session);
		}
	}

	public void voidTicket(Ticket ticket) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = createNewSession();
			tx = session.beginTransaction();
		
			Terminal terminal = Application.getInstance().getTerminal();
			
			ticket.setVoided(true);
			ticket.setClosed(true);
			ticket.setClosingDate(new Date());
			ticket.setTerminal(terminal);
			
			if(ticket.isPaid()) {
				VoidTransaction transaction = new VoidTransaction();
				transaction.setTicket(ticket);
				transaction.setTerminal(terminal);
				transaction.setTransactionTime(new Date());
				transaction.setTransactionType(TransactionType.DEBIT.name());
				transaction.setPaymentType(PaymentType.CASH.name());
				transaction.setAmount(ticket.getPaidAmount());
				transaction.setTerminal(Application.getInstance().getTerminal());
				transaction.setCaptured(true);
				
				PosTransactionService.adjustTerminalBalance(transaction);
				
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

	public List<Ticket> findOpenTickets() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
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

//	public Ticket findTicketByTableNumber(int tableNumber) {
//		Session session = null;
//
//		try {
//			session = getSession();
//			Criteria criteria = session.createCriteria(getReferenceClass());
//			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_TABLE_NUMBER, Integer.valueOf(tableNumber)));
//			
//			List list = criteria.list();
//			if(list.size() <= 0) {
//				return null;
//			}
//			
//			return (Ticket) list.get(0);
//		} finally {
//			closeSession(session);
//		}
//	}
	
//	public boolean hasTicketByTableNumber(int tableNumber) {
//		return findTicketByTableNumber(tableNumber) != null;
//	}

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

	public List<Ticket> findTicketsForLaborHour(Date startDate, Date endDate, int hour, String userType, Terminal terminal) {
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
				criteria.createAlias(Ticket.PROP_OWNER, "u");
				criteria.add(Restrictions.eq("u.type", userType));
			}
			if (terminal != null) {
				criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			}

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findTicketsForShift(Date startDate, Date endDate, Shift shit, String userType, Terminal terminal) {
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
				criteria.createAlias(Ticket.PROP_OWNER, "u");
				criteria.add(Restrictions.eq("u.type", userType));
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
	
	private void adjustInventoryItems(Ticket ticket) {
		List<TicketItem> ticketItems = ticket.getTicketItems();
		if(ticketItems == null) {
			return;
		}
		
		for (TicketItem ticketItem : ticketItems) {
			if(ticketItem.isInventoryHandled()) {
				continue;
			}
			
			Integer menuItemId = ticketItem.getItemId();
			MenuItem menuItem = MenuItemDAO.getInstance().get(menuItemId);
			Recepie recepie = menuItem.getRecepie();
			
			if(recepie == null) {
				return;
			}
			
			List<RecepieItem> recepieItems = recepie.getRecepieItems();
			for (RecepieItem recepieItem : recepieItems) {
				if(!recepieItem.isInventoryDeductable()) {
					continue;
				}
				
				InventoryItem inventoryItem = recepieItem.getInventoryItem();
				Double totalRecepieUnits = inventoryItem.getTotalRecepieUnits();
				inventoryItem.setTotalRecepieUnits(totalRecepieUnits - ticketItem.getItemCount());
				
				InventoryItemDAO.getInstance().saveOrUpdate(inventoryItem);
			}
			
			ticketItem.setInventoryHandled(true);
		}
		
		List deletedItems = ticket.getDeletedItems();
		if(deletedItems == null) {
			return;
		}
		
		for (Object o : deletedItems) {
			if(!(o instanceof TicketItem)) {
				continue;
			}
			
			TicketItem ticketItem = (TicketItem) o;
			
			if(!ticketItem.isInventoryHandled()) {
				continue;
			}
			
			Integer menuItemId = ticketItem.getItemId();
			MenuItem menuItem = MenuItemDAO.getInstance().get(menuItemId);
			Recepie recepie = menuItem.getRecepie();
			
			if(recepie == null) {
				return;
			}
			
			List<RecepieItem> recepieItems = recepie.getRecepieItems();
			for (RecepieItem recepieItem : recepieItems) {
				if(!recepieItem.isInventoryDeductable()) {
					continue;
				}
				
				InventoryItem inventoryItem = recepieItem.getInventoryItem();
				Double totalRecepieUnits = inventoryItem.getTotalRecepieUnits();
				inventoryItem.setTotalRecepieUnits(totalRecepieUnits + ticketItem.getItemCount());
				
				InventoryItemDAO.getInstance().saveOrUpdate(inventoryItem);
			}
			
			ticketItem.setInventoryHandled(true);
		}
	}
}