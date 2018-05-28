/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.actions.NewBarTabAction;
import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseTicket;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.OrderTypeDAO;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.util.DiscountUtil;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

@XmlRootElement(name = "ticket")
public class Ticket extends BaseTicket {

	private static final long serialVersionUID = 1L;
	// public final static int TAKE_OUT = -1;

	//	public final static String DINE_IN = "DINE IN";
	//	public final static String TAKE_OUT = "TAKE OUT";
	//	public final static String PICKUP = "PICKUP";
	//	public final static String HOME_DELIVERY = "HOME DELIVERY";
	//	public final static String DRIVE_THROUGH = "DRIVE THRU";
	//	public final static String BAR_TAB = "BAR_TAB";

	public final static String PROPERTY_CARD_TRANSACTION_ID = "card_transaction_id"; //$NON-NLS-1$
	public final static String PROPERTY_CARD_TRACKS = "card_tracks"; //$NON-NLS-1$
	public static final String PROPERTY_CARD_NAME = "card_name"; //$NON-NLS-1$
	public static final String PROPERTY_PAYMENT_METHOD = "payment_method"; //$NON-NLS-1$
	public static final String PROPERTY_CARD_READER = "card_reader"; //$NON-NLS-1$
	public static final String PROPERTY_CARD_NUMBER = "card_number"; //$NON-NLS-1$
	public static final String PROPERTY_CARD_EXP_YEAR = "card_exp_year"; //$NON-NLS-1$
	public static final String PROPERTY_CARD_EXP_MONTH = "card_exp_month"; //$NON-NLS-1$
	public static final String PROPERTY_ADVANCE_PAYMENT = "advance_payment"; //$NON-NLS-1$
	public static final String PROPERTY_CARD_AUTH_CODE = "card_auth_code"; //$NON-NLS-1$

	private OrderType orderType;

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

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, h:m a"); //$NON-NLS-1$

	private List deletedItems;
	private boolean priceIncludesTax;

	public static final String STATUS_WAITING = "Waiting (Kitchen)"; //$NON-NLS-1$
	public static final String STATUS_READY = "Ready"; //$NON-NLS-1$
	public static final String STATUS_NOT_SENT = "Not Sent"; //$NON-NLS-1$
	public static final String STATUS_DRIVING = "Driving"; //$NON-NLS-1$
	public static final String STATUS_VOID = "Void"; //$NON-NLS-1$

	public static final String CUSTOMER_MOBILE = "CUSTOMER_MOBILE"; //$NON-NLS-1$
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME"; //$NON-NLS-1$
	public static final String CUSTOMER_ID = "CUSTOMER_ID"; //$NON-NLS-1$
	public static final String CUSTOMER_ZIP_CODE = "CUSTOMER_ZIP_CODE"; //$NON-NLS-1$
	public static final String MANAGER_INSTRUCTION = "MANAGER_INSTRUCTION"; //$NON-NLS-1$
	public static final String PHONE_EXTENSION = "PHONE_EXTENSION";

	public static final String DRIVER_OUT_TIME = "OUT_AT"; //$NON-NLS-1$

	public static final String SPLIT = "split";
	public static final String SPLIT_NUMBER = "split_number";
	public static final String ORIGINAL_SPLIT_TICKET_ID = "original_split_ticket_id";

	private String sortOrder;
	private Customer customer;

	//	public String getTableNumbers() {
	//		Set<ShopTable> tables = getTables();
	//		if(tables == null) return "";
	//		
	//		String s = "";
	//		for (Iterator iterator = tables.iterator(); iterator.hasNext();) {
	//			ShopTable shopTable = (ShopTable) iterator.next();
	//			s += shopTable.getTableNumber();
	//			
	//			if(iterator.hasNext()) {
	//				s += ", ";
	//			}
	//		}
	//		
	//		return s;
	//	}

	public void addTable(int tableNumber) {
		List<Integer> numbers = getTableNumbers();
		if (numbers == null) {
			numbers = new ArrayList<Integer>();
			setTableNumbers(numbers);
		}

		numbers.add(tableNumber);
	}

	@Override
	public void setClosed(Boolean closed) {
		super.setClosed(closed);

		if (closed) {
			ShopTableDAO.getInstance().releaseTables(this);
		}
	}

	public void setGratuityAmount(double amount) {
		Gratuity gratuity = getGratuity();
		if (gratuity == null) {
			gratuity = createGratuity();
			setGratuity(gratuity);
		}

		gratuity.setAmount(amount);
	}

