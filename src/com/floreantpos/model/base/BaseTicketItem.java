package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;

import com.floreantpos.model.TicketItemCookingInstruction;


/**
 * This is an object that contains data related to the TICKET_ITEM table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TICKET_ITEM"
 */

public abstract class BaseTicketItem  implements Comparable, Serializable {

	public static String REF = "TicketItem"; //$NON-NLS-1$
	public static String PROP_BEVERAGE = "beverage"; //$NON-NLS-1$
	public static String PROP_SIZE_MODIFIER = "sizeModifier"; //$NON-NLS-1$
	public static String PROP_TAX_RATE = "taxRate"; //$NON-NLS-1$
	public static String PROP_ITEM_UNIT_NAME = "itemUnitName"; //$NON-NLS-1$
	public static String PROP_DISCOUNT_AMOUNT = "discountAmount"; //$NON-NLS-1$
	public static String PROP_PIZZA_TYPE = "pizzaType"; //$NON-NLS-1$
	public static String PROP_PIZZA_SECTION_MODE_TYPE = "pizzaSectionModeType"; //$NON-NLS-1$
	public static String PROP_SHOULD_PRINT_TO_KITCHEN = "shouldPrintToKitchen"; //$NON-NLS-1$
	public static String PROP_TICKET = "ticket"; //$NON-NLS-1$
	public static String PROP_INVENTORY_HANDLED = "inventoryHandled"; //$NON-NLS-1$
	public static String PROP_STOCK_AMOUNT_ADJUSTED = "stockAmountAdjusted"; //$NON-NLS-1$
	public static String PROP_HAS_MODIFIERS = "hasModifiers"; //$NON-NLS-1$
	public static String PROP_TOTAL_AMOUNT_WITHOUT_MODIFIERS = "totalAmountWithoutModifiers"; //$NON-NLS-1$
	public static String PROP_TREAT_AS_SEAT = "treatAsSeat"; //$NON-NLS-1$
	public static String PROP_ITEM_QUANTITY = "itemQuantity"; //$NON-NLS-1$
	public static String PROP_ITEM_ID = "itemId"; //$NON-NLS-1$
	public static String PROP_CATEGORY_NAME = "categoryName"; //$NON-NLS-1$
	public static String PROP_GROUP_NAME = "groupName"; //$NON-NLS-1$
	public static String PROP_SEAT_NUMBER = "seatNumber"; //$NON-NLS-1$
	public static String PROP_ITEM_COUNT = "itemCount"; //$NON-NLS-1$
	public static String PROP_UNIT_PRICE = "unitPrice"; //$NON-NLS-1$
	public static String PROP_TAX_AMOUNT = "taxAmount"; //$NON-NLS-1$
	public static String PROP_FRACTIONAL_UNIT = "fractionalUnit"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_PRINTER_GROUP = "printerGroup"; //$NON-NLS-1$
	public static String PROP_STATUS = "status"; //$NON-NLS-1$
	public static String PROP_PRINTED_TO_KITCHEN = "printedToKitchen"; //$NON-NLS-1$
	public static String PROP_SUBTOTAL_AMOUNT = "subtotalAmount"; //$NON-NLS-1$
	public static String PROP_TAX_AMOUNT_WITHOUT_MODIFIERS = "taxAmountWithoutModifiers"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_SUBTOTAL_AMOUNT_WITHOUT_MODIFIERS = "subtotalAmountWithoutModifiers"; //$NON-NLS-1$
	public static String PROP_TOTAL_AMOUNT = "totalAmount"; //$NON-NLS-1$


