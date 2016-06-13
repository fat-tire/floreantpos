package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the CASH_DRAWER table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="CASH_DRAWER"
 */

public abstract class BaseCashDrawer  implements Comparable, Serializable {

	public static String REF = "CashDrawer";
	public static String PROP_TERMINAL = "terminal";
	public static String PROP_ID = "id";
	public static String PROP_CURRENCY = "currency";
	public static String PROP_BALANCE = "balance";


	// constructors
	public BaseCashDrawer () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCashDrawer (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.Double balance;

	// many to one
	private com.floreantpos.model.Terminal terminal;
	private com.floreantpos.model.Currency currency;



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
	 * Return the value associated with the column: BALANCE
	 */
	public java.lang.Double getBalance () {
									return balance == null ? Double.valueOf(0) : balance;
					}

	/**
	 * Set the value related to the column: BALANCE
	 * @param balance the BALANCE value
	 */
	public void setBalance (java.lang.Double balance) {
		this.balance = balance;
	}



	/**
	 * Return the value associated with the column: TERMINAL_ID
	 */
	public com.floreantpos.model.Terminal getTerminal () {
					return terminal;
			}

	/**
	 * Set the value related to the column: TERMINAL_ID
	 * @param terminal the TERMINAL_ID value
	 */
	public void setTerminal (com.floreantpos.model.Terminal terminal) {
		this.terminal = terminal;
	}



	/**
	 * Return the value associated with the column: CURRENCY_ID
	 */
	public com.floreantpos.model.Currency getCurrency () {
					return currency;
			}

	/**
	 * Set the value related to the column: CURRENCY_ID
	 * @param currency the CURRENCY_ID value
	 */
	public void setCurrency (com.floreantpos.model.Currency currency) {
		this.currency = currency;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.CashDrawer)) return false;
		else {
			com.floreantpos.model.CashDrawer cashDrawer = (com.floreantpos.model.CashDrawer) obj;
			if (null == this.getId() || null == cashDrawer.getId()) return false;
			else return (this.getId().equals(cashDrawer.getId()));
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