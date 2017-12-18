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

import java.util.Calendar;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.LogFactory;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseUser;
import com.floreantpos.model.dao.UserDAO;

@XmlRootElement(name = "user")
public class User extends BaseUser {
	private static final long serialVersionUID = 1L;
	private int id;
	private boolean root;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public User() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public User(java.lang.Integer autoId) {
		super(autoId);
	}

	/**
	 * Constructor for required fields
	 */
	public User(java.lang.Integer autoId, java.lang.String password) {

		super(autoId, password);
	}

	/*[CONSTRUCTOR MARKER END]*/

	public final static String USER_TYPE_MANAGER = "MANAGER"; //$NON-NLS-1$
	public final static String USER_TYPE_CASHIER = "CASHIER"; //$NON-NLS-1$
	public final static String USER_TYPE_SERVER = "SERVER"; //$NON-NLS-1$

	/**
	 * Return the value associated with the column: ACTIVE
	 */
	public java.lang.Boolean isActive() {
		return active == null ? Boolean.TRUE : active;
	}

	public boolean hasPermission(UserPermission permission) {
		return getType().hasPermission(permission);
	}

	public void doClockIn(Terminal terminal, Shift shift, Calendar currentTime) {
		setClockedIn(true);
		setCurrentShift(shift);
		setCurrentTerminal(terminal);
		setLastClockInTime(currentTime.getTime());
		if (isDriver()) {
			setAvailableForDelivery(true);
		}

		LogFactory.getLog(Application.class).info("terminal id befor saving clockIn=" + terminal.getId()); //$NON-NLS-1$

		AttendenceHistory attendenceHistory = new AttendenceHistory();
		attendenceHistory.setClockInTime(currentTime.getTime());
		attendenceHistory.setClockInHour(Short.valueOf((short) currentTime.get(Calendar.HOUR_OF_DAY)));
		attendenceHistory.setUser(this);
		attendenceHistory.setTerminal(terminal);
		attendenceHistory.setShift(shift);

		UserDAO.getInstance().saveClockIn(this, attendenceHistory, shift, currentTime);
	}

	public void doClockOut(AttendenceHistory attendenceHistory, Shift shift, Calendar currentTime) {
		setClockedIn(false);
		setCurrentShift(null);
		setCurrentTerminal(null);
		setLastClockInTime(null);
		setLastClockOutTime(null);
		if (isDriver()) {
			setAvailableForDelivery(false);
		}

		attendenceHistory.setClockedOut(true);
		attendenceHistory.setClockOutTime(currentTime.getTime());
		attendenceHistory.setClockOutHour(Short.valueOf((short) currentTime.get(Calendar.HOUR_OF_DAY)));

		UserDAO.getInstance().saveClockOut(this, attendenceHistory, shift, currentTime);
	}

	public boolean canViewAllOpenTickets() {
		if (getType() == null) {
			return false;
		}

		Set<UserPermission> permissions = getType().getPermissions();
		if (permissions == null) {
			return false;
		}

		for (UserPermission permission : permissions) {
			if (permission.equals(UserPermission.VIEW_ALL_OPEN_TICKETS)) {
				return true;
			}
		}
		return false;
	}

	public boolean canViewAllCloseTickets() {
		if (getType() == null) {
			return false;
		}

		Set<UserPermission> permissions = getType().getPermissions();
		if (permissions == null) {
			return false;
		}

		for (UserPermission permission : permissions) {
			if (permission.equals(UserPermission.VIEW_ALL_CLOSE_TICKETS)) {
				return true;
			}
		}

		return false;
	}

	public void setFullName(String str) {
	}

	public String getStatus() {
		if (isClockedIn()) {
			if (isAvailableForDelivery()) {
				return "Available"; //$NON-NLS-1$
			}
			else {
				return "Driving"; //$NON-NLS-1$
			}
		}
		else {
			return "Not available"; //$NON-NLS-1$
		}
	}

	public String getFullName() {
		return getFirstName() + " " + getLastName(); //$NON-NLS-1$
	}

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName(); //$NON-NLS-1$
	}

	public boolean isManager() {
		return hasPermission(UserPermission.PERFORM_MANAGER_TASK);
	}

	public boolean isAdministrator() {
		return hasPermission(UserPermission.PERFORM_ADMINISTRATIVE_TASK);
	}

	public int getId() {
		return super.getUserId();
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isRoot() {
		return true;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

}