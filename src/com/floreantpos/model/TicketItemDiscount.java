package com.floreantpos.model;

import com.floreantpos.model.base.BaseTicketItemDiscount;
import com.floreantpos.util.DiscountUtil;

public class TicketItemDiscount extends BaseTicketItemDiscount implements ITicketItem {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public TicketItemDiscount() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TicketItemDiscount(java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/

	public TicketItemDiscount(TicketItemDiscount fromDiscount) {
		setDiscountId(fromDiscount.getDiscountId());
		setName(fromDiscount.getName());
		setType(fromDiscount.getType());
		setAutoApply(fromDiscount.isAutoApply());
		setMinimumQuantity(fromDiscount.getMinimumQuantity());
		setValue(fromDiscount.getValue());
		setAmount(fromDiscount.getAmount());
	}

	private int tableRowNum;

	public double calculateDiscount() {
		Double discountAmount = DiscountUtil.calculateDiscountAmount(this);
		setDiscountAmount(discountAmount);

		return discountAmount;
	}

	public void setTableRowNum(int tableRowNum) {
		this.tableRowNum = tableRowNum;
	}

	public int getTableRowNum() {
		return tableRowNum;
	}

	@Override
	public String getItemCode() {
		return ""; //$NON-NLS-1$
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String getNameDisplay() {
		return "   * " + getName(); //$NON-NLS-1$
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
	public Double getUnitPriceDisplay() {
		return null;
	}

	@Override
	public String getItemQuantityDisplay() {
		return null;
	}

	@Override
	public Double getSubTotalAmountDisplay() {
		return null;
	}

	@Override
	public Double getTaxAmountWithoutModifiersDisplay() {
		return null;
	}

	@Override
	public Double getTotalAmountWithoutModifiersDisplay() {
		return null;
	}

	@Override
	public Double getSubTotalAmountWithoutModifiersDisplay() {
		return DiscountUtil.calculateDiscountAmount(this);
	}

	@Override
	public void setDiscountAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public Double getDiscountAmount() {
		return amount;
	}

	@Override
	public String getKitchenStatus() {
		return ""; //$NON-NLS-1$
	}

}