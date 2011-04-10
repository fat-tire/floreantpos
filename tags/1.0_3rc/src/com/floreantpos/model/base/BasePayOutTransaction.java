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

public abstract class BasePayOutTransaction extends com.floreantpos.model.PosTransaction  implements Comparable, Serializable {

	public static String REF = "PayOutTransaction";
	public static String PROP_RECEPIENT = "recepient";
	public static String PROP_NOTE = "note";
	public static String PROP_ID = "id";
	public static String PROP_REASON = "reason";


	// constructors
	public BasePayOutTransaction () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BasePayOutTransaction (java.lang.Integer id) {
		super(id);
	}



	private int hashCode = Integer.MIN_VALUE;


	// fields
	private java.lang.String note;

	// many to one
	private com.floreantpos.model.PayoutReason reason;
	private com.floreantpos.model.PayoutRecepient recepient;






	/**
	 * Return the value associated with the column: note
	 */
	public java.lang.String getNote () {
			return note;
	}

	/**
	 * Set the value related to the column: note
	 * @param note the note value
	 */
	public void setNote (java.lang.String note) {
		this.note = note;
	}



	/**
	 * Return the value associated with the column: REASON_ID
	 */
	public com.floreantpos.model.PayoutReason getReason () {
			return reason;
	}

	/**
	 * Set the value related to the column: REASON_ID
	 * @param reason the REASON_ID value
	 */
	public void setReason (com.floreantpos.model.PayoutReason reason) {
		this.reason = reason;
	}



	/**
	 * Return the value associated with the column: RECEPIENT_ID
	 */
	public com.floreantpos.model.PayoutRecepient getRecepient () {
			return recepient;
	}

	/**
	 * Set the value related to the column: RECEPIENT_ID
	 * @param recepient the RECEPIENT_ID value
	 */
	public void setRecepient (com.floreantpos.model.PayoutRecepient recepient) {
		this.recepient = recepient;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.PayOutTransaction)) return false;
		else {
			com.floreantpos.model.PayOutTransaction payOutTransaction = (com.floreantpos.model.PayOutTransaction) obj;
			if (null == this.getId() || null == payOutTransaction.getId()) return false;
			else return (this.getId().equals(payOutTransaction.getId()));
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