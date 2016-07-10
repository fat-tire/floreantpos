package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;

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
	public String getTranslatedName() {
		String translatedName = super.getTranslatedName();
		if(StringUtils.isEmpty(translatedName)) {
			return getName();
		}
		
		return translatedName;
	}

	@Override
	public String toString() {
		return getName();
	}
}