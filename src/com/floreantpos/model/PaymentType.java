package com.floreantpos.model;

import com.floreantpos.config.CardConfig;

public enum PaymentType {
	CASH("CASH"), DEBIT_VISA("Visa", "visa_card.png"), DEBIT_MASTER_CARD("MasterCard", "master_card.png"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
	CREDIT_VISA("Visa", "visa_card.png"), CREDIT_MASTER_CARD("MasterCard", "master_card.png"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	CREDIT_AMEX("Amex", "am_ex_card.png"), CREDIT_DISCOVERY("Discover", "discover_card.png"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	GIFT_CERTIFICATE("GIFT CERTIFICATE"); //$NON-NLS-1$

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
	
	public boolean isSupported() {
		switch (this) {
		case CASH:
			return true;

		default:
			return CardConfig.isSwipeCardSupported() || CardConfig.isManualEntrySupported() || CardConfig.isExtTerminalSupported();
		}
	}
	
	public PosTransaction createTransaction() {
		PosTransaction transaction = null;
		switch (this) {
			case CREDIT_VISA:
			case CREDIT_AMEX:
			case CREDIT_DISCOVERY:
			case CREDIT_MASTER_CARD:
				transaction = new CreditCardTransaction();
				transaction.setAuthorizable(true);
				break;
				
			case DEBIT_MASTER_CARD:
			case DEBIT_VISA:
				transaction = new DebitCardTransaction();
				transaction.setAuthorizable(true);
				break;
				
			case GIFT_CERTIFICATE:
				transaction = new GiftCertificateTransaction();
				break;
				
			default:
				transaction = new CashTransaction();
				break;
		}
		
		transaction.setPaymentType(name());
		return transaction;
	}
}
