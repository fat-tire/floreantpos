package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.base.BaseVoidTransaction;



public class VoidTransaction extends BaseVoidTransaction {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public VoidTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public VoidTransaction (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public VoidTransaction (
		java.lang.Integer id,
		java.lang.String transactionType,
		java.lang.String paymentType) {

		super (
			id,
			transactionType,
			paymentType);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String getTransactionType() {
		String type = super.getTransactionType();
		
		if(StringUtils.isEmpty(type)) {
			return TransactionType.DEBIT.name();
		}
		
		return type;
	}

}