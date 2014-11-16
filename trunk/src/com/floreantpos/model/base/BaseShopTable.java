package com.floreantpos.model.base;

import java.lang.Comparable;
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
	public static String PROP_NUMBER = "number";
	public static String PROP_ID = "id";
	public static String PROP_Y = "y";
	public static String PROP_X = "x";


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
		protected java.lang.Integer number;
		protected java.lang.Integer x;
		protected java.lang.Integer y;



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
	 * Return the value associated with the column: NUMBER
	 */
	public java.lang.Integer getNumber () {
					return number == null ? Integer.valueOf(0) : number;
			}

	/**
	 * Set the value related to the column: NUMBER
	 * @param number the NUMBER value
	 */
	public void setNumber (java.lang.Integer number) {
		this.number = number;
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