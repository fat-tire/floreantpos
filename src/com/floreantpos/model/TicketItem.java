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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseTicketItem;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.PrinterGroupDAO;
import com.floreantpos.util.DiscountUtil;
import com.floreantpos.util.NumberUtil;

public class TicketItem extends BaseTicketItem implements ITicketItem {
	private static final long serialVersionUID = 1L;

	public enum PIZZA_SECTION_MODE {
		FULL(1), HALF(2), QUARTER(3);

		private final int value;

		private PIZZA_SECTION_MODE(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
		
		public static PIZZA_SECTION_MODE from(int value) {
			if (value == 2) {
				return HALF;
			}
			if (value == 3) {
				return QUARTER;
			}
			return FULL;
		}

		@Override
		public String toString() {
			return name();
		}
	}

	public PIZZA_SECTION_MODE getPizzaSectionMode() {
		return PIZZA_SECTION_MODE.from(getPizzaSectionModeType());
	}

	public void setPizzaSectionMode(PIZZA_SECTION_MODE pizzaSectionMode) {
		setPizzaSectionModeType(pizzaSectionMode.getValue());
	}

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public TicketItem () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TicketItem (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public TicketItem (
		java.lang.Integer id,
		com.floreantpos.model.Ticket ticket) {

		super (
			id,
			ticket);
	}

	/*[CONSTRUCTOR MARKER END]*/

	private MenuItem menuItem;

	public TicketItem clone(TicketItem source) {
		try {
			// Write the object out to a byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(source);
			out.flush();
			out.close();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			return (TicketItem) in.readObject();
		} catch (Exception cnfe) {
			//log here
			return null;
		}
	}

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

	public TicketItemModifier addTicketItemModifier(MenuModifier menuModifier, int modifierType, OrderType type, Multiplier multiplier) {
		TicketItemModifier ticketItemModifier = new TicketItemModifier();
		ticketItemModifier.setModifierId(menuModifier.getId());
		MenuItemModifierGroup menuItemModifierGroup = menuModifier.getMenuItemModifierGroup();
		if (menuItemModifierGroup != null) {
			ticketItemModifier.setMenuItemModifierGroupId(menuItemModifierGroup.getId());
		}
		ticketItemModifier.setItemCount(1);
		ticketItemModifier.setName(menuModifier.getDisplayName());
		double price = menuModifier.getPriceForMultiplier(multiplier);
		if (multiplier != null) {
			ticketItemModifier.setMultiplierName(multiplier.getName());
			ticketItemModifier.setName(multiplier.getTicketPrefix() + " " + menuModifier.getDisplayName());
		}
		ticketItemModifier.setUnitPrice(price);
		ticketItemModifier.setTaxRate(menuModifier.getTaxByOrderType(type));

		ticketItemModifier.setModifierType(modifierType);
		ticketItemModifier.setShouldPrintToKitchen(menuModifier.isShouldPrintToKitchen());
		ticketItemModifier.setTicketItem(this);

		addToticketItemModifiers(ticketItemModifier);

		return ticketItemModifier;
	}

	public TicketItemModifier addTicketItemModifier(MenuModifier menuModifier, boolean addOn) {
		TicketItemModifier ticketItemModifier = new TicketItemModifier();
		ticketItemModifier.setModifierId(menuModifier.getId());
		MenuItemModifierGroup menuItemModifierGroup = menuModifier.getMenuItemModifierGroup();
		if (menuItemModifierGroup != null) {
			ticketItemModifier.setMenuItemModifierGroupId(menuItemModifierGroup.getId());
		}
		ticketItemModifier.setItemCount(1);
		ticketItemModifier.setName(menuModifier.getDisplayName());

		if (addOn) {
			ticketItemModifier.setUnitPrice(menuModifier.getExtraPrice());
			ticketItemModifier.setModifierType(TicketItemModifier.EXTRA_MODIFIER);
		}
		else {
			ticketItemModifier.setUnitPrice(menuModifier.getPrice());
			ticketItemModifier.setModifierType(TicketItemModifier.NORMAL_MODIFIER);
		}
		ticketItemModifier.setTaxRate(menuModifier.getTax() == null ? 0 : menuModifier.getTax().getRate());
		ticketItemModifier.setShouldPrintToKitchen(menuModifier.isShouldPrintToKitchen());
		ticketItemModifier.setTicketItem(this);

		addToticketItemModifiers(ticketItemModifier);

		return ticketItemModifier;
	}

	public void updateModifiersUnitPrice(double defaultSellPortion) {
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		if (ticketItemModifiers != null) {
			for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
				if (!ticketItemModifier.isInfoOnly()) {
					ticketItemModifier.setUnitPrice(ticketItemModifier.getUnitPrice() * defaultSellPortion / 100);
				}
			}
		}
	}

