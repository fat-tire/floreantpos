package com.floreantpos.model;

public enum OrderType {
	DINE_IN, TAKE_OUT, PICKUP, HOME_DELIVERY, DRIVE_THRU, BAR_TAB;
	
	private OrderTypeProperties properties;
	
	public String toString() {
		return name().replaceAll("_", " ");
	}

	public OrderTypeProperties getProperties() {
		return properties;
	}

	public void setProperties(OrderTypeProperties properties) {
		this.properties = properties;
	};
}
