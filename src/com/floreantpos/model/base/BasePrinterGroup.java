package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the PRINTER_GROUP table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="PRINTER_GROUP"
 */

public abstract class BasePrinterGroup  implements Comparable, Serializable {

	public static String REF = "PrinterGroup"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$


	// constructors
	public BasePrinterGroup () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BasePrinterGroup (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BasePrinterGroup (
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
	private java.util.List<String> printerNames;



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
	 * Return the value associated with the column: printers
	 */
	public java.util.List<String> getPrinterNames () {
					return printerNames;
			}

	/**
	 * Set the value related to the column: printers
	 * @param printers the printers value
	 */
	public void setPrinterNames (java.util.List<String> printers) {
		this.printerNames = printers;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.PrinterGroup)) return false;
		else {
			com.floreantpos.model.PrinterGroup printerGroup = (com.floreantpos.model.PrinterGroup) obj;
			if (null == this.getId() || null == printerGroup.getId()) return false;
			else return (this.getId().equals(printerGroup.getId()));
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