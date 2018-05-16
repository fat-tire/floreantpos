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
package com.floreantpos.extension;

import com.floreantpos.config.ui.ConfigurationView;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.views.payment.CardProcessor;

public abstract class PaymentGatewayPlugin extends AbstractFloreantPlugin {
	public abstract boolean shouldShowCardInputProcessor();

	public abstract ConfigurationView getConfigurationPane() throws Exception;

	public abstract CardProcessor getProcessor();

	public abstract String getSecurityCode();

	public abstract boolean printUsingThisTerminal();

	public abstract void printTicket(Ticket ticket);
	public abstract void printTicketWithTipsBlock(Ticket ticket);

	public abstract void printTransaction(PosTransaction transaction, boolean storeCopy, boolean customerCopy);
}
