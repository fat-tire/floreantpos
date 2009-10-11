package com.mdss.pos.model;

import com.mdss.pos.model.base.BasePayoutReason;



public class PayoutReason extends BasePayoutReason {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public PayoutReason () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public PayoutReason (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		return getReason();
	}
}