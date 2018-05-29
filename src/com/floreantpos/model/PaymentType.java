/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.model;
import com.floreantpos.config.CardConfig;

public enum PaymentType {
	CUSTOM_PAYMENT("CUSTOM PAYMENT"), CASH("CASH"), //$NON-NLS-1$ //$NON-NLS-2$
	//CASH("CASH"),
	CREDIT_CARD("CREDIT CARD"), DEBIT_CARD("DEBIT CARD"), DEBIT_VISA("VISA", "visa_card.png"), DEBIT_MASTER_CARD("MASTER CARD", "master_card.png"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ 
	CREDIT_VISA("VISA", "visa_card.png"), CREDIT_MASTER_CARD("MASTER CARD", "master_card.png"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	CREDIT_AMEX("AMEX", "am_ex_card.png"), CREDIT_DISCOVERY("DISCOVER", "discover_card.png"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
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

	public static PaymentType findByValue(String value) {
		if (value != null) {
			for (PaymentType paymentType : values()) {
				if (paymentType.displayString.equals(value)) {
					return paymentType;
				}
			}
		}
		return PaymentType.CASH;
	}

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
			case CREDIT_CARD:
			case CREDIT_VISA:
			case CREDIT_AMEX:
			case CREDIT_DISCOVERY:
			case CREDIT_MASTER_CARD:
				transaction = new CreditCardTransaction();
				transaction.setAuthorizable(true);
				break;

			case DEBIT_CARD:
			case DEBIT_MASTER_CARD:
			case DEBIT_VISA:
				transaction = new DebitCardTransaction();
				transaction.setAuthorizable(true);
				break;

			case GIFT_CERTIFICATE:
				transaction = new GiftCertificateTransaction();
				break;

			case CUSTOM_PAYMENT:
				transaction = new CustomPaymentTransaction();
				break;

			default:
				transaction = new CashTransaction();
				break;
		}

		transaction.setPaymentType(getDisplayString());
		return transaction;
	}
}
