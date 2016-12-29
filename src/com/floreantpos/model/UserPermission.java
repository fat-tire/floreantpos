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

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.model.base.BaseUserPermission;

@XmlRootElement(name = "user-permission")
public class UserPermission extends BaseUserPermission {
	private static final long serialVersionUID = 1L;
	private String resourceBundlePropertyName;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public UserPermission() {
		super();
	}

	public UserPermission(java.lang.String name, String resourceBundlePropertyName) {
		super(name);
		this.resourceBundlePropertyName = resourceBundlePropertyName;
	}

	/*[CONSTRUCTOR MARKER END]*/

	public UserPermission(java.lang.String name, String resourceBundlePropertyName, boolean visibleWithoutPermission) {
		super(name);
		this.resourceBundlePropertyName = resourceBundlePropertyName;
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
		if (StringUtils.isEmpty(resourceBundlePropertyName)) {
			return getName();
		}
		return Messages.getString(resourceBundlePropertyName);
	}

	public final static UserPermission CREATE_TICKET = new UserPermission("Create New Ticket", "UserPermission.0"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission VIEW_ALL_OPEN_TICKETS = new UserPermission("View All Open Ticket", "UserPermission.1"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission VOID_TICKET = new UserPermission("Void Ticket", "UserPermission.3"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission PERFORM_ADMINISTRATIVE_TASK = new UserPermission("Perform Administrative Task", "UserPermission.4"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission PERFORM_MANAGER_TASK = new UserPermission("Perform Manager Task", "UserPermission.5"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission VIEW_BACK_OFFICE = new UserPermission("View Back Office", "UserPermission.6", false); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission AUTHORIZE_TICKETS = new UserPermission("Authorize Tickets", "UserPermission.7"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission DRAWER_ASSIGNMENT = new UserPermission("Drawer Assignment", "UserPermission.8"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission DRAWER_PULL = new UserPermission("Drawer Pull", "UserPermission.9"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission SPLIT_TICKET = new UserPermission("Split Ticket", "UserPermission.10"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission SETTLE_TICKET = new UserPermission("Settle Ticket", "UserPermission.11"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission REOPEN_TICKET = new UserPermission("Reopen Ticket", "UserPermission.12"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission PAY_OUT = new UserPermission("Pay Out", "UserPermission.13"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission SHUT_DOWN = new UserPermission("Shut Down", "UserPermission.15"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission ADD_DISCOUNT = new UserPermission("Add Discount", "UserPermission.16"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission REFUND = new UserPermission("Refund", "UserPermission.17"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission VIEW_EXPLORERS = new UserPermission("View Explorers", "UserPermission.18"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission VIEW_REPORTS = new UserPermission("View Reports", "UserPermission.19"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission MANAGE_TABLE_LAYOUT = new UserPermission("Manage Table Layout", "UserPermission.20"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission TABLE_BOOKING = new UserPermission("Booking", "UserPermission.22"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission MODIFY_PRINTED_TICKET = new UserPermission("Modify Printed Ticket", "UserPermission.21"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission TRANSFER_TICKET = new UserPermission("Transfer Ticket", "UserPermission.23"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission KITCHEN_DISPLAY = new UserPermission("Kitchen Display", "UserPermission.24"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission ALL_FUNCTIONS = new UserPermission("All Functions", "UserPermission.25"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission HOLD_TICKET = new UserPermission("Hold Ticket", "UserPermission.26"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission VIEW_ALL_CLOSE_TICKETS = new UserPermission("View All Close Tickets", "UserPermission.27"); //$NON-NLS-1$ //$NON-NLS-2$
	public final static UserPermission QUICK_MAINTENANCE = new UserPermission("Quick Maintenance", "Quick_Maintenance"); //$NON-NLS-1$ //$NON-NLS-2$

	//public final static UserPermission VIEW_USER_LIST = new UserPermission("View User List");
	//public final static UserPermission GRATUITY_ADMINISTRATION = new UserPermission("Gratuity Administration");
	//public final static UserPermission VIEW_DRAWER_PULL_REPORT = new UserPermission("View Drawer Pull Report");

	public final static UserPermission[] permissions = new UserPermission[] { VIEW_ALL_OPEN_TICKETS, CREATE_TICKET, VOID_TICKET, VIEW_BACK_OFFICE,
			AUTHORIZE_TICKETS, SPLIT_TICKET, SETTLE_TICKET, REOPEN_TICKET, PAY_OUT, DRAWER_ASSIGNMENT, DRAWER_PULL, VIEW_EXPLORERS, VIEW_REPORTS, SHUT_DOWN,
			ADD_DISCOUNT, REFUND, PERFORM_MANAGER_TASK, PERFORM_ADMINISTRATIVE_TASK, MANAGE_TABLE_LAYOUT, TABLE_BOOKING, MODIFY_PRINTED_TICKET,
			TRANSFER_TICKET, KITCHEN_DISPLAY, ALL_FUNCTIONS, HOLD_TICKET, VIEW_ALL_CLOSE_TICKETS, QUICK_MAINTENANCE };

	public boolean isVisibleWithoutPermission() {
		return visibleWithoutPermission;
	}

	public void setVisibleWithoutPermission(boolean visibleWithoutPermission) {
		this.visibleWithoutPermission = visibleWithoutPermission;
	}
}