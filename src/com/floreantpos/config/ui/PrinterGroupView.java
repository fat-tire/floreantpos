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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.PrinterGroup;
import com.floreantpos.model.TerminalPrinters;
import com.floreantpos.model.dao.PrinterGroupDAO;
import com.floreantpos.model.dao.TerminalPrintersDAO;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class PrinterGroupView extends JPanel {

	private JList<PrinterGroup> list;
	private DefaultListModel<PrinterGroup> listModel;

	public PrinterGroupView() {

	}

	public PrinterGroupView(String title) {

		setBorder(BorderFactory.createTitledBorder(title));
		setLayout(new BorderLayout(10, 10));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		JButton btnAdd = new JButton(POSConstants.ADD.toUpperCase()); //$NON-NLS-1$
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddPrinter();
			}
		});
		panel.add(btnAdd);

		JButton btnEdit = new JButton(POSConstants.EDIT.toUpperCase()); //$NON-NLS-1$
		btnEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doEditPrinter();
			}
		});
		panel.add(btnEdit);

		JButton btnDelete = new JButton(POSConstants.DELETE.toUpperCase()); //$NON-NLS-1$
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doDeletePrinterGroup();
			}
		});
		panel.add(btnDelete);

		JButton btnTest = new JButton("TEST"); //$NON-NLS-1$
		btnTest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				testPrinter();
			}
		});
		panel.add(btnTest);

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		listModel = new DefaultListModel<PrinterGroup>();

		List<PrinterGroup> all = PrinterGroupDAO.getInstance().findAll();
		for (PrinterGroup printerGroup : all) {
			listModel.addElement(printerGroup);
		}

		list = new JList<PrinterGroup>(listModel);
		scrollPane.setViewportView(list);
	}

	private void doDeletePrinterGroup() {
		PrinterGroup pGroup = list.getSelectedValue();
		if (pGroup == null) {
			return;
		}

		try {
			PrinterGroupDAO.getInstance().delete(pGroup.getId());
			listModel.removeElement(pGroup);
		} catch (Exception e) {
			POSMessageDialog.showError(Messages.getString("PrinterGroupView.0")); //$NON-NLS-1$
		}
		refresh();
	}

	protected void doEditPrinter() {
		PrinterGroup pGroup = list.getSelectedValue();
		if (pGroup == null) {
			return;
		}

		AddPrinterGroupDialog dialog = new AddPrinterGroupDialog();
		dialog.setPrinterGroup(pGroup);
		dialog.open();

		if (dialog.isCanceled()) {
			return;
		}

		PrinterGroup printerGroup = dialog.getPrinterGroup();
		PrinterGroupDAO.getInstance().saveOrUpdate(printerGroup);
		refresh();
	}

	protected void doAddPrinter() {
		AddPrinterGroupDialog dialog = new AddPrinterGroupDialog();
		dialog.open();

		if (dialog.isCanceled()) {
			return;
		}

		PrinterGroup printerGroup = dialog.getPrinterGroup();
		PrinterGroupDAO.getInstance().saveOrUpdate(printerGroup);

		listModel.addElement(printerGroup);
		refresh();
	}

	private void refresh() {
		listModel.clear();
		List<PrinterGroup> all = PrinterGroupDAO.getInstance().findAll();
		for (PrinterGroup printersG : all) {
			listModel.addElement(printersG);
		}
	}

	private void testPrinter() {
		PrinterGroup printerGroup = list.getSelectedValue();
		if (printerGroup == null) {
			return;
		}

		List<String> printerList = printerGroup.getPrinterNames();

		List<TerminalPrinters> terminalPrinters = TerminalPrintersDAO.getInstance().findTerminalPrinters();

		for (TerminalPrinters terminalPrinter : terminalPrinters) {
			if (printerList.contains(terminalPrinter.getVirtualPrinter().getName())) {
				try {
					String title = "System Information"; //$NON-NLS-1$
					String data = terminalPrinter.getPrinterName() + "-" + terminalPrinter.getVirtualPrinter().getName(); //$NON-NLS-1$
					data += "\n Terminal : " + Application.getInstance().getTerminal().getName(); //$NON-NLS-1$
					data += "\n Current User : " + Application.getCurrentUser().getFirstName(); //$NON-NLS-1$
					data += "\n Floreant Version : " + Application.VERSION; //$NON-NLS-1$
					data += "\n Database Name : " + AppConfig.getDatabaseName() + AppConfig.getDatabaseHost() + AppConfig.getDatabasePort(); //$NON-NLS-1$
					ReceiptPrintService.testPrinter(terminalPrinter.getPrinterName(), title, data);

				} catch (Exception e) {
				}
			}
		}
	}
}
