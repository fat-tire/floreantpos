package com.floreantpos.model;

public interface ITicketItem {
	String getItemCode();
	
	boolean canAddCookingInstruction();
	boolean canAddDiscount();
	boolean canVoid();
	boolean canAddAdOn();
	
	Boolean isPrintedToKitchen();
	
	String getNameDisplay();

	Double getUnitPriceDisplay();

	Integer getItemCountDisplay();

	Double getTaxAmountWithoutModifiersDisplay();

	Double getTotalAmountWithoutModifiersDisplay();
	
	Double getSubTotalAmountWithoutModifiersDisplay();
	
	void setDiscountAmount(Double amount);
	Double getDiscountAmount();
}
