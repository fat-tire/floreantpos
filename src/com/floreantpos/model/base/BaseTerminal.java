package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the TERMINAL table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TERMINAL"
 */

public abstract class BaseTerminal  implements Comparable, Serializable {

	public static String REF = "Terminal";
	public static String PROP_NAME = "name";
	public static String PROP_AUTO_DRAWER_PULL_ENABLE = "autoDrawerPullEnable";
	public static String PROP_OPENING_BALANCE = "openingBalance";
	public static String PROP_HAS_CASH_DRAWER = "hasCashDrawer";
	public static String PROP_CURRENT_BALANCE = "currentBalance";
	public static String PROP_ID = "id";
	public static String PROP_AUTO_DRAWER_PULL_MIN = "autoDrawerPullMin";
	public static String PROP_AUTO_DRAWER_PULL_HOUR = "autoDrawerPullHour";


	// constructors
	public BaseTerminal () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTerminal (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	 java.util.Date modifiedTime;

	// fields
		protected java.lang.String name;
		protected java.lang.Double openingBalance;
		protected java.lang.Double currentBalance;
		protected java.lang.Boolean hasCashDrawer;
		protected java.lang.Boolean autoDrawerPullEnable;
		protected java.lang.Integer autoDrawerPullHour;
		protected java.lang.Integer autoDrawerPullMin;



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
	 * Return the value associated with the column: MODIFIED_TIME
	 */
	public java.util.Date getModifiedTime () {
					return modifiedTime;
			}

	/**
	 * Set the value related to the column: MODIFIED_TIME
	 * @param modifiedTime the MODIFIED_TIME value
	 */
	public void setModifiedTime (java.util.Date modifiedTime) {
		this.modifiedTime = modifiedTime;
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
	 * Return the value associated with the column: OPENING_BALANCE
	 */
	public java.lang.Double getOpeningBalance () {
									return openingBalance == null ? Double.valueOf(0) : openingBalance;
					}

	/**
	 * Set the value related to the column: OPENING_BALANCE
	 * @param openingBalance the OPENING_BALANCE value
	 */
	public void setOpeningBalance (java.lang.Double openingBalance) {
		this.openingBalance = openingBalance;
	}



	/**
	 * Return the value associated with the column: CURRENT_BALANCE
	 */
	public java.lang.Double getCurrentBalance () {
									return currentBalance == null ? Double.valueOf(0) : currentBalance;
					}

	/**
	 * Set the value related to the column: CURRENT_BALANCE
	 * @param currentBalance the CURRENT_BALANCE value
	 */
	public void setCurrentBalance (java.lang.Double currentBalance) {
		this.currentBalance = currentBalance;
	}



	/**
	 * Return the value associated with the column: HAS_CASH_DRAWER
	 */
	public java.lang.Boolean isHasCashDrawer () {
								return hasCashDrawer == null ? Boolean.FALSE : hasCashDrawer;
					}

	/**
	 * Set the value related to the column: HAS_CASH_DRAWER
	 * @param hasCashDrawer the HAS_CASH_DRAWER value
	 */
	public void setHasCashDrawer (java.lang.Boolean hasCashDrawer) {
		this.hasCashDrawer = hasCashDrawer;
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
	public java.lang.Integer getAutoDrawerPullHour () {
					return autoDrawerPullHour == null ? Integer.valueOf(0) : autoDrawerPullHour;
			}

	/**
	 * Set the value related to the column: DRAWER_PULL_HOUR
	 * @param autoDrawerPullHour the DRAWER_PULL_HOUR value
	 */
	public void setAutoDrawerPullHour (java.lang.Integer autoDrawerPullHour) {
		this.autoDrawerPullHour = autoDrawerPullHour;
	}



	/**
	 * Return the value associated with the column: DRAWER_PULL_MIN
	 */
	public java.lang.Integer getAutoDrawerPullMin () {
					return autoDrawerPullMin == null ? Integer.valueOf(0) : autoDrawerPullMin;
			}

	/**
	 * Set the value related to the column: DRAWER_PULL_MIN
	 * @param autoDrawerPullMin the DRAWER_PULL_MIN value
	 */
	public void setAutoDrawerPullMin (java.lang.Integer autoDrawerPullMin) {
		this.autoDrawerPullMin = autoDrawerPullMin;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Terminal)) return false;
		else {
			com.floreantpos.model.Terminal terminal = (com.floreantpos.model.Terminal) obj;
			if (null == this.getId() || null == terminal.getId()) return false;
			else return (this.getId().equals(terminal.getId()));
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