package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.base.BaseInventoryItem;
import com.floreantpos.util.POSUtil;



public class InventoryItem extends BaseInventoryItem {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public InventoryItem () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public InventoryItem (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		return getName();
	}
	
	public static InventoryItem fromCSV(String csvLine) {
		if(StringUtils.isEmpty(csvLine)) {
			return null;
		}
		
		String[] strings = csvLine.split(","); //$NON-NLS-1$
		
		InventoryItem inventoryItem = new InventoryItem();
		
		int index = 0;
		
		try {
			
//			"NAME", "UNIT_PER_PACKAGE", "TOTAL_PACKAGES", "AVERAGE_PACKAGE_PRICE", "TOTAL_RECEPIE_UNITS",
//			"UNIT_PURCHASE_PRICE", "PACKAGE_BARCODE", "UNIT_BARCODE", "PACKAGE_DESC", "SORT_ORDER", "PACKAGE_REORDER_LEVEL",
//			"PACKAGE_REPLENISH_LEVEL","DESCRIPTION","UNIT_SELLING_PRICE"
			
			inventoryItem.setName(strings[index++]);
			inventoryItem.setUnitPerPackage(POSUtil.parseDouble(strings[index++]));
			inventoryItem.setTotalPackages(POSUtil.parseInteger(strings[index++]));
			inventoryItem.setAveragePackagePrice(POSUtil.parseDouble(strings[index++]));
			inventoryItem.setTotalRecepieUnits(POSUtil.parseDouble(strings[index++]));
			inventoryItem.setUnitPurchasePrice(POSUtil.parseDouble(strings[index++]));
			inventoryItem.setPackageBarcode(strings[index++]);
			inventoryItem.setUnitBarcode(strings[index++]);
//			inventoryItem.setPackagingUnit(strings[index++]);
			inventoryItem.setSortOrder(POSUtil.parseInteger(strings[index++]));
			inventoryItem.setPackageReorderLevel(POSUtil.parseInteger(strings[index++]));
			inventoryItem.setPackageReplenishLevel(POSUtil.parseInteger(strings[index++]));
			inventoryItem.setDescription(strings[index++]);
			inventoryItem.setUnitSellingPrice(POSUtil.parseDouble(strings[index++]));
			

		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return inventoryItem;
	}

}