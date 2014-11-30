package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the PACKAGING_UNIT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="PACKAGING_UNIT"
 */

public abstract class BasePackagingUnit  implements Comparable, Serializable {

	public static String REF = "PackagingUnit";
	public static String PROP_UNIT_NAME = "unitName";
	public static String PROP_UNIT_CLASS = "unitClass";
	public static String PROP_FACTOR = "factor";
	public static String PROP_ID = "id";


	// constructors
	public BasePackagingUnit () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BasePackagingUnit (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String unitName;
		protected java.lang.Double factor;
		protected java.lang.String unitClass;



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
	 * Return the value associated with the column: UNIT_NAME
	 */
	public java.lang.String getUnitName () {
					return unitName;
			}

	/**
	 * Set the value related to the column: UNIT_NAME
	 * @param unitName the UNIT_NAME value
	 */
	public void setUnitName (java.lang.String unitName) {
		this.unitName = unitName;
	}



	/**
	 * Return the value associated with the column: FACTOR
	 */
	public java.lang.Double getFactor () {
									return factor == null ? Double.valueOf(0) : factor;
					}

	/**
	 * Set the value related to the column: FACTOR
	 * @param factor the FACTOR value
	 */
	public void setFactor (java.lang.Double factor) {
		this.factor = factor;
	}



	/**
	 * Return the value associated with the column: UNIT_CLASS
	 */
	public java.lang.String getUnitClass () {
					return unitClass;
			}

	/**
	 * Set the value related to the column: UNIT_CLASS
	 * @param unitClass the UNIT_CLASS value
	 */
	public void setUnitClass (java.lang.String unitClass) {
		this.unitClass = unitClass;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.PackagingUnit)) return false;
		else {
			com.floreantpos.model.PackagingUnit packagingUnit = (com.floreantpos.model.PackagingUnit) obj;
			if (null == this.getId() || null == packagingUnit.getId()) return false;
			else return (this.getId().equals(packagingUnit.getId()));
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