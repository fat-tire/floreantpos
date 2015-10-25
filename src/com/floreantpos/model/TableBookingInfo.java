package com.floreantpos.model;

import com.floreantpos.model.base.BaseTableBookingInfo;



public class TableBookingInfo extends BaseTableBookingInfo {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public TableBookingInfo () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TableBookingInfo (java.lang.Integer id) {
		super(id);
	}
	


/*[CONSTRUCTOR MARKER END]*/
	public String toString () {
		return getId().toString();
	}


}