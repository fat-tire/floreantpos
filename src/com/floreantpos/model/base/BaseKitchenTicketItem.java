package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the KITCHEN_TICKET_ITEM table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="KITCHEN_TICKET_ITEM"
 */

public abstract class BaseKitchenTicketItem  implements Comparable, Serializable {

	public static String REF = "KitchenTicketItem";
	public static String PROP_UNIT_NAME = "unitName";
	public static String PROP_TICKET_ITEM_ID = "ticketItemId";
	public static String PROP_QUANTITY = "quantity";
	public static String PROP_SORT_ORDER = "sortOrder";
	public static String PROP_MENU_ITEM_GROUP_NAME = "menuItemGroupName";
	public static String PROP_COOKABLE = "cookable";
	public static String PROP_TICKET_ITEM_MODIFIER_ID = "ticketItemModifierId";
	public static String PROP_FRACTIONAL_UNIT = "fractionalUnit";
	public static String PROP_STATUS = "status";
	public static String PROP_MENU_ITEM_GROUP_ID = "menuItemGroupId";
	public static String PROP_FRACTIONAL_QUANTITY = "fractionalQuantity";
	public static String PROP_ID = "id";
	public static String PROP_VOIDED = "voided";
	public static String PROP_MENU_ITEM_NAME = "menuItemName";
	public static String PROP_MENU_ITEM_CODE = "menuItemCode";


