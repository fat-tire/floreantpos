package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the MENU_ITEM_SHIFT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MENU_ITEM_SHIFT"
 */

public abstract class BaseMenuItemShift  implements Comparable, Serializable {

	public static String REF = "MenuItemShift";
	public static String PROP_SHIFT_PRICE = "shiftPrice";
	public static String PROP_SHIFT = "shift";
	public static String PROP_ID = "id";


	// constructors
	public BaseMenuItemShift () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMenuItemShift (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Double shiftPrice;

	// many to one
	private com.floreantpos.model.Shift shift;



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
	 * Return the value associated with the column: SHIFT_PRICE
	 */
	public java.lang.Double getShiftPrice () {
			return shiftPrice == null ? Double.valueOf(0) : shiftPrice;
	}

	/**
	 * Set the value related to the column: SHIFT_PRICE
	 * @param shiftPrice the SHIFT_PRICE value
	 */
	public void setShiftPrice (java.lang.Double shiftPrice) {
		this.shiftPrice = shiftPrice;
	}



	/**
	 * Return the value associated with the column: SHIFT_ID
	 */
	public com.floreantpos.model.Shift getShift () {
			return shift;
	}

	/**
	 * Set the value related to the column: SHIFT_ID
	 * @param shift the SHIFT_ID value
	 */
	public void setShift (com.floreantpos.model.Shift shift) {
		this.shift = shift;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.MenuItemShift)) return false;
		else {
			com.floreantpos.model.MenuItemShift menuItemShift = (com.floreantpos.model.MenuItemShift) obj;
			if (null == this.getId() || null == menuItemShift.getId()) return false;
			else return (this.getId().equals(menuItemShift.getId()));
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