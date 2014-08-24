package com.floreantpos.model.inventory;

import com.floreantpos.model.inventory.base.BaseInventoryGroup;
import com.floreantpos.util.POSUtil;



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
		return POSUtil.getBoolean(super.isVisible(), true);
	}

	@Override
	public String toString() {
		return getName();
	}
}