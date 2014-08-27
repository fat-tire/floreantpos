package com.floreantpos.model.inventory;

import com.floreantpos.model.inventory.base.BaseInventoryWarehouse;



public class InventoryWarehouse extends BaseInventoryWarehouse {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public InventoryWarehouse () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public InventoryWarehouse (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public InventoryWarehouse (
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