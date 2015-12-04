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
 * This is an object that contains data related to the USER_PERMISSION table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="USER_PERMISSION"
 */

public abstract class BaseUserPermission  implements Comparable, Serializable {

	public static String REF = "UserPermission"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$


	// constructors
	public BaseUserPermission () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseUserPermission (java.lang.String name) {
		this.setName(name);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.String name;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="assigned"
     *  column="NAME"
     */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the unique identifier of this class
	 * @param name the new ID
	 */
	public void setName (java.lang.String name) {
		this.name = name;
		this.hashCode = Integer.MIN_VALUE;
	}






	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.UserPermission)) return false;
		else {
			com.floreantpos.model.UserPermission userPermission = (com.floreantpos.model.UserPermission) obj;
			if (null == this.getName() || null == userPermission.getName()) return false;
			else return (this.getName().equals(userPermission.getName()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getName()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getName().hashCode(); //$NON-NLS-1$
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