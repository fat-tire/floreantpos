package com.floreantpos.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.floreantpos.swing.ListTableModel;

public class PayrollReportModel extends ListTableModel {
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM-dd-yyyy hh:mm a"); //$NON-NLS-1$

	DecimalFormat decimalFormat = new DecimalFormat("0.00"); //$NON-NLS-1$

	public PayrollReportModel() {
		super(new String[] { "userID", "userName", "from", "to", "total", "rate", "payment", "userSSN" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		PayrollReportData data = (PayrollReportData) rows.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return String.valueOf(data.getUser().getUserId());

		case 1:
			return data.getUser().getFirstName() + " " + data.getUser().getLastName(); //$NON-NLS-1$

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
