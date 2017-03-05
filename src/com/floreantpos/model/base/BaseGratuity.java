package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the GRATUITY table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="GRATUITY"
 */

public abstract class BaseGratuity  implements Comparable, Serializable {

	public static String REF = "Gratuity"; //$NON-NLS-1$
	public static String PROP_REFUNDED = "refunded"; //$NON-NLS-1$
	public static String PROP_OWNER = "owner"; //$NON-NLS-1$
	public static String PROP_PAID = "paid"; //$NON-NLS-1$
	public static String PROP_TICKET = "ticket"; //$NON-NLS-1$
	public static String PROP_AMOUNT = "amount"; //$NON-NLS-1$
	public static String PROP_TERMINAL = "terminal"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$


	// constructors
	public BaseGratuity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseGratuity (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.Double amount;
		protected java.lang.Boolean paid;
		protected java.lang.Boolean refunded;

	// many to one
	private com.floreantpos.model.Ticket ticket;
	private com.floreantpos.model.User owner;
	private com.floreantpos.model.Terminal terminal;



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
	 * Return the value associated with the column: AMOUNT
	 */
	public java.lang.Double getAmount () {
									return amount == null ? Double.valueOf(0) : amount;
					}

	/**
	 * Set the value related to the column: AMOUNT
	 * @param amount the AMOUNT value
	 */
	public void setAmount (java.lang.Double amount) {
		this.amount = amount;
	}



	/**
	 * Return the value associated with the column: PAID
	 */
	public java.lang.Boolean isPaid () {
								return paid == null ? Boolean.FALSE : paid;
					}

	/**
	 * Set the value related to the column: PAID
	 * @param paid the PAID value
	 */
	public void setPaid (java.lang.Boolean paid) {
		this.paid = paid;
	}



	/**
	 * Return the value associated with the column: REFUNDED
	 */
	public java.lang.Boolean isRefunded () {
								return refunded == null ? Boolean.FALSE : refunded;
					}

	/**
	 * Set the value related to the column: REFUNDED
	 * @param refunded the REFUNDED value
	 */
	public void setRefunded (java.lang.Boolean refunded) {
		this.refunded = refunded;
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





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Gratuity)) return false;
		else {
			com.floreantpos.model.Gratuity gratuity = (com.floreantpos.model.Gratuity) obj;
			if (null == this.getId() || null == gratuity.getId()) return this == obj;
			else return (this.getId().equals(gratuity.getId()));
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