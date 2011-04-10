package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the MENU_MODIFIER table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MENU_MODIFIER"
 */

public abstract class BaseMenuModifier  implements Comparable, Serializable {

	public static String REF = "MenuModifier";
	public static String PROP_NAME = "name";
	public static String PROP_SHOULD_PRINT_TO_KITCHEN = "shouldPrintToKitchen";
	public static String PROP_EXTRA_PRICE = "extraPrice";
	public static String PROP_ENABLE = "enable";
	public static String PROP_PRICE = "price";
	public static String PROP_TAX = "tax";
	public static String PROP_ID = "id";
	public static String PROP_MODIFIER_GROUP = "modifierGroup";


	// constructors
	public BaseMenuModifier () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMenuModifier (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	private java.util.Date modifiedTime;

	// fields
	private java.lang.String name;
	private java.lang.Double price;
	private java.lang.Double extraPrice;
	private java.lang.Boolean enable;
	private java.lang.Boolean shouldPrintToKitchen;

	// many to one
	private com.floreantpos.model.MenuModifierGroup modifierGroup;
	private com.floreantpos.model.Tax tax;



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
	 * Return the value associated with the column: ENABLE
	 */
	public java.lang.Boolean isEnable () {
					return enable == null ? Boolean.FALSE : enable;
			}

	/**
	 * Set the value related to the column: ENABLE
	 * @param enable the ENABLE value
	 */
	public void setEnable (java.lang.Boolean enable) {
		this.enable = enable;
	}



	/**
	 * Return the value associated with the column: PRINT_TO_KITCHEN
	 */
	public java.lang.Boolean isShouldPrintToKitchen () {
					return shouldPrintToKitchen == null ? Boolean.valueOf(true) : shouldPrintToKitchen;
			}

	/**
	 * Set the value related to the column: PRINT_TO_KITCHEN
	 * @param shouldPrintToKitchen the PRINT_TO_KITCHEN value
	 */
	public void setShouldPrintToKitchen (java.lang.Boolean shouldPrintToKitchen) {
		this.shouldPrintToKitchen = shouldPrintToKitchen;
	}


	/**
	 * Custom property
	 */
	public static String getShouldPrintToKitchenDefaultValue () {
		return "true";
	}


	/**
	 * Return the value associated with the column: GROUP_ID
	 */
	public com.floreantpos.model.MenuModifierGroup getModifierGroup () {
			return modifierGroup;
	}

	/**
	 * Set the value related to the column: GROUP_ID
	 * @param modifierGroup the GROUP_ID value
	 */
	public void setModifierGroup (com.floreantpos.model.MenuModifierGroup modifierGroup) {
		this.modifierGroup = modifierGroup;
	}



	/**
	 * Return the value associated with the column: TAX_ID
	 */
	public com.floreantpos.model.Tax getTax () {
			return tax;
	}

	/**
	 * Set the value related to the column: TAX_ID
	 * @param tax the TAX_ID value
	 */
	public void setTax (com.floreantpos.model.Tax tax) {
		this.tax = tax;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.MenuModifier)) return false;
		else {
			com.floreantpos.model.MenuModifier menuModifier = (com.floreantpos.model.MenuModifier) obj;
			if (null == this.getId() || null == menuModifier.getId()) return false;
			else return (this.getId().equals(menuModifier.getId()));
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