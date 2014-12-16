package com.floreantpos.model;

import com.floreantpos.ui.views.payment.AuthorizeDotNetProcessor;
import com.floreantpos.ui.views.payment.CardProcessor;
import com.floreantpos.ui.views.payment.MercuryPayProcessor;

import net.authorize.data.creditcard.CardType;

public enum MerchantGateway {
	
	AUTHORIZE_NET("Authorize.net"),
	MERCURY_PAY("Mercury Pay");
	
	private String displayString;
	private CardProcessor processor;
	
	private MerchantGateway(String name) {
		this.displayString = name;
	}
	
	public String getDisplayString() {
		return displayString;
	}
	
	public CardProcessor getProcessor() {
		if(processor != null) {
			return processor;
		}
		
		switch (this) {
			case AUTHORIZE_NET:
				processor = new AuthorizeDotNetProcessor();
				break;
				
			case MERCURY_PAY:
				processor = new MercuryPayProcessor();
				break;
				

		}
		
		return processor;
	}
	
//	public boolean isManualEntrySupported() {
//		if(processor != null) {
//			return processor;
//		}
//		
//		switch (this) {
//			case AUTHORIZE_NET:
//				processor = new AuthorizeDotNetProcessor();
//				break;
//				
//			case MERCURY_PAY:
//				processor = new MercuryPayProcessor();
//				break;
//				
//
//		}
//		
//		return processor;
//	}
	
	public boolean isCardTypeSupported(String cardName) {
		if (this == AUTHORIZE_NET) {
			return CardType.findByValue(cardName) != CardType.UNKNOWN;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return displayString;
	}
}
