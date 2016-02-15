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

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.model.dao.VirtualPrinterDAO;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class VirtualPrinterConfigDialog extends POSDialog {
	private VirtualPrinter printer;
	private FixedLengthTextField tfName;

//	private JCheckBox cbDineIn = new JCheckBox(OrderType.DINE_IN.toString());
//	private JCheckBox cbTakeOut = new JCheckBox(OrderType.TAKE_OUT.toString());
//	private JCheckBox cbPickup = new JCheckBox(OrderType.PICKUP.toString());
//	private JCheckBox cbHomeDeli = new JCheckBox(OrderType.HOME_DELIVERY.toString());
//	private JCheckBox cbDriveThru = new JCheckBox(OrderType.DRIVE_THRU.toString());
//	private JCheckBox cbBarTab = new JCheckBox(OrderType.BAR_TAB.toString());

	public VirtualPrinterConfigDialog() throws HeadlessException {
		super(POSUtil.getBackOfficeWindow(), true);
		setTitle(Messages.getString("VirtualPrinterConfigDialog.0")); //$NON-NLS-1$
		
		init();

		pack();
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void init() {
		getContentPane().setLayout(new MigLayout("", "[][grow]", "[][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblName = new JLabel(Messages.getString("VirtualPrinterConfigDialog.4")); //$NON-NLS-1$
		getContentPane().add(lblName, "cell 0 0,alignx trailing"); //$NON-NLS-1$

		tfName = new FixedLengthTextField(60);
		getContentPane().add(tfName, "cell 1 0,growx"); //$NON-NLS-1$

//		JPanel orderTypePanel = new JPanel();
//		orderTypePanel.setBorder(new TitledBorder("ORDER TYPE"));
//		orderTypePanel.add(cbDineIn);
//		orderTypePanel.add(cbTakeOut);
//		orderTypePanel.add(cbPickup);
//		orderTypePanel.add(cbHomeDeli);
//		orderTypePanel.add(cbDriveThru);
//		orderTypePanel.add(cbBarTab);
//		add(orderTypePanel, "newline, grow, span 2");

		JSeparator separator = new JSeparator();
		getContentPane().add(separator, "cell 0 1 2 1,growx, gap top 50px"); //$NON-NLS-1$

		JPanel panel = new JPanel();
		getContentPane().add(panel, "cell 0 4 2 1,grow"); //$NON-NLS-1$

		JButton btnOk = new JButton(Messages.getString("VirtualPrinterConfigDialog.9")); //$NON-NLS-1$
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddPrinter();
			}
		});
		panel.add(btnOk);

		JButton btnCancel = new JButton(Messages.getString("VirtualPrinterConfigDialog.10")); //$NON-NLS-1$
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		panel.add(btnCancel);
	}

	protected void doAddPrinter() {
		try {

			String name = tfName.getText();
			if (StringUtils.isEmpty(name)) {
				POSMessageDialog.showMessage(this, Messages.getString("VirtualPrinterConfigDialog.11")); //$NON-NLS-1$
				return;
			}

			VirtualPrinterDAO printerDAO = VirtualPrinterDAO.getInstance();

			if (printerDAO.findPrinterByName(name) != null) {
				POSMessageDialog.showMessage(this, Messages.getString("VirtualPrinterConfigDialog.12")); //$NON-NLS-1$
				return;
			}

			if (printer == null) {
				printer = new VirtualPrinter();
			}

			printer.setName(name);
			
//			List<String> orderTypeNames = new ArrayList<String>();
//			
//			if(cbDineIn.isSelected()) {
//				orderTypeNames.add(OrderType.DINE_IN.name());
//			}
//			if(cbTakeOut.isSelected()) {
//				orderTypeNames.add(OrderType.TAKE_OUT.name());
//			}
//			if(cbPickup.isSelected()) {
//				orderTypeNames.add(OrderType.PICKUP.name());
//			}
//			if(cbHomeDeli.isSelected()) {
//				orderTypeNames.add(OrderType.HOME_DELIVERY.name());
//			}
//			if(cbDriveThru.isSelected()) {
//				orderTypeNames.add(OrderType.DRIVE_THRU.name());
//			}
//			if(cbBarTab.isSelected()) {
//				orderTypeNames.add(OrderType.BAR_TAB.name());
//			}
			
//			printer.setOrderTypeNames(orderTypeNames);

			printerDAO.saveOrUpdate(printer);

			setCanceled(false);
			dispose();

		} catch (Exception e) {
			POSMessageDialog.showError(this, e.getMessage(), e);
		}
	}

	public VirtualPrinter getPrinter() {
		return printer;
	}

	public void setPrinter(VirtualPrinter printer) {
		this.printer = printer;

		if (printer != null) {
			tfName.setText(printer.getName());
		}
	}
}
