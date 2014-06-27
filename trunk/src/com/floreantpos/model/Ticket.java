package com.floreantpos.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseTicket;

public class Ticket extends BaseTicket {
	private static final long serialVersionUID = 1L;
	public final static int TAKE_OUT = -1;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public Ticket () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Ticket (java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, h:m a");
	private DecimalFormat numberFormat = new DecimalFormat("0.00");

	private List deletedItems;

	@Override
	public void setCreateDate(Date createDate) {
		super.setCreateDate(createDate);
		super.setActiveDate(createDate);
	}

	public void addCookingInstruction(TicketCookingInstruction instruction) {
		if (getCookingInstructions() == null) {
			setCookingInstructions(new HashSet<TicketCookingInstruction>());
		}

		getCookingInstructions().add(instruction);
	}

	public void removeCookingInstruction(TicketCookingInstruction instruction) {
		if (getCookingInstructions() == null) {
			return;
		}

		getCookingInstructions().remove(instruction);
	}

	@Override
	public List<TicketItem> getTicketItems() {
		List<TicketItem> items = super.getTicketItems();

		if (items == null) {
			items = new ArrayList<TicketItem>();
			super.setTicketItems(items);
		}
		return items;
	}

	@Override
	public Integer getNumberOfGuests() {
		Integer guests = super.getNumberOfGuests();
		if (guests == null || guests.intValue() == 0) {
			return Integer.valueOf(1);
		}
		return guests;
	}

	public Ticket(User owner, Date createTime) {
		setOwner(owner);
		setCreateDate(createTime);
	}

	public String getCreateDateFormatted() {
		return dateFormat.format(getCreateDate());
	}

	public String getTitle() {
		String title = "";
		if (getId() != null) {
			title += "#" + getId();
		}
		title += " Server" + ": " + getOwner();
		title += " Create on" + ":" + getCreateDateFormatted();
		title += " Total" + ": " + numberFormat.format(getTotalAmount());

		return title;
	}

	public int getBeverageCount() {
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null)
			return 0;

		int count = 0;
		for (TicketItem ticketItem : ticketItems) {
			if (ticketItem.isBeverage()) {
				count += ticketItem.getItemCount();
			}
		}
		return count;
	}

	private double calculateSubtotalAmount() {
		double subtotalAmount = 0;

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return subtotalAmount;
		}

		for (TicketItem ticketItem : ticketItems) {
			subtotalAmount += ticketItem.calculateSubtotal(true);
		}
		
		subtotalAmount = fixInvalidAmount(subtotalAmount);

