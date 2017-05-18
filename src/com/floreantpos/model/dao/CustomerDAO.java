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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.Customer;
import com.floreantpos.swing.PaginatedTableModel;

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

	public int getNumberOfCustomers() {
		Session session = null;
		Criteria criteria = null;
		try {
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
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

	public int getNumberOfCustomers(String searchString) {
		Session session = null;
		Criteria criteria = null;
		try {
			if (StringUtils.isEmpty(searchString)) {
				return 0;
			}
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.ilike(Customer.PROP_MOBILE_NO, "%" + searchString + "%"));
			disjunction.add(Restrictions.ilike(Customer.PROP_NAME, "%" + searchString + "%"));

			criteria.add(disjunction);

			List list = criteria.list();
			if (list != null) {
				return list.size();
			}
			return 0;
		} finally {
			closeSession(session);
		}

	}

	public int getNumberOfCustomers(String mobile, String loyalty, String name) {
		Session session = null;
		Criteria criteria = null;
		try {
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			Disjunction disjunction = Restrictions.disjunction();

			if (StringUtils.isNotEmpty(mobile))
				disjunction.add(Restrictions.ilike(Customer.PROP_MOBILE_NO, "%" + mobile + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			if (StringUtils.isNotEmpty(loyalty))
				disjunction.add(Restrictions.ilike(Customer.PROP_LOYALTY_NO, "%" + loyalty + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			if (StringUtils.isNotEmpty(name))
				disjunction.add(Restrictions.ilike(Customer.PROP_NAME, "%" + name + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			criteria.add(disjunction);

			List list = criteria.list();
			if (list != null) {
				return list.size();
			}
			return 0;
		} finally {
			closeSession(session);
		}

	}

	public void findBy(String mobile, String loyalty, String name, PaginatedTableModel tableModel) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			Disjunction disjunction = Restrictions.disjunction();

			if (StringUtils.isNotEmpty(mobile))
				disjunction.add(Restrictions.ilike(Customer.PROP_MOBILE_NO, "%" + mobile + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			if (StringUtils.isNotEmpty(loyalty))
				disjunction.add(Restrictions.ilike(Customer.PROP_LOYALTY_NO, "%" + loyalty + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			if (StringUtils.isNotEmpty(name))
				disjunction.add(Restrictions.ilike(Customer.PROP_NAME, "%" + name + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			criteria.add(disjunction);

			criteria.setFirstResult(tableModel.getCurrentRowIndex());
			criteria.setMaxResults(tableModel.getPageSize());
			tableModel.setRows(criteria.list());

		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public List<Customer> findBy(String mobile, String loyalty, String name) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			Disjunction disjunction = Restrictions.disjunction();

			if (StringUtils.isNotEmpty(mobile))
				disjunction.add(Restrictions.ilike(Customer.PROP_MOBILE_NO, "%" + mobile + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			if (StringUtils.isNotEmpty(loyalty))
				disjunction.add(Restrictions.ilike(Customer.PROP_LOYALTY_NO, "%" + loyalty + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			if (StringUtils.isNotEmpty(name))
				disjunction.add(Restrictions.ilike(Customer.PROP_NAME, "%" + name + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			criteria.add(disjunction);

			List list = criteria.list();
			if (list != null || list.size() != 0) {
				return list;
			}

		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
		return null;
	}

	public void findBy(String searchString, PaginatedTableModel tableModel) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			Disjunction disjunction = Restrictions.disjunction();

			if (StringUtils.isEmpty(searchString)) {
				return;
			}
			disjunction.add(Restrictions.ilike(Customer.PROP_MOBILE_NO, "%" + searchString + "%"));
			disjunction.add(Restrictions.ilike(Customer.PROP_NAME, "%" + searchString + "%")); //$NON-NLS-1$ //$NON-NLS-2$

			criteria.add(disjunction);

			criteria.setFirstResult(tableModel.getCurrentRowIndex());
			criteria.setMaxResults(tableModel.getPageSize());
			tableModel.setRows(criteria.list());

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

	public void loadCustomers(PaginatedTableModel tableModel) {
		Session session = null;
		Criteria criteria = null;

		try {
			session = createNewSession();
			criteria = session.createCriteria(getReferenceClass());
			criteria.addOrder(getDefaultOrder());
			criteria.setFirstResult(tableModel.getCurrentRowIndex());
			criteria.setMaxResults(tableModel.getPageSize());
			tableModel.setRows(criteria.list());
			return;

		} finally {
			closeSession(session);
		}
	}
}