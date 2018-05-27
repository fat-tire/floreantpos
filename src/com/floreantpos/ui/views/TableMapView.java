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

import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.model.OrderType;
import com.floreantpos.ui.tableselection.DefaultTableSelectionView;
import com.floreantpos.ui.tableselection.TableSelector;
import com.floreantpos.ui.views.order.ViewPanel;

/**
 * 
 * @author MShahriar
 */
public class TableMapView extends ViewPanel {

	public final static String VIEW_NAME = "TABLE_MAP"; //$NON-NLS-1$

	private TableSelector tableSelector = null;

	private OrderType orderType;
	private static TableMapView instance;

	private TableMapView() {
		initComponents();

		applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		FloorLayoutPlugin floorLayoutPlugin = (FloorLayoutPlugin) ExtensionManager.getPlugin(FloorLayoutPlugin.class);
		if (floorLayoutPlugin == null) {
			tableSelector = DefaultTableSelectionView.getInstance();

		}
		else {
			tableSelector = floorLayoutPlugin.createTableSelector();
		}
		tableSelector.setCreateNewTicket(true);
		tableSelector.updateView(false);
		add(tableSelector, BorderLayout.CENTER);
	}

	public void updateView() {
		tableSelector.redererTables();
	}

	public static TableMapView getInstance() {
		if (instance == null) {
			instance = new TableMapView();
		}

		return instance;
	}

	public static TableMapView getInstance(OrderType orderType) {
		TableMapView instance2 = getInstance();
		instance2.tableSelector.setOrderType(orderType);
		instance2.orderType = orderType;
		return instance2;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}
}