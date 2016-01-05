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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.model.Ticket;
import com.floreantpos.util.NumberUtil;

public class TicketReportModel extends AbstractTableModel {
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00"); //$NON-NLS-1$
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy"); //$NON-NLS-1$
	
	private String[] columnNames = {"id", "date", "tableNum", "status", "total"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	private List<Ticket> items;
	private double grandTotal;
	
	public TicketReportModel() {
		super();
	}

	public int getRowCount() {
		if(items == null) return 0;
		
		return items.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Ticket ticket = items.get(rowIndex);
		
		switch(columnIndex) {
			case 0:
				return String.valueOf(ticket.getId());
				
			case 1: 
				return dateFormat.format(ticket.getCreateDate());
				
			case 2:
				if(ticket.getTableNumbers().size()>0){
					return String.valueOf(ticket.getTableNumbers());
				}
				return ""; 
				
			case 3:
				if(ticket.isClosed()) {
					return com.floreantpos.POSConstants.CLOSED;
				}
				return com.floreantpos.POSConstants.OPEN;
				
			case 4:
				return NumberUtil.formatNumber(ticket.getTotalAmount());
		}
		return null;
	}

	public List<Ticket> getItems() {
		return items;
	}

	public void setItems(List<Ticket> items) {
		this.items = items;
	}
	
	public String getGrandTotalAsString() {
		return formatter.format(grandTotal);
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public void calculateGrandTotal() {
		grandTotal = 0;
		if (items == null) {
			return;
		}

		for (Ticket item : items) {
			grandTotal += item.getDueAmount();
		}
	}

}
