package com.floreantpos.config.ui;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class RestaurantConfigurationView extends ConfigurationView {
	private RestaurantDAO dao;
	private Restaurant restaurant;
	private FixedLengthTextField tfRestaurantName;
	private FixedLengthTextField tfAddressLine1;
	private FixedLengthTextField tfAddressLine2;
	private FixedLengthTextField tfAddressLine3;
	private POSTextField tfTelephone;
	private POSTextField tfCapacity;
	private POSTextField tfTable;
	private POSTextField tfCurrencyName;
	private POSTextField tfCurrencySymbol;
	private POSTextField tfServiceCharge;
	private POSTextField tfDefaultGratuity;
	private POSTextField tfTicketFooter;
	private JTextField tfZipCode;
	
	public RestaurantConfigurationView() {
		setLayout(new MigLayout("", "[grow][grow][][grow]", "[grow][][][][][][][][][][][][][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		JLabel lblNewLabel = new JLabel(Messages.getString("RestaurantConfigurationView.3") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblNewLabel, "cell 0 1,alignx trailing"); //$NON-NLS-1$
		
		tfRestaurantName = new FixedLengthTextField(120);
		add(tfRestaurantName, "cell 1 1 3 1,growx"); //$NON-NLS-1$
		
		JLabel lblAddressLine = new JLabel(Messages.getString("RestaurantConfigurationView.7") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblAddressLine, "cell 0 2,alignx trailing"); //$NON-NLS-1$
		
		tfAddressLine1 = new FixedLengthTextField(60);
		add(tfAddressLine1, "cell 1 2 3 1,growx"); //$NON-NLS-1$
		
		JLabel lblAddressLine_1 = new JLabel(Messages.getString("RestaurantConfigurationView.11") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblAddressLine_1, "cell 0 3,alignx trailing"); //$NON-NLS-1$
		
		tfAddressLine2 = new FixedLengthTextField(60);
		add(tfAddressLine2, "cell 1 3 3 1,growx"); //$NON-NLS-1$
		
		JLabel lblAddressLine_2 = new JLabel(Messages.getString("RestaurantConfigurationView.15") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblAddressLine_2, "cell 0 4,alignx trailing"); //$NON-NLS-1$
		
		tfAddressLine3 = new FixedLengthTextField(60);
		add(tfAddressLine3, "cell 1 4 3 1,growx"); //$NON-NLS-1$
		
		JLabel lblZipCode = new JLabel(Messages.getString("RestaurantConfigurationView.19")); //$NON-NLS-1$
		add(lblZipCode, "cell 0 5,alignx trailing"); //$NON-NLS-1$
		
		tfZipCode = new JTextField();
		add(tfZipCode, "cell 1 5,growx"); //$NON-NLS-1$
		tfZipCode.setColumns(10);
		
		JLabel lblPhone = new JLabel(Messages.getString("RestaurantConfigurationView.22")); //$NON-NLS-1$
		add(lblPhone, "cell 0 6,alignx trailing"); //$NON-NLS-1$
		
		tfTelephone = new POSTextField();
		add(tfTelephone, "cell 1 6 2 1,growx"); //$NON-NLS-1$
		
		JSeparator separator = new JSeparator();
		add(separator, "cell 0 8 4 1,growx"); //$NON-NLS-1$
		
		JLabel lblCapacity = new JLabel(Messages.getString("RestaurantConfigurationView.26")); //$NON-NLS-1$
		add(lblCapacity, "cell 0 9,alignx trailing"); //$NON-NLS-1$
		
		tfCapacity = new POSTextField();
		add(tfCapacity, "cell 1 9,growx"); //$NON-NLS-1$
		
		tfTable = new POSTextField();
		add(tfTable, "cell 3 9,growx"); //$NON-NLS-1$
		
		JLabel lblCurrencyName = new JLabel(Messages.getString("RestaurantConfigurationView.30") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblCurrencyName, "cell 0 10,alignx trailing"); //$NON-NLS-1$
		
		JLabel lblTables = new JLabel(Messages.getString("RestaurantConfigurationView.33") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblTables, "flowx,cell 2 9"); //$NON-NLS-1$
		
		tfCurrencyName = new POSTextField();
		add(tfCurrencyName, "flowx,cell 1 10"); //$NON-NLS-1$
		
		JLabel lblCurrencySymbol = new JLabel(Messages.getString("RestaurantConfigurationView.37") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblCurrencySymbol, "cell 2 10,alignx trailing"); //$NON-NLS-1$
		
		tfCurrencySymbol = new POSTextField();
		add(tfCurrencySymbol, "cell 3 10,growx"); //$NON-NLS-1$
		
		JSeparator separator_1 = new JSeparator();
		add(separator_1, "cell 0 11 4 1,growx"); //$NON-NLS-1$
		
		JLabel lblServiceCharge = new JLabel(Messages.getString("RestaurantConfigurationView.42") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblServiceCharge, "cell 0 12,alignx trailing"); //$NON-NLS-1$
		
		tfServiceCharge = new POSTextField();
		add(tfServiceCharge, "cell 1 12,growx"); //$NON-NLS-1$
		
		JLabel label = new JLabel("%"); //$NON-NLS-1$
		add(label, "cell 2 12"); //$NON-NLS-1$
		
		JLabel lblDefaultGratuity = new JLabel(Messages.getString("RestaurantConfigurationView.48") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblDefaultGratuity, "flowy,cell 0 13,alignx trailing"); //$NON-NLS-1$
		
		tfDefaultGratuity = new POSTextField();
		add(tfDefaultGratuity, "cell 1 13,growx"); //$NON-NLS-1$
		
		JLabel label_1 = new JLabel("%"); //$NON-NLS-1$
		add(label_1, "cell 2 13"); //$NON-NLS-1$
		
		JLabel lblTicketFooterMessage = new JLabel(Messages.getString("RestaurantConfigurationView.54") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		add(lblTicketFooterMessage, "cell 0 14,alignx trailing"); //$NON-NLS-1$
		
		tfTicketFooter = new POSTextField();
		add(tfTicketFooter, "cell 1 14 3 1,growx"); //$NON-NLS-1$
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
			currencySymbol = "$"; //$NON-NLS-1$
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
		tfCapacity.setText(String.valueOf(restaurant.getCapacity()));
		tfTable.setText(String.valueOf(restaurant.getTables()));
		tfCurrencyName.setText(restaurant.getCurrencyName());
		tfCurrencySymbol.setText(restaurant.getCurrencySymbol());
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
