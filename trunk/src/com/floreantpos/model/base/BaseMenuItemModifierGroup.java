package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the MENUITEM_MODIFIERGROUP table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MENUITEM_MODIFIERGROUP"
 */

public abstract class BaseMenuItemModifierGroup  implements Comparable, Serializable {

	public static String REF = "MenuItemModifierGroup";
	public static String PROP_MIN_QUANTITY = "minQuantity";
	public static String PROP_SORT_ORDER = "sortOrder";
	public static String PROP_ID = "id";
	public static String PROP_MODIFIER_GROUP = "modifierGroup";
	public static String PROP_MAX_QUANTITY = "maxQuantity";


	// constructors
	public BaseMenuItemModifierGroup () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMenuItemModifierGroup (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.Integer minQuantity;
		protected java.lang.Integer maxQuantity;
		protected java.lang.Integer sortOrder;

	// many to one
	private com.floreantpos.model.MenuModifierGroup modifierGroup;



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
	 * Return the value associated with the column: MIN_QUANTITY
	 */
	public java.lang.Integer getMinQuantity () {
					return minQuantity == null ? Integer.valueOf(0) : minQuantity;
			}

	/**
	 * Set the value related to the column: MIN_QUANTITY
	 * @param minQuantity the MIN_QUANTITY value
	 */
	public void setMinQuantity (java.lang.Integer minQuantity) {
		this.minQuantity = minQuantity;
	}



	/**
	 * Return the value associated with the column: MAX_QUANTITY
	 */
	public java.lang.Integer getMaxQuantity () {
					return maxQuantity == null ? Integer.valueOf(0) : maxQuantity;
			}

	/**
	 * Set the value related to the column: MAX_QUANTITY
	 * @param maxQuantity the MAX_QUANTITY value
	 */
	public void setMaxQuantity (java.lang.Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
	}



	/**
	 * Return the value associated with the column: SORT_ORDER
	 */
	public java.lang.Integer getSortOrder () {
					return sortOrder == null ? Integer.valueOf(0) : sortOrder;
			}

	/**
	 * Set the value related to the column: SORT_ORDER
	 * @param sortOrder the SORT_ORDER value
	 */
	public void setSortOrder (java.lang.Integer sortOrder) {
		this.sortOrder = sortOrder;
	}



	/**
	 * Return the value associated with the column: MODIFIER_GROUP
	 */
	public com.floreantpos.model.MenuModifierGroup getModifierGroup () {
					return modifierGroup;
			}

	/**
	 * Set the value related to the column: MODIFIER_GROUP
	 * @param modifierGroup the MODIFIER_GROUP value
	 */
	public void setModifierGroup (com.floreantpos.model.MenuModifierGroup modifierGroup) {
		this.modifierGroup = modifierGroup;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.MenuItemModifierGroup)) return false;
		else {
			com.floreantpos.model.MenuItemModifierGroup menuItemModifierGroup = (com.floreantpos.model.MenuItemModifierGroup) obj;
			if (null == this.getId() || null == menuItemModifierGroup.getId()) return false;
			else return (this.getId().equals(menuItemModifierGroup.getId()));
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