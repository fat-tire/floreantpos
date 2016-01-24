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

import javax.swing.JPanel;

import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.ui.dialog.TableSelectionView;
import com.floreantpos.ui.views.order.DefaultOrderServiceExtension;
import com.floreantpos.ui.views.order.ViewPanel;

/**
 * 
 * @author MShahriar
 */
public class TableMapView extends ViewPanel {

	public final static String VIEW_NAME = "TABLE_MAP"; //$NON-NLS-1$

	private TableSelectionView tableView;

	private OrderServiceExtension orderServiceExtension;
	private static TableMapView instance;

	private TableMapView() {
		initComponents();

		orderServiceExtension = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);

		if (orderServiceExtension == null) {
			orderServiceExtension = new DefaultOrderServiceExtension();
		}

		applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

	}

	private void initComponents() {
		setLayout(new BorderLayout());
		tableView = new TableSelectionView();
		FloorLayoutPlugin floorLayoutPlugin = (FloorLayoutPlugin) ExtensionManager.getPlugin(FloorLayoutPlugin.class);
		if (floorLayoutPlugin == null) {
			add(tableView, java.awt.BorderLayout.CENTER);
		}
	}

	public synchronized void updateTableView() {
		tableView.redererTable();
	}

	public static TableMapView getInstance() {
		if (instance == null) {
			instance = new TableMapView();
		}

		return instance;
	}

	public JPanel getTableSelectorPanel() {
		return tableView;
	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}

}