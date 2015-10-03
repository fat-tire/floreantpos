package com.floreantpos.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.base.BaseMenuModifierGroup;

@XmlRootElement(name="menu-modifier-group")
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
}