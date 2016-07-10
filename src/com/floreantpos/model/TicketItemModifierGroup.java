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

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.floreantpos.model.base.BaseTicketItemModifierGroup;
import com.floreantpos.model.OrderType;

public class TicketItemModifierGroup extends BaseTicketItemModifierGroup implements ITicketItem {
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public TicketItemModifierGroup () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TicketItemModifierGroup (java.lang.Integer id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */

	public boolean isMergable(TicketItemModifierGroup thatGroup, boolean merge) {
		List<TicketItemModifier> thisModifiers = getTicketItemModifiers();
		List<TicketItemModifier> thatModifiers = thatGroup.getTicketItemModifiers();

		if (thisModifiers.size() != thatModifiers.size()) {
			return false;
		}

		Comparator<TicketItemModifier> comparator = new Comparator<TicketItemModifier>() {
			@Override
			public int compare(TicketItemModifier o1, TicketItemModifier o2) {
				return o1.getItemId() - o2.getItemId();
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

	public int countFreeModifiers() {
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		if (ticketItemModifiers == null)
			return 0;

		int count = 0;
		for (TicketItemModifier modifier : ticketItemModifiers) {
			if (modifier.getModifierType() == TicketItemModifier.NORMAL_MODIFIER) {
				count += modifier.getItemCount();
			}
		}

		return count;
	}

	public int countItems(boolean excludeNoModifier) {
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		if (ticketItemModifiers == null)
			return 0;

		int count = 0;
		for (TicketItemModifier modifier : ticketItemModifiers) {
			//			if (excludeNoModifier) {
			//				if (modifier.getModifierType() != TicketItemModifier.NO_MODIFIER) {
			//					count += modifier.getItemCount();
			//				}
			//			}
			//			else {
			//				if (modifier.getModifierType() == TicketItemModifier.NO_MODIFIER) {
			//					count++;
			//				}
			//				else {
			//					count += modifier.getItemCount();
			//				}
			//			}
			count += modifier.getItemCount();
		}
		return count;
	}

	public TicketItemModifier findTicketItemModifier(MenuModifier modifier) {
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		if (ticketItemModifiers == null) {
			return null;
		}
		else {
			for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
				if (modifier.getId().equals(ticketItemModifier.getItemId())) {
					return ticketItemModifier;
				}
			}
		}

		return null;
	}

	public TicketItemModifier findTicketItemModifier(MenuModifier modifier, boolean addOn) {
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		if (ticketItemModifiers == null) {
			return null;
		}
		else {
			for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
				if (modifier.getId().equals(ticketItemModifier.getItemId())) {
					if (addOn && ticketItemModifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
						return ticketItemModifier;
					}
					else if (!addOn && ticketItemModifier.getModifierType() == TicketItemModifier.NORMAL_MODIFIER) {
						return ticketItemModifier;
					}
				}
			}
		}

		return null;
	}

	public TicketItemModifier addTicketItemModifier(MenuModifier menuModifier, int modifierType, OrderType type) {
		TicketItemModifier ticketItemModifier = new TicketItemModifier();
		ticketItemModifier.setItemId(menuModifier.getId());
		ticketItemModifier.setGroupId(menuModifier.getModifierGroup().getId());
		ticketItemModifier.setItemCount(1);
		ticketItemModifier.setName(menuModifier.getDisplayName());
		//		ticketItemModifier.setExtraUnitPrice(menuModifier.getExtraPrice());
		//ticketItemModifier.setTaxRate(menuModifier.getTax() == null ? 0 : menuModifier.getTax().getRate());
		ticketItemModifier.setUnitPrice(menuModifier.getPriceByOrderType(type));
		ticketItemModifier.setTaxRate(menuModifier.getTaxByOrderType(type));

		ticketItemModifier.setModifierType(modifierType);
		ticketItemModifier.setShouldPrintToKitchen(menuModifier.isShouldPrintToKitchen());
		ticketItemModifier.setParent(this);

		addToticketItemModifiers(ticketItemModifier);

		return ticketItemModifier;
	}

	public TicketItemModifier addTicketItemModifier(MenuModifier menuModifier, boolean addOn) {
		TicketItemModifier ticketItemModifier = new TicketItemModifier();
		ticketItemModifier.setItemId(menuModifier.getId());
		ticketItemModifier.setGroupId(menuModifier.getModifierGroup().getId());
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
		ticketItemModifier.setParent(this);
		ticketItemModifier.setTicketItem(getParent());

		addToticketItemModifiers(ticketItemModifier);

		return ticketItemModifier;
	}

	public TicketItemModifier removeTicketItemModifier(TicketItemModifier ticketItemModifier) {
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		if (ticketItemModifiers == null)
			return ticketItemModifier;

		for (Iterator iter = ticketItemModifiers.iterator(); iter.hasNext();) {
			TicketItemModifier oldTicketItemModifier = (TicketItemModifier) iter.next();
			if (oldTicketItemModifier.getItemId().equals(ticketItemModifier.getItemId())
					&& oldTicketItemModifier.getModifierType() == ticketItemModifier.getModifierType()) {
				iter.remove();
				return oldTicketItemModifier;
			}
		}
		return ticketItemModifier;
	}

	public void calculatePrice() {
		if (getTicketItemModifiers() == null) {
			return;
		}

		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		for (TicketItemModifier modifier : ticketItemModifiers) {
			modifier.calculatePrice();
		}
	}

	public double getSubtotal() {
		if (getTicketItemModifiers() == null) {
			return 0;
		}

		double subtotal = 0;
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		for (TicketItemModifier modifier : ticketItemModifiers) {
			subtotal += modifier.getSubTotalAmount();
		}
		return subtotal;
	}

	public double getTax() {
		double tax = 0;
		if (getTicketItemModifiers() == null) {
			return tax;
		}

		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		for (TicketItemModifier modifier : ticketItemModifiers) {
			tax += modifier.getTaxAmount();
		}

		return tax;
	}

	public double getTotal() {
		return getSubtotal() + getTax();
	}

	public void setPrintedToKitchen(boolean print) {
		List<TicketItemModifier> modifiers = getTicketItemModifiers();

		if (modifiers != null) {
			for (TicketItemModifier modifier : modifiers) {
				if (!modifier.isPrintedToKitchen()) {
					modifier.setPrintedToKitchen(true);
				}
			}
		}
	}

	@Override
	public String getItemCode() {
		return "";
	}

	@Override
	public boolean canAddCookingInstruction() {
		return false;
	}

	@Override
	public boolean canAddDiscount() {
		return false;
	}

	@Override
	public boolean canVoid() {
		return false;
	}

	@Override
	public boolean canAddAdOn() {
		return false;
	}

	@Override
	public Boolean isPrintedToKitchen() {
		return false;
	}

	@Override
	public String getNameDisplay() {
		return " == " + getSectionName();
	}

	@Override
	public Double getUnitPriceDisplay() {
		return null;
	}

	@Override
	public String getItemQuantityDisplay() {
		return "";
	}

	@Override
	public Double getTaxAmountWithoutModifiersDisplay() {
		return 0.0;
	}

	@Override
	public Double getTotalAmountWithoutModifiersDisplay() {
		return 0.0;
	}

	@Override
	public Double getSubTotalAmountWithoutModifiersDisplay() {
		return null;
	}

	@Override
	public void setDiscountAmount(Double amount) {
		
	}

	@Override
	public Double getDiscountAmount() {
		return 0.0;
	}

	@Override
	public String getKitchenStatus() {
		return "";
	}
}