	// constructors
	public BaseTicketItem () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTicketItem (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseTicketItem (
		java.lang.Integer id,
		com.floreantpos.model.Ticket ticket) {

		this.setId(id);
		this.setTicket(ticket);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.Integer itemId;
		protected java.lang.Integer itemCount;
		protected java.lang.Double itemQuantity;
		protected java.lang.String name;
		protected java.lang.String itemUnitName;
		protected java.lang.String groupName;
		protected java.lang.String categoryName;
		protected java.lang.Double unitPrice;
		protected java.lang.Double taxRate;
		protected java.lang.Double subtotalAmount;
		protected java.lang.Double subtotalAmountWithoutModifiers;
		protected java.lang.Double discountAmount;
		protected java.lang.Double taxAmount;
		protected java.lang.Double taxAmountWithoutModifiers;
		protected java.lang.Double totalAmount;
		protected java.lang.Double totalAmountWithoutModifiers;
		protected java.lang.Boolean beverage;
		protected java.lang.Boolean inventoryHandled;
		protected java.lang.Boolean shouldPrintToKitchen;
		protected java.lang.Boolean treatAsSeat;
		protected java.lang.Integer seatNumber;
		protected java.lang.Boolean fractionalUnit;
		protected java.lang.Boolean hasModifiers;
		protected java.lang.Boolean printedToKitchen;
		protected java.lang.String status;
		protected java.lang.Boolean stockAmountAdjusted;
		protected java.lang.Boolean pizzaType;
		protected java.lang.Integer pizzaSectionModeType;

	// many to one
	private com.floreantpos.model.TicketItemModifier sizeModifier;
	private com.floreantpos.model.Ticket ticket;
	private com.floreantpos.model.PrinterGroup printerGroup;

	// collections
	private java.util.List<com.floreantpos.model.TicketItemModifier> ticketItemModifiers;
	private java.util.List<com.floreantpos.model.TicketItemModifier> addOns;
	private java.util.List<com.floreantpos.model.TicketItemDiscount> discounts;
	private java.util.List<TicketItemCookingInstruction> cookingInstructions;



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
	 * Return the value associated with the column: ITEM_QUANTITY
	 */
	public java.lang.Double getItemQuantity () {
									return itemQuantity == null ? Double.valueOf(0) : itemQuantity;
					}

	/**
	 * Set the value related to the column: ITEM_QUANTITY
	 * @param itemQuantity the ITEM_QUANTITY value
	 */
	public void setItemQuantity (java.lang.Double itemQuantity) {
		this.itemQuantity = itemQuantity;
	}



	/**
	 * Return the value associated with the column: ITEM_NAME
	 */
	public java.lang.String getName () {
					return name;
			}

	/**
	 * Set the value related to the column: ITEM_NAME
	 * @param name the ITEM_NAME value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}



	/**
	 * Return the value associated with the column: ITEM_UNIT_NAME
	 */
	public java.lang.String getItemUnitName () {
					return itemUnitName;
			}

	/**
	 * Set the value related to the column: ITEM_UNIT_NAME
	 * @param itemUnitName the ITEM_UNIT_NAME value
	 */
	public void setItemUnitName (java.lang.String itemUnitName) {
		this.itemUnitName = itemUnitName;
	}



	/**
	 * Return the value associated with the column: GROUP_NAME
	 */
	public java.lang.String getGroupName () {
					return groupName;
			}

	/**
	 * Set the value related to the column: GROUP_NAME
	 * @param groupName the GROUP_NAME value
	 */
	public void setGroupName (java.lang.String groupName) {
		this.groupName = groupName;
	}



	/**
	 * Return the value associated with the column: CATEGORY_NAME
	 */
	public java.lang.String getCategoryName () {
					return categoryName;
			}

	/**
	 * Set the value related to the column: CATEGORY_NAME
	 * @param categoryName the CATEGORY_NAME value
	 */
	public void setCategoryName (java.lang.String categoryName) {
		this.categoryName = categoryName;
	}



	/**
	 * Return the value associated with the column: ITEM_PRICE
	 */
	public java.lang.Double getUnitPrice () {
									return unitPrice == null ? Double.valueOf(0) : unitPrice;
					}

	/**
	 * Set the value related to the column: ITEM_PRICE
	 * @param unitPrice the ITEM_PRICE value
	 */
	public void setUnitPrice (java.lang.Double unitPrice) {
		this.unitPrice = unitPrice;
	}



	/**
	 * Return the value associated with the column: ITEM_TAX_RATE
	 */
	public java.lang.Double getTaxRate () {
									return taxRate == null ? Double.valueOf(0) : taxRate;
					}

	/**
	 * Set the value related to the column: ITEM_TAX_RATE
	 * @param taxRate the ITEM_TAX_RATE value
	 */
	public void setTaxRate (java.lang.Double taxRate) {
		this.taxRate = taxRate;
	}



	/**
	 * Return the value associated with the column: SUB_TOTAL
	 */
	public java.lang.Double getSubtotalAmount () {
									return subtotalAmount == null ? Double.valueOf(0) : subtotalAmount;
					}

	/**
	 * Set the value related to the column: SUB_TOTAL
	 * @param subtotalAmount the SUB_TOTAL value
	 */
	public void setSubtotalAmount (java.lang.Double subtotalAmount) {
		this.subtotalAmount = subtotalAmount;
	}



	/**
	 * Return the value associated with the column: SUB_TOTAL_WITHOUT_MODIFIERS
	 */
	public java.lang.Double getSubtotalAmountWithoutModifiers () {
									return subtotalAmountWithoutModifiers == null ? Double.valueOf(0) : subtotalAmountWithoutModifiers;
					}

	/**
	 * Set the value related to the column: SUB_TOTAL_WITHOUT_MODIFIERS
	 * @param subtotalAmountWithoutModifiers the SUB_TOTAL_WITHOUT_MODIFIERS value
	 */
	public void setSubtotalAmountWithoutModifiers (java.lang.Double subtotalAmountWithoutModifiers) {
		this.subtotalAmountWithoutModifiers = subtotalAmountWithoutModifiers;
	}



	/**
	 * Return the value associated with the column: DISCOUNT
	 */
	public java.lang.Double getDiscountAmount () {
									return discountAmount == null ? Double.valueOf(0) : discountAmount;
					}

	/**
	 * Set the value related to the column: DISCOUNT
	 * @param discountAmount the DISCOUNT value
	 */
	public void setDiscountAmount (java.lang.Double discountAmount) {
		this.discountAmount = discountAmount;
	}



	/**
	 * Return the value associated with the column: TAX_AMOUNT
	 */
	public java.lang.Double getTaxAmount () {
									return taxAmount == null ? Double.valueOf(0) : taxAmount;
					}

	/**
	 * Set the value related to the column: TAX_AMOUNT
	 * @param taxAmount the TAX_AMOUNT value
	 */
	public void setTaxAmount (java.lang.Double taxAmount) {
		this.taxAmount = taxAmount;
	}



	/**
	 * Return the value associated with the column: TAX_AMOUNT_WITHOUT_MODIFIERS
	 */
	public java.lang.Double getTaxAmountWithoutModifiers () {
									return taxAmountWithoutModifiers == null ? Double.valueOf(0) : taxAmountWithoutModifiers;
					}

	/**
	 * Set the value related to the column: TAX_AMOUNT_WITHOUT_MODIFIERS
	 * @param taxAmountWithoutModifiers the TAX_AMOUNT_WITHOUT_MODIFIERS value
	 */
	public void setTaxAmountWithoutModifiers (java.lang.Double taxAmountWithoutModifiers) {
		this.taxAmountWithoutModifiers = taxAmountWithoutModifiers;
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
	 * Return the value associated with the column: TOTAL_PRICE_WITHOUT_MODIFIERS
	 */
	public java.lang.Double getTotalAmountWithoutModifiers () {
									return totalAmountWithoutModifiers == null ? Double.valueOf(0) : totalAmountWithoutModifiers;
					}

	/**
	 * Set the value related to the column: TOTAL_PRICE_WITHOUT_MODIFIERS
	 * @param totalAmountWithoutModifiers the TOTAL_PRICE_WITHOUT_MODIFIERS value
	 */
	public void setTotalAmountWithoutModifiers (java.lang.Double totalAmountWithoutModifiers) {
		this.totalAmountWithoutModifiers = totalAmountWithoutModifiers;
	}



	/**
	 * Return the value associated with the column: BEVERAGE
	 */
	public java.lang.Boolean isBeverage () {
								return beverage == null ? Boolean.FALSE : beverage;
					}

	/**
	 * Set the value related to the column: BEVERAGE
	 * @param beverage the BEVERAGE value
	 */
	public void setBeverage (java.lang.Boolean beverage) {
		this.beverage = beverage;
	}



	/**
	 * Return the value associated with the column: INVENTORY_HANDLED
	 */
	public java.lang.Boolean isInventoryHandled () {
								return inventoryHandled == null ? Boolean.FALSE : inventoryHandled;
					}

	/**
	 * Set the value related to the column: INVENTORY_HANDLED
	 * @param inventoryHandled the INVENTORY_HANDLED value
	 */
	public void setInventoryHandled (java.lang.Boolean inventoryHandled) {
		this.inventoryHandled = inventoryHandled;
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
	 * Return the value associated with the column: TREAT_AS_SEAT
	 */
	public java.lang.Boolean isTreatAsSeat () {
								return treatAsSeat == null ? Boolean.FALSE : treatAsSeat;
					}

	/**
	 * Set the value related to the column: TREAT_AS_SEAT
	 * @param treatAsSeat the TREAT_AS_SEAT value
	 */
	public void setTreatAsSeat (java.lang.Boolean treatAsSeat) {
		this.treatAsSeat = treatAsSeat;
	}



	/**
	 * Return the value associated with the column: SEAT_NUMBER
	 */
	public java.lang.Integer getSeatNumber () {
					return seatNumber == null ? Integer.valueOf(0) : seatNumber;
			}

	/**
	 * Set the value related to the column: SEAT_NUMBER
	 * @param seatNumber the SEAT_NUMBER value
	 */
	public void setSeatNumber (java.lang.Integer seatNumber) {
		this.seatNumber = seatNumber;
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
	 * Return the value associated with the column: HAS_MODIIERS
	 */
	public java.lang.Boolean isHasModifiers () {
								return hasModifiers == null ? Boolean.FALSE : hasModifiers;
					}

	/**
	 * Set the value related to the column: HAS_MODIIERS
	 * @param hasModifiers the HAS_MODIIERS value
	 */
	public void setHasModifiers (java.lang.Boolean hasModifiers) {
		this.hasModifiers = hasModifiers;
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



	/**
	 * Return the value associated with the column: STOCK_AMOUNT_ADJUSTED
	 */
	public java.lang.Boolean isStockAmountAdjusted () {
								return stockAmountAdjusted == null ? Boolean.FALSE : stockAmountAdjusted;
					}

	/**
	 * Set the value related to the column: STOCK_AMOUNT_ADJUSTED
	 * @param stockAmountAdjusted the STOCK_AMOUNT_ADJUSTED value
	 */
	public void setStockAmountAdjusted (java.lang.Boolean stockAmountAdjusted) {
		this.stockAmountAdjusted = stockAmountAdjusted;
	}



	/**
	 * Return the value associated with the column: PIZZA_TYPE
	 */
	public java.lang.Boolean isPizzaType () {
								return pizzaType == null ? Boolean.FALSE : pizzaType;
					}

	/**
	 * Set the value related to the column: PIZZA_TYPE
	 * @param pizzaType the PIZZA_TYPE value
	 */
	public void setPizzaType (java.lang.Boolean pizzaType) {
		this.pizzaType = pizzaType;
	}



	/**
	 * Return the value associated with the column: PIZZA_SECTION_MODE
	 */
	public java.lang.Integer getPizzaSectionModeType () {
					return pizzaSectionModeType == null ? Integer.valueOf(0) : pizzaSectionModeType;
			}

	/**
	 * Set the value related to the column: PIZZA_SECTION_MODE
	 * @param pizzaSectionModeType the PIZZA_SECTION_MODE value
	 */
	public void setPizzaSectionModeType (java.lang.Integer pizzaSectionModeType) {
		this.pizzaSectionModeType = pizzaSectionModeType;
	}



	/**
	 * Return the value associated with the column: SIZE_MODIFIER_ID
	 */
	public com.floreantpos.model.TicketItemModifier getSizeModifier () {
					return sizeModifier;
			}

	/**
	 * Set the value related to the column: SIZE_MODIFIER_ID
	 * @param sizeModifier the SIZE_MODIFIER_ID value
	 */
	public void setSizeModifier (com.floreantpos.model.TicketItemModifier sizeModifier) {
		this.sizeModifier = sizeModifier;
	}



	/**
	 * Return the value associated with the column: TICKET_ID
	 */
	public com.floreantpos.model.Ticket getTicket () {
					return ticket;
			}

	/**
	 * Set the value related to the column: TICKET_ID
	 * @param ticket the TICKET_ID value
	 */
	public void setTicket (com.floreantpos.model.Ticket ticket) {
		this.ticket = ticket;
	}



	/**
	 * Return the value associated with the column: PG_ID
	 */
	public com.floreantpos.model.PrinterGroup getPrinterGroup () {
					return printerGroup;
			}

	/**
	 * Set the value related to the column: PG_ID
	 * @param printerGroup the PG_ID value
	 */
	public void setPrinterGroup (com.floreantpos.model.PrinterGroup printerGroup) {
		this.printerGroup = printerGroup;
	}



	/**
	 * Return the value associated with the column: ticketItemModifiers
	 */
	public java.util.List<com.floreantpos.model.TicketItemModifier> getTicketItemModifiers () {
					return ticketItemModifiers;
			}

	/**
	 * Set the value related to the column: ticketItemModifiers
	 * @param ticketItemModifiers the ticketItemModifiers value
	 */
	public void setTicketItemModifiers (java.util.List<com.floreantpos.model.TicketItemModifier> ticketItemModifiers) {
		this.ticketItemModifiers = ticketItemModifiers;
	}

	public void addToticketItemModifiers (com.floreantpos.model.TicketItemModifier ticketItemModifier) {
		if (null == getTicketItemModifiers()) setTicketItemModifiers(new java.util.ArrayList<com.floreantpos.model.TicketItemModifier>());
		getTicketItemModifiers().add(ticketItemModifier);
	}



	/**
	 * Return the value associated with the column: addOns
	 */
	public java.util.List<com.floreantpos.model.TicketItemModifier> getAddOns () {
					return addOns;
			}

	/**
	 * Set the value related to the column: addOns
	 * @param addOns the addOns value
	 */
	public void setAddOns (java.util.List<com.floreantpos.model.TicketItemModifier> addOns) {
		this.addOns = addOns;
	}

	public void addToaddOns (com.floreantpos.model.TicketItemModifier ticketItemModifier) {
		if (null == getAddOns()) setAddOns(new java.util.ArrayList<com.floreantpos.model.TicketItemModifier>());
		getAddOns().add(ticketItemModifier);
	}



	/**
	 * Return the value associated with the column: discounts
	 */
	public java.util.List<com.floreantpos.model.TicketItemDiscount> getDiscounts () {
					return discounts;
			}

	/**
	 * Set the value related to the column: discounts
	 * @param discounts the discounts value
	 */
	public void setDiscounts (java.util.List<com.floreantpos.model.TicketItemDiscount> discounts) {
		this.discounts = discounts;
	}

	public void addTodiscounts (com.floreantpos.model.TicketItemDiscount ticketItemDiscount) {
		if (null == getDiscounts()) setDiscounts(new java.util.ArrayList<com.floreantpos.model.TicketItemDiscount>());
		getDiscounts().add(ticketItemDiscount);
	}



	/**
	 * Return the value associated with the column: cookingInstructions
	 */
	public java.util.List<TicketItemCookingInstruction> getCookingInstructions() {
					return cookingInstructions;
			}

	/**
	 * Set the value related to the column: cookingInstructions
	 * @param cookingInstructions the cookingInstructions value
	 */
	public void setCookingInstructions(java.util.List<TicketItemCookingInstruction> cookingInstructions) {
		this.cookingInstructions = cookingInstructions;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.TicketItem)) return false;
		else {
			com.floreantpos.model.TicketItem ticketItem = (com.floreantpos.model.TicketItem) obj;
			if (null == this.getId() || null == ticketItem.getId()) return false;
			else return (this.getId().equals(ticketItem.getId()));
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