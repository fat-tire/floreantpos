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

import com.floreantpos.model.PosTransaction;

public interface CardProcessor {
	public void preAuth(PosTransaction transaction) throws Exception;
	public void captureAuthAmount(PosTransaction transaction) throws Exception;
	public void chargeAmount(PosTransaction transaction) throws Exception;
	public void voidTransaction(PosTransaction transaction) throws Exception;
	public String getCardInformationForReceipt(PosTransaction transaction);
	public void cancelTransaction();
	
	public boolean supportTipsAdjustMent();
	public void adjustTips(PosTransaction transaction) throws Exception;
}
