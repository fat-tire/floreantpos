package com.floreantpos.model;

import java.util.Set;

import com.floreantpos.model.base.BaseCashDrawer;

public class CashDrawer extends BaseCashDrawer {
	private static final long serialVersionUID = 1L;

	public CashDrawer() {
		super();
	}

	public CashDrawer(java.lang.Integer id) {
		super(id);
	}

	public CurrencyBalance getCurrencyBalance(Currency currency) {
		Set<CurrencyBalance> list = getCurrencyBalanceList();
		if (list == null) {
			return null;
		}

		for (CurrencyBalance currencyBalance : list) {
			if (currency.equals(currencyBalance.getCurrency())) {
				return currencyBalance;
			}
		}

		return null;
	}
}