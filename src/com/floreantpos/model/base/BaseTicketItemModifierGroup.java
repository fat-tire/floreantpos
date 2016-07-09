package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the TICKETITEM_MODIFIERGROUP table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TICKETITEM_MODIFIERGROUP"
 */

public abstract class BaseTicketItemModifierGroup  implements Comparable, Serializable {

	public static String REF = "TicketItemModifierGroup"; //$NON-NLS-1$
	public static String PROP_MENU_ITEM_MODIFIER_GROUP = "menuItemModifierGroup"; //$NON-NLS-1$
	public static String PROP_MAIN_SECTION = "mainSection"; //$NON-NLS-1$
	public static String PROP_PARENT = "parent"; //$NON-NLS-1$
	public static String PROP_SHOW_SECTION_NAME = "showSectionName"; //$NON-NLS-1$
	public static String PROP_SORT_ORDER = "sortOrder"; //$NON-NLS-1$
	public static String PROP_SECTION_NAME = "sectionName"; //$NON-NLS-1$
	public static String PROP_MAX_QUANTITY = "maxQuantity"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_MIN_QUANTITY = "minQuantity"; //$NON-NLS-1$


	// constructors
	public BaseTicketItemModifierGroup () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTicketItemModifierGroup (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String sectionName;
		protected java.lang.Integer minQuantity;
		protected java.lang.Integer maxQuantity;
		protected java.lang.Boolean showSectionName;
		protected java.lang.Boolean mainSection;
		protected java.lang.Integer sortOrder;

	// many to one
	private com.floreantpos.model.TicketItem parent;
	private com.floreantpos.model.MenuItemModifierGroup menuItemModifierGroup;

	// collections
	private java.util.List<com.floreantpos.model.TicketItemModifier> ticketItemModifiers;



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
	 * Return the value associated with the column: SECTION_NAME
	 */
	public java.lang.String getSectionName () {
					return sectionName;
			}

	/**
	 * Set the value related to the column: SECTION_NAME
	 * @param sectionName the SECTION_NAME value
	 */
	public void setSectionName (java.lang.String sectionName) {
		this.sectionName = sectionName;
	}



	/**
	 * Return the value associated with the column: MIN_QUANTITY
	 */
	public java.lang.Integer getMinQuantity () {
									return minQuantity == null ? Integer.valueOf(0) : minQuantity;
					}

	/**
	 * Set the value related to the column: MIN_QUANTITY
	 * @param minQuantity the MIN_QUANTITY value
	 */
	public void setMinQuantity (java.lang.Integer minQuantity) {
		this.minQuantity = minQuantity;
	}



	/**
	 * Return the value associated with the column: MAX_QUANTITY
	 */
	public java.lang.Integer getMaxQuantity () {
									return maxQuantity == null ? Integer.valueOf(0) : maxQuantity;
					}

	/**
	 * Set the value related to the column: MAX_QUANTITY
	 * @param maxQuantity the MAX_QUANTITY value
	 */
	public void setMaxQuantity (java.lang.Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
	}



	/**
	 * Return the value associated with the column: SHOW_SECTION_NAME
	 */
	public java.lang.Boolean isShowSectionName () {
								return showSectionName == null ? Boolean.FALSE : showSectionName;
					}

	/**
	 * Set the value related to the column: SHOW_SECTION_NAME
	 * @param showSectionName the SHOW_SECTION_NAME value
	 */
	public void setShowSectionName (java.lang.Boolean showSectionName) {
		this.showSectionName = showSectionName;
	}



	/**
	 * Return the value associated with the column: MAIN_SECTION
	 */
	public java.lang.Boolean isMainSection () {
								return mainSection == null ? Boolean.FALSE : mainSection;
					}

	/**
	 * Set the value related to the column: MAIN_SECTION
	 * @param mainSection the MAIN_SECTION value
	 */
	public void setMainSection (java.lang.Boolean mainSection) {
		this.mainSection = mainSection;
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
	 * Return the value associated with the column: parent
	 */
	public com.floreantpos.model.TicketItem getParent () {
					return parent;
			}

	/**
	 * Set the value related to the column: parent
	 * @param parent the parent value
	 */
	public void setParent (com.floreantpos.model.TicketItem parent) {
		this.parent = parent;
	}



	/**
	 * Return the value associated with the column: GROUP_ID
	 */
	public com.floreantpos.model.MenuItemModifierGroup getMenuItemModifierGroup () {
					return menuItemModifierGroup;
			}

	/**
	 * Set the value related to the column: GROUP_ID
	 * @param menuItemModifierGroup the GROUP_ID value
	 */
	public void setMenuItemModifierGroup (com.floreantpos.model.MenuItemModifierGroup menuItemModifierGroup) {
		this.menuItemModifierGroup = menuItemModifierGroup;
	}



	/**
	 * Return the value associated with the column: ticketItemModifiers
	 */
	public java.util.List<com.floreantpos.model.TicketItemModifier> getTicketItemModifiers () {
					return ticketItemModifiers;
			}

	/**
	 * Set the value related to the column: ticketItemModifiers
	 * @param ticketItemModifiers the ticketItemModifiers value
	 */
	public void setTicketItemModifiers (java.util.List<com.floreantpos.model.TicketItemModifier> ticketItemModifiers) {
		this.ticketItemModifiers = ticketItemModifiers;
	}

	public void addToticketItemModifiers (com.floreantpos.model.TicketItemModifier ticketItemModifier) {
		if (null == getTicketItemModifiers()) setTicketItemModifiers(new java.util.ArrayList<com.floreantpos.model.TicketItemModifier>());
		getTicketItemModifiers().add(ticketItemModifier);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.TicketItemModifierGroup)) return false;
		else {
			com.floreantpos.model.TicketItemModifierGroup ticketItemModifierGroup = (com.floreantpos.model.TicketItemModifierGroup) obj;
			if (null == this.getId() || null == ticketItemModifierGroup.getId()) return false;
			else return (this.getId().equals(ticketItemModifierGroup.getId()));
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