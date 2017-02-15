package com.floreantpos.model.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the CUSTOM_PAYMENT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="CUSTOM_PAYMENT"
 */

public abstract class BaseCustomPayment implements Comparable, Serializable {

	public static String REF = "CustomPayment"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_REQUIRED_REF_NUMBER = "requiredRefNumber"; //$NON-NLS-1$
	public static String PROP_REF_NUMBER_FIELD_NAME = "refNumberFieldName"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$

	// constructors
	public BaseCustomPayment() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCustomPayment(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	protected java.lang.String name;
	protected java.lang.Boolean requiredRefNumber;
	protected java.lang.String refNumberFieldName;

	/**
	 * Return the unique identifier of this class
	 * @hibernate.id
	 *  generator-class="identity"
	 *  column="ID"
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: NAME
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Set the value related to the column: NAME
	 * @param name the NAME value
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Return the value associated with the column: REQUIRED_REF_NUMBER
	 */
	public java.lang.Boolean isRequiredRefNumber() {
		return requiredRefNumber == null ? Boolean.FALSE : requiredRefNumber;
	}

	/**
	 * Set the value related to the column: REQUIRED_REF_NUMBER
	 * @param requiredRefNumber the REQUIRED_REF_NUMBER value
	 */
	public void setRequiredRefNumber(java.lang.Boolean requiredRefNumber) {
		this.requiredRefNumber = requiredRefNumber;
	}

	/**
	 * Return the value associated with the column: REF_NUMBER_FIELD_NAME
	 */
	public java.lang.String getRefNumberFieldName() {
		return refNumberFieldName;
	}

	/**
	 * Set the value related to the column: REF_NUMBER_FIELD_NAME
	 * @param refNumberFieldName the REF_NUMBER_FIELD_NAME value
	 */
	public void setRefNumberFieldName(java.lang.String refNumberFieldName) {
		this.refNumberFieldName = refNumberFieldName;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.floreantpos.model.CustomPayment))
			return false;
		else {
			com.floreantpos.model.CustomPayment customPayment = (com.floreantpos.model.CustomPayment) obj;
			if (null == this.getId() || null == customPayment.getId())
				return false;
			else
				return (this.getId().equals(customPayment.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode(); //$NON-NLS-1$
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo(Object obj) {
		if (obj.hashCode() > hashCode())
			return 1;
		else if (obj.hashCode() < hashCode())
			return -1;
		else
			return 0;
	}

	public String toString() {
		return super.toString();
	}

}