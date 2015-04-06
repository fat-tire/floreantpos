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

public abstract class BaseGiftCertificateTransaction extends com.floreantpos.model.PosTransaction  implements Comparable, Serializable {

	public static String REF = "GiftCertificateTransaction";
	public static String PROP_ID = "id";


	// constructors
	public BaseGiftCertificateTransaction () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseGiftCertificateTransaction (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BaseGiftCertificateTransaction (
		java.lang.Integer id,
		java.lang.String transactionType,
		java.lang.String paymentType) {

		super (
			id,
			transactionType,
			paymentType);
	}



	private int hashCode = Integer.MIN_VALUE;









	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.GiftCertificateTransaction)) return false;
		else {
			com.floreantpos.model.GiftCertificateTransaction giftCertificateTransaction = (com.floreantpos.model.GiftCertificateTransaction) obj;
			if (null == this.getId() || null == giftCertificateTransaction.getId()) return false;
			else return (this.getId().equals(giftCertificateTransaction.getId()));
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