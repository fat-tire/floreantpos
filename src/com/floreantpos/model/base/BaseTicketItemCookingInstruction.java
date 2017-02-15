package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the TICKET_ITEM table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TICKET_ITEM"
 */

public abstract class BaseTicketItemCookingInstruction  implements Comparable, Serializable {

	public static String REF = "TicketItemCookingInstruction"; //$NON-NLS-1$
	public static String PROP_DESCRIPTION = "description"; //$NON-NLS-1$
	public static String PROP_PRINTED_TO_KITCHEN = "printedToKitchen"; //$NON-NLS-1$


	// constructors
	public BaseTicketItemCookingInstruction () {
		initialize();
	}

	protected void initialize () {}



	// fields
		protected java.lang.String description;
		protected java.lang.Boolean printedToKitchen;






	/**
	 * Return the value associated with the column: description
	 */
	public java.lang.String getDescription () {
					return description;
			}

	/**
	 * Set the value related to the column: description
	 * @param description the description value
	 */
	public void setDescription (java.lang.String description) {
		this.description = description;
	}



	/**
	 * Return the value associated with the column: printedToKitchen
	 */
	public java.lang.Boolean isPrintedToKitchen () {
								return printedToKitchen == null ? Boolean.FALSE : printedToKitchen;
					}

	/**
	 * Set the value related to the column: printedToKitchen
	 * @param printedToKitchen the printedToKitchen value
	 */
	public void setPrintedToKitchen (java.lang.Boolean printedToKitchen) {
		this.printedToKitchen = printedToKitchen;
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