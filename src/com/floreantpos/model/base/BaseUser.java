package com.floreantpos.model.base;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

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
	public static String PROP_TYPE = "type";
	public static String PROP_PASSWORD = "password";
	public static String PROP_USER_ID = "userId";
	public static String PROP_LAST_NAME = "lastName";
	public static String PROP_SSN = "ssn";
	public static String PROP_PHONE_NO = "phoneNo";
	public static String PROP_DRIVER = "driver";
	public static String PROP_ACTIVE = "active";
	public static String PROP_CURRENT_TERMINAL = "currentTerminal";
	public static String PROP_AVAILABLE_FOR_DELIVERY = "availableForDelivery";
	public static String PROP_AUTO_ID = "autoId";
	public static String PROP_FIRST_NAME = "firstName";
	public static String PROP_COST_PER_HOUR = "costPerHour";
	public static String PROP_CLOCKED_IN = "clockedIn";
	public static String PROP_CURRENT_SHIFT = "currentShift";
	public static String PROP_LAST_CLOCK_OUT_TIME = "lastClockOutTime";


	// constructors
	public BaseUser () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseUser (java.lang.Integer autoId) {
		this.setAutoId(autoId);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseUser (
		java.lang.Integer autoId,
		java.lang.String password) {

		this.setAutoId(autoId);
		this.setPassword(password);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer autoId;

	// fields
		protected java.lang.Integer userId;
		protected java.lang.String password;
		protected java.lang.String firstName;
		protected java.lang.String lastName;
		protected java.lang.String ssn;
		protected java.lang.Double costPerHour;
		protected java.lang.Boolean clockedIn;
		protected java.util.Date lastClockInTime;
		protected java.util.Date lastClockOutTime;
		protected java.lang.String phoneNo;
		protected java.lang.Boolean driver;
		protected java.lang.Boolean availableForDelivery;
		protected java.lang.Boolean active;

	// many to one
	private com.floreantpos.model.Shift currentShift;
	private com.floreantpos.model.Terminal currentTerminal;
	private com.floreantpos.model.UserType type;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="AUTO_ID"
     */
	public java.lang.Integer getAutoId () {
		return autoId;
	}

	/**
	 * Set the unique identifier of this class
	 * @param autoId the new ID
	 */
	public void setAutoId (java.lang.Integer autoId) {
		this.autoId = autoId;
		this.hashCode = Integer.MIN_VALUE;
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
	 * Return the value associated with the column: USER_PASS
	 */
	public java.lang.String getPassword () {
					return password;
			}

	/**
	 * Set the value related to the column: USER_PASS
	 * @param password the USER_PASS value
	 */
	public void setPassword (java.lang.String password) {
		this.password = password;
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
	 * Return the value associated with the column: LAST_CLOCK_OUT_TIME
	 */
	public java.util.Date getLastClockOutTime () {
					return lastClockOutTime;
			}

	/**
	 * Set the value related to the column: LAST_CLOCK_OUT_TIME
	 * @param lastClockOutTime the LAST_CLOCK_OUT_TIME value
	 */
	public void setLastClockOutTime (java.util.Date lastClockOutTime) {
		this.lastClockOutTime = lastClockOutTime;
	}



	/**
	 * Return the value associated with the column: PHONE_NO
	 */
	public java.lang.String getPhoneNo () {
					return phoneNo;
			}

	/**
	 * Set the value related to the column: PHONE_NO
	 * @param phoneNo the PHONE_NO value
	 */
	public void setPhoneNo (java.lang.String phoneNo) {
		this.phoneNo = phoneNo;
	}



	/**
	 * Return the value associated with the column: IS_DRIVER
	 */
	public java.lang.Boolean isDriver () {
								return driver == null ? Boolean.FALSE : driver;
					}

	/**
	 * Set the value related to the column: IS_DRIVER
	 * @param driver the IS_DRIVER value
	 */
	public void setDriver (java.lang.Boolean driver) {
		this.driver = driver;
	}



	/**
	 * Return the value associated with the column: AVAILABLE_FOR_DELIVERY
	 */
	public java.lang.Boolean isAvailableForDelivery () {
								return availableForDelivery == null ? Boolean.FALSE : availableForDelivery;
					}

	/**
	 * Set the value related to the column: AVAILABLE_FOR_DELIVERY
	 * @param availableForDelivery the AVAILABLE_FOR_DELIVERY value
	 */
	public void setAvailableForDelivery (java.lang.Boolean availableForDelivery) {
		this.availableForDelivery = availableForDelivery;
	}



	/**
	 * Return the value associated with the column: ACTIVE
	 */
	public java.lang.Boolean isActive () {
								return active == null ? Boolean.FALSE : active;
					}

	/**
	 * Set the value related to the column: ACTIVE
	 * @param active the ACTIVE value
	 */
	public void setActive (java.lang.Boolean active) {
		this.active = active;
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
	@XmlTransient
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
	public com.floreantpos.model.UserType getType () {
					return type;
			}

	/**
	 * Set the value related to the column: N_USER_TYPE
	 * @param type the N_USER_TYPE value
	 */
	public void setType (com.floreantpos.model.UserType type) {
		this.type = type;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.User)) return false;
		else {
			com.floreantpos.model.User user = (com.floreantpos.model.User) obj;
			if (null == this.getAutoId() || null == user.getAutoId()) return false;
			else return (this.getAutoId().equals(user.getAutoId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getAutoId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getAutoId().hashCode();
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