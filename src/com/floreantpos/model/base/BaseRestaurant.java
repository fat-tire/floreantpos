package com.floreantpos.model.base;

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

	public static String REF = "Restaurant"; //$NON-NLS-1$
	public static String PROP_ITEM_PRICE_INCLUDES_TAX = "itemPriceIncludesTax"; //$NON-NLS-1$
	public static String PROP_TELEPHONE = "telephone"; //$NON-NLS-1$
	public static String PROP_TICKET_FOOTER_MESSAGE = "ticketFooterMessage"; //$NON-NLS-1$
	public static String PROP_SERVICE_CHARGE_PERCENTAGE = "serviceChargePercentage"; //$NON-NLS-1$
	public static String PROP_UNIQUE_ID = "uniqueId"; //$NON-NLS-1$
	public static String PROP_ZIP_CODE = "zipCode"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_DEFAULT_GRATUITY_PERCENTAGE = "defaultGratuityPercentage"; //$NON-NLS-1$
	public static String PROP_CURRENCY_NAME = "currencyName"; //$NON-NLS-1$
	public static String PROP_TABLES = "tables"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_CAPACITY = "capacity"; //$NON-NLS-1$
	public static String PROP_ALLOW_MODIFIER_MAX_EXCEED = "allowModifierMaxExceed"; //$NON-NLS-1$
	public static String PROP_ADDRESS_LINE1 = "addressLine1"; //$NON-NLS-1$
	public static String PROP_ADDRESS_LINE2 = "addressLine2"; //$NON-NLS-1$
	public static String PROP_ADDRESS_LINE3 = "addressLine3"; //$NON-NLS-1$
	public static String PROP_CURRENCY_SYMBOL = "currencySymbol"; //$NON-NLS-1$


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
		protected java.lang.Integer uniqueId;
		protected java.lang.String name;
		protected java.lang.String addressLine1;
		protected java.lang.String addressLine2;
		protected java.lang.String addressLine3;
		protected java.lang.String zipCode;
		protected java.lang.String telephone;
		protected java.lang.Integer capacity;
		protected java.lang.Integer tables;
		protected java.lang.String currencyName;
		protected java.lang.String currencySymbol;
		protected java.lang.Double serviceChargePercentage;
		protected java.lang.Double defaultGratuityPercentage;
		protected java.lang.String ticketFooterMessage;
		protected java.lang.Boolean itemPriceIncludesTax;
		protected java.lang.Boolean allowModifierMaxExceed;



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
	 * Return the value associated with the column: UNIQUE_ID
	 */
	public java.lang.Integer getUniqueId () {
									return uniqueId == null ? Integer.valueOf(0) : uniqueId;
					}

	/**
	 * Set the value related to the column: UNIQUE_ID
	 * @param uniqueId the UNIQUE_ID value
	 */
	public void setUniqueId (java.lang.Integer uniqueId) {
		this.uniqueId = uniqueId;
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
	 * Return the value associated with the column: ZIP_CODE
	 */
	public java.lang.String getZipCode () {
					return zipCode;
			}

	/**
	 * Set the value related to the column: ZIP_CODE
	 * @param zipCode the ZIP_CODE value
	 */
	public void setZipCode (java.lang.String zipCode) {
		this.zipCode = zipCode;
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



	/**
	 * Return the value associated with the column: SC_PERCENTAGE
	 */
	public java.lang.Double getServiceChargePercentage () {
									return serviceChargePercentage == null ? Double.valueOf(0) : serviceChargePercentage;
					}

	/**
	 * Set the value related to the column: SC_PERCENTAGE
	 * @param serviceChargePercentage the SC_PERCENTAGE value
	 */
	public void setServiceChargePercentage (java.lang.Double serviceChargePercentage) {
		this.serviceChargePercentage = serviceChargePercentage;
	}



	/**
	 * Return the value associated with the column: GRATUITY_PERCENTAGE
	 */
	public java.lang.Double getDefaultGratuityPercentage () {
									return defaultGratuityPercentage == null ? Double.valueOf(0) : defaultGratuityPercentage;
					}

	/**
	 * Set the value related to the column: GRATUITY_PERCENTAGE
	 * @param defaultGratuityPercentage the GRATUITY_PERCENTAGE value
	 */
	public void setDefaultGratuityPercentage (java.lang.Double defaultGratuityPercentage) {
		this.defaultGratuityPercentage = defaultGratuityPercentage;
	}



	/**
	 * Return the value associated with the column: TICKET_FOOTER
	 */
	public java.lang.String getTicketFooterMessage () {
					return ticketFooterMessage;
			}

	/**
	 * Set the value related to the column: TICKET_FOOTER
	 * @param ticketFooterMessage the TICKET_FOOTER value
	 */
	public void setTicketFooterMessage (java.lang.String ticketFooterMessage) {
		this.ticketFooterMessage = ticketFooterMessage;
	}



	/**
	 * Return the value associated with the column: PRICE_INCLUDES_TAX
	 */
	public java.lang.Boolean isItemPriceIncludesTax () {
								return itemPriceIncludesTax == null ? Boolean.FALSE : itemPriceIncludesTax;
					}

	/**
	 * Set the value related to the column: PRICE_INCLUDES_TAX
	 * @param itemPriceIncludesTax the PRICE_INCLUDES_TAX value
	 */
	public void setItemPriceIncludesTax (java.lang.Boolean itemPriceIncludesTax) {
		this.itemPriceIncludesTax = itemPriceIncludesTax;
	}



	/**
	 * Return the value associated with the column: ALLOW_MODIFIER_MAX_EXCEED
	 */
	public java.lang.Boolean isAllowModifierMaxExceed () {
								return allowModifierMaxExceed == null ? Boolean.FALSE : allowModifierMaxExceed;
					}

	/**
	 * Set the value related to the column: ALLOW_MODIFIER_MAX_EXCEED
	 * @param allowModifierMaxExceed the ALLOW_MODIFIER_MAX_EXCEED value
	 */
	public void setAllowModifierMaxExceed (java.lang.Boolean allowModifierMaxExceed) {
		this.allowModifierMaxExceed = allowModifierMaxExceed;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Restaurant)) return false;
		else {
			com.floreantpos.model.Restaurant restaurant = (com.floreantpos.model.Restaurant) obj;
			if (null == this.getId() || null == restaurant.getId()) return this == obj;
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