		return subtotalAmount;
	}

	private double calculateDiscountAmount() {
		double subtotalAmount = getSubtotalAmount();
		double discountAmount = 0;

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems != null) {
			for (TicketItem ticketItem : ticketItems) {
				discountAmount += ticketItem.calculateDiscount();
			}
		}

		List<TicketCouponAndDiscount> discounts = getCouponAndDiscounts();
		if (discounts != null) {
			for (TicketCouponAndDiscount discount : discounts) {
				discountAmount += calculateDiscountFromType(discount, subtotalAmount);
			}
		}
		
		discountAmount = fixInvalidAmount(discountAmount);

		return discountAmount;
	}

	private double calculateTax() {
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}

		double tax = 0;
		for (TicketItem ticketItem : ticketItems) {
			tax += ticketItem.calculateTax(true);
		}
		
		double subtotalAfterDiscount = getSubtotalAmount() - getTaxAmount();

		tax = (subtotalAfterDiscount * tax) / getSubtotalAmount(); 
		
		return fixInvalidAmount(tax);
	}

	private double fixInvalidAmount(double tax) {
		if(tax < 0 || Double.isNaN(tax)) {
			tax = 0;
		}
		return tax;
	}

	public void calculatePrice() {
		double subtotalAmount = calculateSubtotalAmount();
		double discountAmount = calculateDiscountAmount();
		
		setSubtotalAmount(subtotalAmount);
		setDiscountAmount(discountAmount);
		
		double taxAmount = calculateTax();
		double serviceChargeAmount = calculateServiceCharge();
		double totalAmount = subtotalAmount - discountAmount + taxAmount + serviceChargeAmount;
		
		totalAmount = fixInvalidAmount(totalAmount);
		

		if (isTaxExempt()) {
			totalAmount = totalAmount - taxAmount;
			taxAmount = 0;
		}
		setTaxAmount(taxAmount);
		setServiceCharge(serviceChargeAmount);
		setTotalAmount(totalAmount);

		double dueAmount = totalAmount - getPaidAmount();
		setDueAmount(dueAmount);
	}

	public double calculateDiscountFromType(TicketCouponAndDiscount coupon, double subtotal) {
		List<TicketItem> ticketItems = getTicketItems();

		double discount = 0;
		int type = coupon.getType();
		double couponValue = coupon.getValue();

		switch (type) {
		case CouponAndDiscount.FIXED_PER_ORDER:
			discount += couponValue;
			break;

		case CouponAndDiscount.FIXED_PER_CATEGORY:
			HashSet<Integer> categoryIds = new HashSet<Integer>();
			for (TicketItem item : ticketItems) {
				Integer itemId = item.getItemId();
				if (!categoryIds.contains(itemId)) {
					discount += couponValue;
					categoryIds.add(itemId);
				}
			}
			break;

		case CouponAndDiscount.FIXED_PER_ITEM:
			for (TicketItem item : ticketItems) {
				discount += (couponValue * item.getItemCount());
			}
			break;

		case CouponAndDiscount.PERCENTAGE_PER_ORDER:
			discount += ((subtotal * couponValue) / 100.0);
			break;

		case CouponAndDiscount.PERCENTAGE_PER_CATEGORY:
			categoryIds = new HashSet<Integer>();
			for (TicketItem item : ticketItems) {
				Integer itemId = item.getItemId();
				if (!categoryIds.contains(itemId)) {
					discount += ((item.getUnitPrice() * couponValue) / 100.0);
					categoryIds.add(itemId);
				}
			}
			break;

		case CouponAndDiscount.PERCENTAGE_PER_ITEM:
			for (TicketItem item : ticketItems) {
				discount += ((item.calculateSubtotal(false) * couponValue) / 100.0);
			}
			break;

		case CouponAndDiscount.FREE_AMOUNT:
			discount += couponValue;
			break;
		}
		return discount;
	}

	public void addDeletedItems(Object o) {
		if (deletedItems == null) {
			deletedItems = new ArrayList();
		}

		deletedItems.add(o);
	}

	public List getDeletedItems() {
		return deletedItems;
	}

	public void clearDeletedItems() {
		if (deletedItems != null) {
			deletedItems.clear();
		}

		deletedItems = null;
	}

	public boolean needsKitchenPrint() {
		if (getDeletedItems() != null && getDeletedItems().size() > 0) {
			return true;
		}

		List<TicketItem> ticketItems = getTicketItems();
		for (TicketItem item : ticketItems) {
			if (item.isShouldPrintToKitchen() && !item.isPrintedToKitchen()) {
				return true;
			}

			List<TicketItemModifierGroup> modifierGroups = item.getTicketItemModifierGroups();
			if (modifierGroups != null) {
				for (TicketItemModifierGroup modifierGroup : modifierGroups) {
					List<TicketItemModifier> ticketItemModifiers = modifierGroup.getTicketItemModifiers();
					if (ticketItemModifiers != null) {
						for (TicketItemModifier modifier : ticketItemModifiers) {
							if (modifier.isShouldPrintToKitchen() && !modifier.isPrintedToKitchen()) {
								return true;
							}
						}
					}
				}
			}
		}

		Set<TicketCookingInstruction> cookingInstructions = getCookingInstructions();
		if (cookingInstructions != null) {
			for (TicketCookingInstruction cookingInstruction : cookingInstructions) {
				if (!cookingInstruction.isPrintedToKitchen()) {
					return true;
				}
			}
		}

		return false;
	}
	
	public double calculateDefaultGratutity() {
		if(getTableNumber() == TAKE_OUT) {
			return 0;
		}
		
		Restaurant restaurant = Application.getInstance().getRestaurant();
		double defaultGratuityPercentage = restaurant.getDefaultGratuityPercentage();
		
		if(defaultGratuityPercentage <= 0) {
			return 0;
		}
		
		Gratuity gratuity = new Gratuity();
		double tip = getDueAmount() * (defaultGratuityPercentage / 100.0);
		gratuity.setAmount(tip);
		gratuity.setOwner(getOwner());
		gratuity.setPaid(false);
		gratuity.setTicket(this);
		gratuity.setTerminal(getTerminal());
		
		setGratuity(gratuity);
		
		return tip;
	}
	
	public double calculateServiceCharge() {
		if(getTableNumber() == TAKE_OUT) {
			return 0;
		}
		
		Restaurant restaurant = Application.getInstance().getRestaurant();
		double serviceChargePercentage = restaurant.getServiceChargePercentage();
		
		double serviceCharge = 0.0;
		
		if(serviceChargePercentage > 0.0) {
			serviceCharge = (getSubtotalAmount() - getDiscountAmount()) * (serviceChargePercentage / 100.0);
		}
		
		return fixInvalidAmount(serviceCharge);
	}
}