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
 * This is an object that contains data related to the SHOP_FLOOR table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="SHOP_FLOOR"
 */

public abstract class BaseShopFloor  implements Comparable, Serializable {

	public static String REF = "ShopFloor"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_IMAGE = "image"; //$NON-NLS-1$
	public static String PROP_OCCUPIED = "occupied"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$


	// constructors
	public BaseShopFloor () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseShopFloor (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String name;
		protected java.lang.Boolean occupied;
		protected java.sql.Blob image;

	// collections
	private java.util.Set<com.floreantpos.model.ShopTable> tables;



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
	 * Return the value associated with the column: NAME
	 */
	public java.lang.String getName () {
					return name;
			}

	/**
	 * Set the value related to the column: NAME
	 * @param name the NAME value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}



	/**
	 * Return the value associated with the column: OCCUPIED
	 */
	public java.lang.Boolean isOccupied () {
								return occupied == null ? Boolean.FALSE : occupied;
					}

	/**
	 * Set the value related to the column: OCCUPIED
	 * @param occupied the OCCUPIED value
	 */
	public void setOccupied (java.lang.Boolean occupied) {
		this.occupied = occupied;
	}



	/**
	 * Return the value associated with the column: IMAGE
	 */
	public java.sql.Blob getImage () {
					return image;
			}

	/**
	 * Set the value related to the column: IMAGE
	 * @param image the IMAGE value
	 */
	public void setImage (java.sql.Blob image) {
		this.image = image;
	}



	/**
	 * Return the value associated with the column: tables
	 */
	public java.util.Set<com.floreantpos.model.ShopTable> getTables () {
					return tables;
			}

	/**
	 * Set the value related to the column: tables
	 * @param tables the tables value
	 */
	public void setTables (java.util.Set<com.floreantpos.model.ShopTable> tables) {
		this.tables = tables;
	}

	public void addTotables (com.floreantpos.model.ShopTable shopTable) {
		if (null == getTables()) setTables(new java.util.TreeSet<com.floreantpos.model.ShopTable>());
		getTables().add(shopTable);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.ShopFloor)) return false;
		else {
			com.floreantpos.model.ShopFloor shopFloor = (com.floreantpos.model.ShopFloor) obj;
			if (null == this.getId() || null == shopFloor.getId()) return false;
			else return (this.getId().equals(shopFloor.getId()));
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