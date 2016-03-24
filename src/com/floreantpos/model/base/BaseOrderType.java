package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the ORDER_TYPE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="ORDER_TYPE"
 */

public abstract class BaseOrderType  implements Comparable, Serializable {

	public static String REF = "OrderType";
	public static String PROP_CLOSE_ON_PAID = "closeOnPaid";
	public static String PROP_SHOW_TABLE_SELECTION = "showTableSelection";
	public static String PROP_SHOW_GUEST_SELECTION = "showGuestSelection";
	public static String PROP_SHOULD_PRINT_TO_KITCHEN = "shouldPrintToKitchen";
	public static String PROP_REQUIRED_DELIVERY_DATA = "requiredDeliveryData";
	public static String PROP_SHOW_ITEM_BARCODE = "showItemBarcode";
	public static String PROP_HAS_FOR_HERE_AND_TO_GO = "hasForHereAndToGo";
	public static String PROP_ENABLED = "enabled";
	public static String PROP_BAR_TAB = "barTab";
	public static String PROP_NAME = "name";
	public static String PROP_SHOW_IN_LOGIN_SCREEN = "showInLoginScreen";
	public static String PROP_PREPAID = "prepaid";
	public static String PROP_ASSIGN_DRIVER = "assignDriver";
	public static String PROP_REQUIRED_CUSTOMER_DATA = "requiredCustomerData";
	public static String PROP_HIDE_ITEM_WITH_EMPTY_INVENTORY = "hideItemWithEmptyInventory";
	public static String PROP_ID = "id";
	public static String PROP_CONSOLIDATE_ITEMS_IN_RECEIPT = "consolidateItemsInReceipt";


