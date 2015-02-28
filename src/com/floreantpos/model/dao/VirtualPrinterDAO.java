package com.floreantpos.model.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.VirtualPrinter;


public class VirtualPrinterDAO extends BaseVirtualPrinterDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public VirtualPrinterDAO () {}

	public VirtualPrinter findPrinterByName(String name) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(getReferenceClass());
		criteria.add(Restrictions.eq(VirtualPrinter.PROP_NAME, name));
		
		return (VirtualPrinter) criteria.uniqueResult();
	}

}