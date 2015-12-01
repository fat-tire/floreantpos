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