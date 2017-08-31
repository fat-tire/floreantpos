package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the GUEST_CHECK_PRINT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="GUEST_CHECK_PRINT"
 */

public abstract class BaseGuestCheckPrint  implements Comparable, Serializable {

	public static String REF = "GuestCheckPrint"; //$NON-NLS-1$
	public static String PROP_TICKET_TOTAL = "ticketTotal"; //$NON-NLS-1$
	public static String PROP_TICKET_ID = "ticketId"; //$NON-NLS-1$
	public static String PROP_USER = "user"; //$NON-NLS-1$
	public static String PROP_TABLE_NO = "tableNo"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_PRINT_TIME = "printTime"; //$NON-NLS-1$


	// constructors
	public BaseGuestCheckPrint () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseGuestCheckPrint (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.Integer ticketId;
		protected java.lang.String tableNo;
		protected java.lang.Double ticketTotal;
		protected java.util.Date printTime;

	// many to one
	private com.floreantpos.model.User user;



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
	 * Return the value associated with the column: TABLE_NO
	 */
	public java.lang.String getTableNo () {
					return tableNo;
			}

	/**
	 * Set the value related to the column: TABLE_NO
	 * @param tableNo the TABLE_NO value
	 */
	public void setTableNo (java.lang.String tableNo) {
		this.tableNo = tableNo;
	}



	/**
	 * Return the value associated with the column: TICKET_TOTAL
	 */
	public java.lang.Double getTicketTotal () {
									return ticketTotal == null ? Double.valueOf(0) : ticketTotal;
					}

	/**
	 * Set the value related to the column: TICKET_TOTAL
	 * @param ticketTotal the TICKET_TOTAL value
	 */
	public void setTicketTotal (java.lang.Double ticketTotal) {
		this.ticketTotal = ticketTotal;
	}



	/**
	 * Return the value associated with the column: PRINT_TIME
	 */
	public java.util.Date getPrintTime () {
					return printTime;
			}

	/**
	 * Set the value related to the column: PRINT_TIME
	 * @param printTime the PRINT_TIME value
	 */
	public void setPrintTime (java.util.Date printTime) {
		this.printTime = printTime;
	}



	/**
	 * Return the value associated with the column: USER_ID
	 */
	public com.floreantpos.model.User getUser () {
					return user;
			}

	/**
	 * Set the value related to the column: USER_ID
	 * @param user the USER_ID value
	 */
	public void setUser (com.floreantpos.model.User user) {
		this.user = user;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.GuestCheckPrint)) return false;
		else {
			com.floreantpos.model.GuestCheckPrint guestCheckPrint = (com.floreantpos.model.GuestCheckPrint) obj;
			if (null == this.getId() || null == guestCheckPrint.getId()) return false;
			else return (this.getId().equals(guestCheckPrint.getId()));
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