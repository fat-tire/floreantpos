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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosSmallButton;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.forms.CustomerForm;

public class CustomerSelectionDialog extends POSDialog {

	private PosSmallButton btnCreateNewCustomer;
	private CustomerTable customerTable;
	private POSTextField tfMobile;
	private POSTextField tfLoyaltyNo;
	private POSTextField tfName;
	private PosSmallButton btnInfo;
	protected Customer selectedCustomer;
	private PosSmallButton btnRemoveCustomer;

	private Ticket ticket;

	public CustomerSelectionDialog() {
		super(Application.getPosWindow(), true);
		setTitle(Messages.getString("CustomerSelectionDialog.3")); //$NON-NLS-1$
	}

	public CustomerSelectionDialog(Ticket ticket) {
		this.ticket = ticket;

		setTitle(Messages.getString("CustomerSelectionDialog.0")); //$NON-NLS-1$

		loadCustomerFromTicket();
	}

	@Override
	public void initUI() {
		setPreferredSize(new Dimension(690, 553));
		getContentPane().setLayout(new MigLayout("", "[549px,grow]", "[grow][][shrink 0,fill][grow][grow]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(new TitledBorder(null, POSConstants.SELECT_CUSTOMER.toUpperCase(), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$

		JPanel searchPanel = new JPanel(new MigLayout());

		JLabel lblByPhone = new JLabel(Messages.getString("CustomerSelectionDialog.1")); //$NON-NLS-1$
		JLabel lblByLoyality = new JLabel(Messages.getString("CustomerSelectionDialog.16")); //$NON-NLS-1$
		JLabel lblByName = new JLabel(Messages.getString("CustomerSelectionDialog.19")); //$NON-NLS-1$

		tfMobile = new POSTextField(16);
		tfLoyaltyNo = new POSTextField(16);
		tfName = new POSTextField(16);

		tfName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearchCustomer();
			}
		});
		tfLoyaltyNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearchCustomer();
			}
		});
		tfMobile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearchCustomer();
			}
		});

		PosSmallButton btnSearch = new PosSmallButton(Messages.getString("CustomerSelectionDialog.15")); //$NON-NLS-1$
		btnSearch.setFocusable(false);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearchCustomer();
			}
		});

		searchPanel.add(lblByPhone);
		searchPanel.add(tfMobile, "height 30!"); //$NON-NLS-1$
		searchPanel.add(lblByLoyality);
		searchPanel.add(tfLoyaltyNo, "height 30!"); //$NON-NLS-1$
		searchPanel.add(lblByName);
		searchPanel.add(tfName, "height 30!"); //$NON-NLS-1$
		searchPanel.add(btnSearch, "wrap, height 35!"); //$NON-NLS-1$

		contentPanel.add(searchPanel, BorderLayout.NORTH); //$NON-NLS-1$

		JPanel customerListPanel = new JPanel(new BorderLayout(0, 0));
		customerListPanel.setBorder(new EmptyBorder(5, 5, 0, 5));

		customerTable = new CustomerTable();
		customerTable.setModel(new CustomerListTableModel());
		customerTable.setFocusable(false);
		customerTable.setRowHeight(35);
		customerTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		customerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				selectedCustomer = customerTable.getSelectedCustomer();
				if (selectedCustomer != null) {
					//btnInfo.setEnabled(true);
				}
				else {
					btnInfo.setEnabled(false);
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFocusable(false);
		scrollPane.setViewportView(customerTable);

		customerListPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		btnInfo = new PosSmallButton(Messages.getString("CustomerSelectionDialog.23")); //$NON-NLS-1$
		btnInfo.setFocusable(false);
		panel.add(btnInfo);
		btnInfo.setEnabled(false);

		PosSmallButton btnHistory = new PosSmallButton(Messages.getString("CustomerSelectionDialog.24")); //$NON-NLS-1$
		btnHistory.setEnabled(false);
		panel.add(btnHistory);

		btnCreateNewCustomer = new PosSmallButton(Messages.getString("CustomerSelectionDialog.25")); //$NON-NLS-1$
		btnCreateNewCustomer.setFocusable(false);
		panel.add(btnCreateNewCustomer);
		btnCreateNewCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCreateNewCustomer();
			}
		});

		btnRemoveCustomer = new PosSmallButton(Messages.getString("CustomerSelectionDialog.26")); //$NON-NLS-1$
		btnRemoveCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doRemoveCustomerFromTicket();
			}
		});
		panel.add(btnRemoveCustomer);

		PosSmallButton btnSelect = new PosSmallButton(Messages.getString("CustomerSelectionDialog.28")); //$NON-NLS-1$
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Customer customer = customerTable.getSelectedCustomer();

				selectedCustomer = customer;

				if (customer == null) {
					POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("CustomerSelectionDialog.27")); //$NON-NLS-1$
					return;
				}

				doSetCustomer(customer);
				setCanceled(false);
				dispose();
			}
		});
		panel.add(btnSelect);

		PosSmallButton btnCancel = new PosSmallButton(Messages.getString("CustomerSelectionDialog.29")); //$NON-NLS-1$
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		panel.add(btnCancel);

		customerListPanel.add(panel, BorderLayout.SOUTH);

		contentPanel.add(customerListPanel, BorderLayout.CENTER); //$NON-NLS-1$

		getContentPane().add(contentPanel, "cell 0 1,grow, gapright 2px"); //$NON-NLS-1$

		JPanel keyPadPanel = new JPanel(new BorderLayout());
		com.floreantpos.swing.QwertyKeyPad qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		keyPadPanel.add(qwertyKeyPad);

		getContentPane().add(keyPadPanel, "cell 0 2,grow, gapright 2px"); //$NON-NLS-1$
	}

	private void loadCustomerFromTicket() {
		String customerIdString = ticket.getProperty(Ticket.CUSTOMER_ID);
		if (StringUtils.isNotEmpty(customerIdString)) {
			int customerId = Integer.parseInt(customerIdString);
			Customer customer = CustomerDAO.getInstance().get(customerId);

			List<Customer> list = new ArrayList<Customer>();
			list.add(customer);
			customerTable.setModel(new CustomerListTableModel(list));
		}
	}

	protected void doSetCustomer(Customer customer) {
		if (ticket != null) {
			ticket.setCustomer(customer);
			TicketDAO.getInstance().saveOrUpdate(ticket);
		}
	}

	protected void doRemoveCustomerFromTicket() {
		int option = POSMessageDialog.showYesNoQuestionDialog(this,
				Messages.getString("CustomerSelectionDialog.2"), Messages.getString("CustomerSelectionDialog.32")); //$NON-NLS-1$ //$NON-NLS-2$
		if (option != JOptionPane.YES_OPTION) {
			return;
		}

		ticket.removeCustomer();
		TicketDAO.getInstance().saveOrUpdate(ticket);
		setCanceled(false);
		dispose();
	}

	protected void doSearchCustomer() {
		String mobile = tfMobile.getText();
		String name = tfName.getText();
		String loyalty = tfLoyaltyNo.getText();

		if (StringUtils.isEmpty(mobile) && StringUtils.isEmpty(loyalty) && StringUtils.isEmpty(name)) {
			List<Customer> list = CustomerDAO.getInstance().findAll();
			customerTable.setModel(new CustomerListTableModel(list));
			return;
		}

		List<Customer> list = CustomerDAO.getInstance().findBy(mobile, loyalty, name);
		customerTable.setModel(new CustomerListTableModel(list));
	}

	protected void doCreateNewCustomer() {
		boolean setKeyPad = true;
		CustomerForm form = new CustomerForm(setKeyPad);
		form.enableCustomerFields(true);
		BeanEditorDialog dialog = new BeanEditorDialog(form);
		dialog.open();

		if (!dialog.isCanceled()) {
			selectedCustomer = (Customer) form.getBean();

			CustomerListTableModel model = (CustomerListTableModel) customerTable.getModel();
			model.addItem(selectedCustomer);
		}
	}

	@Override
	public String getName() {
		return "C"; //$NON-NLS-1$
	}

	public Customer getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setRemoveButtonEnable(boolean enable) {
		btnRemoveCustomer.setEnabled(enable);
	}
}
