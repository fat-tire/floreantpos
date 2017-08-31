package com.floreantpos.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.GuestCheckPrint;
import com.floreantpos.model.Ticket;

public class GuestCheckPrintDAO extends BaseGuestCheckPrintDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public GuestCheckPrintDAO() {
	}

	public List<GuestCheckPrint> findRecentPrints() {
		Session session = null;
		List<GuestCheckPrint> guestCheckPrints = new ArrayList<GuestCheckPrint>();
		try {
			session = createNewSession();
			List<Ticket> openTickets = TicketDAO.getInstance().findOpenTickets();
			for (Ticket ticket : openTickets) {
				Criteria criteria = session.createCriteria(GuestCheckPrint.class);
				criteria.addOrder(Order.desc(GuestCheckPrint.PROP_PRINT_TIME));
				criteria.setMaxResults(1);
				criteria.add(Restrictions.eq(GuestCheckPrint.PROP_TICKET_ID, ticket.getId()));
				GuestCheckPrint uniqueResult = (GuestCheckPrint) criteria.uniqueResult();
				if (uniqueResult != null) {
					guestCheckPrints.add(uniqueResult);
				}
			}
			return guestCheckPrints;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<GuestCheckPrint> findRecentPrints(List<Integer> ticketIs) {
		Session session = null;
		List<GuestCheckPrint> guestCheckPrints = new ArrayList<GuestCheckPrint>();
		try {
			session = createNewSession();
			for (Integer ticket : ticketIs) {
				Criteria criteria = session.createCriteria(GuestCheckPrint.class);
				criteria.addOrder(Order.desc(GuestCheckPrint.PROP_PRINT_TIME));
				criteria.setMaxResults(1);
				criteria.add(Restrictions.eq(GuestCheckPrint.PROP_TICKET_ID, ticket));
				GuestCheckPrint uniqueResult = (GuestCheckPrint) criteria.uniqueResult();
				if (uniqueResult != null) {
					guestCheckPrints.add(uniqueResult);
				}
			}
			return guestCheckPrints;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}