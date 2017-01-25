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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosLog;
import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Printer;
import com.floreantpos.model.TerminalPrinters;
import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.model.dao.TerminalPrintersDAO;
import com.floreantpos.model.dao.VirtualPrinterDAO;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class MultiPrinterPane extends JPanel {

	private JList<Printer> list;
	private List<Printer> printers = new ArrayList<Printer>();;
	private DefaultListModel<Printer> listModel;

	private JXTable table;
	private BeanTableModel<Printer> tableModel;

	private int selectedPrinterType;

	public MultiPrinterPane() {

	}

	public MultiPrinterPane(String title, List<Printer> allPrinters) {
		//this.printers = allPrinters;

		setBorder(BorderFactory.createTitledBorder(title));
		setLayout(new BorderLayout(10, 10));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		JButton btnAdd = new JButton(Messages.getString("MultiPrinterPane.0")); //$NON-NLS-1$
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrinterTypeSelectionDialog dialog = new PrinterTypeSelectionDialog();
				dialog.open();
				if (dialog.isCanceled()) {
					return;
				}
				selectedPrinterType = dialog.getSelectedPrinterType();
				doAddPrinter();
			}
		});
		panel.add(btnAdd);

		JButton btnEdit = new JButton(Messages.getString("MultiPrinterPane.1")); //$NON-NLS-1$
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
				doDeletePrinter();
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

		listModel = new DefaultListModel<Printer>();
		list = new JList<Printer>(listModel);
		//scrollPane.setViewportView(list);

		if (printers != null) {
			for (Printer printer : printers) {
				listModel.addElement(printer);
			}
		}

		tableModel = new BeanTableModel<Printer>(Printer.class);
		tableModel.addColumn("Name", "virtualPrinter"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("Printer", "deviceName"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("Type", "type"); //$NON-NLS-1$ //$NON-NLS-2$

		List<VirtualPrinter> virtualPrinters = VirtualPrinterDAO.getInstance().findAll();

		if (virtualPrinters != null && !virtualPrinters.isEmpty()) {
			for (VirtualPrinter virtualPrinter : virtualPrinters) {
				Printer printer = new Printer();
				printer.setVirtualPrinter(virtualPrinter);
				printer.setDeviceName(""); //$NON-NLS-1$
				TerminalPrinters terminalPrinter = TerminalPrintersDAO.getInstance().findPrinters(printer.getVirtualPrinter());
				if (terminalPrinter != null) {
					if (virtualPrinter.getName().equals(terminalPrinter.getVirtualPrinter().getName())) {
						printer.setDeviceName(terminalPrinter.getPrinterName());
					}
				}

				printers.add(printer);
			}
		}

		tableModel.addRows(printers);
		table = new JXTable(tableModel);
		add(new JScrollPane(table), BorderLayout.CENTER);

	}

	protected void doEditPrinter() {
		//Printer printer = list.getSelectedValue();
		/*if(printer == null) {
			return;
		}
		*/
		int index = table.getSelectedRow();
		if (index < 0)
			return;

		index = table.convertRowIndexToModel(index);
		Printer customPrinter = tableModel.getRow(index);

		AddPrinterDialog dialog = new AddPrinterDialog();
		dialog.setPrinter(customPrinter);
		dialog.open();

		if (dialog.isCanceled()) {
			return;
		}
		Printer p = dialog.getPrinter();

		if (p.isDefaultPrinter()) {
			for (Printer printer2 : printers) {
				printer2.setDefaultPrinter(false);
			}
		}

		//printer.setDefaultPrinter(true);

		VirtualPrinter virtualPrinter = p.getVirtualPrinter();

		VirtualPrinterDAO.getInstance().saveOrUpdate(virtualPrinter);

		TerminalPrinters terminalPrinter = TerminalPrintersDAO.getInstance().findPrinters(virtualPrinter);

		if (terminalPrinter == null) {
			terminalPrinter = new TerminalPrinters();
		}
		terminalPrinter.setTerminal(Application.getInstance().getTerminal());
		terminalPrinter.setPrinterName(p.getDeviceName());
		terminalPrinter.setVirtualPrinter(p.getVirtualPrinter());
		TerminalPrintersDAO.getInstance().saveOrUpdate(terminalPrinter);

		refresh();
	}

	protected void doDeletePrinter() {
		int index = table.getSelectedRow();
		if (index < 0)
			return;

		index = table.convertRowIndexToModel(index);
		Printer customPrinter = tableModel.getRow(index);

		List<TerminalPrinters> terminalPrinters = TerminalPrintersDAO.getInstance().findAll();
		for (TerminalPrinters terminalPrinter : terminalPrinters) {
			if (terminalPrinter.getVirtualPrinter().equals(customPrinter.getVirtualPrinter())) {
				TerminalPrintersDAO.getInstance().delete(terminalPrinter);
			}
		}

		if (customPrinter.getVirtualPrinter() != null) {
			VirtualPrinterDAO.getInstance().delete(customPrinter.getVirtualPrinter());
		}
		refresh();
	}

	protected void doAddPrinter() {
		AddPrinterDialog dialog = new AddPrinterDialog();
		dialog.titlePanel.setTitle(VirtualPrinter.PRINTER_TYPE_NAMES[selectedPrinterType] + " - Printer"); //$NON-NLS-1$
		dialog.open();

		if (dialog.isCanceled()) {
			return;
		}

		Printer p = dialog.getPrinter();

		VirtualPrinterDAO printerDAO = VirtualPrinterDAO.getInstance();

		if (printerDAO.findPrinterByName(p.getVirtualPrinter().getName()) != null) {
			POSMessageDialog.showMessage(this, Messages.getString("VirtualPrinterConfigDialog.12")); //$NON-NLS-1$
			return;
		}

		if (p.isDefaultPrinter()) {
			for (Printer printer : printers) {
				printer.setDefaultPrinter(false);
			}
		}

		VirtualPrinter virtualPrinter = p.getVirtualPrinter();

		for (Printer printer : printers) {
			if (virtualPrinter.equals(printer.getVirtualPrinter())) {
				POSMessageDialog.showError(this.getParent(), Messages.getString("MultiPrinterPane.2")); //$NON-NLS-1$
				return;
			}
		}

		virtualPrinter.setType(selectedPrinterType);
		VirtualPrinterDAO.getInstance().saveOrUpdate(virtualPrinter);

		TerminalPrinters terminalPrinters = new TerminalPrinters();
		terminalPrinters.setTerminal(Application.getInstance().getTerminal());
		terminalPrinters.setPrinterName(p.getDeviceName());
		terminalPrinters.setVirtualPrinter(p.getVirtualPrinter());
		TerminalPrintersDAO.getInstance().saveOrUpdate(terminalPrinters);

		printers.add(p);
		listModel.addElement(p);

		refresh();
	}

	private void refresh() {
		printers.clear();
		List<VirtualPrinter> virtualPrinters = VirtualPrinterDAO.getInstance().findAll();

		if (virtualPrinters != null && !virtualPrinters.isEmpty()) {
			for (VirtualPrinter virtualPrinter : virtualPrinters) {
				Printer printer = new Printer();
				printer.setVirtualPrinter(virtualPrinter);
				printer.setDeviceName(""); //$NON-NLS-1$
				TerminalPrinters terminalPrinter = TerminalPrintersDAO.getInstance().findPrinters(printer.getVirtualPrinter());
				if (terminalPrinter != null) {
					if (virtualPrinter.getName().equals(terminalPrinter.getVirtualPrinter().getName())) {
						printer.setDeviceName(terminalPrinter.getPrinterName());
					}
				}

				printers.add(printer);
			}
		}
		tableModel.removeAll();
		tableModel.addRows(printers);
		table.validate();
		table.repaint();
	}

	private void testPrinter() {
		int index = table.getSelectedRow();
		if (index < 0)
			return;

		index = table.convertRowIndexToModel(index);
		Printer printer = tableModel.getRow(index);

		if (printer.getDeviceName() == null) {
			PosLog.info(getClass(), "No print selected for " + printer.getType());
			return;
		}

		try {
			String title = "System Information"; //$NON-NLS-1$
			String data = printer.getDeviceName() + "-" + printer.getVirtualPrinter().getName(); //$NON-NLS-1$
			data += "\n Terminal : " + Application.getInstance().getTerminal().getName(); //$NON-NLS-1$
			data += "\n Current User : " + Application.getCurrentUser().getFirstName(); //$NON-NLS-1$
			data += "\n Floreant Version : " + Application.VERSION; //$NON-NLS-1$
			data += "\n Database Name : " + AppConfig.getDatabaseName() + AppConfig.getDatabaseHost() + AppConfig.getDatabasePort(); //$NON-NLS-1$
			ReceiptPrintService.testPrinter(printer.getDeviceName(), title, data);

		} catch (Exception e) {
		}
	}
}
