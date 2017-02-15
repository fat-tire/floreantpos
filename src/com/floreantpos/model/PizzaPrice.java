package com.floreantpos.model;

import com.floreantpos.model.base.BasePizzaPrice;

public class PizzaPrice extends BasePizzaPrice {
	private static final long serialVersionUID = 1L;

	public PizzaPrice() {
		super();
	}

	public PizzaPrice(java.lang.Integer id) {
		super(id);
	}

	public Double getPrice(int defaultSellPortion) {
		return super.getPrice() * defaultSellPortion / 100;
	}

}