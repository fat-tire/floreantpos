package com.floreantpos.model.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the TICKET_ITEM table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TICKET_ITEM"
 */

public abstract class BaseTicketItem implements Comparable, Serializable {

	public static String REF = "TicketItem";
	public static String PROP_DISCOUNT_AMOUNT = "discountAmount";
	public static String PROP_TAX_AMOUNT = "taxAmount";
	public static String PROP_SUBTOTAL_AMOUNT = "subtotalAmount";
	public static String PROP_ITEM_ID = "itemId";
	public static String PROP_DISCOUNT_RATE = "discountRate";
	public static String PROP_CATEGORY_NAME = "categoryName";
	public static String PROP_HAS_MODIFIERS = "hasModifiers";
	public static String PROP_SHOULD_PRINT_TO_KITCHEN = "shouldPrintToKitchen";
	public static String PROP_SUBTOTAL_AMOUNT_WITHOUT_MODIFIERS = "subtotalAmountWithoutModifiers";
	public static String PROP_ITEM_COUNT = "itemCount";
	public static String PROP_GROUP_NAME = "groupName";
	public static String PROP_TICKET = "ticket";
	public static String PROP_TAX_RATE = "taxRate";
	public static String PROP_TAX_AMOUNT_WITHOUT_MODIFIERS = "taxAmountWithoutModifiers";
	public static String PROP_PRINTED_TO_KITCHEN = "printedToKitchen";
	public static String PROP_TOTAL_AMOUNT = "totalAmount";
	public static String PROP_NAME = "name";
	public static String PROP_UNIT_PRICE = "unitPrice";
	public static String PROP_ID = "id";
	public static String PROP_BEVERAGE = "beverage";
	public static String PROP_TOTAL_AMOUNT_WITHOUT_MODIFIERS = "totalAmountWithoutModifiers";

