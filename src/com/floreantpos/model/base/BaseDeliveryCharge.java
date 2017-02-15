package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the DELIVERY_CHARGE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="DELIVERY_CHARGE"
 */

public abstract class BaseDeliveryCharge  implements Comparable, Serializable {

	public static String REF = "DeliveryCharge";
	public static String PROP_CHARGE_AMOUNT = "chargeAmount";
	public static String PROP_NAME = "name";
	public static String PROP_START_RANGE = "startRange";
	public static String PROP_ID = "id";
	public static String PROP_END_RANGE = "endRange";
	public static String PROP_ZIP_CODE = "zipCode";


	// constructors
	public BaseDeliveryCharge () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseDeliveryCharge (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String name;
		protected java.lang.String zipCode;
		protected java.lang.Double startRange;
		protected java.lang.Double endRange;
		protected java.lang.Double chargeAmount;



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



	/**
	 * Return the value associated with the column: ZIP_CODE
	 */
	public java.lang.String getZipCode () {
					return zipCode;
			}

	/**
	 * Set the value related to the column: ZIP_CODE
	 * @param zipCode the ZIP_CODE value
	 */
	public void setZipCode (java.lang.String zipCode) {
		this.zipCode = zipCode;
	}



	/**
	 * Return the value associated with the column: START_RANGE
	 */
	public java.lang.Double getStartRange () {
									return startRange == null ? Double.valueOf(0) : startRange;
					}

	/**
	 * Set the value related to the column: START_RANGE
	 * @param startRange the START_RANGE value
	 */
	public void setStartRange (java.lang.Double startRange) {
		this.startRange = startRange;
	}



	/**
	 * Return the value associated with the column: END_RANGE
	 */
	public java.lang.Double getEndRange () {
									return endRange == null ? Double.valueOf(0) : endRange;
					}

	/**
	 * Set the value related to the column: END_RANGE
	 * @param endRange the END_RANGE value
	 */
	public void setEndRange (java.lang.Double endRange) {
		this.endRange = endRange;
	}



	/**
	 * Return the value associated with the column: CHARGE_AMOUNT
	 */
	public java.lang.Double getChargeAmount () {
									return chargeAmount == null ? Double.valueOf(0) : chargeAmount;
					}

	/**
	 * Set the value related to the column: CHARGE_AMOUNT
	 * @param chargeAmount the CHARGE_AMOUNT value
	 */
	public void setChargeAmount (java.lang.Double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.DeliveryCharge)) return false;
		else {
			com.floreantpos.model.DeliveryCharge deliveryCharge = (com.floreantpos.model.DeliveryCharge) obj;
			if (null == this.getId() || null == deliveryCharge.getId()) return false;
			else return (this.getId().equals(deliveryCharge.getId()));
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