package com.floreantpos.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.base.BaseUserPermission;


@XmlRootElement(name="user-permission")
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
	
	public UserPermission (java.lang.String name, boolean visibleWithoutPermission) {
		super(name);
		this.visibleWithoutPermission = visibleWithoutPermission;
	}
	
	private boolean visibleWithoutPermission = true;

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof UserPermission)) {
			return false;
		}
		
		UserPermission p = (UserPermission) obj;
		
		return this.getName().equalsIgnoreCase(p.getName());
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public final static UserPermission CREATE_TICKET = new UserPermission("Create New Ticket");
	public final static UserPermission VIEW_ALL_OPEN_TICKETS = new UserPermission("View All Open Ticket");
	public final static UserPermission EDIT_TICKET = new UserPermission("Edit Ticket");
	public final static UserPermission VOID_TICKET = new UserPermission("Void Ticket");
	public final static UserPermission PERFORM_ADMINISTRATIVE_TASK = new UserPermission("Perform Administrative Task");
	public final static UserPermission PERFORM_MANAGER_TASK = new UserPermission("Perform Manager Task");
	public final static UserPermission VIEW_BACK_OFFICE = new UserPermission("View Back Office", false);
	public final static UserPermission AUTHORIZE_TICKETS = new UserPermission("Authorize Tickets");
	public final static UserPermission DRAWER_ASSIGNMENT = new UserPermission("Drawer Assignment");
	public final static UserPermission DRAWER_PULL = new UserPermission("Drawer Pull");
	public final static UserPermission SPLIT_TICKET = new UserPermission("Split Ticket");
	public final static UserPermission SETTLE_TICKET = new UserPermission("Settle Ticket");
	public final static UserPermission REOPEN_TICKET = new UserPermission("Reopen Ticket");
	public final static UserPermission PAY_OUT = new UserPermission("Pay Out");
	public final static UserPermission TAKE_OUT = new UserPermission("Take Out");
	public final static UserPermission SHUT_DOWN = new UserPermission("Shut Down");
	public final static UserPermission ADD_DISCOUNT = new UserPermission("Add Discount");
	public final static UserPermission REFUND = new UserPermission("Refund");
	public final static UserPermission VIEW_EXPLORERS = new UserPermission("View Explorers");
	public final static UserPermission VIEW_REPORTS = new UserPermission("View Reports");
	public final static UserPermission MANAGE_TABLE_LAYOUT = new UserPermission("Manage Table Layout");
	
	//public final static UserPermission VIEW_USER_LIST = new UserPermission("View User List");
	//public final static UserPermission GRATUITY_ADMINISTRATION = new UserPermission("Gratuity Administration");
	//public final static UserPermission VIEW_DRAWER_PULL_REPORT = new UserPermission("View Drawer Pull Report");
	
	
	public final static UserPermission[] permissions = new UserPermission[] {VIEW_ALL_OPEN_TICKETS,CREATE_TICKET,EDIT_TICKET, VOID_TICKET,
			VIEW_BACK_OFFICE, AUTHORIZE_TICKETS, SPLIT_TICKET, SETTLE_TICKET, REOPEN_TICKET, PAY_OUT, DRAWER_ASSIGNMENT, DRAWER_PULL,
			TAKE_OUT, VIEW_EXPLORERS, VIEW_REPORTS, SHUT_DOWN, ADD_DISCOUNT, REFUND, PERFORM_MANAGER_TASK, PERFORM_ADMINISTRATIVE_TASK, MANAGE_TABLE_LAYOUT};

	public boolean isVisibleWithoutPermission() {
		return visibleWithoutPermission;
	}

	public void setVisibleWithoutPermission(boolean visibleWithoutPermission) {
		this.visibleWithoutPermission = visibleWithoutPermission;
	}
}