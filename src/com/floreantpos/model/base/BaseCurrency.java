package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the CURRENCY table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="CURRENCY"
 */

public abstract class BaseCurrency  implements Comparable, Serializable {

	public static String REF = "Currency";
	public static String PROP_EXCHANGE_RATE = "exchangeRate";
	public static String PROP_SYMBOL = "symbol";
	public static String PROP_ID = "id";
	public static String PROP_MAIN = "main";
	public static String PROP_NAME = "name";


	// constructors
	public BaseCurrency () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCurrency (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseCurrency (
		java.lang.Integer id,
		java.lang.String name) {

		this.setId(id);
		this.setName(name);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String name;
		protected java.lang.String symbol;
		protected java.lang.Boolean main;
		protected java.lang.Double exchangeRate;



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
	 * Return the value associated with the column: SYMBOL
	 */
	public java.lang.String getSymbol () {
					return symbol;
			}

	/**
	 * Set the value related to the column: SYMBOL
	 * @param symbol the SYMBOL value
	 */
	public void setSymbol (java.lang.String symbol) {
		this.symbol = symbol;
	}



	/**
	 * Return the value associated with the column: MAIN
	 */
	public java.lang.Boolean isMain () {
								return main == null ? Boolean.FALSE : main;
					}

	/**
	 * Set the value related to the column: MAIN
	 * @param main the MAIN value
	 */
	public void setMain (java.lang.Boolean main) {
		this.main = main;
	}



	/**
	 * Return the value associated with the column: EXCHANGE_RATE
	 */
	public java.lang.Double getExchangeRate () {
									return exchangeRate == null ? Double.valueOf(1) : exchangeRate;
						}

	/**
	 * Set the value related to the column: EXCHANGE_RATE
	 * @param exchangeRate the EXCHANGE_RATE value
	 */
	public void setExchangeRate (java.lang.Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}


	/**
	 * Custom property
	 */
	public static String getExchangeRateDefaultValue () {
		return "1";
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Currency)) return false;
		else {
			com.floreantpos.model.Currency currency = (com.floreantpos.model.Currency) obj;
			if (null == this.getId() || null == currency.getId()) return false;
			else return (this.getId().equals(currency.getId()));
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