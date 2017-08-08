package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the TAX_GROUP table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TAX_GROUP"
 */

public abstract class BaseTaxGroup  implements Comparable, Serializable {

	public static String REF = "TaxGroup"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$


	// constructors
	public BaseTaxGroup () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTaxGroup (java.lang.String id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseTaxGroup (
		java.lang.String id,
		java.lang.String name) {

		this.setId(id);
		this.setName(name);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.String id;

	// fields
		protected java.lang.String name;

	// collections
	private java.util.List<com.floreantpos.model.Tax> taxes;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="com.floreantpos.util.GlobalIdGenerator"
     *  column="ID"
     */
	public java.lang.String getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.String id) {
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
	 * Return the value associated with the column: taxes
	 */
	public java.util.List<com.floreantpos.model.Tax> getTaxes () {
					return taxes;
			}

	/**
	 * Set the value related to the column: taxes
	 * @param taxes the taxes value
	 */
	public void setTaxes (java.util.List<com.floreantpos.model.Tax> taxes) {
		this.taxes = taxes;
	}

	public void addTotaxes (com.floreantpos.model.Tax tax) {
		if (null == getTaxes()) setTaxes(new java.util.ArrayList<com.floreantpos.model.Tax>());
		getTaxes().add(tax);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.TaxGroup)) return false;
		else {
			com.floreantpos.model.TaxGroup taxGroup = (com.floreantpos.model.TaxGroup) obj;
			if (null == this.getId() || null == taxGroup.getId()) return false;
			else return (this.getId().equals(taxGroup.getId()));
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