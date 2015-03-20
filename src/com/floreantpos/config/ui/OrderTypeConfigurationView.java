package com.floreantpos.config.ui;
import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;

import com.floreantpos.model.OrderType;
import com.floreantpos.model.OrderTypeProperties;
import com.floreantpos.model.dao.OrderTypePropertiesDAO;
import com.floreantpos.swing.FixedLengthTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class OrderTypeConfigurationView extends ConfigurationView {
	private FixedLengthTextField tfDriveThruAlias;
	private FixedLengthTextField tfBarTabAlias;
	private FixedLengthTextField tfDineInAlias;
	private FixedLengthTextField tfTakeOutAlias;
	private FixedLengthTextField tfPickupAlias;
	private FixedLengthTextField tfHomeDeliveryAlias;
	private JCheckBox cbPickup;
	private JCheckBox cbTakeOut;
	private JCheckBox cbDineIn;
	private JCheckBox cbHomeDelivery;
	private JCheckBox cbDriveThru;
	private JCheckBox cbBarTab;
	
	public OrderTypeConfigurationView() {
		setLayout(new MigLayout("", "[grow]", ""));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "ORDER TYPE", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("gap 20px", "[][][][][][]", "[]"));
		
		cbDineIn = new JCheckBox("DINE IN");
		panel.add(cbDineIn, "cell 0 0");
		
		cbTakeOut = new JCheckBox("TAKE OUT");
		panel.add(cbTakeOut, "cell 1 0");
		
		cbPickup = new JCheckBox("PICKUP");
		panel.add(cbPickup, "cell 2 0");
		
		cbHomeDelivery = new JCheckBox("HOME DELIVERY");
		panel.add(cbHomeDelivery, "cell 3 0");
		
		cbDriveThru = new JCheckBox("DRIVE THRU");
		panel.add(cbDriveThru, "cell 4 0");
		
		cbBarTab = new JCheckBox("BAR TAB");
		panel.add(cbBarTab, "cell 5 0");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "ALIAS", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel_1, "cell 0 1,grow");
		panel_1.setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));
		
		JLabel lblDineIn = new JLabel("DINE IN");
		panel_1.add(lblDineIn, "cell 0 0,alignx trailing");
		
		tfDineInAlias = new FixedLengthTextField(40);
		panel_1.add(tfDineInAlias, "cell 1 0,growx");
		
		JLabel lblTakeOut = new JLabel("TAKE OUT");
		panel_1.add(lblTakeOut, "cell 0 1,alignx trailing");
		
		tfTakeOutAlias = new FixedLengthTextField(40);
		panel_1.add(tfTakeOutAlias, "cell 1 1,growx");
		
		JLabel lblPickup = new JLabel("PICKUP");
		panel_1.add(lblPickup, "cell 0 2,alignx trailing");
		
		tfPickupAlias = new FixedLengthTextField(40);
		panel_1.add(tfPickupAlias, "cell 1 2,growx");
		
		JLabel lblHomeDelivery = new JLabel("HOME DELIVERY");
		panel_1.add(lblHomeDelivery, "cell 0 3,alignx trailing");
		
		tfHomeDeliveryAlias = new FixedLengthTextField(40);
		panel_1.add(tfHomeDeliveryAlias, "cell 1 3,growx");
		
		JLabel lblDriveThru = new JLabel("DRIVE THRU");
		panel_1.add(lblDriveThru, "cell 0 4,alignx trailing");
		
		tfDriveThruAlias = new FixedLengthTextField(40);
		panel_1.add(tfDriveThruAlias, "cell 1 4,growx");
		
		JLabel lblBarTab = new JLabel("BAR TAB");
		panel_1.add(lblBarTab, "cell 0 5,alignx trailing");
		
		tfBarTabAlias = new FixedLengthTextField(40);
		panel_1.add(tfBarTabAlias, "cell 1 5,growx");
	}

	@Override
	public boolean save() throws Exception {
		setupModel(OrderType.DINE_IN, cbDineIn, tfDineInAlias);
		setupModel(OrderType.TAKE_OUT, cbTakeOut, tfTakeOutAlias);
		setupModel(OrderType.PICKUP, cbPickup, tfPickupAlias);
		setupModel(OrderType.HOME_DELIVERY, cbHomeDelivery, tfHomeDeliveryAlias);
		setupModel(OrderType.DRIVE_THRU, cbDriveThru, tfDriveThruAlias);
		setupModel(OrderType.BAR_TAB, cbBarTab, tfBarTabAlias);
		
		OrderTypePropertiesDAO.getInstance().saveAll();
		
		return true;
	}

	@Override
	public void initialize() throws Exception {
		setupView(OrderType.DINE_IN, cbDineIn, tfDineInAlias);
		setupView(OrderType.TAKE_OUT, cbTakeOut, tfTakeOutAlias);
		setupView(OrderType.PICKUP, cbPickup, tfPickupAlias);
		setupView(OrderType.HOME_DELIVERY, cbHomeDelivery, tfHomeDeliveryAlias);
		setupView(OrderType.DRIVE_THRU, cbDriveThru, tfDriveThruAlias);
		setupView(OrderType.BAR_TAB, cbBarTab, tfBarTabAlias);
		
		setInitialized(true);
	}
	
	private void setupView(OrderType orderType, JCheckBox checkBox, JTextField textField) {
		OrderTypeProperties properties = orderType.getProperties();
		if(properties == null) {
			checkBox.setSelected(true);
			return;
		}
		
		checkBox.setSelected(properties.isVisible());
		textField.setText(properties.getAlias());
	}
	
	private void setupModel(OrderType orderType, JCheckBox checkBox, JTextField textField) {
		OrderTypeProperties orderTypeProperties = orderType.getProperties();
		if(orderTypeProperties == null) {
			orderTypeProperties = new OrderTypeProperties();
			orderType.setProperties(orderTypeProperties);
		}
		
		orderTypeProperties.setOrdetType(orderType.name());
		orderTypeProperties.setVisible(checkBox.isSelected());
		orderTypeProperties.setAlias(textField.getText());
	}

	@Override
	public String getName() {
		return "Order";
	}

}
