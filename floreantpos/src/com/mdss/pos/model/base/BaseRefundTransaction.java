package com.mdss.pos.model.base;

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

public abstract class BaseRefundTransaction extends com.mdss.pos.model.PosTransaction  implements Comparable, Serializable {

	public static String REF = "RefundTransaction";
	public static String PROP_ID = "id";


	// constructors
	public BaseRefundTransaction () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRefundTransaction (java.lang.Integer id) {
		super(id);
	}



	private int hashCode = Integer.MIN_VALUE;









	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.mdss.pos.model.RefundTransaction)) return false;
		else {
			com.mdss.pos.model.RefundTransaction refundTransaction = (com.mdss.pos.model.RefundTransaction) obj;
			if (null == this.getId() || null == refundTransaction.getId()) return false;
			else return (this.getId().equals(refundTransaction.getId()));
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