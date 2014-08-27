package com.floreantpos.model.inventory;

import com.floreantpos.model.inventory.base.BaseInventoryItem;



public class InventoryItem extends BaseInventoryItem {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public InventoryItem () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public InventoryItem (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public InventoryItem (
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

}