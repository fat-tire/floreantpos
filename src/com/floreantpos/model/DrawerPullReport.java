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

import java.util.HashSet;
import java.util.Set;

import com.floreantpos.model.base.BaseDrawerPullReport;

public class DrawerPullReport extends BaseDrawerPullReport {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public DrawerPullReport() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public DrawerPullReport(java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/

	public void setPayOutNumber(Integer i) {

	}

	public String getCashReceiptNumber() {
		return ""; //$NON-NLS-1$
	}

	public void setCashReceiptNumber(String s) {

	}

	public String getCreditCardReceiptNumber() {
		return ""; //$NON-NLS-1$
	}

	public void setCreditCardReceiptNumber(String s) {

	}

	public String getDrawerBleedNumber() {
		return ""; //$NON-NLS-1$
	}

	public void setDebitCardReceiptNumber(String s) {

	}

	public String getDebitCardReceiptNumber() {
		return ""; //$NON-NLS-1$
	}

	public void setDrawerBleedNumber(String s) {

	}

	public Integer getPayOutNumber() {
		return 0;
	}

	public void addVoidTicketEntry(DrawerPullVoidTicketEntry entry) {
		if (getVoidTickets() == null) {
			setVoidTickets(new HashSet<DrawerPullVoidTicketEntry>());
		}
		getVoidTickets().add(entry);
	}

	public void addCurrencyBalances(Set<CurrencyBalance> currencyBalance) {
		getCurrencyBalances().addAll(currencyBalance);
	}

	@Override
	public Set<CurrencyBalance> getCurrencyBalances() {
		Set<CurrencyBalance> curBalanceList = super.getCurrencyBalances();

		if (curBalanceList == null) {
			curBalanceList = new HashSet<CurrencyBalance>();
			super.setCurrencyBalances(curBalanceList);
		}
		return curBalanceList;
	}

	public void calculate() {
		setTotalRevenue(getNetSales() + getSalesTax() + getSalesDeliveryCharge());
		setGrossReceipts(getTotalRevenue() + getChargedTips());

		double total = getCashReceiptAmount() + getCreditCardReceiptAmount() + getDebitCardReceiptAmount() + getGiftCertReturnAmount()
				+ getGiftCertChangeAmount() - getCashBack() - getRefundAmount();
		setReceiptDifferential(getGrossReceipts() - total);

		setTipsDifferential(getChargedTips() - getTipsPaid());

		double totalCash = getCashReceiptAmount();
		double tips = getTipsPaid();
		double totalPayout = getPayOutAmount();
		double beginCash = getBeginCash();
		double cashBack = getCashBack();
		double refundAmount = getRefundAmount();
		double drawerBleed = getDrawerBleedAmount();

		setDrawerAccountable(beginCash + totalCash - tips - totalPayout - cashBack - refundAmount - drawerBleed);

		Set<DrawerPullVoidTicketEntry> voidTickets = getVoidTickets();
		if (voidTickets != null) {
			double totalVoidAmount = 0;
			for (DrawerPullVoidTicketEntry entry : voidTickets) {
				totalVoidAmount += entry.getAmount();
			}
			setTotalVoid(totalVoidAmount);
		}
	}
}