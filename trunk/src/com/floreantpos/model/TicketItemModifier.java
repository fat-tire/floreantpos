package com.floreantpos.model;

import java.util.List;

import com.floreantpos.model.base.BaseTicketItemModifier;

public class TicketItemModifier extends BaseTicketItemModifier {
	private static final long serialVersionUID = 1L;

	public final static int MODIFIER_NOT_INITIALIZED = 0;
	public final static int NORMAL_MODIFIER = 1;
	public final static int NO_MODIFIER = 2;
	public final static int EXTRA_MODIFIER = 3;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public TicketItemModifier () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TicketItemModifier (java.lang.Integer id) {
		super(id);
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

	private int getPreviousItemsCount() {
		TicketItemModifierGroup ticketItemModifierGroup = getParent();
		List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();

		int count = 0;
		for (TicketItemModifier modifier : ticketItemModifiers) {
			if (modifier == this) {
				return count;
			}
			if (modifier.getModifierType() != TicketItemModifier.NO_MODIFIER) {
				count += modifier.getItemCount();
			}
		}
		return count;
	}

	double calculateTotal() {
		double total = 0;

		TicketItemModifierGroup ticketItemModifierGroup = getParent();
		if (ticketItemModifierGroup == null) {
			setTotalAmount(total);
			return total;
		}

		int previousItemCount = getPreviousItemsCount();
		int maxItemCount = ticketItemModifierGroup.getMaxQuantity();

		int normalItemCount = 0;
		int extraItemCount = 0;

		if (previousItemCount == 0) {
			if (getItemCount() <= maxItemCount) {
				normalItemCount = getItemCount();
				extraItemCount = 0;
			}
			else {
				normalItemCount = maxItemCount;
				extraItemCount = getItemCount() - maxItemCount;
			}
		}

		else {
			maxItemCount = maxItemCount - previousItemCount;
			if (maxItemCount < 0)
				maxItemCount = 0;

			if (getItemCount() <= maxItemCount) {
				normalItemCount = getItemCount();
				extraItemCount = 0;
			}
			else {
				normalItemCount = maxItemCount;
				extraItemCount = getItemCount() - maxItemCount;
			}
		}

		total = normalItemCount * getUnitPrice();
		total += extraItemCount * getExtraUnitPrice();

		setTotalAmount(total);
		return total;
	}
}