package com.floreantpos.model;

import com.floreantpos.model.base.BaseCurrency;

public class Currency extends BaseCurrency {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public Currency () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Currency (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Currency (
		java.lang.Integer id,
		java.lang.String name) {

		super (
			id,
			name);
	}

	/*[CONSTRUCTOR MARKER END]*/
	
	@Override
	public String toString() {
		return getName();
	}
}