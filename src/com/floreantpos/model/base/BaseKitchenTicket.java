package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the KITCHEN_TICKET table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="KITCHEN_TICKET"
 */

public abstract class BaseKitchenTicket  implements Comparable, Serializable {

	public static String REF = "KitchenTicket";
	public static String PROP_OWNER = "owner";
	public static String PROP_STATUS = "status";
	public static String PROP_TABLE_NUMBER = "tableNumber";
	public static String PROP_TERMINAL = "terminal";
	public static String PROP_VOID_REASON = "voidReason";
	public static String PROP_CLOSING_DATE = "closingDate";
	public static String PROP_ID = "id";
	public static String PROP_VOIDED = "voided";
	public static String PROP_CREATE_DATE = "createDate";
	public static String PROP_TICKET_ID = "ticketId";


	// constructors
	public BaseKitchenTicket () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseKitchenTicket (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	 java.util.Date modifiedTime;

	// fields
		protected java.lang.Integer ticketId;
		protected java.util.Date createDate;
		protected java.util.Date closingDate;
		protected java.lang.Boolean voided;
		protected java.lang.String voidReason;
		protected java.lang.Integer tableNumber;
		protected java.lang.String status;

	// many to one
	private com.floreantpos.model.User owner;
	private com.floreantpos.model.Terminal terminal;

	// collections
	private java.util.List<com.floreantpos.model.TicketItem> ticketItems;



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
	 * Return the value associated with the column: MODIFIED_TIME
	 */
	public java.util.Date getModifiedTime () {
					return modifiedTime;
			}

	/**
	 * Set the value related to the column: MODIFIED_TIME
	 * @param modifiedTime the MODIFIED_TIME value
	 */
	public void setModifiedTime (java.util.Date modifiedTime) {
		this.modifiedTime = modifiedTime;
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
	 * Return the value associated with the column: CREATE_DATE
	 */
	public java.util.Date getCreateDate () {
					return createDate;
			}

	/**
	 * Set the value related to the column: CREATE_DATE
	 * @param createDate the CREATE_DATE value
	 */
	public void setCreateDate (java.util.Date createDate) {
		this.createDate = createDate;
	}



	/**
	 * Return the value associated with the column: CLOSING_DATE
	 */
	public java.util.Date getClosingDate () {
					return closingDate;
			}

	/**
	 * Set the value related to the column: CLOSING_DATE
	 * @param closingDate the CLOSING_DATE value
	 */
	public void setClosingDate (java.util.Date closingDate) {
		this.closingDate = closingDate;
	}



	/**
	 * Return the value associated with the column: VOIDED
	 */
	public java.lang.Boolean isVoided () {
								return voided == null ? Boolean.FALSE : voided;
					}

	/**
	 * Set the value related to the column: VOIDED
	 * @param voided the VOIDED value
	 */
	public void setVoided (java.lang.Boolean voided) {
		this.voided = voided;
	}



	/**
	 * Return the value associated with the column: VOID_REASON
	 */
	public java.lang.String getVoidReason () {
					return voidReason;
			}

	/**
	 * Set the value related to the column: VOID_REASON
	 * @param voidReason the VOID_REASON value
	 */
	public void setVoidReason (java.lang.String voidReason) {
		this.voidReason = voidReason;
	}



	/**
	 * Return the value associated with the column: TABLE_NUMBER
	 */
	public java.lang.Integer getTableNumber () {
					return tableNumber == null ? Integer.valueOf(0) : tableNumber;
			}

	/**
	 * Set the value related to the column: TABLE_NUMBER
	 * @param tableNumber the TABLE_NUMBER value
	 */
	public void setTableNumber (java.lang.Integer tableNumber) {
		this.tableNumber = tableNumber;
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
	 * Return the value associated with the column: OWNER_ID
	 */
	public com.floreantpos.model.User getOwner () {
					return owner;
			}

	/**
	 * Set the value related to the column: OWNER_ID
	 * @param owner the OWNER_ID value
	 */
	public void setOwner (com.floreantpos.model.User owner) {
		this.owner = owner;
	}



	/**
	 * Return the value associated with the column: TERMINAL_ID
	 */
	public com.floreantpos.model.Terminal getTerminal () {
					return terminal;
			}

	/**
	 * Set the value related to the column: TERMINAL_ID
	 * @param terminal the TERMINAL_ID value
	 */
	public void setTerminal (com.floreantpos.model.Terminal terminal) {
		this.terminal = terminal;
	}



	/**
	 * Return the value associated with the column: ticketItems
	 */
	public java.util.List<com.floreantpos.model.TicketItem> getTicketItems () {
					return ticketItems;
			}

	/**
	 * Set the value related to the column: ticketItems
	 * @param ticketItems the ticketItems value
	 */
	public void setTicketItems (java.util.List<com.floreantpos.model.TicketItem> ticketItems) {
		this.ticketItems = ticketItems;
	}

	public void addToticketItems (com.floreantpos.model.TicketItem ticketItem) {
		if (null == getTicketItems()) setTicketItems(new java.util.ArrayList<com.floreantpos.model.TicketItem>());
		getTicketItems().add(ticketItem);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.KitchenTicket)) return false;
		else {
			com.floreantpos.model.KitchenTicket kitchenTicket = (com.floreantpos.model.KitchenTicket) obj;
			if (null == this.getId() || null == kitchenTicket.getId()) return false;
			else return (this.getId().equals(kitchenTicket.getId()));
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