	// constructors
	public BaseOrderType () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseOrderType (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseOrderType (
		java.lang.Integer id,
		java.lang.String name) {

		this.setId(id);
		this.setName(name);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String name;
		protected java.lang.Boolean enabled;
		protected java.lang.Boolean showTableSelection;
		protected java.lang.Boolean showGuestSelection;
		protected java.lang.Boolean shouldPrintToKitchen;
		protected java.lang.Boolean prepaid;
		protected java.lang.Boolean closeOnPaid;
		protected java.lang.Boolean requiredCustomerData;
		protected java.lang.Boolean requiredDeliveryData;
		protected java.lang.Boolean assignDriver;
		protected java.lang.Boolean showItemBarcode;
		protected java.lang.Boolean showInLoginScreen;
		protected java.lang.Boolean consolidateItemsInReceipt;
		protected java.lang.Boolean hideItemWithEmptyInventory;
		protected java.lang.Boolean hasForHereAndToGo;
		protected java.lang.Boolean barTab;

	// collections
	private java.util.List<String> orderSubTypes;



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
	 * Return the value associated with the column: ENABLED
	 */
	public java.lang.Boolean isEnabled () {
								return enabled == null ? Boolean.FALSE : enabled;
					}

	/**
	 * Set the value related to the column: ENABLED
	 * @param enabled the ENABLED value
	 */
	public void setEnabled (java.lang.Boolean enabled) {
		this.enabled = enabled;
	}



	/**
	 * Return the value associated with the column: SHOW_TABLE_SELECTION
	 */
	public java.lang.Boolean isShowTableSelection () {
								return showTableSelection == null ? Boolean.FALSE : showTableSelection;
					}

	/**
	 * Set the value related to the column: SHOW_TABLE_SELECTION
	 * @param showTableSelection the SHOW_TABLE_SELECTION value
	 */
	public void setShowTableSelection (java.lang.Boolean showTableSelection) {
		this.showTableSelection = showTableSelection;
	}



	/**
	 * Return the value associated with the column: SHOW_GUEST_SELECTION
	 */
	public java.lang.Boolean isShowGuestSelection () {
								return showGuestSelection == null ? Boolean.FALSE : showGuestSelection;
					}

	/**
	 * Set the value related to the column: SHOW_GUEST_SELECTION
	 * @param showGuestSelection the SHOW_GUEST_SELECTION value
	 */
	public void setShowGuestSelection (java.lang.Boolean showGuestSelection) {
		this.showGuestSelection = showGuestSelection;
	}



	/**
	 * Return the value associated with the column: SHOULD_PRINT_TO_KITCHEN
	 */
	public java.lang.Boolean isShouldPrintToKitchen () {
								return shouldPrintToKitchen == null ? Boolean.FALSE : shouldPrintToKitchen;
					}

	/**
	 * Set the value related to the column: SHOULD_PRINT_TO_KITCHEN
	 * @param shouldPrintToKitchen the SHOULD_PRINT_TO_KITCHEN value
	 */
	public void setShouldPrintToKitchen (java.lang.Boolean shouldPrintToKitchen) {
		this.shouldPrintToKitchen = shouldPrintToKitchen;
	}



	/**
	 * Return the value associated with the column: PREPAID
	 */
	public java.lang.Boolean isPrepaid () {
								return prepaid == null ? Boolean.FALSE : prepaid;
					}

	/**
	 * Set the value related to the column: PREPAID
	 * @param prepaid the PREPAID value
	 */
	public void setPrepaid (java.lang.Boolean prepaid) {
		this.prepaid = prepaid;
	}



	/**
	 * Return the value associated with the column: CLOSE_ON_PAID
	 */
	public java.lang.Boolean isCloseOnPaid () {
								return closeOnPaid == null ? Boolean.FALSE : closeOnPaid;
					}

	/**
	 * Set the value related to the column: CLOSE_ON_PAID
	 * @param closeOnPaid the CLOSE_ON_PAID value
	 */
	public void setCloseOnPaid (java.lang.Boolean closeOnPaid) {
		this.closeOnPaid = closeOnPaid;
	}



	/**
	 * Return the value associated with the column: REQUIRED_CUSTOMER_DATA
	 */
	public java.lang.Boolean isRequiredCustomerData () {
								return requiredCustomerData == null ? Boolean.FALSE : requiredCustomerData;
					}

	/**
	 * Set the value related to the column: REQUIRED_CUSTOMER_DATA
	 * @param requiredCustomerData the REQUIRED_CUSTOMER_DATA value
	 */
	public void setRequiredCustomerData (java.lang.Boolean requiredCustomerData) {
		this.requiredCustomerData = requiredCustomerData;
	}



	/**
	 * Return the value associated with the column: REQUIRED_DELIVERY_DATA
	 */
	public java.lang.Boolean isRequiredDeliveryData () {
								return requiredDeliveryData == null ? Boolean.FALSE : requiredDeliveryData;
					}

	/**
	 * Set the value related to the column: REQUIRED_DELIVERY_DATA
	 * @param requiredDeliveryData the REQUIRED_DELIVERY_DATA value
	 */
	public void setRequiredDeliveryData (java.lang.Boolean requiredDeliveryData) {
		this.requiredDeliveryData = requiredDeliveryData;
	}



	/**
	 * Return the value associated with the column: ASSIGN_DRIVER
	 */
	public java.lang.Boolean isAssignDriver () {
								return assignDriver == null ? Boolean.FALSE : assignDriver;
					}

	/**
	 * Set the value related to the column: ASSIGN_DRIVER
	 * @param assignDriver the ASSIGN_DRIVER value
	 */
	public void setAssignDriver (java.lang.Boolean assignDriver) {
		this.assignDriver = assignDriver;
	}



	/**
	 * Return the value associated with the column: SHOW_ITEM_BARCODE
	 */
	public java.lang.Boolean isShowItemBarcode () {
								return showItemBarcode == null ? Boolean.FALSE : showItemBarcode;
					}

	/**
	 * Set the value related to the column: SHOW_ITEM_BARCODE
	 * @param showItemBarcode the SHOW_ITEM_BARCODE value
	 */
	public void setShowItemBarcode (java.lang.Boolean showItemBarcode) {
		this.showItemBarcode = showItemBarcode;
	}



	/**
	 * Return the value associated with the column: SHOW_IN_LOGIN_SCREEN
	 */
	public java.lang.Boolean isShowInLoginScreen () {
								return showInLoginScreen == null ? Boolean.FALSE : showInLoginScreen;
					}

	/**
	 * Set the value related to the column: SHOW_IN_LOGIN_SCREEN
	 * @param showInLoginScreen the SHOW_IN_LOGIN_SCREEN value
	 */
	public void setShowInLoginScreen (java.lang.Boolean showInLoginScreen) {
		this.showInLoginScreen = showInLoginScreen;
	}



	/**
	 * Return the value associated with the column: CONSOLIDATE_TIEMS_IN_RECEIPT
	 */
	public java.lang.Boolean isConsolidateItemsInReceipt () {
								return consolidateItemsInReceipt == null ? Boolean.FALSE : consolidateItemsInReceipt;
					}

	/**
	 * Set the value related to the column: CONSOLIDATE_TIEMS_IN_RECEIPT
	 * @param consolidateItemsInReceipt the CONSOLIDATE_TIEMS_IN_RECEIPT value
	 */
	public void setConsolidateItemsInReceipt (java.lang.Boolean consolidateItemsInReceipt) {
		this.consolidateItemsInReceipt = consolidateItemsInReceipt;
	}



	/**
	 * Return the value associated with the column: HIDE_ITEM_WITH_EMPTY_INVENTORY
	 */
	public java.lang.Boolean isHideItemWithEmptyInventory () {
								return hideItemWithEmptyInventory == null ? Boolean.FALSE : hideItemWithEmptyInventory;
					}

	/**
	 * Set the value related to the column: HIDE_ITEM_WITH_EMPTY_INVENTORY
	 * @param hideItemWithEmptyInventory the HIDE_ITEM_WITH_EMPTY_INVENTORY value
	 */
	public void setHideItemWithEmptyInventory (java.lang.Boolean hideItemWithEmptyInventory) {
		this.hideItemWithEmptyInventory = hideItemWithEmptyInventory;
	}



	/**
	 * Return the value associated with the column: HAS_FORHERE_AND_TOGO
	 */
	public java.lang.Boolean isHasForHereAndToGo () {
								return hasForHereAndToGo == null ? Boolean.FALSE : hasForHereAndToGo;
					}

	/**
	 * Set the value related to the column: HAS_FORHERE_AND_TOGO
	 * @param hasForHereAndToGo the HAS_FORHERE_AND_TOGO value
	 */
	public void setHasForHereAndToGo (java.lang.Boolean hasForHereAndToGo) {
		this.hasForHereAndToGo = hasForHereAndToGo;
	}



	/**
	 * Return the value associated with the column: BAR_TAB
	 */
	public java.lang.Boolean isBarTab () {
								return barTab == null ? Boolean.FALSE : barTab;
					}

	/**
	 * Set the value related to the column: BAR_TAB
	 * @param barTab the BAR_TAB value
	 */
	public void setBarTab (java.lang.Boolean barTab) {
		this.barTab = barTab;
	}



	/**
	 * Return the value associated with the column: orderSubTypes
	 */
	public java.util.List<String> getOrderSubTypes () {
					return orderSubTypes;
			}

	/**
	 * Set the value related to the column: orderSubTypes
	 * @param orderSubTypes the orderSubTypes value
	 */
	public void setOrderSubTypes (java.util.List<String> orderSubTypes) {
		this.orderSubTypes = orderSubTypes;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.OrderType)) return false;
		else {
			com.floreantpos.model.OrderType orderType = (com.floreantpos.model.OrderType) obj;
			if (null == this.getId() || null == orderType.getId()) return false;
			else return (this.getId().equals(orderType.getId()));
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