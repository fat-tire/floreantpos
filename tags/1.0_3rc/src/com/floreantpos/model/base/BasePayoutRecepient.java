package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the PAYOUT_RECEPIENTS table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="PAYOUT_RECEPIENTS"
 */

public abstract class BasePayoutRecepient  implements Comparable, Serializable {

	public static String REF = "PayoutRecepient";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";


	// constructors
	public BasePayoutRecepient () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BasePayoutRecepient (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;



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
	 * Return the value associated with the column: NAME
	 */
	public java.lang.String getName () {
			return name;
	}

	/**
	 * Set the value related to the column: NAME
	 * @param name the NAME value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.PayoutRecepient)) return false;
		else {
			com.floreantpos.model.PayoutRecepient payoutRecepient = (com.floreantpos.model.PayoutRecepient) obj;
			if (null == this.getId() || null == payoutRecepient.getId()) return false;
			else return (this.getId().equals(payoutRecepient.getId()));
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