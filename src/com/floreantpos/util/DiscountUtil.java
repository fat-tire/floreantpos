package com.floreantpos.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.floreantpos.model.Discount;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TicketItemDiscount;

public class DiscountUtil {
	public static Double calculateDiscountAmount(double price, TicketItemDiscount discount) {

		switch (discount.getType()) {
			case Discount.DISCOUNT_TYPE_AMOUNT:
				return discount.getValue();

			case Discount.DISCOUNT_TYPE_PERCENTAGE:
				return (price * discount.getValue()) / 100.0;
		}

		return (price * discount.getValue()) / 100.0;
	}
	
	public static Double calculateDiscountAmount(double price, TicketCouponAndDiscount discount) {

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
				return (int) (o1.getSubTotalAmountWithoutModifiersDisplay() - o2.getSubTotalAmountWithoutModifiersDisplay());
			}
		});

		return maxDiscount;
	}

	public static TicketCouponAndDiscount getMaxDiscount(List<TicketCouponAndDiscount> discounts, final double price) {
		if (discounts == null || discounts.isEmpty()) {
			return null;
		}
		
		TicketCouponAndDiscount maxDiscount = Collections.max(discounts, new Comparator<TicketCouponAndDiscount>() {
			@Override
			public int compare(TicketCouponAndDiscount o1, TicketCouponAndDiscount o2) {
				return (int) (calculateDiscountAmount(price, o1) - calculateDiscountAmount(price, o2));
			}
		});

		return maxDiscount;
	}

}
