package com.floreantpos.model;

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

	public final static String[] COUPON_TYPE_NAMES = { "Free Amount", "Fixed Per Category",
			"Fixed Per Item", "Fixed Per Order", "Percentage Per Category",
			"Percentage Per Item", "Percentage Per Order" };

	@Override
	public String toString() {
		return COUPON_TYPE_NAMES[getType()];
	}

}