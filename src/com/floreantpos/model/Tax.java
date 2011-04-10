package com.floreantpos.model;

import com.floreantpos.model.base.BaseTax;

public class Tax extends BaseTax {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public Tax () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Tax (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Tax (
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