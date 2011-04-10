package com.floreantpos.jreports;

import java.util.ArrayList;
import java.util.List;

import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;


public class TicketDataSource extends AbstractReportDataSource {

	public TicketDataSource() {
		super(new String[] {"itemQty", "itemName", "itemUnitPrice", "itemSubtotal"});
	}
	
	public TicketDataSource(Ticket ticket) {
		super(new String[] {"itemQty", "itemName", "itemUnitPrice", "itemSubtotal"});
		
		setTicket(ticket);
	}

	private void setTicket(Ticket ticket) {
		ArrayList<Row> rows = new ArrayList<Row>();
		
		List<TicketItem> ticketItems = ticket.getTicketItems();
		if (ticketItems != null) {
			for (TicketItem ticketItem : ticketItems) {
				Row row = new Row(ticketItem.getItemCount(), ticketItem.getName(), ticketItem.getUnitPrice(), ticketItem.getSubtotalAmountWithoutModifiers());
				rows.add(row);
				
				List<TicketItemModifierGroup> modifierGroups = ticketItem.getTicketItemModifierGroups();
				if (modifierGroups != null) {
					for (TicketItemModifierGroup modifierGroup : modifierGroups) {
						List<TicketItemModifier> modifiers = modifierGroup.getTicketItemModifiers();
						if (modifiers != null) {
							for (TicketItemModifier modifier : modifiers) {
								if (modifier.getTotalAmount() == 0) {
									continue;
								}
								boolean extra = false;
								String name = " - " + modifier.getName();
								if (modifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
									name = " - Extra " + name;
									extra = true;
								}
								row = new Row();
								row.setItemCount(modifier.getItemCount());
								row.setItemName(name);
								if(extra) {
									row.setUnitPrice(modifier.getExtraUnitPrice());
								}
								else {
									row.setUnitPrice(modifier.getUnitPrice());
								}
								row.setSubtotalAmount(modifier.getTotalAmount());
								rows.add(row);
							}
						}
					}
				}

			}
		}
		
		setRows(rows);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Row item = (Row) rows.get(rowIndex);
		
		switch(columnIndex) {
		case 0:
			return String.valueOf(item.getItemCount());
			
		case 1:
			return item.getItemName();
			
		case 2:
			return Application.formatNumber(item.getUnitPrice());
			
		case 3:
			return Application.formatNumber(item.getSubtotalAmount());
		}
		
		return null;
	}

	private class Row {
		private int itemCount;
		private String itemName;
		private double unitPrice;
		private double subtotalAmount;
		
		public Row() {
			super();
		}
		
		public Row(int itemCount, String itemName, double unitPrice, double subtotalAmount) {
			super();
			this.itemCount = itemCount;
			this.itemName = itemName;
			this.unitPrice = unitPrice;
			this.subtotalAmount = subtotalAmount;
		}



		public int getItemCount() {
			return itemCount;
		}
		public void setItemCount(int itemCount) {
			this.itemCount = itemCount;
		}
		public String getItemName() {
			return itemName;
		}
		public void setItemName(String itemName) {
			this.itemName = itemName;
		}
		public double getSubtotalAmount() {
			return subtotalAmount;
		}
		public void setSubtotalAmount(double subtotalAmount) {
			this.subtotalAmount = subtotalAmount;
		}
		public double getUnitPrice() {
			return unitPrice;
		}
		public void setUnitPrice(double unitPrice) {
			this.unitPrice = unitPrice;
		}
	}
}
