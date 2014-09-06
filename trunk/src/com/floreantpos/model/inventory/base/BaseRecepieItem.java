package com.floreantpos.model.inventory.base;

import java.lang.Comparable;
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

	public static String REF = "RecepieItem";
	public static String PROP_INVENTORY_ITEM = "inventoryItem";
	public static String PROP_PERCENTAGE = "percentage";
	public static String PROP_ID = "id";


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

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected float percentage;

	// many to one
	private com.floreantpos.model.inventory.InventoryItem inventoryItem;



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
	public float getPercentage () {
					return percentage;
			}

	/**
	 * Set the value related to the column: PERCENTAGE
	 * @param percentage the PERCENTAGE value
	 */
	public void setPercentage (float percentage) {
		this.percentage = percentage;
	}



	/**
	 * Return the value associated with the column: INVENTORY_ITEM
	 */
	public com.floreantpos.model.inventory.InventoryItem getInventoryItem () {
					return inventoryItem;
			}

	/**
	 * Set the value related to the column: INVENTORY_ITEM
	 * @param inventoryItem the INVENTORY_ITEM value
	 */
	public void setInventoryItem (com.floreantpos.model.inventory.InventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.inventory.RecepieItem)) return false;
		else {
			com.floreantpos.model.inventory.RecepieItem recepieItem = (com.floreantpos.model.inventory.RecepieItem) obj;
			if (null == this.getId() || null == recepieItem.getId()) return false;
			else return (this.getId().equals(recepieItem.getId()));
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