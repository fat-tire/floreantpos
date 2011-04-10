package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the TRANSACTIONS table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TRANSACTIONS"
 */

public abstract class BasePosTransaction  implements Comparable, Serializable {

	public static String REF = "PosTransaction";
	public static String PROP_TRANSACTION_TIME = "transactionTime";
	public static String PROP_DRAWER_RESETTED = "drawerResetted";
	public static String PROP_TICKET = "ticket";
	public static String PROP_DISCOUNT_AMOUNT = "discountAmount";
	public static String PROP_TAX_AMOUNT = "taxAmount";
	public static String PROP_USER = "user";
	public static String PROP_GRATUITY_AMOUNT = "gratuityAmount";
	public static String PROP_SUBTOTAL_AMOUNT = "subtotalAmount";
	public static String PROP_TOTAL_AMOUNT = "totalAmount";
	public static String PROP_ID = "id";
	public static String PROP_TERMINAL = "terminal";


	// constructors
	public BasePosTransaction () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BasePosTransaction (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	 java.util.Date modifiedTime;

	// fields
	private java.util.Date transactionTime;
	private java.lang.Double subtotalAmount;
	private java.lang.Double discountAmount;
	private java.lang.Double taxAmount;
	private java.lang.Double totalAmount;
	private java.lang.Double gratuityAmount;
	private java.lang.Boolean drawerResetted;

	// many to one
	private com.floreantpos.model.Terminal terminal;
	private com.floreantpos.model.Ticket ticket;
	private com.floreantpos.model.User user;



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
	 * Return the value associated with the column: TRANSACTION_TIME
	 */
	public java.util.Date getTransactionTime () {
			return transactionTime;
	}

	/**
	 * Set the value related to the column: TRANSACTION_TIME
	 * @param transactionTime the TRANSACTION_TIME value
	 */
	public void setTransactionTime (java.util.Date transactionTime) {
		this.transactionTime = transactionTime;
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
	 * Return the value associated with the column: GRATUITY_AMOUNT
	 */
	public java.lang.Double getGratuityAmount () {
					return gratuityAmount == null ? Double.valueOf(0) : gratuityAmount;
			}

	/**
	 * Set the value related to the column: GRATUITY_AMOUNT
	 * @param gratuityAmount the GRATUITY_AMOUNT value
	 */
	public void setGratuityAmount (java.lang.Double gratuityAmount) {
		this.gratuityAmount = gratuityAmount;
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
	 * Return the value associated with the column: USER_ID
	 */
	public com.floreantpos.model.User getUser () {
			return user;
	}

	/**
	 * Set the value related to the column: USER_ID
	 * @param user the USER_ID value
	 */
	public void setUser (com.floreantpos.model.User user) {
		this.user = user;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.PosTransaction)) return false;
		else {
			com.floreantpos.model.PosTransaction posTransaction = (com.floreantpos.model.PosTransaction) obj;
			if (null == this.getId() || null == posTransaction.getId()) return false;
			else return (this.getId().equals(posTransaction.getId()));
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