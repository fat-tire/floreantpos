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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.base.BaseMenuModifier;
import com.floreantpos.util.POSUtil;

public class MenuModifier extends BaseMenuModifier {
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public MenuModifier () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuModifier (java.lang.Integer id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */

	private transient MenuItemModifierGroup menuItemModifierGroup;

	public MenuItemModifierGroup getMenuItemModifierGroup() {
		return menuItemModifierGroup;
	}

	public void setMenuItemModifierGroup(MenuItemModifierGroup menuItemModifierGroup) {
		this.menuItemModifierGroup = menuItemModifierGroup;
	}

	@Override
	public Integer getSortOrder() {
		return sortOrder == null ? 9999 : sortOrder;
	}

	@Override
	public Integer getButtonColor() {
		return buttonColor;
	}

	@Override
	public Integer getTextColor() {
		return textColor;
	}

	public String getDisplayName() {
		if (TerminalConfig.isUseTranslatedName() && StringUtils.isNotEmpty(getTranslatedName())) {
			return getTranslatedName();
		}

		return super.getName();
	}

	@Override
	public String toString() {
		return getName();
	}

	public String getUniqueId() {
		return ("menu_modifier_" + getName() + "_" + getId()).replaceAll("\\s+", "_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	//

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

	public void removeProperty(String typeProperty, String taxProperty) {
		Map<String, String> properties = getProperties();
		if (properties == null) {
			return;
		}
		properties.remove(typeProperty);
		properties.remove(taxProperty);
	}

	public double getPriceForMultiplier(Multiplier multiplier) {
		double defaultPrice = this.getPrice();
		if (multiplier == null || multiplier.isMain()) {
			return defaultPrice;
		}

		List<ModifierMultiplierPrice> priceList = getMultiplierPriceList();
		if (priceList == null || priceList.isEmpty()) {
			return defaultPrice * multiplier.getRate() / 100;
		}
		for (ModifierMultiplierPrice multiplierPrice : priceList) {
			if (multiplier.getName().equals(multiplierPrice.getMultiplier().getName())) {
				return multiplierPrice.getPrice();
			}
		}
		return defaultPrice * multiplier.getRate() / 100;
	}

	public double getPriceForSize(MenuItemSize size, boolean extra) {
		return getPriceForSizeAndMultiplier(size, extra, null);
	}

	public double getPriceForSizeAndMultiplier(MenuItemSize size, boolean extra, Multiplier multiplier) {
		List<PizzaModifierPrice> priceList = getPizzaModifierPriceList();
		double regularPrice = 0;
		if (isPizzaModifier() && priceList != null) {
			for (PizzaModifierPrice pizzaModifierPrice : priceList) {
				if (size.getId().intValue() == pizzaModifierPrice.getSize().getId().intValue()) {
					List<ModifierMultiplierPrice> multiplierPriceList = pizzaModifierPrice.getMultiplierPriceList();
					if (multiplierPriceList != null) {
						Double multiplierPrice = null;
						for (ModifierMultiplierPrice price : multiplierPriceList) {
							String priceTableMultiplierName = price.getMultiplier().getName();
							if (priceTableMultiplierName.equals(Multiplier.REGULAR)) {
								regularPrice = price.getPrice();
								if (multiplier.getName().equals(Multiplier.REGULAR)) {
									return regularPrice;
								}
							}
							else if (priceTableMultiplierName.equals(multiplier.getName())) {
								multiplierPrice = price.getPrice();
							}
						}
						if (multiplierPrice != null) {
							return multiplierPrice;
						}
					}
				}
			}
		}
		return regularPrice * multiplier.getRate() / 100;
	}

	public double getPriceByOrderType(OrderType type) {
		double defaultPrice = this.getPrice();
		if (type == null) {
			return defaultPrice;
		}

		String priceProp = getProperty(type.name() + "_PRICE"); //$NON-NLS-1$
		if (priceProp == null)
			return defaultPrice;

		try {
			return Double.parseDouble(priceProp);
		} catch (Exception e) {
			return defaultPrice;
		}
	}

	public double getTaxByOrderType(OrderType type) {
		if (this.getTax() == null) {
			return 0;
		}
		double defaultTax = this.getTax().getRate();
		if (type == null) {
			return defaultTax;
		}

		String taxProp = getProperty(type.name() + "_TAX"); //$NON-NLS-1$
		if (taxProp == null)
			return defaultTax;

		try {
			return Double.parseDouble(taxProp);
		} catch (Exception e) {
			return defaultTax;
		}
	}

	public double getExtraPriceByOrderType(OrderType type) {
		double defaultPrice = this.getExtraPrice();
		if (type == null) {
			return defaultPrice;
		}

		String extraPriceProp = getProperty(type.name() + "_EXTRA_PRICE"); //$NON-NLS-1$
		if (extraPriceProp == null)
			return defaultPrice;

		try {
			return Double.parseDouble(extraPriceProp);
		} catch (Exception e) {
			return defaultPrice;
		}
	}

	public double getExtraTaxByOrderType(OrderType type) {
		if (this.getTax() == null) {
			return 0;
		}
		double defaultTax = this.getTax().getRate();
		if (type == null) {
			return defaultTax;
		}

		String extraTaxProp = getProperty(type.name() + "_EXTRA_TAX"); //$NON-NLS-1$
		if (extraTaxProp == null)
			return defaultTax;

		try {
			return Double.parseDouble(extraTaxProp);
		} catch (Exception e) {
			return defaultTax;
		}
	}

	public boolean isPropertyValueTrue(String propertyName) {
		String property = getProperty(propertyName);

		return POSUtil.getBoolean(property);
	}

	public void setPriceByOrderType(String type, double price) {
		type = type.replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
		addProperty(type + "_PRICE", String.valueOf(price)); //$NON-NLS-1$
	}

	public void setTaxByOrderType(String type, double taxRate) {
		type = type.replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
		addProperty(type + "_TAX", String.valueOf(taxRate)); //$NON-NLS-1$
	}

	public void setExtraPriceByOrderType(String type, double extraPrice) {
		type = type.replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
		addProperty(type + "_EXTRA_PRICE", String.valueOf(extraPrice)); //$NON-NLS-1$
	}

	public void setExtraTaxByOrderType(String type, double extraTaxRate) {
		type = type.replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
		addProperty(type + "_EXTRA_TAX", String.valueOf(extraTaxRate)); //$NON-NLS-1$
	}
}