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
package com.floreantpos.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.base.BaseKitchenTicket;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.model.dao.OrderTypeDAO;
import com.floreantpos.util.GlobalIdGenerator;

public class KitchenTicket extends BaseKitchenTicket {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public KitchenTicket() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public KitchenTicket(java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/

	private String customerName;
	private Printer printer;

	public OrderType getType() {
		String type = getTicketType();

		/*if (StringUtils.isEmpty(type)) {
			return OrderType.DINE_IN;
		}*/

		return OrderTypeDAO.getInstance().findByName(type);
	}

	public void setType(OrderType type) {
		setTicketType(type.name());
	}

	public void setPrinter(Printer printer) {
		this.printer = printer;
	}

	public Printer getPrinter() {
		return this.printer;
	}

	public List<Printer> getPrinters() {
		List<Printer> printers = new ArrayList<Printer>();

		PosPrinters posPrinters = PosPrinters.load();
		PrinterGroup virtualPrinter = getPrinterGroup();

		if (virtualPrinter == null) {
			printers.add(posPrinters.getDefaultKitchenPrinter());
			return printers;
		}

		List<String> printerNames = virtualPrinter.getPrinterNames();
		List<Printer> kitchenPrinters = posPrinters.getKitchenPrinters();
		for (Printer printer : kitchenPrinters) {
			if (printerNames.contains(printer.getVirtualPrinter().getName())) {
				printers.add(printer);
			}
		}

		if (printers.size() == 0) {
			printers.add(posPrinters.getDefaultKitchenPrinter());
		}

		return printers;
	}

	//	private static List<TicketItem> consolidateTicketItems(Ticket ticket) {
	//		List<TicketItem> newticketItems = new ArrayList<TicketItem>();
	//		newticketItems.addAll(((Ticket) SerializationUtils.clone(ticket)).getTicketItems());
	//
	//		Map<String, List<TicketItem>> itemMap = new LinkedHashMap<String, List<TicketItem>>();
	//
	//		for (Iterator iterator = newticketItems.iterator(); iterator.hasNext();) {
	//			TicketItem newItem = (TicketItem) iterator.next();
	//
	//			if (!newItem.isShouldPrintToKitchen() || newItem.isPrintedToKitchen()) {
	//				continue;
	//			}
	//			List<TicketItem> itemListInMap = itemMap.get(newItem.getItemId().toString());
	//
	//			if (itemListInMap == null) {
	//				List<TicketItem> list = new ArrayList<TicketItem>();
	//				list.add(newItem);
	//
	//				itemMap.put(newItem.getItemId().toString(), list);
	//			}
	//			else {
	//				boolean merged = false;
	//				for (TicketItem itemInMap : itemListInMap) {
	//					if (itemInMap.isMergable(newItem, false)) {
	//						itemInMap.merge(newItem);
	//						merged = true;
	//						break;
	//					}
	//				}
	//
	//				if (!merged) {
	//					itemListInMap.add(newItem);
	//				}
	//			}
	//		}
	//
	//		newticketItems.clear();
	//		Collection<List<TicketItem>> values = itemMap.values();
	//		for (List<TicketItem> list : values) {
	//			if (list != null) {
	//				newticketItems.addAll(list);
	//			}
	//		}
	//
	//		for (TicketItem originalTicketItem : ticket.getTicketItems()) {
	//			List<TicketItem> itemFromMap = itemMap.get(originalTicketItem.getItemId().toString());
	//
	//			if (itemFromMap != null) {
	//				setPrintedToKitchen(originalTicketItem);
	//			}
	//		}
	//
	//		return newticketItems;
	//	}

	private static void setPrintedToKitchen(TicketItem ticketItem) {
		ticketItem.setPrintedToKitchen(true);
		List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
		if (ticketItemModifiers != null) {
			for (TicketItemModifier itemModifier : ticketItemModifiers) {

				if (!itemModifier.isShouldPrintToKitchen()) {
					continue;
				}
				itemModifier.setPrintedToKitchen(true);
			}
		}

		List<TicketItemModifier> addOns = ticketItem.getAddOns();
		if (addOns != null) {
			for (TicketItemModifier ticketItemModifier : addOns) {
				if (!ticketItemModifier.isShouldPrintToKitchen()) {
					continue;
				}
				ticketItemModifier.setPrintedToKitchen(true);
			}
		}

		List<TicketItemCookingInstruction> cookingInstructions = ticketItem.getCookingInstructions();
		if (cookingInstructions != null) {
			for (TicketItemCookingInstruction cookingInstruction : cookingInstructions) {
				cookingInstruction.setPrintedToKitchen(true);
			}
		}
	}

	public static List<KitchenTicket> fromTicket(Ticket ticket) {
		Map<Printer, KitchenTicket> itemMap = new HashMap<Printer, KitchenTicket>();
		List<KitchenTicket> kitchenTickets = new ArrayList<KitchenTicket>(4);

		Ticket clonedTicket = (Ticket) SerializationUtils.clone(ticket);
		clonedTicket.setGlobalId(GlobalIdGenerator.generateGlobalId());
		clonedTicket.consolidateTicketItems();
		List<TicketItem> ticketItems = clonedTicket.getTicketItems();
		if (ticketItems == null) {
			return kitchenTickets;
		}

		if (ticket.getOrderType().isAllowSeatBasedOrder()) {
			Collections.sort(ticketItems, new Comparator<TicketItem>() {

				@Override
				public int compare(TicketItem o1, TicketItem o2) {
					return o1.getId() - o2.getId();
				}
			});
			Collections.sort(ticketItems, new Comparator<TicketItem>() {

				@Override
				public int compare(TicketItem o1, TicketItem o2) {
					return o1.getSeatNumber() - o2.getSeatNumber();
				}

			});
		}

		for (TicketItem ticketItem : ticketItems) {
			if (ticketItem.isPrintedToKitchen() || !ticketItem.isShouldPrintToKitchen()) {
				continue;
			}

			List<Printer> printers = ticketItem.getPrinters(ticket.getOrderType());
			if (printers == null) {
				continue;
			}

			for (Printer printer : printers) {
				KitchenTicket kitchenTicket = itemMap.get(printer);
				if (kitchenTicket == null) {
					kitchenTicket = new KitchenTicket();
					kitchenTicket.setPrinterGroup(ticketItem.getPrinterGroup());
					kitchenTicket.setTicketId(ticket.getId());
					kitchenTicket.setCreateDate(new Date());
					kitchenTicket.setTicketType(ticket.getTicketType());

					if (ticket.getTableNumbers() != null) {
						kitchenTicket.setTableNumbers(new ArrayList<Integer>(ticket.getTableNumbers()));
					}

					kitchenTicket.setServerName(ticket.getOwner().getFirstName());
					kitchenTicket.setStatus(KitchenTicketStatus.WAITING.name());

					if (StringUtils.isNotEmpty(ticket.getProperty(Ticket.CUSTOMER_NAME))) {
						kitchenTicket.setCustomerName(ticket.getProperty(Ticket.CUSTOMER_NAME));
					}

					KitchenTicketDAO.getInstance().saveOrUpdate(kitchenTicket);

					kitchenTicket.setPrinter(printer);

					itemMap.put(printer, kitchenTicket);
				}

				KitchenTicketItem item = new KitchenTicketItem();
				item.setTicketItemId(ticketItem.getId());
				item.setMenuItemCode(ticketItem.getItemCode());
				item.setMenuItemName(ticketItem.getNameDisplay());
				if (ticketItem.getMenuItem() == null) {
					item.setMenuItemGroupName("MISC."); //$NON-NLS-1$
					item.setMenuItemGroupId(1001);
					item.setSortOrder(10001);
				}
				else {
					item.setMenuItemGroupName(ticketItem.getGroupName());
					item.setMenuItemGroupId(ticketItem.getMenuItem().getParent().getId());
					item.setSortOrder(ticketItem.getMenuItem().getParent().getSortOrder());
				}

				item.setFractionalUnit(ticketItem.isFractionalUnit());
				item.setUnitName(ticketItem.getItemUnitName());

				if (ticketItem.isFractionalUnit()) {
					item.setFractionalQuantity(ticketItem.getItemQuantity());
				}
				else {
					item.setQuantity(ticketItem.getItemCount());
				}
				item.setStatus(KitchenTicketStatus.WAITING.name());

				kitchenTicket.addToticketItems(item);

				ticketItem.setPrintedToKitchen(true);

				includeModifiers(ticketItem, kitchenTicket);
				includeCookintInstructions(ticketItem, kitchenTicket);

			}

		}

		Collection<KitchenTicket> values = itemMap.values();

		for (KitchenTicket kitchenTicket : values) {
			kitchenTickets.add(kitchenTicket);
			String kitchenTicketNumber = ticket.getProperty("KITCHEN_TICKET_NUMBER"); //$NON-NLS-1$
			if (kitchenTicketNumber == null) {
				kitchenTicketNumber = "1"; //$NON-NLS-1$
			}
			else {
				kitchenTicketNumber = String.valueOf(Integer.valueOf(kitchenTicketNumber) + 1);
			}
			ticket.addProperty("KITCHEN_TICKET_NUMBER", kitchenTicketNumber); //$NON-NLS-1$
			kitchenTicket.setSequenceNumber(Integer.valueOf(kitchenTicketNumber));
		}
		ticket.markPrintedToKitchen();
		return kitchenTickets;
	}

	private static void includeCookintInstructions(TicketItem ticketItem, KitchenTicket kitchenTicket) {
		List<TicketItemCookingInstruction> cookingInstructions = ticketItem.getCookingInstructions();
		if (cookingInstructions != null) {
			for (TicketItemCookingInstruction ticketItemCookingInstruction : cookingInstructions) {
				KitchenTicketItem item = new KitchenTicketItem();
				item.setCookable(false);
				item.setMenuItemName(ticketItemCookingInstruction.getNameDisplay());
				if (ticketItem.getMenuItem() == null) {
					item.setMenuItemGroupName("MISC."); //$NON-NLS-1$
					item.setMenuItemGroupId(1001);
					item.setSortOrder(10001);
				}
				else {
					item.setMenuItemGroupName(ticketItem.getGroupName());
					item.setMenuItemGroupId(ticketItem.getMenuItem().getParent().getId());
					item.setSortOrder(ticketItem.getMenuItem().getParent().getSortOrder());
				}
				kitchenTicket.addToticketItems(item);
				ticketItemCookingInstruction.setPrintedToKitchen(true);
			}
		}
	}

	private static void includeModifiers(TicketItem ticketItem, KitchenTicket kitchenTicket) {
		List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
		if (ticketItemModifiers != null) {
			for (TicketItemModifier itemModifier : ticketItemModifiers) {
				/*	if (!itemModifier.isShouldPrintToKitchen()) {
						continue;
					}*/

				if (itemModifier.isPrintedToKitchen() || !itemModifier.isShouldPrintToKitchen()) {
					continue;
				}

				//System.out.println(itemModifier.getName());

				KitchenTicketItem item = new KitchenTicketItem();
				item.setMenuItemCode(""); //$NON-NLS-1$
				item.setTicketItemModifierId(itemModifier.getId());
				String nameDisplay = (itemModifier.isInfoOnly() ? "" : "  --") + itemModifier.getNameDisplay();
				item.setMenuItemName(nameDisplay);
				if (ticketItem.getMenuItem() == null) {
					item.setMenuItemGroupName("MISC."); //$NON-NLS-1$
					item.setMenuItemGroupId(1001);
					item.setSortOrder(10001);
				}
				else {
					item.setMenuItemGroupName(ticketItem.getGroupName());
					item.setMenuItemGroupId(ticketItem.getMenuItem().getParent().getId());
					item.setSortOrder(ticketItem.getMenuItem().getParent().getSortOrder());
				}
				//item.setFractionalQuantity(itemModifier.getItemCount().doubleValue());
				item.setQuantity(itemModifier.getItemCount());
				item.setStatus(KitchenTicketStatus.WAITING.name());
				kitchenTicket.addToticketItems(item);

				itemModifier.setPrintedToKitchen(true);
			}
		}

		List<TicketItemModifier> addOns = ticketItem.getAddOns();
		if (addOns != null) {
			for (TicketItemModifier ticketItemModifier : addOns) {
				/*if (!ticketItemModifier.isShouldPrintToKitchen()) {
					continue;
				}*/

				if (ticketItemModifier.isPrintedToKitchen() || !ticketItemModifier.isShouldPrintToKitchen()) {
					continue;
				}
				KitchenTicketItem item = new KitchenTicketItem();
				item.setMenuItemCode(""); //$NON-NLS-1$
				item.setTicketItemModifierId(ticketItem.getId());
				item.setMenuItemName(ticketItemModifier.getNameDisplay());
				if (ticketItem.getMenuItem() == null) {
					item.setMenuItemGroupName("MISC."); //$NON-NLS-1$
					item.setMenuItemGroupId(1001);
					item.setSortOrder(10001);
				}
				else {
					item.setMenuItemGroupName(ticketItem.getGroupName());
					item.setMenuItemGroupId(ticketItem.getMenuItem().getParent().getId());
					item.setSortOrder(ticketItem.getMenuItem().getParent().getSortOrder());
				}
				//item.setFractionalQuantity(ticketItemModifier.getItemCount().doubleValue());
				item.setQuantity(ticketItemModifier.getItemCount());
				item.setStatus(KitchenTicketStatus.WAITING.name());
				kitchenTicket.addToticketItems(item);

				ticketItemModifier.setPrintedToKitchen(true);
			}
		}
	}

	public static enum KitchenTicketStatus {
		WAITING, VOID, DONE;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}