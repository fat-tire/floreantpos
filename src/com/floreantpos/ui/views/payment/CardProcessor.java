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
