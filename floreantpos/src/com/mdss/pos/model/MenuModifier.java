package com.mdss.pos.model;

import com.mdss.pos.model.base.BaseMenuModifier;

public class MenuModifier extends BaseMenuModifier {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuModifier () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuModifier (java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/
	
	private transient MenuItemModifierGroup menuItemModifierGroup;
	
	public MenuItemModifierGroup getMenuItemModifierGroup() {
		return menuItemModifierGroup;
	}
	
	public void setMenuItemModifierGroup(MenuItemModifierGroup menuItemModifierGroup) {
		this.menuItemModifierGroup = menuItemModifierGroup;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}