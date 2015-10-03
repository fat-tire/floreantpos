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

	public static String REF = "Terminal"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_OPENING_BALANCE = "openingBalance"; //$NON-NLS-1$
	public static String PROP_ASSIGNED_USER = "assignedUser"; //$NON-NLS-1$
	public static String PROP_HAS_CASH_DRAWER = "hasCashDrawer"; //$NON-NLS-1$
	public static String PROP_CURRENT_BALANCE = "currentBalance"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$


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

	// fields
		protected java.lang.String name;
		protected java.lang.Double openingBalance;
		protected java.lang.Double currentBalance;
		protected java.lang.Boolean hasCashDrawer;

	// many to one
	private com.floreantpos.model.User assignedUser;



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
	 * Return the value associated with the column: ASSIGNED_USER
	 */
	public com.floreantpos.model.User getAssignedUser () {
					return assignedUser;
			}

	/**
	 * Set the value related to the column: ASSIGNED_USER
	 * @param assignedUser the ASSIGNED_USER value
	 */
	public void setAssignedUser (com.floreantpos.model.User assignedUser) {
		this.assignedUser = assignedUser;
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
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode(); //$NON-NLS-1$
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