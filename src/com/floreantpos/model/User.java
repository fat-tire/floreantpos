package com.floreantpos.model;

import java.util.Calendar;

import org.apache.commons.logging.LogFactory;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseUser;
import com.floreantpos.model.dao.UserDAO;

public class User extends BaseUser {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public User () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public User (java.lang.Integer autoId) {
		super(autoId);
	}

	/**
	 * Constructor for required fields
	 */
	public User (
		java.lang.Integer autoId,
		java.lang.String password,
		java.lang.String ssn) {

		super (
			autoId,
			password,
			ssn);
	}

	/*[CONSTRUCTOR MARKER END]*/

	public final static String USER_TYPE_MANAGER = "MANAGER";
	public final static String USER_TYPE_CASHIER = "CASHIER";
	public final static String USER_TYPE_SERVER = "SERVER";

	
	public void doClockIn(Terminal terminal, Shift shift, Calendar currentTime) {
		setClockedIn(true);
		setCurrentShift(shift);
		setCurrentTerminal(terminal);
		setLastClockInTime(currentTime.getTime());
		
		LogFactory.getLog(Application.class).info("terminal id befor saving clockIn=" + terminal.getId());
		
		AttendenceHistory attendenceHistory = new AttendenceHistory();
		attendenceHistory.setClockInTime(currentTime.getTime());
		attendenceHistory.setClockInHour(Short.valueOf((short) currentTime.get(Calendar.HOUR)));
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
		
		attendenceHistory.setClockedOut(true);
		attendenceHistory.setClockOutTime(currentTime.getTime());
		attendenceHistory.setClockOutHour(Short.valueOf((short) currentTime.get(Calendar.HOUR)));
		
		UserDAO.getInstance().saveClockOut(this, attendenceHistory, shift, currentTime);
	}

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName();
	}
	
//	public boolean isManager() {
//		return USER_TYPE_MANAGER.equals(getUserType());
//	}
//	
//	public boolean isCashier() {
//		return USER_TYPE_CASHIER.equals(getUserType());
//	}
//	
//	public boolean isServer() {
//		return USER_TYPE_SERVER.equals(getUserType());
//	}
}