package com.floreantpos.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
