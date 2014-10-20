package com.floreantpos.model;

import com.floreantpos.model.base.BaseRefundTransaction;



public class RefundTransaction extends BaseRefundTransaction {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public RefundTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public RefundTransaction (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public RefundTransaction (
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