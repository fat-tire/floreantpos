package com.floreantpos.model;

import com.floreantpos.Messages;

public class TipsCashoutReportData {
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
			this.saleType = Messages.getString("TipsCashoutReportData.0"); //$NON-NLS-1$
		}
		else {
			this.saleType = this.saleType.replaceAll("_", " "); //$NON-NLS-1$ //$NON-NLS-2$
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