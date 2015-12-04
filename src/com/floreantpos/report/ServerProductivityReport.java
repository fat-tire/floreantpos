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
import java.util.Date;
import java.util.List;

import com.floreantpos.swing.ListTableModel;
import com.floreantpos.util.NumberUtil;

public class ServerProductivityReport {
	//GST_GROSS=total sales
	//AVG_CHK=total sale/total ckeck count
	//gross_gst = total guests ..

	private Date fromDate;

	private Date toDate;

	private Date reportTime;

	private List<ServerProductivityReportData> reportDatas = new ArrayList<ServerProductivityReportData>();

	private ServerProductivityReportTableModel tableModel;

	public ServerProductivityReportTableModel getTableModel() {
		if (tableModel == null) {
			tableModel = new ServerProductivityReportTableModel(reportDatas);
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

	public void addReportData(ServerProductivityReportData data) {
		reportDatas.add(data);
	}

	public static class ServerProductivityReportData {
		private String serverName;
		private int totalCheckCount;
		private int totalGuestCount;
		private double totalSales;
		private double totalAllocation;
		
		private String categoryName;
		private int checkCount;
		private double salesDiscount;
		private double averageCheck;
		private double averageGuest;
		private double grossSales;
		private double netSales;
		private double averageNetSales;
		private double allocation;

		public double getAllocation() {
			return allocation;
		}

		public void setAllocation(double allocation) {
			this.allocation = allocation;
		}

		public double getAverageCheck() {
			return averageCheck;
		}

		public void setAverageCheck(double averageCheck) {
			this.averageCheck = averageCheck;
		}

		public double getAverageGuest() {
			return averageGuest;
		}

		public void setAverageGuest(double averageGuest) {
			this.averageGuest = averageGuest;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}

		public double getSalesDiscount() {
			return salesDiscount;
		}

		public void setSalesDiscount(double salesDiscount) {
			this.salesDiscount = salesDiscount;
		}

		public String getServerName() {
			return serverName;
		}

		public void setServerName(String serverName) {
			this.serverName = serverName;
		}

		public double getTotalAllocation() {
			return totalAllocation;
		}

		public void setTotalAllocation(double totalAllocation) {
			this.totalAllocation = totalAllocation;
		}

		public int getTotalCheckCount() {
			return totalCheckCount;
		}

		public void setTotalCheckCount(int totalCheckCount) {
			this.totalCheckCount = totalCheckCount;
		}

		public int getTotalGuestCount() {
			return totalGuestCount;
		}

		public void setTotalGuestCount(int totalGuestCount) {
			this.totalGuestCount = totalGuestCount;
		}

		public double getTotalSales() {
			return totalSales;
		}

		public void setTotalSales(double totalSales) {
			this.totalSales = totalSales;
		}

		public void calculate() {
			if(totalCheckCount > 0) {
				averageCheck = totalSales / totalCheckCount;
			}
			if(totalGuestCount > 0) {
				averageGuest = totalSales / totalGuestCount;
			}
			netSales = grossSales - salesDiscount;
			if(checkCount > 0) {
				averageNetSales = netSales / checkCount;
				allocation = ((double) totalCheckCount / (double) checkCount) * 100.0;
			}
			
		}

		public double getAverageNetSales() {
			return averageNetSales;
		}

		public void setAverageNetSales(double averageNetSales) {
			this.averageNetSales = averageNetSales;
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

		public int getCheckCount() {
			return checkCount;
		}

		public void setCheckCount(int checkCountInCategory) {
			this.checkCount = checkCountInCategory;
		}
	}

	public static class ServerProductivityReportTableModel extends ListTableModel {
		String[] columnNames = { "serverName", "categoryName",  //$NON-NLS-1$ //$NON-NLS-2$
				"totalCheckCount", "totalGuestCount", "totalSales",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"netSales", "averageNetSales", "totalAllocation",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"grossSales", "salesDiscount", "averageCheck",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"averageGuest", "allocation","checkCount" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				};

		public ServerProductivityReportTableModel(List<ServerProductivityReportData> datas) {
			setColumnNames(columnNames);
			setRows(datas);
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			ServerProductivityReportData data = (ServerProductivityReportData) rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return data.getServerName();

			case 1:
				return data.getCategoryName();

			case 2:
				return String.valueOf(data.getTotalCheckCount());

			case 3:
				return String.valueOf(data.getTotalGuestCount());

			case 4:
				return NumberUtil.formatNumber(data.getTotalSales());

			case 5:
				return NumberUtil.formatNumber(data.getNetSales());
			
			case 6:
				return NumberUtil.formatNumber(data.getAverageNetSales());
			
			case 7:
				return NumberUtil.formatNumber(data.getTotalAllocation());
			
			case 8:
				return NumberUtil.formatNumber(data.getGrossSales());
			
			case 9:
				return NumberUtil.formatNumber(data.getSalesDiscount());
			
			case 10:
				return NumberUtil.formatNumber(data.getAverageCheck());
			
			case 11:
				return NumberUtil.formatNumber(data.getAverageGuest());
				
			case 12:
				return NumberUtil.formatNumber(data.getAllocation());
				
			case 13:
				return String.valueOf(data.getCheckCount());
			}

			return null;
		}

	}
}
