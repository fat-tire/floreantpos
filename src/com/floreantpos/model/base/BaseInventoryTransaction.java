package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the INVENTORY_TRANSACTION table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="INVENTORY_TRANSACTION"
 */

public abstract class BaseInventoryTransaction  implements Comparable, Serializable {

	public static String REF = "InventoryTransaction"; //$NON-NLS-1$
	public static String PROP_INVENTORY_ITEM = "inventoryItem"; //$NON-NLS-1$
	public static String PROP_QUANTITY = "quantity"; //$NON-NLS-1$
	public static String PROP_TO_WAREHOUSE = "toWarehouse"; //$NON-NLS-1$
	public static String PROP_VENDOR = "vendor"; //$NON-NLS-1$
	public static String PROP_TRANSACTION_DATE = "transactionDate"; //$NON-NLS-1$
	public static String PROP_FROM_WAREHOUSE = "fromWarehouse"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_UNIT_PRICE = "unitPrice"; //$NON-NLS-1$
	public static String PROP_REMARK = "remark"; //$NON-NLS-1$
	public static String PROP_REFERENCE_NO = "referenceNo"; //$NON-NLS-1$


	// constructors
	public BaseInventoryTransaction () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseInventoryTransaction (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.util.Date transactionDate;
	protected java.lang.Double quantity;
		protected java.lang.Double unitPrice;
		protected java.lang.String remark;

	// many to one
	private com.floreantpos.model.PurchaseOrder referenceNo;
	private com.floreantpos.model.InventoryItem inventoryItem;
	private com.floreantpos.model.InventoryVendor vendor;
	private com.floreantpos.model.InventoryWarehouse fromWarehouse;
	private com.floreantpos.model.InventoryWarehouse toWarehouse;



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
	 * Return the value associated with the column: TRANSACTION_DATE
	 */
	public java.util.Date getTransactionDate () {
					return transactionDate;
			}

	/**
	 * Set the value related to the column: TRANSACTION_DATE
	 * @param transactionDate the TRANSACTION_DATE value
	 */
	public void setTransactionDate (java.util.Date transactionDate) {
		this.transactionDate = transactionDate;
	}



	/**
	 * Return the value associated with the column: UNIT_QUANTITY
	 */
	public java.lang.Double getQuantity() {
		return quantity == null ? Double.valueOf(0) : quantity;
	}

	/**
	 * Set the value related to the column: UNIT_QUANTITY
	 * @param quantity the UNIT_QUANTITY value
	 */
	public void setQuantity(java.lang.Double quantity) {
		this.quantity = quantity;
	}



	/**
	 * Return the value associated with the column: UNIT_PRICE
	 */
	public java.lang.Double getUnitPrice () {
									return unitPrice == null ? Double.valueOf(0) : unitPrice;
					}

	/**
	 * Set the value related to the column: UNIT_PRICE
	 * @param unitPrice the UNIT_PRICE value
	 */
	public void setUnitPrice (java.lang.Double unitPrice) {
		this.unitPrice = unitPrice;
	}



	/**
	 * Return the value associated with the column: REMARK
	 */
	public java.lang.String getRemark () {
					return remark;
			}

	/**
	 * Set the value related to the column: REMARK
	 * @param remark the REMARK value
	 */
	public void setRemark (java.lang.String remark) {
		this.remark = remark;
	}



	/**
	 * Return the value associated with the column: REFERENCE_ID
	 */
	public com.floreantpos.model.PurchaseOrder getReferenceNo () {
					return referenceNo;
			}

	/**
	 * Set the value related to the column: REFERENCE_ID
	 * @param referenceNo the REFERENCE_ID value
	 */
	public void setReferenceNo (com.floreantpos.model.PurchaseOrder referenceNo) {
		this.referenceNo = referenceNo;
	}



	/**
	 * Return the value associated with the column: ITEM_ID
	 */
	public com.floreantpos.model.InventoryItem getInventoryItem () {
					return inventoryItem;
			}

	/**
	 * Set the value related to the column: ITEM_ID
	 * @param inventoryItem the ITEM_ID value
	 */
	public void setInventoryItem (com.floreantpos.model.InventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}



	/**
	 * Return the value associated with the column: VENDOR_ID
	 */
	public com.floreantpos.model.InventoryVendor getVendor () {
					return vendor;
			}

	/**
	 * Set the value related to the column: VENDOR_ID
	 * @param vendor the VENDOR_ID value
	 */
	public void setVendor (com.floreantpos.model.InventoryVendor vendor) {
		this.vendor = vendor;
	}



	/**
	 * Return the value associated with the column: FROM_WAREHOUSE_ID
	 */
	public com.floreantpos.model.InventoryWarehouse getFromWarehouse () {
					return fromWarehouse;
			}

	/**
	 * Set the value related to the column: FROM_WAREHOUSE_ID
	 * @param fromWarehouse the FROM_WAREHOUSE_ID value
	 */
	public void setFromWarehouse (com.floreantpos.model.InventoryWarehouse fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}



	/**
	 * Return the value associated with the column: TO_WAREHOUSE_ID
	 */
	public com.floreantpos.model.InventoryWarehouse getToWarehouse () {
					return toWarehouse;
			}

	/**
	 * Set the value related to the column: TO_WAREHOUSE_ID
	 * @param toWarehouse the TO_WAREHOUSE_ID value
	 */
	public void setToWarehouse (com.floreantpos.model.InventoryWarehouse toWarehouse) {
		this.toWarehouse = toWarehouse;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.InventoryTransaction)) return false;
		else {
			com.floreantpos.model.InventoryTransaction inventoryTransaction = (com.floreantpos.model.InventoryTransaction) obj;
			if (null == this.getId() || null == inventoryTransaction.getId())
				return this == obj;
			else return (this.getId().equals(inventoryTransaction.getId()));
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