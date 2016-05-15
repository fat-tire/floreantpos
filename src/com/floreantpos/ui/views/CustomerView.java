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
/*
 * SwitchboardView.java
 *
 * Created on August 14, 2006, 11:45 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.util.Locale;

import com.floreantpos.customer.CustomerSelector;
import com.floreantpos.customer.DefaultCustomerListView;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.model.OrderType;
import com.floreantpos.ui.views.order.ViewPanel;

/**
 * 
 * @author MShahriar
 */
public class CustomerView extends ViewPanel {

	public final static String VIEW_NAME = "CUSTOMER_ACTIVITY"; //$NON-NLS-1$

	private CustomerSelector customerSelector = null;
	private static CustomerView instance;

	private CustomerView() {
		initComponents();
		applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		OrderServiceExtension orderServicePlugin = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);
		if (orderServicePlugin == null) {
			customerSelector = new DefaultCustomerListView();

		}
		else {
			customerSelector = orderServicePlugin.createCustomerSelector();
		}
		customerSelector.setCreateNewTicket(true);
		customerSelector.updateView(false);
		add(customerSelector, BorderLayout.CENTER);
	}

	public void updateView() {
		customerSelector.redererCustomers();
	}

	private static CustomerView getInstance() {
		if (instance == null) {
			instance = new CustomerView();
		}

		return instance;
	}

	public static CustomerView getInstance(OrderType orderType) {
		CustomerView instance2 = getInstance();
		instance2.customerSelector.setOrderType(orderType);

		return instance2;
	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}
}