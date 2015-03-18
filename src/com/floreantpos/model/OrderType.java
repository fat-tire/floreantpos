package com.floreantpos.model;

public enum OrderType {
	DINE_IN, TAKE_OUT, PICKUP, HOME_DELIVERY, DRIVE_THRU, BAR_TAB;
	
	public String toString() {
		return name().replaceAll("_", " ");
	};
}
