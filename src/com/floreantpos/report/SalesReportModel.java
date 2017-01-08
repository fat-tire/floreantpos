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

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class SalesReportModel extends AbstractTableModel {
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00"); //$NON-NLS-1$

	private String[] columnNames = { "Id", "Name", "Price", "QTY", "Total", "Dis", "Tax", "Tax Total", "Gross Total" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ 
	private List<ReportItem> items;
	private double grandTotal;
	private double totalQuantity;
	private double taxTotal;
	private double grossTotal;
	private double discountTotal;
	private double itemTotal;

	public SalesReportModel() {
		super();
	}

	public int getRowCount() {
		if (items == null) {
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

		switch (columnIndex) {
			case 0:
				return item.getUniqueId();
			case 1:
				return item.getName();

			case 2:
				return formatter.format(item.getPrice());

			case 3:
				return item.getQuantity();

			case 4:
				return formatter.format(item.getTotal());

			case 5:
				return String.valueOf(item.getDiscount());

			case 6:
				return String.valueOf(item.getTaxRate()) + "%"; //$NON-NLS-1$

			case 7:
				return formatter.format(item.getTaxTotal());

			case 8:
				return item.getGrossTotal();
		}

		return null;
	}

	public double getGrossTotal() {
		return grossTotal;
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
		return formatter.format(grandTotal);
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public void calculateGrandTotal() {
		grandTotal = 0;
		if (items == null) {
			return;
		}

		for (ReportItem item : items) {
			grandTotal += item.getTotal();
		}
	}

	public String getTaxTotalAsString() {
		return formatter.format(taxTotal);
	}

	public void setTaxTotal(double taxTotal) {
		this.taxTotal = taxTotal;
	}

	public void calculateTaxTotal() {
		taxTotal = 0;
		if (items == null) {
			return;
		}

		for (ReportItem item : items) {
			taxTotal += item.getTaxTotal();
		}
	}

	public double getGrossTotalAsDouble() {
		return grossTotal;
	}

	public void setGrossTotal(double grossTotal) {
		this.grossTotal = grossTotal;
	}

	public void calculateGrossTotal() {
		grossTotal = 0;
		if (items == null) {
			return;
		}

		for (ReportItem item : items) {
			grossTotal += item.getGrossTotal();
		}
	}

	public double getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public void calculateTotalQuantity() {
		totalQuantity = 0;
		if (items == null) {
			return;
		}

		for (ReportItem item : items) {
			totalQuantity += item.getQuantity();
		}
	}

	public void calculateTotal() {
		itemTotal = 0.0;
		if (items == null) {
			return;
		}

		for (ReportItem item : items) {
			itemTotal += item.getTotal();
		}
	}

	public String getTotalAsString() {
		return formatter.format(itemTotal);
	}

	public String getDiscountTotalAsString() {
		return String.valueOf(discountTotal);
	}

	public void setDiscountTotal(int discountTotal) {
		this.discountTotal = discountTotal;
	}

	public void calculateDiscountTotal() {
		discountTotal = 0;
		if (items == null) {
			return;
		}

		for (ReportItem item : items) {
			discountTotal += item.getDiscount();
		}
	}

}
