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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.extension.OrderServiceFactory;
import com.floreantpos.model.Customer;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.forms.QuickCustomerForm;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.TicketAlreadyExistsException;

public class DefaultCustomerListView extends CustomerSelector {

	private PosButton btnCreateNewCustomer;
	private CustomerTable customerTable;
	private POSTextField tfMobile;
	private POSTextField tfLoyaltyNo;
	private POSTextField tfName;
	private PosButton btnInfo;
	protected Customer selectedCustomer;
	private PosButton btnRemoveCustomer;

	private Ticket ticket;
	private PosButton btnCancel;
	private QwertyKeyPad qwertyKeyPad;

	public DefaultCustomerListView() {
		initUI();
	}

	public DefaultCustomerListView(Ticket ticket) {
		this.ticket = ticket;
		initUI();
		loadCustomerFromTicket();
	}

	public void initUI() {
		setLayout(new MigLayout("fill", "[grow]", "[grow][grow][grow]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

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

		PosButton btnSearch = new PosButton(Messages.getString("CustomerSelectionDialog.15")); //$NON-NLS-1$
		btnSearch.setFocusable(false);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearchCustomer();
			}
		});

		PosButton btnKeyboard = new PosButton(IconFactory.getIcon("/images/", "keyboard.png")); //$NON-NLS-1$ //$NON-NLS-2$
		btnKeyboard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				qwertyKeyPad.setCollapsed(!qwertyKeyPad.isCollapsed());
			}
		});

		searchPanel.add(lblByPhone, "growy"); //$NON-NLS-1$
		searchPanel.add(tfMobile, "growy"); //$NON-NLS-1$
		searchPanel.add(lblByLoyality, "growy"); //$NON-NLS-1$
		searchPanel.add(tfLoyaltyNo, "growy"); //$NON-NLS-1$
		searchPanel.add(lblByName, "growy"); //$NON-NLS-1$
		searchPanel.add(tfName, "growy"); //$NON-NLS-1$
		searchPanel.add(btnKeyboard, "growy,w " + PosUIManager.getSize(80) + "!,h " + PosUIManager.getSize(35) + "!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		searchPanel.add(btnSearch, ",growy,h " + PosUIManager.getSize(35) + "!"); //$NON-NLS-1$ //$NON-NLS-2$

		add(searchPanel, "cell 0 1"); //$NON-NLS-1$

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(new TitledBorder(null, POSConstants.SELECT_CUSTOMER.toUpperCase(), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$

		JPanel customerListPanel = new JPanel(new BorderLayout(0, 0));
		customerListPanel.setBorder(new EmptyBorder(5, 5, 0, 5));

		customerTable = new CustomerTable();
		customerTable.setModel(new CustomerListTableModel());
		customerTable.setFocusable(false);
		customerTable.setRowHeight(60);
		customerTable.getTableHeader().setPreferredSize(new Dimension(100, 35));
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

		JPanel panel = new JPanel(new MigLayout("hidemode 3,al center", "sg", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		btnInfo = new PosButton(Messages.getString("CustomerSelectionDialog.23")); //$NON-NLS-1$
		btnInfo.setFocusable(false);
		panel.add(btnInfo, "grow"); //$NON-NLS-1$
		btnInfo.setEnabled(false);

		PosButton btnHistory = new PosButton(Messages.getString("CustomerSelectionDialog.24")); //$NON-NLS-1$
		btnHistory.setEnabled(false);
		panel.add(btnHistory, "grow"); //$NON-NLS-1$

		btnCreateNewCustomer = new PosButton(Messages.getString("CustomerSelectionDialog.25")); //$NON-NLS-1$
		btnCreateNewCustomer.setFocusable(false);
		panel.add(btnCreateNewCustomer, "grow"); //$NON-NLS-1$
		btnCreateNewCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCreateNewCustomer();
			}
		});

		btnRemoveCustomer = new PosButton(Messages.getString("CustomerSelectionDialog.26")); //$NON-NLS-1$
		btnRemoveCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doRemoveCustomerFromTicket();
			}
		});
		panel.add(btnRemoveCustomer, "grow"); //$NON-NLS-1$

		PosButton btnSelect = new PosButton(Messages.getString("CustomerSelectionDialog.28")); //$NON-NLS-1$
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isCreateNewTicket()) {
					doCreateNewTicket();
				}
				else {
					closeDialog(false);
				}
			}
		});
		panel.add(btnSelect, "grow"); //$NON-NLS-1$

		btnCancel = new PosButton(Messages.getString("CustomerSelectionDialog.29")); //$NON-NLS-1$
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog(true);
			}
		});
		panel.add(btnCancel, "grow"); //$NON-NLS-1$

		customerListPanel.add(panel, BorderLayout.SOUTH);
		centerPanel.add(customerListPanel, BorderLayout.CENTER); //$NON-NLS-1$

		add(centerPanel, "cell 0 2,grow"); //$NON-NLS-1$

		qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		qwertyKeyPad.setCollapsed(false);
		add(qwertyKeyPad, "cell 0 3,grow"); //$NON-NLS-1$
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

	private void closeDialog(boolean canceled) {
		Window windowAncestor = SwingUtilities.getWindowAncestor(DefaultCustomerListView.this);
		if (windowAncestor instanceof POSDialog) {
			((POSDialog) windowAncestor).setCanceled(false);
			windowAncestor.dispose();
		}
	}

	protected void doSetCustomer(Customer customer) {
		if (ticket != null) {
			ticket.setCustomer(customer);
			TicketDAO.getInstance().saveOrUpdate(ticket);
		}
	}

	private void doCreateNewTicket() {
		try {
			Customer selectedCustomer = getSelectedCustomer();

			if (selectedCustomer == null) {
				return;
			}
			closeDialog(false);
			OrderServiceFactory.getOrderService().createNewTicket(getOrderType(), null, selectedCustomer);

		} catch (TicketAlreadyExistsException e) {

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
		//setCanceled(false);
		//dispose();
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

		QuickCustomerForm form = new QuickCustomerForm(setKeyPad);

		//TODO: handle exception

		form.enableCustomerFields(true);
		BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), form);
		dialog.setResizable(false);
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

	@Override
	public void updateView(boolean update) {
		if (update) {
			btnCancel.setVisible(true);
		}
		else {
			btnCancel.setVisible(false);
		}
	}

	@Override
	public void redererCustomers() {

	}
}
