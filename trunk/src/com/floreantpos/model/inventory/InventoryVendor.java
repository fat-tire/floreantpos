package com.floreantpos.model.inventory;

import com.floreantpos.model.inventory.base.BaseInventoryVendor;



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


}