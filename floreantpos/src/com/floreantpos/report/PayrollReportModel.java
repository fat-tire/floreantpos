package com.floreantpos.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.floreantpos.bo.ui.explorer.ListTableModel;

public class PayrollReportModel extends ListTableModel {
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");

	DecimalFormat decimalFormat = new DecimalFormat("0.00");

	public PayrollReportModel() {
		super(new String[] { "userID", "userName", "from", "to", com.floreantpos.POSConstants.TOTAL, "rate", "payment", "userSSN" });
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		PayrollReportData data = (PayrollReportData) rows.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return data.getUser().getUserId();

		case 1:
			return data.getUser().getFirstName() + " " + data.getUser().getLastName();

		case 2:
			return dateFormat2.format(data.getFrom());

		case 3:
			return dateFormat2.format(data.getTo());

		case 4:
			return data.getTotalHour();

		case 5:
			return decimalFormat.format(data.getRate());

		case 6:
			return data.getPayment();
			
		case 7:
			return data.getUser().getSsn();
		}
		return null;
	}

}
