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

	public static String REF = "KitchenTicketItem"; //$NON-NLS-1$
	public static String PROP_STATUS = "status"; //$NON-NLS-1$
	public static String PROP_QUANTITY = "quantity"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_VOIDED = "voided"; //$NON-NLS-1$
	public static String PROP_MENU_ITEM_NAME = "menuItemName"; //$NON-NLS-1$
	public static String PROP_MENU_ITEM_CODE = "menuItemCode"; //$NON-NLS-1$
	public static String PROP_COOKABLE = "cookable"; //$NON-NLS-1$


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
		protected java.lang.String menuItemCode;
		protected java.lang.String menuItemName;
		protected java.lang.Integer quantity;
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
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode(); //$NON-NLS-1$
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