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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.model.OrderTypeProperties;
import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.model.dao.OrderTypePropertiesDAO;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.views.LoginView;
import com.floreantpos.ui.views.order.OrderType;

public class OrderTypeConfigurationView extends ConfigurationView {
	private FixedLengthTextField tfDriveThruAlias = new FixedLengthTextField(40);
	private FixedLengthTextField tfBarTabAlias = new FixedLengthTextField(40);
	private FixedLengthTextField tfForHereAlias = new FixedLengthTextField(40);
	private FixedLengthTextField tfDineInAlias = new FixedLengthTextField(40);
	private FixedLengthTextField tfTakeOutAlias = new FixedLengthTextField(40);
	private FixedLengthTextField tfPickupAlias = new FixedLengthTextField(40);
	private FixedLengthTextField tfHomeDeliveryAlias = new FixedLengthTextField(40);

	private JCheckBox cbDineInEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);
	private JCheckBox cbTakeOutEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);
	private JCheckBox cbForHereEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);
	private JCheckBox cbPickupEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);
	private JCheckBox cbHomeDeliveryEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);
	private JCheckBox cbDriveThruEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);
	private JCheckBox cbBarTabEnable = new JCheckBox(POSConstants.ENABLE_OPTION_LABEL);

	private JCheckBox cbDineInDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);
	private JCheckBox cbTakeOutDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);
	private JCheckBox cbForHereDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);
	private JCheckBox cbPickupDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);
	private JCheckBox cbHomeDeliveryDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);
	private JCheckBox cbDriveThruDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);
	private JCheckBox cbBarTabDelayPay = new JCheckBox(POSConstants.LATER_PAYMENT_OPTION_LABEL);

	private JCheckBox cbShowTableSelection = new JCheckBox(Messages.getString("OrderTypeConfigurationView.0")); //$NON-NLS-1$
	private JCheckBox cbShowGuestSelection = new JCheckBox(Messages.getString("OrderTypeConfigurationView.1")); //$NON-NLS-1$

	private JComboBox<VirtualPrinter> cbDineInPrinter = new JComboBox<VirtualPrinter>();
	private JComboBox<VirtualPrinter> cbTakeoutPrinter = new JComboBox<VirtualPrinter>();
	private JComboBox<VirtualPrinter> cbForHerePrinter = new JComboBox<VirtualPrinter>();
	private JComboBox<VirtualPrinter> cbPickupPrinter = new JComboBox<VirtualPrinter>();
	private JComboBox<VirtualPrinter> cbHomeDeliPrinter = new JComboBox<VirtualPrinter>();
	private JComboBox<VirtualPrinter> cbDriveThruPrinter = new JComboBox<VirtualPrinter>();
	private JComboBox<VirtualPrinter> cbBarPrinter = new JComboBox<VirtualPrinter>();

	private JCheckBox cbConsolidateTicketItems = new JCheckBox(Messages.getString("OrderTypeConfigurationView.2")); //$NON-NLS-1$

	public OrderTypeConfigurationView() {
		setLayout(new MigLayout("", "", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		if (TerminalConfig.isMultipleOrderSupported()) {
			addOption(OrderType.DINE_IN, cbDineInEnable, cbDineInDelayPay, tfDineInAlias, cbDineInPrinter);
			addOption(OrderType.TAKE_OUT, cbTakeOutEnable, cbTakeOutDelayPay, tfTakeOutAlias, cbTakeoutPrinter);
			addOption(OrderType.FOR_HERE, cbForHereEnable, cbForHereDelayPay, tfForHereAlias, cbForHerePrinter);

			OrderServiceExtension orderServiceExtension = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);
			if (orderServiceExtension != null) {
				addOption(OrderType.PICKUP, cbPickupEnable, cbPickupDelayPay, tfPickupAlias, cbPickupPrinter);
				addOption(OrderType.HOME_DELIVERY, cbHomeDeliveryEnable, cbHomeDeliveryDelayPay, tfHomeDeliveryAlias, cbHomeDeliPrinter);
				addOption(OrderType.DRIVE_THRU, cbDriveThruEnable, cbDriveThruDelayPay, tfDriveThruAlias, cbDriveThruPrinter);
			}
			addOption(OrderType.BAR_TAB, cbBarTabEnable, cbBarTabDelayPay, tfBarTabAlias, cbBarPrinter);

			add(cbConsolidateTicketItems, "newline,gapleft 10, gaptop 2"); //$NON-NLS-1$

			cbDineInEnable.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = cbDineInEnable.isSelected();
					cbShowTableSelection.setEnabled(selected);
					cbShowGuestSelection.setEnabled(selected);
				}
			});
		}
		else {
			addOption(OrderType.TAKE_OUT, cbTakeOutEnable, cbTakeOutDelayPay, tfTakeOutAlias, cbTakeoutPrinter);
		}

	}

	private void addOption(OrderType orderType, JCheckBox cbEnable, JCheckBox cbPostPaid, JTextField tfAlias, JComboBox<VirtualPrinter> cbPrinter) {
		JPanel panel = new JPanel(new MigLayout("fill", "grow", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		panel.setBorder(BorderFactory.createTitledBorder(orderType.name().replaceAll("_", " "))); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(cbEnable, "gapright 25"); //$NON-NLS-1$
		//panel.add(cbPostPaid, "gapright 25"); //$NON-NLS-1$
		panel.add(new JLabel(POSConstants.ALIAS_LABEL));
		panel.add(tfAlias);
		//		panel.add(new JLabel("Printer"), "newline");
		//		panel.add(cbPrinter, "span 3, grow");

		if (orderType == OrderType.DINE_IN) {
			panel.add(cbShowTableSelection, "span 2, newline, wrap"); //$NON-NLS-1$
			panel.add(cbShowGuestSelection, "span 2, wrap"); //$NON-NLS-1$
		}

		add(panel, "newline, grow"); //$NON-NLS-1$
	}

	@Override
	public boolean save() throws Exception {
		setupModel(OrderType.DINE_IN, cbDineInEnable, cbDineInDelayPay, tfDineInAlias, cbDineInPrinter);
		setupModel(OrderType.TAKE_OUT, cbTakeOutEnable, cbTakeOutDelayPay, tfTakeOutAlias, cbTakeoutPrinter);
		setupModel(OrderType.FOR_HERE, cbForHereEnable, cbForHereDelayPay, tfForHereAlias, cbForHerePrinter);
		setupModel(OrderType.PICKUP, cbPickupEnable, cbPickupDelayPay, tfPickupAlias, cbPickupPrinter);
		setupModel(OrderType.HOME_DELIVERY, cbHomeDeliveryEnable, cbHomeDeliveryDelayPay, tfHomeDeliveryAlias, cbHomeDeliPrinter);
		setupModel(OrderType.DRIVE_THRU, cbDriveThruEnable, cbDriveThruDelayPay, tfDriveThruAlias, cbDriveThruPrinter);
		setupModel(OrderType.BAR_TAB, cbBarTabEnable, cbBarTabDelayPay, tfBarTabAlias, cbBarPrinter);

		OrderTypePropertiesDAO.getInstance().saveAll();

		TerminalConfig.setShouldShowTableSelection(cbShowTableSelection.isSelected());
		TerminalConfig.setShouldShowGuestSelection(cbShowGuestSelection.isSelected());
		TerminalConfig.setConsolidateTicketItems(cbConsolidateTicketItems.isSelected());

		LoginView.getInstance().initializeOrderButtonPanel();

		return true;
	}

	@Override
	public void initialize() throws Exception {
		//		List<VirtualPrinter> virtualPrinters = VirtualPrinterDAO.getInstance().findAll();
		//
		//		cbDineInPrinter.setModel(new ListComboBoxModel(virtualPrinters));
		//		cbTakeoutPrinter.setModel(new ListComboBoxModel(virtualPrinters));
		//		cbPickupPrinter.setModel(new ListComboBoxModel(virtualPrinters));
		//		cbHomeDeliPrinter.setModel(new ListComboBoxModel(virtualPrinters));
		//		cbDriveThruPrinter.setModel(new ListComboBoxModel(virtualPrinters));
		//		cbBarPrinter.setModel(new ListComboBoxModel(virtualPrinters));

		setupView(OrderType.DINE_IN, cbDineInEnable, cbDineInDelayPay, tfDineInAlias, cbDineInPrinter);
		setupView(OrderType.TAKE_OUT, cbTakeOutEnable, cbTakeOutDelayPay, tfTakeOutAlias, cbTakeoutPrinter);
		setupView(OrderType.FOR_HERE, cbForHereEnable, cbForHereDelayPay, tfForHereAlias, cbForHerePrinter);
		setupView(OrderType.PICKUP, cbPickupEnable, cbPickupDelayPay, tfPickupAlias, cbPickupPrinter);
		setupView(OrderType.HOME_DELIVERY, cbHomeDeliveryEnable, cbHomeDeliveryDelayPay, tfHomeDeliveryAlias, cbHomeDeliPrinter);
		setupView(OrderType.DRIVE_THRU, cbDriveThruEnable, cbDriveThruDelayPay, tfDriveThruAlias, cbDriveThruPrinter);
		setupView(OrderType.BAR_TAB, cbBarTabEnable, cbBarTabDelayPay, tfBarTabAlias, cbBarPrinter);
		
		cbShowTableSelection.setSelected(TerminalConfig.isShouldShowTableSelection());
		cbShowGuestSelection.setSelected(TerminalConfig.isShouldShowGuestSelection());
		cbConsolidateTicketItems.setSelected(TerminalConfig.isConsolidateTicketItems());

		setInitialized(true);
	}

	private void setupView(OrderType orderType, JCheckBox checkBox, JCheckBox cbPayLater, JTextField textField, JComboBox<VirtualPrinter> cbPrinter) {
		OrderTypeProperties properties = orderType.getProperties();
		if (properties == null) {
			checkBox.setSelected(true);
			cbPayLater.setSelected(true);
			return;
		}

		checkBox.setSelected(properties.isVisible());
		cbPayLater.setSelected(properties.isPostPaid());
		textField.setText(properties.getAlias());
	}

	private void setupModel(OrderType orderType, JCheckBox checkBox, JCheckBox cbPayLater, JTextField textField, JComboBox<VirtualPrinter> cbPrinter) {
		OrderTypeProperties orderTypeProperties = orderType.getProperties();
		if (orderTypeProperties == null) {
			orderTypeProperties = new OrderTypeProperties();
			orderType.setProperties(orderTypeProperties);
		}

		orderTypeProperties.setOrdetType(orderType.name());
		orderTypeProperties.setVisible(checkBox.isSelected());
		orderTypeProperties.setPostPaid(cbPayLater.isSelected());
		orderTypeProperties.setAlias(textField.getText());
	}

	@Override
	public String getName() {
		return "Order"; //$NON-NLS-1$
	}

}
