package com.floreantpos.model;

import com.floreantpos.model.base.BaseOrderTypeProperties;



public class OrderTypeProperties extends BaseOrderTypeProperties {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public OrderTypeProperties () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public OrderTypeProperties (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public Boolean isVisible() {
		return visible == null ? Boolean.TRUE : visible;
	}
}