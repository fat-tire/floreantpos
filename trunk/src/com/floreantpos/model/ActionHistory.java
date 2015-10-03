package com.floreantpos.model;

import com.floreantpos.Messages;
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

	public final static String NEW_CHECK = Messages.getString("ActionHistory.0");//added //$NON-NLS-1$
	public final static String EDIT_CHECK = Messages.getString("ActionHistory.1");//added //$NON-NLS-1$
	public final static String SPLIT_CHECK = Messages.getString("ActionHistory.2");//added //$NON-NLS-1$
	public final static String VOID_CHECK = Messages.getString("ActionHistory.3");//added //$NON-NLS-1$
	public final static String REOPEN_CHECK = Messages.getString("ActionHistory.4");//added //$NON-NLS-1$
	public final static String SETTLE_CHECK = Messages.getString("ActionHistory.5");//added //$NON-NLS-1$
	public final static String PRINT_CHECK = Messages.getString("ActionHistory.6");//added //$NON-NLS-1$
	public final static String PAY_CHECK = Messages.getString("ActionHistory.7"); //$NON-NLS-1$
	public final static String GROUP_SETTLE = Messages.getString("ActionHistory.8");//added //$NON-NLS-1$
	public final static String PAY_OUT = Messages.getString("ActionHistory.9");//added //$NON-NLS-1$
	public final static String PAY_TIPS = Messages.getString("ActionHistory.10");//added //$NON-NLS-1$
}