	public double getGratuityAmount() {
		Gratuity gratuity = getGratuity();
		if (gratuity != null) {
			return gratuity.getAmount();
		}
		return 0;
	}

	public Gratuity createGratuity() {
		Gratuity gratuity;
		gratuity = new Gratuity();
		gratuity.setTicket(this);
		gratuity.setTerminal(Application.getInstance().getTerminal());
		gratuity.setOwner(getOwner());
		gratuity.setPaid(false);
		return gratuity;
	}

	public boolean hasGratuity() {
		return getGratuity() != null;
	}

	@Override
	public void setCreateDate(Date createDate) {
		super.setCreateDate(createDate);
		super.setActiveDate(createDate);
	}

	@Override
	public Date getDeliveryDate() {
		Date deliveryDate = super.getDeliveryDate();

		/*if (deliveryDate == null) {
			deliveryDate = getCreateDate();
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE, 10);
			deliveryDate = c.getTime();
		}*/

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
		String title = ""; //$NON-NLS-1$
		if (getId() != null) {
			title += "#" + getId(); //$NON-NLS-1$
		}
		title += Messages.getString("Ticket.1") + ": " + getOwner(); //$NON-NLS-1$ //$NON-NLS-2$
		title += Messages.getString("Ticket.18") + ":" + getCreateDateFormatted(); //$NON-NLS-1$ //$NON-NLS-2$
		title += Messages.getString("Ticket.20") + ": " + NumberUtil.formatNumber(getTotalAmount()); //$NON-NLS-1$ //$NON-NLS-2$

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
		double discountAmount = calculateItemsDiscountAmount();
		double toleranceAmount = calculateToleranceAmount();
		double ticketDiscountAmount = calculateTicketDiscountAmount(discountAmount);
		if (ticketDiscountAmount > 0) {
			discountAmount = ticketDiscountAmount;
		}

		setSubtotalAmount(subtotalAmount);
		discountAmount += toleranceAmount;

		double taxAmount = calculateTax();
		if (ticketDiscountAmount > 0) {
			double discountTax = taxAmount * ticketDiscountAmount / subtotalAmount;
			taxAmount = taxAmount - discountTax;
		}
		setDiscountAmount(discountAmount);
		setTaxAmount(taxAmount);

		Double deliveryChargeAmount = NumberUtil.roundToTwoDigit(getDeliveryCharge());

		double serviceChargeAmount = calculateServiceCharge();
		double totalAmount = 0;

		if (priceIncludesTax) {
			totalAmount = subtotalAmount - discountAmount + deliveryChargeAmount + serviceChargeAmount;
		}
		else {
			totalAmount = subtotalAmount - discountAmount + deliveryChargeAmount + taxAmount + serviceChargeAmount;
		}

		if (getGratuity() != null) {
			totalAmount += getGratuity().getAmount();
		}
		
		totalAmount += getAdjustmentAmount();
		totalAmount = fixInvalidAmount(totalAmount);

		setServiceCharge(serviceChargeAmount);
		setTotalAmount(NumberUtil.roundToTwoDigit(totalAmount));

		double dueAmount = totalAmount - getPaidAmount();
		setDueAmount(NumberUtil.roundToTwoDigit(dueAmount));
	}

	public void updateTicketItemPriceByOrderType() {
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return;
		}

