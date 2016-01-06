package com.floreantpos.model;

public enum DiscountType {
	AMOUNT("AMOUNT"),
	PERCENTAGE("PERCENTAGE"),
	RE_PRICE("RE-PRICE"),
	ALT_PRICE("ALT-PRICE");
	
	private String displayText;
	
	private DiscountType(String displayText) {
		this.displayText = displayText;
	}
	
	public String getDisplayText() {
		return displayText;
	}
}
