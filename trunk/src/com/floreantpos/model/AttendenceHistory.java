package com.floreantpos.model;

import com.floreantpos.model.base.BaseAttendenceHistory;

public class AttendenceHistory extends BaseAttendenceHistory {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public AttendenceHistory () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public AttendenceHistory (java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/

//	public Boolean isClockedOut() {
//		if (super.isClockedOut() == null) {
//			return Boolean.FALSE;
//		}
//		return super.isClockedOut();
//	}

}