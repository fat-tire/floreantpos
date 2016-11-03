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
package com.floreantpos.customer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.Customer;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.forms.CustomerForm;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.PosGuiUtil;

public class CustomerExplorer extends TransparentPanel {
	private List<Customer> customerList;

	private JTable table;

	private BeanTableModel<Customer> tableModel;

	public CustomerExplorer() {
		CustomerDAO dao = new CustomerDAO();
		customerList = dao.findAll();

		tableModel = new BeanTableModel<Customer>(Customer.class);
		tableModel.addColumn("ID", "autoId"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("NAME", "name"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("LOYALTY", "loyaltyNo"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("TELEPHONE", "telephoneNo"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("EMAIL", "email"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("DOB", "dob"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("SSN", "ssn"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("ADDRESS", "address"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("CITY", "city"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("STATE", "state"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("ZIP", "zipCode"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("COUNTRY", "country"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("CREDIT LIMIT", "creditLimit"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("CREDIT SPENT", "creditSpent"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn("NOTE", "note"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addRows(customerList);

		table = new JTable(tableModel);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		PosGuiUtil.setColumnWidth(table, 0, 40);

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		JButton addButton = new JButton(com.floreantpos.POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					boolean setKeyPad = true;
					CustomerForm editor = new CustomerForm(setKeyPad);
					editor.enableCustomerFields(true);

					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();
					if (dialog.isCanceled())
						return;
					Customer customer = (Customer) editor.getBean();
					tableModel.addRow(customer);
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		JButton editButton = new JButton(com.floreantpos.POSConstants.EDIT);
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					Customer customer = customerList.get(index);

					boolean setKeyPad = true;
					CustomerForm editor = new CustomerForm();
					editor.enableCustomerFields(true);

					editor.setBean(customer);
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		JButton deleteButton = new JButton(com.floreantpos.POSConstants.DELETE);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					if (ConfirmDeleteDialog
							.showMessage(CustomerExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
						Customer customer = customerList.get(index);
						CustomerDAO dao = new CustomerDAO();
						dao.delete(customer);
						tableModel.removeRow(customer);
					}
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}
}
