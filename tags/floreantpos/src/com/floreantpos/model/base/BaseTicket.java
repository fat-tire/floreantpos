package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;

import com.floreantpos.model.TicketCookingInstruction;


/**
 * This is an object that contains data related to the TICKET table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TICKET"
 */

public abstract class BaseTicket  implements Comparable, Serializable {

	public static String REF = "Ticket";
	public static String PROP_BAR_CODE = "barCode";
	public static String PROP_RE_OPENED = "reOpened";
	public static String PROP_VOID_REASON = "voidReason";
	public static String PROP_DUE_AMOUNT = "dueAmount";
	public static String PROP_TRANSACTION_TYPE = "transactionType";
	public static String PROP_DISCOUNT_AMOUNT = "discountAmount";
	public static String PROP_CREATE_DATE = "createDate";
	public static String PROP_NUMBER_OF_GUESTS = "numberOfGuests";
	public static String PROP_PAID = "paid";
	public static String PROP_ACTIVE_DATE = "activeDate";
	public static String PROP_CARD_TYPE = "cardType";
	public static String PROP_CREATION_HOUR = "creationHour";
	public static String PROP_DRAWER_RESETTED = "drawerResetted";
	public static String PROP_CARD_NUMBER = "cardNumber";
	public static String PROP_OWNER = "owner";
	public static String PROP_GRATUITY = "gratuity";
	public static String PROP_TABLE_NUMBER = "tableNumber";
	public static String PROP_TERMINAL = "terminal";
	public static String PROP_CLOSED = "closed";
	public static String PROP_CLOSING_DATE = "closingDate";
	public static String PROP_TRANSACTION_CODE = "transactionCode";
	public static String PROP_SHIFT = "shift";
	public static String PROP_TAX_AMOUNT = "taxAmount";
	public static String PROP_SUBTOTAL_AMOUNT = "subtotalAmount";
	public static String PROP_VOIDED_BY = "voidedBy";
	public static String PROP_TAX_EXEMPT = "taxExempt";
	public static String PROP_ID = "id";
	public static String PROP_WASTED = "wasted";
	public static String PROP_VOIDED = "voided";
	public static String PROP_TOTAL_AMOUNT = "totalAmount";
	public static String PROP_PAID_AMOUNT = "paidAmount";


