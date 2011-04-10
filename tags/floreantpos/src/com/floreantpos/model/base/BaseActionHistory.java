package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the ACTION_HISTORY table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="ACTION_HISTORY"
 */

public abstract class BaseActionHistory  implements Comparable, Serializable {

	public static String REF = "ActionHistory";
	public static String PROP_PERFORMER = "performer";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_ACTION_NAME = "actionName";
	public static String PROP_ACTION_TIME = "actionTime";
	public static String PROP_ID = "id";


	// constructors
	public BaseActionHistory () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseActionHistory (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.util.Date actionTime;
	private java.lang.String actionName;
	private java.lang.String description;

	// many to one
	private com.floreantpos.model.User performer;



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
	 * Return the value associated with the column: ACTION_TIME
	 */
	public java.util.Date getActionTime () {
			return actionTime;
	}

	/**
	 * Set the value related to the column: ACTION_TIME
	 * @param actionTime the ACTION_TIME value
	 */
	public void setActionTime (java.util.Date actionTime) {
		this.actionTime = actionTime;
	}



	/**
	 * Return the value associated with the column: ACTION_NAME
	 */
	public java.lang.String getActionName () {
			return actionName;
	}

	/**
	 * Set the value related to the column: ACTION_NAME
	 * @param actionName the ACTION_NAME value
	 */
	public void setActionName (java.lang.String actionName) {
		this.actionName = actionName;
	}



	/**
	 * Return the value associated with the column: DESCRIPTION
	 */
	public java.lang.String getDescription () {
			return description;
	}

	/**
	 * Set the value related to the column: DESCRIPTION
	 * @param description the DESCRIPTION value
	 */
	public void setDescription (java.lang.String description) {
		this.description = description;
	}



	/**
	 * Return the value associated with the column: USER_ID
	 */
	public com.floreantpos.model.User getPerformer () {
			return performer;
	}

	/**
	 * Set the value related to the column: USER_ID
	 * @param performer the USER_ID value
	 */
	public void setPerformer (com.floreantpos.model.User performer) {
		this.performer = performer;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.ActionHistory)) return false;
		else {
			com.floreantpos.model.ActionHistory actionHistory = (com.floreantpos.model.ActionHistory) obj;
			if (null == this.getId() || null == actionHistory.getId()) return false;
			else return (this.getId().equals(actionHistory.getId()));
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