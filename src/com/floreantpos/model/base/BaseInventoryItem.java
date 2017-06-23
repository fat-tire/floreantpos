package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the INVENTORY_ITEM table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="INVENTORY_ITEM"
 */

public abstract class BaseInventoryItem  implements Comparable, Serializable {

	public static String REF = "InventoryItem"; //$NON-NLS-1$
	public static String PROP_PACKAGE_BARCODE = "packageBarcode"; //$NON-NLS-1$
	public static String PROP_PACKAGING_UNIT = "packagingUnit"; //$NON-NLS-1$
	public static String PROP_DESCRIPTION = "description"; //$NON-NLS-1$
	public static String PROP_RECIPE_UNIT = "recipeUnit"; //$NON-NLS-1$
	public static String PROP_ITEM_VENDOR = "itemVendor"; //$NON-NLS-1$
	public static String PROP_ITEM_GROUP = "itemGroup"; //$NON-NLS-1$
	public static String PROP_VISIBLE = "visible"; //$NON-NLS-1$
	public static String PROP_AVERAGE_PACKAGE_PRICE = "averagePackagePrice"; //$NON-NLS-1$
	public static String PROP_SORT_ORDER = "sortOrder"; //$NON-NLS-1$
	public static String PROP_UNIT_BARCODE = "unitBarcode"; //$NON-NLS-1$
	public static String PROP_PACKAGE_REPLENISH_LEVEL = "packageReplenishLevel"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_LAST_UPDATE_DATE = "lastUpdateDate"; //$NON-NLS-1$
	public static String PROP_TOTAL_PACKAGES = "totalPackages"; //$NON-NLS-1$
	public static String PROP_ITEM_LOCATION = "itemLocation"; //$NON-NLS-1$
	public static String PROP_CREATE_TIME = "createTime"; //$NON-NLS-1$
	public static String PROP_TOTAL_RECEPIE_UNITS = "totalRecepieUnits"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_UNIT_PER_PACKAGE = "unitPerPackage"; //$NON-NLS-1$
	public static String PROP_PACKAGE_REORDER_LEVEL = "packageReorderLevel"; //$NON-NLS-1$
	public static String PROP_UNIT_SELLING_PRICE = "unitSellingPrice"; //$NON-NLS-1$
	public static String PROP_UNIT_PURCHASE_PRICE = "unitPurchasePrice"; //$NON-NLS-1$


