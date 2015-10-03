package com.floreantpos.model;

import com.floreantpos.Messages;
import com.floreantpos.model.base.BaseCouponAndDiscount;



public class CouponAndDiscount extends BaseCouponAndDiscount {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CouponAndDiscount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CouponAndDiscount (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public final static int FREE_AMOUNT = 0;
	public final static int FIXED_PER_CATEGORY = 1;
	public final static int FIXED_PER_ITEM = 2;
	public final static int FIXED_PER_ORDER = 3;
	public final static int PERCENTAGE_PER_CATEGORY = 4;
	public final static int PERCENTAGE_PER_ITEM = 5;
	public final static int PERCENTAGE_PER_ORDER = 6;

	public final static String[] COUPON_TYPE_NAMES = { Messages.getString("CouponAndDiscount.0"), Messages.getString("CouponAndDiscount.1"), //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("CouponAndDiscount.2"), Messages.getString("CouponAndDiscount.3"), Messages.getString("CouponAndDiscount.4"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Messages.getString("CouponAndDiscount.5"), Messages.getString("CouponAndDiscount.6") }; //$NON-NLS-1$ //$NON-NLS-2$

	@Override
	public String toString() {
		return COUPON_TYPE_NAMES[getType()];
	}

}