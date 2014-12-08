package com.floreantpos.ui.views.payment;

import net.authorize.data.creditcard.CardType;

import com.floreantpos.model.PosTransaction;

public class MercuryPayProcessor implements CardProcessor {

	@Override
	public void authorizeAmount(PosTransaction transaction) throws Exception {
	}

	@Override
	public String authorizeAmount(String cardTracks, double amount, String cardType) throws Exception {
		return null;
	}

	@Override
	public String authorizeAmount(String cardNumber, String expMonth, String expYear, double amount, CardType cardType) throws Exception {
		return null;
	}

	@Override
	public void captureAuthorizedAmount(PosTransaction transaction) throws Exception {
	}

	@Override
	public void captureNewAmount(PosTransaction transaction) throws Exception {
	}

	@Override
	public void voidAmount(PosTransaction transaction) throws Exception {
	}

	@Override
	public void voidAmount(String transId, double amount) throws Exception {
	}

}
