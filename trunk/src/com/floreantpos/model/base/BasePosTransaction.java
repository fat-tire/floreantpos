package com.floreantpos.model.base;

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
	public static String PROP_USER = "user";
	public static String PROP_RECEPIENT = "recepient";
	public static String PROP_AUTHORIZABLE = "authorizable";
	public static String PROP_TRANSACTION_TYPE = "transactionType";
	public static String PROP_GIFT_CERT_CASH_BACK_AMOUNT = "giftCertCashBackAmount";
	public static String PROP_CARD_EXPIRY_MONTH = "cardExpiryMonth";
	public static String PROP_CARD_READER = "cardReader";
	public static String PROP_GIFT_CERT_NUMBER = "giftCertNumber";
	public static String PROP_TICKET = "ticket";
	public static String PROP_CARD_MERCHANT_GATEWAY = "cardMerchantGateway";
	public static String PROP_CARD_TYPE = "cardType";
	public static String PROP_CARD_AUTH_CODE = "cardAuthCode";
	public static String PROP_TRANSACTION_TIME = "transactionTime";
	public static String PROP_DRAWER_RESETTED = "drawerResetted";
	public static String PROP_REASON = "reason";
	public static String PROP_CARD_NUMBER = "cardNumber";
	public static String PROP_GIFT_CERT_FACE_VALUE = "giftCertFaceValue";
	public static String PROP_AMOUNT = "amount";
	public static String PROP_CAPTURED = "captured";
	public static String PROP_CARD_EXPIRY_YEAR = "cardExpiryYear";
	public static String PROP_TERMINAL = "terminal";
	public static String PROP_NOTE = "note";
	public static String PROP_CARD_TRACK = "cardTrack";
	public static String PROP_PAYMENT_TYPE = "paymentType";
	public static String PROP_TIPS_AMOUNT = "tipsAmount";
	public static String PROP_TENDER_AMOUNT = "tenderAmount";
	public static String PROP_CARD_TRANSACTION_ID = "cardTransactionId";
	public static String PROP_ID = "id";
	public static String PROP_GIFT_CERT_PAID_AMOUNT = "giftCertPaidAmount";


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

	/**
	 * Constructor for required fields
	 */
	public BasePosTransaction (
		java.lang.Integer id,
		java.lang.String transactionType,
		java.lang.String paymentType) {

		this.setId(id);
		this.setTransactionType(transactionType);
		this.setPaymentType(paymentType);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.util.Date transactionTime;
		protected java.lang.Double amount;
		protected java.lang.Double tipsAmount;
		protected java.lang.Double tenderAmount;
		protected java.lang.String transactionType;
		protected java.lang.String paymentType;
		protected java.lang.Boolean captured;
		protected java.lang.Boolean authorizable;
		protected java.lang.String cardTrack;
		protected java.lang.String cardNumber;
		protected java.lang.String cardAuthCode;
		protected java.lang.String cardType;
		protected java.lang.String cardTransactionId;
		protected java.lang.String cardMerchantGateway;
		protected java.lang.String cardExpiryMonth;
		protected java.lang.String cardExpiryYear;
		protected java.lang.String cardReader;
		protected java.lang.String giftCertNumber;
		protected java.lang.Double giftCertFaceValue;
		protected java.lang.Double giftCertPaidAmount;
		protected java.lang.Double giftCertCashBackAmount;
		protected java.lang.Boolean drawerResetted;
		protected java.lang.String note;

	// many to one
	private com.floreantpos.model.Terminal terminal;
	private com.floreantpos.model.Ticket ticket;
	private com.floreantpos.model.User user;
	private com.floreantpos.model.PayoutReason reason;
	private com.floreantpos.model.PayoutRecepient recepient;

	// collections
	private java.util.Map<String, String> properties;



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
	 * Return the value associated with the column: TIPS_AMOUNT
	 */
	public java.lang.Double getTipsAmount () {
									return tipsAmount == null ? Double.valueOf(0) : tipsAmount;
					}

	/**
	 * Set the value related to the column: TIPS_AMOUNT
	 * @param tipsAmount the TIPS_AMOUNT value
	 */
	public void setTipsAmount (java.lang.Double tipsAmount) {
		this.tipsAmount = tipsAmount;
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
	 * Return the value associated with the column: PAYMENT_SUB_TYPE
	 */
	public java.lang.String getPaymentType () {
					return paymentType;
			}

	/**
	 * Set the value related to the column: PAYMENT_SUB_TYPE
	 * @param paymentType the PAYMENT_SUB_TYPE value
	 */
	public void setPaymentType (java.lang.String paymentType) {
		this.paymentType = paymentType;
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
	 * Return the value associated with the column: AUTHORIZABLE
	 */
	public java.lang.Boolean isAuthorizable () {
								return authorizable == null ? Boolean.FALSE : authorizable;
					}

	/**
	 * Set the value related to the column: AUTHORIZABLE
	 * @param authorizable the AUTHORIZABLE value
	 */
	public void setAuthorizable (java.lang.Boolean authorizable) {
		this.authorizable = authorizable;
	}



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
	 * Return the value associated with the column: CARD_AUTH_CODE
	 */
	public java.lang.String getCardAuthCode () {
					return cardAuthCode;
			}

	/**
	 * Set the value related to the column: CARD_AUTH_CODE
	 * @param cardAuthCode the CARD_AUTH_CODE value
	 */
	public void setCardAuthCode (java.lang.String cardAuthCode) {
		this.cardAuthCode = cardAuthCode;
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
	 * Return the value associated with the column: CARD_READER
	 */
	public java.lang.String getCardReader () {
					return cardReader;
			}

	/**
	 * Set the value related to the column: CARD_READER
	 * @param cardReader the CARD_READER value
	 */
	public void setCardReader (java.lang.String cardReader) {
		this.cardReader = cardReader;
	}



	/**
	 * Return the value associated with the column: GIFT_CERT_NUMBER
	 */
	public java.lang.String getGiftCertNumber () {
					return giftCertNumber;
			}

	/**
	 * Set the value related to the column: GIFT_CERT_NUMBER
	 * @param giftCertNumber the GIFT_CERT_NUMBER value
	 */
	public void setGiftCertNumber (java.lang.String giftCertNumber) {
		this.giftCertNumber = giftCertNumber;
	}



	/**
	 * Return the value associated with the column: GIFT_CERT_FACE_VALUE
	 */
	public java.lang.Double getGiftCertFaceValue () {
									return giftCertFaceValue == null ? Double.valueOf(0) : giftCertFaceValue;
					}

	/**
	 * Set the value related to the column: GIFT_CERT_FACE_VALUE
	 * @param giftCertFaceValue the GIFT_CERT_FACE_VALUE value
	 */
	public void setGiftCertFaceValue (java.lang.Double giftCertFaceValue) {
		this.giftCertFaceValue = giftCertFaceValue;
	}



	/**
	 * Return the value associated with the column: GIFT_CERT_PAID_AMOUNT
	 */
	public java.lang.Double getGiftCertPaidAmount () {
									return giftCertPaidAmount == null ? Double.valueOf(0) : giftCertPaidAmount;
					}

	/**
	 * Set the value related to the column: GIFT_CERT_PAID_AMOUNT
	 * @param giftCertPaidAmount the GIFT_CERT_PAID_AMOUNT value
	 */
	public void setGiftCertPaidAmount (java.lang.Double giftCertPaidAmount) {
		this.giftCertPaidAmount = giftCertPaidAmount;
	}



	/**
	 * Return the value associated with the column: GIFT_CERT_CASH_BACK_AMOUNT
	 */
	public java.lang.Double getGiftCertCashBackAmount () {
									return giftCertCashBackAmount == null ? Double.valueOf(0) : giftCertCashBackAmount;
					}

	/**
	 * Set the value related to the column: GIFT_CERT_CASH_BACK_AMOUNT
	 * @param giftCertCashBackAmount the GIFT_CERT_CASH_BACK_AMOUNT value
	 */
	public void setGiftCertCashBackAmount (java.lang.Double giftCertCashBackAmount) {
		this.giftCertCashBackAmount = giftCertCashBackAmount;
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



	/**
	 * Return the value associated with the column: properties
	 */
	public java.util.Map<String, String> getProperties () {
					return properties;
			}

	/**
	 * Set the value related to the column: properties
	 * @param properties the properties value
	 */
	public void setProperties (java.util.Map<String, String> properties) {
		this.properties = properties;
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