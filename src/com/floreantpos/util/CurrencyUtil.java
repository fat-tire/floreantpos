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
package com.floreantpos.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.floreantpos.model.Currency;
import com.floreantpos.model.dao.CurrencyDAO;

public class CurrencyUtil {
	private static Currency mainCurrency;
	private static List<Currency> auxiliaryCurrencyList;

	public static void populateCurrency() {
		auxiliaryCurrencyList = new ArrayList<Currency>();

		List<Currency> currencyList = CurrencyDAO.getInstance().findAll();

		if (currencyList != null) {
			for (Currency currency : currencyList) {
				if (currency.isMain()) {
					mainCurrency = currency;
				}
				else {
					auxiliaryCurrencyList.add(currency);
				}
			}
		}
	}

	public static Currency getMainCurrency() {
		return mainCurrency;
	}

	public static List<Currency> getAuxiliaryCurrencyList() {
		return auxiliaryCurrencyList;
	}

	public static List<Currency> getAllCurrency() {
		List<Currency> currencyList = new ArrayList<Currency>();
		currencyList.add(mainCurrency);
		Collections.sort(auxiliaryCurrencyList, new Comparator<Currency>() {
			@Override
			public int compare(Currency curr1, Currency curr2) {
				return curr1.getName().compareTo(curr2.getName());
			}
		});
		currencyList.addAll(auxiliaryCurrencyList);
		return currencyList;
	}

	public static String getCurrencyName() {
		String currencyName = null;
		if (mainCurrency != null) {
			currencyName = mainCurrency.getName();
		}
		else {
			currencyName = "USD"; //$NON-NLS-1$
		}
		return currencyName;
	}

	public static String getCurrencySymbol() {
		String currencySymbol = null;
		if (mainCurrency != null) {
			currencySymbol = mainCurrency.getSymbol();
		}
		else {
			currencySymbol = "$"; //$NON-NLS-1$
		}
		return currencySymbol;
	}
}
