package com.floreantpos.model;

import com.floreantpos.model.base.BaseVirtualPrinter;



public class VirtualPrinter extends BaseVirtualPrinter {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public VirtualPrinter () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public VirtualPrinter (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public VirtualPrinter (
		java.lang.Integer id,
		java.lang.String name) {

		super (
			id,
			name);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof VirtualPrinter)) {
			return false;
		}
		
		VirtualPrinter other = (VirtualPrinter) obj;
		
		return this.name.equalsIgnoreCase(other.name);
	}

	@Override
	public String toString() {
		return getName();
	}
}