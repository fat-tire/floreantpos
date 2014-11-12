package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.KitchenTicket.KitchenTicketStatus;

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
}