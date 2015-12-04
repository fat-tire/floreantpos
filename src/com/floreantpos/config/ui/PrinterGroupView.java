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
import com.floreantpos.model.PrinterGroup;
import com.floreantpos.model.dao.PrinterGroupDAO;

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
		
		JButton btnAdd = new JButton(Messages.getString("PrinterGroupView.0")); //$NON-NLS-1$
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddPrinter();
			}
		});
		panel.add(btnAdd);
		
		JButton btnEdit = new JButton(Messages.getString("PrinterGroupView.1")); //$NON-NLS-1$
		btnEdit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				doEditPrinter();
			}
		});
		panel.add(btnEdit);
		
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

	protected void doEditPrinter() {
//		PrinterGroup printer = list.getSelectedValue();
//		if(printer == null) {
//			return;
//		}
//		
//		AddPrinterDialog dialog = new AddPrinterDialog();
//		dialog.setPrinter(printer);
//		dialog.open();
//
//		if (dialog.isCanceled()) {
//			return;
//		}
//
//		Printer p = dialog.getPrinter();
//
//		if (p.isDefaultPrinter()) {
//			for (Printer printer2 : printerGroups) {
//				printer2.setDefaultPrinter(false);
//			}
//		}
//		
//		printer.setDefaultPrinter(true);
	}

	protected void doAddPrinter() {
		AddPrinterGroupDialog dialog = new AddPrinterGroupDialog();
		dialog.open();
		
		if(dialog.isCanceled()) {
			return;
		}
		
		PrinterGroup printerGroup = dialog.getPrinterGroup();
		PrinterGroupDAO.getInstance().saveOrUpdate(printerGroup);
		
		listModel.addElement(printerGroup);
	}
}
