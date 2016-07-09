package com.floreantpos.model;

import com.floreantpos.model.base.BaseMenuItemSize;



public class MenuItemSize extends BaseMenuItemSize {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuItemSize () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuItemSize (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		return getName();
	}
}