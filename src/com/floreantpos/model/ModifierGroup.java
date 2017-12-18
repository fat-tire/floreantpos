package com.floreantpos.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.base.BaseModifierGroup;


@XmlRootElement(name="modifier-group")
public class ModifierGroup extends BaseModifierGroup {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ModifierGroup () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ModifierGroup (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/
private MenuItemModifierGroup menuItemModifierGroup;
	
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
		return ("menu_modifiergroup_" + getName() + "_" + getId()).replaceAll("\\s+", "_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	public MenuItemModifierGroup getMenuItemModifierGroup() {
		return menuItemModifierGroup;
	}

	public void setMenuItemModifierGroup(MenuItemModifierGroup menuItemModifierGroup) {
		this.menuItemModifierGroup = menuItemModifierGroup;
	}

}