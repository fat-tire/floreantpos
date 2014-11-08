package com.floreantpos.model;

public interface ITicketItem {
	String getItemCode();
	boolean canAddCookingInstruction();
	Boolean isPrintedToKitchen();
	
	String getNameDisplay();

	Double getUnitPriceDisplay();

	Integer getItemCountDisplay();

	Double getTaxAmountWithoutModifiersDisplay();

	Double getTotalAmountWithoutModifiersDisplay();
}
