package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the TICKET_DISCOUNT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TICKET_DISCOUNT"
 */

public abstract class BaseTicketDiscount  implements Comparable, Serializable {

	public static String REF = "TicketDiscount";
	public static String PROP_MINIMUM_AMOUNT = "minimumAmount";
	public static String PROP_NAME = "name";
	public static String PROP_TICKET = "ticket";
	public static String PROP_VALUE = "value";
	public static String PROP_DISCOUNT_ID = "discountId";
	public static String PROP_TYPE = "type";
	public static String PROP_ID = "id";
	public static String PROP_AUTO_APPLY = "autoApply";


	// constructors
	public BaseTicketDiscount () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTicketDiscount (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.Integer discountId;
		protected java.lang.String name;
		protected java.lang.Integer type;
		protected java.lang.Boolean autoApply;
		protected java.lang.Integer minimumAmount;
		protected java.lang.Double value;

	// many to one
	private com.floreantpos.model.Ticket ticket;



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
	 * Return the value associated with the column: DISCOUNT_ID
	 */
	public java.lang.Integer getDiscountId () {
									return discountId == null ? Integer.valueOf(0) : discountId;
					}

	/**
	 * Set the value related to the column: DISCOUNT_ID
	 * @param discountId the DISCOUNT_ID value
	 */
	public void setDiscountId (java.lang.Integer discountId) {
		this.discountId = discountId;
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
	 * Return the value associated with the column: AUTO_APPLY
	 */
	public java.lang.Boolean isAutoApply () {
								return autoApply == null ? Boolean.FALSE : autoApply;
					}

	/**
	 * Set the value related to the column: AUTO_APPLY
	 * @param autoApply the AUTO_APPLY value
	 */
	public void setAutoApply (java.lang.Boolean autoApply) {
		this.autoApply = autoApply;
	}



	/**
	 * Return the value associated with the column: MINIMUM_AMOUNT
	 */
	public java.lang.Integer getMinimumAmount () {
									return minimumAmount == null ? Integer.valueOf(0) : minimumAmount;
					}

	/**
	 * Set the value related to the column: MINIMUM_AMOUNT
	 * @param minimumAmount the MINIMUM_AMOUNT value
	 */
	public void setMinimumAmount (java.lang.Integer minimumAmount) {
		this.minimumAmount = minimumAmount;
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
	 * Return the value associated with the column: TICKET_ID
	 */
	public com.floreantpos.model.Ticket getTicket () {
					return ticket;
			}

	/**
	 * Set the value related to the column: TICKET_ID
	 * @param ticket the TICKET_ID value
	 */
	public void setTicket (com.floreantpos.model.Ticket ticket) {
		this.ticket = ticket;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.TicketDiscount)) return false;
		else {
			com.floreantpos.model.TicketDiscount ticketDiscount = (com.floreantpos.model.TicketDiscount) obj;
			if (null == this.getId() || null == ticketDiscount.getId()) return false;
			else return (this.getId().equals(ticketDiscount.getId()));
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