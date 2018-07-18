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
 * CategoryBeanEditor.java
 *
 * Created on July 30, 2006, 11:20 PM
 */

package com.floreantpos.ui.model;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.dao.OrderTypeDAO;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author  MShahriar
 */
public class OrderTypeForm extends BeanEditor implements ItemListener {

	private JLabel jLabel1;
	private FixedLengthTextField tfName;
	private JCheckBox chkEnabled;
	private JCheckBox chkShowTableSelection;
	private JCheckBox chkShowGuestSelection;
	private JCheckBox chkShouldPrintToKitchen;
	private JCheckBox chkCloseOnPaid;
	private JCheckBox chkPrepaid;
	private JCheckBox chkDelivery;
	private JCheckBox chkRequiredCustomerData;
	private JCheckBox chkShowItemBarcode;
	private JCheckBox chkShowInLoginScreen;
	private JCheckBox chkConsolidateItemsInReceipt;
	private JCheckBox chkAllowSeatBasedOrder;
	private JCheckBox chkHideItemWithEmptyInventory;
	private JCheckBox chkHasForHereAndToGo;
	private JCheckBox chkBarTab;
	private JCheckBox chkPreAuthCreditCard;
	private JCheckBox chkShowPriceOnButton;
	private JCheckBox chkShowStockCountOnButton;
	private JCheckBox chkShowUnitPriceInTicketGrid;
	private JCheckBox chkRetailOrder;
	private JCheckBox chkAllowToAddTipsLater;

	OrderType orderType;
	JList<String> list;
	DefaultListModel<String> listModel;

	public OrderTypeForm() throws Exception {
		this(new OrderType());
		initHandler();
	}

	public OrderTypeForm(OrderType orderType) throws Exception {
		this.orderType = orderType;
		initComponents();

		setBean(orderType);
		initHandler();
	}

	public String getDisplayText() {
		OrderType orderType = (OrderType) getBean();
		if (orderType.getId() == null) {
			return POSConstants.ORDER_TYPE;
		}
		return POSConstants.ORDER_TYPE;
	}

	private void initHandler() {
		chkRequiredCustomerData.addItemListener(this);
		chkDelivery.addItemListener(this);
	}

