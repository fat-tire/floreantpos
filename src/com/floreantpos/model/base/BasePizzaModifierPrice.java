package com.floreantpos.model.base;

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
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_SIZE = "size"; //$NON-NLS-1$


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

	// many to one
	private com.floreantpos.model.MenuItemSize size;

	// collections
	private java.util.List<com.floreantpos.model.ModifierMultiplierPrice> multiplierPriceList;



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



	/**
	 * Return the value associated with the column: multiplierPriceList
	 */
	public java.util.List<com.floreantpos.model.ModifierMultiplierPrice> getMultiplierPriceList () {
					return multiplierPriceList;
			}

	/**
	 * Set the value related to the column: multiplierPriceList
	 * @param multiplierPriceList the multiplierPriceList value
	 */
	public void setMultiplierPriceList (java.util.List<com.floreantpos.model.ModifierMultiplierPrice> multiplierPriceList) {
		this.multiplierPriceList = multiplierPriceList;
	}

	public void addTomultiplierPriceList (com.floreantpos.model.ModifierMultiplierPrice modifierMultiplierPrice) {
		if (null == getMultiplierPriceList()) setMultiplierPriceList(new java.util.ArrayList<com.floreantpos.model.ModifierMultiplierPrice>());
		getMultiplierPriceList().add(modifierMultiplierPrice);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.PizzaModifierPrice)) return false;
		else {
			com.floreantpos.model.PizzaModifierPrice pizzaModifierPrice = (com.floreantpos.model.PizzaModifierPrice) obj;
			if (null == this.getId() || null == pizzaModifierPrice.getId()) return this == obj;
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