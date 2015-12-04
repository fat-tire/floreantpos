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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.DebitCardTransaction;
import com.floreantpos.swing.ListTableModel;

public class SalesDetailedReport {
	private Date fromDate;
	private Date toDate;
	private Date reportTime;
	
	int giftCertReturnCount;
	double giftCertReturnAmount;
	int giftCertChangeCount;
	double giftCertChangeAmount;
	
	int tipsCount;
	double chargedTips;
	double tipsPaid;
	double tipsDifferential;

	private List<DrawerPullData> drawerPullDatas = new ArrayList<DrawerPullData>();
	private Map<String,CreditCardData> creditCardDatas = new HashMap<String,CreditCardData>();

	public void addCreditCardData(CreditCardTransaction t) {
		CreditCardData data = creditCardDatas.get(t.getCardType());
		if(data == null) {
			data = new CreditCardData();
			data.setCardName(t.getCardType());
			creditCardDatas.put(t.getCardType(), data);
		}
		data.setSalesCount(data.getSalesCount() + 1);
		data.setSalesAmount(data.getSalesAmount() + t.getAmount());
		data.setNetSalesAmount(data.getNetSalesAmount() + t.getAmount());
		//data.setNetTipsAmount(data.getNetTipsAmount() + t.getGratuityAmount());
	}
	
	public void addCreditCardData(DebitCardTransaction t) {
		CreditCardData data = creditCardDatas.get(t.getCardType());
		if(data == null) {
			data = new CreditCardData();
			data.setCardName(t.getCardType());
			creditCardDatas.put(t.getCardType(), data);
		}
		data.setSalesCount(data.getSalesCount() + 1);
		data.setSalesAmount(data.getSalesAmount() + t.getAmount());
		data.setNetSalesAmount(data.getNetSalesAmount() + t.getAmount());
//		data.setNetTipsAmount(data.getNetTipsAmount() + t.getGratuityAmount());
	}

	public void addDrawerPullData(DrawerPullData data) {
		drawerPullDatas.add(data);
	}

	public static class DrawerPullData {
		private Integer drawerPullId;
		private int ticketCount;
		private double idealAmount;
		private double actualAmount;
		private double varinceAmount;

		public double getActualAmount() {
			return actualAmount;
		}

		public void setActualAmount(double actualAmount) {
			this.actualAmount = actualAmount;
		}

		public Integer getDrawerPullId() {
			return drawerPullId;
		}

		public void setDrawerPullId(Integer drawerPullId) {
			this.drawerPullId = drawerPullId;
		}

		public double getIdealAmount() {
			return idealAmount;
		}

		public void setIdealAmount(double idealAmount) {
			this.idealAmount = idealAmount;
		}

		public int getTicketCount() {
			return ticketCount;
		}

		public void setTicketCount(int ticketCount) {
			this.ticketCount = ticketCount;
		}

		public double getVarinceAmount() {
			return varinceAmount;
		}

		public void setVarinceAmount(double varinceAmount) {
			this.varinceAmount = varinceAmount;
		}

	}

	public static class CreditCardData {
		String cardName;
		int salesCount;
		double salesAmount;
		int returnCount;
		double returnAmount;
		double netSalesAmount;
		double netTipsAmount;
		double percentage;

		public String getCardName() {
			return cardName;
		}

		public void setCardName(String cardName) {
			this.cardName = cardName;
		}

		public double getNetSalesAmount() {
			return netSalesAmount;
		}

		public void setNetSalesAmount(double netSalesAmount) {
			this.netSalesAmount = netSalesAmount;
		}

		public double getNetTipsAmount() {
			return netTipsAmount;
		}

		public void setNetTipsAmount(double netTipsAmount) {
			this.netTipsAmount = netTipsAmount;
		}

		public double getPercentage() {
			return percentage;
		}

		public void setPercentage(double percentage) {
			this.percentage = percentage;
		}