		for (TicketItem ticketItem : ticketItems) {
			Integer itemId = Integer.parseInt(ticketItem.getItemId().toString());
			MenuItem menuItem = MenuItemDAO.getInstance().initialize(MenuItemDAO.getInstance().get(itemId));
			if (menuItem != null) {
				ticketItem.setUnitPrice(menuItem.getPriceByOrderType(getOrderType()));
				ticketItem.setTaxRate(menuItem.calculateTaxRate());
			}
		}
	}

	public void updateTicketItemPriceByOrderType(String name) {
		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems == null) {
			return;
		}

		for (TicketItem ticketItem : ticketItems) {
			Integer itemId = Integer.parseInt(ticketItem.getItemId().toString());
			MenuItem menuItem = MenuItemDAO.getInstance().initialize(MenuItemDAO.getInstance().get(itemId));
			if (menuItem != null) {
				ticketItem.setUnitPrice(menuItem.getPriceByOrderType(name));
				ticketItem.setTaxRate(menuItem.calculateTaxRate());
			}
		}
	}

	//	public TicketCouponAndDiscount getLargestDiscoutn() {
	//		List<TicketCouponAndDiscount> couponAndDiscounts = getCouponAndDiscounts();
	//	}

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

	private double calculateItemsDiscountAmount() {
		double ticketItemDiscounts = 0;

		List<TicketItem> ticketItems = getTicketItems();
		if (ticketItems != null) {
			for (TicketItem ticketItem : ticketItems) {
				ticketItemDiscounts += ticketItem.getDiscountAmount();
			}
		}
		ticketItemDiscounts = fixInvalidAmount(ticketItemDiscounts);
		return NumberUtil.roundToTwoDigit(ticketItemDiscounts);
	}

	public double calculateToleranceAmount() {
		double discount = 0;
		TicketDiscount tolerance = null;
		if (getDiscounts() != null) {
			for (TicketDiscount tDiscount : getDiscounts()) {
				if (tDiscount.getName().equals("Tolerance")) { //$NON-NLS-1$
					tolerance = tDiscount;
					continue;
				}
			}
		}
		if (tolerance != null) {
			discount += tolerance.getValue();
		}
		discount = fixInvalidAmount(discount);
		return NumberUtil.roundToTwoDigit(discount);
	}

	private double calculateTicketDiscountAmount(double itemsDiscount) {
		double discount = 0;
		List<TicketDiscount> discounts = new ArrayList<>();
		if (getDiscounts() != null) {
			for (TicketDiscount tDiscount : getDiscounts()) {
				if (tDiscount.getName().equals("Tolerance")) { //$NON-NLS-1$
					continue;
				}
				discounts.add(tDiscount);
			}
		}
		TicketDiscount ticketCouponAndDiscount = DiscountUtil.getMaxDiscount(discounts, itemsDiscount);
		if (ticketCouponAndDiscount != null) {
			discount = DiscountUtil.calculateDiscountAmount(getSubtotalAmount() - discount, ticketCouponAndDiscount);
		}
		discount = fixInvalidAmount(discount);
		return NumberUtil.roundToTwoDigit(discount);
	}

	public Double getDeliveryCharge() {
		Double deliveryCharge = super.getDeliveryCharge();
		if (deliveryCharge == null) {
			return 0.0;
		}
		return deliveryCharge;
	}

	public double getAmountByType(TicketDiscount discount) {

		switch (discount.getType()) {
			case Discount.DISCOUNT_TYPE_AMOUNT:
				return discount.getValue();

			case Discount.DISCOUNT_TYPE_PERCENTAGE:
				return (discount.getValue() * getSubtotalAmount()) / 100;

			default:
				break;
		}

		return 0;

	}

	public static TicketDiscount convertToTicketDiscount(Discount discount, Ticket ticket) {
		TicketDiscount ticketDiscount = new TicketDiscount();
		ticketDiscount.setDiscountId(discount.getId());
		ticketDiscount.setName(discount.getName());
		ticketDiscount.setType(discount.getType());
		ticketDiscount.setMinimumAmount(discount.getMinimunBuy());
		ticketDiscount.setValue(discount.getValue());
		ticketDiscount.setTicket(ticket);
		return ticketDiscount;
	}

	private double calculateTax() {
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

	public double calculateDiscountFromType(TicketDiscount coupon, double subtotal) {
		List<TicketItem> ticketItems = getTicketItems();

		double discount = 0;
		int type = coupon.getType();
		double couponValue = coupon.getValue();

		switch (type) {
			case Discount.FIXED_PER_ORDER:
				discount += couponValue;
				break;

			case Discount.FIXED_PER_CATEGORY:
				HashSet<Integer> categoryIds = new HashSet<Integer>();
				for (TicketItem item : ticketItems) {
					Integer itemId = item.getItemId();
					if (!categoryIds.contains(itemId)) {
						discount += couponValue;
						categoryIds.add(itemId);
					}
				}
				break;

			case Discount.FIXED_PER_ITEM:
				for (TicketItem item : ticketItems) {
					discount += (couponValue * item.getItemCount());
				}
				break;

			case Discount.PERCENTAGE_PER_ORDER:
				discount += ((subtotal * couponValue) / 100.0);
				break;

			case Discount.PERCENTAGE_PER_CATEGORY:
				categoryIds = new HashSet<Integer>();
				for (TicketItem item : ticketItems) {
					Integer itemId = item.getItemId();
					if (!categoryIds.contains(itemId)) {
						discount += ((item.getUnitPrice() * couponValue) / 100.0);
						categoryIds.add(itemId);
					}
				}
				break;

			case Discount.PERCENTAGE_PER_ITEM:
				for (TicketItem item : ticketItems) {
					discount += ((item.getSubtotalAmountWithoutModifiers() * couponValue) / 100.0);
				}
				break;

			case Discount.FREE_AMOUNT:
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

	public int countItem(TicketItem ticketItem) {
		List<TicketItem> items = getTicketItems();
		if (items == null) {
			return 0;
		}

		int count = 0;
		for (TicketItem ticketItem2 : items) {
			if (ticketItem.getItemId().equals(ticketItem2.getItemId())) {
				++count;
			}
		}

		return count;
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

			List<TicketItemModifier> ticketItemModifiers = item.getTicketItemModifiers();
			if (ticketItemModifiers != null) {
				for (TicketItemModifier modifier : ticketItemModifiers) {
					if (modifier.isShouldPrintToKitchen() && !modifier.isPrintedToKitchen()) {
						return true;
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
		/*if (getType() != OrderType.DINE_IN) {
			return 0;
		}*/

		Restaurant restaurant = Application.getInstance().getRestaurant();
		double serviceChargePercentage = restaurant.getServiceChargePercentage();

		double serviceCharge = 0.0;

		if (serviceChargePercentage > 0.0) {
			serviceCharge = (getSubtotalAmount() - getDiscountAmount()) * (serviceChargePercentage / 100.0);
		}

		return NumberUtil.roundToTwoDigit(fixInvalidAmount(serviceCharge));
	}

	public OrderType getOrderType() {

		/*if (StringUtils.isEmpty(type)) {
			return OrderType.DINE_IN;
		}*/

		if (orderType == null) {
			String type = getTicketType();
			orderType = OrderTypeDAO.getInstance().findByName(type);
		}
		return orderType;
	}

	public void setOrderType(OrderType type) {
		orderType = type;
		setTicketType(type.getName());
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

	public String getProperty(String key, String defaultValue) {
		if (getProperties() == null) {
			return null;
		}

		String string = getProperties().get(key);
		if (StringUtils.isEmpty(string)) {
			return defaultValue;
		}

		return string;
	}

	public void removeProperty(String propertyName) {
		Map<String, String> properties = getProperties();
		if (properties == null) {
			return;
		}

		properties.remove(propertyName);
	}

	public boolean isPropertyValueTrue(String propertyName) {
		String property = getProperty(propertyName);

		return POSUtil.getBoolean(property);
	}

	public String toURLForm() {
		String s = "ticket_id=" + getId(); //$NON-NLS-1$

		List<TicketItem> items = getTicketItems();
		if (items == null || items.size() == 0) {
			return s;
		}

		for (int i = 0; i < items.size(); i++) {
			TicketItem ticketItem = items.get(i);
			s += "&items[" + i + "][id]=" + ticketItem.getId(); //$NON-NLS-1$ //$NON-NLS-2$
			s += "&items[" + i + "][name]=" + POSUtil.encodeURLString(ticketItem.getName()); //$NON-NLS-1$ //$NON-NLS-2$
			s += "&items[" + i + "][price]=" + ticketItem.getSubtotalAmount(); //$NON-NLS-1$ //$NON-NLS-2$
		}

		s += "&tax=" + getTaxAmount(); //$NON-NLS-1$
		s += "&subtotal=" + getSubtotalAmount(); //$NON-NLS-1$
		s += "&grandtotal=" + getTotalAmount(); //$NON-NLS-1$

		return s;
	}

	public void setCustomer(Customer customer) {
		if (customer != null) {
			addProperty(Ticket.CUSTOMER_ID, String.valueOf(customer.getAutoId()));
			addProperty(Ticket.CUSTOMER_NAME, customer.getFirstName());
			addProperty(Ticket.CUSTOMER_MOBILE, customer.getMobileNo());
			addProperty(Ticket.CUSTOMER_ZIP_CODE, customer.getZipCode());
		}
		if (customer != null) {
			setCustomerId(customer.getAutoId());
		}
	}

	public void removeCustomer() {
		removeProperty(CUSTOMER_ID);
		removeProperty(CUSTOMER_NAME);
		removeProperty(CUSTOMER_MOBILE);
		removeProperty(CUSTOMER_ZIP_CODE);
	}

	public String getSortOrder() {
		if (sortOrder == null) {
			return ""; //$NON-NLS-1$
		}
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getStatus() {
		if (super.getStatus() == null) {
			return ""; //$NON-NLS-1$
		}
		return super.getStatus();
	}

	public void consolidateTicketItems() {
		List<TicketItem> ticketItems = getTicketItems();

		Map<String, List<TicketItem>> itemMap = new LinkedHashMap<String, List<TicketItem>>();

		for (Iterator iterator = ticketItems.iterator(); iterator.hasNext();) {
			TicketItem newItem = (TicketItem) iterator.next();
			if (newItem.isPrintedToKitchen())
				continue;
			List<TicketItem> itemListInMap = itemMap.get(newItem.getItemId().toString());

			if (itemListInMap == null) {
				List<TicketItem> list = new ArrayList<TicketItem>();
				list.add(newItem);

				itemMap.put(newItem.getItemId().toString(), list);
			}
			else {
				boolean merged = false;
				for (TicketItem itemInMap : itemListInMap) {
					if (itemInMap.isMergable(newItem, false)) {
						itemInMap.merge(newItem);
						merged = true;
						break;
					}
				}

				if (!merged) {
					itemListInMap.add(newItem);
				}
			}
		}

		getTicketItems().clear();
		Collection<List<TicketItem>> values = itemMap.values();
		for (List<TicketItem> list : values) {
			if (list != null) {
				getTicketItems().addAll(list);
			}
		}
		List<TicketItem> ticketItemList = getTicketItems();
		if (getOrderType().isAllowSeatBasedOrder()) {
			Collections.sort(ticketItemList, new Comparator<TicketItem>() {

				@Override
				public int compare(TicketItem o1, TicketItem o2) {
					return o1.getId() - o2.getId();
				}
			});
			Collections.sort(ticketItemList, new Comparator<TicketItem>() {

				@Override
				public int compare(TicketItem o1, TicketItem o2) {
					return o1.getSeatNumber() - o2.getSeatNumber();
				}

			});
		}
		calculatePrice();
	}

	/**
	 * Mark ticket items, modifiers, add-ons as printed to kitchen
	 */
	public void markPrintedToKitchen() {
		List<TicketItem> ticketItems = getTicketItems();
		for (TicketItem ticketItem : ticketItems) {
			if (ticketItem.isPrintedToKitchen() || !ticketItem.isShouldPrintToKitchen()) {
				if (!ticketItem.isHasModifiers())
					continue;
			}

			List<Printer> printers = ticketItem.getPrinters(getOrderType());
			if (printers == null) {
				continue;
			}
			ticketItem.setPrintedToKitchen(true);
			List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
			if (ticketItemModifiers != null) {
				for (TicketItemModifier itemModifier : ticketItemModifiers) {
					itemModifier.setPrintedToKitchen(true);
				}
			}

			List<TicketItemModifier> addOns = ticketItem.getAddOns();
			if (addOns != null) {
				for (TicketItemModifier ticketItemModifier : addOns) {
					ticketItemModifier.setPrintedToKitchen(true);
				}
			}
		}
	}

	public String getDiffWithCrntTime() {
		return DateUtil.getElapsedTime(getCreateDate(), new Date());
	}

	public void setDiffWithCrntTime(String diffWithCrntTime) {
	}

	public Customer getCustomer() {
		if (this.customer != null) {
			return this.customer;
		}
		Integer customerId = getCustomerId();
		if (customerId == null) {
			return null;
		}
		customer = CustomerDAO.getInstance().get(customerId);
		return customer;
	}

	public PosTransaction getBartabTransaction() {
		String bartabTransactionId = getProperty(NewBarTabAction.BARTAB_TRANSACTION_ID);
		if (StringUtils.isNotEmpty(bartabTransactionId)) {
			for (PosTransaction transaction : getTransactions()) {
				if (bartabTransactionId.equals(String.valueOf(transaction.getId()))) {
					return transaction;
				}
			}
		}
		for (PosTransaction transaction : getTransactions()) {
			if (transaction instanceof CreditCardTransaction && transaction.isAuthorizable() && !transaction.isCaptured() && !transaction.isVoided()) {
				return transaction;
			}
		}
		for (PosTransaction transaction : getTransactions()) {
			if (transaction instanceof CreditCardTransaction) {
				return transaction;
			}
		}
		return null;
	}
}