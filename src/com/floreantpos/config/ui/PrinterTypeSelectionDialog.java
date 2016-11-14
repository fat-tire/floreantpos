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
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

/**
 * 
 * @author MShahriar
 */
public class PrinterTypeSelectionDialog extends POSDialog implements ActionListener {

	private JPanel buttonsPanel;

	private POSToggleButton btnReportPrinter;
	private POSToggleButton btnReceiptPrinter;
	private POSToggleButton btnKitchenPrinter;
	private POSToggleButton btnPackingPrinter;
	private POSToggleButton btnKitchenDisplayPrinter;

	private int selectedPrinterType;

	public PrinterTypeSelectionDialog() {
		super(POSUtil.getBackOfficeWindow(), true);
		initializeComponent();
		initializeData();
	}

	private void initializeComponent() {
		setTitle("Select Printer Type"); //$NON-NLS-1$
		setLayout(new BorderLayout());

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Select Printer Type");//$NON-NLS-1$
		add(titlePanel, BorderLayout.NORTH);

		JPanel buttonActionPanel = new JPanel(new MigLayout("fill")); //$NON-NLS-1$

		PosButton btnCancel = new PosButton(POSConstants.CANCEL.toUpperCase());
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				selectedPrinterType = 0;
				setCanceled(true);
				dispose();
			}
		});

		buttonActionPanel.add(btnCancel, "grow, span"); //$NON-NLS-1$

		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		footerPanel.add(new JSeparator(), BorderLayout.NORTH);
		footerPanel.add(buttonActionPanel);

		add(footerPanel, BorderLayout.SOUTH);

		buttonsPanel = new JPanel(new MigLayout("fill", "sg, fill", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		add(buttonsPanel, BorderLayout.CENTER);

		setSize(500, 280);
	}

	private void initializeData() {
		try {
			ButtonGroup group = new ButtonGroup();
			btnReportPrinter = new POSToggleButton(VirtualPrinter.PRINTER_TYPE_NAMES[VirtualPrinter.REPORT]);
			btnReceiptPrinter = new POSToggleButton(VirtualPrinter.PRINTER_TYPE_NAMES[VirtualPrinter.RECEIPT]);
			btnKitchenPrinter = new POSToggleButton(VirtualPrinter.PRINTER_TYPE_NAMES[VirtualPrinter.KITCHEN]);
			btnPackingPrinter = new POSToggleButton(VirtualPrinter.PRINTER_TYPE_NAMES[VirtualPrinter.PACKING]);
			btnKitchenDisplayPrinter = new POSToggleButton(VirtualPrinter.PRINTER_TYPE_NAMES[VirtualPrinter.KITCHEN_DISPLAY]);

			btnReportPrinter.addActionListener(this);
			btnReceiptPrinter.addActionListener(this);
			btnKitchenPrinter.addActionListener(this);
			btnPackingPrinter.addActionListener(this);
			btnKitchenDisplayPrinter.addActionListener(this);

			buttonsPanel.add(btnReportPrinter, "growy"); //$NON-NLS-1$
			buttonsPanel.add(btnReceiptPrinter, "growy"); //$NON-NLS-1$
			buttonsPanel.add(btnKitchenPrinter, "growy"); //$NON-NLS-1$
			buttonsPanel.add(btnPackingPrinter, "growy"); //$NON-NLS-1$
			buttonsPanel.add(btnKitchenDisplayPrinter, "growy"); //$NON-NLS-1$

			group.add(btnReportPrinter);
			group.add(btnReceiptPrinter);
			group.add(btnKitchenPrinter);
			group.add(btnPackingPrinter);
			group.add(btnKitchenDisplayPrinter);

		} catch (PosException e) {
			POSMessageDialog.showError(PrinterTypeSelectionDialog.this, e.getLocalizedMessage(), e);
		}
	}

	protected void doFinish() {
		setCanceled(false);
		dispose();
	}

	public int getSelectedPrinterType() {
		return selectedPrinterType;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == btnReportPrinter) {
			selectedPrinterType = VirtualPrinter.REPORT;
		}
		else if (source == btnReceiptPrinter) {
			selectedPrinterType = VirtualPrinter.RECEIPT;
		}
		else if (source == btnKitchenPrinter) {
			selectedPrinterType = VirtualPrinter.KITCHEN;
		}
		else if (source == btnPackingPrinter) {
			selectedPrinterType = VirtualPrinter.PACKING;
		}
		else if (source == btnKitchenDisplayPrinter) {
			selectedPrinterType = VirtualPrinter.KITCHEN_DISPLAY;
		}
		doFinish();
	}
}
