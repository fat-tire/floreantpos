package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the MENU_GROUP table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="MENU_GROUP"
 */

public abstract class BaseMenuGroup  implements Comparable, Serializable {

	public static String REF = "MenuGroup";
	public static String PROP_NAME = "name";
	public static String PROP_PARENT = "parent";
	public static String PROP_TEXT_COLOR_CODE = "textColorCode";
	public static String PROP_VISIBLE = "visible";
	public static String PROP_SORT_ORDER = "sortOrder";
	public static String PROP_BUTTON_COLOR_CODE = "buttonColorCode";
	public static String PROP_ID = "id";
	public static String PROP_TRANSLATED_NAME = "translatedName";


	// constructors
	public BaseMenuGroup () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMenuGroup (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseMenuGroup (
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
		protected java.lang.Integer sortOrder;
		protected java.lang.Integer buttonColorCode;
		protected java.lang.Integer textColorCode;

	// many to one
	private com.floreantpos.model.MenuCategory parent;

	// collections
	private java.util.List<com.floreantpos.model.Discount> discounts;



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
	 * Return the value associated with the column: BTN_COLOR
	 */
	public java.lang.Integer getButtonColorCode () {
									return buttonColorCode == null ? Integer.valueOf(0) : buttonColorCode;
					}

	/**
	 * Set the value related to the column: BTN_COLOR
	 * @param buttonColorCode the BTN_COLOR value
	 */
	public void setButtonColorCode (java.lang.Integer buttonColorCode) {
		this.buttonColorCode = buttonColorCode;
	}



	/**
	 * Return the value associated with the column: TEXT_COLOR
	 */
	public java.lang.Integer getTextColorCode () {
									return textColorCode == null ? Integer.valueOf(0) : textColorCode;
					}

	/**
	 * Set the value related to the column: TEXT_COLOR
	 * @param textColorCode the TEXT_COLOR value
	 */
	public void setTextColorCode (java.lang.Integer textColorCode) {
		this.textColorCode = textColorCode;
	}



	/**
	 * Return the value associated with the column: CATEGORY_ID
	 */
	public com.floreantpos.model.MenuCategory getParent () {
					return parent;
			}

	/**
	 * Set the value related to the column: CATEGORY_ID
	 * @param parent the CATEGORY_ID value
	 */
	public void setParent (com.floreantpos.model.MenuCategory parent) {
		this.parent = parent;
	}



	/**
	 * Return the value associated with the column: discounts
	 */
	public java.util.List<com.floreantpos.model.Discount> getDiscounts () {
					return discounts;
			}

	/**
	 * Set the value related to the column: discounts
	 * @param discounts the discounts value
	 */
	public void setDiscounts (java.util.List<com.floreantpos.model.Discount> discounts) {
		this.discounts = discounts;
	}

	public void addTodiscounts (com.floreantpos.model.Discount discount) {
		if (null == getDiscounts()) setDiscounts(new java.util.ArrayList<com.floreantpos.model.Discount>());
		getDiscounts().add(discount);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.MenuGroup)) return false;
		else {
			com.floreantpos.model.MenuGroup menuGroup = (com.floreantpos.model.MenuGroup) obj;
			if (null == this.getId() || null == menuGroup.getId()) return false;
			else return (this.getId().equals(menuGroup.getId()));
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