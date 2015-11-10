package com.floreantpos.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

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

	private String bookedTables;

	/**
	 * @return the bookedTables
	 */
	public String getBookedTables() {
		List<ShopTable> shopTables = getTables();
		if(shopTables == null || shopTables.isEmpty()) {
			return null;
		}
		bookedTables=String.valueOf("");
		for (ShopTable table : shopTables) {
			bookedTables += table + String.valueOf(",");
		}

		if(StringUtils.endsWith(bookedTables, String.valueOf(","))) {
			bookedTables = StringUtils.removeEnd(bookedTables, String.valueOf(","));
		}
		return bookedTables;
	}

	/**
	 * @param bookedTables the bookedTables to set
	 */
	public void setBookedTables(String bookedTables) {
		this.bookedTables = bookedTables;
	}

}