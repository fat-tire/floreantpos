package com.mdss.pos.model;

import com.mdss.pos.model.base.BaseMenuCategory;

public class MenuCategory extends BaseMenuCategory {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuCategory () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuCategory (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public MenuCategory (
		java.lang.Integer id,
		java.lang.String name) {

		super (
			id,
			name);
	}

	/*[CONSTRUCTOR MARKER END]*/
	
	@Override
	public String toString() {
		return getName();
	}
}