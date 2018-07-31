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
package com.floreantpos.ui.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.Customer;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.model.util.ZipCodeUtil;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class QuickCustomerForm extends BeanEditor<Customer> {
	static MyOwnFocusTraversalPolicy newPolicy;
	private JTextArea tfAddress;
	private FixedLengthTextField tfCity;
	private FixedLengthTextField tfZip;
	private FixedLengthTextField tfFirstName;
	private FixedLengthTextField tfLastName;
	private FixedLengthTextField tfName;
	private JTextField tfState;
	private JTextField tfCellPhone;
	private QwertyKeyPad qwertyKeyPad;

	public boolean isKeypad;

	public QuickCustomerForm() {
		createCustomerForm();
	}

	public QuickCustomerForm(boolean enable) {
		isKeypad = enable;
		createCustomerForm();
	}

	private void createCustomerForm() {
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setOpaque(true);
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new MigLayout("insets 10 10 10 10", "[][][][]", "[][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Customer Information"));

		JLabel lblAddress = new JLabel(Messages.getString("CustomerForm.18")); //$NON-NLS-1$
		tfAddress = new JTextArea(new FixedLengthDocument(220));
		JScrollPane scrlDescription = new JScrollPane(tfAddress);
		scrlDescription.setPreferredSize(PosUIManager.getSize(338, 52));

		JLabel lblZip = new JLabel(Messages.getString("CustomerForm.21")); //$NON-NLS-1$
		tfZip = new FixedLengthTextField(30);

		JLabel lblCitytown = new JLabel(Messages.getString("CustomerForm.24")); //$NON-NLS-1$
		tfCity = new FixedLengthTextField();

		JLabel lblState = new JLabel(Messages.getString("QuickCustomerForm.0")); //$NON-NLS-1$
		tfState = new JTextField(30);

		JLabel lblCellPhone = new JLabel(Messages.getString("CustomerForm.32")); //$NON-NLS-1$

		inputPanel.add(lblCellPhone, "cell 0 1,alignx right"); //$NON-NLS-1$
		tfCellPhone = new JTextField(30);
		inputPanel.add(tfCellPhone, "cell 1 1"); //$NON-NLS-1$
		//setPreferredSize(PosUIManager.getSize(800, 350));

		JLabel lblFirstName = new JLabel(Messages.getString("CustomerForm.3")); //$NON-NLS-1$

		//inputPanel.add(lblFirstName, "cell 0 2,alignx right"); //$NON-NLS-1$
		tfFirstName = new FixedLengthTextField();
		//inputPanel.add(tfFirstName, "cell 1 2"); //$NON-NLS-1$

		JLabel lblLastName = new JLabel(Messages.getString("CustomerForm.11")); //$NON-NLS-1$

		//inputPanel.add(lblLastName, "cell 0 3,alignx right"); //$NON-NLS-1$
		tfLastName = new FixedLengthTextField();
		//inputPanel.add(tfLastName, "cell 1 3"); //$NON-NLS-1$

		JLabel lblName = new JLabel("Name"); //$NON-NLS-1$

		inputPanel.add(lblName, "cell 0 3,alignx right"); //$NON-NLS-1$
		tfName = new FixedLengthTextField();
		tfName.setLength(120);
		inputPanel.add(tfName, "cell 1 3"); //$NON-NLS-1$

		inputPanel.add(lblZip, "cell 0 4,right"); //$NON-NLS-1$
		inputPanel.add(tfZip, "cell 1 4"); //$NON-NLS-1$

		inputPanel.add(lblCitytown, "cell 0 5,right"); //$NON-NLS-1$
		inputPanel.add(tfCity, "cell 1 5"); //$NON-NLS-1$

		inputPanel.add(lblState, "cell 0 6,right"); //$NON-NLS-1$
		inputPanel.add(tfState, "cell 1 6"); //$NON-NLS-1$

		inputPanel.add(lblAddress, "cell 2 1 1 6,right"); //$NON-NLS-1$
		inputPanel.add(scrlDescription, "grow, cell 3 1 1 6"); //$NON-NLS-1$

		qwertyKeyPad = new QwertyKeyPad();

		add(inputPanel, BorderLayout.CENTER);

		if (isKeypad) {
			add(qwertyKeyPad, BorderLayout.SOUTH); //$NON-NLS-1$
		}

		tfZip.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getStateAndCityByZipCode();
			}
		});

		tfZip.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				getStateAndCityByZipCode();
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});

		enableCustomerFields(false);
		callOrderController();
	}

	public void callOrderController() {
		Vector<Component> order = new Vector<Component>();

		order.add(tfCellPhone);
		order.add(tfName);
		//order.add(tfFirstName);
		//order.add(tfLastName);
		order.add(tfZip);
		order.add(tfCity);
		order.add(tfState);
		order.add(tfAddress);

		newPolicy = new MyOwnFocusTraversalPolicy(order);

		this.setFocusCycleRoot(true);
		this.setFocusTraversalPolicy(newPolicy);
	}

	public void enableCustomerFields(boolean enable) {
		tfName.setEnabled(enable);
		tfLastName.setEnabled(enable);
		tfFirstName.setEnabled(enable);
		tfAddress.setEnabled(enable);
		tfCity.setEnabled(enable);
		tfZip.setEnabled(enable);
		tfCellPhone.setEnabled(enable);
	}

	@Override
	public void setFieldsEnable(boolean enable) {
		tfName.setEnabled(enable);
		tfFirstName.setEnabled(enable);
		tfLastName.setEnabled(enable);
		tfAddress.setEnabled(enable);
		tfCity.setEnabled(enable);
		tfZip.setEnabled(enable);
		tfCellPhone.setEnabled(enable);
	}

	public void setFieldsEditable(boolean editable) {
		tfName.setEditable(editable);
		tfFirstName.setEditable(editable);
		tfLastName.setEditable(editable);
		tfAddress.setEditable(editable);
		tfCity.setEditable(editable);
		tfZip.setEditable(editable);
		tfCellPhone.setEditable(editable);
	}

	@Override
	public void createNew() {
		setBean(new Customer());
		tfName.setText("");//$NON-NLS-1$
		tfFirstName.setText("");//$NON-NLS-1$
		tfLastName.setText("");//$NON-NLS-1$
		tfAddress.setText(""); //$NON-NLS-1$
		tfCity.setText(""); //$NON-NLS-1$
		tfZip.setText(""); //$NON-NLS-1$
		tfCellPhone.setText("");//$NON-NLS-1$
	}

	@Override
	public boolean save() {
		try {
			if (!updateModel())
				return false;
			Customer customer = (Customer) getBean();
			CustomerDAO.getInstance().saveOrUpdate(customer);
			updateView();
			return true;
		} catch (IllegalModelStateException e) {
		} catch (StaleObjectStateException e) {
			BOMessageDialog.showError(this, Messages.getString("CustomerForm.47")); //$NON-NLS-1$
		}
		return false;
	}

	@Override
	protected void updateView() {
		Customer customer = (Customer) getBean();
		if (customer == null) {
			return;
		}
		tfName.setText(customer.getName());
		tfFirstName.setText(customer.getFirstName());
		tfLastName.setText(customer.getLastName());
		tfCity.setText(customer.getCity());
		//tfZip.setText(customer.getState());
		//TODO: 
		tfState.setText(customer.getState());
		tfZip.setText(customer.getZipCode());
		tfCellPhone.setText(customer.getMobileNo());
		tfAddress.setText(customer.getAddress());
	}

	@Override
	protected boolean updateModel() throws IllegalModelStateException {
		String mobile = tfCellPhone.getText();
		String name = tfName.getText();
		String fullName[] = name.split(" ");
		String fname = fullName[0];
		String lastName = name.substring(fname.length(), name.length());

		if (StringUtils.isEmpty(mobile) && StringUtils.isEmpty(name)) {
			POSMessageDialog.showError(null, Messages.getString("QuickCustomerForm.1")); //$NON-NLS-1$
			return false;
		}
		Customer customer = (Customer) getBean();

		if (customer == null) {
			customer = new Customer();
			setBean(customer, false);
		}
		customer.setName(name);
		customer.setFirstName(fname);
		customer.setLastName(lastName);
		customer.setAddress(tfAddress.getText());
		customer.setCity(tfCity.getText());
		customer.setState(tfState.getText());
		//customer.setState(tfZip.getText());
		//TODO: 
		customer.setZipCode(tfZip.getText());
		customer.setMobileNo(tfCellPhone.getText());

		return true;
	}

	@Override
	public boolean delete() {
		try {
			Customer bean2 = getBean();
			if (bean2 == null)
				return false;

			int option = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getBackOfficeWindow(), "Are you sure to delete selected table?", "Confirm"); //$NON-NLS-1$ //$NON-NLS-2$
			if (option != JOptionPane.YES_OPTION) {
				return false;
			}
			CustomerDAO.getInstance().delete(bean2);
			return true;
		} catch (Exception e) {
			POSMessageDialog.showError(POSUtil.getBackOfficeWindow(), e.getMessage(), e);
		}
		return false;
	}

	@Override
	public String getDisplayText() {
		return Messages.getString("CustomerForm.54"); //$NON-NLS-1$
	}

	public static class MyOwnFocusTraversalPolicy extends FocusTraversalPolicy {
		Vector<Component> order;

		public MyOwnFocusTraversalPolicy(Vector<Component> order) {
			this.order = new Vector<Component>(order.size());
			this.order.addAll(order);
		}

		public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
			int idx = (order.indexOf(aComponent) + 1) % order.size();
			return order.get(idx);
		}

		public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
			int idx = order.indexOf(aComponent) - 1;
			if (idx < 0) {
				idx = order.size() - 1;
			}
			return order.get(idx);
		}

		public Component getDefaultComponent(Container focusCycleRoot) {
			return order.get(0);
		}

		public Component getLastComponent(Container focusCycleRoot) {
			return order.lastElement();
		}

		public Component getFirstComponent(Container focusCycleRoot) {
			return order.get(0);
		}
	}

	private void getStateAndCityByZipCode() {

		String zipCode = tfZip.getText();

		if (zipCode == null || zipCode.isEmpty()) {
			tfState.setText(""); //$NON-NLS-1$
			tfCity.setText(""); //$NON-NLS-1$
			return;
		}

		String city = ZipCodeUtil.getCity(zipCode);
		String state = ZipCodeUtil.getState(zipCode);

		tfState.setText(state);
		tfCity.setText(city);
	}

	public void setPhoneNo(String phoneNo) {
		tfCellPhone.setText(phoneNo);
	}
}
