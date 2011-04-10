package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the USER_TYPE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="USER_TYPE"
 */

public abstract class BaseUserType  implements Comparable, Serializable {

	public static String REF = "UserType";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";


	// constructors
	public BaseUserType () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseUserType (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;

	// collections
	private java.util.Set<com.floreantpos.model.UserPermission> permissions;



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
	 * Return the value associated with the column: P_NAME
	 */
	public java.lang.String getName () {
			return name;
	}

	/**
	 * Set the value related to the column: P_NAME
	 * @param name the P_NAME value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}



	/**
	 * Return the value associated with the column: permissions
	 */
	public java.util.Set<com.floreantpos.model.UserPermission> getPermissions () {
			return permissions;
	}

	/**
	 * Set the value related to the column: permissions
	 * @param permissions the permissions value
	 */
	public void setPermissions (java.util.Set<com.floreantpos.model.UserPermission> permissions) {
		this.permissions = permissions;
	}

	public void addTopermissions (com.floreantpos.model.UserPermission userPermission) {
		if (null == getPermissions()) setPermissions(new java.util.TreeSet<com.floreantpos.model.UserPermission>());
		getPermissions().add(userPermission);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.UserType)) return false;
		else {
			com.floreantpos.model.UserType userType = (com.floreantpos.model.UserType) obj;
			if (null == this.getId() || null == userType.getId()) return false;
			else return (this.getId().equals(userType.getId()));
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