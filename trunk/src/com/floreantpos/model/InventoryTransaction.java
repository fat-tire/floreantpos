package com.floreantpos.model;

import com.floreantpos.model.base.BaseInventoryTransaction;



public class InventoryTransaction extends BaseInventoryTransaction {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public InventoryTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public InventoryTransaction (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public static String PROP_TYPE = "type"; //$NON-NLS-1$
	
	protected InventoryTransactionType type;
	
	public InventoryTransactionType getType() {
		return type;
	}
	
	public void setType(InventoryTransactionType type) {
		this.type = type;
	}

}