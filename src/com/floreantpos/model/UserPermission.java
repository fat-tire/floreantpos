/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.Messages;
import com.floreantpos.model.base.BaseUserPermission;

@XmlRootElement(name = "user-permission")
public class UserPermission extends BaseUserPermission {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public UserPermission() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public UserPermission(java.lang.String name) {
		super(name);
	}

	/*[CONSTRUCTOR MARKER END]*/

	public UserPermission(java.lang.String name, boolean visibleWithoutPermission) {
		super(name);
		this.visibleWithoutPermission = visibleWithoutPermission;
	}

	private boolean visibleWithoutPermission = true;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UserPermission)) {
			return false;
		}

		UserPermission p = (UserPermission) obj;

		return this.getName().equalsIgnoreCase(p.getName());
	}

	@Override
	public String toString() {
		return getName();
	}

	public final static UserPermission CREATE_TICKET = new UserPermission(Messages.getString("UserPermission.0")); //$NON-NLS-1$
	public final static UserPermission VIEW_ALL_OPEN_TICKETS = new UserPermission(Messages.getString("UserPermission.1")); //$NON-NLS-1$
	public final static UserPermission VOID_TICKET = new UserPermission(Messages.getString("UserPermission.3")); //$NON-NLS-1$
	public final static UserPermission PERFORM_ADMINISTRATIVE_TASK = new UserPermission(Messages.getString("UserPermission.4")); //$NON-NLS-1$
	public final static UserPermission PERFORM_MANAGER_TASK = new UserPermission(Messages.getString("UserPermission.5")); //$NON-NLS-1$
	public final static UserPermission VIEW_BACK_OFFICE = new UserPermission(Messages.getString("UserPermission.6"), false); //$NON-NLS-1$
	public final static UserPermission AUTHORIZE_TICKETS = new UserPermission(Messages.getString("UserPermission.7")); //$NON-NLS-1$
	public final static UserPermission DRAWER_ASSIGNMENT = new UserPermission(Messages.getString("UserPermission.8")); //$NON-NLS-1$
	public final static UserPermission DRAWER_PULL = new UserPermission(Messages.getString("UserPermission.9")); //$NON-NLS-1$
	public final static UserPermission SPLIT_TICKET = new UserPermission(Messages.getString("UserPermission.10")); //$NON-NLS-1$
	public final static UserPermission SETTLE_TICKET = new UserPermission(Messages.getString("UserPermission.11")); //$NON-NLS-1$
	public final static UserPermission REOPEN_TICKET = new UserPermission(Messages.getString("UserPermission.12")); //$NON-NLS-1$
	public final static UserPermission PAY_OUT = new UserPermission(Messages.getString("UserPermission.13")); //$NON-NLS-1$
	public final static UserPermission SHUT_DOWN = new UserPermission(Messages.getString("UserPermission.15")); //$NON-NLS-1$
	public final static UserPermission ADD_DISCOUNT = new UserPermission(Messages.getString("UserPermission.16")); //$NON-NLS-1$
	public final static UserPermission REFUND = new UserPermission(Messages.getString("UserPermission.17")); //$NON-NLS-1$
	public final static UserPermission VIEW_EXPLORERS = new UserPermission(Messages.getString("UserPermission.18")); //$NON-NLS-1$
	public final static UserPermission VIEW_REPORTS = new UserPermission(Messages.getString("UserPermission.19")); //$NON-NLS-1$
	public final static UserPermission MANAGE_TABLE_LAYOUT = new UserPermission(Messages.getString("UserPermission.20")); //$NON-NLS-1$
	public final static UserPermission TABLE_BOOKING = new UserPermission(Messages.getString("UserPermission.22")); //$NON-NLS-1$
	public final static UserPermission MODIFY_PRINTED_TICKET = new UserPermission(Messages.getString("UserPermission.21")); //$NON-NLS-1$
	public final static UserPermission TRANSFER_TICKET = new UserPermission("Transfer Ticket");
	public final static UserPermission KITCHEN_DISPLAY = new UserPermission("Kitchen Display");
	public final static UserPermission ALL_FUNCTIONS = new UserPermission("All Functions");
	public final static UserPermission HOLD_TICKET = new UserPermission("Hold Ticket");
	public final static UserPermission VIEW_ALL_CLOSE_TICKETS = new UserPermission("View All Close Tickets");
	
	//public final static UserPermission VIEW_USER_LIST = new UserPermission("View User List");
	//public final static UserPermission GRATUITY_ADMINISTRATION = new UserPermission("Gratuity Administration");
	//public final static UserPermission VIEW_DRAWER_PULL_REPORT = new UserPermission("View Drawer Pull Report");

	public final static UserPermission[] permissions = new UserPermission[] { VIEW_ALL_OPEN_TICKETS, CREATE_TICKET, VOID_TICKET, VIEW_BACK_OFFICE,
			AUTHORIZE_TICKETS, SPLIT_TICKET, SETTLE_TICKET, REOPEN_TICKET, PAY_OUT, DRAWER_ASSIGNMENT, DRAWER_PULL, VIEW_EXPLORERS, VIEW_REPORTS,
			SHUT_DOWN, ADD_DISCOUNT, REFUND, PERFORM_MANAGER_TASK, PERFORM_ADMINISTRATIVE_TASK, MANAGE_TABLE_LAYOUT, TABLE_BOOKING, MODIFY_PRINTED_TICKET,
			TRANSFER_TICKET, KITCHEN_DISPLAY, ALL_FUNCTIONS,HOLD_TICKET,VIEW_ALL_CLOSE_TICKETS};

	public boolean isVisibleWithoutPermission() {
		return visibleWithoutPermission;
	}

	public void setVisibleWithoutPermission(boolean visibleWithoutPermission) {
		this.visibleWithoutPermission = visibleWithoutPermission;
	}
}