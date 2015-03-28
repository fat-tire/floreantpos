package com.floreantpos.config.ui;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.OrderTypeProperties;
import com.floreantpos.model.dao.OrderTypePropertiesDAO;
import com.floreantpos.swing.FixedLengthTextField;
import com.jidesoft.swing.TitledSeparator;

public class OrderTypeConfigurationView extends ConfigurationView {
	private FixedLengthTextField tfDriveThruAlias = new FixedLengthTextField(40);
	private FixedLengthTextField tfBarTabAlias = new FixedLengthTextField(40);
	private FixedLengthTextField tfDineInAlias = new FixedLengthTextField(40);
	private FixedLengthTextField tfTakeOutAlias = new FixedLengthTextField(40);
	private FixedLengthTextField tfPickupAlias = new FixedLengthTextField(40);
	private FixedLengthTextField tfHomeDeliveryAlias = new FixedLengthTextField(40);
	
	private JCheckBox cbDineInEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);
	private JCheckBox cbTakeOutEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);
	private JCheckBox cbPickupEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);
	private JCheckBox cbHomeDeliveryEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);
	private JCheckBox cbDriveThruEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);
	private JCheckBox cbBarTabEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);
	
	private JCheckBox cbDineInDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);
	private JCheckBox cbTakeOutDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);
	private JCheckBox cbPickupDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);
	private JCheckBox cbHomeDeliveryDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);
	private JCheckBox cbDriveThruDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);
	private JCheckBox cbBarTabDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);
	
	public OrderTypeConfigurationView() {
		setLayout(new MigLayout("", "", ""));
		
		addOption(OrderType.DINE_IN, cbDineInEnable, cbDineInDelayPay, tfDineInAlias);
		addOption(OrderType.TAKE_OUT, cbTakeOutEnable, cbTakeOutDelayPay, tfTakeOutAlias);
		addOption(OrderType.PICKUP, cbPickupEnable, cbPickupDelayPay, tfPickupAlias);
		addOption(OrderType.HOME_DELIVERY, cbHomeDeliveryEnable, cbHomeDeliveryDelayPay, tfHomeDeliveryAlias);
		addOption(OrderType.DRIVE_THRU, cbDriveThruEnable, cbDriveThruDelayPay, tfDriveThruAlias);
		addOption(OrderType.BAR_TAB, cbBarTabEnable, cbBarTabDelayPay, tfBarTabAlias);
	}
	
	private void addOption(OrderType orderType, JCheckBox cbEnable, JCheckBox cbLaterPay, JTextField tfAlias) {
		TitledSeparator separator = new TitledSeparator(orderType.name().replaceAll("_", " "));
		add(separator, "gaptop 30, newline, span 5, grow, wrap");
		add(cbEnable, "gapright 25");
		add(cbLaterPay, "gapright 25");
		add(new JLabel(POSConstants.ALIAS_LABEL));
		add(tfAlias);
	}

	@Override
	public boolean save() throws Exception {
		setupModel(OrderType.DINE_IN, cbDineInEnable, cbDineInDelayPay, tfDineInAlias);
		setupModel(OrderType.TAKE_OUT, cbTakeOutEnable, cbTakeOutDelayPay, tfTakeOutAlias);
		setupModel(OrderType.PICKUP, cbPickupEnable, cbPickupDelayPay, tfPickupAlias);
		setupModel(OrderType.HOME_DELIVERY, cbHomeDeliveryEnable, cbHomeDeliveryDelayPay, tfHomeDeliveryAlias);
		setupModel(OrderType.DRIVE_THRU, cbDriveThruEnable, cbDriveThruDelayPay, tfDriveThruAlias);
		setupModel(OrderType.BAR_TAB, cbBarTabEnable, cbBarTabDelayPay, tfBarTabAlias);
		
		OrderTypePropertiesDAO.getInstance().saveAll();
		
		return true;
	}

	@Override
	public void initialize() throws Exception {
		setupView(OrderType.DINE_IN, cbDineInEnable, cbDineInDelayPay, tfDineInAlias);
		setupView(OrderType.TAKE_OUT, cbTakeOutEnable, cbTakeOutDelayPay, tfTakeOutAlias);
		setupView(OrderType.PICKUP, cbPickupEnable, cbPickupDelayPay, tfPickupAlias);
		setupView(OrderType.HOME_DELIVERY, cbHomeDeliveryEnable, cbHomeDeliveryDelayPay, tfHomeDeliveryAlias);
		setupView(OrderType.DRIVE_THRU, cbDriveThruEnable, cbDriveThruDelayPay, tfDriveThruAlias);
		setupView(OrderType.BAR_TAB, cbBarTabEnable, cbBarTabDelayPay, tfBarTabAlias);
		
		setInitialized(true);
	}
	
	private void setupView(OrderType orderType, JCheckBox checkBox, JCheckBox cbPayLater, JTextField textField) {
		OrderTypeProperties properties = orderType.getProperties();
		if(properties == null) {
			checkBox.setSelected(true);
			return;
		}
		
		checkBox.setSelected(properties.isVisible());
		cbPayLater.setSelected(properties.isAllowDelayPayment());
		textField.setText(properties.getAlias());
	}
	
	private void setupModel(OrderType orderType, JCheckBox checkBox, JCheckBox cbPayLater, JTextField textField) {
		OrderTypeProperties orderTypeProperties = orderType.getProperties();
		if(orderTypeProperties == null) {
			orderTypeProperties = new OrderTypeProperties();
			orderType.setProperties(orderTypeProperties);
		}
		
		orderTypeProperties.setOrdetType(orderType.name());
		orderTypeProperties.setVisible(checkBox.isSelected());
		orderTypeProperties.setAllowDelayPayment(cbPayLater.isSelected());
		orderTypeProperties.setAlias(textField.getText());
	}

	@Override
	public String getName() {
		return "Order";
	}

}
