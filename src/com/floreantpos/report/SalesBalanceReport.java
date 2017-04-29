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

import java.util.Date;

public class SalesBalanceReport {
	private Date fromDate;
	private Date toDate;
	private Date reportTime;

	private double grossTaxableSalesAmount;
	private double grossNonTaxableSalesAmount;
	private double discountAmount;
	private double netSalesAmount;
	private double salesTaxAmount;
	private double totalRevenueAmount;
	private double giftCertSalesAmount;
	private double payInsAmount;
	private double chargedTipsAmount;
	private double grossReceiptsAmount;
	private double cashReceiptsAmount;
	private double creditCardReceiptsAmount;
	private double arReceiptsAmount;
	private double giftCertReturnAmount;
	private double giftCertChangeAmount;
	private double cashBackAmount;
	private double receiptDiffAmount;
	private double grossTipsPaidAmount;
	private double tipsDiscountAmount;
	private double cashPayoutAmount;
	private double cashAccountableAmount;
	private double drawerPullsAmount;
	private double coCurrentAmount;
	private double coPreviousAmount;
	private double overShortAmount;

	private double visaCreditCardAmount;
	private double masterCardAmount;
	private double amexAmount;
	private double discoveryAmount;

	public double getArReceiptsAmount() {
		return arReceiptsAmount;
	}

	public void setArReceiptsAmount(double arReceiptsAmount) {
		this.arReceiptsAmount = arReceiptsAmount;
	}

	public double getCashAccountableAmount() {
		return cashAccountableAmount;
	}

	public void setCashAccountableAmount(double cashAccountableAmount) {
		this.cashAccountableAmount = cashAccountableAmount;
	}

	public double getCashBackAmount() {
		return cashBackAmount;
	}

