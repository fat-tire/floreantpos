package com.floreantpos.model;

import com.floreantpos.model.base.BaseCreditCardTransaction;



public class CreditCardTransaction extends BaseCreditCardTransaction {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CreditCardTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CreditCardTransaction (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CreditCardTransaction (
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