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
import java.util.Iterator;
import java.util.List;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseTicketItem;
import com.floreantpos.util.DiscountUtil;
import com.floreantpos.util.NumberUtil;

public class TicketItem extends BaseTicketItem implements ITicketItem {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public TicketItem() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TicketItem(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public TicketItem(java.lang.Integer id, com.floreantpos.model.Ticket ticket) {

		super(id, ticket);
	}

	/*[CONSTRUCTOR MARKER END]*/

	private boolean priceIncludesTax;

	private int tableRowNum;

	public int getTableRowNum() {
		return tableRowNum;
	}

	public void setTableRowNum(int tableRowNum) {
		this.tableRowNum = tableRowNum;
	}

	public boolean canAddCookingInstruction() {
		if (isPrintedToKitchen())
			return false;

		return true;
	}

	public java.lang.Double getTaxAmount() {
		if (getTicket().isTaxExempt()) {
			return 0.0;
		}

		return super.getTaxAmount();
	}

	@Override
	public String toString() {
		return getName();
	}

	public void addCookingInstruction(TicketItemCookingInstruction cookingInstruction) {
		List<TicketItemCookingInstruction> cookingInstructions = getCookingInstructions();

		if (cookingInstructions == null) {
			cookingInstructions = new ArrayList<TicketItemCookingInstruction>(2);
			setCookingInstructions(cookingInstructions);
		}

		cookingInstructions.add(cookingInstruction);
	}

	public void addCookingInstructions(List<TicketItemCookingInstruction> instructions) {
		List<TicketItemCookingInstruction> cookingInstructions = getCookingInstructions();

		if (cookingInstructions == null) {
			cookingInstructions = new ArrayList<TicketItemCookingInstruction>(2);
			setCookingInstructions(cookingInstructions);
		}

		cookingInstructions.addAll(instructions);
	}

	public void removeCookingInstruction(TicketItemCookingInstruction itemCookingInstruction) {
		List<TicketItemCookingInstruction> cookingInstructions2 = getCookingInstructions();
		if (cookingInstructions2 == null) {
			return;
		}

		for (Iterator iterator = cookingInstructions2.iterator(); iterator.hasNext();) {
			TicketItemCookingInstruction ticketItemCookingInstruction = (TicketItemCookingInstruction) iterator.next();
			if (ticketItemCookingInstruction.getTableRowNum() == itemCookingInstruction.getTableRowNum()) {
				iterator.remove();
				return;
			}
		}
	}

	public TicketItemModifierGroup findTicketItemModifierGroup(MenuModifier menuModifier, boolean createNew) {
		MenuItemModifierGroup menuItemModifierGroup = menuModifier.getMenuItemModifierGroup();

		List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();

		if (ticketItemModifierGroups != null) {
			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				if (ticketItemModifierGroup.getMenuItemModifierGroup().getId().equals(menuItemModifierGroup.getId())) {
					return ticketItemModifierGroup;
				}
			}
		}

		TicketItemModifierGroup ticketItemModifierGroup = new TicketItemModifierGroup();
		ticketItemModifierGroup.setMenuItemModifierGroup(menuItemModifierGroup);
		ticketItemModifierGroup.setMinQuantity(menuItemModifierGroup.getMinQuantity());
		ticketItemModifierGroup.setMaxQuantity(menuItemModifierGroup.getMaxQuantity());
		ticketItemModifierGroup.setParent(this);
		addToticketItemModifierGroups(ticketItemModifierGroup);

		return ticketItemModifierGroup;
	}

	public TicketItemModifierGroup findTicketItemModifierGroup(int menuModifierGroupId) {
		List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();

		if (ticketItemModifierGroups != null) {
			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				if (ticketItemModifierGroup.getMenuItemModifierGroup().getId() == menuModifierGroupId) {
					return ticketItemModifierGroup;
				}
			}
		}

