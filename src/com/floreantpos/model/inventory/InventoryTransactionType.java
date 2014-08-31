package com.floreantpos.model.inventory;

import com.floreantpos.model.inventory.base.BaseInventoryTransactionType;



public class InventoryTransactionType extends BaseInventoryTransactionType {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public InventoryTransactionType () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public InventoryTransactionType (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public InOutEnum getInOutEnum() {
		return InOutEnum.fromInt(getInOrOut());
	}
	
	public void setInOrOut(InOutEnum e) {
		super.setInOrOut(e.getType());
	}
}