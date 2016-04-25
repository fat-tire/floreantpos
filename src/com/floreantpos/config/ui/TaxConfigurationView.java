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
package com.floreantpos.config.ui;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.util.POSUtil;

public class TaxConfigurationView extends ConfigurationView {
	public static final String CONFIG_TAB_TAX = Messages.getString("TaxConfigurationView.0"); //$NON-NLS-1$
	private Restaurant restaurant;
	private JCheckBox cbItemSalesPriceIncludesTax;

	public TaxConfigurationView() {
		setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new MigLayout("", "[]", "[]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		cbItemSalesPriceIncludesTax = new JCheckBox(Messages.getString("TaxConfigurationView.4")); //$NON-NLS-1$
		contentPanel.add(cbItemSalesPriceIncludesTax, "cell 0 0"); //$NON-NLS-1$

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(null);
		add(scrollPane);
	}

	@Override
	public boolean save() throws Exception {
		if (!isInitialized()) {
			return true;
		}

		restaurant.setItemPriceIncludesTax(cbItemSalesPriceIncludesTax.isSelected());

		RestaurantDAO.getInstance().saveOrUpdate(restaurant);

		Application.getInstance().refreshRestaurant();

		return true;
	}

	@Override
	public void initialize() throws Exception {
		restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));
		cbItemSalesPriceIncludesTax.setSelected(POSUtil.getBoolean(restaurant.isItemPriceIncludesTax()));

		setInitialized(true);
	}

	@Override
	public String getName() {
		return CONFIG_TAB_TAX;
	}

}
