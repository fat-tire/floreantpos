package com.floreantpos.model.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.CouponAndDiscount;


public class CouponAndDiscountDAO extends BaseCouponAndDiscountDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public CouponAndDiscountDAO () {}

	public List<CouponAndDiscount> getValidCoupons() {
        Session session = null;

        Date currentDate = new Date();
        
        try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(CouponAndDiscount.PROP_DISABLED, Boolean.FALSE));
			criteria.add(Restrictions.or(Restrictions.eq(CouponAndDiscount.PROP_NEVER_EXPIRE, Boolean.TRUE), Restrictions.ge(CouponAndDiscount.PROP_EXPIRY_DATE, currentDate)));
			return criteria.list();
		} finally {
			closeSession(session);
		}

    }


}