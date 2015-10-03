package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the PURCHASE_ORDER table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="PURCHASE_ORDER"
 */

public abstract class BasePurchaseOrder  implements Comparable, Serializable {

	public static String REF = "PurchaseOrder"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_ORDER_ID = "orderId"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$


	// constructors
	public BasePurchaseOrder () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BasePurchaseOrder (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String orderId;
		protected java.lang.String name;



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
	 * Return the value associated with the column: ORDER_ID
	 */
	public java.lang.String getOrderId () {
					return orderId;
			}

	/**
	 * Set the value related to the column: ORDER_ID
	 * @param orderId the ORDER_ID value
	 */
	public void setOrderId (java.lang.String orderId) {
		this.orderId = orderId;
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
		if (!(obj instanceof com.floreantpos.model.PurchaseOrder)) return false;
		else {
			com.floreantpos.model.PurchaseOrder purchaseOrder = (com.floreantpos.model.PurchaseOrder) obj;
			if (null == this.getId() || null == purchaseOrder.getId()) return false;
			else return (this.getId().equals(purchaseOrder.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode(); //$NON-NLS-1$
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