	// constructors
	public BaseTicket () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTicket (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	 java.util.Date modifiedTime;

	// fields
	private java.util.Date createDate;
	private java.util.Date closingDate;
	private java.util.Date activeDate;
	private java.lang.Integer creationHour;
	private java.lang.Boolean paid;
	private java.lang.Boolean voided;
	private java.lang.String voidReason;
	private java.lang.Boolean wasted;
	private java.lang.Boolean closed;
	private java.lang.Boolean drawerResetted;
	private java.lang.Double subtotalAmount;
	private java.lang.Double discountAmount;
	private java.lang.Double taxAmount;
	private java.lang.Double totalAmount;
	private java.lang.Double paidAmount;
	private java.lang.Double dueAmount;
	private java.lang.Integer tableNumber;
	private java.lang.Integer numberOfGuests;
	private java.lang.String transactionType;
	private java.lang.String transactionCode;
	private java.lang.String barCode;
	private java.lang.String cardType;
	private java.lang.String cardNumber;
	private java.lang.Boolean taxExempt;
	private java.lang.Boolean reOpened;

	// many to one
	private com.floreantpos.model.Shift shift;
	private com.floreantpos.model.User owner;
	private com.floreantpos.model.Gratuity gratuity;
	private com.floreantpos.model.User voidedBy;
	private com.floreantpos.model.Terminal terminal;

	// collections
	private java.util.List<com.floreantpos.model.TicketItem> ticketItems;
	private java.util.List<com.floreantpos.model.TicketCouponAndDiscount> couponAndDiscounts;
	private java.util.Set<TicketCookingInstruction> cookingInstructions;



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
	 * Return the value associated with the column: CREATE_DATE
	 */
	public java.util.Date getCreateDate () {
			return createDate;
	}

	/**
	 * Set the value related to the column: CREATE_DATE
	 * @param createDate the CREATE_DATE value
	 */
	public void setCreateDate (java.util.Date createDate) {
		this.createDate = createDate;
	}



	/**
	 * Return the value associated with the column: CLOSING_DATE
	 */
	public java.util.Date getClosingDate () {
			return closingDate;
	}

	/**
	 * Set the value related to the column: CLOSING_DATE
	 * @param closingDate the CLOSING_DATE value
	 */
	public void setClosingDate (java.util.Date closingDate) {
		this.closingDate = closingDate;
	}



	/**
	 * Return the value associated with the column: ACTIVE_DATE
	 */
	public java.util.Date getActiveDate () {
			return activeDate;
	}

	/**
	 * Set the value related to the column: ACTIVE_DATE
	 * @param activeDate the ACTIVE_DATE value
	 */
	public void setActiveDate (java.util.Date activeDate) {
		this.activeDate = activeDate;
	}



	/**
	 * Return the value associated with the column: CREATION_HOUR
	 */
	public java.lang.Integer getCreationHour () {
			return creationHour == null ? Integer.valueOf(0) : creationHour;
	}

	/**
	 * Set the value related to the column: CREATION_HOUR
	 * @param creationHour the CREATION_HOUR value
	 */
	public void setCreationHour (java.lang.Integer creationHour) {
		this.creationHour = creationHour;
	}



	/**
	 * Return the value associated with the column: PAID
	 */
	public java.lang.Boolean isPaid () {
					return paid == null ? Boolean.FALSE : paid;
			}

	/**
	 * Set the value related to the column: PAID
	 * @param paid the PAID value
	 */
	public void setPaid (java.lang.Boolean paid) {
		this.paid = paid;
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
	 * Return the value associated with the column: VOID_REASON
	 */
	public java.lang.String getVoidReason () {
			return voidReason;
	}

	/**
	 * Set the value related to the column: VOID_REASON
	 * @param voidReason the VOID_REASON value
	 */
	public void setVoidReason (java.lang.String voidReason) {
		this.voidReason = voidReason;
	}



	/**
	 * Return the value associated with the column: WASTED
	 */
	public java.lang.Boolean isWasted () {
					return wasted == null ? Boolean.FALSE : wasted;
			}

	/**
	 * Set the value related to the column: WASTED
	 * @param wasted the WASTED value
	 */
	public void setWasted (java.lang.Boolean wasted) {
		this.wasted = wasted;
	}



	/**
	 * Return the value associated with the column: SETTLED
	 */
	public java.lang.Boolean isClosed () {
					return closed == null ? Boolean.FALSE : closed;
			}

	/**
	 * Set the value related to the column: SETTLED
	 * @param closed the SETTLED value
	 */
	public void setClosed (java.lang.Boolean closed) {
		this.closed = closed;
	}



	/**
	 * Return the value associated with the column: DRAWER_RESETTED
	 */
	public java.lang.Boolean isDrawerResetted () {
					return drawerResetted == null ? Boolean.FALSE : drawerResetted;
			}

	/**
	 * Set the value related to the column: DRAWER_RESETTED
	 * @param drawerResetted the DRAWER_RESETTED value
	 */
	public void setDrawerResetted (java.lang.Boolean drawerResetted) {
		this.drawerResetted = drawerResetted;
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
	 * Return the value associated with the column: TOTAL_DISCOUNT
	 */
	public java.lang.Double getDiscountAmount () {
					return discountAmount == null ? Double.valueOf(0) : discountAmount;
			}

	/**
	 * Set the value related to the column: TOTAL_DISCOUNT
	 * @param discountAmount the TOTAL_DISCOUNT value
	 */
	public void setDiscountAmount (java.lang.Double discountAmount) {
		this.discountAmount = discountAmount;
	}



	/**
	 * Return the value associated with the column: TOTAL_TAX
	 */
	public java.lang.Double getTaxAmount () {
					return taxAmount == null ? Double.valueOf(0) : taxAmount;
			}

	/**
	 * Set the value related to the column: TOTAL_TAX
	 * @param taxAmount the TOTAL_TAX value
	 */
	public void setTaxAmount (java.lang.Double taxAmount) {
		this.taxAmount = taxAmount;
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
	 * Return the value associated with the column: PAID_AMOUNT
	 */
	public java.lang.Double getPaidAmount () {
					return paidAmount == null ? Double.valueOf(0) : paidAmount;
			}

	/**
	 * Set the value related to the column: PAID_AMOUNT
	 * @param paidAmount the PAID_AMOUNT value
	 */
	public void setPaidAmount (java.lang.Double paidAmount) {
		this.paidAmount = paidAmount;
	}



	/**
	 * Return the value associated with the column: DUE_AMOUNT
	 */
	public java.lang.Double getDueAmount () {
					return dueAmount == null ? Double.valueOf(0) : dueAmount;
			}

	/**
	 * Set the value related to the column: DUE_AMOUNT
	 * @param dueAmount the DUE_AMOUNT value
	 */
	public void setDueAmount (java.lang.Double dueAmount) {
		this.dueAmount = dueAmount;
	}



	/**
	 * Return the value associated with the column: TABLE_NUMBER
	 */
	public java.lang.Integer getTableNumber () {
			return tableNumber == null ? Integer.valueOf(0) : tableNumber;
	}

	/**
	 * Set the value related to the column: TABLE_NUMBER
	 * @param tableNumber the TABLE_NUMBER value
	 */
	public void setTableNumber (java.lang.Integer tableNumber) {
		this.tableNumber = tableNumber;
	}



	/**
	 * Return the value associated with the column: NUMBER_OF_GUESTS
	 */
	public java.lang.Integer getNumberOfGuests () {
			return numberOfGuests == null ? Integer.valueOf(0) : numberOfGuests;
	}

	/**
	 * Set the value related to the column: NUMBER_OF_GUESTS
	 * @param numberOfGuests the NUMBER_OF_GUESTS value
	 */
	public void setNumberOfGuests (java.lang.Integer numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}



	/**
	 * Return the value associated with the column: TRANSACTION_TYPE
	 */
	public java.lang.String getTransactionType () {
			return transactionType;
	}

	/**
	 * Set the value related to the column: TRANSACTION_TYPE
	 * @param transactionType the TRANSACTION_TYPE value
	 */
	public void setTransactionType (java.lang.String transactionType) {
		this.transactionType = transactionType;
	}



	/**
	 * Return the value associated with the column: TRANSACTION_CODE
	 */
	public java.lang.String getTransactionCode () {
			return transactionCode;
	}

	/**
	 * Set the value related to the column: TRANSACTION_CODE
	 * @param transactionCode the TRANSACTION_CODE value
	 */
	public void setTransactionCode (java.lang.String transactionCode) {
		this.transactionCode = transactionCode;
	}



	/**
	 * Return the value associated with the column: BAR_CODE
	 */
	public java.lang.String getBarCode () {
			return barCode;
	}

	/**
	 * Set the value related to the column: BAR_CODE
	 * @param barCode the BAR_CODE value
	 */
	public void setBarCode (java.lang.String barCode) {
		this.barCode = barCode;
	}



	/**
	 * Return the value associated with the column: CARD_TYPE
	 */
	public java.lang.String getCardType () {
			return cardType;
	}

	/**
	 * Set the value related to the column: CARD_TYPE
	 * @param cardType the CARD_TYPE value
	 */
	public void setCardType (java.lang.String cardType) {
		this.cardType = cardType;
	}



	/**
	 * Return the value associated with the column: CARD_NUMBER
	 */
	public java.lang.String getCardNumber () {
			return cardNumber;
	}

	/**
	 * Set the value related to the column: CARD_NUMBER
	 * @param cardNumber the CARD_NUMBER value
	 */
	public void setCardNumber (java.lang.String cardNumber) {
		this.cardNumber = cardNumber;
	}



	/**
	 * Return the value associated with the column: IS_TAX_EXEMPT
	 */
	public java.lang.Boolean isTaxExempt () {
					return taxExempt == null ? Boolean.FALSE : taxExempt;
			}

	/**
	 * Set the value related to the column: IS_TAX_EXEMPT
	 * @param taxExempt the IS_TAX_EXEMPT value
	 */
	public void setTaxExempt (java.lang.Boolean taxExempt) {
		this.taxExempt = taxExempt;
	}



	/**
	 * Return the value associated with the column: IS_RE_OPENED
	 */
	public java.lang.Boolean isReOpened () {
					return reOpened == null ? Boolean.FALSE : reOpened;
			}

	/**
	 * Set the value related to the column: IS_RE_OPENED
	 * @param reOpened the IS_RE_OPENED value
	 */
	public void setReOpened (java.lang.Boolean reOpened) {
		this.reOpened = reOpened;
	}



	/**
	 * Return the value associated with the column: SHIFT_ID
	 */
	public com.floreantpos.model.Shift getShift () {
			return shift;
	}

	/**
	 * Set the value related to the column: SHIFT_ID
	 * @param shift the SHIFT_ID value
	 */
	public void setShift (com.floreantpos.model.Shift shift) {
		this.shift = shift;
	}



	/**
	 * Return the value associated with the column: OWNER_ID
	 */
	public com.floreantpos.model.User getOwner () {
			return owner;
	}

	/**
	 * Set the value related to the column: OWNER_ID
	 * @param owner the OWNER_ID value
	 */
	public void setOwner (com.floreantpos.model.User owner) {
		this.owner = owner;
	}



	/**
	 * Return the value associated with the column: GRATUITY_ID
	 */
	public com.floreantpos.model.Gratuity getGratuity () {
			return gratuity;
	}

	/**
	 * Set the value related to the column: GRATUITY_ID
	 * @param gratuity the GRATUITY_ID value
	 */
	public void setGratuity (com.floreantpos.model.Gratuity gratuity) {
		this.gratuity = gratuity;
	}



	/**
	 * Return the value associated with the column: VOID_BY_USER
	 */
	public com.floreantpos.model.User getVoidedBy () {
			return voidedBy;
	}

	/**
	 * Set the value related to the column: VOID_BY_USER
	 * @param voidedBy the VOID_BY_USER value
	 */
	public void setVoidedBy (com.floreantpos.model.User voidedBy) {
		this.voidedBy = voidedBy;
	}



	/**
	 * Return the value associated with the column: TERMINAL_ID
	 */
	public com.floreantpos.model.Terminal getTerminal () {
			return terminal;
	}

	/**
	 * Set the value related to the column: TERMINAL_ID
	 * @param terminal the TERMINAL_ID value
	 */
	public void setTerminal (com.floreantpos.model.Terminal terminal) {
		this.terminal = terminal;
	}



	/**
	 * Return the value associated with the column: ticketItems
	 */
	public java.util.List<com.floreantpos.model.TicketItem> getTicketItems () {
			return ticketItems;
	}

	/**
	 * Set the value related to the column: ticketItems
	 * @param ticketItems the ticketItems value
	 */
	public void setTicketItems (java.util.List<com.floreantpos.model.TicketItem> ticketItems) {
		this.ticketItems = ticketItems;
	}

	public void addToticketItems (com.floreantpos.model.TicketItem ticketItem) {
		if (null == getTicketItems()) setTicketItems(new java.util.ArrayList<com.floreantpos.model.TicketItem>());
		getTicketItems().add(ticketItem);
	}



	/**
	 * Return the value associated with the column: couponAndDiscounts
	 */
	public java.util.List<com.floreantpos.model.TicketCouponAndDiscount> getCouponAndDiscounts () {
			return couponAndDiscounts;
	}

	/**
	 * Set the value related to the column: couponAndDiscounts
	 * @param couponAndDiscounts the couponAndDiscounts value
	 */
	public void setCouponAndDiscounts (java.util.List<com.floreantpos.model.TicketCouponAndDiscount> couponAndDiscounts) {
		this.couponAndDiscounts = couponAndDiscounts;
	}

	public void addTocouponAndDiscounts (com.floreantpos.model.TicketCouponAndDiscount ticketCouponAndDiscount) {
		if (null == getCouponAndDiscounts()) setCouponAndDiscounts(new java.util.ArrayList<com.floreantpos.model.TicketCouponAndDiscount>());
		getCouponAndDiscounts().add(ticketCouponAndDiscount);
	}



	/**
	 * Return the value associated with the column: cookingInstructions
	 */
	public java.util.Set<TicketCookingInstruction> getCookingInstructions () {
			return cookingInstructions;
	}

	/**
	 * Set the value related to the column: cookingInstructions
	 * @param cookingInstructions the cookingInstructions value
	 */
	public void setCookingInstructions (java.util.Set<TicketCookingInstruction> cookingInstructions) {
		this.cookingInstructions = cookingInstructions;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Ticket)) return false;
		else {
			com.floreantpos.model.Ticket ticket = (com.floreantpos.model.Ticket) obj;
			if (null == this.getId() || null == ticket.getId()) return false;
			else return (this.getId().equals(ticket.getId()));
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