package com.floreantpos.model;

public enum OrderTypeId {
	DINE_IN(1, "Dine IN"), TAKE_OUT(2, "Take Out"), PICKUP(3, "Pickup"), HOME_DELIVERY(4, "Delivery");

	private int id;
	private String description;

	private OrderTypeId(int id, String description) {
		this.id = id;
		this.description = description;
	}

	//	DINE_IN, TAKE_OUT, PICKUP, HOME_DELIVERY, DRIVE_THRU, BAR_TAB;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	//	public String toString() {
	//		return name().replaceAll("_", " ");
	//	};
}
