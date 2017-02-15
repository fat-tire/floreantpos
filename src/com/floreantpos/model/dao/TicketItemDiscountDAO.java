package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemDiscount;


public class TicketItemDiscountDAO extends BaseTicketItemDiscountDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public TicketItemDiscountDAO () {}
	
	public List<TicketItemDiscount> findTicketItemDiscounts(TicketItem ticketItem) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(TicketItemDiscount.PROP_TICKET_ITEM,ticketItem));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}


}