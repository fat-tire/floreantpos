package com.floreantpos.model;

import com.floreantpos.model.base.BaseShift;

public class Shift extends BaseShift {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public Shift () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Shift (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Shift (
		java.lang.Integer id,
		java.lang.String name) {

		super (
			id,
			name);
	}

	/*[CONSTRUCTOR MARKER END]*/

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Shift)) return false;
		
		return getName().equalsIgnoreCase( ((Shift) obj).getName());
	}
	
	@Override
	public String toString() {
		return getName();
	}
}