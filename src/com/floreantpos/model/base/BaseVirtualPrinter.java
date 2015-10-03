package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the VIRTUAL_PRINTER table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="VIRTUAL_PRINTER"
 */

public abstract class BaseVirtualPrinter  implements Comparable, Serializable {

	public static String REF = "VirtualPrinter"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$


	// constructors
	public BaseVirtualPrinter () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseVirtualPrinter (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseVirtualPrinter (
		java.lang.Integer id,
		java.lang.String name) {

		this.setId(id);
		this.setName(name);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String name;

	// collections
	private java.util.List<String> orderTypeNames;



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
	 * Return the value associated with the column: orderTypeNames
	 */
	public java.util.List<String> getOrderTypeNames () {
					return orderTypeNames;
			}

	/**
	 * Set the value related to the column: orderTypeNames
	 * @param orderTypeNames the orderTypeNames value
	 */
	public void setOrderTypeNames (java.util.List<String> orderTypeNames) {
		this.orderTypeNames = orderTypeNames;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.VirtualPrinter)) return false;
		else {
			com.floreantpos.model.VirtualPrinter virtualPrinter = (com.floreantpos.model.VirtualPrinter) obj;
			if (null == this.getId() || null == virtualPrinter.getId()) return false;
			else return (this.getId().equals(virtualPrinter.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode(); //$NON-NLS-1$
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