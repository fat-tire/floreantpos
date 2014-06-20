package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the USERS table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="USERS"
 */

public abstract class BaseUser  implements Comparable, Serializable {

	public static String REF = "User";
	public static String PROP_LAST_CLOCK_IN_TIME = "lastClockInTime";
	public static String PROP_CURRENT_TERMINAL = "currentTerminal";
	public static String PROP_SECRET_KEY = "secretKey";
	public static String PROP_NEW_USER_TYPE = "newUserType";
	public static String PROP_FIRST_NAME = "firstName";
	public static String PROP_COST_PER_HOUR = "costPerHour";
	public static String PROP_USER_ID = "userId";
	public static String PROP_LAST_NAME = "lastName";
	public static String PROP_CLOCKED_IN = "clockedIn";
	public static String PROP_CURRENT_SHIFT = "currentShift";
	public static String PROP_SSN = "ssn";


	// constructors
	public BaseUser () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseUser (java.lang.String secretKey) {
		this.setSecretKey(secretKey);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.String secretKey;

	 java.util.Date modifiedTime;

	// fields
	private java.lang.Integer userId;
	private java.lang.String firstName;
	private java.lang.String lastName;
	private java.lang.String ssn;
	private java.lang.Double costPerHour;
	private java.lang.Boolean clockedIn;
	private java.util.Date lastClockInTime;

	// many to one
	private com.floreantpos.model.Shift currentShift;
	private com.floreantpos.model.Terminal currentTerminal;
	private com.floreantpos.model.UserType newUserType;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="assigned"
     *  column="SECRET_KEY"
     */
	public java.lang.String getSecretKey () {
		return secretKey;
	}

	/**
	 * Set the unique identifier of this class
	 * @param secretKey the new ID
	 */
	public void setSecretKey (java.lang.String secretKey) {
		this.secretKey = secretKey;
		this.hashCode = Integer.MIN_VALUE;
	}



	/**
	 * Return the value associated with the column: MODIFIED_TIME
	 */
	public java.util.Date getModifiedTime () {
			return modifiedTime;
	}

	/**
	 * Set the value related to the column: MODIFIED_TIME
	 * @param modifiedTime the MODIFIED_TIME value
	 */
	public void setModifiedTime (java.util.Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}




	/**
	 * Return the value associated with the column: USER_ID
	 */
	public java.lang.Integer getUserId () {
			return userId == null ? Integer.valueOf(0) : userId;
	}

	/**
	 * Set the value related to the column: USER_ID
	 * @param userId the USER_ID value
	 */
	public void setUserId (java.lang.Integer userId) {
		this.userId = userId;
	}



	/**
	 * Return the value associated with the column: FIRST_NAME
	 */
	public java.lang.String getFirstName () {
			return firstName;
	}

	/**
	 * Set the value related to the column: FIRST_NAME
	 * @param firstName the FIRST_NAME value
	 */
	public void setFirstName (java.lang.String firstName) {
		this.firstName = firstName;
	}



	/**
	 * Return the value associated with the column: LAST_NAME
	 */
	public java.lang.String getLastName () {
			return lastName;
	}

	/**
	 * Set the value related to the column: LAST_NAME
	 * @param lastName the LAST_NAME value
	 */
	public void setLastName (java.lang.String lastName) {
		this.lastName = lastName;
	}



	/**
	 * Return the value associated with the column: SSN
	 */
	public java.lang.String getSsn () {
			return ssn;
	}

	/**
	 * Set the value related to the column: SSN
	 * @param ssn the SSN value
	 */
	public void setSsn (java.lang.String ssn) {
		this.ssn = ssn;
	}



	/**
	 * Return the value associated with the column: COST_PER_HOUR
	 */
	public java.lang.Double getCostPerHour () {
					return costPerHour == null ? Double.valueOf(0) : costPerHour;
			}

	/**
	 * Set the value related to the column: COST_PER_HOUR
	 * @param costPerHour the COST_PER_HOUR value
	 */
	public void setCostPerHour (java.lang.Double costPerHour) {
		this.costPerHour = costPerHour;
	}



	/**
	 * Return the value associated with the column: CLOCKED_IN
	 */
	public java.lang.Boolean isClockedIn () {
					return clockedIn == null ? Boolean.FALSE : clockedIn;
			}

	/**
	 * Set the value related to the column: CLOCKED_IN
	 * @param clockedIn the CLOCKED_IN value
	 */
	public void setClockedIn (java.lang.Boolean clockedIn) {
		this.clockedIn = clockedIn;
	}



	/**
	 * Return the value associated with the column: LAST_CLOCK_IN_TIME
	 */
	public java.util.Date getLastClockInTime () {
			return lastClockInTime;
	}

	/**
	 * Set the value related to the column: LAST_CLOCK_IN_TIME
	 * @param lastClockInTime the LAST_CLOCK_IN_TIME value
	 */
	public void setLastClockInTime (java.util.Date lastClockInTime) {
		this.lastClockInTime = lastClockInTime;
	}



	/**
	 * Return the value associated with the column: SHIFT_ID
	 */
	public com.floreantpos.model.Shift getCurrentShift () {
			return currentShift;
	}

	/**
	 * Set the value related to the column: SHIFT_ID
	 * @param currentShift the SHIFT_ID value
	 */
	public void setCurrentShift (com.floreantpos.model.Shift currentShift) {
		this.currentShift = currentShift;
	}



	/**
	 * Return the value associated with the column: currentTerminal
	 */
	public com.floreantpos.model.Terminal getCurrentTerminal () {
			return currentTerminal;
	}

	/**
	 * Set the value related to the column: currentTerminal
	 * @param currentTerminal the currentTerminal value
	 */
	public void setCurrentTerminal (com.floreantpos.model.Terminal currentTerminal) {
		this.currentTerminal = currentTerminal;
	}



	/**
	 * Return the value associated with the column: N_USER_TYPE
	 */
	public com.floreantpos.model.UserType getNewUserType () {
			return newUserType;
	}

	/**
	 * Set the value related to the column: N_USER_TYPE
	 * @param newUserType the N_USER_TYPE value
	 */
	public void setNewUserType (com.floreantpos.model.UserType newUserType) {
		this.newUserType = newUserType;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.User)) return false;
		else {
			com.floreantpos.model.User user = (com.floreantpos.model.User) obj;
			if (null == this.getSecretKey() || null == user.getSecretKey()) return false;
			else return (this.getSecretKey().equals(user.getSecretKey()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getSecretKey()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getSecretKey().hashCode();
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