	// constructors
	public BaseTicketItem() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTicketItem(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseTicketItem(java.lang.Integer id, com.floreantpos.model.Ticket ticket) {

		this.setId(id);
		this.setTicket(ticket);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	java.util.Date modifiedTime;

	// fields
	private java.lang.Integer itemId;
	private java.lang.Integer itemCount;
	private java.lang.String name;
	private java.lang.String groupName;
	private java.lang.String categoryName;
	private java.lang.Double unitPrice;
	private java.lang.Double discountRate;
	private java.lang.Double taxRate;
	private java.lang.Double subtotalAmount;
	private java.lang.Double subtotalAmountWithoutModifiers;
	private java.lang.Double discountAmount;
	private java.lang.Double taxAmount;
	private java.lang.Double taxAmountWithoutModifiers;
	private java.lang.Double totalAmount;
	private java.lang.Double totalAmountWithoutModifiers;
	private java.lang.Boolean beverage;
	private java.lang.Boolean shouldPrintToKitchen;
	private java.lang.Boolean hasModifiers;
	private java.lang.Boolean printedToKitchen;

	// many to one
	private com.floreantpos.model.Ticket ticket;

	// collections
	private java.util.List<com.floreantpos.model.TicketItemModifierGroup> ticketItemModifierGroups;

	/**
	 * Return the unique identifier of this class
	 * @hibernate.id
	 *  generator-class="identity"
	 *  column="ID"
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: MODIFIED_TIME
	 */
	public java.util.Date getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * Set the value related to the column: MODIFIED_TIME
	 * @param modifiedTime the MODIFIED_TIME value
	 */
	public void setModifiedTime(java.util.Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	/**
	 * Return the value associated with the column: ITEM_ID
	 */
	public java.lang.Integer getItemId() {
		return itemId == null ? Integer.valueOf(0) : itemId;
	}

	/**
	 * Set the value related to the column: ITEM_ID
	 * @param itemId the ITEM_ID value
	 */
	public void setItemId(java.lang.Integer itemId) {
		this.itemId = itemId;
	}

	/**
	 * Return the value associated with the column: ITEM_COUNT
	 */
	public java.lang.Integer getItemCount() {
		return itemCount == null ? Integer.valueOf(0) : itemCount;
	}

	/**
	 * Set the value related to the column: ITEM_COUNT
	 * @param itemCount the ITEM_COUNT value
	 */
	public void setItemCount(java.lang.Integer itemCount) {
		this.itemCount = itemCount;
	}

	/**
	 * Return the value associated with the column: ITEM_NAME
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Set the value related to the column: ITEM_NAME
	 * @param name the ITEM_NAME value
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Return the value associated with the column: GROUP_NAME
	 */
	public java.lang.String getGroupName() {
		return groupName;
	}

	/**
	 * Set the value related to the column: GROUP_NAME
	 * @param groupName the GROUP_NAME value
	 */
	public void setGroupName(java.lang.String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Return the value associated with the column: CATEGORY_NAME
	 */
	public java.lang.String getCategoryName() {
		return categoryName;
	}

	/**
	 * Set the value related to the column: CATEGORY_NAME
	 * @param categoryName the CATEGORY_NAME value
	 */
	public void setCategoryName(java.lang.String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * Return the value associated with the column: ITEM_PRICE
	 */
	public java.lang.Double getUnitPrice() {
		return unitPrice == null ? Double.valueOf(0) : unitPrice;
	}

	/**
	 * Set the value related to the column: ITEM_PRICE
	 * @param unitPrice the ITEM_PRICE value
	 */
	public void setUnitPrice(java.lang.Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * Return the value associated with the column: DISCOUNT_RATE
	 */
	public java.lang.Double getDiscountRate() {
		return discountRate == null ? Double.valueOf(0) : discountRate;
	}

	/**
	 * Set the value related to the column: DISCOUNT_RATE
	 * @param discountRate the DISCOUNT_RATE value
	 */
	public void setDiscountRate(java.lang.Double discountRate) {
		this.discountRate = discountRate;
	}

	/**
	 * Return the value associated with the column: ITEM_TAX_RATE
	 */
	public java.lang.Double getTaxRate() {
		return taxRate == null ? Double.valueOf(0) : taxRate;
	}

	/**
	 * Set the value related to the column: ITEM_TAX_RATE
	 * @param taxRate the ITEM_TAX_RATE value
	 */
	public void setTaxRate(java.lang.Double taxRate) {
		this.taxRate = taxRate;
	}

	/**
	 * Return the value associated with the column: SUB_TOTAL
	 */
	public java.lang.Double getSubtotalAmount() {
		return subtotalAmount == null ? Double.valueOf(0) : subtotalAmount;
	}

	/**
	 * Set the value related to the column: SUB_TOTAL
	 * @param subtotalAmount the SUB_TOTAL value
	 */
	public void setSubtotalAmount(java.lang.Double subtotalAmount) {
		this.subtotalAmount = subtotalAmount;
	}

	/**
	 * Return the value associated with the column: SUB_TOTAL_WITH_MODIFIERS
	 */
	public java.lang.Double getSubtotalAmountWithoutModifiers() {
		return subtotalAmountWithoutModifiers == null ? Double.valueOf(0)
				: subtotalAmountWithoutModifiers;
	}

	/**
	 * Set the value related to the column: SUB_TOTAL_WITH_MODIFIERS
	 * @param subtotalAmountWithoutModifiers the SUB_TOTAL_WITH_MODIFIERS value
	 */
	public void setSubtotalAmountWithoutModifiers(
			java.lang.Double subtotalAmountWithoutModifiers) {
		this.subtotalAmountWithoutModifiers = subtotalAmountWithoutModifiers;
	}

	/**
	 * Return the value associated with the column: DISCOUNT
	 */
	public java.lang.Double getDiscountAmount() {
		return discountAmount == null ? Double.valueOf(0) : discountAmount;
	}

	/**
	 * Set the value related to the column: DISCOUNT
	 * @param discountAmount the DISCOUNT value
	 */
	public void setDiscountAmount(java.lang.Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	/**
	 * Return the value associated with the column: TAX_AMOUNT
	 */
	public java.lang.Double getTaxAmount() {
		return taxAmount == null ? Double.valueOf(0) : taxAmount;
	}

	/**
	 * Set the value related to the column: TAX_AMOUNT
	 * @param taxAmount the TAX_AMOUNT value
	 */
	public void setTaxAmount(java.lang.Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	/**
	 * Return the value associated with the column: TAX_AMOUNT_WITH_MODIFIERS
	 */
	public java.lang.Double getTaxAmountWithoutModifiers() {
		return taxAmountWithoutModifiers == null ? Double.valueOf(0)
				: taxAmountWithoutModifiers;
	}

	/**
	 * Set the value related to the column: TAX_AMOUNT_WITH_MODIFIERS
	 * @param taxAmountWithoutModifiers the TAX_AMOUNT_WITH_MODIFIERS value
	 */
	public void setTaxAmountWithoutModifiers(
			java.lang.Double taxAmountWithoutModifiers) {
		this.taxAmountWithoutModifiers = taxAmountWithoutModifiers;
	}

	/**
	 * Return the value associated with the column: TOTAL_PRICE
	 */
	public java.lang.Double getTotalAmount() {
		return totalAmount == null ? Double.valueOf(0) : totalAmount;
	}

	/**
	 * Set the value related to the column: TOTAL_PRICE
	 * @param totalAmount the TOTAL_PRICE value
	 */
	public void setTotalAmount(java.lang.Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * Return the value associated with the column: TOTAL_PRICE_WITH_MODIFIERS
	 */
	public java.lang.Double getTotalAmountWithoutModifiers() {
		return totalAmountWithoutModifiers == null ? Double.valueOf(0)
				: totalAmountWithoutModifiers;
	}

	/**
	 * Set the value related to the column: TOTAL_PRICE_WITH_MODIFIERS
	 * @param totalAmountWithoutModifiers the TOTAL_PRICE_WITH_MODIFIERS value
	 */
	public void setTotalAmountWithoutModifiers(
			java.lang.Double totalAmountWithoutModifiers) {
		this.totalAmountWithoutModifiers = totalAmountWithoutModifiers;
	}

	/**
	 * Return the value associated with the column: BEVERAGE
	 */
	public java.lang.Boolean isBeverage() {
		return beverage == null ? Boolean.FALSE : beverage;
	}

	/**
	 * Set the value related to the column: BEVERAGE
	 * @param beverage the BEVERAGE value
	 */
	public void setBeverage(java.lang.Boolean beverage) {
		this.beverage = beverage;
	}

	/**
	 * Return the value associated with the column: PRINT_TO_KITCHEN
	 */
	public java.lang.Boolean isShouldPrintToKitchen() {
		return shouldPrintToKitchen == null ? Boolean.valueOf(true)
				: shouldPrintToKitchen;
	}

	/**
	 * Set the value related to the column: PRINT_TO_KITCHEN
	 * @param shouldPrintToKitchen the PRINT_TO_KITCHEN value
	 */
	public void setShouldPrintToKitchen(java.lang.Boolean shouldPrintToKitchen) {
		this.shouldPrintToKitchen = shouldPrintToKitchen;
	}

	/**
	 * Custom property
	 */
	public static String getShouldPrintToKitchenDefaultValue() {
		return "true";
	}

	/**
	 * Return the value associated with the column: HAS_MODIIERS
	 */
	public java.lang.Boolean isHasModifiers() {
		return hasModifiers == null ? Boolean.FALSE : hasModifiers;
	}

	/**
	 * Set the value related to the column: HAS_MODIIERS
	 * @param hasModifiers the HAS_MODIIERS value
	 */
	public void setHasModifiers(java.lang.Boolean hasModifiers) {
		this.hasModifiers = hasModifiers;
	}

	/**
	 * Return the value associated with the column: PRINTED_TO_KITCHEN
	 */
	public java.lang.Boolean isPrintedToKitchen() {
		return printedToKitchen == null ? Boolean.FALSE : printedToKitchen;
	}

	/**
	 * Set the value related to the column: PRINTED_TO_KITCHEN
	 * @param printedToKitchen the PRINTED_TO_KITCHEN value
	 */
	public void setPrintedToKitchen(java.lang.Boolean printedToKitchen) {
		this.printedToKitchen = printedToKitchen;
	}

	/**
	 * Return the value associated with the column: TICKET_ID
	 */
	public com.floreantpos.model.Ticket getTicket() {
		return ticket;
	}

	/**
	 * Set the value related to the column: TICKET_ID
	 * @param ticket the TICKET_ID value
	 */
	public void setTicket(com.floreantpos.model.Ticket ticket) {
		this.ticket = ticket;
	}

	/**
	 * Return the value associated with the column: ticketItemModifierGroups
	 */
	public java.util.List<com.floreantpos.model.TicketItemModifierGroup> getTicketItemModifierGroups() {
		return ticketItemModifierGroups;
	}

	/**
	 * Set the value related to the column: ticketItemModifierGroups
	 * @param ticketItemModifierGroups the ticketItemModifierGroups value
	 */
	public void setTicketItemModifierGroups(
			java.util.List<com.floreantpos.model.TicketItemModifierGroup> ticketItemModifierGroups) {
		this.ticketItemModifierGroups = ticketItemModifierGroups;
	}

	public void addToticketItemModifierGroups(
			com.floreantpos.model.TicketItemModifierGroup ticketItemModifierGroup) {
		if (null == getTicketItemModifierGroups())
			setTicketItemModifierGroups(new java.util.ArrayList<com.floreantpos.model.TicketItemModifierGroup>());
		getTicketItemModifierGroups().add(ticketItemModifierGroup);
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.floreantpos.model.TicketItem))
			return false;
		else {
			com.floreantpos.model.TicketItem ticketItem = (com.floreantpos.model.TicketItem) obj;
			if (null == this.getId() || null == ticketItem.getId())
				return false;
			else
				return (this.getId().equals(ticketItem.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":"
						+ this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo(Object obj) {
		if (obj.hashCode() > hashCode())
			return 1;
		else if (obj.hashCode() < hashCode())
			return -1;
		else
			return 0;
	}

	public String toString() {
		return super.toString();
	}

}