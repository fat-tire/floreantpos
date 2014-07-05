package com.floreantpos.model;

import com.floreantpos.model.base.BaseCustomer;



public class Customer extends BaseCustomer {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Customer () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Customer (java.lang.Integer autoId) {
		super(autoId);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	@Override
	public String toString() {
		String name = getName();
		return name;
	}
}