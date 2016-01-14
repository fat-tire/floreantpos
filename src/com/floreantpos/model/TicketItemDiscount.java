package com.floreantpos.model;

import com.floreantpos.model.base.BaseTicketItemDiscount;



public class TicketItemDiscount extends BaseTicketItemDiscount implements ITicketItem {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public TicketItemDiscount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TicketItemDiscount (java.lang.Integer id) {
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
	public Integer getItemCountDisplay() {
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
		TicketItem ticketItem2 = getTicketItem();
		
		return (ticketItem2.getSubtotalAmountWithoutModifiers() * getValue()) / 100.0;
	}

	@Override
	public void setDiscountAmount(Double amount) {
		
	}

	@Override
	public Double getDiscountAmount() {
		return null;
	}

}