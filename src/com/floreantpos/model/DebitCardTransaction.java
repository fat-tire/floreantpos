package com.floreantpos.model;

import com.floreantpos.model.base.BaseDebitCardTransaction;



public class DebitCardTransaction extends BaseDebitCardTransaction {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public DebitCardTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public DebitCardTransaction (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public DebitCardTransaction (
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