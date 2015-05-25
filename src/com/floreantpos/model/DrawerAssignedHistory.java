package com.floreantpos.model;

import com.floreantpos.model.base.BaseDrawerAssignedHistory;



public class DrawerAssignedHistory extends BaseDrawerAssignedHistory {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public DrawerAssignedHistory () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public DrawerAssignedHistory (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public final static String ASSIGNMENT_OPERATION = "ASSIGN";
	public final static String CLOSE_OPERATION = "CLOSE";
}