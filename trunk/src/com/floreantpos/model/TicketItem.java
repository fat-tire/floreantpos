package com.floreantpos.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseTicketItem;
import com.floreantpos.util.NumberUtil;

public class TicketItem extends BaseTicketItem implements ITicketItem {
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

	private boolean priceIncludesTax;

	private int tableRowNum;

	public int getTableRowNum() {
		return tableRowNum;
	}

	public void setTableRowNum(int tableRowNum) {
		this.tableRowNum = tableRowNum;
	}

	public boolean canAddCookingInstruction() {
		if (isPrintedToKitchen())
			return false;

		return true;
	}

	public java.lang.Double getTaxAmount() {
		if(getTicket().isTaxExempt()) {
			return 0.0;
		}
		
		return super.getTaxAmount();
	}

	@Override
	public String toString() {
		return getName();
	}

	public void addCookingInstruction(TicketItemCookingInstruction cookingInstruction) {
		List<TicketItemCookingInstruction> cookingInstructions = getCookingInstructions();

		if (cookingInstructions == null) {
			cookingInstructions = new ArrayList<TicketItemCookingInstruction>(2);
			setCookingInstructions(cookingInstructions);
		}

		cookingInstructions.add(cookingInstruction);
	}

	public void addCookingInstructions(List<TicketItemCookingInstruction> instructions) {
		List<TicketItemCookingInstruction> cookingInstructions = getCookingInstructions();

		if (cookingInstructions == null) {
			cookingInstructions = new ArrayList<TicketItemCookingInstruction>(2);
			setCookingInstructions(cookingInstructions);
		}

		cookingInstructions.addAll(instructions);
	}

	public void removeCookingInstruction(TicketItemCookingInstruction itemCookingInstruction) {
		List<TicketItemCookingInstruction> cookingInstructions2 = getCookingInstructions();
		if (cookingInstructions2 == null) {
			return;
		}

		for (Iterator iterator = cookingInstructions2.iterator(); iterator.hasNext();) {
			TicketItemCookingInstruction ticketItemCookingInstruction = (TicketItemCookingInstruction) iterator.next();
			if (ticketItemCookingInstruction.getTableRowNum() == itemCookingInstruction.getTableRowNum()) {
				iterator.remove();
				return;
			}
		}
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
	
	public TicketItemModifierGroup findTicketItemModifierGroup(Integer menuModifierGroupId) {
		List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
		
		if (ticketItemModifierGroups != null) {
			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				if (ticketItemModifierGroup.getModifierGroupId() == menuModifierGroupId) {
					return ticketItemModifierGroup;
				}
			}
		}
		
		return null;
	}

	public void calculatePrice() {
		priceIncludesTax = Application.getInstance().isPriceIncludesTax();

		List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
		if (ticketItemModifierGroups != null) {
			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				ticketItemModifierGroup.calculatePrice();
			}
		}

