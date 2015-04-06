package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the RECEPIE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="RECEPIE"
 */

public abstract class BaseRecepie  implements Comparable, Serializable {

	public static String REF = "Recepie";
	public static String PROP_MENU_ITEM = "menuItem";
	public static String PROP_ID = "id";


	// constructors
	public BaseRecepie () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRecepie (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// many to one
	private com.floreantpos.model.MenuItem menuItem;

	// collections
	private java.util.List<com.floreantpos.model.RecepieItem> recepieItems;



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
	 * Return the value associated with the column: MENU_ITEM
	 */
	public com.floreantpos.model.MenuItem getMenuItem () {
					return menuItem;
			}

	/**
	 * Set the value related to the column: MENU_ITEM
	 * @param menuItem the MENU_ITEM value
	 */
	public void setMenuItem (com.floreantpos.model.MenuItem menuItem) {
		this.menuItem = menuItem;
	}



	/**
	 * Return the value associated with the column: recepieItems
	 */
	public java.util.List<com.floreantpos.model.RecepieItem> getRecepieItems () {
					return recepieItems;
			}

	/**
	 * Set the value related to the column: recepieItems
	 * @param recepieItems the recepieItems value
	 */
	public void setRecepieItems (java.util.List<com.floreantpos.model.RecepieItem> recepieItems) {
		this.recepieItems = recepieItems;
	}

	public void addTorecepieItems (com.floreantpos.model.RecepieItem recepieItem) {
		if (null == getRecepieItems()) setRecepieItems(new java.util.ArrayList<com.floreantpos.model.RecepieItem>());
		getRecepieItems().add(recepieItem);
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Recepie)) return false;
		else {
			com.floreantpos.model.Recepie recepie = (com.floreantpos.model.Recepie) obj;
			if (null == this.getId() || null == recepie.getId()) return false;
			else return (this.getId().equals(recepie.getId()));
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