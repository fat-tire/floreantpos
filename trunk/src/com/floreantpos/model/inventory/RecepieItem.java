package com.floreantpos.model.inventory;

import com.floreantpos.model.inventory.base.BaseRecepieItem;



public class RecepieItem extends BaseRecepieItem {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public RecepieItem () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public RecepieItem (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public RecepieItem (
		java.lang.Integer id,
		com.floreantpos.model.inventory.Recepie recepie) {

		super (
			id,
			recepie);
	}

/*[CONSTRUCTOR MARKER END]*/


}