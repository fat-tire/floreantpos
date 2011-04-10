package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the TICKET_COUPON_DISCOUNT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TICKET_COUPON_DISCOUNT"
 */

public abstract class BaseTicketCouponAndDiscount  implements Comparable, Serializable {

	public static String REF = "TicketCouponAndDiscount";
	public static String PROP_NAME = "name";
	public static String PROP_COUPON_AND_DISCOUNT_ID = "couponAndDiscountId";
	public static String PROP_VALUE = "value";
	public static String PROP_TYPE = "type";
	public static String PROP_ID = "id";


	// constructors
	public BaseTicketCouponAndDiscount () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTicketCouponAndDiscount (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer couponAndDiscountId;
	private java.lang.String name;
	private java.lang.Integer type;
	private java.lang.Double value;



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
	 * Return the value associated with the column: COUPON_DISCOUNT_ID
	 */
	public java.lang.Integer getCouponAndDiscountId () {
			return couponAndDiscountId == null ? Integer.valueOf(0) : couponAndDiscountId;
	}

	/**
	 * Set the value related to the column: COUPON_DISCOUNT_ID
	 * @param couponAndDiscountId the COUPON_DISCOUNT_ID value
	 */
	public void setCouponAndDiscountId (java.lang.Integer couponAndDiscountId) {
		this.couponAndDiscountId = couponAndDiscountId;
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





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.TicketCouponAndDiscount)) return false;
		else {
			com.floreantpos.model.TicketCouponAndDiscount ticketCouponAndDiscount = (com.floreantpos.model.TicketCouponAndDiscount) obj;
			if (null == this.getId() || null == ticketCouponAndDiscount.getId()) return false;
			else return (this.getId().equals(ticketCouponAndDiscount.getId()));
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