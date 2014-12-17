package com.floreantpos.ui.views.payment;

import net.authorize.data.creditcard.CardType;

import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;

public interface CardProcessor {
	public void authorizeAmount(PosTransaction transaction) throws Exception;
	
	public String authorizeAmount(Ticket ticket, String cardTracks, double amount, String cardType) throws Exception;
	
	public String authorizeAmount(String cardNumber, String expMonth, String expYear, double amount, CardType cardType) throws Exception;
	
	public void captureAuthorizedAmount(PosTransaction transaction) throws Exception;
	
	public void captureNewAmount(PosTransaction transaction) throws Exception;
	
	public void voidAmount(PosTransaction transaction) throws Exception;
	
	public void voidAmount(String transId, double amount) throws Exception;
}
