package com.floreantpos.model.inventory;

import com.floreantpos.model.inventory.base.BaseInventoryLocation;



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
	public String toString() {
		return getName();
	}

}