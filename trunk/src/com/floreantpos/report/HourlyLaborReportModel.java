package com.floreantpos.report;

import java.util.List;

import com.floreantpos.report.HourlyLaborReportView.LaborReportData;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.util.NumberUtil;

/**
 * Created by IntelliJ IDEA.
 * User: mshahriar
 * Date: Feb 28, 2007
 * Time: 12:41:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class HourlyLaborReportModel extends ListTableModel {
	private String[] columnNames = { "period", "checks", "guests", "sales", "manHour", "labor", "salesPerMHr", "guestsPerMHr", "checksPerMHr", "laborCost" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$

	public HourlyLaborReportModel() {
		setColumnNames(columnNames);
	}

	public HourlyLaborReportModel(List rows) {
		setColumnNames(columnNames);
		setRows(rows);
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		LaborReportData reportData = (LaborReportData) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return reportData.getPeriod();
				
			case 1:
				return String.valueOf(reportData.getNoOfChecks());
				
			case 2:
				return String.valueOf(reportData.getNoOfGuests());
				
			case 3:
				return NumberUtil.formatNumber(reportData.getSales());
				
			case 4:
				return NumberUtil.formatNumber(reportData.getManHour());
				
			case 5:
				return NumberUtil.formatNumber(reportData.getLabor());
				
			case 6:
				return NumberUtil.formatNumber(reportData.getSalesPerMHr());
				
			case 7:
				return NumberUtil.formatNumber(reportData.getGuestsPerMHr());
				
			case 8:
				return NumberUtil.formatNumber(reportData.getCheckPerMHr());
				
			case 9:
				return NumberUtil.formatNumber(reportData.getLaborCost());
		}
		return null; 
	}
}
