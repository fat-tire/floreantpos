package com.floreantpos.model;

public enum PaymentType {
	CASH, VISA, MASTER_CARD, GIFT_CERTIFICATE;
	
	@Override
	public String toString() {
		String string = name();
		return string.replaceAll("_", " ");
	};
}
