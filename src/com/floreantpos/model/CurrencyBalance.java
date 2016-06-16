package com.floreantpos.model;

import com.floreantpos.model.base.BaseCurrencyBalance;

public class CurrencyBalance extends BaseCurrencyBalance {
	private static final long serialVersionUID = 1L;

	private String currencyName;
	private double cashCreditAmount;
	private double cashBackAmount;

	public CurrencyBalance() {
		super();
	}

	public CurrencyBalance(java.lang.Integer id) {
		super(id);
	}

	public double getCashBackAmount() {
		return cashBackAmount;
	}

	public void setCashBackAmount(double cashBack) {
		this.cashBackAmount = cashBack;
	}

	public double getCashCreditAmount() {
		return cashCreditAmount;
	}

	public void setCashCreditAmount(double cashCredit) {
		this.cashCreditAmount = cashCredit;
	}

	public String getCurrencyName() {
		return super.getCurrency().getName();
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	

}