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
package com.floreantpos.ui.model;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.MenuItem;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class MenuItemPriceByOrderTypeDialog extends POSDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JComboBox cbOrderTypes;
	private JComboBox cbTax;
	private JTextField tfPrice;
	private String key;

	private MenuItem menuItem;

	public MenuItemPriceByOrderTypeDialog(MenuItem item) {
		this.menuItem = item;
		init();
	}

	public MenuItemPriceByOrderTypeDialog(MenuItem item, String key) {
		this.menuItem = item;
		this.key = key;
		init();
	}

	private void init() {
		createView();
		cbOrderTypes.addItem(OrderType.DINE_IN.toString());
		cbOrderTypes.addItem(OrderType.BAR_TAB.toString());
		cbOrderTypes.addItem(OrderType.DRIVE_THRU.toString());
		cbOrderTypes.addItem(OrderType.HOME_DELIVERY.toString());
		cbOrderTypes.addItem(OrderType.PICKUP.toString());
		cbOrderTypes.addItem(OrderType.TAKE_OUT.toString());
		cbOrderTypes.addItem(OrderType.FOR_HERE.toString());
		cbOrderTypes.addItem(OrderType.RETAIL.toString());

		TaxDAO.getInstance().findAll();

		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});

		//call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		//call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		setMenuItem(menuItem);
	}

	private void onOK() {
		if (!updateModel())
			return;

		try {
			MenuItemDAO dao = new MenuItemDAO();
			dao.saveOrUpdate(menuItem);
			setCanceled(false);
			dispose();
		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void onCancel() {
		setCanceled(true);
		dispose();
	}

	private void updateView() {
		if (menuItem == null)
			return;
		String modifiedKey = key;

		if (modifiedKey != null) {
			modifiedKey = modifiedKey.replaceAll("_PRICE", "");
			modifiedKey = modifiedKey.replaceAll("_", " ");
			cbOrderTypes.setSelectedItem(modifiedKey);
			tfPrice.setText(String.valueOf(menuItem.getProperties().get(key)));
		}
	}

	public boolean updateModel() {
		double price = 0;
		try {
			price = Double.parseDouble(tfPrice.getText());
		} catch (Exception x) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.PRICE_IS_NOT_VALID_);
			return false;
		}
		menuItem.setPriceByOrderType(cbOrderTypes.getSelectedItem().toString(), price);
		return true;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;

		updateView();
	}

	private void createView() {
		contentPane = new JPanel(new BorderLayout());
		final JLabel label1 = new JLabel();
		label1.setText("Select order type:"); //$NON-NLS-1$
		cbOrderTypes = new JComboBox();

		final JLabel label3 = new JLabel();
		label3.setText("Select tax:"); //$NON-NLS-1$
		cbTax = new JComboBox();

		final JLabel label2 = new JLabel();
		label2.setText(com.floreantpos.POSConstants.PRICE + ":"); //$NON-NLS-1$
		tfPrice = new JTextField();

		JPanel panel = new JPanel(new MigLayout("", "grow", ""));

		panel.add(label1);
		panel.add(cbOrderTypes, "grow,wrap");
		panel.add(label3);
		panel.add(cbTax, "grow,wrap");
		panel.add(label2);
		panel.add(tfPrice, "grow");

		contentPane.add(panel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new MigLayout("al center center", "sg", ""));
		buttonOK = new JButton("Ok");
		buttonCancel = new JButton("Cancel");

		buttonPanel.add(buttonOK, "grow");
		buttonPanel.add(buttonCancel, "grow");
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		add(contentPane);
	}
}
