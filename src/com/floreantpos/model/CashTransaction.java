package com.floreantpos.model;

import com.floreantpos.model.base.BaseCashTransaction;



public class CashTransaction extends BaseCashTransaction {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CashTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CashTransaction (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

}