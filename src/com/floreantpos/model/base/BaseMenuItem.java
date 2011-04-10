package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the MENU_ITEM table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MENU_ITEM"
 */

public abstract class BaseMenuItem  implements Comparable, Serializable {

	public static String REF = "MenuItem";
	public static String PROP_PRICE = "price";
	public static String PROP_VISIBLE = "visible";
	public static String PROP_PARENT = "parent";
	public static String PROP_DISCOUNT_RATE = "discountRate";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";
	public static String PROP_TAX = "tax";


	// constructors
	public BaseMenuItem () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMenuItem (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseMenuItem (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Double price) {

		this.setId(id);
		this.setName(name);
		this.setPrice(price);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	 java.util.Date modifiedTime;

	// fields
	private java.lang.String name;
	private java.lang.Double price;
	private java.lang.Double discountRate;
	private java.lang.Boolean visible;

	// many to one
	private com.floreantpos.model.MenuGroup parent;
	private com.floreantpos.model.Tax tax;

	// collections
	private java.util.List<com.floreantpos.model.MenuItemShift> shifts;
	private java.util.List<com.floreantpos.model.MenuItemModifierGroup> menuItemModiferGroups;



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
	 * Return the value associated with the column: DISCOUNT_RATE
	 */
	public java.lang.Double getDiscountRate () {
					return discountRate == null ? Double.valueOf(0) : discountRate;
			}

	/**
	 * Set the value related to the column: DISCOUNT_RATE
	 * @param discountRate the DISCOUNT_RATE value
	 */
	public void setDiscountRate (java.lang.Double discountRate) {
		this.discountRate = discountRate;
	}



	/**
	 * Return the value associated with the column: VISIBLE
	 */
	public java.lang.Boolean isVisible () {
					return visible == null ? Boolean.valueOf(true) : visible;
			}

	/**
	 * Set the value related to the column: VISIBLE
	 * @param visible the VISIBLE value
	 */
	public void setVisible (java.lang.Boolean visible) {
		this.visible = visible;
	}


	/**
	 * Custom property
	 */
	public static String getVisibleDefaultValue () {
		return "true";
	}


	/**
	 * Return the value associated with the column: GROUP_ID
	 */
	public com.floreantpos.model.MenuGroup getParent () {
			return parent;
	}

	/**
	 * Set the value related to the column: GROUP_ID
	 * @param parent the GROUP_ID value
	 */
	public void setParent (com.floreantpos.model.MenuGroup parent) {
		this.parent = parent;
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



	/**
	 * Return the value associated with the column: shifts
	 */
	public java.util.List<com.floreantpos.model.MenuItemShift> getShifts () {
			return shifts;
	}

	/**
	 * Set the value related to the column: shifts
	 * @param shifts the shifts value
	 */
	public void setShifts (java.util.List<com.floreantpos.model.MenuItemShift> shifts) {
		this.shifts = shifts;
	}

	public void addToshifts (com.floreantpos.model.MenuItemShift menuItemShift) {
		if (null == getShifts()) setShifts(new java.util.ArrayList<com.floreantpos.model.MenuItemShift>());
		getShifts().add(menuItemShift);
	}



	/**
	 * Return the value associated with the column: menuItemModiferGroups
	 */
	public java.util.List<com.floreantpos.model.MenuItemModifierGroup> getMenuItemModiferGroups () {
			return menuItemModiferGroups;
	}

	/**
	 * Set the value related to the column: menuItemModiferGroups
	 * @param menuItemModiferGroups the menuItemModiferGroups value
	 */
	public void setMenuItemModiferGroups (java.util.List<com.floreantpos.model.MenuItemModifierGroup> menuItemModiferGroups) {
		this.menuItemModiferGroups = menuItemModiferGroups;
	}

	public void addTomenuItemModiferGroups (com.floreantpos.model.MenuItemModifierGroup menuItemModifierGroup) {
		if (null == getMenuItemModiferGroups()) setMenuItemModiferGroups(new java.util.ArrayList<com.floreantpos.model.MenuItemModifierGroup>());
		getMenuItemModiferGroups().add(menuItemModifierGroup);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.MenuItem)) return false;
		else {
			com.floreantpos.model.MenuItem menuItem = (com.floreantpos.model.MenuItem) obj;
			if (null == this.getId() || null == menuItem.getId()) return false;
			else return (this.getId().equals(menuItem.getId()));
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