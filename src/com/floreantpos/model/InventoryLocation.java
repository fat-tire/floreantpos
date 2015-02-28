package com.floreantpos.model;

import com.floreantpos.model.base.BaseInventoryLocation;



public class InventoryLocation extends BaseInventoryLocation {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public InventoryLocation () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public InventoryLocation (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public InventoryLocation (
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