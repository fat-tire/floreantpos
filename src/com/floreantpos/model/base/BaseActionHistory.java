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
 * This is an object that contains data related to the ACTION_HISTORY table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="ACTION_HISTORY"
 */

public abstract class BaseActionHistory  implements Comparable, Serializable {

	public static String REF = "ActionHistory"; //$NON-NLS-1$
	public static String PROP_PERFORMER = "performer"; //$NON-NLS-1$
	public static String PROP_DESCRIPTION = "description"; //$NON-NLS-1$
	public static String PROP_ACTION_NAME = "actionName"; //$NON-NLS-1$
	public static String PROP_ACTION_TIME = "actionTime"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$


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