	public void setCashBackAmount(double cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public double getCashPayoutAmount() {
		return cashPayoutAmount;
	}

	public void setCashPayoutAmount(double cashPayoutAmount) {
		this.cashPayoutAmount = cashPayoutAmount;
	}

	public double getCashReceiptsAmount() {
		return cashReceiptsAmount;
	}

	public void setCashReceiptsAmount(double cashReceiptsAmount) {
		this.cashReceiptsAmount = cashReceiptsAmount;
	}

	public double getChargedTipsAmount() {
		return chargedTipsAmount;
	}

	public void setChargedTipsAmount(double chargedTipsAmount) {
		this.chargedTipsAmount = chargedTipsAmount;
	}

	public double getCoCurrentAmount() {
		return coCurrentAmount;
	}

	public void setCoCurrentAmount(double coCurrentAmount) {
		this.coCurrentAmount = coCurrentAmount;
	}

	public double getCoPreviousAmount() {
		return coPreviousAmount;
	}

	public void setCoPreviousAmount(double coPreviousAmount) {
		this.coPreviousAmount = coPreviousAmount;
	}

	public double getCreditCardReceiptsAmount() {
		return creditCardReceiptsAmount;
	}

	public void setCreditCardReceiptsAmount(double creditCardReceiptsAmount) {
		this.creditCardReceiptsAmount = creditCardReceiptsAmount;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public double getDrawerPullsAmount() {
		return drawerPullsAmount;
	}

	public void setDrawerPullsAmount(double drawerPullsAmount) {
		this.drawerPullsAmount = drawerPullsAmount;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public double getGiftCertChangeAmount() {
		return giftCertChangeAmount;
	}

	public void setGiftCertChangeAmount(double giftCertChangeAmount) {
		this.giftCertChangeAmount = giftCertChangeAmount;
	}

	public double getGiftCertReturnAmount() {
		return giftCertReturnAmount;
	}

	public void setGiftCertReturnAmount(double giftCertReturnAmount) {
		this.giftCertReturnAmount = giftCertReturnAmount;
	}

	public double getGiftCertSalesAmount() {
		return giftCertSalesAmount;
	}

	public void setGiftCertSalesAmount(double giftCertSalesAmount) {
		this.giftCertSalesAmount = giftCertSalesAmount;
	}

	public double getGrossNonTaxableSalesAmount() {
		return grossNonTaxableSalesAmount;
	}

	public void setGrossNonTaxableSalesAmount(double grossNonTaxableSalesAmount) {
		this.grossNonTaxableSalesAmount = grossNonTaxableSalesAmount;
	}

	public double getGrossReceiptsAmount() {
		return grossReceiptsAmount;
	}

	public void setGrossReceiptsAmount(double grossReceiptsAmount) {
		this.grossReceiptsAmount = grossReceiptsAmount;
	}

	public double getGrossTaxableSalesAmount() {
		return grossTaxableSalesAmount;
	}

	public void setGrossTaxableSalesAmount(double grossTaxableSalesAmount) {
		this.grossTaxableSalesAmount = grossTaxableSalesAmount;
	}

	public double getGrossTipsPaidAmount() {
		return grossTipsPaidAmount;
	}

	public void setGrossTipsPaidAmount(double grossTipsPaidAmount) {
		this.grossTipsPaidAmount = grossTipsPaidAmount;
	}

	public double getNetSalesAmount() {
		return netSalesAmount;
	}

	public void setNetSalesAmount(double netSalesAmount) {
		this.netSalesAmount = netSalesAmount;
	}

	public double getOverShortAmount() {
		return overShortAmount;
	}

	public void setOverShortAmount(double overShortAmount) {
		this.overShortAmount = overShortAmount;
	}

	public double getPayInsAmount() {
		return payInsAmount;
	}

	public void setPayInsAmount(double payInsAmount) {
		this.payInsAmount = payInsAmount;
	}

	public double getReceiptDiffAmount() {
		return receiptDiffAmount;
	}

	public void setReceiptDiffAmount(double receiptDiffAmount) {
		this.receiptDiffAmount = receiptDiffAmount;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public double getSalesTaxAmount() {
		return salesTaxAmount;
	}

	public void setSalesTaxAmount(double salesTaxAmount) {
		this.salesTaxAmount = salesTaxAmount;
	}

	public double getTipsDiscountAmount() {
		return tipsDiscountAmount;
	}

	public void setTipsDiscountAmount(double tipsDiscountAmount) {
		this.tipsDiscountAmount = tipsDiscountAmount;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public double getTotalRevenueAmount() {
		return totalRevenueAmount;
	}

	public void setTotalRevenueAmount(double totalRevenueAmount) {
		this.totalRevenueAmount = totalRevenueAmount;
	}

	public void calculate() {
		netSalesAmount = (grossTaxableSalesAmount + grossNonTaxableSalesAmount) - discountAmount;
		totalRevenueAmount = netSalesAmount + salesTaxAmount;
		grossReceiptsAmount = totalRevenueAmount + payInsAmount + chargedTipsAmount;
		receiptDiffAmount = grossReceiptsAmount - cashReceiptsAmount - creditCardReceiptsAmount - arReceiptsAmount - giftCertReturnAmount
				+ giftCertChangeAmount + cashBackAmount;
		cashAccountableAmount = cashReceiptsAmount - grossTipsPaidAmount + tipsDiscountAmount - cashPayoutAmount - giftCertChangeAmount - cashBackAmount;
		overShortAmount = cashAccountableAmount - drawerPullsAmount - coCurrentAmount + coPreviousAmount;

	}

	public double getVisaCreditCardAmount() {
		return visaCreditCardAmount;
	}

	public void setVisaCreditCardAmount(double visaCreditCardAmount) {
		this.visaCreditCardAmount = visaCreditCardAmount;
	}

	public double getMasterCardAmount() {
		return masterCardAmount;
	}

	public void setMasterCardAmount(double masterCardAmount) {
		this.masterCardAmount = masterCardAmount;
	}

	public double getAmexAmount() {
		return amexAmount;
	}

	public void setAmexAmount(double amexAmount) {
		this.amexAmount = amexAmount;
	}

	public double getDiscoveryAmount() {
		return discoveryAmount;
	}

	public void setDiscoveryAmount(double discoveryAmount) {
		this.discoveryAmount = discoveryAmount;
	}

}
