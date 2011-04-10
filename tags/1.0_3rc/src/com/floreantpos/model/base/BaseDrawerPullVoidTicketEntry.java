package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the DRAWER_PULL_REPORT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="DRAWER_PULL_REPORT"
 */

public abstract class BaseDrawerPullVoidTicketEntry  implements Comparable, Serializable {

	public static String REF = "DrawerPullVoidTicketEntry";
	public static String PROP_AMOUNT = "amount";
	public static String PROP_QUANTITY = "quantity";
	public static String PROP_HAST = "hast";
	public static String PROP_CODE = "code";
	public static String PROP_REASON = "reason";


	// constructors
	public BaseDrawerPullVoidTicketEntry () {
		initialize();
	}

	protected void initialize () {}



	// fields
	private java.lang.Integer code;
	private java.lang.String reason;
	private java.lang.String hast;
	private java.lang.Integer quantity;
	private java.lang.Double amount;






	/**
	 * Return the value associated with the column: code
	 */
	public java.lang.Integer getCode () {
			return code == null ? Integer.valueOf(0) : code;
	}

	/**
	 * Set the value related to the column: code
	 * @param code the code value
	 */
	public void setCode (java.lang.Integer code) {
		this.code = code;
	}



	/**
	 * Return the value associated with the column: reason
	 */
	public java.lang.String getReason () {
			return reason;
	}

	/**
	 * Set the value related to the column: reason
	 * @param reason the reason value
	 */
	public void setReason (java.lang.String reason) {
		this.reason = reason;
	}



	/**
	 * Return the value associated with the column: hast
	 */
	public java.lang.String getHast () {
			return hast;
	}

	/**
	 * Set the value related to the column: hast
	 * @param hast the hast value
	 */
	public void setHast (java.lang.String hast) {
		this.hast = hast;
	}



	/**
	 * Return the value associated with the column: quantity
	 */
	public java.lang.Integer getQuantity () {
			return quantity == null ? Integer.valueOf(0) : quantity;
	}

	/**
	 * Set the value related to the column: quantity
	 * @param quantity the quantity value
	 */
	public void setQuantity (java.lang.Integer quantity) {
		this.quantity = quantity;
	}



	/**
	 * Return the value associated with the column: amount
	 */
	public java.lang.Double getAmount () {
					return amount == null ? Double.valueOf(0) : amount;
			}

	/**
	 * Set the value related to the column: amount
	 * @param amount the amount value
	 */
	public void setAmount (java.lang.Double amount) {
		this.amount = amount;
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