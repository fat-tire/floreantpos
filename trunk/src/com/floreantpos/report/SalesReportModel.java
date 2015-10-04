package com.floreantpos.report;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;

public class SalesReportModel extends AbstractTableModel {
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00"); //$NON-NLS-1$
	private String currencySymbol;
	
	private String[] columnNames = {Messages.getString("SalesReportModel.1"), Messages.getString("SalesReportModel.2"), Messages.getString("SalesReportModel.3"), Messages.getString("SalesReportModel.4"), Messages.getString("SalesReportModel.5")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	private List<ReportItem> items;
	private double grandTotal;
	
	public SalesReportModel() {
		super();
		currencySymbol = Application.getCurrencySymbol();
	}

	public int getRowCount() {
		if(items == null) {
			return 0;
		}
		
		return items.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		ReportItem item = items.get(rowIndex);
		
		switch(columnIndex) {
			case 0:
				return item.getName();
				
			case 1:
				return currencySymbol + " " + formatter.format(item.getPrice()); //$NON-NLS-1$
				
			case 2:
				return String.valueOf(item.getQuantity());
				
			case 3:
				return String.valueOf(item.getTaxRate()) + "%"; //$NON-NLS-1$
				
			case 4:
				return currencySymbol + " " + formatter.format(item.getTotal()); //$NON-NLS-1$
		}
		
		
		return null;
	}

	public List<ReportItem> getItems() {
		return items;
	}

	public void setItems(List<ReportItem> items) {
		this.items = items;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public String getGrandTotalAsString() {
		return currencySymbol + " " + formatter.format(grandTotal); //$NON-NLS-1$
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public void calculateGrandTotal() {
		grandTotal = 0;
		if(items == null) {
			return;
		}
		
		for (ReportItem item : items) {
			grandTotal += item.getTotal();
		}
	}
}
