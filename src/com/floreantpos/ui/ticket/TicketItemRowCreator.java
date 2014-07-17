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
	public static void calculateTicketRows(Ticket ticket, Map<String, ITicketItem> tableRows) {
		tableRows.clear();

		int rowNum = 0;

		if (ticket == null || ticket.getTicketItems() == null)
			return;

		List<TicketItem> ticketItems = ticket.getTicketItems();
		for (TicketItem ticketItem : ticketItems) {
			ticketItem.setTableRowNum(rowNum);
			tableRows.put(String.valueOf(rowNum), ticketItem);
			rowNum++;

			List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
					if (ticketItemModifiers != null) {
						for (TicketItemModifier itemModifier : ticketItemModifiers) {
							itemModifier.setTableRowNum(rowNum);
							tableRows.put(String.valueOf(rowNum), itemModifier);
							rowNum++;
						}
					}
				}
			}

			List<TicketItemCookingInstruction> cookingInstructions = ticketItem.getCookingInstructions();
			if (cookingInstructions != null) {
				for (TicketItemCookingInstruction ticketItemCookingInstruction : cookingInstructions) {
					ticketItemCookingInstruction.setTableRowNum(rowNum);
					tableRows.put(String.valueOf(rowNum), ticketItemCookingInstruction);
					rowNum++;
				}
			}
		}
	}
}
