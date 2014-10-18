package com.floreantpos.report;

import java.util.List;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.util.NumberUtil;

public class CardReportModel extends ListTableModel<CreditCardTransaction> {
	
	public CardReportModel(List<CreditCardTransaction> datas) {
		super(new String[] {"ticketId", "cardType", "authCode", "subtotal", "tips", "total"}, datas);
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
