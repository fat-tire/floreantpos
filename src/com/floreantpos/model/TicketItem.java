package com.floreantpos.model;

import java.util.List;

import com.floreantpos.model.base.BaseTicketItem;

public class TicketItem extends BaseTicketItem {
	private static final long serialVersionUID = 1L;

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

	private int tableRowNum;

	public int getTableRowNum() {
		return tableRowNum;
	}

	public void setTableRowNum(int tableRowNum) {
		this.tableRowNum = tableRowNum;
	}

	@Override
	public String toString() {
		return getName();
	}

	public TicketItemModifierGroup findTicketItemModifierGroup(MenuModifier menuModifier, boolean createNew) {
		MenuItemModifierGroup menuItemModifierGroup = menuModifier.getMenuItemModifierGroup();

		List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();

		if (ticketItemModifierGroups != null) {
			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				if (ticketItemModifierGroup.getModifierGroupId().equals(menuItemModifierGroup.getId())) {
					return ticketItemModifierGroup;
				}
			}
		}

		TicketItemModifierGroup ticketItemModifierGroup = new TicketItemModifierGroup();
		ticketItemModifierGroup.setModifierGroupId(menuItemModifierGroup.getId());
		ticketItemModifierGroup.setMinQuantity(menuItemModifierGroup.getMinQuantity());
		ticketItemModifierGroup.setMaxQuantity(menuItemModifierGroup.getMaxQuantity());
		ticketItemModifierGroup.setParent(this);
		addToticketItemModifierGroups(ticketItemModifierGroup);

		return ticketItemModifierGroup;
	}

	double calculateSubtotal(boolean includeModifierPrice) {
		double subTotalAmount = getUnitPrice() * getItemCount();
		setSubtotalAmountWithoutModifiers(subTotalAmount);

		if (includeModifierPrice) {
			List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					subTotalAmount += ticketItemModifierGroup.getSubtotal();
				}
			}
			setSubtotalAmount(subTotalAmount);
		}

		return subTotalAmount;
	}

	double calculateDiscount() {
		double subtotal = calculateSubtotal(false);
		double discountRate = getDiscountRate();

		double discount = 0;
		if (discountRate > 0) {
			discount = subtotal * discountRate / 100.0;
		}
		setDiscountAmount(discount);
		return discount;
	}

	double calculateTax(boolean includeModifierTax) {
		double subtotalItemPrice = calculateSubtotal(false) - calculateDiscount();

		double taxRate = getTaxRate();
		double tax = 0;

		if (taxRate > 0) {
			tax = subtotalItemPrice * taxRate / 100.0;
		}
		setTaxAmountWithoutModifiers(tax);

		if (includeModifierTax) {
			List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					tax += ticketItemModifierGroup.getTax();
				}
			}
			setTaxAmount(tax);
		}

		return tax;
	}

	double calculateTotal(boolean includeModifiers) {
		double totalPrice = getUnitPrice() * getItemCount();

		totalPrice -= calculateDiscount();

		double taxRate = getTaxRate();

		double tax = 0;
		if (taxRate > 0) {
			tax = totalPrice * taxRate / 100.0;
		}
		totalPrice += tax;
		setTotalAmountWithoutModifiers(totalPrice);

		if (includeModifiers) {
			List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					totalPrice += ticketItemModifierGroup.getTotal();
				}
			}
			setTotalAmount(totalPrice);
		}

		return totalPrice;
	}
}