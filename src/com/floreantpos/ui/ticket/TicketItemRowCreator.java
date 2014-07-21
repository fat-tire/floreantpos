package com.floreantpos.ui.ticket;

import java.util.List;
import java.util.Map;

import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemCookingInstruction;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;

public class TicketItemRowCreator {
	private Ticket ticket;
	private Map<String, ITicketItem> tableRows;
	private boolean kitchenPrint;
	private boolean includeModifiers;
	private boolean includeCookingInstructions;

	public TicketItemRowCreator() {
		super();
	}

	public TicketItemRowCreator(Ticket ticket) {
		super();
		this.ticket = ticket;
	}

	public TicketItemRowCreator(Ticket ticket, Map<String, ITicketItem> tableRows) {
		super();
		this.ticket = ticket;
		this.tableRows = tableRows;
	}
	
	

	public static void calculateTicketRows(Ticket ticket, Map<String, ITicketItem> tableRows) {
		calculateTicketRows(ticket, tableRows, true, true);
	}

	public static void calculateTicketRows(Ticket ticket, Map<String, ITicketItem> tableRows, boolean includeModifiers, boolean includeCookingInstructions) {
		calculateTicketRows(ticket, tableRows, false, includeModifiers, includeCookingInstructions);
	}

	public static void calculateTicketRows(Ticket ticket, Map<String, ITicketItem> tableRows, boolean kitchenPrint, boolean includeModifiers,
			boolean includeCookingInstructions) {
		tableRows.clear();

		int rowNum = 0;

		if (ticket == null || ticket.getTicketItems() == null)
			return;

		List<TicketItem> ticketItems = ticket.getTicketItems();
		for (TicketItem ticketItem : ticketItems) {

			if (kitchenPrint && (ticketItem.isPrintedToKitchen() || !ticketItem.isShouldPrintToKitchen())) {
				continue;
			}

			ticketItem.setTableRowNum(rowNum);
			tableRows.put(String.valueOf(rowNum), ticketItem);
			rowNum++;

			if (includeModifiers) {
				rowNum = includeModifiers(ticketItem, tableRows, rowNum, kitchenPrint);
			}

			if (includeCookingInstructions) {
				rowNum = includeCookintInstructions(ticketItem, tableRows, rowNum);
			}
		}
	}

	private static int includeCookintInstructions(TicketItem ticketItem, Map<String, ITicketItem> tableRows, int rowNum) {
		List<TicketItemCookingInstruction> cookingInstructions = ticketItem.getCookingInstructions();
		if (cookingInstructions != null) {
			for (TicketItemCookingInstruction ticketItemCookingInstruction : cookingInstructions) {
				ticketItemCookingInstruction.setTableRowNum(rowNum);
				tableRows.put(String.valueOf(rowNum), ticketItemCookingInstruction);
				rowNum++;
			}
		}
		return rowNum;
	}

	private static int includeModifiers(TicketItem ticketItem, Map<String, ITicketItem> tableRows, int rowNum, boolean kitchenPrint) {
		List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
		if (ticketItemModifierGroups != null) {
			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
				if (ticketItemModifiers != null) {
					for (TicketItemModifier itemModifier : ticketItemModifiers) {

						if (kitchenPrint && (itemModifier.isPrintedToKitchen() || !itemModifier.isShouldPrintToKitchen())) {
							continue;
						}

						itemModifier.setTableRowNum(rowNum);
						tableRows.put(String.valueOf(rowNum), itemModifier);
						rowNum++;
					}
				}
			}
		}
		return rowNum;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Map<String, ITicketItem> getTableRows() {
		return tableRows;
	}

	public void setTableRows(Map<String, ITicketItem> tableRows) {
		this.tableRows = tableRows;
	}

	public boolean isKitchenPrint() {
		return kitchenPrint;
	}

	public void setKitchenPrint(boolean kitchenPrint) {
		this.kitchenPrint = kitchenPrint;
	}

	public boolean isIncludeModifiers() {
		return includeModifiers;
	}

	public void setIncludeModifiers(boolean includeModifiers) {
		this.includeModifiers = includeModifiers;
	}

	public boolean isIncludeCookingInstructions() {
		return includeCookingInstructions;
	}

	public void setIncludeCookingInstructions(boolean includeCookingInstructions) {
		this.includeCookingInstructions = includeCookingInstructions;
	}
}
