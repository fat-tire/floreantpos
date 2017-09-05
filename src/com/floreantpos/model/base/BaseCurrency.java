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

	public static String REF = "Currency"; //$NON-NLS-1$
	public static String PROP_DECIMAL_PLACES = "decimalPlaces"; //$NON-NLS-1$
	public static String PROP_SALES_PRICE = "salesPrice"; //$NON-NLS-1$
	public static String PROP_EXCHANGE_RATE = "exchangeRate"; //$NON-NLS-1$
	public static String PROP_SYMBOL = "symbol"; //$NON-NLS-1$
	public static String PROP_TOLERANCE = "tolerance"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_CODE = "code"; //$NON-NLS-1$
	public static String PROP_MAIN = "main"; //$NON-NLS-1$
	public static String PROP_BUY_PRICE = "buyPrice"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$


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

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String code;
		protected java.lang.String name;
		protected java.lang.String symbol;
		protected java.lang.Double exchangeRate;
		protected java.lang.Integer decimalPlaces;
		protected java.lang.Double tolerance;
		protected java.lang.Double buyPrice;
		protected java.lang.Double salesPrice;
		protected java.lang.Boolean main;



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
	 * Return the value associated with the column: CODE
	 */
	public java.lang.String getCode () {
					return code;
			}

	/**
	 * Set the value related to the column: CODE
	 * @param code the CODE value
	 */
	public void setCode (java.lang.String code) {
		this.code = code;
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


	/**
	 * Return the value associated with the column: DECIMAL_PLACES
	 */
	public java.lang.Integer getDecimalPlaces () {
									return decimalPlaces == null ? 2 : decimalPlaces;
						}

	/**
	 * Set the value related to the column: DECIMAL_PLACES
	 * @param decimalPlaces the DECIMAL_PLACES value
	 */
	public void setDecimalPlaces (java.lang.Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}


	/**
	 * Custom property
	 */
	public static String getDecimalPlacesDefaultValue () {
		return "2";
	}


	/**
	 * Return the value associated with the column: TOLERANCE
	 */
	public java.lang.Double getTolerance () {
									return tolerance == null ? Double.valueOf(0) : tolerance;
					}

	/**
	 * Set the value related to the column: TOLERANCE
	 * @param tolerance the TOLERANCE value
	 */
	public void setTolerance (java.lang.Double tolerance) {
		this.tolerance = tolerance;
	}



	/**
	 * Return the value associated with the column: BUY_PRICE
	 */
	public java.lang.Double getBuyPrice () {
									return buyPrice == null ? Double.valueOf(0) : buyPrice;
					}

	/**
	 * Set the value related to the column: BUY_PRICE
	 * @param buyPrice the BUY_PRICE value
	 */
	public void setBuyPrice (java.lang.Double buyPrice) {
		this.buyPrice = buyPrice;
	}



	/**
	 * Return the value associated with the column: SALES_PRICE
	 */
	public java.lang.Double getSalesPrice () {
									return salesPrice == null ? Double.valueOf(0) : salesPrice;
					}

	/**
	 * Set the value related to the column: SALES_PRICE
	 * @param salesPrice the SALES_PRICE value
	 */
	public void setSalesPrice (java.lang.Double salesPrice) {
		this.salesPrice = salesPrice;
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





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Currency)) return false;
		else {
			com.floreantpos.model.Currency currency = (com.floreantpos.model.Currency) obj;
			if (null == this.getId() || null == currency.getId()) return this == obj;
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