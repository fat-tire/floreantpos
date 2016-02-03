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
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class ModifierPriceByOrderTypeDialog extends POSDialog {
	private JPanel contentPane;
	private JButton btnOK;
	private JButton btnCancel;
	private JComboBox cbOrderTypes;
	private JComboBox cbTax;
	private JTextField tfPrice;
	private String key;

	private MenuModifier modifier;

	public ModifierPriceByOrderTypeDialog(MenuModifier modifier) {
		this.modifier = modifier;
		init();
	}

	public ModifierPriceByOrderTypeDialog(MenuModifier modifier, String key) {
		this.modifier = modifier;
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

		setMenuModifier(modifier);
	}

	private void onOK() {
		if (!updateModel())
			return;

		try {
			MenuModifierDAO dao = new MenuModifierDAO();
			dao.saveOrUpdate(modifier);
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
		if (modifier == null)
			return;

		String modifiedKey = key;

		if (modifiedKey != null) {
			modifiedKey = modifiedKey.replaceAll("_PRICE", ""); //$NON-NLS-1$ //$NON-NLS-2$
			modifiedKey = modifiedKey.replaceAll("_", " "); //$NON-NLS-1$ //$NON-NLS-2$
			cbOrderTypes.setSelectedItem(modifiedKey);

			String taxKey = key;
			taxKey = taxKey.replaceAll("_PRICE", "_TAX"); //$NON-NLS-1$ //$NON-NLS-2$
			Tax newtax = TaxDAO.getInstance().findByTaxRate(Double.parseDouble(modifier.getProperty(taxKey)));
			cbTax.setSelectedItem(newtax);

			tfPrice.setText(String.valueOf(modifier.getProperties().get(key)));
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
		modifier.setTaxByOrderType(cbOrderTypes.getSelectedItem().toString(), tax.getRate());
		modifier.setPriceByOrderType(cbOrderTypes.getSelectedItem().toString(), price);
		return true;
	}

	public MenuModifier getMenuModifier() {
		return modifier;
	}

	public void setMenuModifier(MenuModifier modifier) {
		this.modifier = modifier;

		updateView();
	}

	private void createView() {
		contentPane = new JPanel(new BorderLayout());
		final JLabel label1 = new JLabel();
		label1.setText("Order type:"); //$NON-NLS-1$
		cbOrderTypes = new JComboBox();

		final JLabel label3 = new JLabel();
		label3.setText("Tax:"); //$NON-NLS-1$
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
		btnOK = new JButton(Messages.getString("ModifierPriceByOrderTypeDialog.0")); //$NON-NLS-1$
		btnCancel = new JButton(Messages.getString("ModifierPriceByOrderTypeDialog.19")); //$NON-NLS-1$

		buttonPanel.add(btnOK, "grow"); //$NON-NLS-1$
		buttonPanel.add(btnCancel, "grow"); //$NON-NLS-1$
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		add(contentPane);
	}
}
