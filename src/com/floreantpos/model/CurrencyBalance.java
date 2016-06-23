package com.floreantpos.model;

import com.floreantpos.model.base.BaseCurrencyBalance;

public class CurrencyBalance extends BaseCurrencyBalance {
	private static final long serialVersionUID = 1L;

	private String currencyName;

	public CurrencyBalance() {
		super();
	}

	public CurrencyBalance(java.lang.Integer id) {
		super(id);
	}

	public String getCurrencyName() {
		return super.getCurrency().getName();
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

}