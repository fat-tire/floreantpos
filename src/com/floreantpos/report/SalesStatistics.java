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
import java.util.List;

import com.floreantpos.POSConstants;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.util.NumberUtil;

public class SalesStatistics {
	private int capacity;
	private int guestCount;
	private double guestPerSeat;
	private double tableTurnOver;
	private double avgGuest;
	private int openChecks;
	private int voidChecks;
	private int oppdChecks;
	private int trngChecks;
	private int ropnChecks;
	
	private int ntaxChecks;
	private double ntaxAmount;
	
	private int mergeChecks;
	
	private double laborHour;
	private double laborCost;
	private double laborSale;
	
	private int tables;
	private int checkCount;
	private double guestPerCheck;
	private String turnOverTime;
	private double avgCheck;
	private double openAmount;
	private double voidAmount;
	private double paidChecks;
	private double trngAmount;
	private double ropnAmount;
	
	private double mergeAmount;
	private double labor;
	
	private double grossSale;
	private double discount;
	private double tax;
	private double netSale;

	private ArrayList<ShiftwiseSalesTableData> salesTableDataList;

	public void calculateOthers() {
		netSale = grossSale - discount;
		
		if (tables > 0) {
			tableTurnOver = checkCount / tables;
		}
		if(guestCount > 0) {
			avgGuest = netSale / guestCount;
		}
		if (capacity > 0) {
			guestPerSeat = guestCount / capacity;
		}
		if (checkCount > 0) {
			guestPerCheck = guestCount / checkCount;
			avgCheck = grossSale /checkCount;
		}
		
	}

	public double getNetSale() {
		return netSale;
	}

	public void setNetSale(double netSale) {
		this.netSale = netSale;
	}

	public double getAvgGuest() {
		return avgGuest;
	}

	public void setAvgGuest(double averageGuest) {
		this.avgGuest = averageGuest;
	}

	public double getAvgCheck() {
		return avgCheck;
	}

