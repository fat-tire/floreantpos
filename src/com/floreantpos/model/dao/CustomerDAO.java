package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.Customer;

public class CustomerDAO extends BaseCustomerDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public CustomerDAO() {
	}

	public List<Customer> findBy(String field, String value) {
		Session session = null;
		

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.like(field, "%" + value + "%"));

			return criteria.list();
			
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}

	}

}