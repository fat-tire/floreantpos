package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the INVENTORY_LOCATION table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="INVENTORY_LOCATION"
 */

public abstract class BaseInventoryLocation  implements Comparable, Serializable {

	public static String REF = "InventoryLocation";
	public static String PROP_NAME = "name";
	public static String PROP_WAREHOUSE = "warehouse";
	public static String PROP_VISIBLE = "visible";
	public static String PROP_SORT_ORDER = "sortOrder";
	public static String PROP_ID = "id";


	// constructors
	public BaseInventoryLocation () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseInventoryLocation (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseInventoryLocation (
		java.lang.Integer id,
		java.lang.String name) {

		this.setId(id);
		this.setName(name);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String name;
		protected java.lang.Integer sortOrder;
		protected java.lang.Boolean visible;

	// many to one
	private com.floreantpos.model.InventoryWarehouse warehouse;



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
	 * Return the value associated with the column: SORT_ORDER
	 */
	public java.lang.Integer getSortOrder () {
					return sortOrder == null ? Integer.valueOf(0) : sortOrder;
			}

	/**
	 * Set the value related to the column: SORT_ORDER
	 * @param sortOrder the SORT_ORDER value
	 */
	public void setSortOrder (java.lang.Integer sortOrder) {
		this.sortOrder = sortOrder;
	}



	/**
	 * Return the value associated with the column: VISIBLE
	 */
	public java.lang.Boolean isVisible () {
								return visible == null ? Boolean.FALSE : visible;
					}

	/**
	 * Set the value related to the column: VISIBLE
	 * @param visible the VISIBLE value
	 */
	public void setVisible (java.lang.Boolean visible) {
		this.visible = visible;
	}



	/**
	 * Return the value associated with the column: WAREHOUSE_ID
	 */
	public com.floreantpos.model.InventoryWarehouse getWarehouse () {
					return warehouse;
			}

	/**
	 * Set the value related to the column: WAREHOUSE_ID
	 * @param warehouse the WAREHOUSE_ID value
	 */
	public void setWarehouse (com.floreantpos.model.InventoryWarehouse warehouse) {
		this.warehouse = warehouse;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.InventoryLocation)) return false;
		else {
			com.floreantpos.model.InventoryLocation inventoryLocation = (com.floreantpos.model.InventoryLocation) obj;
			if (null == this.getId() || null == inventoryLocation.getId()) return false;
			else return (this.getId().equals(inventoryLocation.getId()));
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