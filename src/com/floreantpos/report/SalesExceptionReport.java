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

import com.floreantpos.model.Discount;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketDiscount;
import com.floreantpos.swing.ListTableModel;

public class SalesExceptionReport {
	private Date fromDate;
	private Date toDate;
	private Date reportTime;

	private List<VoidData> voidedTickets = new ArrayList<VoidData>();
	private Map<Integer, DiscountData> disountMap = new HashMap<Integer, DiscountData>();

	public void addVoidToVoidData(Ticket ticket) {
		double amount = ticket.getSubtotalAmount();
		String voidReason = ticket.getVoidReason();

		/*boolean found = false;
		for (VoidData voidData : voidedTickets) {
			if (voidData.getReasonCode().equalsIgnoreCase(voidReason)) {
				voidData.setCount(voidData.getCount() + 1);
				voidData.setAmount(voidData.getAmount() + amount);
				found = true;
				break;
			}
		}*/
		//if (!found) {
		VoidData voidData = new VoidData();
		voidData.id = ticket.getId();
		voidData.setReasonCode(voidReason);
		voidData.setCount(1);
		voidData.setAmount(amount);
		voidData.wasted = ticket.isWasted();

		voidedTickets.add(voidData);
		//}
	}

	public void addDiscountData(Ticket ticket) {
		List<TicketDiscount> discounts = ticket.getDiscounts();
		if (discounts != null) {
			for (TicketDiscount discount : discounts) {
				String name = discount.getName();
				DiscountData discountData = disountMap.get(discount.getDiscountId());
				if (discountData == null) {
					discountData = new DiscountData();
					discountData.code = discount.getDiscountId();
					discountData.name = name;
					disountMap.put(discount.getDiscountId(), discountData);
				}

				discountData.totalCount = ++discountData.totalCount;
				discountData.totalDiscount = discountData.totalDiscount + discount.getValue();
				discountData.totalGuest = discountData.totalGuest + ticket.getNumberOfGuests();
				discountData.totalNetSales = discountData.totalNetSales + ticket.getSubtotalAmount();
				discountData.partySize = (double) (discountData.totalGuest / (double) discountData.totalCount);
				discountData.checkSize = (double) (discountData.totalNetSales / (double) discountData.totalCount);
			}
		}
	}

	public void addEmptyDiscounts(List<Discount> discounts) {
		if (discounts != null) {
			for (Discount discount : discounts) {
				String name = discount.getName();
				DiscountData discountData = disountMap.get(discount.getId());
				if (discountData == null) {
					discountData = new DiscountData();
					discountData.code = discount.getId();
					discountData.name = name;
					disountMap.put(discount.getId(), discountData);
				}
			}
		}
	}

	public static class VoidData {
		Integer id;
		private String reasonCode;
		private int count;
		private double amount;
		boolean wasted;

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getReasonCode() {
			return reasonCode;
		}

		public void setReasonCode(String reasonCode) {
			this.reasonCode = reasonCode;
		}
	}

	public static class DiscountData {
		private int code;
		private String name;
		private int totalCount;
		private double totalDiscount;
		private double totalNetSales;
		private double totalGuest;
		private double partySize;
		private double checkSize;
		private double countPercentage;
		private double ratioDNet;

		public double getCheckSize() {
			return checkSize;
		}

		public void setCheckSize(double checkSize) {
			this.checkSize = checkSize;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public double getCountPercentage() {
			return countPercentage;
		}

		public void setCountPercentage(double countPercentage) {
			this.countPercentage = countPercentage;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getPartySize() {
			return partySize;
		}

		public void setPartySize(double partySize) {
			this.partySize = partySize;
		}

		public double getRatioDNet() {
			return ratioDNet;
		}

		public void setRatioDNet(double ratioDNet) {
			this.ratioDNet = ratioDNet;
		}

		public int getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}

		public double getTotalDiscount() {
			return totalDiscount;
		}

		public void setTotalDiscount(double totalDiscount) {
			this.totalDiscount = totalDiscount;
		}

		public double getTotalGuest() {
			return totalGuest;
		}

		public void setTotalGuest(double totalGuest) {
			this.totalGuest = totalGuest;
		}

		public double getTotalNetSales() {
			return totalNetSales;
		}

		public void setTotalNetSales(double totalNetSales) {
			this.totalNetSales = totalNetSales;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

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

	public VoidTableModel getVoidTableModel() {
		VoidTableModel model = new VoidTableModel();
		model.setRows(this.voidedTickets);

		return model;
	}

	public DiscountTableModel getDiscountTableModel() {
		DiscountTableModel model = new DiscountTableModel();
		ArrayList list = new ArrayList(disountMap.values());
		model.setRows(list);

		return model;
	}

	public class VoidTableModel extends ListTableModel {
		public VoidTableModel() {
			setColumnNames(new String[] { "code", "reason", "wast", "qty", "amount" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			VoidData data = (VoidData) rows.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return String.valueOf(data.id);

				case 1:
					return data.getReasonCode();

				case 2:
					return data.wasted ? "Y" : "N"; //$NON-NLS-1$ //$NON-NLS-2$

				case 3:
					return String.valueOf(data.getCount());

				case 4:
					return data.getAmount();
			}

			return null;
		}

	}

	public class DiscountTableModel extends ListTableModel {
		public DiscountTableModel() {
			setColumnNames(new String[] {
					"no", "name", "code", "totalCount", "totalDiscount", "totalNetSales", "totalGuests", "partySize", "checkSize", "countPercent", "ratioDnet" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			DiscountData data = (DiscountData) rows.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return data.code;

				case 1:
					return data.name;

				case 2:
					return data.code;

				case 3:
					return data.totalCount;

				case 4:
					return data.totalDiscount;
				case 5:
					return data.totalNetSales;
				case 6:
					return data.totalGuest;
				case 7:
					return data.partySize;
				case 8:
					return data.checkSize;
				case 9:
					return data.countPercentage;
				case 10:
					return data.ratioDNet;
			}

			return null;
		}

	}
}
