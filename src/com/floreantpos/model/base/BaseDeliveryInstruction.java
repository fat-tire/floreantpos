package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the DELIVERY_INSTRUCTION table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="DELIVERY_INSTRUCTION"
 */

public abstract class BaseDeliveryInstruction  implements Comparable, Serializable {

	public static String REF = "DeliveryInstruction";
	public static String PROP_CUSTOMER = "customer";
	public static String PROP_NOTES = "notes";
	public static String PROP_ID = "id";


	// constructors
	public BaseDeliveryInstruction () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseDeliveryInstruction (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String notes;

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
	 * Return the value associated with the column: NOTES
	 */
	public java.lang.String getNotes () {
					return notes;
			}

	/**
	 * Set the value related to the column: NOTES
	 * @param notes the NOTES value
	 */
	public void setNotes (java.lang.String notes) {
		this.notes = notes;
	}



	/**
	 * Return the value associated with the column: CUSTOMER_NO
	 */
	public com.floreantpos.model.Customer getCustomer () {
					return customer;
			}

	/**
	 * Set the value related to the column: CUSTOMER_NO
	 * @param customer the CUSTOMER_NO value
	 */
	public void setCustomer (com.floreantpos.model.Customer customer) {
		this.customer = customer;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.DeliveryInstruction)) return false;
		else {
			com.floreantpos.model.DeliveryInstruction deliveryInstruction = (com.floreantpos.model.DeliveryInstruction) obj;
			if (null == this.getId() || null == deliveryInstruction.getId()) return false;
			else return (this.getId().equals(deliveryInstruction.getId()));
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