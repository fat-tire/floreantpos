package com.floreantpos.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.main.Application;

public class CreditCardReport {
	private Date fromDate;

	private Date toDate;

	private Date reportTime;

	private int totalSalesCount;

	private double totalSales;

	private double netTips;

	private double tipsPaid;

	private double netCharge;
	
	private List<CreditCardReportData> reportDatas = new ArrayList<CreditCardReportData>();

	private CreditCardReportTableModel tableModel;
	
	public CreditCardReportTableModel getTableModel() {
		if(tableModel == null) {
			tableModel = new CreditCardReportTableModel(reportDatas);
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

	public double getNetCharge() {
		return netCharge;
	}

	public void setNetCharge(double netCharge) {
		this.netCharge = netCharge;
	}

	public double getNetTips() {
		return netTips;
	}

	public void setNetTips(double netTips) {
		this.netTips = netTips;
	}

	public double getTipsPaid() {
		return tipsPaid;
	}

	public void setTipsPaid(double tipsPaid) {
		this.tipsPaid = tipsPaid;
	}

	public double getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(double totalSales) {
		this.totalSales = totalSales;
	}

	public int getTotalSalesCount() {
		return totalSalesCount;
	}

	public void setTotalSalesCount(int totalSalesCount) {
		this.totalSalesCount = totalSalesCount;
	}
	
	public void addReportData(CreditCardReportData data) {
		reportDatas.add(data);
	}
	
	public static class CreditCardReportData {
		private Integer refId;

		private String cardType;

		private Double subtotal;

		private Double tips;

		private Double total;

		public String getCardType() {
			return cardType;
		}

		public void setCardType(String cardType) {
			this.cardType = cardType;
		}

		public Integer getRefId() {
			return refId;
		}

		public void setRefId(Integer refId) {
			this.refId = refId;
		}

		public Double getSubtotal() {
			return subtotal;
		}

		public void setSubtotal(Double subtotal) {
			this.subtotal = subtotal;
		}

		public Double getTips() {
			return tips;
		}

		public void setTips(Double tips) {
			this.tips = tips;
		}

		public Double getTotal() {
			return total;
		}

		public void setTotal(Double total) {
			this.total = total;
		}

	}
	
	public static class CreditCardReportTableModel extends ListTableModel {
		
		public CreditCardReportTableModel(List<CreditCardReportData> datas) {
			super(new String[] {"refId", "cardType", com.floreantpos.POSConstants.SUBTOTAL, "tips", com.floreantpos.POSConstants.TOTAL}, datas);
		}
		
		
		public Object getValueAt(int rowIndex, int columnIndex) {
			CreditCardReportData data = (CreditCardReportData) rows.get(rowIndex);
			
			switch(columnIndex) {
			case 0:
				return String.valueOf(data.getRefId());
				
			case 1:
				return data.getCardType();
				
			case 2:
				return Application.formatNumber(data.getSubtotal());
				
			case 3:
				return Application.formatNumber(data.getTips());
				
			case 4:
				return Application.formatNumber(data.getTotal());
			}
			
			return null;
		}
		
	}
}
