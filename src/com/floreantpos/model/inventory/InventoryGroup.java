package com.floreantpos.model.inventory;

import com.floreantpos.model.inventory.base.BaseInventoryGroup;



public class InventoryGroup extends BaseInventoryGroup {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public InventoryGroup () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public InventoryGroup (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public InventoryGroup (
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