package com.floreantpos.model;

import com.floreantpos.model.base.BaseMenuModifierGroup;

public class MenuModifierGroup extends BaseMenuModifierGroup {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuModifierGroup () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuModifierGroup (java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/
	
	@Override
	public String toString() {
		return getName();
	}
}