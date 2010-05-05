package com.floreantpos.config.ui;

import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class RestaurantConfigurationView extends ConfigurationView {
	private RestaurantDAO dao;
	private Restaurant restaurant;
	
	private JTextField tfRestaurantName = new FixedLengthTextField(40);
	private JTextField tfAddressLine1 = new FixedLengthTextField(20);
	private JTextField tfAddressLine2 = new FixedLengthTextField(20);
	private JTextField tfAddressLine3 = new FixedLengthTextField(20);
	private JTextField tfTelephone = new FixedLengthTextField(16);
	private JTextField tfCurrencyName = new FixedLengthTextField(20);
	private JTextField tfCurrencySymbol = new FixedLengthTextField(10);
	private JTextField tfCapacity = new JTextField();
	private JTextField tfTable = new JTextField();
	
	public RestaurantConfigurationView() {
		setLayout(new MigLayout("align 50% 50%"));
		
		addRow(com.floreantpos.POSConstants.RESTAURANT_NAME_, tfRestaurantName, "w 300");
		addRow(com.floreantpos.POSConstants.ADDRESS_LINE1, tfAddressLine1, "w 300");
		addRow(com.floreantpos.POSConstants.ADDRESS_LINE2, tfAddressLine2, "w 300");
		addRow(com.floreantpos.POSConstants.ADDRESS_LINE3, tfAddressLine3, "w 300");
		addRow(com.floreantpos.POSConstants.TELEPHONE + ":", tfTelephone, "w 150");
		addRow(com.floreantpos.POSConstants.CAPACITY_, tfCapacity, "w 150");
		addRow(com.floreantpos.POSConstants.TABLES + ":", tfTable, "w 150");
		addRow(com.floreantpos.POSConstants.CURRENCY_NAME_, tfCurrencyName, "w 50");
		addRow(com.floreantpos.POSConstants.CURRENCY_SYMBOL_, tfCurrencySymbol, "w 50");
	}

	@Override
	public boolean save() throws Exception {
		if(!isInitialized()) {
			return true;
		}
		
		String name = null;
		String addr1 = null;
		String addr2 = null;
		String addr3 = null;
		String telephone = null;
		String currencyName = null;
		String currencySymbol = null;
		
		int capacity = 299;
		int tables = 74;

		name = tfRestaurantName.getText();
		addr1 = tfAddressLine1.getText();
		addr2 = tfAddressLine2.getText();
		addr3 = tfAddressLine3.getText();
		telephone = tfTelephone.getText();
		currencyName = tfCurrencyName.getText();
		currencySymbol = tfCurrencySymbol.getText();
		
		if(StringUtils.isEmpty(currencyName)) {
			currencyName = com.floreantpos.POSConstants.DOLLAR;
		}
		if(StringUtils.isEmpty(currencySymbol)) {
			currencySymbol = "$";
		}
		
		try {
			capacity = Integer.parseInt(tfCapacity.getText());
		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.CAPACITY_IS_NOT_VALID_);
			return false;
		}

		try {
			tables = Integer.parseInt(tfTable.getText());
		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.NUMBER_OF_TABLES_IS_NOT_VALID);
			return false;
		}

		restaurant.setName(name);
		restaurant.setAddressLine1(addr1);
		restaurant.setAddressLine2(addr2);
		restaurant.setAddressLine3(addr3);
		restaurant.setTelephone(telephone);
		restaurant.setCapacity(capacity);
		restaurant.setTables(tables);
		restaurant.setCurrencyName(currencyName);
		restaurant.setCurrencySymbol(currencySymbol);
		
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
		tfTelephone.setText(restaurant.getTelephone());
		tfCapacity.setText(String.valueOf(restaurant.getCapacity()));
		tfTable.setText(String.valueOf(restaurant.getTables()));
		tfCurrencyName.setText(restaurant.getCurrencyName());
		tfCurrencySymbol.setText(restaurant.getCurrencySymbol());
		
		setInitialized(true);
	}
	
	@Override
	public String getName() {
		return com.floreantpos.POSConstants.RESTAURANT_CONFIGURATION;
	}
}
