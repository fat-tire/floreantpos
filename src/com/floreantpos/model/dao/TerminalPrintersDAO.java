package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.main.Application;
import com.floreantpos.model.TerminalPrinters;
import com.floreantpos.model.VirtualPrinter;


public class TerminalPrintersDAO extends BaseTerminalPrintersDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public TerminalPrintersDAO () {}
	
	public List<TerminalPrinters> findTerminalPrinters() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(TerminalPrinters.PROP_TERMINAL, Application.getInstance().getTerminal()));

			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	
	public TerminalPrinters findPrinters(VirtualPrinter virtualPrinter) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.and(Restrictions.eq(TerminalPrinters.PROP_TERMINAL, Application.getInstance().getTerminal()), Restrictions.eq(TerminalPrinters.PROP_VIRTUAL_PRINTER,virtualPrinter)));
			return (TerminalPrinters) criteria.uniqueResult();
		} finally {
			closeSession(session);
		}
	}
}