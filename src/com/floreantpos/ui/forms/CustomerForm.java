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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;

import com.floreantpos.Messages;
import com.floreantpos.PosLog;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.Customer;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.PosSmallButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.PosGuiUtil;

public class CustomerForm extends BeanEditor<Customer> {
	static MyOwnFocusTraversalPolicy newPolicy;
	private FixedLengthTextField tfLoyaltyNo;
	private JTextField tfAddress;
	private FixedLengthTextField tfCity;
	private FixedLengthTextField tfZip;
	private FixedLengthTextField tfCountry;
	private DoubleTextField tfCreditLimit;
	private JCheckBox cbVip;
	private FixedLengthTextField tfFirstName;
	private FixedLengthTextField tfLastName;
	private FixedLengthTextField tfEmail;
	private JLabel lblDob;
	private FixedLengthTextField tfDoB;
	private JLabel lblLoyaltyPoint;
	private IntegerTextField tfLoyaltyPoint;
	private JLabel lblPicture;
	private JPanel picturePanel;
	private PosSmallButton btnSelectImage;
	private PosSmallButton btnClearImage;
	private BufferedImage image;
	private JComboBox cbSalutation;
	private JLabel lblHomePhone;
	private JLabel lblWorkPhone;
	private JLabel lblMobile;
	private JLabel lblSocialSecurityNumber;
	private FixedLengthTextField tfHomePhone;
	private FixedLengthTextField tfWorkPhone;
	private IntegerTextField tfMobile;
	private FixedLengthTextField tfSocialSecurityNumber;
	private QwertyKeyPad qwertyKeyPad;

	public boolean isKeypad;

	public CustomerForm() {
		createCustomerForm();
	}

	public CustomerForm(boolean enable) {
		isKeypad = enable;
		createCustomerForm();

	}

