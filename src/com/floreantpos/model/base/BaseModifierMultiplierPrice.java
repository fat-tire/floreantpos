package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the MODIFIER_MULTIPLIER_PRICE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MODIFIER_MULTIPLIER_PRICE"
 */

public abstract class BaseModifierMultiplierPrice  implements Comparable, Serializable {

	public static String REF = "ModifierMultiplierPrice"; //$NON-NLS-1$
	public static String PROP_PIZZA_MODIFIER_PRICE = "pizzaModifierPrice"; //$NON-NLS-1$
	public static String PROP_PRICE = "price"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_MODIFIER = "modifier"; //$NON-NLS-1$
	public static String PROP_MULTIPLIER = "multiplier"; //$NON-NLS-1$


	// constructors
	public BaseModifierMultiplierPrice () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseModifierMultiplierPrice (java.lang.Integer id) {
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
	private com.floreantpos.model.Multiplier multiplier;
	private com.floreantpos.model.MenuModifier modifier;
	private com.floreantpos.model.PizzaModifierPrice pizzaModifierPrice;



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
		return price;
	}

	/**
	 * Set the value related to the column: PRICE
	 * @param price the PRICE value
	 */
	public void setPrice (java.lang.Double price) {
		this.price = price;
	}


	/**
	 * Custom property
	 */
	public static String getPriceDefaultValue () {
		return "null";
	}


	/**
	 * Return the value associated with the column: MULTIPLIER_ID
	 */
	public com.floreantpos.model.Multiplier getMultiplier () {
					return multiplier;
			}

	/**
	 * Set the value related to the column: MULTIPLIER_ID
	 * @param multiplier the MULTIPLIER_ID value
	 */
	public void setMultiplier (com.floreantpos.model.Multiplier multiplier) {
		this.multiplier = multiplier;
	}



	/**
	 * Return the value associated with the column: MENUMODIFIER_ID
	 */
	public com.floreantpos.model.MenuModifier getModifier () {
					return modifier;
			}

	/**
	 * Set the value related to the column: MENUMODIFIER_ID
	 * @param modifier the MENUMODIFIER_ID value
	 */
	public void setModifier (com.floreantpos.model.MenuModifier modifier) {
		this.modifier = modifier;
	}



	/**
	 * Return the value associated with the column: PIZZA_MODIFIER_PRICE_ID
	 */
	public com.floreantpos.model.PizzaModifierPrice getPizzaModifierPrice () {
					return pizzaModifierPrice;
			}

	/**
	 * Set the value related to the column: PIZZA_MODIFIER_PRICE_ID
	 * @param pizzaModifierPrice the PIZZA_MODIFIER_PRICE_ID value
	 */
	public void setPizzaModifierPrice (com.floreantpos.model.PizzaModifierPrice pizzaModifierPrice) {
		this.pizzaModifierPrice = pizzaModifierPrice;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.ModifierMultiplierPrice)) return false;
		else {
			com.floreantpos.model.ModifierMultiplierPrice modifierMultiplierPrice = (com.floreantpos.model.ModifierMultiplierPrice) obj;
			if (null == this.getId() || null == modifierMultiplierPrice.getId()) return this == obj;
			else return (this.getId().equals(modifierMultiplierPrice.getId()));
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