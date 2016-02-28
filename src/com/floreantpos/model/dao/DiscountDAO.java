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
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.Discount;
import com.floreantpos.model.MenuItem;

public class DiscountDAO extends BaseDiscountDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public DiscountDAO() {
	}

	public List<Discount> findAllValidCoupons() {
		Session session = null;

		Date currentDate = new Date();

		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Discount.PROP_ENABLED, Boolean.TRUE));
			criteria.add(Restrictions.or(Restrictions.eq(Discount.PROP_NEVER_EXPIRE, Boolean.TRUE), Restrictions.ge(Discount.PROP_EXPIRY_DATE, currentDate)));
			return criteria.list();
		} finally {
			closeSession(session);
		}

	}
	public List<Discount> getValidCoupons() {
		Session session = null;

		Date currentDate = new Date();

		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Discount.PROP_ENABLED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Discount.PROP_QUALIFICATION_TYPE, Discount.QUALIFICATION_TYPE_ITEM));
			criteria.add(Restrictions.or(Restrictions.eq(Discount.PROP_NEVER_EXPIRE, Boolean.TRUE), Restrictions.ge(Discount.PROP_EXPIRY_DATE, currentDate)));
			return criteria.list();
		} finally {
			closeSession(session);
		}

	}

	public List<Discount> getValidCoupon(MenuItem menuItem) {
		List<Discount> discountList = new ArrayList<Discount>();
		for (Discount discount : getValidCoupons()) {
			if (discount.getMenuItems().contains(menuItem) || discount.isApplyToAll()) {
				discountList.add(discount);
			}
		}
		return discountList;
	}

	public List<Discount> getTicketValidCoupon() {
		Session session = null;

		Date currentDate = new Date();

		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Discount.PROP_ENABLED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Discount.PROP_QUALIFICATION_TYPE, Discount.QUALIFICATION_TYPE_ORDER));
			criteria.add(Restrictions.or(Restrictions.eq(Discount.PROP_NEVER_EXPIRE, Boolean.TRUE), Restrictions.ge(Discount.PROP_EXPIRY_DATE, currentDate)));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public Discount getDiscountByBarcode(String barcode) {
		Session session = null;
		Criteria criteria = null;
		try {
			session = createNewSession();
			criteria = session.createCriteria(Discount.class);
			criteria.add(Restrictions.like(Discount.PROP_BARCODE, barcode));
			List<Discount> result = criteria.list();
			if (result == null || result.isEmpty()) {
				return null;
			}
			return (Discount) result.get(0);
		} finally {
			closeSession(session);
		}
	}
}