package com.floreantpos.model;

public enum PaymentType {
	CASH("CASH"), DEBIT_VISA("VISA", "visa_card.png"), DEBIT_MASTER_CARD("MASTER CARD", "master_card.png"), 
	CREDIT_VISA("VISA", "visa_card.png"), CREDIT_MASTER_CARD("MASTER CARD", "master_card.png"), 
	CREDIT_AMEX("AMEX", "am_ex_card.png"), CREDIT_DISCOVERY("DISCOVER", "discover_card.png"), 
	GIFT_CERTIFICATE("GIFT CERTIFICATE");

	private String displayString;
	private String imageFile;

	private PaymentType(String display) {
		this.displayString = display;
	}

	private PaymentType(String display, String image) {
		this.displayString = display;
		this.imageFile = image;
	}

	@Override
	public String toString() {
		return displayString;
	}

	public String getDisplayString() {
		return displayString;
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}

	public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	};
}
