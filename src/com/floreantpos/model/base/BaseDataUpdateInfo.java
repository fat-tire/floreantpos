package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the DATA_UPDATE_INFO table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="DATA_UPDATE_INFO"
 */

public abstract class BaseDataUpdateInfo  implements Comparable, Serializable {

	public static String REF = "DataUpdateInfo"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_LAST_UPDATE_TIME = "lastUpdateTime"; //$NON-NLS-1$


	// constructors
	public BaseDataUpdateInfo () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseDataUpdateInfo (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.util.Date lastUpdateTime;



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
	 * Return the value associated with the column: LAST_UPDATE_TIME
	 */
	public java.util.Date getLastUpdateTime () {
					return lastUpdateTime;
			}

	/**
	 * Set the value related to the column: LAST_UPDATE_TIME
	 * @param lastUpdateTime the LAST_UPDATE_TIME value
	 */
	public void setLastUpdateTime (java.util.Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.DataUpdateInfo)) return false;
		else {
			com.floreantpos.model.DataUpdateInfo dataUpdateInfo = (com.floreantpos.model.DataUpdateInfo) obj;
			if (null == this.getId() || null == dataUpdateInfo.getId()) return false;
			else return (this.getId().equals(dataUpdateInfo.getId()));
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