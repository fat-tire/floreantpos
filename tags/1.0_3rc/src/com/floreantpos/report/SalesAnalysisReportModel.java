package com.floreantpos.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.main.Application;

public class SalesAnalysisReportModel extends ListTableModel {

	public SalesAnalysisReportModel(List<SalesAnalysisData> dataList) {
		super(new String[] { "shiftName", "categoryName", "count", "gross", com.floreantpos.POSConstants.DISCOUNT, "netSales", "avgGross", "avgDiscount", "avgNet", "percentage" }, dataList);
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
				return Application.formatNumber(data.gross);

			case 4:
				return Application.formatNumber(data.discount);

			case 5:
				return Application.formatNumber(data.netSales);

			case 6:
				return " ";//Application.formatNumber(data.avgGross);

			case 7:
				return " ";//Application.formatNumber(data.avgDiscount);

			case 8:
				return " ";//Application.formatNumber(data.avgNet);

			case 9:
				return " ";//Application.formatNumber(data.percentage);
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
		data.setShiftName("SHIFT1");
		data.setCategoryName("C");
		list.add(data);

		data = new SalesAnalysisData();
		data.setShiftName("SHIFT1");
		data.setCategoryName("C2");
		list.add(data);

		data = new SalesAnalysisData();
		data.setShiftName("SHIFT2");
		data.setCategoryName("C");
		list.add(data);

		JasperReport report = (JasperReport) JRLoader.loadObject(SalesAnalysisData.class.getResource("/com/floreantpos/ui/report/sales_summary_report2.jasper"));
		JasperPrint print = JasperFillManager.fillReport(report, new HashMap(), new JRBeanCollectionDataSource(list));

		JasperViewer.viewReport(print, true);
	}
}