	private void createCustomerForm() {
		setOpaque(true);
		setLayout(new MigLayout("", "[][][grow][][grow]", "[][][][][][][][][][][][][][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		picturePanel = new JPanel(new MigLayout());

		lblPicture = new JLabel(""); //$NON-NLS-1$
		lblPicture.setIconTextGap(0);
		lblPicture.setHorizontalAlignment(SwingConstants.CENTER);
		picturePanel.setBorder(new TitledBorder(null, Messages.getString("CustomerForm.10"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		picturePanel.add(lblPicture, "wrap,center"); //$NON-NLS-1$

		btnSelectImage = new PosSmallButton();
		btnSelectImage.setText(Messages.getString("CustomerForm.44")); //$NON-NLS-1$
		picturePanel.add(btnSelectImage, "split 2"); //$NON-NLS-1$

		btnClearImage = new PosSmallButton();
		btnClearImage.setText(Messages.getString("CustomerForm.45")); //$NON-NLS-1$
		picturePanel.add(btnClearImage);

		add(picturePanel, "cell 0 0 0 8"); //$NON-NLS-1$

		JLabel lblSalutation = new JLabel(Messages.getString("CustomerForm.0")); //$NON-NLS-1$
		add(lblSalutation, "cell 1 0,right"); //$NON-NLS-1$

		cbSalutation = new JComboBox();
		cbSalutation.addItem(Messages.getString("CustomerForm.2")); //$NON-NLS-1$
		cbSalutation.addItem(Messages.getString("CustomerForm.4")); //$NON-NLS-1$
		cbSalutation.addItem(Messages.getString("CustomerForm.5")); //$NON-NLS-1$
		cbSalutation.addItem(Messages.getString("CustomerForm.6")); //$NON-NLS-1$

		cbSalutation.setPreferredSize(new Dimension(100, 0));

		add(cbSalutation, "cell 2 0,grow"); //$NON-NLS-1$

		JLabel lblFirstName = new JLabel(Messages.getString("CustomerForm.3")); //$NON-NLS-1$

		add(lblFirstName, "cell 1 1,right "); //$NON-NLS-1$

		tfFirstName = new FixedLengthTextField(30);
		add(tfFirstName, "cell 2 1,grow"); //$NON-NLS-1$
		//tfFirstName.setFocusTraversalPolicy(policy)

		JLabel lblLastName = new JLabel(Messages.getString("CustomerForm.11")); //$NON-NLS-1$
		add(lblLastName, "cell 1 2,right"); //$NON-NLS-1$

		tfLastName = new FixedLengthTextField();
		add(tfLastName, "cell 2 2,grow"); //$NON-NLS-1$

		lblDob = new JLabel("DoB (MM-DD-YYYY)"); //$NON-NLS-1$
		add(lblDob, "cell 1 3,right"); //$NON-NLS-1$

		tfDoB = new FixedLengthTextField();
		add(tfDoB, "cell 2 3,grow"); //$NON-NLS-1$

		JLabel lblAddress = new JLabel(Messages.getString("CustomerForm.18")); //$NON-NLS-1$
		add(lblAddress, "cell 1 4,right"); //$NON-NLS-1$

		tfAddress = new JTextField();
		add(tfAddress, "cell 2 4,grow"); //$NON-NLS-1$

		JLabel lblZip = new JLabel(Messages.getString("CustomerForm.21")); //$NON-NLS-1$
		add(lblZip, "cell 1 5,right"); //$NON-NLS-1$

		tfZip = new FixedLengthTextField();
		add(tfZip, "cell 2 5,grow"); //$NON-NLS-1$

		lblSocialSecurityNumber = new JLabel(Messages.getString("CustomerForm.22")); //$NON-NLS-1$
		add(lblSocialSecurityNumber, "cell 3 0,right"); //$NON-NLS-1$

		tfSocialSecurityNumber = new FixedLengthTextField();
		add(tfSocialSecurityNumber, "cell 4 0,grow"); //$NON-NLS-1$

		JLabel lblCitytown = new JLabel(Messages.getString("CustomerForm.24")); //$NON-NLS-1$
		add(lblCitytown, "cell 3 1,right"); //$NON-NLS-1$
		//
		tfCity = new FixedLengthTextField();
		add(tfCity, "cell 4 1,grow"); //$NON-NLS-1$

		JLabel lblCountry = new JLabel(Messages.getString("CustomerForm.27")); //$NON-NLS-1$
		add(lblCountry, "cell 3 2,right"); //$NON-NLS-1$

		tfCountry = new FixedLengthTextField();
		tfCountry.setText(Messages.getString("CustomerForm.29")); //$NON-NLS-1$
		add(tfCountry, "cell 4 2,grow"); //$NON-NLS-1$

		lblMobile = new JLabel(Messages.getString("CustomerForm.32")); //$NON-NLS-1$
		add(lblMobile, "cell 3 3 ,right"); //$NON-NLS-1$

		tfMobile = new IntegerTextField(10);
		add(tfMobile, "cell 4 3,grow"); //$NON-NLS-1$

		lblHomePhone = new JLabel("Home Phone");//$NON-NLS-1$
		add(lblHomePhone, "cell 3 4,right"); //$NON-NLS-1$

		tfHomePhone = new FixedLengthTextField();
		add(tfHomePhone, "cell 4 4,grow"); //$NON-NLS-1$

		lblWorkPhone = new JLabel(Messages.getString("CustomerForm.39")); //$NON-NLS-1$
		add(lblWorkPhone, "cell 3 5,right"); //$NON-NLS-1$

		tfWorkPhone = new FixedLengthTextField();
		add(tfWorkPhone, "cell 4 5,grow"); //$NON-NLS-1$

		JLabel lblEmail = new JLabel(Messages.getString("CustomerForm.15")); //$NON-NLS-1$
		add(lblEmail, "cell 3 6 ,right"); //$NON-NLS-1$

		tfEmail = new FixedLengthTextField();
		add(tfEmail, "cell 4 6,grow"); //$NON-NLS-1$

		lblLoyaltyPoint = new JLabel(Messages.getString("CustomerForm.34")); //$NON-NLS-1$
		add(lblLoyaltyPoint, "cell 3 7,right"); //$NON-NLS-1$

		tfLoyaltyPoint = new IntegerTextField();
		add(tfLoyaltyPoint, "cell 4 7,grow"); //$NON-NLS-1$

		cbVip = new JCheckBox(Messages.getString("CustomerForm.41")); //$NON-NLS-1$
		cbVip.setFocusable(false);
		add(cbVip, "cell 4 8,wrap"); //$NON-NLS-1$

		JLabel lblLoyaltyNo = new JLabel(Messages.getString("CustomerForm.31")); //$NON-NLS-1$
		add(lblLoyaltyNo, "cell 1 6,right"); //$NON-NLS-1$

		tfLoyaltyNo = new FixedLengthTextField();
		tfLoyaltyNo.setLength(8);
		add(tfLoyaltyNo, "cell 2 6,grow"); //$NON-NLS-1$

		JLabel lblCreditLimit = new JLabel(Messages.getString("CustomerForm.37")); //$NON-NLS-1$
		add(lblCreditLimit, "cell 1 7,right"); //$NON-NLS-1$

		tfCreditLimit = new DoubleTextField();
		tfCreditLimit.setText("500.00"); //$NON-NLS-1$
		add(tfCreditLimit, "cell 2 7,grow"); //$NON-NLS-1$

		qwertyKeyPad = new QwertyKeyPad();

		if (isKeypad) {
			add(qwertyKeyPad, "cell 0 10 5 5,grow"); //$NON-NLS-1$
		}

		btnSelectImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					BufferedImage tmpImage;
					tmpImage = PosGuiUtil.selectImageFile();
					if (tmpImage != null) {
						image = tmpImage;
					}
					if (image == null) {
						return;
					}
					ImageIcon imageIcon = new ImageIcon(image);
					lblPicture.setIcon(imageIcon);
				} catch (Exception e1) {
					PosLog.error(getClass(), e1);
				}
			}
		});
		btnClearImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDefaultCustomerPicture();
			}
		});

		setDefaultCustomerPicture();
		enableCustomerFields(false);
		callOrderController();
	}

	public void callOrderController() {
		Vector<Component> order = new Vector<Component>();
		order.add(tfFirstName);
		order.add(tfLastName);
		order.add(tfDoB);
		order.add(tfAddress);
		order.add(tfZip);
		order.add(tfLoyaltyNo);
		order.add(tfCreditLimit);
		order.add(tfSocialSecurityNumber);
		order.add(tfCity);
		order.add(tfCountry);
		order.add(tfMobile);
		order.add(tfHomePhone);
		order.add(tfWorkPhone);
		order.add(tfEmail);
		order.add(tfLoyaltyPoint);

		newPolicy = new MyOwnFocusTraversalPolicy(order);

		this.setFocusCycleRoot(true);
		this.setFocusTraversalPolicy(newPolicy);
		/*JFrame frame=new JFrame();
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		    //Create and set up the content pane.
		    JComponent newContentPane = this;
		    newContentPane.setOpaque(true); //content panes must be opaque
		    frame.setContentPane(newContentPane);
		    frame.setFocusTraversalPolicy(newPolicy);
		    //Display the window.
		    frame.pack();
		    frame.setVisible(true);*/
	}

	public void enableCustomerFields(boolean enable) {
		cbSalutation.setEnabled(enable);
		tfLastName.setEnabled(enable);
		tfFirstName.setEnabled(enable);
		tfEmail.setEnabled(enable);
		tfLoyaltyNo.setEnabled(enable);
		tfAddress.setEnabled(enable);
		tfCity.setEnabled(enable);
		tfCreditLimit.setEnabled(enable);
		tfZip.setEnabled(enable);
		tfCountry.setEnabled(enable);
		cbVip.setEnabled(enable);
		tfDoB.setEnabled(enable);
		btnClearImage.setEnabled(enable);
		btnSelectImage.setEnabled(enable);
		tfLoyaltyPoint.setEnabled(enable);

		tfHomePhone.setEnabled(enable);
		tfWorkPhone.setEnabled(enable);
		tfMobile.setEnabled(enable);
		tfSocialSecurityNumber.setEnabled(enable);
	}

	@Override
	public void setFieldsEnable(boolean enable) {
		cbSalutation.setEnabled(enable);
		tfFirstName.setEnabled(enable);
		tfLastName.setEnabled(enable);
		tfEmail.setEnabled(enable);
		tfLoyaltyNo.setEnabled(enable);
		tfAddress.setEnabled(enable);
		tfCity.setEnabled(enable);
		tfCreditLimit.setEnabled(enable);
		tfZip.setEnabled(enable);
		tfCountry.setEnabled(enable);
		cbVip.setEnabled(enable);
		tfDoB.setEnabled(enable);
		btnClearImage.setEnabled(enable);
		btnSelectImage.setEnabled(enable);
		tfLoyaltyPoint.setEnabled(enable);

		tfHomePhone.setEnabled(enable);
		tfWorkPhone.setEnabled(enable);
		tfMobile.setEnabled(enable);
		tfSocialSecurityNumber.setEnabled(enable);
	}

	public void setFieldsEditable(boolean editable) {
		cbSalutation.setEditable(editable);
		tfFirstName.setEditable(editable);
		tfLastName.setEditable(editable);
		tfEmail.setEditable(editable);
		tfLoyaltyNo.setEditable(editable);
		tfAddress.setEditable(editable);
		tfCity.setEditable(editable);
		tfCreditLimit.setEditable(editable);
		tfZip.setEditable(editable);
		tfCountry.setEditable(editable);
		cbVip.setEnabled(editable);
		tfDoB.setEditable(editable);
		btnClearImage.setEnabled(editable);
		btnSelectImage.setEnabled(editable);
		tfLoyaltyPoint.setEditable(editable);

		tfHomePhone.setEditable(editable);
		tfWorkPhone.setEditable(editable);
		tfMobile.setEditable(editable);
		tfSocialSecurityNumber.setEditable(editable);
	}

	@Override
	public void createNew() {
		setBean(new Customer());
		tfFirstName.setText("");//$NON-NLS-1$
		tfLastName.setText("");//$NON-NLS-1$
		cbSalutation.setSelectedIndex(0);
		tfDoB.setText(""); //$NON-NLS-1$
		tfAddress.setText(""); //$NON-NLS-1$
		tfCity.setText(""); //$NON-NLS-1$
		tfCountry.setText(""); //$NON-NLS-1$
		tfCreditLimit.setText(""); //$NON-NLS-1$
		tfEmail.setText(""); //$NON-NLS-1$
		tfLoyaltyNo.setText(""); //$NON-NLS-1$
		tfLoyaltyPoint.setText(""); //$NON-NLS-1$
		tfHomePhone.setText(""); //$NON-NLS-1$
		tfZip.setText(""); //$NON-NLS-1$
		cbVip.setSelected(false);
		tfWorkPhone.setText("");//$NON-NLS-1$
		tfMobile.setText("");//$NON-NLS-1$
		tfSocialSecurityNumber.setText("");//$NON-NLS-1$
		setDefaultCustomerPicture();
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
		cbSalutation.setSelectedItem(customer.getSalutation());
		tfFirstName.setText(customer.getFirstName());
		tfLastName.setText(customer.getLastName());
		tfDoB.setText(customer.getDob());
		tfAddress.setText(customer.getAddress());
		tfCity.setText(customer.getCity());
		tfCountry.setText(customer.getCountry());
		tfCreditLimit.setText(String.valueOf(customer.getCreditLimit()));
		tfEmail.setText(customer.getEmail());
		tfLoyaltyNo.setText(customer.getLoyaltyNo());
		tfLoyaltyPoint.setText(customer.getLoyaltyPoint().toString());
		tfHomePhone.setText(customer.getHomePhoneNo());
		tfZip.setText(customer.getZipCode());
		cbVip.setSelected(customer.isVip());
		tfWorkPhone.setText(customer.getWorkPhoneNo());
		tfMobile.setText(customer.getMobileNo());
		if (customer.getSocialSecurityNumber() != null) {
			tfSocialSecurityNumber.setText(String.valueOf(customer.getSocialSecurityNumber()));
		}

		byte[] picture = customer.getPicture();
		if (picture != null) {
			lblPicture.setIcon(new ImageIcon(picture));
		}
		else {
			setDefaultCustomerPicture();
		}

		/*else {
			cbSalutaion.setSelectedIndex(0);
			tfFirstName.setText(""); //$NON-NLS-1$
			tfLastName.setText("");
			tfDoB.setText(""); //$NON-NLS-1$
			tfAddress.setText(""); //$NON-NLS-1$
			tfCity.setText(""); //$NON-NLS-1$
			tfCountry.setText(""); //$NON-NLS-1$
			tfCreditLimit.setText(""); //$NON-NLS-1$
			tfEmail.setText(""); //$NON-NLS-1$
			tfLoyaltyNo.setText(""); //$NON-NLS-1$
			tfLoyaltyPoint.setText(""); //$NON-NLS-1$
			tfHomePhone.setText(""); //$NON-NLS-1$
			tfZip.setText(""); //$NON-NLS-1$
			cbVip.setSelected(false);
			tfWorkPhone.setText("");
			tfMobile.setText("");
			tfNationalId.setText("");
			setDefaultCustomerPicture();
		}*/
	}

	private void setDefaultCustomerPicture() {
		try {
			InputStream stream = getClass().getResourceAsStream("/images/generic-profile-pic-v2.png"); //$NON-NLS-1$
			byte[] picture2 = IOUtils.toByteArray(stream);
			IOUtils.closeQuietly(stream);
			lblPicture.setIcon(new ImageIcon(picture2));
		} catch (IOException e) {
			PosLog.error(getClass(), e);
		}
	}

	@Override
	protected boolean updateModel() throws IllegalModelStateException {
		String mobile = tfMobile.getText();
		String fname = tfFirstName.getText();
		String loyaltyNo = tfLoyaltyNo.getText();

		if (StringUtils.isEmpty(mobile) && StringUtils.isEmpty(fname) && StringUtils.isEmpty(loyaltyNo)) {
			POSMessageDialog.showError(null, Messages.getString("CustomerForm.60")); //$NON-NLS-1$
			return false;
		}
		Customer customer = (Customer) getBean();

		if (customer == null) {
			customer = new Customer();
			setBean(customer, false);
		}
		customer.setSalutation(cbSalutation.getSelectedItem().toString());
		customer.setFirstName(tfFirstName.getText());
		customer.setLastName(tfLastName.getText());
		customer.setDob(tfDoB.getText());
		customer.setAddress(tfAddress.getText());
		customer.setCity(tfCity.getText());
		customer.setCountry(tfCountry.getText());
		customer.setCreditLimit(PosGuiUtil.parseDouble(tfCreditLimit));
		customer.setEmail(tfEmail.getText());
		customer.setLoyaltyNo(tfLoyaltyNo.getText());
		customer.setLoyaltyPoint(tfLoyaltyPoint.getInteger());
		customer.setHomePhoneNo(tfHomePhone.getText());
		//customer.setState(tfZip.getText());
		customer.setZipCode(tfZip.getText());
		customer.setVip(cbVip.isSelected());
		customer.setMobileNo(tfMobile.getText());
		customer.setSocialSecurityNumber(tfSocialSecurityNumber.getText());
		customer.setWorkPhoneNo(tfWorkPhone.getText());

		if (image != null) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, "jpg", baos); //$NON-NLS-1$
				byte[] bytes = baos.toByteArray();
				customer.setPicture(bytes);
			} catch (Exception e) {
				//
			}
		}
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
		/*			if (editMode) {
						return Messages.getString("CustomerExplorerForm.19"); //$NON-NLS-1$
					}
					return Messages.getString("CustomerExplorerForm.20"); //$NON-NLS-1$
		*/
		return Messages.getString("CustomerForm.54"); //$NON-NLS-1$
	}

	//

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
}
