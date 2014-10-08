package com.floreantpos.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseTicket;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

public class Ticket extends BaseTicket {
	private static final long serialVersionUID = 1L;
	// public final static int TAKE_OUT = -1;

//	public final static String DINE_IN = "DINE IN";
//	public final static String TAKE_OUT = "TAKE OUT";
//	public final static String PICKUP = "PICKUP";
//	public final static String HOME_DELIVERY = "HOME DELIVERY";
//	public final static String DRIVE_THROUGH = "DRIVE THRU";
//	public final static String BAR_TAB = "BAR_TAB";
	
	public final static String PROPERTY_CARD_TRANSACTION_ID = "card_transaction_id";
	public final static String PROPERTY_CARD_TRACKS = "card_tracks";
	public static final String PROPERTY_CARD_NAME = "card_name";
	public static final String PROPERTY_PAYMENT_METHOD = "payment_method";
	public static final String PROPERTY_CARD_READER = "card_reader";
	public static final String PROPERTY_CARD_NUMBER = "card_number";
	public static final String PROPERTY_CARD_EXP_YEAR = "card_exp_year";
	public static final String PROPERTY_CARD_EXP_MONTH = "card_exp_month";
	public static final String PROPERTY_ADVANCE_PAYMENT = "advance_payment";
	public static final String PROPERTY_CARD_AUTH_CODE = "card_auth_code";
	
	public static final double BAR_TAB_ADVANCE = 25;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Ticket () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Ticket (java.lang.Integer id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, h:m a");
	private DecimalFormat numberFormat = new DecimalFormat("0.00");

	private List deletedItems;

	private boolean priceIncludesTax;

	@Override
	public java.lang.Integer getCreationHour() {
		Integer creationHour = super.getCreationHour();

		return creationHour == null ? Integer.valueOf(0) : creationHour;
	}

	@Override
	public java.lang.Boolean isPaid() {
		Boolean boolean1 = super.isPaid();

		return boolean1 == null ? Boolean.FALSE : boolean1;
	}

	@Override
	public java.lang.Boolean isVoided() {
		Boolean voided = super.isVoided();

		return voided == null ? Boolean.FALSE : voided;
	}

	@Override
	public java.lang.Boolean isWasted() {
		Boolean wasted = super.isWasted();

		return wasted == null ? Boolean.FALSE : wasted;
	}

	@Override
	public java.lang.Boolean isClosed() {
		Boolean closed = super.isClosed();

		return closed == null ? Boolean.FALSE : closed;
	}

	@Override
	public java.lang.Boolean isDrawerResetted() {
		Boolean drawerResetted = super.isDrawerResetted();

		return drawerResetted == null ? Boolean.FALSE : drawerResetted;
	}

	@Override
	public java.lang.Double getSubtotalAmount() {
		Double subtotalAmount = super.getSubtotalAmount();

		return subtotalAmount == null ? Double.valueOf(0) : subtotalAmount;
	}

	@Override
	public java.lang.Integer getTableNumber() {
		Integer tableNumber = super.getTableNumber();
		return tableNumber == null ? Integer.valueOf(0) : tableNumber;
	}

	@Override
	public java.lang.Boolean isTaxExempt() {
		Boolean taxExempt = super.isTaxExempt();
		return taxExempt == null ? Boolean.FALSE : taxExempt;
	}

	@Override
	public java.lang.Boolean isReOpened() {
		Boolean reOpened = super.isReOpened();
		return reOpened == null ? Boolean.FALSE : reOpened;
	}
	
	@Override
	public java.lang.Double getServiceCharge() {
		Double serviceCharge = super.getServiceCharge();
		return serviceCharge == null ? Double.valueOf(0) : serviceCharge;
	}
	
	@Override
	public java.lang.Double getDeliveryCharge() {
		Double deliveryCharge = super.getDeliveryCharge();
		return deliveryCharge == null ? Double.valueOf(0) : deliveryCharge;
	}
	
	@Override
	public java.lang.Boolean isCustomerWillPickup() {
		Boolean customerWillPickup = super.isCustomerWillPickup();
		return customerWillPickup == null ? Boolean.FALSE : customerWillPickup;
	}

	@Override
	public void setCreateDate(Date createDate) {
		super.setCreateDate(createDate);
		super.setActiveDate(createDate);
	}

	@Override
	public Date getDeliveryDate() {
		Date deliveryDate = super.getDeliveryDate();

		if (deliveryDate == null) {
			deliveryDate = getCreateDate();
			Calendar c = Calendar.getInstance();
			c.setTime(deliveryDate);
			c.add(Calendar.MINUTE, 10);
			deliveryDate = c.getTime();
		}

		return deliveryDate;
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
	
	public void calculatePrice() {
		priceIncludesTax = Application.getInstance().isPriceIncludesTax();

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return;
		}

		for (TicketItem ticketItem : ticketItems) {
			ticketItem.calculatePrice();
		}

		double subtotalAmount = calculateSubtotalAmount();
		double discountAmount = calculateDiscountAmount();

		setSubtotalAmount(subtotalAmount);
		setDiscountAmount(discountAmount);

		double taxAmount = calculateTax();
		setTaxAmount(taxAmount);

		double serviceChargeAmount = calculateServiceCharge();
		double totalAmount = 0;

		if (priceIncludesTax) {
			totalAmount = subtotalAmount - discountAmount + serviceChargeAmount;
		}
		else {
			totalAmount = subtotalAmount - discountAmount + taxAmount + serviceChargeAmount;
		}

		if (getGratuity() != null) {
			totalAmount += getGratuity().getAmount();
		}

		totalAmount = fixInvalidAmount(totalAmount);

		setServiceCharge(serviceChargeAmount);
		setTotalAmount(NumberUtil.roundToTwoDigit(totalAmount));

		double dueAmount = totalAmount - getPaidAmount();
		setDueAmount(NumberUtil.roundToTwoDigit(dueAmount));
	}

	private double calculateSubtotalAmount() {
		double subtotalAmount = 0;

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return subtotalAmount;
		}

		for (TicketItem ticketItem : ticketItems) {
			subtotalAmount += ticketItem.getSubtotalAmount();
		}

		subtotalAmount = fixInvalidAmount(subtotalAmount);

		return NumberUtil.roundToTwoDigit(subtotalAmount);
	}

