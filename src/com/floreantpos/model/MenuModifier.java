package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.base.BaseMenuModifier;

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
	public Integer getSortOrder() {
		return sortOrder == null ? Integer.MAX_VALUE : sortOrder;
	}
	
	@Override
	public Integer getButtonColor() {
		return buttonColor;
	}
	
	@Override
	public Integer getTextColor() {
		return textColor;
	}
	
	public String getDisplayName() {
		if(TerminalConfig.isUseTranslatedName() && StringUtils.isNotEmpty(getTranslatedName())) {
			return getTranslatedName();
		}
		
		return super.getName();
	}
	
	@Override
	public String toString() {
		return getName();
	}

	public String getUniqueId() {
		return ("menu_modifier_" + getName() + "_" + getId()).replaceAll("\\s+", "_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}