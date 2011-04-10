package com.floreantpos.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.main.Application;

public class MenuUsageReport {
	private Date fromDate;

	private Date toDate;

	private Date reportTime;

	private List<MenuUsageReportData> reportDatas = new ArrayList<MenuUsageReportData>();

	private MenuUsageReportTableModel tableModel;

	public MenuUsageReportTableModel getTableModel() {
		if (tableModel == null) {
			tableModel = new MenuUsageReportTableModel(reportDatas);
		}
		return tableModel;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public void addReportData(MenuUsageReportData data) {
		reportDatas.add(data);
	}

	public static class MenuUsageReportData {
		private int count;
		private String categoryName;
		private double grossSales;
		private double discount;
		private double netSales;
		private double avgSales;
		private double profit;
		private double costPercentage;
		private double percentage;

		public double getAvgSales() {
			return avgSales;
		}

		public void setAvgSales(double avgSales) {
			this.avgSales = avgSales;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}

		public double getCostPercentage() {
			return costPercentage;
		}

		public void setCostPercentage(double costPercentage) {
			this.costPercentage = costPercentage;
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

		public double getGrossSales() {
			return grossSales;
		}

		public void setGrossSales(double grossSales) {
			this.grossSales = grossSales;
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

		public double getProfit() {
			return profit;
		}

		public void setProfit(double profit) {
			this.profit = profit;
		}

		public void calculate() {
			netSales = grossSales - discount;
			profit = netSales;
		}
	}

	public static class MenuUsageReportTableModel extends ListTableModel {

		public MenuUsageReportTableModel(List<MenuUsageReportData> datas) {
			super(new String[] { "category", "count", "grossSale", com.floreantpos.POSConstants.DISCOUNT, "netSale", "avgSale", "profit", "cost", "percentage" }, datas);
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuUsageReportData data = (MenuUsageReportData) rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return data.getCategoryName();

			case 1:
				return String.valueOf(data.getCount());

			case 2:
				return Application.formatNumber(data.getGrossSales());

			case 3:
				return Application.formatNumber(data.getDiscount());

			case 4:
				return Application.formatNumber(data.getNetSales());
			case 5:
				return " ";//Application.formatNumber(data.getAvgSales());
			case 6:
				return Application.formatNumber(data.getProfit());
			case 7:
				return " ";//Application.formatNumber(data.getCostPercentage());
			case 8:
				return " ";//Application.formatNumber(data.getPercentage());
			}

			return null;
		}

	}
}
