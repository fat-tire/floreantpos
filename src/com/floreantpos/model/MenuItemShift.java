package com.floreantpos.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.base.BaseMenuItemShift;

@XmlRootElement(name="menu-item-shift")
public class MenuItemShift extends BaseMenuItemShift {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuItemShift () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuItemShift (java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/
}