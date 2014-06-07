package com.floreantpos.config.ui;

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.ui.dialog.POSMessageDialog;
import javax.swing.JSeparator;

public class RestaurantConfigurationView extends ConfigurationView {
	private RestaurantDAO dao;
	private Restaurant restaurant;
	private POSTextField tfRestaurantName;
	private POSTextField tfAddressLine1;
	private POSTextField tfAddressLine2;
	private POSTextField tfAddressLine3;
	private POSTextField tfTelephone;
	private POSTextField tfCapacity;
	private POSTextField tfTable;
	private POSTextField tfCurrencyName;
	private POSTextField tfCurrencySymbol;
	private POSTextField tfServiceCharge;
	private POSTextField tfDefaultGratuity;
	
	public RestaurantConfigurationView() {
		setLayout(new MigLayout("", "[grow][grow][][grow]", "[grow][][][][][][][][][][][]"));
		
		JLabel lblNewLabel = new JLabel("Restaurant name:");
		add(lblNewLabel, "cell 0 1,alignx trailing");
		
		tfRestaurantName = new POSTextField();
		add(tfRestaurantName, "cell 1 1 3 1,growx");
		
		JLabel lblAddressLine = new JLabel("Address line 1:");
		add(lblAddressLine, "cell 0 2,alignx trailing");
		
		tfAddressLine1 = new POSTextField();
		add(tfAddressLine1, "cell 1 2 3 1,growx");
		
		JLabel lblAddressLine_1 = new JLabel("Address line 2:");
		add(lblAddressLine_1, "cell 0 3,alignx trailing");
		
		tfAddressLine2 = new POSTextField();
		add(tfAddressLine2, "cell 1 3 3 1,growx");
		
		JLabel lblAddressLine_2 = new JLabel("Address line 3:");
		add(lblAddressLine_2, "cell 0 4,alignx trailing");
		
		tfAddressLine3 = new POSTextField();
		add(tfAddressLine3, "cell 1 4 3 1,growx");
		
		JLabel lblPhone = new JLabel("Phone:");
		add(lblPhone, "cell 0 5,alignx trailing");
		
		tfTelephone = new POSTextField();
		add(tfTelephone, "cell 1 5 2 1,growx");
		
		JSeparator separator = new JSeparator();
		add(separator, "cell 0 6 4 1,growx");
		
		JLabel lblCapacity = new JLabel("Capacity:");
		add(lblCapacity, "cell 0 7,alignx trailing");
		
		tfCapacity = new POSTextField();
		add(tfCapacity, "cell 1 7,growx");
		
		tfTable = new POSTextField();
		add(tfTable, "cell 3 7,growx");
		
		JLabel lblCurrencyName = new JLabel("Currency name:");
		add(lblCurrencyName, "cell 0 8,alignx trailing");
		
		JLabel lblTables = new JLabel("Tables:");
		add(lblTables, "flowx,cell 2 7");
		
		tfCurrencyName = new POSTextField();
		add(tfCurrencyName, "flowx,cell 1 8");
		
		JLabel lblCurrencySymbol = new JLabel("Currency symbol:");
		add(lblCurrencySymbol, "cell 2 8,alignx trailing");
		
		tfCurrencySymbol = new POSTextField();
		add(tfCurrencySymbol, "cell 3 8,growx");
		
		JSeparator separator_1 = new JSeparator();
		add(separator_1, "cell 0 9 4 1,growx");
		
		JLabel lblServiceCharge = new JLabel("Service Charge:");
		add(lblServiceCharge, "cell 0 10,alignx trailing");
		
		tfServiceCharge = new POSTextField();
		add(tfServiceCharge, "cell 1 10,growx");
		
		JLabel label = new JLabel("%");
		add(label, "cell 2 10");
		
		JLabel lblDefaultGratuity = new JLabel("Default gratuity:");
		add(lblDefaultGratuity, "cell 0 11,alignx trailing");
		
		tfDefaultGratuity = new POSTextField();
		add(tfDefaultGratuity, "cell 1 11,growx");
		
		JLabel label_1 = new JLabel("%");
		add(label_1, "cell 2 11");
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
		
		int capacity = 0;
		int tables = 0;
		double serviceCharge = 0;
		double gratuityPercentage = 0;

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
		restaurant.setTelephone(telephone);
		restaurant.setCapacity(capacity);
		restaurant.setTables(tables);
		restaurant.setCurrencyName(currencyName);
		restaurant.setCurrencySymbol(currencySymbol);
		restaurant.setServiceChargePercentage(serviceCharge);
		restaurant.setDefaultGratuityPercentage(gratuityPercentage);
		
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
		tfServiceCharge.setText(String.valueOf(restaurant.getServiceChargePercentage()));
		tfDefaultGratuity.setText(String.valueOf(restaurant.getDefaultGratuityPercentage()));
		
		setInitialized(true);
	}
	
	@Override
	public String getName() {
		return com.floreantpos.POSConstants.RESTAURANT_CONFIGURATION;
	}
}
