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

public abstract class BaseGiftCertificateTransaction extends com.floreantpos.model.PosTransaction  implements Comparable, Serializable {

	public static String REF = "GiftCertificateTransaction";
	public static String PROP_FACE_VALUE = "faceValue";
	public static String PROP_ID = "id";
	public static String PROP_CASH_BACK_AMOUNT = "cashBackAmount";
	public static String PROP_PAID_AMOUNT = "paidAmount";


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



	private int hashCode = Integer.MIN_VALUE;


	// fields
		protected double faceValue;
		protected double paidAmount;
		protected double cashBackAmount;






	/**
	 * Return the value associated with the column: FACE_VALUE
	 */
	public double getFaceValue () {
					return faceValue;
			}

	/**
	 * Set the value related to the column: FACE_VALUE
	 * @param faceValue the FACE_VALUE value
	 */
	public void setFaceValue (double faceValue) {
		this.faceValue = faceValue;
	}



	/**
	 * Return the value associated with the column: PAID_AMOUNT
	 */
	public double getPaidAmount () {
					return paidAmount;
			}

	/**
	 * Set the value related to the column: PAID_AMOUNT
	 * @param paidAmount the PAID_AMOUNT value
	 */
	public void setPaidAmount (double paidAmount) {
		this.paidAmount = paidAmount;
	}



	/**
	 * Return the value associated with the column: CASH_BACK_AMOUNT
	 */
	public double getCashBackAmount () {
					return cashBackAmount;
			}

	/**
	 * Set the value related to the column: CASH_BACK_AMOUNT
	 * @param cashBackAmount the CASH_BACK_AMOUNT value
	 */
	public void setCashBackAmount (double cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}





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