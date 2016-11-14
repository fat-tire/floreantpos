package com.floreantpos.model;

public enum DiscountType {
	AMOUNT("AMOUNT"), //$NON-NLS-1$
	PERCENTAGE("PERCENTAGE"), //$NON-NLS-1$
	RE_PRICE("RE-PRICE"), //$NON-NLS-1$
	ALT_PRICE("ALT-PRICE"); //$NON-NLS-1$
	
	private String displayText;
	
	private DiscountType(String displayText) {
		this.displayText = displayText;
	}
	
	public String getDisplayText() {
		return displayText;
	}
}
