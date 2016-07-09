package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the PIZZA_MODIFIER_PRICE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="PIZZA_MODIFIER_PRICE"
 */

public abstract class BasePizzaModifierPrice  implements Comparable, Serializable {

	public static String REF = "PizzaModifierPrice"; //$NON-NLS-1$
	public static String PROP_PRICE = "price"; //$NON-NLS-1$
	public static String PROP_SIZE = "size"; //$NON-NLS-1$
	public static String PROP_EXTRA_PRICE = "extraPrice"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$


	// constructors
	public BasePizzaModifierPrice () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BasePizzaModifierPrice (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.Double price;
		protected java.lang.Double extraPrice;

	// many to one
	private com.floreantpos.model.MenuItemSize size;



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
	 * Return the value associated with the column: PRICE
	 */
	public java.lang.Double getPrice () {
									return price == null ? Double.valueOf(0) : price;
					}

	/**
	 * Set the value related to the column: PRICE
	 * @param price the PRICE value
	 */
	public void setPrice (java.lang.Double price) {
		this.price = price;
	}



	/**
	 * Return the value associated with the column: EXTRA_PRICE
	 */
	public java.lang.Double getExtraPrice () {
									return extraPrice == null ? Double.valueOf(0) : extraPrice;
					}

	/**
	 * Set the value related to the column: EXTRA_PRICE
	 * @param extraPrice the EXTRA_PRICE value
	 */
	public void setExtraPrice (java.lang.Double extraPrice) {
		this.extraPrice = extraPrice;
	}



	/**
	 * Return the value associated with the column: ITEM_SIZE
	 */
	public com.floreantpos.model.MenuItemSize getSize () {
					return size;
			}

	/**
	 * Set the value related to the column: ITEM_SIZE
	 * @param size the ITEM_SIZE value
	 */
	public void setSize (com.floreantpos.model.MenuItemSize size) {
		this.size = size;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.PizzaModifierPrice)) return false;
		else {
			com.floreantpos.model.PizzaModifierPrice pizzaModifierPrice = (com.floreantpos.model.PizzaModifierPrice) obj;
			if (null == this.getId() || null == pizzaModifierPrice.getId()) return false;
			else return (this.getId().equals(pizzaModifierPrice.getId()));
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