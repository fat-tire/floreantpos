package com.floreantpos.model;

import com.floreantpos.model.base.BaseInventoryVendor;



public class InventoryVendor extends BaseInventoryVendor {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public InventoryVendor () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public InventoryVendor (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public InventoryVendor (
		java.lang.Integer id,
		java.lang.String name) {

		super (
			id,
			name);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public Boolean isVisible() {
		return visible == null ? true : visible;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}