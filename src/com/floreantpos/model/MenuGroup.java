package com.floreantpos.model;

import com.floreantpos.model.base.BaseMenuGroup;

public class MenuGroup extends BaseMenuGroup {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuGroup () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuGroup (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public MenuGroup (
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