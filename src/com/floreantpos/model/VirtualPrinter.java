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
	public VirtualPrinter (java.lang.Integer autoId) {
		super(autoId);
	}

	/**
	 * Constructor for required fields
	 */
	public VirtualPrinter (
		java.lang.Integer autoId,
		java.lang.String name) {

		super (
			autoId,
			name);
	}

/*[CONSTRUCTOR MARKER END]*/


}