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
 * This is an object that contains data related to the VOID_REASONS table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="VOID_REASONS"
 */

public abstract class BaseVoidReason  implements Comparable, Serializable {

	public static String REF = "VoidReason"; //$NON-NLS-1$
	public static String PROP_REASON_TEXT = "reasonText"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$


	// constructors
	public BaseVoidReason () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseVoidReason (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String reasonText;



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
	 * Return the value associated with the column: REASON_TEXT
	 */
	public java.lang.String getReasonText () {
			return reasonText;
	}

	/**
	 * Set the value related to the column: REASON_TEXT
	 * @param reasonText the REASON_TEXT value
	 */
	public void setReasonText (java.lang.String reasonText) {
		this.reasonText = reasonText;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.VoidReason)) return false;
		else {
			com.floreantpos.model.VoidReason voidReason = (com.floreantpos.model.VoidReason) obj;
			if (null == this.getId() || null == voidReason.getId()) return false;
			else return (this.getId().equals(voidReason.getId()));
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