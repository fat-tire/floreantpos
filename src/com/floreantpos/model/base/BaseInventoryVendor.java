package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the INVENTORY_VENDOR table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="INVENTORY_VENDOR"
 */

public abstract class BaseInventoryVendor  implements Comparable, Serializable {

	public static String REF = "InventoryVendor"; //$NON-NLS-1$
	public static String PROP_ZIP = "zip"; //$NON-NLS-1$
	public static String PROP_EMAIL = "email"; //$NON-NLS-1$
	public static String PROP_ADDRESS = "address"; //$NON-NLS-1$
	public static String PROP_STATE = "state"; //$NON-NLS-1$
	public static String PROP_PHONE = "phone"; //$NON-NLS-1$
	public static String PROP_VISIBLE = "visible"; //$NON-NLS-1$
	public static String PROP_COUNTRY = "country"; //$NON-NLS-1$
	public static String PROP_CITY = "city"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_FAX = "fax"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$


	// constructors
	public BaseInventoryVendor () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseInventoryVendor (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseInventoryVendor (
		java.lang.Integer id,
 java.lang.String name, java.lang.String address, java.lang.String city, java.lang.String state,
			java.lang.String zip, java.lang.String country, java.lang.String email, java.lang.String phone) {

		this.setId(id);
		this.setName(name);
		this.setAddress(address);
		this.setCity(city);
		this.setState(state);
		this.setZip(zip);
		this.setCountry(country);
		this.setEmail(email);
		this.setPhone(phone);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String name;
		protected java.lang.Boolean visible;
	protected java.lang.String address;
	protected java.lang.String city;
	protected java.lang.String state;
	protected java.lang.String zip;
	protected java.lang.String country;
	protected java.lang.String email;
	protected java.lang.String phone;
	protected java.lang.String fax;



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
	 * Return the value associated with the column: VISIBLE
	 */
	public java.lang.Boolean isVisible () {
								return visible == null ? Boolean.FALSE : visible;
					}

	/**
	 * Set the value related to the column: VISIBLE
	 * @param visible the VISIBLE value
	 */
	public void setVisible (java.lang.Boolean visible) {
		this.visible = visible;
	}



	/**
	 * Return the value associated with the column: ADDRESS
	 */
	public java.lang.String getAddress() {
		return address;
	}

	/**
	 * Set the value related to the column: ADDRESS
	 * @param address the ADDRESS value
	 */
	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	/**
	 * Return the value associated with the column: CITY
	 */
	public java.lang.String getCity() {
		return city;
	}

	/**
	 * Set the value related to the column: CITY
	 * @param city the CITY value
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}

	/**
	 * Return the value associated with the column: STATE
	 */
	public java.lang.String getState() {
		return state;
	}

	/**
	 * Set the value related to the column: STATE
	 * @param state the STATE value
	 */
	public void setState(java.lang.String state) {
		this.state = state;
	}

	/**
	 * Return the value associated with the column: ZIP
	 */
	public java.lang.String getZip() {
		return zip;
	}

	/**
	 * Set the value related to the column: ZIP
	 * @param zip the ZIP value
	 */
	public void setZip(java.lang.String zip) {
		this.zip = zip;
	}

	/**
	 * Return the value associated with the column: COUNTRY
	 */
	public java.lang.String getCountry() {
		return country;
	}

	/**
	 * Set the value related to the column: COUNTRY
	 * @param country the COUNTRY value
	 */
	public void setCountry(java.lang.String country) {
		this.country = country;
	}

	/**
	 * Return the value associated with the column: EMAIL
	 */
	public java.lang.String getEmail() {
		return email;
	}

	/**
	 * Set the value related to the column: EMAIL
	 * @param email the EMAIL value
	 */
	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	/**
	 * Return the value associated with the column: PHONE
	 */
	public java.lang.String getPhone() {
		return phone;
	}

	/**
	 * Set the value related to the column: PHONE
	 * @param phone the PHONE value
	 */
	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}

	/**
	 * Return the value associated with the column: FAX
	 */
	public java.lang.String getFax() {
		return fax;
	}

	/**
	 * Set the value related to the column: FAX
	 * @param fax the FAX value
	 */
	public void setFax(java.lang.String fax) {
		this.fax = fax;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.InventoryVendor)) return false;
		else {
			com.floreantpos.model.InventoryVendor inventoryVendor = (com.floreantpos.model.InventoryVendor) obj;
			if (null == this.getId() || null == inventoryVendor.getId()) return false;
			else return (this.getId().equals(inventoryVendor.getId()));
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