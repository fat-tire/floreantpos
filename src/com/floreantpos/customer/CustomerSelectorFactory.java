package com.floreantpos.customer;

import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.model.OrderType;

public class CustomerSelectorFactory {
	private static CustomerSelector customerSelector;

	public static CustomerSelectorDialog createCustomerSelectorDialog(OrderType orderType) {
		OrderServiceExtension orderServicePlugin = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);
		if (customerSelector == null) {
			if (orderServicePlugin == null) {
				customerSelector = new DefaultCustomerListView();
			}
			else {
				customerSelector = orderServicePlugin.createNewCustomerSelector();
			}
		}
		customerSelector.setOrderType(orderType);
		customerSelector.redererCustomers();

		return new CustomerSelectorDialog(customerSelector);
	}
}
