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
package com.floreantpos.report;

import java.util.List;

import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.util.NumberUtil;

public class CardReportModel extends ListTableModel<CreditCardTransaction> {
	
	public CardReportModel(List<CreditCardTransaction> datas) {
		super(new String[] {"ticketId", "cardType", "authCode", "subtotal", "tips", "total"}, datas); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CreditCardTransaction transaction = getRowData(rowIndex);
		
		switch (columnIndex) {
			case 0:
				return String.valueOf(transaction.getTicket().getId());
				
			case 1:
				return transaction.getCardType();
				
			case 2:
				return transaction.getCardAuthCode();
				
			case 3:
				return NumberUtil.formatNumber(transaction.getAmount() - transaction.getTipsAmount());
				
			case 4:
				return NumberUtil.formatNumber(transaction.getTipsAmount());
				
			case 5:
				return NumberUtil.formatNumber(transaction.getAmount());
		}
		
		return null;
	}

}
