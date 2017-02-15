package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the DELIVERY_CONFIGURATION table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="DELIVERY_CONFIGURATION"
 */

public abstract class BaseDeliveryConfiguration  implements Comparable, Serializable {

	public static String REF = "DeliveryConfiguration";
	public static String PROP_UNIT_NAME = "unitName";
	public static String PROP_ID = "id";
	public static String PROP_CHARGE_BY_ZIP_CODE = "chargeByZipCode";
	public static String PROP_UNIT_SYMBOL = "unitSymbol";


	// constructors
	public BaseDeliveryConfiguration () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseDeliveryConfiguration (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String unitName;
		protected java.lang.String unitSymbol;
		protected java.lang.Boolean chargeByZipCode;



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
	 * Return the value associated with the column: UNIT_SYMBOL
	 */
	public java.lang.String getUnitSymbol () {
					return unitSymbol;
			}

	/**
	 * Set the value related to the column: UNIT_SYMBOL
	 * @param unitSymbol the UNIT_SYMBOL value
	 */
	public void setUnitSymbol (java.lang.String unitSymbol) {
		this.unitSymbol = unitSymbol;
	}



	/**
	 * Return the value associated with the column: CHARGE_BY_ZIP_CODE
	 */
	public java.lang.Boolean isChargeByZipCode () {
								return chargeByZipCode == null ? Boolean.FALSE : chargeByZipCode;
					}

	/**
	 * Set the value related to the column: CHARGE_BY_ZIP_CODE
	 * @param chargeByZipCode the CHARGE_BY_ZIP_CODE value
	 */
	public void setChargeByZipCode (java.lang.Boolean chargeByZipCode) {
		this.chargeByZipCode = chargeByZipCode;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.DeliveryConfiguration)) return false;
		else {
			com.floreantpos.model.DeliveryConfiguration deliveryConfiguration = (com.floreantpos.model.DeliveryConfiguration) obj;
			if (null == this.getId() || null == deliveryConfiguration.getId()) return false;
			else return (this.getId().equals(deliveryConfiguration.getId()));
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