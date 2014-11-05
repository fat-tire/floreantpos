package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the VIRTUAL_PRINTER table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="VIRTUAL_PRINTER"
 */

public abstract class BaseVirtualPrinter  implements Comparable, Serializable {

	public static String REF = "VirtualPrinter";
	public static String PROP_NAME = "name";
	public static String PROP_DEVICE_NAME = "deviceName";
	public static String PROP_AUTO_ID = "autoId";


	// constructors
	public BaseVirtualPrinter () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseVirtualPrinter (java.lang.Integer autoId) {
		this.setAutoId(autoId);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseVirtualPrinter (
		java.lang.Integer autoId,
		java.lang.String name) {

		this.setAutoId(autoId);
		this.setName(name);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer autoId;

	 java.util.Date modifiedTime;

	// fields
		protected java.lang.String name;
		protected java.lang.String deviceName;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="AUTO_ID"
     */
	public java.lang.Integer getAutoId () {
		return autoId;
	}

	/**
	 * Set the unique identifier of this class
	 * @param autoId the new ID
	 */
	public void setAutoId (java.lang.Integer autoId) {
		this.autoId = autoId;
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
	 * Return the value associated with the column: DEVICE_NAME
	 */
	public java.lang.String getDeviceName () {
					return deviceName;
			}

	/**
	 * Set the value related to the column: DEVICE_NAME
	 * @param deviceName the DEVICE_NAME value
	 */
	public void setDeviceName (java.lang.String deviceName) {
		this.deviceName = deviceName;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.VirtualPrinter)) return false;
		else {
			com.floreantpos.model.VirtualPrinter virtualPrinter = (com.floreantpos.model.VirtualPrinter) obj;
			if (null == this.getAutoId() || null == virtualPrinter.getAutoId()) return false;
			else return (this.getAutoId().equals(virtualPrinter.getAutoId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getAutoId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getAutoId().hashCode();
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