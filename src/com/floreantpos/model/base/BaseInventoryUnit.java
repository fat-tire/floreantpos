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
 * This is an object that contains data related to the INVENTORY_UNIT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="INVENTORY_UNIT"
 */

public abstract class BaseInventoryUnit  implements Comparable, Serializable {

	public static String REF = "InventoryUnit"; //$NON-NLS-1$
	public static String PROP_SHORT_NAME = "shortName"; //$NON-NLS-1$
	public static String PROP_CONVERSION_FACTOR2 = "conversionFactor2"; //$NON-NLS-1$
	public static String PROP_ALTERNATIVE_NAME = "alternativeName"; //$NON-NLS-1$
	public static String PROP_CONVERSION_FACTOR1 = "conversionFactor1"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_CONVERSION_FACTOR3 = "conversionFactor3"; //$NON-NLS-1$
	public static String PROP_LONG_NAME = "longName"; //$NON-NLS-1$


	// constructors
	public BaseInventoryUnit () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseInventoryUnit (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String shortName;
		protected java.lang.String longName;
		protected java.lang.String alternativeName;
		protected java.lang.String conversionFactor1;
		protected java.lang.String conversionFactor2;
		protected java.lang.String conversionFactor3;



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
	 * Return the value associated with the column: LONG_NAME
	 */
	public java.lang.String getLongName () {
					return longName;
			}

	/**
	 * Set the value related to the column: LONG_NAME
	 * @param longName the LONG_NAME value
	 */
	public void setLongName (java.lang.String longName) {
		this.longName = longName;
	}



	/**
	 * Return the value associated with the column: ALT_NAME
	 */
	public java.lang.String getAlternativeName () {
					return alternativeName;
			}

	/**
	 * Set the value related to the column: ALT_NAME
	 * @param alternativeName the ALT_NAME value
	 */
	public void setAlternativeName (java.lang.String alternativeName) {
		this.alternativeName = alternativeName;
	}



	/**
	 * Return the value associated with the column: CONV_FACTOR1
	 */
	public java.lang.String getConversionFactor1 () {
					return conversionFactor1;
			}

	/**
	 * Set the value related to the column: CONV_FACTOR1
	 * @param conversionFactor1 the CONV_FACTOR1 value
	 */
	public void setConversionFactor1 (java.lang.String conversionFactor1) {
		this.conversionFactor1 = conversionFactor1;
	}



	/**
	 * Return the value associated with the column: CONV_FACTOR2
	 */
	public java.lang.String getConversionFactor2 () {
					return conversionFactor2;
			}

	/**
	 * Set the value related to the column: CONV_FACTOR2
	 * @param conversionFactor2 the CONV_FACTOR2 value
	 */
	public void setConversionFactor2 (java.lang.String conversionFactor2) {
		this.conversionFactor2 = conversionFactor2;
	}



	/**
	 * Return the value associated with the column: CONV_FACTOR3
	 */
	public java.lang.String getConversionFactor3 () {
					return conversionFactor3;
			}

	/**
	 * Set the value related to the column: CONV_FACTOR3
	 * @param conversionFactor3 the CONV_FACTOR3 value
	 */
	public void setConversionFactor3 (java.lang.String conversionFactor3) {
		this.conversionFactor3 = conversionFactor3;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.InventoryUnit)) return false;
		else {
			com.floreantpos.model.InventoryUnit inventoryUnit = (com.floreantpos.model.InventoryUnit) obj;
			if (null == this.getId() || null == inventoryUnit.getId()) return false;
			else return (this.getId().equals(inventoryUnit.getId()));
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