		setSubtotalAmount(NumberUtil.roundToTwoDigit(calculateSubtotal(true)));
		setSubtotalAmountWithoutModifiers(NumberUtil.roundToTwoDigit(calculateSubtotal(false)));
		setDiscountAmount(NumberUtil.roundToTwoDigit(calculateDiscount()));
		setTaxAmount(NumberUtil.roundToTwoDigit(calculateTax(true)));
		setTaxAmountWithoutModifiers(NumberUtil.roundToTwoDigit(calculateTax(false)));
		setTotalAmount(NumberUtil.roundToTwoDigit(calculateTotal(true)));
		setTotalAmountWithoutModifiers(NumberUtil.roundToTwoDigit(calculateTotal(false)));
	}

	//	public double calculateSubtotal() {
	//		double subtotal = NumberUtil.roundToTwoDigit(calculateSubtotal(true));
	//		
	//		return subtotal;
	//	}
	//	
	//	public double calculateSubtotalWithoutModifiers() {
	//		double subtotalWithoutModifiers = NumberUtil.roundToTwoDigit(calculateSubtotal(false));
	//		
	//		return subtotalWithoutModifiers;
	//	}

	private double calculateSubtotal(boolean includeModifierPrice) {
		double subTotalAmount = NumberUtil.roundToTwoDigit(getUnitPrice() * getItemCount());

		if (includeModifierPrice) {
			List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					subTotalAmount += ticketItemModifierGroup.getSubtotal();
				}
			}
		}

		return subTotalAmount;
	}

	private double calculateDiscount() {
		double discountRate = getDiscountRate();
		
		if(discountRate < 0) {
			return getDiscountAmount();
		}
		
		double subtotalWithoutModifiers = getSubtotalAmountWithoutModifiers();
		double discount = 0;
		if (discountRate > 0) {
			discount = subtotalWithoutModifiers * discountRate / 100.0;
		}

		return discount;
	}

	private double calculateTax(boolean includeModifierTax) {
		double subtotal = 0;

		subtotal = getSubtotalAmountWithoutModifiers();

		double discount = getDiscountAmount();

		subtotal = subtotal - discount;

		double taxRate = getTaxRate();
		double tax = 0;

		if (taxRate > 0) {
			if (priceIncludesTax) {
				tax = subtotal - (subtotal / (1 + (taxRate / 100.0)));
			}
			else {
				tax = subtotal * (taxRate / 100.0);
			}
		}

		if (includeModifierTax) {
			List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					tax += ticketItemModifierGroup.getTax();
				}
			}
		}

		return tax;
	}

	private double calculateTotal(boolean includeModifiers) {
		double total = 0;

		if (includeModifiers) {
			if (priceIncludesTax) {
				total = getSubtotalAmount() - getDiscountAmount();
			}
			else {
				total = getSubtotalAmount() - getDiscountAmount() + getTaxAmount();
			}
		}
		else {
			if (priceIncludesTax) {
				total = getSubtotalAmountWithoutModifiers() - getDiscountAmount();
			}
			else {
				total = getSubtotalAmountWithoutModifiers() - getDiscountAmount() + getTaxAmountWithoutModifiers();
			}
		}

		return total;
	}

	@Override
	public String getNameDisplay() {
		return getName();
	}

	@Override
	public Double getUnitPriceDisplay() {
		return getUnitPrice();
	}

	@Override
	public Integer getItemCountDisplay() {
		return getItemCount();
	}

	@Override
	public Double getTaxAmountWithoutModifiersDisplay() {
		return getTaxAmountWithoutModifiers();
	}

	@Override
	public Double getTotalAmountWithoutModifiersDisplay() {
		return getTotalAmountWithoutModifiers();
	}
	
	@Override
	public Double getSubTotalAmountWithoutModifiersDisplay() {
		return getSubtotalAmountWithoutModifiers();
	}

	public boolean isPriceIncludesTax() {
		return priceIncludesTax;
	}

	public void setPriceIncludesTax(boolean priceIncludesTax) {
		this.priceIncludesTax = priceIncludesTax;
	}
	
	@Override
	public String getItemCode() {
		return String.valueOf(getItemId());
	}
	
	public Printer getPrinter(OrderType orderType) {
		PosPrinters printers = Application.getPrinters();
		PrinterGroup printerGroup = getPrinterGroup();
		
		if(printerGroup == null) {
			return printers.getDefaultKitchenPrinter();
		}
		
		//return printers.getKitchenPrinterFor(virtualPrinter);
		List<String> printerNames = printerGroup.getPrinterNames();
		List<Printer> kitchenPrinters = printers.getKitchenPrinters();
		for (Printer printer : kitchenPrinters) {
			if(printerNames.contains(printer.getVirtualPrinter().getName())) {
				return printer;
			}
		}
		
		return printers.getDefaultKitchenPrinter();
	}

	@Override
	public boolean canAddDiscount() {
		return true;
	}

	@Override
	public boolean canVoid() {
		return true;
	}

	@Override
	public boolean canAddAdOn() {
		return true;
	}
}