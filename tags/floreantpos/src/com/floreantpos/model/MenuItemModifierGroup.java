package com.floreantpos.model;

import com.floreantpos.model.base.BaseMenuItemModifierGroup;



public class MenuItemModifierGroup extends BaseMenuItemModifierGroup {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuItemModifierGroup () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuItemModifierGroup (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	@Override
	public String toString() {
		if(getModifierGroup() != null) {
			return getModifierGroup().getName();
		}
		return "";
	}

}