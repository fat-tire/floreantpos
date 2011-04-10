package com.floreantpos.report;

import java.util.List;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.main.Application;
import com.floreantpos.ui.report.HourlyLaborReportView.LaborReportData;

/**
 * Created by IntelliJ IDEA.
 * User: mshahriar
 * Date: Feb 28, 2007
 * Time: 12:41:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class HourlyLaborReportModel extends ListTableModel {
	private String[] columnNames = { "period", "checks", "guests", "sales", "manHour", "labor", "salesPerMHr", "guestsPerMHr", "checksPerMHr", "laborCost" };

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
				return Application.formatNumber(reportData.getSales());
				
			case 4:
				return Application.formatNumber(reportData.getManHour());
				
			case 5:
				return Application.formatNumber(reportData.getLabor());
				
			case 6:
				return Application.formatNumber(reportData.getSalesPerMHr());
				
			case 7:
				return Application.formatNumber(reportData.getGuestsPerMHr());
				
			case 8:
				return Application.formatNumber(reportData.getCheckPerMHr());
				
			case 9:
				return Application.formatNumber(reportData.getLaborCost());
		}
		return null; 
	}
}
