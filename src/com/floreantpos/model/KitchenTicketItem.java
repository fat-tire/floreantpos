package com.floreantpos.model;

import com.floreantpos.model.base.BaseKitchenTicketItem;

public class KitchenTicketItem extends BaseKitchenTicketItem {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public KitchenTicketItem() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public KitchenTicketItem(java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/

	/**
	 * Return the value associated with the column: COOKABLE
	 */
	public java.lang.Boolean isCookable() {
		return cookable == null ? Boolean.TRUE : cookable;
	}
}