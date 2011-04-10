package com.floreantpos.model;

import com.floreantpos.model.base.BaseUserPermission;



public class UserPermission extends BaseUserPermission {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public UserPermission () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public UserPermission (java.lang.String name) {
		super(name);
	}

/*[CONSTRUCTOR MARKER END]*/

	/*public UserPermission(String name) {
		setName(name);
	}*/
	
	@Override
	public String toString() {
		return getName();
	}
	
	public final static UserPermission CREATE_NEW_TICKET = new UserPermission("Create New Ticket");
	public final static UserPermission VIEW_ALL_OPEN_TICKET = new UserPermission("View All Open Ticket");
	public final static UserPermission EDIT_TICKET = new UserPermission("Edit Ticket");
	public final static UserPermission VOID_TICKET = new UserPermission("Void Ticket");
	public final static UserPermission PERFORM_ADMINISTRATIVE_TASK = new UserPermission("Perform Administrative Task");
	public final static UserPermission PERFORM_MANAGER_TASK = new UserPermission("Perform Manager Task");
	public final static UserPermission VIEW_BACK_OFFICE = new UserPermission("View Back Office");
	public final static UserPermission SPLIT_TICKET = new UserPermission("Split Ticket");
	public final static UserPermission SETTLE_TICKET = new UserPermission("Settle Ticket");
	public final static UserPermission REOPEN_TICKET = new UserPermission("Reopen Ticket");
	public final static UserPermission PAY_OUT = new UserPermission("Pay Out");
	public final static UserPermission TAKE_OUT = new UserPermission("Take Out");
	//public final static UserPermission VIEW_USER_LIST = new UserPermission("View User List");
	//public final static UserPermission GRATUITY_ADMINISTRATION = new UserPermission("Gratuity Administration");
	//public final static UserPermission VIEW_DRAWER_PULL_REPORT = new UserPermission("View Drawer Pull Report");
	public final static UserPermission VIEW_EXPLORERS = new UserPermission("View Explorers");
	public final static UserPermission VIEW_REPORTS = new UserPermission("View Reports");
	
	public final static UserPermission[] permissions = new UserPermission[] {VIEW_ALL_OPEN_TICKET,CREATE_NEW_TICKET,EDIT_TICKET, VOID_TICKET, PERFORM_MANAGER_TASK,
			VIEW_BACK_OFFICE, SPLIT_TICKET, SETTLE_TICKET, REOPEN_TICKET, PAY_OUT,
			TAKE_OUT, PERFORM_ADMINISTRATIVE_TASK, VIEW_EXPLORERS, VIEW_REPORTS};
}