package com.floreantpos.model;

import java.util.Iterator;
import java.util.List;

import com.floreantpos.model.base.BaseTableBookingInfo;

public class TableBookingInfo extends BaseTableBookingInfo {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public TableBookingInfo() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TableBookingInfo(java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/
	public String toString() {
		return getId().toString();
	}
	
	private String bookedTableNumbers;

	/**
	 * @return table numbers as comma separated string.
	 */
	public String getBookedTableNumbers() {
		if(bookedTableNumbers != null) {
			return bookedTableNumbers;
		}
		
		List<ShopTable> shopTables = getTables();
		if(shopTables == null || shopTables.isEmpty()) {
			return null;
		}
		String tableNumbers = "";
		
		for (Iterator iterator = shopTables.iterator(); iterator.hasNext();) {
			ShopTable shopTable = (ShopTable) iterator.next();
			tableNumbers += shopTable.getTableNumber();
			
			if(iterator.hasNext()) {
				tableNumbers += ", ";
			}
		}
		
		return tableNumbers;
	}
	
	public void setBookedTableNumbers(String bookTableNumbers) {
		this.bookedTableNumbers = bookTableNumbers;
	}
}