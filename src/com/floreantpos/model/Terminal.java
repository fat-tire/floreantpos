package com.floreantpos.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.base.BaseTerminal;

@XmlRootElement(name = "terminal")
public class Terminal extends BaseTerminal {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public Terminal () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Terminal (java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/

	/**
	 * Return the value associated with the column: HAS_CASH_DRAWER
	 */
	@Override
	public java.lang.Boolean isHasCashDrawer() {
		return hasCashDrawer == null ? Boolean.TRUE : hasCashDrawer;
	}
	
	public boolean isCashDrawerAssigned() {
		return getAssignedUser() != null;
	}

	@Override
	public String toString() {
		return getName();
	}
}