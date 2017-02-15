package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the GLOBAL_CONFIG table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="GLOBAL_CONFIG"
 */

public abstract class BaseGlobalConfig  implements Comparable, Serializable {

	public static String REF = "GlobalConfig";
	public static String PROP_VALUE = "value";
	public static String PROP_KEY = "key";
	public static String PROP_ID = "id";


	// constructors
	public BaseGlobalConfig () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseGlobalConfig (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String key;
	protected java.lang.String value;



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
	 * Return the value associated with the column: POS_KEY
	 */
	public java.lang.String getKey () {
					return key;
			}

	/**
	 * Set the value related to the column: POS_KEY
	 * @param key the POS_KEY value
	 */
	public void setKey (java.lang.String key) {
		this.key = key;
	}



	/**
	 * Return the value associated with the column: POS_VALUE
	 */
	public java.lang.String getValue() {
					return value;
			}

	/**
	 * Set the value related to the column: POS_VALUE
	 * @param value the POS_VALUE value
	 */
	public void setValue(java.lang.String value) {
		this.value = value;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.GlobalConfig)) return false;
		else {
			com.floreantpos.model.GlobalConfig globalConfig = (com.floreantpos.model.GlobalConfig) obj;
			if (null == this.getId() || null == globalConfig.getId()) return false;
			else return (this.getId().equals(globalConfig.getId()));
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