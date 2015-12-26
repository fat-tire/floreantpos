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

import java.util.Iterator;
import java.util.List;

import com.floreantpos.model.base.BaseTicketItemModifierGroup;

public class TicketItemModifierGroup extends BaseTicketItemModifierGroup {
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

	public int countItems(boolean excludeNoModifier) {
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		if (ticketItemModifiers == null)
			return 0;

		int count = 0;
		for (TicketItemModifier modifier : ticketItemModifiers) {
			if (excludeNoModifier) {
				if (modifier.getModifierType() != TicketItemModifier.NO_MODIFIER) {
					count += modifier.getItemCount();
				}
			}
			else {
				if (modifier.getModifierType() == TicketItemModifier.NO_MODIFIER) {
					count++;
				}
				else {
					count += modifier.getItemCount();
				}
			}
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

	public TicketItemModifier addTicketItemModifier(MenuModifier menuModifier, int modifierType) {
		TicketItemModifier ticketItemModifier = new TicketItemModifier();
		ticketItemModifier.setItemId(menuModifier.getId());
		ticketItemModifier.setGroupId(menuModifier.getModifierGroup().getId());
		ticketItemModifier.setItemCount(1);
		ticketItemModifier.setName(menuModifier.getDisplayName());
		ticketItemModifier.setUnitPrice(menuModifier.getPrice());
		ticketItemModifier.setExtraUnitPrice(menuModifier.getExtraPrice());
		ticketItemModifier.setTaxRate(menuModifier.getTax() == null ? 0 : menuModifier.getTax().getRate());
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
		
		if(addOn) {
			ticketItemModifier.setUnitPrice(menuModifier.getExtraPrice());
			//ticketItemModifier.setExtraUnitPrice(menuModifier.getExtraPrice());
		}
		ticketItemModifier.setTaxRate(menuModifier.getTax() == null ? 0 : menuModifier.getTax().getRate());
		//ticketItemModifier.setModifierType(modifierType);
		ticketItemModifier.setShouldPrintToKitchen(menuModifier.isShouldPrintToKitchen());
		ticketItemModifier.setParent(this);

		addToticketItemModifiers(ticketItemModifier);

		return ticketItemModifier;
	}

	public TicketItemModifier removeTicketItemModifier(TicketItemModifier ticketItemModifier) {
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		if (ticketItemModifiers == null)
			return ticketItemModifier;

		for (Iterator iter = ticketItemModifiers.iterator(); iter.hasNext();) {
			TicketItemModifier oldTicketItemModifier = (TicketItemModifier) iter.next();
			if (oldTicketItemModifier.getItemId().equals(ticketItemModifier.getItemId())) {
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
}