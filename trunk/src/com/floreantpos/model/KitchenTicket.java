package com.floreantpos.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseKitchenTicket;
import com.floreantpos.model.dao.KitchenTicketDAO;



public class KitchenTicket extends BaseKitchenTicket {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public KitchenTicket () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public KitchenTicket (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	public OrderType getType() {
		String type = getTicketType();
		
		if(StringUtils.isEmpty(type)) {
			return OrderType.DINE_IN;
		}
		
		return OrderType.valueOf(type);
	}
	
	public void setType(OrderType type) {
		setTicketType(type.name());
	}
	
	public Printer getPrinter() {
		PosPrinters printers = Application.getPrinters();
		VirtualPrinter virtualPrinter = getVirtualPrinter();
		
		if(virtualPrinter == null) {
			return printers.getDefaultKitchenPrinter();
		}
		
		return printers.getKitchenPrinterFor(virtualPrinter);
	}

	public static List<KitchenTicket> fromTicket(Ticket ticket) {
		Map<Printer, KitchenTicket> itemMap = new HashMap<Printer, KitchenTicket>();
		List<KitchenTicket> kitchenTickets = new ArrayList<KitchenTicket>(2);
		
		List<TicketItem> ticketItems = ticket.getTicketItems();
		if(ticketItems == null) {
			return kitchenTickets;
		}
		
		for (TicketItem ticketItem : ticketItems) {
			if(ticketItem.isPrintedToKitchen() || !ticketItem.isShouldPrintToKitchen()) {
				continue;
			}
			
			Printer printer = ticketItem.getPrinter();
			if(printer == null) {
				continue;
			}
			
			KitchenTicket kitchenTicket = itemMap.get(printer);
			if(kitchenTicket == null) {
				kitchenTicket = new KitchenTicket();
				kitchenTicket.setVirtualPrinter(ticketItem.getVirtualPrinter());
				kitchenTicket.setTicketId(ticket.getId());
				kitchenTicket.setCreateDate(new Date());
				kitchenTicket.setTicketType(ticket.getTicketType());
				
				if(ticket.getTableNumbers() != null) {
					kitchenTicket.setTableNumbers(new ArrayList<String>(ticket.getTableNumbers()));
				}
				
				kitchenTicket.setServerName(ticket.getOwner().getFirstName());
				kitchenTicket.setStatus(KitchenTicketStatus.WAITING.name());
				kitchenTicket.setVirtualPrinter(printer.getVirtualPrinter());
				
				KitchenTicketDAO.getInstance().saveOrUpdate(kitchenTicket);
				
				itemMap.put(printer, kitchenTicket);
			}
			
			KitchenTicketItem item = new KitchenTicketItem();
			item.setMenuItemCode(ticketItem.getItemCode());
			item.setMenuItemName(ticketItem.getNameDisplay());
			item.setQuantity(ticketItem.getItemCountDisplay());
			item.setStatus(KitchenTicketStatus.WAITING.name());
			kitchenTicket.addToticketItems(item);
			
			ticketItem.setPrintedToKitchen(true);
			
			includeModifiers(ticketItem, kitchenTicket);
			includeCookintInstructions(ticketItem, kitchenTicket);
		}
		
		Collection<KitchenTicket> values = itemMap.values();
		for (KitchenTicket kitchenTicket : values) {
			kitchenTickets.add(kitchenTicket);
		}
		
		return kitchenTickets;
	}
	
	private static void includeCookintInstructions(TicketItem ticketItem, KitchenTicket kitchenTicket) {
		List<TicketItemCookingInstruction> cookingInstructions = ticketItem.getCookingInstructions();
		if (cookingInstructions != null) {
			for (TicketItemCookingInstruction ticketItemCookingInstruction : cookingInstructions) {
				KitchenTicketItem item = new KitchenTicketItem();
				item.setMenuItemCode("");
				item.setMenuItemName(ticketItemCookingInstruction.getNameDisplay());
				item.setQuantity(ticketItemCookingInstruction.getItemCountDisplay());
				kitchenTicket.addToticketItems(item);
			}
		}
	}

	private static void includeModifiers(TicketItem ticketItem, KitchenTicket kitchenTicket) {
		List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
		if (ticketItemModifierGroups != null) {
			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
				if (ticketItemModifiers != null) {
					for (TicketItemModifier itemModifier : ticketItemModifiers) {

						if (itemModifier.isPrintedToKitchen() || !itemModifier.isShouldPrintToKitchen()) {
							continue;
						}

						KitchenTicketItem item = new KitchenTicketItem();
						item.setMenuItemCode("");
						item.setMenuItemName(itemModifier.getNameDisplay());
						item.setQuantity(itemModifier.getItemCountDisplay());
						item.setStatus(KitchenTicketStatus.WAITING.name());
						kitchenTicket.addToticketItems(item);
						
						itemModifier.setPrintedToKitchen(true);
					}
				}
			}
		}
	}
	
	public static enum KitchenTicketStatus {
		WAITING, VOID, DONE;
	}
}