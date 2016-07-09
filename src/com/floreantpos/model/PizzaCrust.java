package com.floreantpos.model;

import com.floreantpos.model.base.BasePizzaCrust;



public class PizzaCrust extends BasePizzaCrust {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public PizzaCrust () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public PizzaCrust (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		return getName();
	}
}