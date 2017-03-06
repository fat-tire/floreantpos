package com.floreantpos.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.floreantpos.model.Discount;
import com.floreantpos.model.TicketDiscount;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemDiscount;

public class DiscountUtil {
	public static Double calculateDiscountAmount(TicketItemDiscount ticketItemDiscount) {
		TicketItem ticketItem = ticketItemDiscount.getTicketItem();
		//		Ticket ticket = ticketItem.getTicket();

		int itemCount = ticketItem.getItemCount(); //ticket.countItem(ticketItem);
		double subtotalAmount = ticketItem.getSubtotalAmount();
		double amountToBeDiscounted = subtotalAmount / itemCount;
		if (ticketItemDiscount.getMinimumQuantity() > 0) {
			int minQuantity = ticketItemDiscount.getMinimumQuantity();
			
			switch (ticketItemDiscount.getType()) {
				case Discount.DISCOUNT_TYPE_AMOUNT:
					return Math.floor(itemCount / minQuantity) * ticketItemDiscount.getValue();

				case Discount.DISCOUNT_TYPE_PERCENTAGE:
					return Math.floor(itemCount / minQuantity) * (amountToBeDiscounted * ticketItemDiscount.getValue() / 100);
			}
		}
		switch (ticketItemDiscount.getType()) {
			case Discount.DISCOUNT_TYPE_AMOUNT:
				return ticketItemDiscount.getValue();

			case Discount.DISCOUNT_TYPE_PERCENTAGE:
				return (amountToBeDiscounted * ticketItemDiscount.getValue()) / 100.0;
		}

		return 0.0;
	}

	public static Double calculateDiscountAmount(double price, TicketDiscount discount) {

		switch (discount.getType()) {
			case Discount.DISCOUNT_TYPE_AMOUNT:
				return discount.getValue();

			case Discount.DISCOUNT_TYPE_PERCENTAGE:
				return (price * discount.getValue()) / 100.0;
		}

		return (price * discount.getValue()) / 100.0;
	}

	public static TicketItemDiscount getMaxDiscount(List<TicketItemDiscount> discounts) {
		if (discounts == null || discounts.isEmpty()) {
			return null;
		}

		TicketItemDiscount maxDiscount = Collections.max(discounts, new Comparator<TicketItemDiscount>() {
			@Override
			public int compare(TicketItemDiscount o1, TicketItemDiscount o2) {
				return (int) (o1.getSubTotalAmountDisplay() - o2.getSubTotalAmountDisplay());
			}
		});

		return maxDiscount;
	}

	public static TicketDiscount getMaxDiscount(List<TicketDiscount> discounts, final double price) {
		if (discounts == null || discounts.isEmpty()) {
			return null;
		}

		TicketDiscount maxDiscount = Collections.max(discounts, new Comparator<TicketDiscount>() {
			@Override
			public int compare(TicketDiscount o1, TicketDiscount o2) {
				return (int) (calculateDiscountAmount(price, o1) - calculateDiscountAmount(price, o2));
			}
		});

		return maxDiscount;
	}

}
