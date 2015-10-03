package com.floreantpos.model;

import java.util.List;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseTicketItemModifier;
import com.floreantpos.util.NumberUtil;

public class TicketItemModifier extends BaseTicketItemModifier implements ITicketItem {
	private static final long	serialVersionUID			= 1L;

	public final static int		MODIFIER_NOT_INITIALIZED	= 0;
	public final static int		NORMAL_MODIFIER				= 1;
	public final static int		NO_MODIFIER					= 2;
	public final static int		EXTRA_MODIFIER				= 3;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public TicketItemModifier() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TicketItemModifier(java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/
	
	private boolean selected;
	
	boolean priceIncludesTax;

	private int	tableRowNum;

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

	public boolean canAddCookingInstruction() {
		return false;
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

	public void calculatePrice() {
		priceIncludesTax = Application.getInstance().isPriceIncludesTax();
		
		calculateSubTotal();
		calculateTax();
		setTotalAmount(NumberUtil.roundToTwoDigit(calculateTotal()));
	}

	private void calculateTax() {
		double tax = getSubTotalAmount() * (getTaxRate() / 100);
		double subtotal = getSubTotalAmount();
		double taxRate = getTaxRate();
		
		if(priceIncludesTax) {
			tax = getSubTotalAmount() * (getTaxRate() / 100);
		}
		else {
			tax = subtotal * (taxRate / 100);
		}
		
		setTaxAmount(NumberUtil.roundToTwoDigit(tax));
	}

	private double calculateTotal() {
		if(priceIncludesTax) {
			return getSubTotalAmount();
		}
		
		return getSubTotalAmount() + getTaxAmount();
	}

	private double calculateSubTotal() {
		double total = 0;

		TicketItemModifierGroup ticketItemModifierGroup = getParent();
		if (ticketItemModifierGroup == null) {
			setSubTotalAmount(total);
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
		total = NumberUtil.roundToTwoDigit(total);

		setSubTotalAmount(total);
		return total;
	}

	@Override
	public String getNameDisplay() {
		String display = getName();

		if (getModifierType() == TicketItemModifier.NO_MODIFIER) {
			display = Messages.getString("TicketItemModifier.0") + display; //$NON-NLS-1$
			return display;
		}

		return " - " + display; //$NON-NLS-1$
	}

	@Override
	public Double getUnitPriceDisplay() {
		if (getModifierType() == TicketItemModifier.NO_MODIFIER) {
			return null;
		}
		if (getModifierType() == TicketItemModifier.NORMAL_MODIFIER) {
			return getUnitPrice();
		}
		if (getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
			return getExtraUnitPrice();
		}

		return null;
	}

	@Override
	public Integer getItemCountDisplay() {
		if (getModifierType() == TicketItemModifier.NO_MODIFIER) {
			return null;
		}
		
		return getItemCount();
	}

	@Override
	public Double getTaxAmountWithoutModifiersDisplay() {
		if (getModifierType() == TicketItemModifier.NO_MODIFIER) {
			return null;
		}

		return getTaxAmount();
	}

	@Override
	public Double getTotalAmountWithoutModifiersDisplay() {
		if (getModifierType() == TicketItemModifier.NO_MODIFIER) {
			return null;
		}

		return getTotalAmount();
	}
	
	@Override
	public Double getSubTotalAmountWithoutModifiersDisplay() {
		if (getModifierType() == TicketItemModifier.NO_MODIFIER) {
			return null;
		}
		return getSubTotalAmount();
	}

	public boolean isPriceIncludesTax() {
		return priceIncludesTax;
	}

	public void setPriceIncludesTax(boolean priceIncludesTax) {
		this.priceIncludesTax = priceIncludesTax;
	}
	
	@Override
	public String getItemCode() {
		return ""; //$NON-NLS-1$
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
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
	public void setDiscountAmount(Double amount) {
	}

	@Override
	public Double getDiscountAmount() {
		return null;
	}
}