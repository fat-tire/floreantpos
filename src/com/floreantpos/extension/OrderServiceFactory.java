package com.floreantpos.extension;

import com.floreantpos.ui.views.order.DefaultOrderServiceExtension;

public class OrderServiceFactory {
	private static OrderServiceExtension orderService;
	
	public static OrderServiceExtension getOrderService() {
		if(orderService != null) {
			return orderService;
		}
		
		orderService = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);

		if (orderService == null) {
			orderService = new DefaultOrderServiceExtension();
		}
		
		return orderService;
	}
}
