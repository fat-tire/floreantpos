package com.floreantpos.model;

import net.authorize.data.creditcard.CardType;

public enum MerchantGateway {
	
	AUTHORIZE_NET("Authorize.net"),
	MERCURY_PAY("Mercury Pay");
	
	private String displayString;
	
	private MerchantGateway(String name) {
		this.displayString = name;
	}
	
	public String getDisplayString() {
		return displayString;
	}
	
	public boolean isCardTypeSupported(String cardName) {
		if (this == AUTHORIZE_NET) {
			return CardType.findByValue(cardName) != CardType.UNKNOWN;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return displayString;
	}
}