		return null;
	}

	public TicketItemModifier findAddOnFor(MenuModifier modifier) {
		List<TicketItemModifier> list = getAddOns();
		if (list == null) {
			return null;
		}

		for (TicketItemModifier ticketItemModifier : list) {
			if (modifier.getId().equals(ticketItemModifier.getItemId())) {
				return ticketItemModifier;
			}
		}

		return null;
	}

	public void addAddOn(MenuModifier menuModifier) {
		List<TicketItemModifier> list = getAddOns();
		if (list == null) {
			list = new ArrayList<TicketItemModifier>(2);
			setAddOns(list);
		}

		for (int i = list.size() - 1; i >= 0; i--) {
			TicketItemModifier ticketItemModifier = (TicketItemModifier) list.get(i);
			if (menuModifier.getId().equals(ticketItemModifier.getItemId())) {
				if ((i != list.size() - 1) || ticketItemModifier.isPrintedToKitchen()) {
					list.add(convertToAddOn(menuModifier));
				}
				else {
					ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() + 1);
				}
				return;
			}
		}

		list.add(convertToAddOn(menuModifier));
	}

	public TicketItemModifier convertToAddOn(MenuModifier menuModifier) {
		TicketItemModifier ticketItemModifier = new TicketItemModifier();
		ticketItemModifier.setItemId(menuModifier.getId());
		ticketItemModifier.setGroupId(menuModifier.getModifierGroup().getId());
		ticketItemModifier.setItemCount(1);
		ticketItemModifier.setName(menuModifier.getDisplayName());

		ticketItemModifier.setUnitPrice(menuModifier.getExtraPrice());
		ticketItemModifier.setModifierType(TicketItemModifier.EXTRA_MODIFIER);

		ticketItemModifier.setTaxRate(menuModifier.getTax() == null ? 0 : menuModifier.getTax().getRate());
		ticketItemModifier.setShouldPrintToKitchen(menuModifier.isShouldPrintToKitchen());
		ticketItemModifier.setTicketItem(this);

		return ticketItemModifier;
	}
	
	public void removeAddOn(TicketItemModifier addOn) {
		List<TicketItemModifier> addOns = getAddOns();
		if(addOns == null) {
			return;
		}
		
		for (Iterator iterator = addOns.iterator(); iterator.hasNext();) {
			TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
			if(ticketItemModifier.getItemId().equals(addOn.getItemId())) {
				iterator.remove();
			}
		}
	}

	public void calculatePrice() {
		priceIncludesTax = Application.getInstance().isPriceIncludesTax();

		List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
		if (ticketItemModifierGroups != null) {
			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				ticketItemModifierGroup.calculatePrice();
			}
		}

		List<TicketItemModifier> addOns = getAddOns();
		if (addOns != null) {
			for (TicketItemModifier ticketItemModifier : addOns) {
				ticketItemModifier.calculatePrice();
			}
		}

		setSubtotalAmount(NumberUtil.roundToTwoDigit(calculateSubtotal(true)));
		setSubtotalAmountWithoutModifiers(NumberUtil.roundToTwoDigit(calculateSubtotal(false)));
		setDiscountAmount(NumberUtil.roundToTwoDigit(calculateDiscount()));
		setTaxAmount(NumberUtil.roundToTwoDigit(calculateTax(true)));
		setTaxAmountWithoutModifiers(NumberUtil.roundToTwoDigit(calculateTax(false)));
		setTotalAmount(NumberUtil.roundToTwoDigit(calculateTotal(true)));
		setTotalAmountWithoutModifiers(NumberUtil.roundToTwoDigit(calculateTotal(false)));
	}

	//	public double calculateSubtotal() {
	//		double subtotal = NumberUtil.roundToTwoDigit(calculateSubtotal(true));
	//		
	//		return subtotal;
	//	}
	//	
	//	public double calculateSubtotalWithoutModifiers() {
	//		double subtotalWithoutModifiers = NumberUtil.roundToTwoDigit(calculateSubtotal(false));
	//		
	//		return subtotalWithoutModifiers;
	//	}

	private double calculateSubtotal(boolean includeModifierPrice) {
		double subTotalAmount = NumberUtil.roundToTwoDigit(getUnitPrice() * getItemCount());

		if (includeModifierPrice) {
			List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					subTotalAmount += ticketItemModifierGroup.getSubtotal();
				}
			}

			List<TicketItemModifier> addOns = getAddOns();
			if (addOns != null) {
				for (TicketItemModifier ticketItemModifier : addOns) {
					subTotalAmount += ticketItemModifier.getSubTotalAmount();
				}
			}
		}

		return subTotalAmount;
	}

	//TODO: ITERATE ALL discount and calculate discounts
	//	private double calculateDiscount() {
	//		double discountRate = getDiscountRate();
	//		
	//		if(discountRate < 0) {
	//			return getDiscountAmount();
	//		}
	//		
	//		double subtotalWithoutModifiers = getSubtotalAmountWithoutModifiers();
	//		double discount = 0;
	//		if (discountRate > 0) {
	//			discount = subtotalWithoutModifiers * discountRate / 100.0;
	//		}
	//		return 0;
	//	}

	private double calculateDiscount() {
		double discount = 0;
		TicketItemDiscount maxDiscount = DiscountUtil.getMaxDiscount(getDiscounts());
		if (maxDiscount != null) {
			discount = maxDiscount.calculateDiscount();
		}

		Ticket ticket = getTicket();
		if (ticket != null) {
			TicketDiscount ticketCouponAndDiscount = DiscountUtil.getMaxDiscount(ticket.getDiscounts(), getSubtotalAmountWithoutModifiers());
			if (ticketCouponAndDiscount != null) {
				discount += DiscountUtil.calculateDiscountAmount(getSubtotalAmountWithoutModifiers() - discount, ticketCouponAndDiscount);
			}
			System.out.println();

		}

		return discount;
	}

	public double getAmountByType(TicketItemDiscount discount) {

		switch (discount.getType()) {
			case Discount.DISCOUNT_TYPE_AMOUNT:
				return discount.getValue();

			case Discount.DISCOUNT_TYPE_PERCENTAGE:
				return (discount.getValue() * getUnitPrice()) / 100;

			default:
				break;
		}

		return 0;
	}

	private double calculateTax(boolean includeModifierTax) {
		double subtotal = 0;

		subtotal = getSubtotalAmountWithoutModifiers();

		double discount = getDiscountAmount();

		subtotal = subtotal - discount;

		double taxRate = getTaxRate();
		double tax = 0;

		if (taxRate > 0) {
			if (priceIncludesTax) {
				tax = subtotal - (subtotal / (1 + (taxRate / 100.0)));
			}
			else {
				tax = subtotal * (taxRate / 100.0);
			}
		}

		if (includeModifierTax) {
			List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					tax += ticketItemModifierGroup.getTax();
				}
			}

			List<TicketItemModifier> addOns = getAddOns();
			if (addOns != null) {
				for (TicketItemModifier ticketItemModifier : addOns) {
					tax += ticketItemModifier.getTaxAmount();
				}
			}
		}

		return tax;
	}

	private double calculateTotal(boolean includeModifiers) {
		double total = 0;

		if (includeModifiers) {
			if (priceIncludesTax) {
				total = getSubtotalAmount() - getDiscountAmount();
			}
			else {
				total = getSubtotalAmount() - getDiscountAmount() + getTaxAmount();
			}
		}
		else {
			if (priceIncludesTax) {
				total = getSubtotalAmountWithoutModifiers() - getDiscountAmount();
			}
			else {
				total = getSubtotalAmountWithoutModifiers() - getDiscountAmount() + getTaxAmountWithoutModifiers();
			}
		}

		return total;
	}

	@Override
	public String getNameDisplay() {
		return getName();
	}

	@Override
	public Double getUnitPriceDisplay() {
		return getUnitPrice();
	}

	@Override
	public Integer getItemCountDisplay() {
		return getItemCount();
	}

	@Override
	public Double getTaxAmountWithoutModifiersDisplay() {
		return getTaxAmountWithoutModifiers();
	}

	@Override
	public Double getTotalAmountWithoutModifiersDisplay() {
		return getTotalAmountWithoutModifiers();
	}

	@Override
	public Double getSubTotalAmountWithoutModifiersDisplay() {
		return getSubtotalAmountWithoutModifiers();
	}

	public boolean isPriceIncludesTax() {
		return priceIncludesTax;
	}

	public void setPriceIncludesTax(boolean priceIncludesTax) {
		this.priceIncludesTax = priceIncludesTax;
	}

	@Override
	public String getItemCode() {
		return String.valueOf(getItemId());
	}

	public Printer getPrinter(OrderType orderType) {
		PosPrinters printers = Application.getPrinters();
		PrinterGroup printerGroup = getPrinterGroup();

		if (printerGroup == null) {
			return printers.getDefaultKitchenPrinter();
		}

		//return printers.getKitchenPrinterFor(virtualPrinter);
		List<String> printerNames = printerGroup.getPrinterNames();
		List<Printer> kitchenPrinters = printers.getKitchenPrinters();
		for (Printer printer : kitchenPrinters) {
			if (printerNames.contains(printer.getVirtualPrinter().getName())) {
				return printer;
			}
		}

		return printers.getDefaultKitchenPrinter();
	}

	@Override
	public boolean canAddDiscount() {
		return true;
	}

	@Override
	public boolean canVoid() {
		return true;
	}

	@Override
	public boolean canAddAdOn() {
		return true;
	}
}