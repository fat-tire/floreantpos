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
 * This is an object that contains data related to the RECEPIE_ITEM table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="RECEPIE_ITEM"
 */

public abstract class BaseRecepieItem  implements Comparable, Serializable {

	public static String REF = "RecepieItem"; //$NON-NLS-1$
	public static String PROP_INVENTORY_ITEM = "inventoryItem"; //$NON-NLS-1$
	public static String PROP_PERCENTAGE = "percentage"; //$NON-NLS-1$
	public static String PROP_RECEPIE = "recepie"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_INVENTORY_DEDUCTABLE = "inventoryDeductable"; //$NON-NLS-1$


	// constructors
	public BaseRecepieItem () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRecepieItem (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseRecepieItem (
		java.lang.Integer id,
		com.floreantpos.model.Recepie recepie) {

		this.setId(id);
		this.setRecepie(recepie);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.Double percentage;
		protected java.lang.Boolean inventoryDeductable;

	// many to one
	private com.floreantpos.model.InventoryItem inventoryItem;
	private com.floreantpos.model.Recepie recepie;



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
	 * Return the value associated with the column: PERCENTAGE
	 */
	public java.lang.Double getPercentage () {
									return percentage == null ? Double.valueOf(0) : percentage;
					}

	/**
	 * Set the value related to the column: PERCENTAGE
	 * @param percentage the PERCENTAGE value
	 */
	public void setPercentage (java.lang.Double percentage) {
		this.percentage = percentage;
	}



	/**
	 * Return the value associated with the column: INVENTORY_DEDUCTABLE
	 */
	public java.lang.Boolean isInventoryDeductable () {
								return inventoryDeductable == null ? Boolean.FALSE : inventoryDeductable;
					}

	/**
	 * Set the value related to the column: INVENTORY_DEDUCTABLE
	 * @param inventoryDeductable the INVENTORY_DEDUCTABLE value
	 */
	public void setInventoryDeductable (java.lang.Boolean inventoryDeductable) {
		this.inventoryDeductable = inventoryDeductable;
	}



	/**
	 * Return the value associated with the column: INVENTORY_ITEM
	 */
	public com.floreantpos.model.InventoryItem getInventoryItem () {
					return inventoryItem;
			}

	/**
	 * Set the value related to the column: INVENTORY_ITEM
	 * @param inventoryItem the INVENTORY_ITEM value
	 */
	public void setInventoryItem (com.floreantpos.model.InventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}



	/**
	 * Return the value associated with the column: RECEPIE_ID
	 */
	public com.floreantpos.model.Recepie getRecepie () {
					return recepie;
			}

	/**
	 * Set the value related to the column: RECEPIE_ID
	 * @param recepie the RECEPIE_ID value
	 */
	public void setRecepie (com.floreantpos.model.Recepie recepie) {
		this.recepie = recepie;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.RecepieItem)) return false;
		else {
			com.floreantpos.model.RecepieItem recepieItem = (com.floreantpos.model.RecepieItem) obj;
			if (null == this.getId() || null == recepieItem.getId()) return false;
			else return (this.getId().equals(recepieItem.getId()));
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