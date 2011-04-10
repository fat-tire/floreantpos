package com.floreantpos.model.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the CURRENCY table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="CURRENCY"
 */

public abstract class BaseCurrency implements Comparable, Serializable {

	public static String REF = "Currency";
	public static String PROP_NAME = "name";
	public static String PROP_SYMBOL = "symbol";
	public static String PROP_DOLLAR_EXCHANGE_RATE = "dollarExchangeRate";
	public static String PROP_ID = "id";

	// constructors
	public BaseCurrency() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCurrency(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseCurrency(java.lang.Integer id, java.lang.String name) {

		this.setId(id);
		this.setName(name);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	java.util.Date modifiedTime;

	// fields
	private java.lang.String name;
	private java.lang.String symbol;
	private java.lang.Double dollarExchangeRate;

	/**
	 * Return the unique identifier of this class
	 * @hibernate.id
	 *  generator-class="assigned"
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
	 * Return the value associated with the column: MODIFIED_TIME
	 */
	public java.util.Date getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * Set the value related to the column: MODIFIED_TIME
	 * @param modifiedTime the MODIFIED_TIME value
	 */
	public void setModifiedTime(java.util.Date modifiedTime) {
		this.modifiedTime = modifiedTime;
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
	 * Return the value associated with the column: SYMBOL
	 */
	public java.lang.String getSymbol() {
		return symbol;
	}

	/**
	 * Set the value related to the column: SYMBOL
	 * @param symbol the SYMBOL value
	 */
	public void setSymbol(java.lang.String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Return the value associated with the column: DOLLAR_EXCHANGE_RATE
	 */
	public java.lang.Double getDollarExchangeRate() {
		return dollarExchangeRate == null ? Double.valueOf(1) : dollarExchangeRate;
	}

	/**
	 * Set the value related to the column: DOLLAR_EXCHANGE_RATE
	 * @param dollarExchangeRate the DOLLAR_EXCHANGE_RATE value
	 */
	public void setDollarExchangeRate(java.lang.Double dollarExchangeRate) {
		this.dollarExchangeRate = dollarExchangeRate;
	}

	/**
	 * Custom property
	 */
	public static String getDollarExchangeRateDefaultValue() {
		return "1";
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.floreantpos.model.Currency))
			return false;
		else {
			com.floreantpos.model.Currency currency = (com.floreantpos.model.Currency) obj;
			if (null == this.getId() || null == currency.getId())
				return false;
			else
				return (this.getId().equals(currency.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
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