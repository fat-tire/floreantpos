package com.floreantpos.model;

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
	
	@Override
	public String toString() {
		return displayString;
	}
}
