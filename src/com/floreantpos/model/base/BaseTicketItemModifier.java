package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the TICKET_ITEM_MODIFIER table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TICKET_ITEM_MODIFIER"
 */

public abstract class BaseTicketItemModifier  implements Comparable, Serializable {

	public static String REF = "TicketItemModifier";
	public static String PROP_PARENT = "parent";
	public static String PROP_ITEM_ID = "itemId";
	public static String PROP_ITEM_COUNT = "itemCount";
	public static String PROP_TAX_RATE = "taxRate";
	public static String PROP_UNIT_PRICE = "unitPrice";
	public static String PROP_GROUP_ID = "groupId";
	public static String PROP_NAME = "name";
	public static String PROP_SHOULD_PRINT_TO_KITCHEN = "shouldPrintToKitchen";
	public static String PROP_PRINTED_TO_KITCHEN = "printedToKitchen";
	public static String PROP_EXTRA_UNIT_PRICE = "extraUnitPrice";
	public static String PROP_TOTAL_AMOUNT = "totalAmount";
	public static String PROP_ID = "id";
	public static String PROP_MODIFIER_TYPE = "modifierType";


	// constructors
	public BaseTicketItemModifier () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTicketItemModifier (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer itemId;
	private java.lang.Integer groupId;
	private java.lang.Integer itemCount;
	private java.lang.String name;
	private java.lang.Double unitPrice;
	private java.lang.Double extraUnitPrice;
	private java.lang.Double taxRate;
	private java.lang.Integer modifierType;
	private java.lang.Double totalAmount;
	private java.lang.Boolean shouldPrintToKitchen;
	private java.lang.Boolean printedToKitchen;

	// many to one
	private com.floreantpos.model.TicketItemModifierGroup parent;



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
	 * Return the value associated with the column: ITEM_ID
	 */
	public java.lang.Integer getItemId () {
			return itemId == null ? Integer.valueOf(0) : itemId;
	}

	/**
	 * Set the value related to the column: ITEM_ID
	 * @param itemId the ITEM_ID value
	 */
	public void setItemId (java.lang.Integer itemId) {
		this.itemId = itemId;
	}



	/**
	 * Return the value associated with the column: GROUP_ID
	 */
	public java.lang.Integer getGroupId () {
			return groupId == null ? Integer.valueOf(0) : groupId;
	}

	/**
	 * Set the value related to the column: GROUP_ID
	 * @param groupId the GROUP_ID value
	 */
	public void setGroupId (java.lang.Integer groupId) {
		this.groupId = groupId;
	}



	/**
	 * Return the value associated with the column: ITEM_COUNT
	 */
	public java.lang.Integer getItemCount () {
			return itemCount == null ? Integer.valueOf(0) : itemCount;
	}

	/**
	 * Set the value related to the column: ITEM_COUNT
	 * @param itemCount the ITEM_COUNT value
	 */
	public void setItemCount (java.lang.Integer itemCount) {
		this.itemCount = itemCount;
	}



	/**
	 * Return the value associated with the column: MODIFIER_NAME
	 */
	public java.lang.String getName () {
			return name;
	}

	/**
	 * Set the value related to the column: MODIFIER_NAME
	 * @param name the MODIFIER_NAME value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}



	/**
	 * Return the value associated with the column: MODIFIER_PRICE
	 */
	public java.lang.Double getUnitPrice () {
					return unitPrice == null ? Double.valueOf(0) : unitPrice;
			}

	/**
	 * Set the value related to the column: MODIFIER_PRICE
	 * @param unitPrice the MODIFIER_PRICE value
	 */
	public void setUnitPrice (java.lang.Double unitPrice) {
		this.unitPrice = unitPrice;
	}



	/**
	 * Return the value associated with the column: EXTRA_PRICE
	 */
	public java.lang.Double getExtraUnitPrice () {
					return extraUnitPrice == null ? Double.valueOf(0) : extraUnitPrice;
			}

	/**
	 * Set the value related to the column: EXTRA_PRICE
	 * @param extraUnitPrice the EXTRA_PRICE value
	 */
	public void setExtraUnitPrice (java.lang.Double extraUnitPrice) {
		this.extraUnitPrice = extraUnitPrice;
	}



	/**
	 * Return the value associated with the column: MODIFIER_TAX_RATE
	 */
	public java.lang.Double getTaxRate () {
					return taxRate == null ? Double.valueOf(0) : taxRate;
			}

	/**
	 * Set the value related to the column: MODIFIER_TAX_RATE
	 * @param taxRate the MODIFIER_TAX_RATE value
	 */
	public void setTaxRate (java.lang.Double taxRate) {
		this.taxRate = taxRate;
	}



	/**
	 * Return the value associated with the column: MODIFIER_TYPE
	 */
	public java.lang.Integer getModifierType () {
			return modifierType == null ? Integer.valueOf(0) : modifierType;
	}

	/**
	 * Set the value related to the column: MODIFIER_TYPE
	 * @param modifierType the MODIFIER_TYPE value
	 */
	public void setModifierType (java.lang.Integer modifierType) {
		this.modifierType = modifierType;
	}



	/**
	 * Return the value associated with the column: TOTAL_PRICE
	 */
	public java.lang.Double getTotalAmount () {
					return totalAmount == null ? Double.valueOf(0) : totalAmount;
			}

	/**
	 * Set the value related to the column: TOTAL_PRICE
	 * @param totalAmount the TOTAL_PRICE value
	 */
	public void setTotalAmount (java.lang.Double totalAmount) {
		this.totalAmount = totalAmount;
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
	 * Return the value associated with the column: PRINTED_TO_KITCHEN
	 */
	public java.lang.Boolean isPrintedToKitchen () {
					return printedToKitchen == null ? Boolean.FALSE : printedToKitchen;
			}

	/**
	 * Set the value related to the column: PRINTED_TO_KITCHEN
	 * @param printedToKitchen the PRINTED_TO_KITCHEN value
	 */
	public void setPrintedToKitchen (java.lang.Boolean printedToKitchen) {
		this.printedToKitchen = printedToKitchen;
	}



	/**
	 * Return the value associated with the column: ModifierGroup_ID
	 */
	public com.floreantpos.model.TicketItemModifierGroup getParent () {
			return parent;
	}

	/**
	 * Set the value related to the column: ModifierGroup_ID
	 * @param parent the ModifierGroup_ID value
	 */
	public void setParent (com.floreantpos.model.TicketItemModifierGroup parent) {
		this.parent = parent;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.TicketItemModifier)) return false;
		else {
			com.floreantpos.model.TicketItemModifier ticketItemModifier = (com.floreantpos.model.TicketItemModifier) obj;
			if (null == this.getId() || null == ticketItemModifier.getId()) return false;
			else return (this.getId().equals(ticketItemModifier.getId()));
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