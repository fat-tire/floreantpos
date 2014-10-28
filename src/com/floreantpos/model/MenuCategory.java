package com.floreantpos.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.base.BaseMenuCategory;

@XmlRootElement(name="menu-category")
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

	public String getUniqueId() {
		return ("menu_category_" + getName() + "_" + getId()).replaceAll("\\s+", "_");
	}
}