package com.floreantpos.model;

import com.floreantpos.model.base.BaseCashDropTransaction;



public class CashDropTransaction extends BaseCashDropTransaction {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CashDropTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CashDropTransaction (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CashDropTransaction (
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