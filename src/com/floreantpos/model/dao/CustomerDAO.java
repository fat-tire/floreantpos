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

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.Customer;

public class CustomerDAO extends BaseCustomerDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public CustomerDAO() {
	}

	@Override
	public Order getDefaultOrder() {
		return Order.asc(Customer.PROP_AUTO_ID);
	}

	public List<Customer> findBy(String mobile, String loyalty, String name) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			Disjunction disjunction = Restrictions.disjunction();

			if (StringUtils.isNotEmpty(mobile))
				disjunction.add(Restrictions.like(Customer.PROP_MOBILE_NO, "%" + mobile + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			if (StringUtils.isNotEmpty(loyalty))
				disjunction.add(Restrictions.like(Customer.PROP_LOYALTY_NO, "%" + loyalty + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			if (StringUtils.isNotEmpty(name))
				disjunction.add(Restrictions.like(Customer.PROP_FIRST_NAME, "%" + name + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			criteria.add(disjunction);

			return criteria.list();

		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public List<Customer> findBy(String searchString) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			Disjunction disjunction = Restrictions.disjunction();

			if (StringUtils.isNotEmpty(searchString))
				disjunction.add(Restrictions.like(Customer.PROP_MOBILE_NO, "%" + searchString + "%"));
			disjunction.add(Restrictions.like(Customer.PROP_LOYALTY_NO, "%" + searchString + "%"));
			disjunction.add(Restrictions.like(Customer.PROP_FIRST_NAME, "%" + searchString + "%"));

			criteria.add(disjunction);
			return criteria.list();
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}

	}

	public List<Customer> findByMobileNumber(String mobileNo) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());

			if (StringUtils.isNotEmpty(mobileNo))
				criteria.add(Restrictions.eq(Customer.PROP_MOBILE_NO, mobileNo));
			return criteria.list();
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public List<Customer> findByName(String name) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());

			if (StringUtils.isNotEmpty(name))
				criteria.add(Restrictions.ilike(Customer.PROP_FIRST_NAME, name + "%".trim(), MatchMode.ANYWHERE)); //$NON-NLS-1$ //$NON-NLS-2$

			return criteria.list();

		} finally {
			if (session != null) {
				closeSession(session);
			}
		}

	}

	public Customer findById(int customerId) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());

			criteria.add(Restrictions.eq(Customer.PROP_AUTO_ID, customerId));

			return (Customer) criteria.uniqueResult();

		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}
}