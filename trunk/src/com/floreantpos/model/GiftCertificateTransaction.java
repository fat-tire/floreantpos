package com.floreantpos.model;

import com.floreantpos.model.base.BaseGiftCertificateTransaction;



public class GiftCertificateTransaction extends BaseGiftCertificateTransaction {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public GiftCertificateTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public GiftCertificateTransaction (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public GiftCertificateTransaction (
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