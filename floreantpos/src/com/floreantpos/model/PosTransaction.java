package com.floreantpos.model;

import com.floreantpos.model.base.BasePosTransaction;

public abstract class PosTransaction extends BasePosTransaction {
	private static final long serialVersionUID = 1L;
	
	public final static String TYPE_CASH = "CASH";
	public final static String TYPE_CREDIT_CARD = "CREDIT_CARD";
	public final static String TYPE_DEBIT_CARD = "DEBIT_CARD";
	public final static String TYPE_GIFT_CERT = "GIFT_CERT";
	public final static String TYPE_PAYOUT = "PAY_OUT";
	public final static String tYPE_REFUND = "REFUND";

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public PosTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public PosTransaction (java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/
}