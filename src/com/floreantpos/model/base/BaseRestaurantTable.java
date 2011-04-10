package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the RESTAURANT_TABLE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="RESTAURANT_TABLE"
 */

public abstract class BaseRestaurantTable  implements Comparable, Serializable {

	public static String REF = "RestaurantTable";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";
	public static String PROP_CAPACITY = "capacity";
	public static String PROP_BOOKED = "booked";


	// constructors
	public BaseRestaurantTable () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRestaurantTable (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer capacity;
	private java.lang.String name;
	private java.lang.Boolean booked;



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





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.RestaurantTable)) return false;
		else {
			com.floreantpos.model.RestaurantTable restaurantTable = (com.floreantpos.model.RestaurantTable) obj;
			if (null == this.getId() || null == restaurantTable.getId()) return false;
			else return (this.getId().equals(restaurantTable.getId()));
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