package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the DELIVERY_ADDRESS table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="DELIVERY_ADDRESS"
 */

public abstract class BaseDeliveryAddress  implements Comparable, Serializable {

	public static String REF = "DeliveryAddress";
	public static String PROP_CUSTOMER = "customer";
	public static String PROP_ADDRESS = "address";
	public static String PROP_ID = "id";


	// constructors
	public BaseDeliveryAddress () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseDeliveryAddress (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String address;

	// many to one
	private com.floreantpos.model.Customer customer;



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
	 * Return the value associated with the column: ADDRESS
	 */
	public java.lang.String getAddress () {
					return address;
			}

	/**
	 * Set the value related to the column: ADDRESS
	 * @param address the ADDRESS value
	 */
	public void setAddress (java.lang.String address) {
		this.address = address;
	}



	/**
	 * Return the value associated with the column: CUSTOMER_ID
	 */
	public com.floreantpos.model.Customer getCustomer () {
					return customer;
			}

	/**
	 * Set the value related to the column: CUSTOMER_ID
	 * @param customer the CUSTOMER_ID value
	 */
	public void setCustomer (com.floreantpos.model.Customer customer) {
		this.customer = customer;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.DeliveryAddress)) return false;
		else {
			com.floreantpos.model.DeliveryAddress deliveryAddress = (com.floreantpos.model.DeliveryAddress) obj;
			if (null == this.getId() || null == deliveryAddress.getId()) return false;
			else return (this.getId().equals(deliveryAddress.getId()));
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