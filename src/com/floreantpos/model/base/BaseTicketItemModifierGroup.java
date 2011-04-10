package com.floreantpos.model.base;

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

	public static String REF = "TicketItemModifierGroup";
	public static String PROP_MIN_QUANTITY = "minQuantity";
	public static String PROP_MODIFIER_GROUP_ID = "modifierGroupId";
	public static String PROP_PARENT = "parent";
	public static String PROP_ID = "id";
	public static String PROP_MAX_QUANTITY = "maxQuantity";


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

	private java.util.Date modifiedTime;

	// fields
	private java.lang.Integer modifierGroupId;
	private java.lang.Integer minQuantity;
	private java.lang.Integer maxQuantity;

	// many to one
	private com.floreantpos.model.TicketItem parent;

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
	 * Return the value associated with the column: GROUP_ID
	 */
	public java.lang.Integer getModifierGroupId () {
			return modifierGroupId == null ? Integer.valueOf(0) : modifierGroupId;
	}

	/**
	 * Set the value related to the column: GROUP_ID
	 * @param modifierGroupId the GROUP_ID value
	 */
	public void setModifierGroupId (java.lang.Integer modifierGroupId) {
		this.modifierGroupId = modifierGroupId;
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