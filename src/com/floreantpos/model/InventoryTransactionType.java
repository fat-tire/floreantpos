package com.floreantpos.model;

import com.floreantpos.model.base.BaseInventoryTransactionType;



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
	
	public void setInOrOutEnum(InOutEnum e) {
		super.setInOrOut(e.getType());
	}
	
	@Override
	public String toString() {
		return name;
	}
}