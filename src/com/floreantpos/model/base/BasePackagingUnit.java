/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.model.base;

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

	public static String REF = "PackagingUnit"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_FACTOR = "factor"; //$NON-NLS-1$
	public static String PROP_SHORT_NAME = "shortName"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_DIMENSION = "dimension"; //$NON-NLS-1$
	public static String PROP_BASE_UNIT = "baseUnit"; //$NON-NLS-1$


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
		protected java.lang.String name;
		protected java.lang.String shortName;
		protected java.lang.Double factor;
		protected java.lang.Boolean baseUnit;
		protected java.lang.String dimension;



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
	 * Return the value associated with the column: SHORT_NAME
	 */
	public java.lang.String getShortName () {
					return shortName;
			}

	/**
	 * Set the value related to the column: SHORT_NAME
	 * @param shortName the SHORT_NAME value
	 */
	public void setShortName (java.lang.String shortName) {
		this.shortName = shortName;
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
	 * Return the value associated with the column: BASEUNIT
	 */
	public java.lang.Boolean isBaseUnit () {
								return baseUnit == null ? Boolean.FALSE : baseUnit;
					}

	/**
	 * Set the value related to the column: BASEUNIT
	 * @param baseUnit the BASEUNIT value
	 */
	public void setBaseUnit (java.lang.Boolean baseUnit) {
		this.baseUnit = baseUnit;
	}



	/**
	 * Return the value associated with the column: DIMENSION
	 */
	public java.lang.String getDimension () {
					return dimension;
			}

	/**
	 * Set the value related to the column: DIMENSION
	 * @param dimension the DIMENSION value
	 */
	public void setDimension (java.lang.String dimension) {
		this.dimension = dimension;
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