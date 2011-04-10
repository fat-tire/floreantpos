package com.floreantpos.model;

import com.floreantpos.model.base.BaseVoidReason;



public class VoidReason extends BaseVoidReason {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public VoidReason () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public VoidReason (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		return getReasonText();
	}
}