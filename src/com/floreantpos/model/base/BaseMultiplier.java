package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the MULTIPLIER table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MULTIPLIER"
 */

public abstract class BaseMultiplier  implements Comparable, Serializable {

	public static String REF = "Multiplier"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_MAIN = "main"; //$NON-NLS-1$
	public static String PROP_BUTTON_COLOR = "buttonColor"; //$NON-NLS-1$
	public static String PROP_DEFAULT_MULTIPLIER = "defaultMultiplier"; //$NON-NLS-1$
	public static String PROP_SORT_ORDER = "sortOrder"; //$NON-NLS-1$
	public static String PROP_TICKET_PREFIX = "ticketPrefix"; //$NON-NLS-1$
	public static String PROP_TEXT_COLOR = "textColor"; //$NON-NLS-1$
	public static String PROP_RATE = "rate"; //$NON-NLS-1$


	// constructors
	public BaseMultiplier () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMultiplier (java.lang.String name) {
		this.setName(name);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.String name;

	// fields
		protected java.lang.String ticketPrefix;
		protected java.lang.Double rate;
		protected java.lang.Integer sortOrder;
		protected java.lang.Boolean defaultMultiplier;
		protected java.lang.Boolean main;
		protected java.lang.Integer buttonColor;
		protected java.lang.Integer textColor;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="assigned"
     *  column="NAME"
     */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the unique identifier of this class
	 * @param name the new ID
	 */
	public void setName (java.lang.String name) {
		this.name = name;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: TICKET_PREFIX
	 */
	public java.lang.String getTicketPrefix () {
					return ticketPrefix;
			}

	/**
	 * Set the value related to the column: TICKET_PREFIX
	 * @param ticketPrefix the TICKET_PREFIX value
	 */
	public void setTicketPrefix (java.lang.String ticketPrefix) {
		this.ticketPrefix = ticketPrefix;
	}



	/**
	 * Return the value associated with the column: RATE
	 */
	public java.lang.Double getRate () {
									return rate == null ? Double.valueOf(0) : rate;
					}

	/**
	 * Set the value related to the column: RATE
	 * @param rate the RATE value
	 */
	public void setRate (java.lang.Double rate) {
		this.rate = rate;
	}



	/**
	 * Return the value associated with the column: SORT_ORDER
	 */
	public java.lang.Integer getSortOrder () {
									return sortOrder == null ? Integer.valueOf(0) : sortOrder;
					}

	/**
	 * Set the value related to the column: SORT_ORDER
	 * @param sortOrder the SORT_ORDER value
	 */
	public void setSortOrder (java.lang.Integer sortOrder) {
		this.sortOrder = sortOrder;
	}



	/**
	 * Return the value associated with the column: DEFAULT_MULTIPLIER
	 */
	public java.lang.Boolean isDefaultMultiplier () {
								return defaultMultiplier == null ? Boolean.FALSE : defaultMultiplier;
					}

	/**
	 * Set the value related to the column: DEFAULT_MULTIPLIER
	 * @param defaultMultiplier the DEFAULT_MULTIPLIER value
	 */
	public void setDefaultMultiplier (java.lang.Boolean defaultMultiplier) {
		this.defaultMultiplier = defaultMultiplier;
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
	 * Return the value associated with the column: BTN_COLOR
	 */
	public java.lang.Integer getButtonColor () {
									return buttonColor == null ? Integer.valueOf(0) : buttonColor;
					}

	/**
	 * Set the value related to the column: BTN_COLOR
	 * @param buttonColor the BTN_COLOR value
	 */
	public void setButtonColor (java.lang.Integer buttonColor) {
		this.buttonColor = buttonColor;
	}



	/**
	 * Return the value associated with the column: TEXT_COLOR
	 */
	public java.lang.Integer getTextColor () {
									return textColor == null ? Integer.valueOf(0) : textColor;
					}

	/**
	 * Set the value related to the column: TEXT_COLOR
	 * @param textColor the TEXT_COLOR value
	 */
	public void setTextColor (java.lang.Integer textColor) {
		this.textColor = textColor;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Multiplier)) return false;
		else {
			com.floreantpos.model.Multiplier multiplier = (com.floreantpos.model.Multiplier) obj;
			if (null == this.getName() || null == multiplier.getName()) return this == obj;
			else return (this.getName().equals(multiplier.getName()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getName()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getName().hashCode();
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