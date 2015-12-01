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
package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the ATTENDENCE_HISTORY table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="ATTENDENCE_HISTORY"
 */

public abstract class BaseAttendenceHistory  implements Comparable, Serializable {

	public static String REF = "AttendenceHistory"; //$NON-NLS-1$
	public static String PROP_USER = "user"; //$NON-NLS-1$
	public static String PROP_CLOCK_IN_TIME = "clockInTime"; //$NON-NLS-1$
	public static String PROP_CLOCK_OUT_TIME = "clockOutTime"; //$NON-NLS-1$
	public static String PROP_TERMINAL = "terminal"; //$NON-NLS-1$
	public static String PROP_CLOCK_IN_HOUR = "clockInHour"; //$NON-NLS-1$
	public static String PROP_CLOCKED_OUT = "clockedOut"; //$NON-NLS-1$
	public static String PROP_SHIFT = "shift"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_CLOCK_OUT_HOUR = "clockOutHour"; //$NON-NLS-1$


	// constructors
	public BaseAttendenceHistory () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseAttendenceHistory (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.util.Date clockInTime;
	private java.util.Date clockOutTime;
	private java.lang.Short clockInHour;
	private java.lang.Short clockOutHour;
	private java.lang.Boolean clockedOut;

	// many to one
	private com.floreantpos.model.User user;
	private com.floreantpos.model.Shift shift;
	private com.floreantpos.model.Terminal terminal;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: CLOCK_IN_TIME
	 */
	public java.util.Date getClockInTime () {
			return clockInTime;
	}

	/**
	 * Set the value related to the column: CLOCK_IN_TIME
	 * @param clockInTime the CLOCK_IN_TIME value
	 */
	public void setClockInTime (java.util.Date clockInTime) {
		this.clockInTime = clockInTime;
	}



	/**
	 * Return the value associated with the column: CLOCK_OUT_TIME
	 */
	public java.util.Date getClockOutTime () {
			return clockOutTime;
	}

	/**
	 * Set the value related to the column: CLOCK_OUT_TIME
	 * @param clockOutTime the CLOCK_OUT_TIME value
	 */
	public void setClockOutTime (java.util.Date clockOutTime) {
		this.clockOutTime = clockOutTime;
	}



	/**
	 * Return the value associated with the column: CLOCK_IN_HOUR
	 */
	public java.lang.Short getClockInHour () {
			return clockInHour;
	}

	/**
	 * Set the value related to the column: CLOCK_IN_HOUR
	 * @param clockInHour the CLOCK_IN_HOUR value
	 */
	public void setClockInHour (java.lang.Short clockInHour) {
		this.clockInHour = clockInHour;
	}



	/**
	 * Return the value associated with the column: CLOCK_OUT_HOUR
	 */
	public java.lang.Short getClockOutHour () {
			return clockOutHour;
	}

	/**
	 * Set the value related to the column: CLOCK_OUT_HOUR
	 * @param clockOutHour the CLOCK_OUT_HOUR value
	 */
	public void setClockOutHour (java.lang.Short clockOutHour) {
		this.clockOutHour = clockOutHour;
	}



	/**
	 * Return the value associated with the column: CLOCKED_OUT
	 */
	public java.lang.Boolean isClockedOut () {
			return clockedOut == null ? Boolean.FALSE : clockedOut;
	}

	/**
	 * Set the value related to the column: CLOCKED_OUT
	 * @param clockedOut the CLOCKED_OUT value
	 */
	public void setClockedOut (java.lang.Boolean clockedOut) {
		this.clockedOut = clockedOut;
	}



	/**
	 * Return the value associated with the column: USER_ID
	 */
	public com.floreantpos.model.User getUser () {
			return user;
	}

	/**
	 * Set the value related to the column: USER_ID
	 * @param user the USER_ID value
	 */
	public void setUser (com.floreantpos.model.User user) {
		this.user = user;
	}



	/**
	 * Return the value associated with the column: SHIFT_ID
	 */
	public com.floreantpos.model.Shift getShift () {
			return shift;
	}

	/**
	 * Set the value related to the column: SHIFT_ID
	 * @param shift the SHIFT_ID value
	 */
	public void setShift (com.floreantpos.model.Shift shift) {
		this.shift = shift;
	}



	/**
	 * Return the value associated with the column: TERMINAL_ID
	 */
	public com.floreantpos.model.Terminal getTerminal () {
			return terminal;
	}

	/**
	 * Set the value related to the column: TERMINAL_ID
	 * @param terminal the TERMINAL_ID value
	 */
	public void setTerminal (com.floreantpos.model.Terminal terminal) {
		this.terminal = terminal;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.AttendenceHistory)) return false;
		else {
			com.floreantpos.model.AttendenceHistory attendenceHistory = (com.floreantpos.model.AttendenceHistory) obj;
			if (null == this.getId() || null == attendenceHistory.getId()) return false;
			else return (this.getId().equals(attendenceHistory.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode(); //$NON-NLS-1$
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo (Object obj) {
		if (obj.hashCode() > hashCode()) return 1;
		else if (obj.hashCode() < hashCode()) return -1;
		else return 0;
	}

	public String toString () {
		return super.toString();
	}


}