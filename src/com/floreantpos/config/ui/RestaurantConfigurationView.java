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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.POSTextField;

public class RestaurantConfigurationView extends ConfigurationView {
	private RestaurantDAO dao;
	private Restaurant restaurant;
	private FixedLengthTextField tfRestaurantName;
	private FixedLengthTextField tfAddressLine1;
	private FixedLengthTextField tfAddressLine2;
	private FixedLengthTextField tfAddressLine3;
	private POSTextField tfTelephone;
	//private POSTextField tfCurrencyName;
	//private POSTextField tfCurrencySymbol;
	private POSTextField tfServiceCharge;
	private POSTextField tfDefaultGratuity;
	private POSTextField tfTicketFooter;
	private JTextField tfZipCode;

	public RestaurantConfigurationView() {
		setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel(new MigLayout("fillx", "[][grow][][grow]", "[][][][][][][][][][][][][][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblNewLabel = new JLabel(Messages.getString("RestaurantConfigurationView.3") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(lblNewLabel, "cell 0 1,alignx trailing"); //$NON-NLS-1$

		tfRestaurantName = new FixedLengthTextField();
		tfRestaurantName.setLength(120);
		contentPanel.add(tfRestaurantName, "cell 1 1 3 1,growx"); //$NON-NLS-1$

		JLabel lblAddressLine = new JLabel(Messages.getString("RestaurantConfigurationView.7") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(lblAddressLine, "cell 0 2,alignx trailing"); //$NON-NLS-1$

		tfAddressLine1 = new FixedLengthTextField();
		tfAddressLine1.setLength(60);
		contentPanel.add(tfAddressLine1, "cell 1 2 3 1,growx"); //$NON-NLS-1$

		JLabel lblAddressLine_1 = new JLabel(Messages.getString("RestaurantConfigurationView.11") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(lblAddressLine_1, "cell 0 3,alignx trailing"); //$NON-NLS-1$

		tfAddressLine2 = new FixedLengthTextField();
		tfAddressLine2.setLength(60);
		contentPanel.add(tfAddressLine2, "cell 1 3 3 1,growx"); //$NON-NLS-1$

		JLabel lblAddressLine_2 = new JLabel(Messages.getString("RestaurantConfigurationView.15") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(lblAddressLine_2, "cell 0 4,alignx trailing"); //$NON-NLS-1$

		tfAddressLine3 = new FixedLengthTextField();
		tfAddressLine3.setLength(60);
		contentPanel.add(tfAddressLine3, "cell 1 4 3 1,growx"); //$NON-NLS-1$

		JLabel lblZipCode = new JLabel(Messages.getString("RestaurantConfigurationView.19")); //$NON-NLS-1$
		contentPanel.add(lblZipCode, "cell 0 5,alignx trailing"); //$NON-NLS-1$

		tfZipCode = new JTextField();
		contentPanel.add(tfZipCode, "cell 1 5,growx"); //$NON-NLS-1$
		tfZipCode.setColumns(10);

		JLabel lblPhone = new JLabel(Messages.getString("RestaurantConfigurationView.22")); //$NON-NLS-1$
		contentPanel.add(lblPhone, "cell 0 6,alignx trailing"); //$NON-NLS-1$

		tfTelephone = new POSTextField();
		contentPanel.add(tfTelephone, "cell 1 6,growx"); //$NON-NLS-1$

		//JSeparator separator = new JSeparator();
		//contentPanel.add(separator, "cell 0 8 4 1,growx"); //$NON-NLS-1$

		//JLabel lblCurrencyName = new JLabel(Messages.getString("RestaurantConfigurationView.30") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		//contentPanel.add(lblCurrencyName, "cell 0 10,alignx trailing"); //$NON-NLS-1$

		//tfCurrencyName = new POSTextField();
		//contentPanel.add(tfCurrencyName, "growx,cell 1 10"); //$NON-NLS-1$

		//JLabel lblCurrencySymbol = new JLabel(Messages.getString("RestaurantConfigurationView.37") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		//contentPanel.add(lblCurrencySymbol, "cell 2 10,alignx trailing"); //$NON-NLS-1$

		//tfCurrencySymbol = new POSTextField();
		//contentPanel.add(tfCurrencySymbol, "cell 3 10,growx"); //$NON-NLS-1$

		//JSeparator separator_1 = new JSeparator();
		//contentPanel.add(separator_1, "cell 0 11 4 1,growx"); //$NON-NLS-1$

		JLabel lblServiceCharge = new JLabel(Messages.getString("RestaurantConfigurationView.42") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(lblServiceCharge, "cell 0 12,alignx trailing"); //$NON-NLS-1$

		tfServiceCharge = new POSTextField();
		contentPanel.add(tfServiceCharge, "cell 1 12,growx"); //$NON-NLS-1$

		JLabel label = new JLabel("%"); //$NON-NLS-1$
		contentPanel.add(label, "cell 2 12"); //$NON-NLS-1$

		JLabel lblDefaultGratuity = new JLabel(Messages.getString("RestaurantConfigurationView.48") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(lblDefaultGratuity, "flowy,cell 0 13,alignx trailing"); //$NON-NLS-1$

		tfDefaultGratuity = new POSTextField();
		contentPanel.add(tfDefaultGratuity, "cell 1 13,growx"); //$NON-NLS-1$

		JLabel label_1 = new JLabel("%"); //$NON-NLS-1$
		contentPanel.add(label_1, "cell 2 13"); //$NON-NLS-1$

		JLabel lblTicketFooterMessage = new JLabel(Messages.getString("RestaurantConfigurationView.54") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(lblTicketFooterMessage, "cell 0 14,alignx trailing"); //$NON-NLS-1$

		tfTicketFooter = new POSTextField();
		contentPanel.add(tfTicketFooter, "cell 1 14 3 1,growx"); //$NON-NLS-1$

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(null);
		add(scrollPane);
	}

	@Override
	public boolean save() throws Exception {
		if (!isInitialized()) {
			return true;
		}

		String name = null;
		String addr1 = null;
		String addr2 = null;
		String addr3 = null;
		String telephone = null;
		String currencyName = null;
		String currencySymbol = null;

		int capacity = 0;
		int tables = 0;
		double serviceCharge = 0;
		double gratuityPercentage = 0;

		name = tfRestaurantName.getText();
		addr1 = tfAddressLine1.getText();
		addr2 = tfAddressLine2.getText();
		addr3 = tfAddressLine3.getText();
		telephone = tfTelephone.getText();
		//currencyName = tfCurrencyName.getText();
		//currencySymbol = tfCurrencySymbol.getText();

		if (StringUtils.isEmpty(currencyName)) {
			currencyName = com.floreantpos.POSConstants.DOLLAR;
		}
		if (StringUtils.isEmpty(currencySymbol)) {
			currencySymbol = "$"; //$NON-NLS-1$
		}

		try {
			serviceCharge = Double.parseDouble(tfServiceCharge.getText());
		} catch (Exception x) {
			//do nothing
		}

		try {
			gratuityPercentage = Double.parseDouble(tfDefaultGratuity.getText());
		} catch (Exception x) {
			//do nothing
		}

		restaurant.setName(name);
		restaurant.setAddressLine1(addr1);
		restaurant.setAddressLine2(addr2);
		restaurant.setAddressLine3(addr3);
		restaurant.setZipCode(tfZipCode.getText());
		restaurant.setTelephone(telephone);
		restaurant.setCapacity(capacity);
		restaurant.setTables(tables);
		restaurant.setCurrencyName(currencyName);
		restaurant.setCurrencySymbol(currencySymbol);
		restaurant.setServiceChargePercentage(serviceCharge);
		restaurant.setDefaultGratuityPercentage(gratuityPercentage);
		restaurant.setTicketFooterMessage(tfTicketFooter.getText());

		dao.saveOrUpdate(restaurant);

		Application.getInstance().refreshRestaurant();

		return true;
	}

	@Override
	public void initialize() throws Exception {
		dao = new RestaurantDAO();
		restaurant = dao.get(Integer.valueOf(1));

		tfRestaurantName.setText(restaurant.getName());
		tfAddressLine1.setText(restaurant.getAddressLine1());
		tfAddressLine2.setText(restaurant.getAddressLine2());
		tfAddressLine3.setText(restaurant.getAddressLine3());
		tfZipCode.setText(restaurant.getZipCode());
		tfTelephone.setText(restaurant.getTelephone());
		//tfCurrencyName.setText(restaurant.getCurrencyName());
		//tfCurrencySymbol.setText(restaurant.getCurrencySymbol());
		tfServiceCharge.setText(String.valueOf(restaurant.getServiceChargePercentage()));
		tfDefaultGratuity.setText(String.valueOf(restaurant.getDefaultGratuityPercentage()));
		tfTicketFooter.setText(restaurant.getTicketFooterMessage());

		setInitialized(true);
	}

	@Override
	public String getName() {
		return com.floreantpos.POSConstants.CONFIG_TAB_RESTAURANT;
	}
}
