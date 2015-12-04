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
 * This is an object that contains data related to the INVENTORY_META_CODE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="INVENTORY_META_CODE"
 */

public abstract class BaseInventoryMetaCode  implements Comparable, Serializable {

	public static String REF = "InventoryMetaCode"; //$NON-NLS-1$
	public static String PROP_DESCRIPTION = "description"; //$NON-NLS-1$
	public static String PROP_CODE_NO = "codeNo"; //$NON-NLS-1$
	public static String PROP_TYPE = "type"; //$NON-NLS-1$
	public static String PROP_CODE_TEXT = "codeText"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$


	// constructors
	public BaseInventoryMetaCode () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseInventoryMetaCode (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String type;
		protected java.lang.String codeText;
		protected java.lang.Integer codeNo;
		protected java.lang.String description;



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
	 * Return the value associated with the column: TYPE
	 */
	public java.lang.String getType () {
					return type;
			}

	/**
	 * Set the value related to the column: TYPE
	 * @param type the TYPE value
	 */
	public void setType (java.lang.String type) {
		this.type = type;
	}



	/**
	 * Return the value associated with the column: CODE_TEXT
	 */
	public java.lang.String getCodeText () {
					return codeText;
			}

	/**
	 * Set the value related to the column: CODE_TEXT
	 * @param codeText the CODE_TEXT value
	 */
	public void setCodeText (java.lang.String codeText) {
		this.codeText = codeText;
	}



	/**
	 * Return the value associated with the column: CODE_NO
	 */
	public java.lang.Integer getCodeNo () {
					return codeNo == null ? Integer.valueOf(0) : codeNo;
			}

	/**
	 * Set the value related to the column: CODE_NO
	 * @param codeNo the CODE_NO value
	 */
	public void setCodeNo (java.lang.Integer codeNo) {
		this.codeNo = codeNo;
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





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.InventoryMetaCode)) return false;
		else {
			com.floreantpos.model.InventoryMetaCode inventoryMetaCode = (com.floreantpos.model.InventoryMetaCode) obj;
			if (null == this.getId() || null == inventoryMetaCode.getId()) return false;
			else return (this.getId().equals(inventoryMetaCode.getId()));
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