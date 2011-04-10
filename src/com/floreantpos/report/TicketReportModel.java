package com.floreantpos.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.model.Ticket;

public class TicketReportModel extends AbstractTableModel {
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
//	private String currencySymbol;
	
	private String[] columnNames = {com.floreantpos.POSConstants.ID, "date", "tableNum", "status", com.floreantpos.POSConstants.TOTAL};
	private List<Ticket> items;
	
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
				return String.valueOf(ticket.getTableNumber());
				
			case 3:
				if(ticket.isClosed()) {
					return com.floreantpos.POSConstants.CLOSED;
				}
				return com.floreantpos.POSConstants.OPEN;
				
			case 4:
				return formatter.format(ticket.getTotalAmount());
		}
		return null;
	}

	public List<Ticket> getItems() {
		return items;
	}

	public void setItems(List<Ticket> items) {
		this.items = items;
	}

}
