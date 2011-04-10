package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the RESTAURANT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="RESTAURANT"
 */

public abstract class BaseRestaurant  implements Comparable, Serializable {

	public static String REF = "Restaurant";
	public static String PROP_CURRENCY_NAME = "currencyName";
	public static String PROP_CAPACITY = "capacity";
	public static String PROP_ADDRESS_LINE3 = "addressLine3";
	public static String PROP_CURRENCY_SYMBOL = "currencySymbol";
	public static String PROP_TELEPHONE = "telephone";
	public static String PROP_ADDRESS_LINE2 = "addressLine2";
	public static String PROP_ADDRESS_LINE1 = "addressLine1";
	public static String PROP_AUTO_DRAWER_PULL_ENABLE = "autoDrawerPullEnable";
	public static String PROP_DRAWER_PULL_MIN = "drawerPullMin";
	public static String PROP_TABLES = "tables";
	public static String PROP_NAME = "name";
	public static String PROP_DRAWER_PULL_HOUR = "drawerPullHour";
	public static String PROP_ID = "id";


	// constructors
	public BaseRestaurant () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRestaurant (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;
	private java.lang.String addressLine1;
	private java.lang.String addressLine2;
	private java.lang.String addressLine3;
	private java.lang.String telephone;
	private java.lang.Integer capacity;
	private java.lang.Integer tables;
	private java.lang.Boolean autoDrawerPullEnable;
	private java.lang.Integer drawerPullHour;
	private java.lang.Integer drawerPullMin;
	private java.lang.String currencyName;
	private java.lang.String currencySymbol;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="assigned"
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
	 * Return the value associated with the column: ADDRESS_LINE1
	 */
	public java.lang.String getAddressLine1 () {
			return addressLine1;
	}

	/**
	 * Set the value related to the column: ADDRESS_LINE1
	 * @param addressLine1 the ADDRESS_LINE1 value
	 */
	public void setAddressLine1 (java.lang.String addressLine1) {
		this.addressLine1 = addressLine1;
	}



	/**
	 * Return the value associated with the column: ADDRESS_LINE2
	 */
	public java.lang.String getAddressLine2 () {
			return addressLine2;
	}

	/**
	 * Set the value related to the column: ADDRESS_LINE2
	 * @param addressLine2 the ADDRESS_LINE2 value
	 */
	public void setAddressLine2 (java.lang.String addressLine2) {
		this.addressLine2 = addressLine2;
	}



	/**
	 * Return the value associated with the column: ADDRESS_LINE3
	 */
	public java.lang.String getAddressLine3 () {
			return addressLine3;
	}

	/**
	 * Set the value related to the column: ADDRESS_LINE3
	 * @param addressLine3 the ADDRESS_LINE3 value
	 */
	public void setAddressLine3 (java.lang.String addressLine3) {
		this.addressLine3 = addressLine3;
	}



	/**
	 * Return the value associated with the column: TELEPHONE
	 */
	public java.lang.String getTelephone () {
			return telephone;
	}

	/**
	 * Set the value related to the column: TELEPHONE
	 * @param telephone the TELEPHONE value
	 */
	public void setTelephone (java.lang.String telephone) {
		this.telephone = telephone;
	}



	/**
	 * Return the value associated with the column: CAPACITY
	 */
	public java.lang.Integer getCapacity () {
			return capacity == null ? Integer.valueOf(0) : capacity;
	}

	/**
	 * Set the value related to the column: CAPACITY
	 * @param capacity the CAPACITY value
	 */
	public void setCapacity (java.lang.Integer capacity) {
		this.capacity = capacity;
	}



	/**
	 * Return the value associated with the column: TABLES
	 */
	public java.lang.Integer getTables () {
			return tables == null ? Integer.valueOf(0) : tables;
	}

	/**
	 * Set the value related to the column: TABLES
	 * @param tables the TABLES value
	 */
	public void setTables (java.lang.Integer tables) {
		this.tables = tables;
	}



	/**
	 * Return the value associated with the column: AUTODRAWERPULLENABLE
	 */
	public java.lang.Boolean isAutoDrawerPullEnable () {
					return autoDrawerPullEnable == null ? Boolean.FALSE : autoDrawerPullEnable;
			}

	/**
	 * Set the value related to the column: AUTODRAWERPULLENABLE
	 * @param autoDrawerPullEnable the AUTODRAWERPULLENABLE value
	 */
	public void setAutoDrawerPullEnable (java.lang.Boolean autoDrawerPullEnable) {
		this.autoDrawerPullEnable = autoDrawerPullEnable;
	}



	/**
	 * Return the value associated with the column: DRAWER_PULL_HOUR
	 */
	public java.lang.Integer getDrawerPullHour () {
			return drawerPullHour == null ? Integer.valueOf(0) : drawerPullHour;
	}

	/**
	 * Set the value related to the column: DRAWER_PULL_HOUR
	 * @param drawerPullHour the DRAWER_PULL_HOUR value
	 */
	public void setDrawerPullHour (java.lang.Integer drawerPullHour) {
		this.drawerPullHour = drawerPullHour;
	}



	/**
	 * Return the value associated with the column: DRAWER_PULL_MIN
	 */
	public java.lang.Integer getDrawerPullMin () {
			return drawerPullMin == null ? Integer.valueOf(0) : drawerPullMin;
	}

	/**
	 * Set the value related to the column: DRAWER_PULL_MIN
	 * @param drawerPullMin the DRAWER_PULL_MIN value
	 */
	public void setDrawerPullMin (java.lang.Integer drawerPullMin) {
		this.drawerPullMin = drawerPullMin;
	}



	/**
	 * Return the value associated with the column: CNAME
	 */
	public java.lang.String getCurrencyName () {
			return currencyName;
	}

	/**
	 * Set the value related to the column: CNAME
	 * @param currencyName the CNAME value
	 */
	public void setCurrencyName (java.lang.String currencyName) {
		this.currencyName = currencyName;
	}



	/**
	 * Return the value associated with the column: CSYMBOL
	 */
	public java.lang.String getCurrencySymbol () {
			return currencySymbol;
	}

	/**
	 * Set the value related to the column: CSYMBOL
	 * @param currencySymbol the CSYMBOL value
	 */
	public void setCurrencySymbol (java.lang.String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Restaurant)) return false;
		else {
			com.floreantpos.model.Restaurant restaurant = (com.floreantpos.model.Restaurant) obj;
			if (null == this.getId() || null == restaurant.getId()) return false;
			else return (this.getId().equals(restaurant.getId()));
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