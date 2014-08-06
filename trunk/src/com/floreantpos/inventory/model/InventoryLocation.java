package com.floreantpos.inventory.model;

import com.floreantpos.inventory.model.base.BaseInventoryLocation;



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


}