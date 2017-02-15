package com.floreantpos.model.base;

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

	public static String REF = "KitchenTicket"; //$NON-NLS-1$
	public static String PROP_PRINTER_GROUP = "printerGroup"; //$NON-NLS-1$
	public static String PROP_STATUS = "status"; //$NON-NLS-1$
	public static String PROP_CLOSING_DATE = "closingDate"; //$NON-NLS-1$
	public static String PROP_TICKET_TYPE = "ticketType"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_VOIDED = "voided"; //$NON-NLS-1$
	public static String PROP_SERVER_NAME = "serverName"; //$NON-NLS-1$
	public static String PROP_SEQUENCE_NUMBER = "sequenceNumber"; //$NON-NLS-1$
	public static String PROP_CREATE_DATE = "createDate"; //$NON-NLS-1$
	public static String PROP_TICKET_ID = "ticketId"; //$NON-NLS-1$


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

	// fields
		protected java.lang.Integer ticketId;
		protected java.util.Date createDate;
		protected java.util.Date closingDate;
		protected java.lang.Boolean voided;
		protected java.lang.Integer sequenceNumber;
		protected java.lang.String status;
		protected java.lang.String serverName;
		protected java.lang.String ticketType;

	// many to one
	private com.floreantpos.model.PrinterGroup printerGroup;

	// collections
	private java.util.List<Integer> tableNumbers;
	private java.util.List<com.floreantpos.model.KitchenTicketItem> ticketItems;



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
	 * Return the value associated with the column: CLOSE_DATE
	 */
	public java.util.Date getClosingDate () {
					return closingDate;
			}

	/**
	 * Set the value related to the column: CLOSE_DATE
	 * @param closingDate the CLOSE_DATE value
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
	 * Return the value associated with the column: SEQUENCE_NUMBER
	 */
	public java.lang.Integer getSequenceNumber () {
									return sequenceNumber == null ? Integer.valueOf(0) : sequenceNumber;
					}

	/**
	 * Set the value related to the column: SEQUENCE_NUMBER
	 * @param sequenceNumber the SEQUENCE_NUMBER value
	 */
	public void setSequenceNumber (java.lang.Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
	 * Return the value associated with the column: SERVER_NAME
	 */
	public java.lang.String getServerName () {
					return serverName;
			}

	/**
	 * Set the value related to the column: SERVER_NAME
	 * @param serverName the SERVER_NAME value
	 */
	public void setServerName (java.lang.String serverName) {
		this.serverName = serverName;
	}



	/**
	 * Return the value associated with the column: TICKET_TYPE
	 */
	public java.lang.String getTicketType () {
					return ticketType;
			}

	/**
	 * Set the value related to the column: TICKET_TYPE
	 * @param ticketType the TICKET_TYPE value
	 */
	public void setTicketType (java.lang.String ticketType) {
		this.ticketType = ticketType;
	}



	/**
	 * Return the value associated with the column: PG_ID
	 */
	public com.floreantpos.model.PrinterGroup getPrinterGroup () {
					return printerGroup;
			}

	/**
	 * Set the value related to the column: PG_ID
	 * @param printerGroup the PG_ID value
	 */
	public void setPrinterGroup (com.floreantpos.model.PrinterGroup printerGroup) {
		this.printerGroup = printerGroup;
	}



	/**
	 * Return the value associated with the column: tableNumbers
	 */
	public java.util.List<Integer> getTableNumbers() {
					return tableNumbers;
			}

	/**
	 * Set the value related to the column: tableNumbers
	 * @param tableNumbers the tableNumbers value
	 */
	public void setTableNumbers(java.util.List<Integer> tableNumbers) {
		this.tableNumbers = tableNumbers;
	}



	/**
	 * Return the value associated with the column: ticketItems
	 */
	public java.util.List<com.floreantpos.model.KitchenTicketItem> getTicketItems () {
					return ticketItems;
			}

	/**
	 * Set the value related to the column: ticketItems
	 * @param ticketItems the ticketItems value
	 */
	public void setTicketItems (java.util.List<com.floreantpos.model.KitchenTicketItem> ticketItems) {
		this.ticketItems = ticketItems;
	}

	public void addToticketItems (com.floreantpos.model.KitchenTicketItem kitchenTicketItem) {
		if (null == getTicketItems()) setTicketItems(new java.util.ArrayList<com.floreantpos.model.KitchenTicketItem>());
		getTicketItems().add(kitchenTicketItem);
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