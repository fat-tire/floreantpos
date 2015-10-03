package com.floreantpos.model.base;

import java.io.Serializable;

import com.floreantpos.Messages;


/**
 * This is an object that contains data related to the CASH_DRAWER_RESET_HISTORY table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="CASH_DRAWER_RESET_HISTORY"
 */

public abstract class BaseCashDrawerResetHistory  implements Comparable, Serializable {

	public static String REF = Messages.getString("BaseCashDrawerResetHistory.0"); //$NON-NLS-1$
	public static String PROP_DRAWER_PULL_REPORT = Messages.getString("BaseCashDrawerResetHistory.1"); //$NON-NLS-1$
	public static String PROP_RESETED_BY = Messages.getString("BaseCashDrawerResetHistory.2"); //$NON-NLS-1$
	public static String PROP_RESET_TIME = Messages.getString("BaseCashDrawerResetHistory.3"); //$NON-NLS-1$
	public static String PROP_ID = Messages.getString("BaseCashDrawerResetHistory.4"); //$NON-NLS-1$


	// constructors
	public BaseCashDrawerResetHistory () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCashDrawerResetHistory (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.util.Date resetTime;

	// one to one
	private com.floreantpos.model.DrawerPullReport drawerPullReport;

	// many to one
	private com.floreantpos.model.User resetedBy;



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
	 * Return the value associated with the column: RESET_TIME
	 */
	public java.util.Date getResetTime () {
			return resetTime;
	}

	/**
	 * Set the value related to the column: RESET_TIME
	 * @param resetTime the RESET_TIME value
	 */
	public void setResetTime (java.util.Date resetTime) {
		this.resetTime = resetTime;
	}



	/**
	 * Return the value associated with the column: drawerPullReport
	 */
	public com.floreantpos.model.DrawerPullReport getDrawerPullReport () {
			return drawerPullReport;
	}

	/**
	 * Set the value related to the column: drawerPullReport
	 * @param drawerPullReport the drawerPullReport value
	 */
	public void setDrawerPullReport (com.floreantpos.model.DrawerPullReport drawerPullReport) {
		this.drawerPullReport = drawerPullReport;
	}



	/**
	 * Return the value associated with the column: USER_ID
	 */
	public com.floreantpos.model.User getResetedBy () {
			return resetedBy;
	}

	/**
	 * Set the value related to the column: USER_ID
	 * @param resetedBy the USER_ID value
	 */
	public void setResetedBy (com.floreantpos.model.User resetedBy) {
		this.resetedBy = resetedBy;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.CashDrawerResetHistory)) return false;
		else {
			com.floreantpos.model.CashDrawerResetHistory cashDrawerResetHistory = (com.floreantpos.model.CashDrawerResetHistory) obj;
			if (null == this.getId() || null == cashDrawerResetHistory.getId()) return false;
			else return (this.getId().equals(cashDrawerResetHistory.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + Messages.getString("BaseCashDrawerResetHistory.5") + this.getId().hashCode(); //$NON-NLS-1$
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