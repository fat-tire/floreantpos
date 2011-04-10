package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the VOID_REASONS table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="VOID_REASONS"
 */

public abstract class BaseVoidReason  implements Comparable, Serializable {

	public static String REF = "VoidReason";
	public static String PROP_REASON_TEXT = "reasonText";
	public static String PROP_ID = "id";


	// constructors
	public BaseVoidReason () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseVoidReason (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String reasonText;



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
	 * Return the value associated with the column: REASON_TEXT
	 */
	public java.lang.String getReasonText () {
			return reasonText;
	}

	/**
	 * Set the value related to the column: REASON_TEXT
	 * @param reasonText the REASON_TEXT value
	 */
	public void setReasonText (java.lang.String reasonText) {
		this.reasonText = reasonText;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.VoidReason)) return false;
		else {
			com.floreantpos.model.VoidReason voidReason = (com.floreantpos.model.VoidReason) obj;
			if (null == this.getId() || null == voidReason.getId()) return false;
			else return (this.getId().equals(voidReason.getId()));
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