	private double calculateDiscountAmount() {
		double subtotalAmount = getSubtotalAmount();
		double discountAmount = 0;

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems != null) {
			for (TicketItem ticketItem : ticketItems) {
				discountAmount += ticketItem.getDiscountAmount();
			}
		}

		List<TicketCouponAndDiscount> discounts = getCouponAndDiscounts();
		if (discounts != null) {
			for (TicketCouponAndDiscount discount : discounts) {
				discountAmount += calculateDiscountFromType(discount, subtotalAmount);
			}
		}

		discountAmount = fixInvalidAmount(discountAmount);

		return NumberUtil.roundToTwoDigit(discountAmount);
	}

	private double calculateTax() {
		if (isTaxExempt()) {
			return 0;
		}

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return 0;
		}

		double tax = 0;
		for (TicketItem ticketItem : ticketItems) {
			tax += ticketItem.getTaxAmount();
		}

		return NumberUtil.roundToTwoDigit(fixInvalidAmount(tax));
	}

	private double fixInvalidAmount(double tax) {
		if (tax < 0 || Double.isNaN(tax)) {
			tax = 0;
		}
		return tax;
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
					discount += ((item.getSubtotalAmountWithoutModifiers() * couponValue) / 100.0);
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

			List<TicketItemCookingInstruction> cookingInstructions = item.getCookingInstructions();
			if (cookingInstructions != null) {
				for (TicketItemCookingInstruction ticketItemCookingInstruction : cookingInstructions) {
					if (!ticketItemCookingInstruction.isPrintedToKitchen()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	//	public double calculateDefaultGratutity() {
	//		if (!DINE_IN.equals(getTicketType())) {
	//			return 0;
	//		}
	//
	//		Restaurant restaurant = Application.getInstance().getRestaurant();
	//		double defaultGratuityPercentage = restaurant.getDefaultGratuityPercentage();
	//
	//		if (defaultGratuityPercentage <= 0) {
	//			return 0;
	//		}
	//
	//		Gratuity gratuity = new Gratuity();
	//		double tip = getDueAmount() * (defaultGratuityPercentage / 100.0);
	//		gratuity.setAmount(tip);
	//		gratuity.setOwner(getOwner());
	//		gratuity.setPaid(false);
	//		gratuity.setTicket(this);
	//		gratuity.setTerminal(getTerminal());
	//
	//		setGratuity(gratuity);
	//
	//		return tip;
	//	}

	public double calculateServiceCharge() {
		if (getType() != TicketType.DINE_IN) {
			return 0;
		}

		Restaurant restaurant = Application.getInstance().getRestaurant();
		double serviceChargePercentage = restaurant.getServiceChargePercentage();

		double serviceCharge = 0.0;

		if (serviceChargePercentage > 0.0) {
			serviceCharge = (getSubtotalAmount() - getDiscountAmount()) * (serviceChargePercentage / 100.0);
		}

		return NumberUtil.roundToTwoDigit(fixInvalidAmount(serviceCharge));
	}

	public TicketType getType() {
		String type = getTicketType();
		
		if(StringUtils.isEmpty(type)) {
			return TicketType.DINE_IN;
		}
		
		return TicketType.valueOf(type);
	}
	
	public void setType(TicketType type) {
		setTicketType(type.name());
	}

	public boolean isPriceIncludesTax() {
		return priceIncludesTax;
	}

	public void setPriceIncludesTax(boolean priceIncludesTax) {
		this.priceIncludesTax = priceIncludesTax;
	}

	public void addProperty(String name, String value) {
		if (getProperties() == null) {
			setProperties(new HashMap<String, String>());
		}

		getProperties().put(name, value);
	}

	public boolean hasProperty(String key) {
		return getProperty(key) != null;
	}

	public String getProperty(String key) {
		if (getProperties() == null) {
			return null;
		}

		return getProperties().get(key);
	}
	
	public boolean isPropertyValueTrue(String propertyName) {
		String property = getProperty(propertyName);
		
		return POSUtil.getBoolean(property);
	}
	
	public String toURLForm() {
		String s = "ticket_id=" + getId();
		
		List<TicketItem> items = getTicketItems();
		if(items == null || items.size() == 0) {
			return s;
		}
		
		for (int i = 0; i < items.size(); i++) {
			TicketItem ticketItem = items.get(i);
			s += "&items[" + i + "][id]=" + ticketItem.getId();
			s += "&items[" + i + "][name]=" + POSUtil.encodeURLString(ticketItem.getName());
			s += "&items[" + i + "][price]=" + ticketItem.getSubtotalAmount();
		}
		
		s+= "&tax=" + getTaxAmount();
		s+= "&subtotal=" + getSubtotalAmount();
		s+= "&grandtotal=" + getTotalAmount();
		
		return s;
	}
}