package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the SHOP_FLOOR_TEMPLATE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="SHOP_FLOOR_TEMPLATE"
 */

public abstract class BaseShopFloorTemplate  implements Comparable, Serializable {

	public static String REF = "ShopFloorTemplate"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_MAIN = "main"; //$NON-NLS-1$
	public static String PROP_DEFAULT_FLOOR = "defaultFloor"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_FLOOR = "floor"; //$NON-NLS-1$


	// constructors
	public BaseShopFloorTemplate () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseShopFloorTemplate (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String name;
		protected java.lang.Boolean defaultFloor;
		protected java.lang.Boolean main;

	// many to one
	private com.floreantpos.model.ShopFloor floor;

	// collections
	private java.util.Map<String,String> properties;



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
	 * Return the value associated with the column: DEFAULT_FLOOR
	 */
	public java.lang.Boolean isDefaultFloor () {
								return defaultFloor == null ? Boolean.FALSE : defaultFloor;
					}

	/**
	 * Set the value related to the column: DEFAULT_FLOOR
	 * @param defaultFloor the DEFAULT_FLOOR value
	 */
	public void setDefaultFloor (java.lang.Boolean defaultFloor) {
		this.defaultFloor = defaultFloor;
	}



	/**
	 * Return the value associated with the column: MAIN
	 */
	public java.lang.Boolean isMain () {
								return main == null ? Boolean.FALSE : main;
					}

	/**
	 * Set the value related to the column: MAIN
	 * @param main the MAIN value
	 */
	public void setMain (java.lang.Boolean main) {
		this.main = main;
	}



	/**
	 * Return the value associated with the column: FLOOR_ID
	 */
	public com.floreantpos.model.ShopFloor getFloor () {
					return floor;
			}

	/**
	 * Set the value related to the column: FLOOR_ID
	 * @param floor the FLOOR_ID value
	 */
	public void setFloor (com.floreantpos.model.ShopFloor floor) {
		this.floor = floor;
	}



	/**
	 * Return the value associated with the column: properties
	 */
	public java.util.Map<String,String> getProperties () {
					return properties;
			}

	/**
	 * Set the value related to the column: properties
	 * @param properties the properties value
	 */
	public void setProperties(java.util.Map<String, String> properties) {
		this.properties = properties;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.ShopFloorTemplate)) return false;
		else {
			com.floreantpos.model.ShopFloorTemplate shopFloorTemplate = (com.floreantpos.model.ShopFloorTemplate) obj;
			if (null == this.getId() || null == shopFloorTemplate.getId()) return this == obj;
			else return (this.getId().equals(shopFloorTemplate.getId()));
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