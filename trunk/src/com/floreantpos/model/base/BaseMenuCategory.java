package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the MENU_CATEGORY table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MENU_CATEGORY"
 */

public abstract class BaseMenuCategory  implements Comparable, Serializable {

	public static String REF = "MenuCategory";
	public static String PROP_NAME = "name";
	public static String PROP_BEVERAGE = "beverage";
	public static String PROP_BUTTON_COLOR = "buttonColor";
	public static String PROP_VISIBLE = "visible";
	public static String PROP_SORT_ORDER = "sortOrder";
	public static String PROP_ID = "id";
	public static String PROP_TRANSLATED_NAME = "translatedName";
	public static String PROP_TEXT_COLOR = "textColor";


	// constructors
	public BaseMenuCategory () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMenuCategory (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseMenuCategory (
		java.lang.Integer id,
		java.lang.String name) {

		this.setId(id);
		this.setName(name);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String name;
		protected java.lang.String translatedName;
		protected java.lang.Boolean visible;
		protected java.lang.Boolean beverage;
		protected java.lang.Integer sortOrder;
		protected java.lang.Integer buttonColor;
		protected java.lang.Integer textColor;

	// collections
	private java.util.List<com.floreantpos.model.MenuGroup> menuGroups;



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
	 * Return the value associated with the column: VISIBLE
	 */
	public java.lang.Boolean isVisible () {
								return visible == null ? Boolean.FALSE : visible;
					}

	/**
	 * Set the value related to the column: VISIBLE
	 * @param visible the VISIBLE value
	 */
	public void setVisible (java.lang.Boolean visible) {
		this.visible = visible;
	}



	/**
	 * Return the value associated with the column: BEVERAGE
	 */
	public java.lang.Boolean isBeverage () {
								return beverage == null ? Boolean.FALSE : beverage;
					}

	/**
	 * Set the value related to the column: BEVERAGE
	 * @param beverage the BEVERAGE value
	 */
	public void setBeverage (java.lang.Boolean beverage) {
		this.beverage = beverage;
	}



	/**
	 * Return the value associated with the column: SORT_ORDER
	 */
	public java.lang.Integer getSortOrder () {
					return sortOrder == null ? Integer.MAX_VALUE : sortOrder;
			}

	/**
	 * Set the value related to the column: SORT_ORDER
	 * @param sortOrder the SORT_ORDER value
	 */
	public void setSortOrder (java.lang.Integer sortOrder) {
		this.sortOrder = sortOrder;
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



	/**
	 * Return the value associated with the column: menuGroups
	 */
	public java.util.List<com.floreantpos.model.MenuGroup> getMenuGroups () {
					return menuGroups;
			}

	/**
	 * Set the value related to the column: menuGroups
	 * @param menuGroups the menuGroups value
	 */
	public void setMenuGroups (java.util.List<com.floreantpos.model.MenuGroup> menuGroups) {
		this.menuGroups = menuGroups;
	}

	public void addTomenuGroups (com.floreantpos.model.MenuGroup menuGroup) {
		if (null == getMenuGroups()) setMenuGroups(new java.util.ArrayList<com.floreantpos.model.MenuGroup>());
		getMenuGroups().add(menuGroup);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.MenuCategory)) return false;
		else {
			com.floreantpos.model.MenuCategory menuCategory = (com.floreantpos.model.MenuCategory) obj;
			if (null == this.getId() || null == menuCategory.getId()) return false;
			else return (this.getId().equals(menuCategory.getId()));
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