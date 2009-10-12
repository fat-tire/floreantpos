package com.floreantpos.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.main.Application;

public class TipsCashoutReport {
	private String server;

	private Date fromDate;

	private Date toDate;

	private Date reportTime;
	
	private int cashTipsCount;
	private double cashTipsAmount;
	private int chargedTipsCount;
	private double chargedTipsAmount;
	private double totalTips;
	private double averageTips;
	private double paidTips;
	private double tipsDue;
	
	private List<TipsCashoutReportData> datas;

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

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public void addReportData(TipsCashoutReportData data) {
		if (datas == null) {
			datas = new ArrayList<TipsCashoutReportData>();
		}

		datas.add(data);
	}
	
	public List<TipsCashoutReportData> getDatas() {
		return datas;
	}
	
	public void calculateOthers() {
		if(datas == null) {
			return;
		}
		for (TipsCashoutReportData data : datas) {
			if("*CASH*".equals(data.getSaleType())) {
				++cashTipsCount;
				cashTipsAmount += data.getTips();
			}
			else {
				++chargedTipsCount;
				chargedTipsAmount += data.getTips();
			}
			totalTips += data.getTips();
			if(data.isPaid()) {
				++paidTips;
			}
			else {
				tipsDue += data.getTips();
			}
		}
		averageTips = totalTips / datas.size();
	}
	
	public static class TipsCashoutReportData {
		private Integer ticketId;

		private String saleType;

		private Double ticketTotal;

		private Double tips;
		
		private boolean paid;

		public String getSaleType() {
			return saleType;
		}

		public void setSaleType(String saleType) {
			this.saleType = saleType;
			if(this.saleType == null) {
				this.saleType = "*CASH*";
			}
			else {
				this.saleType = this.saleType.replaceAll("_", " ");
			}
		}

		public Integer getTicketId() {
			return ticketId;
		}

		public void setTicketId(Integer ticketId) {
			this.ticketId = ticketId;
		}

		public Double getTicketTotal() {
			return ticketTotal;
		}

		public void setTicketTotal(Double ticketTotal) {
			this.ticketTotal = ticketTotal;
		}

		public Double getTips() {
			return tips;
		}

		public void setTips(Double tips) {
			this.tips = tips;
		}

		public boolean isPaid() {
			return paid;
		}

		public void setPaid(boolean paid) {
			this.paid = paid;
		}
	}
	
	public static class TipsCashoutReportTableModel extends ListTableModel {
		public TipsCashoutReportTableModel(List<TipsCashoutReportData> datas) {
			super(new String[] {"Ref#", "CD Type", "Total", "Tips"}, datas);
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			TipsCashoutReportData data = (TipsCashoutReportData) rows.get(rowIndex);
			
			switch(columnIndex) {
			case 0:
				return data.getTicketId();
				
			case 1:
				return data.getSaleType();
				
			case 2:
				return Application.formatNumber(data.getTicketTotal());
				
			case 3:
				return Application.formatNumber(data.getTips());
			}
			
			return null;
		}
	}

	public double getAverageTips() {
		return averageTips;
	}

	public void setAverageTips(double averageTips) {
		this.averageTips = averageTips;
	}

	public int getCashTipsCount() {
		return cashTipsCount;
	}

	public void setCashTipsCount(int cashTipsCount) {
		this.cashTipsCount = cashTipsCount;
	}

	public int getChargedTipsCount() {
		return chargedTipsCount;
	}

	public void setChargedTipsCount(int chargedTipsCount) {
		this.chargedTipsCount = chargedTipsCount;
	}

	public double getPaidTips() {
		return paidTips;
	}

	public void setPaidTips(double paidTips) {
		this.paidTips = paidTips;
	}

	public double getTotalTips() {
		return totalTips;
	}

	public void setTotalTips(double totalTips) {
		this.totalTips = totalTips;
	}

	public double getCashTipsAmount() {
		return cashTipsAmount;
	}

	public void setCashTipsAmount(double cashTipsAmount) {
		this.cashTipsAmount = cashTipsAmount;
	}

	public double getChargedTipsAmount() {
		return chargedTipsAmount;
	}

	public void setChargedTipsAmount(double chargedTipsAmount) {
		this.chargedTipsAmount = chargedTipsAmount;
	}

	public double getTipsDue() {
		return tipsDue;
	}

	public void setTipsDue(double tipsDue) {
		this.tipsDue = tipsDue;
	}
}
