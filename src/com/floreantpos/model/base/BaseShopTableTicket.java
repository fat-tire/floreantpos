package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the SHOP_TABLE_STATUS table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="SHOP_TABLE_STATUS"
 */

public abstract class BaseShopTableTicket  implements Comparable, Serializable {

	public static String REF = "ShopTableTicket"; //$NON-NLS-1$
	public static String PROP_TICKET_ID = "ticketId"; //$NON-NLS-1$
	public static String PROP_USER_NAME = "userName"; //$NON-NLS-1$
	public static String PROP_USER_ID = "userId"; //$NON-NLS-1$


	// constructors
	public BaseShopTableTicket () {
		initialize();
	}

	protected void initialize () {}



	// fields
		protected java.lang.Integer ticketId;
		protected java.lang.Integer userId;
		protected java.lang.String userName;






	/**
	 * Return the value associated with the column: TICKET_ID
	 */
	public java.lang.Integer getTicketId () {
									return ticketId == null ? Integer.valueOf(0) : ticketId;
					}

	/**
	 * Set the value related to the column: TICKET_ID
	 * @param ticketId the TICKET_ID value
	 */
	public void setTicketId (java.lang.Integer ticketId) {
		this.ticketId = ticketId;
	}



	/**
	 * Return the value associated with the column: USER_ID
	 */
	public java.lang.Integer getUserId () {
									return userId == null ? Integer.valueOf(0) : userId;
					}

	/**
	 * Set the value related to the column: USER_ID
	 * @param userId the USER_ID value
	 */
	public void setUserId (java.lang.Integer userId) {
		this.userId = userId;
	}



	/**
	 * Return the value associated with the column: USER_NAME
	 */
	public java.lang.String getUserName () {
					return userName;
			}

	/**
	 * Set the value related to the column: USER_NAME
	 * @param userName the USER_NAME value
	 */
	public void setUserName (java.lang.String userName) {
		this.userName = userName;
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