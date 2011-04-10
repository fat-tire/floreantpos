package com.floreantpos.model;

import com.floreantpos.model.base.BaseActionHistory;



public class ActionHistory extends BaseActionHistory {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ActionHistory () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ActionHistory (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public final static String NEW_CHECK = "NEW CHECK";//added
	public final static String EDIT_CHECK = "EDIT CHECK";//added
	public final static String SPLIT_CHECK = "SPLIT CHECK";//added
	public final static String VOID_CHECK = "VOID CHECK";//added
	public final static String REOPEN_CHECK = "REOPEN CHECK";//added
	public final static String SETTLE_CHECK = "SETTLE CHECK";//added
	public final static String PRINT_CHECK = "PRINT RECRIPT";//added
	public final static String PAY_CHECK = "PAY CHECK";
	public final static String GROUP_SETTLE = "GROUP SETTLE";//added
	public final static String PAY_OUT = "PAY OUT";//added
	public final static String PAY_TIPS = "PAY TIPS";//added
}