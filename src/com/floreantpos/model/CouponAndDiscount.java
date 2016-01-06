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

	public final static int DISCOUNT_TYPE_AMOUNT = 0;
	public final static int DISCOUNT_TYPE_PERCENTAGE = 1;
	public final static int DISCOUNT_TYPE_RE_PRICE = 2;
	public final static int DISCOUNT_TYPE_ALT_PRICE = 3;
	
	public final static int QUALIFICATION_TYPE_ITEM = 0;
	public final static int QUALIFICATION_TYPE_GROUP = 1;
	public final static int QUALIFICATION_TYPE_CATEGORY = 2;
	public final static int QUALIFICATION_TYPE_ORDER = 3;

	public final static String[] COUPON_TYPE_NAMES = { "AMOUNT", "PERCENTAGE" }; //$NON-NLS-1$ //$NON-NLS-2$

	@Override
	public String toString() {
		return COUPON_TYPE_NAMES[getType()];
	}

}