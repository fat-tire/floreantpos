package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the SHOP_TABLE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="SHOP_TABLE"
 */

public abstract class BaseShopTable  implements Comparable, Serializable {

	public static String REF = "ShopTable";
	public static String PROP_NAME = "name";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_TABLE_NUMBER = "tableNumber";
	public static String PROP_OCCUPIED = "occupied";
	public static String PROP_ID = "id";
	public static String PROP_DIRTY = "dirty";
	public static String PROP_CAPACITY = "capacity";
	public static String PROP_BOOKED = "booked";
	public static String PROP_Y = "y";
	public static String PROP_X = "x";
	public static String PROP_FLOOR = "floor";


	// constructors
	public BaseShopTable () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseShopTable (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String name;
		protected java.lang.String description;
		protected java.lang.Integer capacity;
		protected java.lang.String tableNumber;
		protected java.lang.Integer x;
		protected java.lang.Integer y;
		protected java.lang.Boolean occupied;
		protected java.lang.Boolean booked;
		protected java.lang.Boolean dirty;

	// many to one
	private com.floreantpos.model.ShopFloor floor;

	// collections
	private java.util.List<com.floreantpos.model.ShopTableType> types;



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
	 * Return the value associated with the column: CAPACITY
	 */
	public java.lang.Integer getCapacity () {
					return capacity == null ? Integer.valueOf(0) : capacity;
			}

	/**
	 * Set the value related to the column: CAPACITY
	 * @param capacity the CAPACITY value
	 */
	public void setCapacity (java.lang.Integer capacity) {
		this.capacity = capacity;
	}



	/**
	 * Return the value associated with the column: TABLE_NUMBER
	 */
	public java.lang.String getTableNumber () {
					return tableNumber;
			}

	/**
	 * Set the value related to the column: TABLE_NUMBER
	 * @param tableNumber the TABLE_NUMBER value
	 */
	public void setTableNumber (java.lang.String tableNumber) {
		this.tableNumber = tableNumber;
	}



	/**
	 * Return the value associated with the column: X
	 */
	public java.lang.Integer getX () {
					return x == null ? Integer.valueOf(0) : x;
			}

	/**
	 * Set the value related to the column: X
	 * @param x the X value
	 */
	public void setX (java.lang.Integer x) {
		this.x = x;
	}



	/**
	 * Return the value associated with the column: Y
	 */
	public java.lang.Integer getY () {
					return y == null ? Integer.valueOf(0) : y;
			}

	/**
	 * Set the value related to the column: Y
	 * @param y the Y value
	 */
	public void setY (java.lang.Integer y) {
		this.y = y;
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
	 * Return the value associated with the column: BOOKED
	 */
	public java.lang.Boolean isBooked () {
								return booked == null ? Boolean.FALSE : booked;
					}

	/**
	 * Set the value related to the column: BOOKED
	 * @param booked the BOOKED value
	 */
	public void setBooked (java.lang.Boolean booked) {
		this.booked = booked;
	}



	/**
	 * Return the value associated with the column: DIRTY
	 */
	public java.lang.Boolean isDirty () {
								return dirty == null ? Boolean.FALSE : dirty;
					}

	/**
	 * Set the value related to the column: DIRTY
	 * @param dirty the DIRTY value
	 */
	public void setDirty (java.lang.Boolean dirty) {
		this.dirty = dirty;
	}



	/**
	 * Return the value associated with the column: FLOOR_ID
	 */
	public com.floreantpos.model.ShopFloor getFloor () {
					return floor;
			}

	/**
	 * Set the value related to the column: FLOOR_ID
	 * @param floor the FLOOR_ID value
	 */
	public void setFloor (com.floreantpos.model.ShopFloor floor) {
		this.floor = floor;
	}



	/**
	 * Return the value associated with the column: types
	 */
	public java.util.List<com.floreantpos.model.ShopTableType> getTypes () {
					return types;
			}

	/**
	 * Set the value related to the column: types
	 * @param types the types value
	 */
	public void setTypes (java.util.List<com.floreantpos.model.ShopTableType> types) {
		this.types = types;
	}

	public void addTotypes (com.floreantpos.model.ShopTableType shopTableType) {
		if (null == getTypes()) setTypes(new java.util.ArrayList<com.floreantpos.model.ShopTableType>());
		getTypes().add(shopTableType);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.ShopTable)) return false;
		else {
			com.floreantpos.model.ShopTable shopTable = (com.floreantpos.model.ShopTable) obj;
			if (null == this.getId() || null == shopTable.getId()) return false;
			else return (this.getId().equals(shopTable.getId()));
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