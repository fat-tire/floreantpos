package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the MENU_ITEM_SIZE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MENU_ITEM_SIZE"
 */

public abstract class BaseMenuItemSize  implements Comparable, Serializable {

	public static String REF = "MenuItemSize"; //$NON-NLS-1$
	public static String PROP_DESCRIPTION = "description"; //$NON-NLS-1$
	public static String PROP_TRANSLATED_NAME = "translatedName"; //$NON-NLS-1$
	public static String PROP_SIZE_IN_INCH = "sizeInInch"; //$NON-NLS-1$
	public static String PROP_SORT_ORDER = "sortOrder"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_DEFAULT_SIZE = "defaultSize"; //$NON-NLS-1$


	// constructors
	public BaseMenuItemSize () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMenuItemSize (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String name;
		protected java.lang.String translatedName;
		protected java.lang.String description;
		protected java.lang.Integer sortOrder;
		protected java.lang.Double sizeInInch;
		protected java.lang.Boolean defaultSize;



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
	 * Return the value associated with the column: TRANSLATED_NAME
	 */
	public java.lang.String getTranslatedName () {
					return translatedName;
			}

	/**
	 * Set the value related to the column: TRANSLATED_NAME
	 * @param translatedName the TRANSLATED_NAME value
	 */
	public void setTranslatedName (java.lang.String translatedName) {
		this.translatedName = translatedName;
	}



	/**
	 * Return the value associated with the column: DESCRIPTION
	 */
	public java.lang.String getDescription () {
					return description;
			}

	/**
	 * Set the value related to the column: DESCRIPTION
	 * @param description the DESCRIPTION value
	 */
	public void setDescription (java.lang.String description) {
		this.description = description;
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
	 * Return the value associated with the column: SIZE_IN_INCH
	 */
	public java.lang.Double getSizeInInch () {
									return sizeInInch == null ? Double.valueOf(0) : sizeInInch;
					}

	/**
	 * Set the value related to the column: SIZE_IN_INCH
	 * @param sizeInInch the SIZE_IN_INCH value
	 */
	public void setSizeInInch (java.lang.Double sizeInInch) {
		this.sizeInInch = sizeInInch;
	}



	/**
	 * Return the value associated with the column: DEFAULT_SIZE
	 */
	public java.lang.Boolean isDefaultSize () {
								return defaultSize == null ? Boolean.FALSE : defaultSize;
					}

	/**
	 * Set the value related to the column: DEFAULT_SIZE
	 * @param defaultSize the DEFAULT_SIZE value
	 */
	public void setDefaultSize (java.lang.Boolean defaultSize) {
		this.defaultSize = defaultSize;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.MenuItemSize)) return false;
		else {
			com.floreantpos.model.MenuItemSize menuItemSize = (com.floreantpos.model.MenuItemSize) obj;
			if (null == this.getId() || null == menuItemSize.getId()) return false;
			else return (this.getId().equals(menuItemSize.getId()));
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