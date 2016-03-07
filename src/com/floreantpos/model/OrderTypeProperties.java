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

import java.util.ArrayList;
import java.util.List;

import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.model.base.BaseOrderTypeProperties;
import com.floreantpos.model.dao.OrderTypePropertiesDAO;
import com.floreantpos.ui.views.order.OrderType;

public class OrderTypeProperties extends BaseOrderTypeProperties {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public OrderTypeProperties() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public OrderTypeProperties(java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/

	@Override
	public Boolean isVisible() {
		return visible == null ? Boolean.TRUE : visible;
	}

	public static List<String> getVisibleOrderTypes() {
		List<String> orderTypes = new ArrayList<String>();
		List<OrderTypeProperties> orderTypeProperties = OrderTypePropertiesDAO.getInstance().findVisibleOrderTypeProperties();

		OrderServiceExtension orderServiceExtension = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);
		for (OrderTypeProperties orderType : orderTypeProperties) {
			if (orderServiceExtension == null) {
				String oType = orderType.getOrdetType();
				if (oType.equals(OrderType.HOME_DELIVERY.name()) || oType.equals(OrderType.PICKUP.name()) || oType.equals(OrderType.DRIVE_THRU.name())) {
					continue;
				}
			}
			orderTypes.add(orderType.ordetType.replaceAll("_", " "));
		}
		return orderTypes;
	}
}