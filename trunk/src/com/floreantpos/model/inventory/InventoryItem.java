package com.floreantpos.model.inventory;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.inventory.base.BaseInventoryItem;
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

	/**
	 * Constructor for required fields
	 */
	public InventoryItem (
		java.lang.Integer id,
		java.lang.String name) {

		super (
			id,
			name);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public Boolean isVisible() {
		return visible == null ? true : visible;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public static InventoryItem fromCSV(String csvLine) {
		if(StringUtils.isEmpty(csvLine)) {
			return null;
		}
		
		String[] strings = csvLine.split(",");
		
		InventoryItem inventoryItem = new InventoryItem();
		
		int index = 0;
		
//		new NotNull(), // name
//        new Optional(), // barcode
//        new Optional(), // description
//        new Optional(), // packSizeDescription
//        new Optional(new ParseInt()), // itemPerPackSize
//        new Optional(new ParseInt()), // sortOrder
//        new Optional(), // packSizeReorderLevel
//        new Optional(), // packSizeReplenishLevel
//        new Optional(new ParseInt()), // packSizeQuantityOnHand
//        new Optional(new ParseInt()), // totalPackSizeValue
//        new Optional(new ParseInt()), // totalPacks
//        new Optional(new ParseDouble()), // totalBalance
//        new Optional(new ParseInt()), // totalRecepieUnits
//        new Optional(new ParseDouble()), // purchasePrice
//        new Optional(new ParseDouble()), // sellingPrice
		
		try {
			inventoryItem.setName(strings[index++]);
			inventoryItem.setDescription(strings[index++]);
			//group
			inventoryItem.setItemPerPackSize(POSUtil.parseInteger(strings[index++]));
			inventoryItem.setTotalPackSizeValue(POSUtil.parseInteger(strings[index++]));
			inventoryItem.setSortOrder(POSUtil.parseInteger(strings[index++]));
			//vendor
			//location
			inventoryItem.setPackSizeReorderLevel(strings[index++]);
			inventoryItem.setPackSizeReplenishLevel(strings[index++]);
			inventoryItem.setBarcode(strings[index++]);
			inventoryItem.setTotalPacks(POSUtil.parseInteger(strings[index++]));
			inventoryItem.setTotalBalance((float) POSUtil.parseDouble(strings[index++]));
			inventoryItem.setTotalRecepieUnits(POSUtil.parseInteger(strings[index++]));
			inventoryItem.setPurchasePrice(POSUtil.parseDouble(strings[index++]));
			inventoryItem.setSellingPrice(POSUtil.parseDouble(strings[index++]));
			inventoryItem.setPackSizeDescription(strings[index++]);

		} catch (Exception e) {
			//e.printStackTrace();
		}
		//name, barcode, pack size description, item/pack size, sortorder, pack size reorder level, pack size replenish level, description, total packs, total balance, total recepie unit, total pack size value, purchase price, selling price
		
		return inventoryItem;

//		inventoryItem.setItemGroup((InventoryGroup) cbGroup.getSelectedItem());
//		inventoryItem.setItemLocation((InventoryLocation) cbLocation.getSelectedItem());
//		inventoryItem.setItemVendor((InventoryVendor) cbVendor.getSelectedItem());
	}

}