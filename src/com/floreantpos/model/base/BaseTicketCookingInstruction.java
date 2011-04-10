package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the TICKET table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TICKET"
 */

public abstract class BaseTicketCookingInstruction  implements Comparable, Serializable {

	public static String REF = "TicketCookingInstruction";
	public static String PROP_PRINTED_TO_KITCHEN = "printedToKitchen";
	public static String PROP_DESCRIPTION = "description";


	// constructors
	public BaseTicketCookingInstruction () {
		initialize();
	}

	protected void initialize () {}



	// fields
	private java.lang.String description;
	private java.lang.Boolean printedToKitchen;






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