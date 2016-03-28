package com.floreantpos.model;

import com.floreantpos.model.base.BaseCustomPaymentTransaction;

public class CustomPaymentTransaction extends BaseCustomPaymentTransaction {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public CustomPaymentTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CustomPaymentTransaction (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CustomPaymentTransaction (
		java.lang.Integer id,
		java.lang.String transactionType,
		java.lang.String paymentType) {

		super (
			id,
			transactionType,
			paymentType);
	}

	/*[CONSTRUCTOR MARKER END]*/

}