	// constructors
	public BaseInventoryItem () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseInventoryItem (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.util.Date createTime;
		protected java.util.Date lastUpdateDate;
		protected java.lang.String name;
		protected java.lang.String packageBarcode;
		protected java.lang.String unitBarcode;
		protected java.lang.Double unitPerPackage;
		protected java.lang.Integer sortOrder;
		protected java.lang.Integer packageReorderLevel;
		protected java.lang.Integer packageReplenishLevel;
		protected java.lang.String description;
		protected java.lang.Double averagePackagePrice;
		protected java.lang.Double totalPackages;
		protected java.lang.Double totalRecepieUnits;
		protected java.lang.Double unitPurchasePrice;
		protected java.lang.Double unitSellingPrice;
		protected java.lang.Boolean visible;

	// many to one
	private com.floreantpos.model.PackagingUnit packagingUnit;
	private com.floreantpos.model.PackagingUnit recipeUnit;
	private com.floreantpos.model.InventoryGroup itemGroup;
	private com.floreantpos.model.InventoryLocation itemLocation;
	private com.floreantpos.model.InventoryVendor itemVendor;



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
	 * Return the value associated with the column: CREATE_TIME
	 */
	public java.util.Date getCreateTime () {
					return createTime;
			}

	/**
	 * Set the value related to the column: CREATE_TIME
	 * @param createTime the CREATE_TIME value
	 */
	public void setCreateTime (java.util.Date createTime) {
		this.createTime = createTime;
	}



	/**
	 * Return the value associated with the column: LAST_UPDATE_DATE
	 */
	public java.util.Date getLastUpdateDate () {
					return lastUpdateDate;
			}

	/**
	 * Set the value related to the column: LAST_UPDATE_DATE
	 * @param lastUpdateDate the LAST_UPDATE_DATE value
	 */
	public void setLastUpdateDate (java.util.Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
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
	 * Return the value associated with the column: PACKAGE_BARCODE
	 */
	public java.lang.String getPackageBarcode () {
					return packageBarcode;
			}

	/**
	 * Set the value related to the column: PACKAGE_BARCODE
	 * @param packageBarcode the PACKAGE_BARCODE value
	 */
	public void setPackageBarcode (java.lang.String packageBarcode) {
		this.packageBarcode = packageBarcode;
	}



	/**
	 * Return the value associated with the column: UNIT_BARCODE
	 */
	public java.lang.String getUnitBarcode () {
					return unitBarcode;
			}

	/**
	 * Set the value related to the column: UNIT_BARCODE
	 * @param unitBarcode the UNIT_BARCODE value
	 */
	public void setUnitBarcode (java.lang.String unitBarcode) {
		this.unitBarcode = unitBarcode;
	}



	/**
	 * Return the value associated with the column: UNIT_PER_PACKAGE
	 */
	public java.lang.Double getUnitPerPackage () {
									return unitPerPackage == null ? Double.valueOf(0) : unitPerPackage;
					}

	/**
	 * Set the value related to the column: UNIT_PER_PACKAGE
	 * @param unitPerPackage the UNIT_PER_PACKAGE value
	 */
	public void setUnitPerPackage (java.lang.Double unitPerPackage) {
		this.unitPerPackage = unitPerPackage;
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
	 * Return the value associated with the column: PACKAGE_REORDER_LEVEL
	 */
	public java.lang.Integer getPackageReorderLevel () {
									return packageReorderLevel == null ? Integer.valueOf(0) : packageReorderLevel;
					}

	/**
	 * Set the value related to the column: PACKAGE_REORDER_LEVEL
	 * @param packageReorderLevel the PACKAGE_REORDER_LEVEL value
	 */
	public void setPackageReorderLevel (java.lang.Integer packageReorderLevel) {
		this.packageReorderLevel = packageReorderLevel;
	}



	/**
	 * Return the value associated with the column: PACKAGE_REPLENISH_LEVEL
	 */
	public java.lang.Integer getPackageReplenishLevel () {
									return packageReplenishLevel == null ? Integer.valueOf(0) : packageReplenishLevel;
					}

	/**
	 * Set the value related to the column: PACKAGE_REPLENISH_LEVEL
	 * @param packageReplenishLevel the PACKAGE_REPLENISH_LEVEL value
	 */
	public void setPackageReplenishLevel (java.lang.Integer packageReplenishLevel) {
		this.packageReplenishLevel = packageReplenishLevel;
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
	 * Return the value associated with the column: AVERAGE_PACKAGE_PRICE
	 */
	public java.lang.Double getAveragePackagePrice () {
									return averagePackagePrice == null ? Double.valueOf(0) : averagePackagePrice;
					}

	/**
	 * Set the value related to the column: AVERAGE_PACKAGE_PRICE
	 * @param averagePackagePrice the AVERAGE_PACKAGE_PRICE value
	 */
	public void setAveragePackagePrice (java.lang.Double averagePackagePrice) {
		this.averagePackagePrice = averagePackagePrice;
	}



	/**
	 * Return the value associated with the column: TOTAL_UNIT_PACKAGES
	 */
	public java.lang.Double getTotalPackages () {
									return totalPackages == null ? Double.valueOf(0) : totalPackages;
					}

	/**
	 * Set the value related to the column: TOTAL_UNIT_PACKAGES
	 * @param totalPackages the TOTAL_UNIT_PACKAGES value
	 */
	public void setTotalPackages (java.lang.Double totalPackages) {
		this.totalPackages = totalPackages;
	}



	/**
	 * Return the value associated with the column: TOTAL_RECEPIE_UNITS
	 */
	public java.lang.Double getTotalRecepieUnits () {
									return totalRecepieUnits == null ? Double.valueOf(0) : totalRecepieUnits;
					}

	/**
	 * Set the value related to the column: TOTAL_RECEPIE_UNITS
	 * @param totalRecepieUnits the TOTAL_RECEPIE_UNITS value
	 */
	public void setTotalRecepieUnits (java.lang.Double totalRecepieUnits) {
		this.totalRecepieUnits = totalRecepieUnits;
	}



	/**
	 * Return the value associated with the column: UNIT_PURCHASE_PRICE
	 */
	public java.lang.Double getUnitPurchasePrice () {
									return unitPurchasePrice == null ? Double.valueOf(0) : unitPurchasePrice;
					}

	/**
	 * Set the value related to the column: UNIT_PURCHASE_PRICE
	 * @param unitPurchasePrice the UNIT_PURCHASE_PRICE value
	 */
	public void setUnitPurchasePrice (java.lang.Double unitPurchasePrice) {
		this.unitPurchasePrice = unitPurchasePrice;
	}



	/**
	 * Return the value associated with the column: UNIT_SELLING_PRICE
	 */
	public java.lang.Double getUnitSellingPrice () {
									return unitSellingPrice == null ? Double.valueOf(0) : unitSellingPrice;
					}

	/**
	 * Set the value related to the column: UNIT_SELLING_PRICE
	 * @param unitSellingPrice the UNIT_SELLING_PRICE value
	 */
	public void setUnitSellingPrice (java.lang.Double unitSellingPrice) {
		this.unitSellingPrice = unitSellingPrice;
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
	 * Return the value associated with the column: PUNIT_ID
	 */
	public com.floreantpos.model.PackagingUnit getPackagingUnit () {
					return packagingUnit;
			}

	/**
	 * Set the value related to the column: PUNIT_ID
	 * @param packagingUnit the PUNIT_ID value
	 */
	public void setPackagingUnit (com.floreantpos.model.PackagingUnit packagingUnit) {
		this.packagingUnit = packagingUnit;
	}



	/**
	 * Return the value associated with the column: RECIPE_UNIT_ID
	 */
	public com.floreantpos.model.PackagingUnit getRecipeUnit () {
					return recipeUnit;
			}

	/**
	 * Set the value related to the column: RECIPE_UNIT_ID
	 * @param recipeUnit the RECIPE_UNIT_ID value
	 */
	public void setRecipeUnit (com.floreantpos.model.PackagingUnit recipeUnit) {
		this.recipeUnit = recipeUnit;
	}



	/**
	 * Return the value associated with the column: ITEM_GROUP_ID
	 */
	public com.floreantpos.model.InventoryGroup getItemGroup () {
					return itemGroup;
			}

	/**
	 * Set the value related to the column: ITEM_GROUP_ID
	 * @param itemGroup the ITEM_GROUP_ID value
	 */
	public void setItemGroup (com.floreantpos.model.InventoryGroup itemGroup) {
		this.itemGroup = itemGroup;
	}



	/**
	 * Return the value associated with the column: ITEM_LOCATION_ID
	 */
	public com.floreantpos.model.InventoryLocation getItemLocation () {
					return itemLocation;
			}

	/**
	 * Set the value related to the column: ITEM_LOCATION_ID
	 * @param itemLocation the ITEM_LOCATION_ID value
	 */
	public void setItemLocation (com.floreantpos.model.InventoryLocation itemLocation) {
		this.itemLocation = itemLocation;
	}



	/**
	 * Return the value associated with the column: ITEM_VENDOR_ID
	 */
	public com.floreantpos.model.InventoryVendor getItemVendor () {
					return itemVendor;
			}

	/**
	 * Set the value related to the column: ITEM_VENDOR_ID
	 * @param itemVendor the ITEM_VENDOR_ID value
	 */
	public void setItemVendor (com.floreantpos.model.InventoryVendor itemVendor) {
		this.itemVendor = itemVendor;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.InventoryItem)) return false;
		else {
			com.floreantpos.model.InventoryItem inventoryItem = (com.floreantpos.model.InventoryItem) obj;
			if (null == this.getId() || null == inventoryItem.getId()) return this == obj;
			else return (this.getId().equals(inventoryItem.getId()));
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