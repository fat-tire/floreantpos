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

public abstract class BaseCreditCardTransaction extends com.floreantpos.model.PosTransaction  implements Comparable, Serializable {

	public static String REF = "CreditCardTransaction";
	public static String PROP_CARD_NUMBER = "cardNumber";
	public static String PROP_CARD_TYPE = "cardType";
	public static String PROP_ID = "id";


	// constructors
	public BaseCreditCardTransaction () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCreditCardTransaction (java.lang.Integer id) {
		super(id);
	}



	private int hashCode = Integer.MIN_VALUE;


	// fields
	private java.lang.String cardNumber;
	private java.lang.String cardType;






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





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.CreditCardTransaction)) return false;
		else {
			com.floreantpos.model.CreditCardTransaction creditCardTransaction = (com.floreantpos.model.CreditCardTransaction) obj;
			if (null == this.getId() || null == creditCardTransaction.getId()) return false;
			else return (this.getId().equals(creditCardTransaction.getId()));
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