package com.floreantpos.model.inventory.base;

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

	public static String REF = "InventoryItem";
	public static String PROP_ITEM_PER_PACK_SIZE = "itemPerPackSize";
	public static String PROP_PURCHASE_PRICE = "purchasePrice";
	public static String PROP_BARCODE = "barcode";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_ITEM_VENDOR = "itemVendor";
	public static String PROP_ITEM_GROUP = "itemGroup";
	public static String PROP_VISIBLE = "visible";
	public static String PROP_SORT_ORDER = "sortOrder";
	public static String PROP_PACK_SIZE_REORDER_LEVEL = "packSizeReorderLevel";
	public static String PROP_PACK_SIZE_REPLENISH_LEVEL = "packSizeReplenishLevel";
	public static String PROP_PACK_SIZE_QUANTITY_ON_HAND = "packSizeQuantityOnHand";
	public static String PROP_TOTAL_PACKS = "totalPacks";
	public static String PROP_TOTAL_BALANCE = "totalBalance";
	public static String PROP_NAME = "name";
	public static String PROP_LAST_UPDATE_DATE = "lastUpdateDate";
	public static String PROP_ITEM_LOCATION = "itemLocation";
	public static String PROP_TOTAL_PACK_SIZE_VALUE = "totalPackSizeValue";
	public static String PROP_CREATE_TIME = "createTime";
	public static String PROP_SELLING_PRICE = "sellingPrice";
	public static String PROP_PACK_SIZE_DESCRIPTION = "packSizeDescription";
	public static String PROP_TOTAL_RECEPIE_UNITS = "totalRecepieUnits";
	public static String PROP_ID = "id";


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

	/**
	 * Constructor for required fields
	 */
	public BaseInventoryItem (
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

	 java.util.Date modifiedTime;

	// fields
		protected java.lang.String name;
		protected java.lang.String barcode;
		protected java.lang.String packSizeDescription;
		protected int itemPerPackSize;
		protected java.lang.Integer sortOrder;
		protected java.lang.String packSizeReorderLevel;
		protected java.lang.String packSizeReplenishLevel;
		protected java.lang.String description;
		protected int packSizeQuantityOnHand;
		protected int totalPackSizeValue;
		protected java.util.Date createTime;
		protected int totalPacks;
		protected float totalBalance;
		protected int totalRecepieUnits;
		protected java.util.Date lastUpdateDate;
		protected double purchasePrice;
		protected double sellingPrice;
		protected java.lang.Boolean visible;

	// many to one
	private com.floreantpos.model.inventory.InventoryGroup itemGroup;
	private com.floreantpos.model.inventory.InventoryLocation itemLocation;
	private com.floreantpos.model.inventory.InventoryVendor itemVendor;



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
	 * Return the value associated with the column: BARCODE
	 */
	public java.lang.String getBarcode () {
					return barcode;
			}

	/**
	 * Set the value related to the column: BARCODE
	 * @param barcode the BARCODE value
	 */
	public void setBarcode (java.lang.String barcode) {
		this.barcode = barcode;
	}



	/**
	 * Return the value associated with the column: PACKSIZE_DESC
	 */
	public java.lang.String getPackSizeDescription () {
					return packSizeDescription;
			}

	/**
	 * Set the value related to the column: PACKSIZE_DESC
	 * @param packSizeDescription the PACKSIZE_DESC value
	 */
	public void setPackSizeDescription (java.lang.String packSizeDescription) {
		this.packSizeDescription = packSizeDescription;
	}



	/**
	 * Return the value associated with the column: TOT_ITEM_PER_PACKSIZE
	 */
	public int getItemPerPackSize () {
					return itemPerPackSize;
			}

	/**
	 * Set the value related to the column: TOT_ITEM_PER_PACKSIZE
	 * @param itemPerPackSize the TOT_ITEM_PER_PACKSIZE value
	 */
	public void setItemPerPackSize (int itemPerPackSize) {
		this.itemPerPackSize = itemPerPackSize;
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
	 * Return the value associated with the column: PACK_SIZE_REORDER_LEVEL
	 */
	public java.lang.String getPackSizeReorderLevel () {
					return packSizeReorderLevel;
			}

	/**
	 * Set the value related to the column: PACK_SIZE_REORDER_LEVEL
	 * @param packSizeReorderLevel the PACK_SIZE_REORDER_LEVEL value
	 */
	public void setPackSizeReorderLevel (java.lang.String packSizeReorderLevel) {
		this.packSizeReorderLevel = packSizeReorderLevel;
	}



	/**
	 * Return the value associated with the column: PACK_SIZE_REPLENISH_LEVEL
	 */
	public java.lang.String getPackSizeReplenishLevel () {
					return packSizeReplenishLevel;
			}

	/**
	 * Set the value related to the column: PACK_SIZE_REPLENISH_LEVEL
	 * @param packSizeReplenishLevel the PACK_SIZE_REPLENISH_LEVEL value
	 */
	public void setPackSizeReplenishLevel (java.lang.String packSizeReplenishLevel) {
		this.packSizeReplenishLevel = packSizeReplenishLevel;
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
	 * Return the value associated with the column: PACKSIZE_QTY_ON_HAND
	 */
	public int getPackSizeQuantityOnHand () {
					return packSizeQuantityOnHand;
			}

	/**
	 * Set the value related to the column: PACKSIZE_QTY_ON_HAND
	 * @param packSizeQuantityOnHand the PACKSIZE_QTY_ON_HAND value
	 */
	public void setPackSizeQuantityOnHand (int packSizeQuantityOnHand) {
		this.packSizeQuantityOnHand = packSizeQuantityOnHand;
	}



	/**
	 * Return the value associated with the column: TOT_PACKSIZE_VALUE
	 */
	public int getTotalPackSizeValue () {
					return totalPackSizeValue;
			}

	/**
	 * Set the value related to the column: TOT_PACKSIZE_VALUE
	 * @param totalPackSizeValue the TOT_PACKSIZE_VALUE value
	 */
	public void setTotalPackSizeValue (int totalPackSizeValue) {
		this.totalPackSizeValue = totalPackSizeValue;
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
	 * Return the value associated with the column: BALANCE_TOTAL_PACKS
	 */
	public int getTotalPacks () {
					return totalPacks;
			}

	/**
	 * Set the value related to the column: BALANCE_TOTAL_PACKS
	 * @param totalPacks the BALANCE_TOTAL_PACKS value
	 */
	public void setTotalPacks (int totalPacks) {
		this.totalPacks = totalPacks;
	}



	/**
	 * Return the value associated with the column: BALANCE_TOTAL_ITEMS
	 */
	public float getTotalBalance () {
					return totalBalance;
			}

	/**
	 * Set the value related to the column: BALANCE_TOTAL_ITEMS
	 * @param totalBalance the BALANCE_TOTAL_ITEMS value
	 */
	public void setTotalBalance (float totalBalance) {
		this.totalBalance = totalBalance;
	}



	/**
	 * Return the value associated with the column: BALANCE_TOTAL_RECEPIE_UNITS
	 */
	public int getTotalRecepieUnits () {
					return totalRecepieUnits;
			}

	/**
	 * Set the value related to the column: BALANCE_TOTAL_RECEPIE_UNITS
	 * @param totalRecepieUnits the BALANCE_TOTAL_RECEPIE_UNITS value
	 */
	public void setTotalRecepieUnits (int totalRecepieUnits) {
		this.totalRecepieUnits = totalRecepieUnits;
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
	 * Return the value associated with the column: PURCHASE_PRICE
	 */
	public double getPurchasePrice () {
					return purchasePrice;
			}

	/**
	 * Set the value related to the column: PURCHASE_PRICE
	 * @param purchasePrice the PURCHASE_PRICE value
	 */
	public void setPurchasePrice (double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}



	/**
	 * Return the value associated with the column: SELLING_PRICE
	 */
	public double getSellingPrice () {
					return sellingPrice;
			}

	/**
	 * Set the value related to the column: SELLING_PRICE
	 * @param sellingPrice the SELLING_PRICE value
	 */
	public void setSellingPrice (double sellingPrice) {
		this.sellingPrice = sellingPrice;
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
	 * Return the value associated with the column: ITEM_GROUP_ID
	 */
	public com.floreantpos.model.inventory.InventoryGroup getItemGroup () {
					return itemGroup;
			}

	/**
	 * Set the value related to the column: ITEM_GROUP_ID
	 * @param itemGroup the ITEM_GROUP_ID value
	 */
	public void setItemGroup (com.floreantpos.model.inventory.InventoryGroup itemGroup) {
		this.itemGroup = itemGroup;
	}



	/**
	 * Return the value associated with the column: ITEM_LOCATION_ID
	 */
	public com.floreantpos.model.inventory.InventoryLocation getItemLocation () {
					return itemLocation;
			}

	/**
	 * Set the value related to the column: ITEM_LOCATION_ID
	 * @param itemLocation the ITEM_LOCATION_ID value
	 */
	public void setItemLocation (com.floreantpos.model.inventory.InventoryLocation itemLocation) {
		this.itemLocation = itemLocation;
	}



	/**
	 * Return the value associated with the column: ITEM_VENDOR_ID
	 */
	public com.floreantpos.model.inventory.InventoryVendor getItemVendor () {
					return itemVendor;
			}

	/**
	 * Set the value related to the column: ITEM_VENDOR_ID
	 * @param itemVendor the ITEM_VENDOR_ID value
	 */
	public void setItemVendor (com.floreantpos.model.inventory.InventoryVendor itemVendor) {
		this.itemVendor = itemVendor;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.inventory.InventoryItem)) return false;
		else {
			com.floreantpos.model.inventory.InventoryItem inventoryItem = (com.floreantpos.model.inventory.InventoryItem) obj;
			if (null == this.getId() || null == inventoryItem.getId()) return false;
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