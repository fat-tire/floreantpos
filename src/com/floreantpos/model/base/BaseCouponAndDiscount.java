package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the COUPON_AND_DISCOUNT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="COUPON_AND_DISCOUNT"
 */

public abstract class BaseCouponAndDiscount  implements Comparable, Serializable {

	public static String REF = "CouponAndDiscount";
	public static String PROP_NAME = "name";
	public static String PROP_EXPIRY_DATE = "expiryDate";
	public static String PROP_NEVER_EXPIRE = "neverExpire";
	public static String PROP_VALUE = "value";
	public static String PROP_TYPE = "type";
	public static String PROP_DISABLED = "disabled";
	public static String PROP_ID = "id";


	// constructors
	public BaseCouponAndDiscount () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCouponAndDiscount (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;
	private java.lang.Integer type;
	private java.lang.Double value;
	private java.util.Date expiryDate;
	private java.lang.Boolean disabled;
	private java.lang.Boolean neverExpire;



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
	 * Return the value associated with the column: TYPE
	 */
	public java.lang.Integer getType () {
			return type == null ? Integer.valueOf(0) : type;
	}

	/**
	 * Set the value related to the column: TYPE
	 * @param type the TYPE value
	 */
	public void setType (java.lang.Integer type) {
		this.type = type;
	}



	/**
	 * Return the value associated with the column: VALUE
	 */
	public java.lang.Double getValue () {
					return value == null ? Double.valueOf(0) : value;
			}

	/**
	 * Set the value related to the column: VALUE
	 * @param value the VALUE value
	 */
	public void setValue (java.lang.Double value) {
		this.value = value;
	}



	/**
	 * Return the value associated with the column: EXPIRY_DATE
	 */
	public java.util.Date getExpiryDate () {
			return expiryDate;
	}

	/**
	 * Set the value related to the column: EXPIRY_DATE
	 * @param expiryDate the EXPIRY_DATE value
	 */
	public void setExpiryDate (java.util.Date expiryDate) {
		this.expiryDate = expiryDate;
	}



	/**
	 * Return the value associated with the column: DISABLED
	 */
	public java.lang.Boolean isDisabled () {
					return disabled == null ? Boolean.FALSE : disabled;
			}

	/**
	 * Set the value related to the column: DISABLED
	 * @param disabled the DISABLED value
	 */
	public void setDisabled (java.lang.Boolean disabled) {
		this.disabled = disabled;
	}



	/**
	 * Return the value associated with the column: NEVER_EXPIRE
	 */
	public java.lang.Boolean isNeverExpire () {
					return neverExpire == null ? Boolean.FALSE : neverExpire;
			}

	/**
	 * Set the value related to the column: NEVER_EXPIRE
	 * @param neverExpire the NEVER_EXPIRE value
	 */
	public void setNeverExpire (java.lang.Boolean neverExpire) {
		this.neverExpire = neverExpire;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.CouponAndDiscount)) return false;
		else {
			com.floreantpos.model.CouponAndDiscount couponAndDiscount = (com.floreantpos.model.CouponAndDiscount) obj;
			if (null == this.getId() || null == couponAndDiscount.getId()) return false;
			else return (this.getId().equals(couponAndDiscount.getId()));
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