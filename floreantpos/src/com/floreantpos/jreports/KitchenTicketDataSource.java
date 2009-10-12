package com.floreantpos.jreports;

import java.util.ArrayList;
import java.util.List;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;


public class KitchenTicketDataSource extends AbstractReportDataSource {

	public KitchenTicketDataSource() {
		super(new String[] {"itemNo", "itemName", "itemQty"});
	}
	
	public KitchenTicketDataSource(Ticket ticket) {
		super(new String[] {"itemNo", "itemName", "itemQty"});
		
		setTicket(ticket);
	}

	public void setTicket(Ticket ticket) {
		ArrayList<Row> rows = new ArrayList<Row>();
		
		List<TicketItem> ticketItems = ticket.getTicketItems();
		if (ticketItems != null) {
			for (TicketItem ticketItem : ticketItems) {
				if(ticketItem.isShouldPrintToKitchen() && !ticketItem.isPrintedToKitchen()) {
					Row row1 = new Row(ticketItem.getItemCount(), ticketItem.getName(), ticketItem.getId());
					rows.add(row1);
				}
				ticketItem.setPrintedToKitchen(true);
				
				List<TicketItemModifierGroup> modifierGroups = ticketItem.getTicketItemModifierGroups();
				if (modifierGroups != null) {
					for (TicketItemModifierGroup modifierGroup : modifierGroups) {
						List<TicketItemModifier> modifiers = modifierGroup.getTicketItemModifiers();
						if (modifiers != null) {
							for (TicketItemModifier modifier : modifiers) {
								if(!modifier.isShouldPrintToKitchen() || modifier.isPrintedToKitchen()) {
									continue;
								}
								modifier.setPrintedToKitchen(true);
								
								String name = " - " + modifier.getName();
								if (modifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
									name = " - Extra " + name;
								}
								Row row = new Row();
								row.setItemCount(modifier.getItemCount());
								row.setItemName(name);
								row.setItemNo(modifier.getId());
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
			return String.valueOf(item.getItemNo());
			
		case 1:
			return item.getItemName();
			
		case 2:
			return String.valueOf(item.getItemCount());
		}
		
		return null;
	}

	private class Row {
		private int itemCount;
		private String itemName;
		private int itemNo;
		
		public Row() {
			super();
		}

		public Row(int itemCount, String itemName, int itemNo) {
			super();
			this.itemCount = itemCount;
			this.itemName = itemName;
			this.itemNo = itemNo;
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

		public int getItemNo() {
			return itemNo;
		}

		public void setItemNo(int itemNo) {
			this.itemNo = itemNo;
		}
		
		
	}
}
