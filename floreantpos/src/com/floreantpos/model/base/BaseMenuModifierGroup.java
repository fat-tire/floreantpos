package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the MENU_MODIFIER_GROUP table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MENU_MODIFIER_GROUP"
 */

public abstract class BaseMenuModifierGroup  implements Comparable, Serializable {

	public static String REF = "MenuModifierGroup";
	public static String PROP_NAME = "name";
	public static String PROP_EXCLUSIVE = "exclusive";
	public static String PROP_REQUIRED = "required";
	public static String PROP_ENABLE = "enable";
	public static String PROP_ID = "id";


	// constructors
	public BaseMenuModifierGroup () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMenuModifierGroup (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	private java.util.Date modifiedTime;

	// fields
	private java.lang.String name;
	private java.lang.Boolean enable;
	private java.lang.Boolean exclusive;
	private java.lang.Boolean required;

	// collections
	private java.util.Set<com.floreantpos.model.MenuModifier> modifiers;



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
	 * Return the value associated with the column: ENABLED
	 */
	public java.lang.Boolean isEnable () {
			return enable == null ? Boolean.FALSE : enable;
	}

	/**
	 * Set the value related to the column: ENABLED
	 * @param enable the ENABLED value
	 */
	public void setEnable (java.lang.Boolean enable) {
		this.enable = enable;
	}



	/**
	 * Return the value associated with the column: EXCLUSIVED
	 */
	public java.lang.Boolean isExclusive () {
			return exclusive == null ? Boolean.FALSE : exclusive;
	}

	/**
	 * Set the value related to the column: EXCLUSIVED
	 * @param exclusive the EXCLUSIVED value
	 */
	public void setExclusive (java.lang.Boolean exclusive) {
		this.exclusive = exclusive;
	}



	/**
	 * Return the value associated with the column: REQUIRED
	 */
	public java.lang.Boolean isRequired () {
			return required == null ? Boolean.FALSE : required;
	}

	/**
	 * Set the value related to the column: REQUIRED
	 * @param required the REQUIRED value
	 */
	public void setRequired (java.lang.Boolean required) {
		this.required = required;
	}



	/**
	 * Return the value associated with the column: modifiers
	 */
	public java.util.Set<com.floreantpos.model.MenuModifier> getModifiers () {
			return modifiers;
	}

	/**
	 * Set the value related to the column: modifiers
	 * @param modifiers the modifiers value
	 */
	public void setModifiers (java.util.Set<com.floreantpos.model.MenuModifier> modifiers) {
		this.modifiers = modifiers;
	}

	public void addTomodifiers (com.floreantpos.model.MenuModifier menuModifier) {
		if (null == getModifiers()) setModifiers(new java.util.TreeSet<com.floreantpos.model.MenuModifier>());
		getModifiers().add(menuModifier);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.MenuModifierGroup)) return false;
		else {
			com.floreantpos.model.MenuModifierGroup menuModifierGroup = (com.floreantpos.model.MenuModifierGroup) obj;
			if (null == this.getId() || null == menuModifierGroup.getId()) return false;
			else return (this.getId().equals(menuModifierGroup.getId()));
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