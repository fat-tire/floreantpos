package com.floreantpos.model;

import java.util.Iterator;
import java.util.List;

import com.floreantpos.model.base.BaseTicketItemModifierGroup;



public class TicketItemModifierGroup extends BaseTicketItemModifierGroup {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public TicketItemModifierGroup () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TicketItemModifierGroup (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	public int countItems(boolean excludeNoModifier) {
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		if(ticketItemModifiers == null) return 0;
		
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
		ticketItemModifier.setName(menuModifier.getName());
		ticketItemModifier.setUnitPrice(menuModifier.getPrice());
		ticketItemModifier.setExtraUnitPrice(menuModifier.getExtraPrice());
		ticketItemModifier.setTaxRate(menuModifier.getTax() == null ? 0 : menuModifier.getTax().getRate());
		ticketItemModifier.setModifierType(modifierType);
		ticketItemModifier.setShouldPrintToKitchen(menuModifier.isShouldPrintToKitchen());
		ticketItemModifier.setParent(this);
		
		addToticketItemModifiers(ticketItemModifier);
		
		return ticketItemModifier;
	}
	
	public TicketItemModifier removeTicketItemModifier(TicketItemModifier ticketItemModifier) {
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		if(ticketItemModifiers == null) return ticketItemModifier;
		
		for (Iterator iter = ticketItemModifiers.iterator(); iter.hasNext();) {
			TicketItemModifier oldTicketItemModifier = (TicketItemModifier) iter.next();
			if(oldTicketItemModifier.getItemId().equals(ticketItemModifier.getItemId())) {
				iter.remove();
				return oldTicketItemModifier;
			}
		}
		return ticketItemModifier;
	}

	public double getSubtotal() {
		if(getTicketItemModifiers() == null) {
			return 0;
		}
		
		double subtotal = 0;
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		for (TicketItemModifier modifier : ticketItemModifiers) {
			/*switch(modifier.getModifierType()) {
				case TicketItemModifier.NORMAL_MODIFIER:
					subtotal += modifier.getPrice();
					break;
					
				case TicketItemModifier.EXTRA_MODIFIER:
					int extraItemCount = Math.abs(getMaxQuantity() - modifier.getItemCount());
					subtotal += (extraItemCount * modifier.getExtraPrice());
					break;
			}*/
			subtotal += modifier.calculateTotal();
		}
		return subtotal;
	}

	public double getTax() {
		double tax = 0;
		if(getTicketItemModifiers() == null) {
			return tax;
		}
		
		List<TicketItemModifier> ticketItemModifiers = getTicketItemModifiers();
		for (TicketItemModifier modifier : ticketItemModifiers) {
			double taxRate = modifier.getTaxRate();
			double modifierSubtotal = modifier.calculateTotal();
			
			if (taxRate > 0) {
				tax += (modifierSubtotal * taxRate / 100.0);
			}
			/*switch(modifier.getModifierType()) {
				case TicketItemModifier.EXTRA_MODIFIER:
					double taxRate = modifier.getTaxRate();
					int extraItemCount = Math.abs(getMaxQuantity() - modifier.getItemCount());
					double modifierSubtotal = (extraItemCount * modifier.getExtraPrice());
					
					if (taxRate > 0) {
						tax += (modifierSubtotal * taxRate / 100.0);
					}
					break;
			}*/
		}
		return tax;
	}
	
	public double getTotal() {
		return getSubtotal() +  getTax();
	}
}