	public void setAvgCheck(double avgChecks) {
		this.avgCheck = avgChecks;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(int checkCount) {
		this.checkCount = checkCount;
	}

	public int getGuestCount() {
		return guestCount;
	}

	public void setGuestCount(int guestCount) {
		this.guestCount = guestCount;
	}

	public double getGuestPerCheck() {
		return guestPerCheck;
	}

	public void setGuestPerCheck(double guestPerCheck) {
		this.guestPerCheck = guestPerCheck;
	}

	public double getGuestPerSeat() {
		return guestPerSeat;
	}

	public void setGuestPerSeat(double guestPerSeat) {
		this.guestPerSeat = guestPerSeat;
	}

	public double getLabor() {
		return labor;
	}

	public void setLabor(double labor) {
		this.labor = labor;
	}

	public double getLaborCost() {
		return laborCost;
	}

	public void setLaborCost(double laborCost) {
		this.laborCost = laborCost;
	}

	public double getLaborHour() {
		return laborHour;
	}

	public void setLaborHour(double laborHours) {
		this.laborHour = laborHours;
	}

	public double getLaborSale() {
		return laborSale;
	}

	public void setLaborSale(double laborSales) {
		this.laborSale = laborSales;
	}

	public double getMergeAmount() {
		return mergeAmount;
	}

	public void setMergeAmount(double mergeAmount) {
		this.mergeAmount = mergeAmount;
	}

	public int getMergeChecks() {
		return mergeChecks;
	}

	public void setMergeChecks(int mergeChecks) {
		this.mergeChecks = mergeChecks;
	}

	public double getNtaxAmount() {
		return ntaxAmount;
	}

	public void setNtaxAmount(double ntaxAmount) {
		this.ntaxAmount = ntaxAmount;
	}

	public int getNtaxChecks() {
		return ntaxChecks;
	}

	public void setNtaxChecks(int ntaxChecks) {
		this.ntaxChecks = ntaxChecks;
	}

	public double getOpenAmount() {
		return openAmount;
	}

	public void setOpenAmount(double openAmount) {
		this.openAmount = openAmount;
	}

	public int getOpenChecks() {
		return openChecks;
	}

	public void setOpenChecks(int openChecks) {
		this.openChecks = openChecks;
	}

	public int getOppdChecks() {
		return oppdChecks;
	}

	public void setOppdChecks(int oppdChecks) {
		this.oppdChecks = oppdChecks;
	}

	public double getPaidChecks() {
		return paidChecks;
	}

	public void setPaidChecks(double paidChecks) {
		this.paidChecks = paidChecks;
	}

	public double getRopnAmount() {
		return ropnAmount;
	}

	public void setRopnAmount(double ropnAmount) {
		this.ropnAmount = ropnAmount;
	}

	public int getRopnChecks() {
		return ropnChecks;
	}

	public void setRopnChecks(int ropnChecks) {
		this.ropnChecks = ropnChecks;
	}

	public int getTables() {
		return tables;
	}

	public void setTables(int tables) {
		this.tables = tables;
	}

	public double getTableTurnOver() {
		return tableTurnOver;
	}

	public void setTableTurnOver(double tableTurnOver) {
		this.tableTurnOver = tableTurnOver;
	}

	public double getTrngAmount() {
		return trngAmount;
	}

	public void setTrngAmount(double trngAmount) {
		this.trngAmount = trngAmount;
	}

	public int getTrngChecks() {
		return trngChecks;
	}

	public void setTrngChecks(int trngChecks) {
		this.trngChecks = trngChecks;
	}

	public String getTurnOverTime() {
		return turnOverTime;
	}

	public void setTurnOverTime(String turnOverTime) {
		this.turnOverTime = turnOverTime;
	}

	public double getVoidAmount() {
		return voidAmount;
	}

	public void setVoidAmount(double voidAmount) {
		this.voidAmount = voidAmount;
	}

	public int getVoidChecks() {
		return voidChecks;
	}

	public void setVoidChecks(int voidChecks) {
		this.voidChecks = voidChecks;
	}

	public ArrayList<ShiftwiseSalesTableData> getSalesTableDataList() {
		return salesTableDataList;
	}

	public void addSalesTableData(ShiftwiseSalesTableData data) {
		if (salesTableDataList == null) {
			salesTableDataList = new ArrayList<ShiftwiseSalesTableData>();
		}
		salesTableDataList.add(data);
	}

	public static class ShiftwiseSalesTableData {
		private String shiftName;
		private String profitCenter;
		private int checkCount;
		private int guestCount;
		private int entre;
		private double totalSales;
		private double avgChecks;
		private double avgGuests;
		private double percentage;

		public double getAvgChecks() {
			return avgChecks;
		}

		public void setAvgChecks(double avgChecks) {
			this.avgChecks = avgChecks;
		}

		public double getAvgGuests() {
			return avgGuests;
		}

		public void setAvgGuests(double avgGuests) {
			this.avgGuests = avgGuests;
		}

		public int getCheckCount() {
			return checkCount;
		}

		public void setCheckCount(int checkCount) {
			this.checkCount = checkCount;
		}

		public int getEntre() {
			return entre;
		}

		public void setEntre(int entre) {
			this.entre = entre;
		}

		public int getGuestCount() {
			return guestCount;
		}

		public void setGuestCount(int guestCount) {
			this.guestCount = guestCount;
		}

		public double getPercentage() {
			return percentage;
		}

		public void setPercentage(double percentage) {
			this.percentage = percentage;
		}

		public String getShiftName() {
			return shiftName;
		}

		public void setShiftName(String shiftName) {
			this.shiftName = shiftName;
		}

		public double getTotalSales() {
			return totalSales;
		}

		public void setTotalSales(double totalSales) {
			this.totalSales = totalSales;
		}

		public void calculateOthers() {
			if (totalSales > 0 && checkCount > 0) {
				avgChecks = (double) (totalSales / checkCount);
			}
			if (totalSales > 0 && guestCount > 0) {
				avgGuests = (double) (totalSales / guestCount);
			}
		}

		public String getProfitCenter() {
			return profitCenter;
		}

		public void setProfitCenter(String profitCenter) {
			this.profitCenter = profitCenter;
		}
	}

	public static class ShiftwiseDataTableModel extends ListTableModel {
		public ShiftwiseDataTableModel(List<ShiftwiseSalesTableData> list) {
			super(new String[] { POSConstants.DAYPART,
					"profitCenter", //$NON-NLS-1$
					POSConstants.CHECK,
					"Guest", //$NON-NLS-1$
					POSConstants.ENTER,
					POSConstants.SALES,
					POSConstants.AVGCHK,
					POSConstants.AVERAGE_GUEST,
					POSConstants.PERCENTAGE },
					list);
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			ShiftwiseSalesTableData data = (ShiftwiseSalesTableData) rows.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return data.getShiftName();
					
				case 1:
					return data.getProfitCenter();

				case 2:
					return String.valueOf(data.getCheckCount());

				case 3:
					return String.valueOf(data.getGuestCount());

				case 4:
					return " "; //$NON-NLS-1$

				case 5:
					return NumberUtil.formatNumber(data.getTotalSales());

				case 6:
					return NumberUtil.formatNumber(data.getAvgChecks());

				case 7:
					return NumberUtil.formatNumber(data.getAvgGuests());

				case 8:
					return NumberUtil.formatNumber(data.getPercentage());
			}
			return null;
		}

	}

	public double getGrossSale() {
		return grossSale;
	}

	public void setGrossSale(double grossSale) {
		this.grossSale = grossSale;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double totalDiscount) {
		this.discount = totalDiscount;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double totalTax) {
		this.tax = totalTax;
	}
}