	// constructors
	public BaseKitchenTicketItem () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseKitchenTicketItem (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.Boolean cookable;
		protected java.lang.Integer ticketItemId;
		protected java.lang.Integer ticketItemModifierId;
		protected java.lang.String menuItemCode;
		protected java.lang.String menuItemName;
		protected java.lang.Integer menuItemGroupId;
		protected java.lang.String menuItemGroupName;
		protected java.lang.Integer quantity;
		protected java.lang.Double fractionalQuantity;
		protected java.lang.Boolean fractionalUnit;
		protected java.lang.String unitName;
		protected java.lang.Integer sortOrder;
		protected java.lang.Boolean voided;
		protected java.lang.String status;



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
	 * Return the value associated with the column: COOKABLE
	 */
	public java.lang.Boolean isCookable () {
								return cookable == null ? Boolean.FALSE : cookable;
					}

	/**
	 * Set the value related to the column: COOKABLE
	 * @param cookable the COOKABLE value
	 */
	public void setCookable (java.lang.Boolean cookable) {
		this.cookable = cookable;
	}



	/**
	 * Return the value associated with the column: TICKET_ITEM_ID
	 */
	public java.lang.Integer getTicketItemId () {
									return ticketItemId == null ? Integer.valueOf(0) : ticketItemId;
					}

	/**
	 * Set the value related to the column: TICKET_ITEM_ID
	 * @param ticketItemId the TICKET_ITEM_ID value
	 */
	public void setTicketItemId (java.lang.Integer ticketItemId) {
		this.ticketItemId = ticketItemId;
	}



	/**
	 * Return the value associated with the column: TICKET_ITEM_MODIFIER_ID
	 */
	public java.lang.Integer getTicketItemModifierId () {
									return ticketItemModifierId == null ? Integer.valueOf(0) : ticketItemModifierId;
					}

	/**
	 * Set the value related to the column: TICKET_ITEM_MODIFIER_ID
	 * @param ticketItemModifierId the TICKET_ITEM_MODIFIER_ID value
	 */
	public void setTicketItemModifierId (java.lang.Integer ticketItemModifierId) {
		this.ticketItemModifierId = ticketItemModifierId;
	}



	/**
	 * Return the value associated with the column: MENU_ITEM_CODE
	 */
	public java.lang.String getMenuItemCode () {
					return menuItemCode;
			}

	/**
	 * Set the value related to the column: MENU_ITEM_CODE
	 * @param menuItemCode the MENU_ITEM_CODE value
	 */
	public void setMenuItemCode (java.lang.String menuItemCode) {
		this.menuItemCode = menuItemCode;
	}



	/**
	 * Return the value associated with the column: MENU_ITEM_NAME
	 */
	public java.lang.String getMenuItemName () {
					return menuItemName;
			}

	/**
	 * Set the value related to the column: MENU_ITEM_NAME
	 * @param menuItemName the MENU_ITEM_NAME value
	 */
	public void setMenuItemName (java.lang.String menuItemName) {
		this.menuItemName = menuItemName;
	}



	/**
	 * Return the value associated with the column: MENU_ITEM_GROUP_ID
	 */
	public java.lang.Integer getMenuItemGroupId () {
									return menuItemGroupId == null ? Integer.valueOf(0) : menuItemGroupId;
					}

	/**
	 * Set the value related to the column: MENU_ITEM_GROUP_ID
	 * @param menuItemGroupId the MENU_ITEM_GROUP_ID value
	 */
	public void setMenuItemGroupId (java.lang.Integer menuItemGroupId) {
		this.menuItemGroupId = menuItemGroupId;
	}



	/**
	 * Return the value associated with the column: MENU_ITEM_GROUP_NAME
	 */
	public java.lang.String getMenuItemGroupName () {
					return menuItemGroupName;
			}

	/**
	 * Set the value related to the column: MENU_ITEM_GROUP_NAME
	 * @param menuItemGroupName the MENU_ITEM_GROUP_NAME value
	 */
	public void setMenuItemGroupName (java.lang.String menuItemGroupName) {
		this.menuItemGroupName = menuItemGroupName;
	}



	/**
	 * Return the value associated with the column: QUANTITY
	 */
	public java.lang.Integer getQuantity () {
									return quantity == null ? Integer.valueOf(0) : quantity;
					}

	/**
	 * Set the value related to the column: QUANTITY
	 * @param quantity the QUANTITY value
	 */
	public void setQuantity (java.lang.Integer quantity) {
		this.quantity = quantity;
	}



	/**
	 * Return the value associated with the column: FRACTIONAL_QUANTITY
	 */
	public java.lang.Double getFractionalQuantity () {
									return fractionalQuantity == null ? Double.valueOf(0) : fractionalQuantity;
					}

	/**
	 * Set the value related to the column: FRACTIONAL_QUANTITY
	 * @param fractionalQuantity the FRACTIONAL_QUANTITY value
	 */
	public void setFractionalQuantity (java.lang.Double fractionalQuantity) {
		this.fractionalQuantity = fractionalQuantity;
	}



	/**
	 * Return the value associated with the column: FRACTIONAL_UNIT
	 */
	public java.lang.Boolean isFractionalUnit () {
								return fractionalUnit == null ? Boolean.FALSE : fractionalUnit;
					}

	/**
	 * Set the value related to the column: FRACTIONAL_UNIT
	 * @param fractionalUnit the FRACTIONAL_UNIT value
	 */
	public void setFractionalUnit (java.lang.Boolean fractionalUnit) {
		this.fractionalUnit = fractionalUnit;
	}



	/**
	 * Return the value associated with the column: UNIT_NAME
	 */
	public java.lang.String getUnitName () {
					return unitName;
			}

	/**
	 * Set the value related to the column: UNIT_NAME
	 * @param unitName the UNIT_NAME value
	 */
	public void setUnitName (java.lang.String unitName) {
		this.unitName = unitName;
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
	 * Return the value associated with the column: VOIDED
	 */
	public java.lang.Boolean isVoided () {
								return voided == null ? Boolean.FALSE : voided;
					}

	/**
	 * Set the value related to the column: VOIDED
	 * @param voided the VOIDED value
	 */
	public void setVoided (java.lang.Boolean voided) {
		this.voided = voided;
	}



	/**
	 * Return the value associated with the column: STATUS
	 */
	public java.lang.String getStatus () {
					return status;
			}

	/**
	 * Set the value related to the column: STATUS
	 * @param status the STATUS value
	 */
	public void setStatus (java.lang.String status) {
		this.status = status;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.KitchenTicketItem)) return false;
		else {
			com.floreantpos.model.KitchenTicketItem kitchenTicketItem = (com.floreantpos.model.KitchenTicketItem) obj;
			if (null == this.getId() || null == kitchenTicketItem.getId()) return false;
			else return (this.getId().equals(kitchenTicketItem.getId()));
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