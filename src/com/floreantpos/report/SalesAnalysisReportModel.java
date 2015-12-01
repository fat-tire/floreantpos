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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import com.floreantpos.swing.ListTableModel;

public class SalesAnalysisReportModel extends ListTableModel {

	public SalesAnalysisReportModel(List<SalesAnalysisData> dataList) {
		super(new String[] { "shiftName", "categoryName", "count", "gross", "discount", "netSales", "avgGross", "avgDiscount", "avgNet", "percentage" }, dataList); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		SalesAnalysisData data = (SalesAnalysisData) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return data.shiftName;

			case 1:
				return data.categoryName;

			case 2:
				return String.valueOf(data.count);

			case 3:
				return String.valueOf(data.gross);

			case 4:
				return String.valueOf(data.discount);

			case 5:
				return String.valueOf(data.netSales);

			case 6:
				return " ";//Application.formatNumber(data.avgGross); //$NON-NLS-1$

			case 7:
				return " ";//Application.formatNumber(data.avgDiscount); //$NON-NLS-1$

			case 8:
				return " ";//Application.formatNumber(data.avgNet); //$NON-NLS-1$

			case 9:
				return " ";//Application.formatNumber(data.percentage); //$NON-NLS-1$
		}

		return null;
	}

	public static class SalesAnalysisData {
		private String shiftName;
		private String categoryName;
		private int count;
		private double gross;
		private double discount;
		private double netSales;
		private double avgGross;
		private double avgDiscount;
		private double avgNet;
		private double percentage;
		
		public void calculate() {
			netSales = gross - discount;
		}

		public double getAvgDiscount() {
			return avgDiscount;
		}

		public void setAvgDiscount(double avgDiscount) {
			this.avgDiscount = avgDiscount;
		}

		public double getAvgGross() {
			return avgGross;
		}

		public void setAvgGross(double avgGross) {
			this.avgGross = avgGross;
		}

		public double getAvgNet() {
			return avgNet;
		}

		public void setAvgNet(double avgNet) {
			this.avgNet = avgNet;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public double getDiscount() {
			return discount;
		}

		public void setDiscount(double discount) {
			this.discount = discount;
		}

		public double getGross() {
			return gross;
		}

		public void setGross(double gross) {
			this.gross = gross;
		}

		public double getNetSales() {
			return netSales;
		}

		public void setNetSales(double netSales) {
			this.netSales = netSales;
		}

		public double getPercentage() {
			return percentage;
		}

		public void setPercentage(double percentage) {
			this.percentage = percentage;
		}

		public String getShiftName() {
			return shiftName;
		}

		public void setShiftName(String shiftName) {
			this.shiftName = shiftName;
		}

	}

	public static void main(String[] args) throws Exception {
		ArrayList list = new ArrayList();

		SalesAnalysisData data = new SalesAnalysisData();
		data.setShiftName("SHIFT1"); //$NON-NLS-1$
		data.setCategoryName("C"); //$NON-NLS-1$
		list.add(data);

		data = new SalesAnalysisData();
		data.setShiftName("SHIFT1"); //$NON-NLS-1$
		data.setCategoryName("C2"); //$NON-NLS-1$
		list.add(data);

		data = new SalesAnalysisData();
		data.setShiftName("SHIFT2"); //$NON-NLS-1$
		data.setCategoryName("C"); //$NON-NLS-1$
		list.add(data);

		JasperReport report = ReportUtil.getReport("sales_summary_report2"); //$NON-NLS-1$
		JasperPrint print = JasperFillManager.fillReport(report, new HashMap(), new JRBeanCollectionDataSource(list));

		JasperViewer.viewReport(print, true);
	}
}
