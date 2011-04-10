package com.floreantpos.model;

import com.floreantpos.model.base.BasePayoutRecepient;



public class PayoutRecepient extends BasePayoutRecepient {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public PayoutRecepient () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public PayoutRecepient (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		return getName();
	}
}