	public boolean contains(TicketItemModifier ticketItemModifier) {
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		int count = 0;
		if (ticketItemModifiers != null) {
			for (TicketItemModifier ticketItemModifier2 : ticketItemModifiers) {
				if (!ticketItemModifier2.isInfoOnly()) {
					if (ticketItemModifier.getName().trim().equals(ticketItemModifier2.getName().trim())) {
						count++;
					}
				}
			}
		}
		return (count > 1) ? true : false;
	}

	public TicketItemModifier removeTicketItemModifier(TicketItemModifier ticketItemModifier) {
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		if (ticketItemModifiers == null)
			return ticketItemModifier;

		for (Iterator iter = ticketItemModifiers.iterator(); iter.hasNext();) {
			TicketItemModifier oldTicketItemModifier = (TicketItemModifier) iter.next();
			if (oldTicketItemModifier.getModifierId().intValue() == ticketItemModifier.getModifierId().intValue()
					&& oldTicketItemModifier.getModifierType() == ticketItemModifier.getModifierType()) {
				iter.remove();
				return oldTicketItemModifier;
			}
		}
		return ticketItemModifier;
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

	//	public TicketItemModifierGroup findTicketItemModifierGroup(MenuModifier menuModifier, boolean createNew) {
	//		MenuItemModifierGroup menuItemModifierGroup = menuModifier.getMenuItemModifierGroup();
	//
	//		List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
	//
	//		if (ticketItemModifierGroups != null) {
	//			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
	//				if (ticketItemModifierGroup.getMenuItemModifierGroup().getId().equals(menuItemModifierGroup.getId())) {
	//					return ticketItemModifierGroup;
	//				}
	//			}
	//		}
	//
	//		TicketItemModifierGroup ticketItemModifierGroup = new TicketItemModifierGroup();
	//		ticketItemModifierGroup.setMenuItemModifierGroup(menuItemModifierGroup);
	//		ticketItemModifierGroup.setMinQuantity(menuItemModifierGroup.getMinQuantity());
	//		ticketItemModifierGroup.setMaxQuantity(menuItemModifierGroup.getMaxQuantity());
	//		ticketItemModifierGroup.setParent(this);
	//		addToticketItemModifierGroups(ticketItemModifierGroup);
	//
	//		return ticketItemModifierGroup;
	//	}
	//
	//	public TicketItemModifierGroup findTicketItemModifierGroup(int menuModifierGroupId) {
	//		List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
	//
	//		if (ticketItemModifierGroups != null) {
	//			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
	//				if (ticketItemModifierGroup.getMenuItemModifierGroup().getId() == menuModifierGroupId) {
	//					return ticketItemModifierGroup;
	//				}
	//			}
	//		}
	//
	//		return null;
	//	}

	public TicketItemModifier findAddOnFor(MenuModifier modifier) {
		List<TicketItemModifier> list = getAddOns();
		if (list == null) {
			return null;
		}

		for (TicketItemModifier ticketItemModifier : list) {
			if (modifier.getId().equals(ticketItemModifier.getModifierId())) {
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
			if (menuModifier.getId().equals(ticketItemModifier.getModifierId())) {
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
		ticketItemModifier.setModifierId(menuModifier.getId());
		ticketItemModifier.setMenuItemModifierGroupId(menuModifier.getModifierGroup().getId());
		ticketItemModifier.setItemCount(1);
		ticketItemModifier.setName(menuModifier.getDisplayName());

		//ticketItemModifier.setUnitPrice(menuModifier.getExtraPrice());
		ticketItemModifier.setUnitPrice(menuModifier.getExtraPriceByOrderType(this.getTicket().getOrderType()));

		ticketItemModifier.setModifierType(TicketItemModifier.EXTRA_MODIFIER);
		//ticketItemModifier.setTaxRate(menuModifier.getTax() == null ? 0 : menuModifier.getTax().getRate());
		ticketItemModifier.setTaxRate(menuModifier.getExtraTaxByOrderType(this.getTicket().getOrderType()));
		ticketItemModifier.setShouldPrintToKitchen(menuModifier.isShouldPrintToKitchen());
		ticketItemModifier.setTicketItem(this);

		return ticketItemModifier;
	}

	public void removeAddOn(TicketItemModifier addOn) {
		List<TicketItemModifier> addOns = getAddOns();
		if (addOns == null) {
			return;
		}

		for (Iterator iterator = addOns.iterator(); iterator.hasNext();) {
			TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
			if (ticketItemModifier.getModifierId().equals(addOn.getModifierId())) {
				iterator.remove();
			}
		}
	}

	public void calculatePrice() {
		priceIncludesTax = Application.getInstance().isPriceIncludesTax();

		if (getSizeModifier() != null) {
			getSizeModifier().calculatePrice();
		}
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		if (ticketItemModifiers != null) {
			for (TicketItemModifier modifier : ticketItemModifiers) {
				modifier.calculatePrice();
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

	public boolean isMergable(TicketItem otherItem, boolean merge) {
		if (this.isFractionalUnit() || this.getItemId() == 0 || (this.getCookingInstructions() != null && this.getCookingInstructions().size() > 0)) {
			return false;
		}
		if (!this.isHasModifiers() && !otherItem.isHasModifiers()) {
			if (this.isTreatAsSeat() == otherItem.isTreatAsSeat() && this.getSeatNumber().intValue() == otherItem.getSeatNumber().intValue()) {
				return true;
			}
			if (this.getItemId().equals(otherItem.getItemId()) && this.getSeatNumber() == otherItem.getSeatNumber()) {
				return true;
			}

			return false;
		}

		//		List<TicketItemModifierGroup> thisModifierGroups = this.getTicketItemModifierGroups();
		//		List<TicketItemModifierGroup> thatModifierGroups = otherItem.getTicketItemModifierGroups();
		//		if (thatModifierGroups == null) {
		//			return true;
		//		}
		//		if (thisModifierGroups.size() != thatModifierGroups.size()) {
		//			return false;
		//		}
		//
		//		Comparator<TicketItemModifierGroup> comparator = new Comparator<TicketItemModifierGroup>() {
		//			@Override
		//			public int compare(TicketItemModifierGroup o1, TicketItemModifierGroup o2) {
		//				return o1.getMenuItemModifierGroup().getId() - o2.getMenuItemModifierGroup().getId();
		//			}
		//		};
		//
		//		Collections.sort(thisModifierGroups, comparator);
		//		Collections.sort(thatModifierGroups, comparator);
		//
		//		Iterator<TicketItemModifierGroup> thisIterator = thisModifierGroups.iterator();
		//		Iterator<TicketItemModifierGroup> thatIterator = thatModifierGroups.iterator();
		//
		//		while (thisIterator.hasNext()) {
		//			TicketItemModifierGroup next1 = thisIterator.next();
		//			TicketItemModifierGroup next2 = thatIterator.next();
		//
		//			if (comparator.compare(next1, next2) != 0) {
		//				return false;
		//			}
		//			if (!next1.isMergable(next2, false)) {
		//				return false;
		//			}
		//
		//			if (merge) {
		//				next1.isMergable(next2, merge);
		//			}
		//		}

		if (!isMergableModifiers(getTicketItemModifiers(), otherItem.getTicketItemModifiers(), merge)) {
			return false;
		}
		if (!isMergableModifiers(getAddOns(), otherItem.getAddOns(), merge)) {
			return false;
		}

		return true;
	}

	public boolean isMergableModifiers(List<TicketItemModifier> thisModifiers, List<TicketItemModifier> thatModifiers, boolean merge) {
		if (thatModifiers == null) {
			return true;
		}
		if (thisModifiers.size() != thatModifiers.size()) {
			return false;
		}

		Comparator<TicketItemModifier> comparator = new Comparator<TicketItemModifier>() {
			@Override
			public int compare(TicketItemModifier o1, TicketItemModifier o2) {
				return o1.getModifierId() - o2.getModifierId();
			}
		};

		Collections.sort(thisModifiers, comparator);
		Collections.sort(thatModifiers, comparator);

		Iterator<TicketItemModifier> thisIterator = thisModifiers.iterator();
		Iterator<TicketItemModifier> thatIterator = thatModifiers.iterator();

		while (thisIterator.hasNext()) {
			TicketItemModifier next1 = thisIterator.next();
			TicketItemModifier next2 = thatIterator.next();

			if (comparator.compare(next1, next2) != 0) {
				return false;
			}

			if (merge) {
				next1.merge(next2);
			}
		}
		return true;
	}

	public void merge(TicketItem otherItem) {
		if (!this.isHasModifiers() && !otherItem.isHasModifiers()) {
			this.setItemCount(this.getItemCount() + otherItem.getItemCount());
			return;
		}
		if (isMergable(otherItem, true)) {
			this.setItemCount(this.getItemCount() + otherItem.getItemCount());
		}
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
		//TODO: added Fractional Item Unit Quantity
		double subTotalAmount;
		if (this.isFractionalUnit()) {
			subTotalAmount = NumberUtil.roundToTwoDigit(getUnitPrice() * getItemQuantity());
		}
		else {
			subTotalAmount = NumberUtil.roundToTwoDigit(getUnitPrice() * getItemCount());
		}

		if (getSizeModifier() != null) {
			subTotalAmount += getSizeModifier().getSubTotalAmount();
		}

		if (includeModifierPrice) {
			List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
			Set<Integer> averagePricedModifierList = new HashSet<Integer>();
			if (ticketItemModifiers != null) {
				for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
					if (ticketItemModifier.isInfoOnly()) {
						continue;
					}

					if (ticketItemModifier.isShouldSectionWisePrice()) {
						subTotalAmount += ticketItemModifier.getSubTotalAmount();
					}
					else {
						
						/*	if modifier is not selected as section wise modifier
						 *  then average price for modifier will be applied
						 * 
						 * */
						if (!averagePricedModifierList.contains(ticketItemModifier.getModifierId())) {
							subTotalAmount += ticketItemModifier.getSubTotalAmount();
							averagePricedModifierList.add(ticketItemModifier.getModifierId());
						}
					}

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
			//			List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
			//			if (ticketItemModifierGroups != null) {
			//				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
			//					tax += ticketItemModifierGroup.getTax();
			//				}
			//			}
			List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
			if (ticketItemModifiers != null) {
				for (TicketItemModifier modifier : ticketItemModifiers) {
					tax += modifier.getTaxAmount();
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
		String name = getName();
		if (getSizeModifier() != null) {
			name += "\n" + getSizeModifier().getNameDisplay();
		}
		return name;
	}

	@Override
	public Double getUnitPriceDisplay() {
		if (isTreatAsSeat())
			return null;

		return getUnitPrice();
	}

	@Override
	public String getItemQuantityDisplay() {
		if (isTreatAsSeat())
			return ""; //$NON-NLS-1$

		if (isFractionalUnit()) {
			double itemQuantity = getItemQuantity();
			if (itemQuantity % 1 == 0) {
				return String.valueOf((int) itemQuantity) + getItemUnitName();
			}
			itemQuantity = NumberUtil.roundToTwoDigit(itemQuantity);
			return itemQuantity + getItemUnitName();
		}

		return String.valueOf(getItemCount());
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
	public Double getSubTotalAmountDisplay() {
		return getSubtotalAmount();
	}

	@Override
	public Double getSubTotalAmountWithoutModifiersDisplay() {
		if (isTreatAsSeat())
			return null;

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

	public List<Printer> getPrinters(OrderType orderType) {
		PosPrinters printers = PosPrinters.load();
		PrinterGroup printerGroup = getPrinterGroup();

		List<Printer> printerAll = new ArrayList<Printer>();

		if (printerGroup == null) {
			printerAll.add(printers.getDefaultKitchenPrinter());
			return printerAll;
		}

		List<String> printerNames = printerGroup.getPrinterNames();
		List<Printer> kitchenPrinters = printers.getKitchenPrinters();
		for (Printer printer : kitchenPrinters) {
			if (printerNames.contains(printer.getVirtualPrinter().getName())) {
				printerAll.add(printer);
			}
		}
		return printerAll;
	}

	public PrinterGroup getPrinterGroup() {
		if (super.getPrinterGroup() == null) {
			List<PrinterGroup> printerGroups = PrinterGroupDAO.getInstance().findAll();
			for (PrinterGroup printerGroup : printerGroups) {
				if (printerGroup.isIsDefault()) {
					return printerGroup;
				}
			}
		}
		return super.getPrinterGroup();
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

	public MenuItem getMenuItem() {
		if (menuItem == null) {
			menuItem = MenuItemDAO.getInstance().loadInitialized(getItemId());
		}

		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	@Override
	public String getKitchenStatus() {
		if (super.getStatus() == null) {
			return ""; //$NON-NLS-1$
		}
		return super.getStatus();
	}

	public TicketItemModifier findTicketItemModifierFor(MenuModifier menuModifier) {
		List<TicketItemModifier> modifiers = getTicketItemModifiers();
		if (modifiers == null) {
			return null;
		}
		for (TicketItemModifier ticketItemModifier : modifiers) {
			Integer itemId = ticketItemModifier.getModifierId();
			if (itemId != null && itemId.intValue() == menuModifier.getId().intValue()) {
				return ticketItemModifier;
			}
		}
		return null;
	}

	public TicketItemModifier findTicketItemModifierFor(MenuModifier menuModifier, Multiplier multiplier) {
		List<TicketItemModifier> modifiers = getTicketItemModifiers();
		if (modifiers == null) {
			return null;
		}
		for (TicketItemModifier ticketItemModifier : modifiers) {
			Integer itemId = ticketItemModifier.getModifierId();
			if (itemId != null && itemId.intValue() == menuModifier.getId().intValue() && multiplier.getName().equals(ticketItemModifier.getMultiplierName())) {
				return ticketItemModifier;
			}
		}
		return null;
	}

	public TicketItemModifier findTicketItemModifierFor(MenuModifier menuModifier, String sectionName) {
		return findTicketItemModifierFor(menuModifier, sectionName, null);
	}

	public TicketItemModifier findTicketItemModifierFor(MenuModifier menuModifier, String sectionName, Multiplier multiplier) {
		List<TicketItemModifier> modifiers = getTicketItemModifiers();
		if (modifiers == null) {
			return null;
		}
		for (TicketItemModifier ticketItemModifier : modifiers) {
			Integer itemId = ticketItemModifier.getModifierId();
			if (multiplier != null) {
				if ((itemId != null && itemId.intValue() == menuModifier.getId().intValue())
						&& (sectionName != null && sectionName.equals(ticketItemModifier.getSectionName()) && (multiplier != null && multiplier.getName()
								.equals(ticketItemModifier.getMultiplierName())))) {
					return ticketItemModifier;
				}
			}

			if ((itemId != null && itemId.intValue() == menuModifier.getId().intValue())
					&& (sectionName != null && sectionName.equals(ticketItemModifier.getSectionName()))) {
				return ticketItemModifier;
			}
		}
		return null;
	}

	public int countModifierFromGroup(MenuItemModifierGroup menuItemModifierGroup) {
		List<TicketItemModifier> modifiers = getTicketItemModifiers();
		if (modifiers == null) {
			return 0;
		}
		int modifierFromGroupCount = 0;
		for (TicketItemModifier ticketItemModifier : modifiers) {
			Integer groupId = ticketItemModifier.getMenuItemModifierGroupId();
			if (groupId != null && groupId.intValue() == menuItemModifierGroup.getId().intValue()) {
				modifierFromGroupCount += ticketItemModifier.getItemCount();
			}
		}
		return modifierFromGroupCount;
	}

	public boolean requiredModifiersAdded(MenuItemModifierGroup menuItemModifierGroup) {
		int minQuantity = menuItemModifierGroup.getMinQuantity();
		if (minQuantity == 0) {
			return true;
		}
		return countModifierFromGroup(menuItemModifierGroup) >= minQuantity;
	}

	public boolean deleteTicketItemModifier(TicketItemModifier ticketItemModifierToRemove) {
		List<TicketItemModifier> modifiers = getTicketItemModifiers();
		if (modifiers == null) {
			return false;
		}
		for (Iterator iterator = modifiers.iterator(); iterator.hasNext();) {
			TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
			if (ticketItemModifier == ticketItemModifierToRemove) {
				iterator.remove();
				return true;
			}
		}

		return false;
	}

	public boolean deleteTicketItemModifierByName(TicketItemModifier ticketItemModifierToRemove) {
		List<TicketItemModifier> modifiers = getTicketItemModifiers();
		if (modifiers == null) {
			return false;
		}
		for (Iterator iterator = modifiers.iterator(); iterator.hasNext();) {
			TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
			if (ticketItemModifier.getName().equals(ticketItemModifierToRemove.getName())) {
				iterator.remove();
				return true;
			}
		}

		return false;
	}
	
	public java.util.List<com.floreantpos.model.TicketItemDiscount> getDiscounts () {
		if(super.getDiscounts()==null) {
			super.setDiscounts(new ArrayList<TicketItemDiscount>());
		}
		return super.getDiscounts();
}

}