package com.floreantpos.model.inventory.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the INVENTORY_TRANSACTION_TYPE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="INVENTORY_TRANSACTION_TYPE"
 */

public abstract class BaseInventoryTransactionType  implements Comparable, Serializable {

	public static String REF = "InventoryTransactionType";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";
	public static String PROP_IN_OR_OUT = "inOrOut";


	// constructors
	public BaseInventoryTransactionType () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseInventoryTransactionType (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	private java.util.Date modifiedTime;

	// fields
		protected java.lang.String name;
		protected java.lang.Integer inOrOut;



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
	 * Return the value associated with the column: MODIFIED_TIME
	 */
	public java.util.Date getModifiedTime () {
					return modifiedTime;
			}

	/**
	 * Set the value related to the column: MODIFIED_TIME
	 * @param modifiedTime the MODIFIED_TIME value
	 */
	public void setModifiedTime (java.util.Date modifiedTime) {
		this.modifiedTime = modifiedTime;
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
	 * Return the value associated with the column: IN_OR_OUT
	 */
	public java.lang.Integer getInOrOut () {
					return inOrOut == null ? Integer.valueOf(0) : inOrOut;
			}

	/**
	 * Set the value related to the column: IN_OR_OUT
	 * @param inOrOut the IN_OR_OUT value
	 */
	public void setInOrOut (java.lang.Integer inOrOut) {
		this.inOrOut = inOrOut;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.inventory.InventoryTransactionType)) return false;
		else {
			com.floreantpos.model.inventory.InventoryTransactionType inventoryTransactionType = (com.floreantpos.model.inventory.InventoryTransactionType) obj;
			if (null == this.getId() || null == inventoryTransactionType.getId()) return false;
			else return (this.getId().equals(inventoryTransactionType.getId()));
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