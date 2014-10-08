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
	public static String PROP_ADDITIONAL_AMOUNT = "additionalAmount";
	public static String PROP_USER = "user";
	public static String PROP_AMOUNT = "amount";
	public static String PROP_CAPTURED = "captured";
	public static String PROP_ADDITIONAL_AMOUNT_INFO = "additionalAmountInfo";
	public static String PROP_TERMINAL = "terminal";
	public static String PROP_NOTE = "note";
	public static String PROP_RECEPIENT = "recepient";
	public static String PROP_TICKET = "ticket";
	public static String PROP_TENDER_AMOUNT = "tenderAmount";
	public static String PROP_ID = "id";
	public static String PROP_TRANSACTION_TIME = "transactionTime";
	public static String PROP_DRAWER_RESETTED = "drawerResetted";
	public static String PROP_DEBIT = "debit";
	public static String PROP_REASON = "reason";


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
		protected java.util.Date transactionTime;
		protected java.lang.Double amount;
		protected java.lang.Double tenderAmount;
		protected java.lang.Boolean debit;
		protected java.lang.Boolean captured;
		protected java.lang.Double additionalAmount;
		protected java.lang.String additionalAmountInfo;
		protected java.lang.Boolean drawerResetted;
		protected java.lang.String note;

	// many to one
	private com.floreantpos.model.Terminal terminal;
	private com.floreantpos.model.Ticket ticket;
	private com.floreantpos.model.User user;
	private com.floreantpos.model.PayoutReason reason;
	private com.floreantpos.model.PayoutRecepient recepient;



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
	 * Return the value associated with the column: AMOUNT
	 */
	public java.lang.Double getAmount () {
									return amount == null ? Double.valueOf(0) : amount;
					}

	/**
	 * Set the value related to the column: AMOUNT
	 * @param amount the AMOUNT value
	 */
	public void setAmount (java.lang.Double amount) {
		this.amount = amount;
	}



	/**
	 * Return the value associated with the column: TENDER_AMOUNT
	 */
	public java.lang.Double getTenderAmount () {
									return tenderAmount == null ? Double.valueOf(0) : tenderAmount;
					}

	/**
	 * Set the value related to the column: TENDER_AMOUNT
	 * @param tenderAmount the TENDER_AMOUNT value
	 */
	public void setTenderAmount (java.lang.Double tenderAmount) {
		this.tenderAmount = tenderAmount;
	}



	/**
	 * Return the value associated with the column: DEBIT
	 */
	public java.lang.Boolean isDebit () {
								return debit == null ? Boolean.FALSE : debit;
					}

	/**
	 * Set the value related to the column: DEBIT
	 * @param debit the DEBIT value
	 */
	public void setDebit (java.lang.Boolean debit) {
		this.debit = debit;
	}



	/**
	 * Return the value associated with the column: CAPTURED
	 */
	public java.lang.Boolean isCaptured () {
								return captured == null ? Boolean.FALSE : captured;
					}

	/**
	 * Set the value related to the column: CAPTURED
	 * @param captured the CAPTURED value
	 */
	public void setCaptured (java.lang.Boolean captured) {
		this.captured = captured;
	}



	/**
	 * Return the value associated with the column: ADDITIONAL_AMOUNT
	 */
	public java.lang.Double getAdditionalAmount () {
									return additionalAmount == null ? Double.valueOf(0) : additionalAmount;
					}

	/**
	 * Set the value related to the column: ADDITIONAL_AMOUNT
	 * @param additionalAmount the ADDITIONAL_AMOUNT value
	 */
	public void setAdditionalAmount (java.lang.Double additionalAmount) {
		this.additionalAmount = additionalAmount;
	}



	/**
	 * Return the value associated with the column: ADDITIONAL_AMOUNT_INFO
	 */
	public java.lang.String getAdditionalAmountInfo () {
					return additionalAmountInfo;
			}

	/**
	 * Set the value related to the column: ADDITIONAL_AMOUNT_INFO
	 * @param additionalAmountInfo the ADDITIONAL_AMOUNT_INFO value
	 */
	public void setAdditionalAmountInfo (java.lang.String additionalAmountInfo) {
		this.additionalAmountInfo = additionalAmountInfo;
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
	 * Return the value associated with the column: NOTE
	 */
	public java.lang.String getNote () {
					return note;
			}

	/**
	 * Set the value related to the column: NOTE
	 * @param note the NOTE value
	 */
	public void setNote (java.lang.String note) {
		this.note = note;
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



	/**
	 * Return the value associated with the column: PAYOUT_REASON_ID
	 */
	public com.floreantpos.model.PayoutReason getReason () {
					return reason;
			}

	/**
	 * Set the value related to the column: PAYOUT_REASON_ID
	 * @param reason the PAYOUT_REASON_ID value
	 */
	public void setReason (com.floreantpos.model.PayoutReason reason) {
		this.reason = reason;
	}



	/**
	 * Return the value associated with the column: PAYOUT_RECEPIENT_ID
	 */
	public com.floreantpos.model.PayoutRecepient getRecepient () {
					return recepient;
			}

	/**
	 * Set the value related to the column: PAYOUT_RECEPIENT_ID
	 * @param recepient the PAYOUT_RECEPIENT_ID value
	 */
	public void setRecepient (com.floreantpos.model.PayoutRecepient recepient) {
		this.recepient = recepient;
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