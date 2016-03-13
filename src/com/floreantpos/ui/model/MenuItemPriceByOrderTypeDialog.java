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
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class MenuItemPriceByOrderTypeDialog extends POSDialog {
	private JPanel contentPane;
	private JButton btnOK;
	private JButton btnCancel;
	private JComboBox cbOrderTypes;
	private JComboBox cbTax;
	private JTextField tfPrice;
	private String key;

	private MenuItem menuItem;

	public MenuItemPriceByOrderTypeDialog(Frame owner, MenuItem item) {
		super(owner, true);
		this.menuItem = item;
		init();
	}

	public MenuItemPriceByOrderTypeDialog(Frame owner, MenuItem item, String key) {
		super(owner, true);
		this.menuItem = item;
		this.key = key;
		init();
	}

	private void init() {
		createView();
		List<OrderType> orderTypes = Application.getInstance().getOrderTypes();
		if (orderTypes != null) {
			for (OrderType orderType : orderTypes) {
				cbOrderTypes.addItem(orderType.getName());
			}
			cbOrderTypes.addItem(OrderType.FOR_HERE);
			cbOrderTypes.addItem(OrderType.TO_GO);
		}

		setModal(true);
		getRootPane().setDefaultButton(btnOK);

		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
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

		if (key != null) {
			cbOrderTypes.setSelectedItem(menuItem.getStringWithOutUnderScore(key, "_PRICE"));

			Tax newtax = TaxDAO.getInstance().findByTaxRate(Double.parseDouble(menuItem.getProperty(menuItem.replaceString(key, "_PRICE", "_TAX"))));

			cbTax.setSelectedItem(newtax);

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

		Tax tax = (Tax) cbTax.getSelectedItem();
		if (cbOrderTypes.getSelectedItem() == null) {
			return false;
		}
		menuItem.setTaxByOrderType(cbOrderTypes.getSelectedItem().toString(), tax.getRate());
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
		label1.setText(Messages.getString("MenuItemPriceByOrderTypeDialog.6")); //$NON-NLS-1$
		cbOrderTypes = new JComboBox();

		final JLabel label3 = new JLabel();
		label3.setText(Messages.getString("MenuItemPriceByOrderTypeDialog.7")); //$NON-NLS-1$
		cbTax = new JComboBox(new DefaultComboBoxModel(TaxDAO.getInstance().findAll().toArray()));

		final JLabel label2 = new JLabel();
		label2.setText(com.floreantpos.POSConstants.PRICE + ":"); //$NON-NLS-1$
		tfPrice = new JTextField();

		JPanel panel = new JPanel(new MigLayout("", "grow", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		panel.add(label1, "right"); //$NON-NLS-1$
		panel.add(cbOrderTypes, "grow,wrap"); //$NON-NLS-1$
		panel.add(label2, "right"); //$NON-NLS-1$
		panel.add(tfPrice, "grow,wrap"); //$NON-NLS-1$
		panel.add(label3, "right"); //$NON-NLS-1$
		panel.add(cbTax, "grow"); //$NON-NLS-1$

		contentPane.add(panel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new MigLayout("al center center", "sg", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		btnOK = new JButton(Messages.getString("MenuItemPriceByOrderTypeDialog.0")); //$NON-NLS-1$
		btnCancel = new JButton(Messages.getString("MenuItemPriceByOrderTypeDialog.21")); //$NON-NLS-1$

		buttonPanel.add(btnOK, "grow"); //$NON-NLS-1$
		buttonPanel.add(btnCancel, "grow"); //$NON-NLS-1$
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		add(contentPane);
	}
}