	private void initComponents() {

		TransparentPanel generalPanel = new com.floreantpos.swing.TransparentPanel();

		jLabel1 = new JLabel(com.floreantpos.POSConstants.NAME + ":"); //$NON-NLS-1$
		tfName = new com.floreantpos.swing.FixedLengthTextField();
		tfName.setLength(120);

		chkEnabled = new JCheckBox(POSConstants.ENABLED);
		chkShowTableSelection = new JCheckBox(Messages.getString("OrderTypeForm.1")); //$NON-NLS-1$
		chkShowGuestSelection = new JCheckBox(Messages.getString("OrderTypeForm.2")); //$NON-NLS-1$
		chkShouldPrintToKitchen = new JCheckBox(Messages.getString("OrderTypeForm.3")); //$NON-NLS-1$
		chkCloseOnPaid = new JCheckBox(Messages.getString("OrderTypeForm.4")); //$NON-NLS-1$
		chkPrepaid = new JCheckBox(Messages.getString("OrderTypeForm.5")); //$NON-NLS-1$
		chkDelivery = new JCheckBox("Delivery"); //$NON-NLS-1$
		chkRequiredCustomerData = new JCheckBox(Messages.getString("OrderTypeForm.6")); //$NON-NLS-1$
		chkShowItemBarcode = new JCheckBox(Messages.getString("OrderTypeForm.9")); //$NON-NLS-1$
		chkShowInLoginScreen = new JCheckBox(Messages.getString("OrderTypeForm.10")); //$NON-NLS-1$
		chkConsolidateItemsInReceipt = new JCheckBox(Messages.getString("OrderTypeForm.11")); //$NON-NLS-1$
		chkAllowSeatBasedOrder = new JCheckBox("Allow seat based order"); //$NON-NLS-1$
		chkHideItemWithEmptyInventory = new JCheckBox(Messages.getString("OrderTypeForm.12")); //$NON-NLS-1$
		chkHasForHereAndToGo = new JCheckBox(Messages.getString("OrderTypeForm.13")); //$NON-NLS-1$
		chkBarTab = new JCheckBox(Messages.getString("OrderTypeForm.14")); //$NON-NLS-1$
		chkPreAuthCreditCard = new JCheckBox(Messages.getString("OrderTypeForm.0")); //$NON-NLS-1$

		chkShowPriceOnButton = new JCheckBox("Show price on button");
		chkShowStockCountOnButton = new JCheckBox("Show count on button");
		chkShowUnitPriceInTicketGrid = new JCheckBox("Show unit price in ticket grid");
		chkRetailOrder = new JCheckBox("Retail");
		chkAllowToAddTipsLater = new JCheckBox("Allow to add tips later");

		generalPanel.setLayout(new MigLayout("", "[87px][327px,grow]", "[19px][][19px][][][21px][15px]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		generalPanel.add(jLabel1, "cell 0 0,alignx left,aligny center"); //$NON-NLS-1$
		generalPanel.add(tfName, "cell 1 0,growx,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkEnabled, "cell 1 1,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkShowTableSelection, "cell 1 2,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkShowGuestSelection, "cell 1 3,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkShouldPrintToKitchen, "cell 1 4,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkPrepaid, "cell 1 5,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkCloseOnPaid, "cell 1 6,alignx left,aligny top"); //$NON-NLS-1$
		OrderServiceExtension orderServiceExtension = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);
		generalPanel.add(chkRequiredCustomerData, "cell 1 7,alignx left,aligny top"); //$NON-NLS-1$
		if (orderServiceExtension != null) {
			generalPanel.add(chkDelivery, "cell 1 8,alignx left,aligny top"); //$NON-NLS-1$
		}
		generalPanel.add(chkShowItemBarcode, "cell 1 10,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkShowInLoginScreen, "cell 1 11,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkConsolidateItemsInReceipt, "cell 1 12,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkAllowSeatBasedOrder, "cell 1 13,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkHideItemWithEmptyInventory, "cell 1 14,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkHasForHereAndToGo, "cell 1 15,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkBarTab, "cell 1 16,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkPreAuthCreditCard, "cell 1 17,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkShowPriceOnButton, "cell 1 18,alignx left,aligny top,wrap"); //$NON-NLS-1$
		generalPanel.add(chkShowStockCountOnButton, "cell 1 19,alignx left,aligny top");
		//generalPanel.add(chkShowUnitPriceInTicketGrid, "cell 1 19,alignx left,aligny top"); //$NON-NLS-1$
		generalPanel.add(chkRetailOrder, "cell 1 20,alignx left,aligny top");
		generalPanel.add(chkAllowToAddTipsLater, "cell 1 21,alignx left,aligny top");

		add(new JScrollPane(generalPanel));
	}

	protected void updateView() {
		OrderType ordersType = (OrderType) getBean();

		if (ordersType == null) {
			tfName.setText(""); //$NON-NLS-1$
			chkEnabled.setSelected(false);
			return;
		}

		tfName.setText(ordersType.getName());
		if (ordersType.getId() == null) {
			chkEnabled.setSelected(true);
		}
		else {
			chkEnabled.setSelected(ordersType.isEnabled());
			chkShowTableSelection.setSelected(ordersType.isShowTableSelection());
			chkShowGuestSelection.setSelected(ordersType.isShowGuestSelection());
			chkShouldPrintToKitchen.setSelected(ordersType.isShouldPrintToKitchen());
			chkPrepaid.setSelected(ordersType.isPrepaid());
			chkCloseOnPaid.setSelected(ordersType.isCloseOnPaid());
			chkDelivery.setSelected(ordersType.isDelivery());
			chkRequiredCustomerData.setSelected(ordersType.isRequiredCustomerData());
			chkShowItemBarcode.setSelected(ordersType.isShowItemBarcode());
			chkShowInLoginScreen.setSelected(ordersType.isShowInLoginScreen());
			chkConsolidateItemsInReceipt.setSelected(ordersType.isConsolidateItemsInReceipt());
			chkAllowSeatBasedOrder.setSelected(ordersType.isAllowSeatBasedOrder());
			chkHideItemWithEmptyInventory.setSelected(ordersType.isHideItemWithEmptyInventory());
			chkHasForHereAndToGo.setSelected(ordersType.isHasForHereAndToGo());
			chkBarTab.setSelected(ordersType.isBarTab());
			chkPreAuthCreditCard.setSelected(ordersType.isPreAuthCreditCard());
			chkShowPriceOnButton.setSelected(orderType.isShowPriceOnButton());
			chkShowStockCountOnButton.setSelected(orderType.isShowStockCountOnButton());
			chkShowUnitPriceInTicketGrid.setSelected(orderType.isShowUnitPriceInTicketGrid());
			chkRetailOrder.setSelected(ordersType.isRetailOrder());
			chkAllowToAddTipsLater.setSelected(ordersType.isAllowToAddTipsLater());
		}
	}

	protected boolean updateModel() {
		OrderType ordersType = (OrderType) getBean();
		if (ordersType == null) {
			return false;
		}

		String categoryName = tfName.getText();
		if (POSUtil.isBlankOrNull(categoryName)) {
			MessageDialog.showError(Messages.getString("MenuCategoryForm.26")); //$NON-NLS-1$
			return false;
		}

		ordersType.setName(categoryName);
		ordersType.setEnabled(chkEnabled.isSelected());
		//if (!chkBarTab.isSelected()) {
		ordersType.setShowTableSelection(chkShowTableSelection.isSelected());
		ordersType.setShowGuestSelection(chkShowGuestSelection.isSelected());
		ordersType.setShouldPrintToKitchen(chkShouldPrintToKitchen.isSelected());
		ordersType.setPrepaid(chkPrepaid.isSelected());
		ordersType.setCloseOnPaid(chkCloseOnPaid.isSelected());
		ordersType.setDelivery(chkDelivery.isSelected());
		ordersType.setRequiredCustomerData(chkRequiredCustomerData.isSelected());
		ordersType.setShowItemBarcode(chkShowItemBarcode.isSelected());
		ordersType.setShowInLoginScreen(chkShowInLoginScreen.isSelected());
		ordersType.setConsolidateItemsInReceipt(chkConsolidateItemsInReceipt.isSelected());
		ordersType.setAllowSeatBasedOrder(chkAllowSeatBasedOrder.isSelected());
		ordersType.setHideItemWithEmptyInventory(chkHideItemWithEmptyInventory.isSelected());
		ordersType.setHasForHereAndToGo(chkHasForHereAndToGo.isSelected());
		ordersType.setPreAuthCreditCard(chkPreAuthCreditCard.isSelected());
		ordersType.setShowPriceOnButton(chkShowPriceOnButton.isSelected());
		ordersType.setShowStockCountOnButton(chkShowStockCountOnButton.isSelected());
		ordersType.setShowUnitPriceInTicketGrid(chkShowUnitPriceInTicketGrid.isSelected());
		ordersType.setBarTab(chkBarTab.isSelected());
		ordersType.setRetailOrder(chkRetailOrder.isSelected());
		ordersType.addProperty(OrderType.ALLOW_TO_ADD_TIPS_LATER, String.valueOf(chkAllowToAddTipsLater.isSelected()));

		return true;
	}

	@Override
	public boolean save() {
		try {

			if (!updateModel())
				return false;

			OrderType ordersType = (OrderType) getBean();
			OrderTypeDAO.getInstance().saveOrUpdate(ordersType);

			POSMessageDialog.showMessage(com.floreantpos.util.POSUtil.getFocusedWindow(), Messages.getString("TerminalConfigurationView.40")); //$NON-NLS-1$

			return true;

		} catch (Exception x) {
			MessageDialog.showError(x);
			return false;
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		JCheckBox chkBox = (JCheckBox) e.getItem();
		if (chkBox == chkDelivery) {
			if (chkDelivery.isSelected()) {
				chkRequiredCustomerData.setSelected(true);
				chkRequiredCustomerData.setEnabled(false);
			}
			else {
				chkRequiredCustomerData.setEnabled(true);
			}
		}
	}
}
