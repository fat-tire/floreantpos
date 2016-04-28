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
 * MiscTicketItemDialog.java
 *
 * Created on September 8, 2006, 10:04 PM
 */

package com.floreantpos.ui.dialog;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.PrinterGroup;
import com.floreantpos.model.Tax;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.PrinterGroupDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.PosComboRenderer;
import com.floreantpos.swing.QwertyKeyPad;

/**
 *
 * @author  MShahriar
 */
public class MiscTicketItemDialog extends OkCancelOptionDialog {
	private TicketItem ticketItem;
	private javax.swing.JComboBox cbTax;
	private FixedLengthTextField tfItemName;
	private DoubleTextField tfItemPrice;
	private JComboBox cbPrinterGroup;
	private JLabel lblTax;

	public MiscTicketItemDialog() {
		super(Application.getPosWindow(), true);
		setTitle(Messages.getString("MiscTicketItemDialog.0")); //$NON-NLS-1$
		initComponents();
	}

	private void initComponents() {
		JPanel contentPane = new JPanel(new MigLayout("inset 0, fillx", "", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		setTitle(Messages.getString("MiscTicketItemDialog.4")); //$NON-NLS-1$
		setTitlePaneText(Messages.getString("MiscTicketItemDialog.4")); //$NON-NLS-1$

		JLabel lblName = new JLabel(Messages.getString("MiscTicketItemDialog.6")); //$NON-NLS-1$
		contentPane.add(lblName, "newline,alignx trailing"); //$NON-NLS-1$

		tfItemName = new FixedLengthTextField();
		tfItemName.setLength(120);
		contentPane.add(tfItemName, "grow, span, h 40"); //$NON-NLS-1$

		JLabel lblPrice = new JLabel(Messages.getString("MiscTicketItemDialog.9")); //$NON-NLS-1$
		contentPane.add(lblPrice, "newline,alignx trailing"); //$NON-NLS-1$

		tfItemPrice = new DoubleTextField();
		contentPane.add(tfItemPrice, "grow, w 120, h 40"); //$NON-NLS-1$

		lblTax = new JLabel(Messages.getString("MiscTicketItemDialog.12"));//$NON-NLS-2$
		contentPane.add(lblTax, "alignx trailing"); //$NON-NLS-1$ 

		PosComboRenderer comboRenderer = new PosComboRenderer();
		comboRenderer.setEnableDefaultValueShowing(false);

		cbTax = new JComboBox();
		cbTax.setRenderer(comboRenderer);
		contentPane.add(cbTax, "w 200!, h 40"); //$NON-NLS-1$

		contentPane.add(new JLabel(Messages.getString("MiscTicketItemDialog.15")), "alignx trailing"); //$NON-NLS-1$ //$NON-NLS-2$

		cbPrinterGroup = new JComboBox();
		cbPrinterGroup.setRenderer(comboRenderer);
		contentPane.add(cbPrinterGroup, "w 200!, h 40"); //$NON-NLS-1$

		QwertyKeyPad keyPad = new QwertyKeyPad();
		contentPane.add(keyPad, "newline, grow, span, gaptop 10"); //$NON-NLS-1$

		getContentPanel().add(contentPane);

		initData();
	}

	private void initData() {
		List<Tax> taxes = TaxDAO.getInstance().findAll();

		cbTax.addItem("Select Tax");
		for (Tax tax : taxes) {
			cbTax.addItem(tax);
		}
		int defaultTaxId = TerminalConfig.getMiscItemDefaultTaxId();
		if (defaultTaxId != -1) {
			for (int i = 0; i < taxes.size(); i++) {
				Tax tax = taxes.get(i);
				if (tax.getId() == defaultTaxId) {
					cbTax.setSelectedIndex(i);
					break;
				}
			}
		}

		List<PrinterGroup> printerGroups = PrinterGroupDAO.getInstance().findAll();
		cbPrinterGroup.setModel(new ComboBoxModel(printerGroups));
	}

	public void doCancel() {
		setCanceled(true);
		ticketItem = null;
		dispose();
	}

	public void doOk() {
		double amount = tfItemPrice.getDouble();
		String itemName = tfItemName.getText();

		if (StringUtils.isEmpty(itemName)) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("MiscTicketItemDialog.1")); //$NON-NLS-1$
			return;
		}

		if (Double.isNaN(amount)) {
			amount = 0;
		}

		setCanceled(false);

		ticketItem = new TicketItem();
		ticketItem.setItemCount(1);
		ticketItem.setUnitPrice(amount);
		ticketItem.setName(itemName);
		ticketItem.setCategoryName(com.floreantpos.POSConstants.MISC_BUTTON_TEXT);
		ticketItem.setGroupName(com.floreantpos.POSConstants.MISC_BUTTON_TEXT);
		ticketItem.setShouldPrintToKitchen(true);

		Object selectedObject = cbTax.getSelectedItem();

		if (selectedObject instanceof Tax) {
			Tax tax = (Tax) selectedObject;
			if (tax != null) {
				ticketItem.setTaxRate(tax.getRate());
				TerminalConfig.setMiscItemDefaultTaxId(tax.getId());
			}
		}

		PrinterGroup printerGroup = (PrinterGroup) cbPrinterGroup.getSelectedItem();
		if (printerGroup != null) {
			ticketItem.setPrinterGroup(printerGroup);
		}

		dispose();
	}

	public TicketItem getTicketItem() {
		return ticketItem;
	}
}
