package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the ZIP_CODE_VS_DELIVERY_CHARGE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="ZIP_CODE_VS_DELIVERY_CHARGE"
 */

public abstract class BaseZipCodeVsDeliveryCharge  implements Comparable, Serializable {

	public static String REF = "ZipCodeVsDeliveryCharge";
	public static String PROP_DELIVERY_CHARGE = "deliveryCharge";
	public static String PROP_ID = "id";
	public static String PROP_ZIP_CODE = "zipCode";


	// constructors
	public BaseZipCodeVsDeliveryCharge () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseZipCodeVsDeliveryCharge (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseZipCodeVsDeliveryCharge (
		java.lang.Integer id,
		java.lang.String zipCode,
		double deliveryCharge) {

		this.setId(id);
		this.setZipCode(zipCode);
		this.setDeliveryCharge(deliveryCharge);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String zipCode;
		protected double deliveryCharge;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="AUTO_ID"
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
	 * Return the value associated with the column: DELIVERY_CHARGE
	 */
	public double getDeliveryCharge () {
					return deliveryCharge;
			}

	/**
	 * Set the value related to the column: DELIVERY_CHARGE
	 * @param deliveryCharge the DELIVERY_CHARGE value
	 */
	public void setDeliveryCharge (double deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.ZipCodeVsDeliveryCharge)) return false;
		else {
			com.floreantpos.model.ZipCodeVsDeliveryCharge zipCodeVsDeliveryCharge = (com.floreantpos.model.ZipCodeVsDeliveryCharge) obj;
			if (null == this.getId() || null == zipCodeVsDeliveryCharge.getId()) return false;
			else return (this.getId().equals(zipCodeVsDeliveryCharge.getId()));
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