		public double getReturnAmount() {
			return returnAmount;
		}

		public void setReturnAmount(double returnAmount) {
			this.returnAmount = returnAmount;
		}

		public int getReturnCount() {
			return returnCount;
		}

		public void setReturnCount(int returnCount) {
			this.returnCount = returnCount;
		}

		public double getSalesAmount() {
			return salesAmount;
		}

		public void setSalesAmount(double salesAmount) {
			this.salesAmount = salesAmount;
		}

		public int getSalesCount() {
			return salesCount;
		}

		public void setSalesCount(int salesCount) {
			this.salesCount = salesCount;
		}

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

	public DrawerPullDataTableModel getDrawerPullDataTableModel() {
		DrawerPullDataTableModel model = new DrawerPullDataTableModel();
		model.setRows(this.drawerPullDatas);

		return model;
	}

	public CreditCardDataTableModel getCreditCardDataTableModel() {
		CreditCardDataTableModel model = new CreditCardDataTableModel();
		ArrayList list = new ArrayList(creditCardDatas.values());
		model.setRows(list);

		return model;
	}

	public class DrawerPullDataTableModel extends ListTableModel {
		public DrawerPullDataTableModel() {
			setColumnNames(new String[] { "no", "count", "ideal", "actual", "variant" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			DrawerPullData data = (DrawerPullData) rows.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return com.floreantpos.POSConstants.DRAWER_PULL_ + data.drawerPullId;

				case 1:
					return data.getTicketCount();

				case 2:
					return data.idealAmount;

				case 3:
					return data.actualAmount;

				case 4:
					return data.getVarinceAmount();
			}

			return null;
		}

	}

	public class CreditCardDataTableModel extends ListTableModel {
		public CreditCardDataTableModel() {
			setColumnNames(new String[] { "creditCard", "salesCount", "salesAmount", "returnCount", "returnAmount", "netAmount", "netTipsAmount", "percentage" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			CreditCardData data = (CreditCardData) rows.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return data.cardName;

				case 1:
					return data.salesCount;

				case 2:
					return data.salesAmount;

				case 3:
					return data.returnCount;

				case 4:
					return data.returnAmount;
				case 5:
					return data.netSalesAmount;
				case 6:
					return data.netTipsAmount;
				case 7:
					return data.percentage;
			}

			return null;
		}

	}

	public double getChargedTips() {
		return chargedTips;
	}

	public void setChargedTips(double chargedTips) {
		this.chargedTips = chargedTips;
	}

	public double getGiftCertChangeAmount() {
		return giftCertChangeAmount;
	}

	public void setGiftCertChangeAmount(double giftCertChangeAmount) {
		this.giftCertChangeAmount = giftCertChangeAmount;
	}

	public int getGiftCertChangeCount() {
		return giftCertChangeCount;
	}

	public void setGiftCertChangeCount(int giftCertChangeCount) {
		this.giftCertChangeCount = giftCertChangeCount;
	}

	public double getGiftCertReturnAmount() {
		return giftCertReturnAmount;
	}

	public void setGiftCertReturnAmount(double giftCertReturnAmount) {
		this.giftCertReturnAmount = giftCertReturnAmount;
	}

	public int getGiftCertReturnCount() {
		return giftCertReturnCount;
	}

	public void setGiftCertReturnCount(int giftCertReturnCount) {
		this.giftCertReturnCount = giftCertReturnCount;
	}

	public double getTipsDifferential() {
		return tipsDifferential;
	}

	public void setTipsDifferential(double tipsDifferential) {
		this.tipsDifferential = tipsDifferential;
	}

	public double getTipsPaid() {
		return tipsPaid;
	}

	public void setTipsPaid(double tipsPaid) {
		this.tipsPaid = tipsPaid;
	}

	public int getTipsCount() {
		return tipsCount;
	}

	public void setTipsCount(int tipsCount) {
		this.tipsCount = tipsCount;
	}
}
