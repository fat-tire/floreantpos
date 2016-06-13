package com.floreantpos.model;

import com.floreantpos.model.base.BaseCashDrawer;

public class CashDrawer extends BaseCashDrawer {
	private static final long serialVersionUID = 1L;

	private double cashCreditAmount;
	private double cashBackAmount;

	public CashDrawer() {
		super();
	}

	public CashDrawer(java.lang.Integer id) {
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

}