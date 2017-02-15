package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the PIZZA_PRICE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="PIZZA_PRICE"
 */

public abstract class BasePizzaPrice  implements Comparable, Serializable {

	public static String REF = "PizzaPrice"; //$NON-NLS-1$
	public static String PROP_CRUST = "crust"; //$NON-NLS-1$
	public static String PROP_ORDER_TYPE = "orderType"; //$NON-NLS-1$
	public static String PROP_PRICE = "price"; //$NON-NLS-1$
	public static String PROP_SIZE = "size"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$


	// constructors
	public BasePizzaPrice () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BasePizzaPrice (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.Double price;

	// many to one
	private com.floreantpos.model.MenuItemSize size;
	private com.floreantpos.model.PizzaCrust crust;
	private com.floreantpos.model.OrderType orderType;



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
	 * Return the value associated with the column: MENU_ITEM_SIZE
	 */
	public com.floreantpos.model.MenuItemSize getSize () {
					return size;
			}

	/**
	 * Set the value related to the column: MENU_ITEM_SIZE
	 * @param size the MENU_ITEM_SIZE value
	 */
	public void setSize (com.floreantpos.model.MenuItemSize size) {
		this.size = size;
	}



	/**
	 * Return the value associated with the column: CRUST
	 */
	public com.floreantpos.model.PizzaCrust getCrust () {
					return crust;
			}

	/**
	 * Set the value related to the column: CRUST
	 * @param crust the CRUST value
	 */
	public void setCrust (com.floreantpos.model.PizzaCrust crust) {
		this.crust = crust;
	}



	/**
	 * Return the value associated with the column: ORDER_TYPE
	 */
	public com.floreantpos.model.OrderType getOrderType () {
					return orderType;
			}

	/**
	 * Set the value related to the column: ORDER_TYPE
	 * @param orderType the ORDER_TYPE value
	 */
	public void setOrderType (com.floreantpos.model.OrderType orderType) {
		this.orderType = orderType;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.PizzaPrice)) return false;
		else {
			com.floreantpos.model.PizzaPrice pizzaPrice = (com.floreantpos.model.PizzaPrice) obj;
			if (null == this.getId() || null == pizzaPrice.getId()) return false;
			else return (this.getId().equals(pizzaPrice.getId()));
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