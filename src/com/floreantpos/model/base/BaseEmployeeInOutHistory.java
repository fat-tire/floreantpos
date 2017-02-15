package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the EMPLOYEE_IN_OUT_HISTORY table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="EMPLOYEE_IN_OUT_HISTORY"
 */

public abstract class BaseEmployeeInOutHistory  implements Comparable, Serializable {

	public static String REF = "EmployeeInOutHistory";
	public static String PROP_USER = "user";
	public static String PROP_OUT_TIME = "outTime";
	public static String PROP_IN_HOUR = "inHour";
	public static String PROP_TERMINAL = "terminal";
	public static String PROP_CLOCK_OUT = "clockOut";
	public static String PROP_SHIFT = "shift";
	public static String PROP_OUT_HOUR = "outHour";
	public static String PROP_IN_TIME = "inTime";
	public static String PROP_ID = "id";


	// constructors
	public BaseEmployeeInOutHistory () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseEmployeeInOutHistory (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.util.Date outTime;
		protected java.util.Date inTime;
		protected java.lang.Short outHour;
		protected java.lang.Short inHour;
		protected java.lang.Boolean clockOut;

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
	 * Return the value associated with the column: OUT_TIME
	 */
	public java.util.Date getOutTime () {
					return outTime;
			}

	/**
	 * Set the value related to the column: OUT_TIME
	 * @param outTime the OUT_TIME value
	 */
	public void setOutTime (java.util.Date outTime) {
		this.outTime = outTime;
	}



	/**
	 * Return the value associated with the column: IN_TIME
	 */
	public java.util.Date getInTime () {
					return inTime;
			}

	/**
	 * Set the value related to the column: IN_TIME
	 * @param inTime the IN_TIME value
	 */
	public void setInTime (java.util.Date inTime) {
		this.inTime = inTime;
	}



	/**
	 * Return the value associated with the column: OUT_HOUR
	 */
	public java.lang.Short getOutHour () {
					return outHour;
			}

	/**
	 * Set the value related to the column: OUT_HOUR
	 * @param outHour the OUT_HOUR value
	 */
	public void setOutHour (java.lang.Short outHour) {
		this.outHour = outHour;
	}



	/**
	 * Return the value associated with the column: IN_HOUR
	 */
	public java.lang.Short getInHour () {
					return inHour;
			}

	/**
	 * Set the value related to the column: IN_HOUR
	 * @param inHour the IN_HOUR value
	 */
	public void setInHour (java.lang.Short inHour) {
		this.inHour = inHour;
	}



	/**
	 * Return the value associated with the column: CLOCK_OUT
	 */
	public java.lang.Boolean isClockOut () {
								return clockOut == null ? Boolean.FALSE : clockOut;
					}

	/**
	 * Set the value related to the column: CLOCK_OUT
	 * @param clockOut the CLOCK_OUT value
	 */
	public void setClockOut (java.lang.Boolean clockOut) {
		this.clockOut = clockOut;
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
		if (!(obj instanceof com.floreantpos.model.EmployeeInOutHistory)) return false;
		else {
			com.floreantpos.model.EmployeeInOutHistory employeeInOutHistory = (com.floreantpos.model.EmployeeInOutHistory) obj;
			if (null == this.getId() || null == employeeInOutHistory.getId()) return false;
			else return (this.getId().equals(employeeInOutHistory.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
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