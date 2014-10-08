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
	public static String PROP_CARD_TRACK = "cardTrack";
	public static String PROP_CARD_EXPIRY_YEAR = "cardExpiryYear";
	public static String PROP_AUTHORIZATION_CODE = "authorizationCode";
	public static String PROP_CARD_ENTRY_TYPE = "cardEntryType";
	public static String PROP_CARD_TRANSACTION_ID = "cardTransactionId";
	public static String PROP_ID = "id";
	public static String PROP_CARD_MERCHANT_GATEWAY = "cardMerchantGateway";
	public static String PROP_CARD_TYPE = "cardType";
	public static String PROP_CARD_EXPIRY_MONTH = "cardExpiryMonth";


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
		protected java.lang.String cardTrack;
		protected java.lang.String cardNumber;
		protected java.lang.String authorizationCode;
		protected java.lang.String cardType;
		protected java.lang.String cardTransactionId;
		protected java.lang.String cardMerchantGateway;
		protected java.lang.String cardExpiryMonth;
		protected java.lang.String cardExpiryYear;
		protected java.lang.String cardEntryType;






	/**
	 * Return the value associated with the column: CARD_TRACK
	 */
	public java.lang.String getCardTrack () {
					return cardTrack;
			}

	/**
	 * Set the value related to the column: CARD_TRACK
	 * @param cardTrack the CARD_TRACK value
	 */
	public void setCardTrack (java.lang.String cardTrack) {
		this.cardTrack = cardTrack;
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
	 * Return the value associated with the column: CARD_AUTHORIZATION_CODE
	 */
	public java.lang.String getAuthorizationCode () {
					return authorizationCode;
			}

	/**
	 * Set the value related to the column: CARD_AUTHORIZATION_CODE
	 * @param authorizationCode the CARD_AUTHORIZATION_CODE value
	 */
	public void setAuthorizationCode (java.lang.String authorizationCode) {
		this.authorizationCode = authorizationCode;
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
	 * Return the value associated with the column: CARD_TRANSACTION_ID
	 */
	public java.lang.String getCardTransactionId () {
					return cardTransactionId;
			}

	/**
	 * Set the value related to the column: CARD_TRANSACTION_ID
	 * @param cardTransactionId the CARD_TRANSACTION_ID value
	 */
	public void setCardTransactionId (java.lang.String cardTransactionId) {
		this.cardTransactionId = cardTransactionId;
	}



	/**
	 * Return the value associated with the column: CARD_MERCHANT_GATEWAY
	 */
	public java.lang.String getCardMerchantGateway () {
					return cardMerchantGateway;
			}

	/**
	 * Set the value related to the column: CARD_MERCHANT_GATEWAY
	 * @param cardMerchantGateway the CARD_MERCHANT_GATEWAY value
	 */
	public void setCardMerchantGateway (java.lang.String cardMerchantGateway) {
		this.cardMerchantGateway = cardMerchantGateway;
	}



	/**
	 * Return the value associated with the column: CARD_EXP_MONTH
	 */
	public java.lang.String getCardExpiryMonth () {
					return cardExpiryMonth;
			}

	/**
	 * Set the value related to the column: CARD_EXP_MONTH
	 * @param cardExpiryMonth the CARD_EXP_MONTH value
	 */
	public void setCardExpiryMonth (java.lang.String cardExpiryMonth) {
		this.cardExpiryMonth = cardExpiryMonth;
	}



	/**
	 * Return the value associated with the column: CARD_EXP_YEAR
	 */
	public java.lang.String getCardExpiryYear () {
					return cardExpiryYear;
			}

	/**
	 * Set the value related to the column: CARD_EXP_YEAR
	 * @param cardExpiryYear the CARD_EXP_YEAR value
	 */
	public void setCardExpiryYear (java.lang.String cardExpiryYear) {
		this.cardExpiryYear = cardExpiryYear;
	}



	/**
	 * Return the value associated with the column: CARD_ENTRY_TYPE
	 */
	public java.lang.String getCardEntryType () {
					return cardEntryType;
			}

	/**
	 * Set the value related to the column: CARD_ENTRY_TYPE
	 * @param cardEntryType the CARD_ENTRY_TYPE value
	 */
	public void setCardEntryType (java.lang.String cardEntryType) {
		this.cardEntryType = cardEntryType;
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