package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the TABLE_BOOKING_INFO table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TABLE_BOOKING_INFO"
 */

public abstract class BaseTableBookingInfo  implements Comparable, Serializable {

	public static String REF = "TableBookingInfo";
	public static String PROP_CUSTOMER = "customer";
	public static String PROP_USER = "user";
	public static String PROP_STATUS = "status";
	public static String PROP_TO_DATE = "toDate";
	public static String PROP_GUEST_COUNT = "guestCount";
	public static String PROP_FROM_DATE = "fromDate";
	public static String PROP_ID = "id";
	public static String PROP_BOOKING_CONFIRM = "bookingConfirm";
	public static String PROP_PAID_AMOUNT = "paidAmount";
	public static String PROP_TOTAL_AMOUUNT = "totalAmouunt";
	public static String PROP_PAYMENT_STATUS = "paymentStatus";
	public static String PROP_BOOKING_TYPE = "bookingType";


	// constructors
	public BaseTableBookingInfo () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTableBookingInfo (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.util.Date fromDate;
		protected java.util.Date toDate;
		protected java.lang.Integer guestCount;
		protected java.lang.String status;
		protected java.lang.String paymentStatus;
		protected java.lang.String bookingConfirm;
		protected java.lang.Integer totalAmouunt;
		protected java.lang.Integer paidAmount;
		protected java.lang.String bookingType;

	// many to one
	private com.floreantpos.model.User user;
	private com.floreantpos.model.Customer customer;

	// collections
	private java.util.List<com.floreantpos.model.ShopTable> tables;



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
	 * Return the value associated with the column: FROM_DATE
	 */
	public java.util.Date getFromDate () {
					return fromDate;
			}

	/**
	 * Set the value related to the column: FROM_DATE
	 * @param fromDate the FROM_DATE value
	 */
	public void setFromDate (java.util.Date fromDate) {
		this.fromDate = fromDate;
	}



	/**
	 * Return the value associated with the column: TO_DATE
	 */
	public java.util.Date getToDate () {
					return toDate;
			}

	/**
	 * Set the value related to the column: TO_DATE
	 * @param toDate the TO_DATE value
	 */
	public void setToDate (java.util.Date toDate) {
		this.toDate = toDate;
	}



	/**
	 * Return the value associated with the column: GUEST_COUNT
	 */
	public java.lang.Integer getGuestCount () {
									return guestCount == null ? Integer.valueOf(0) : guestCount;
					}

	/**
	 * Set the value related to the column: GUEST_COUNT
	 * @param guestCount the GUEST_COUNT value
	 */
	public void setGuestCount (java.lang.Integer guestCount) {
		this.guestCount = guestCount;
	}



	/**
	 * Return the value associated with the column: STATUS
	 */
	public java.lang.String getStatus () {
					return status;
			}

	/**
	 * Set the value related to the column: STATUS
	 * @param status the STATUS value
	 */
	public void setStatus (java.lang.String status) {
		this.status = status;
	}



	/**
	 * Return the value associated with the column: PAYMENT_STATUS
	 */
	public java.lang.String getPaymentStatus () {
					return paymentStatus;
			}

	/**
	 * Set the value related to the column: PAYMENT_STATUS
	 * @param paymentStatus the PAYMENT_STATUS value
	 */
	public void setPaymentStatus (java.lang.String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}



	/**
	 * Return the value associated with the column: BOOKING_CONFIRM
	 */
	public java.lang.String getBookingConfirm () {
					return bookingConfirm;
			}

	/**
	 * Set the value related to the column: BOOKING_CONFIRM
	 * @param bookingConfirm the BOOKING_CONFIRM value
	 */
	public void setBookingConfirm (java.lang.String bookingConfirm) {
		this.bookingConfirm = bookingConfirm;
	}



	/**
	 * Return the value associated with the column: TOTAL_AMOUNT
	 */
	public java.lang.Integer getTotalAmouunt () {
									return totalAmouunt == null ? Integer.valueOf(0) : totalAmouunt;
					}

	/**
	 * Set the value related to the column: TOTAL_AMOUNT
	 * @param totalAmouunt the TOTAL_AMOUNT value
	 */
	public void setTotalAmouunt (java.lang.Integer totalAmouunt) {
		this.totalAmouunt = totalAmouunt;
	}



	/**
	 * Return the value associated with the column: PAID_AMOUNT
	 */
	public java.lang.Integer getPaidAmount () {
									return paidAmount == null ? Integer.valueOf(0) : paidAmount;
					}

	/**
	 * Set the value related to the column: PAID_AMOUNT
	 * @param paidAmount the PAID_AMOUNT value
	 */
	public void setPaidAmount (java.lang.Integer paidAmount) {
		this.paidAmount = paidAmount;
	}



	/**
	 * Return the value associated with the column: BOOKING_TYPE
	 */
	public java.lang.String getBookingType () {
					return bookingType;
			}

	/**
	 * Set the value related to the column: BOOKING_TYPE
	 * @param bookingType the BOOKING_TYPE value
	 */
	public void setBookingType (java.lang.String bookingType) {
		this.bookingType = bookingType;
	}



	/**
	 * Return the value associated with the column: user_id
	 */
	public com.floreantpos.model.User getUser () {
					return user;
			}

	/**
	 * Set the value related to the column: user_id
	 * @param user the user_id value
	 */
	public void setUser (com.floreantpos.model.User user) {
		this.user = user;
	}



	/**
	 * Return the value associated with the column: customer_id
	 */
	public com.floreantpos.model.Customer getCustomer () {
					return customer;
			}

	/**
	 * Set the value related to the column: customer_id
	 * @param customer the customer_id value
	 */
	public void setCustomer (com.floreantpos.model.Customer customer) {
		this.customer = customer;
	}



	/**
	 * Return the value associated with the column: tables
	 */
	public java.util.List<com.floreantpos.model.ShopTable> getTables () {
					return tables;
			}

	/**
	 * Set the value related to the column: tables
	 * @param tables the tables value
	 */
	public void setTables (java.util.List<com.floreantpos.model.ShopTable> tables) {
		this.tables = tables;
	}

	public void addTotables (com.floreantpos.model.ShopTable shopTable) {
		if (null == getTables()) setTables(new java.util.ArrayList<com.floreantpos.model.ShopTable>());
		getTables().add(shopTable);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.TableBookingInfo)) return false;
		else {
			com.floreantpos.model.TableBookingInfo tableBookingInfo = (com.floreantpos.model.TableBookingInfo) obj;
			if (null == this.getId() || null == tableBookingInfo.getId()) return false;
			else return (this.getId().equals(tableBookingInfo.getId()));
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