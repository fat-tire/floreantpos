package com.floreantpos.ui.forms;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.main.Application;
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
import com.floreantpos.util.PosGuiUtil;

public class CustomerForm extends BeanEditor<Customer> {
	private FixedLengthTextField tfLoyaltyNo;
	private FixedLengthTextField tfAddress;
	private FixedLengthTextField tfCity;
	private FixedLengthTextField tfZip;
	private FixedLengthTextField tfCountry;
	private DoubleTextField tfCreditLimit;
	private JCheckBox cbVip;
	private FixedLengthTextField tfName;
	private FixedLengthTextField tfPhone;
	private FixedLengthTextField tfEmail;
	private JLabel lblDob;
	private FixedLengthTextField tfDoB;
	private JPanel panel;
	private QwertyKeyPad qwertyKeyPad;
	private JLabel lblLoyaltyPoint;
	private IntegerTextField tfLoyaltyPoint;
	private JLabel lblPicture;
	private JPanel panel_1;
	private PosSmallButton btnSelectImage;
	private PosSmallButton btnClearImage;
	
	private BufferedImage image;
	
	public CustomerForm() {
		setLayout(new MigLayout("", "[][grow][][grow][]", "[19px][][][][][grow][grow]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		JLabel lblName = new JLabel(Messages.getString("CustomerForm.3")); //$NON-NLS-1$
		add(lblName, "cell 0 0,alignx trailing,aligny center"); //$NON-NLS-1$
		
		tfName = new FixedLengthTextField(60);
		tfName.setLength(60);
		add(tfName, "cell 1 0,growx,aligny top"); //$NON-NLS-1$
		
		lblDob = new JLabel("DoB (MM-DD-YYYY)"); //$NON-NLS-1$
		add(lblDob, "cell 2 0,alignx trailing"); //$NON-NLS-1$
		
		tfDoB = new FixedLengthTextField();
		tfDoB.setLength(16);
		add(tfDoB, "cell 3 0,growx"); //$NON-NLS-1$
		
		lblPicture = new JLabel(""); //$NON-NLS-1$
		lblPicture.setPreferredSize(new Dimension(120, 120));
		lblPicture.setIconTextGap(0);
		lblPicture.setHorizontalAlignment(SwingConstants.CENTER);
		lblPicture.setBorder(new TitledBorder(null, Messages.getString("CustomerForm.10"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		add(lblPicture, "cell 4 0 1 5,grow"); //$NON-NLS-1$
		
		JLabel lblPhone = new JLabel(Messages.getString("CustomerForm.12")); //$NON-NLS-1$
		add(lblPhone, "cell 0 1,alignx trailing"); //$NON-NLS-1$
		
		tfPhone = new FixedLengthTextField(30);
		tfPhone.setLength(30);
		add(tfPhone, "cell 1 1,growx"); //$NON-NLS-1$
		
		JLabel lblEmail = new JLabel(Messages.getString("CustomerForm.15")); //$NON-NLS-1$
		add(lblEmail, "cell 2 1,alignx trailing"); //$NON-NLS-1$
		
		tfEmail = new FixedLengthTextField(40);
		tfEmail.setLength(40);
		add(tfEmail, "flowx,cell 3 1,growx"); //$NON-NLS-1$
		
		JLabel lblAddress = new JLabel(Messages.getString("CustomerForm.18")); //$NON-NLS-1$
		add(lblAddress, "cell 0 2,alignx trailing"); //$NON-NLS-1$
		
		tfAddress = new FixedLengthTextField(120);
		tfAddress.setLength(120);
		add(tfAddress, "cell 1 2,growx"); //$NON-NLS-1$
		
		JLabel lblZip = new JLabel(Messages.getString("CustomerForm.21")); //$NON-NLS-1$
		add(lblZip, "flowx,cell 2 2,alignx trailing"); //$NON-NLS-1$
		
		tfZip = new FixedLengthTextField(30);
		tfZip.setLength(30);
		add(tfZip, "cell 3 2,growx"); //$NON-NLS-1$
		
		JLabel lblCitytown = new JLabel(Messages.getString("CustomerForm.24")); //$NON-NLS-1$
		add(lblCitytown, "cell 0 3,alignx trailing"); //$NON-NLS-1$
		
		tfCity = new FixedLengthTextField(30);
		tfCity.setLength(30);
		add(tfCity, "flowx,cell 1 3,growx"); //$NON-NLS-1$
		
		JLabel lblCountry = new JLabel(Messages.getString("CustomerForm.27")); //$NON-NLS-1$
		add(lblCountry, "cell 2 3,alignx trailing"); //$NON-NLS-1$
		
		tfCountry = new FixedLengthTextField(30);
		tfCountry.setText(Messages.getString("CustomerForm.29")); //$NON-NLS-1$
		tfCountry.setLength(30);
		add(tfCountry, "cell 3 3,growx"); //$NON-NLS-1$
		
		JLabel lblLoyaltyNo = new JLabel(Messages.getString("CustomerForm.31")); //$NON-NLS-1$
		add(lblLoyaltyNo, "cell 0 4,alignx trailing"); //$NON-NLS-1$
		
		tfLoyaltyNo = new FixedLengthTextField(30);
		tfLoyaltyNo.setLength(30);
		add(tfLoyaltyNo, "cell 1 4"); //$NON-NLS-1$
		
		lblLoyaltyPoint = new JLabel(Messages.getString("CustomerForm.34")); //$NON-NLS-1$
		add(lblLoyaltyPoint, "cell 2 4,alignx trailing"); //$NON-NLS-1$
		
		tfLoyaltyPoint = new IntegerTextField();
		tfLoyaltyPoint.setColumns(10);
		add(tfLoyaltyPoint, "cell 3 4"); //$NON-NLS-1$
		
		JLabel lblCreditLimit = new JLabel(Messages.getString("CustomerForm.37")); //$NON-NLS-1$
		add(lblCreditLimit, "cell 0 5,alignx trailing,aligny top"); //$NON-NLS-1$
		
		tfCreditLimit = new DoubleTextField();
		tfCreditLimit.setText("500.00"); //$NON-NLS-1$
		tfCreditLimit.setColumns(10);
		add(tfCreditLimit, "cell 1 5,aligny top"); //$NON-NLS-1$
		
		cbVip = new JCheckBox(Messages.getString("CustomerForm.41")); //$NON-NLS-1$
		cbVip.setFocusable(false);
		add(cbVip, "cell 3 5,alignx leading,aligny top"); //$NON-NLS-1$
		
		panel_1 = new JPanel();
		add(panel_1, "cell 4 5,growx,aligny top"); //$NON-NLS-1$
		
		btnSelectImage = new PosSmallButton();
		btnSelectImage.setText(Messages.getString("CustomerForm.44")); //$NON-NLS-1$
		panel_1.add(btnSelectImage);
		
		btnClearImage = new PosSmallButton();
		btnClearImage.setText(Messages.getString("CustomerForm.45")); //$NON-NLS-1$
		panel_1.add(btnClearImage);
		
		panel = new JPanel();
		add(panel, "cell 0 6 5 1,grow"); //$NON-NLS-1$
		
		qwertyKeyPad = new QwertyKeyPad();
		panel.add(qwertyKeyPad);
		
		btnSelectImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					image = PosGuiUtil.selectImageFile();
					if(image == null) {
						return;
					}
					
					ImageIcon imageIcon = new ImageIcon(image);
					lblPicture.setIcon(imageIcon);
				} catch (Exception e1) {
					e1.printStackTrace();
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
	}
	
	public void setFieldsEditable(boolean editable) {
		tfName.setEditable(editable);
		tfPhone.setEditable(editable);
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
	}

	@Override
	public boolean save() {
		 try {
			if (!updateModel())
				return false;

			Customer customer = (Customer) getBean();
			CustomerDAO.getInstance().saveOrUpdate(customer);
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
		
		if(customer != null) {
			tfName.setText(customer.getName());
			tfDoB.setText(customer.getDob());
			tfAddress.setText(customer.getAddress());
			tfCity.setText(customer.getCity());
			tfCountry.setText(customer.getCountry());
			tfCreditLimit.setText(String.valueOf(customer.getCreditLimit()));
			tfEmail.setText(customer.getEmail());
			tfLoyaltyNo.setText(customer.getLoyaltyNo());
			tfLoyaltyPoint.setText(customer.getLoyaltyPoint().toString());
			tfPhone.setText(customer.getTelephoneNo());
			tfZip.setText(customer.getState());
			cbVip.setSelected(customer.isVip());
			
			byte[] picture = customer.getPicture();
			if(picture != null) {
				lblPicture.setIcon(new ImageIcon(picture));
			}
			else {
				setDefaultCustomerPicture();
			}
		}
		else {
			tfName.setText(""); //$NON-NLS-1$
			tfDoB.setText(""); //$NON-NLS-1$
			tfAddress.setText(""); //$NON-NLS-1$
			tfCity.setText(""); //$NON-NLS-1$
			tfCountry.setText(""); //$NON-NLS-1$
			tfCreditLimit.setText(""); //$NON-NLS-1$
			tfEmail.setText(""); //$NON-NLS-1$
			tfLoyaltyNo.setText(""); //$NON-NLS-1$
			tfLoyaltyPoint.setText(""); //$NON-NLS-1$
			tfPhone.setText(""); //$NON-NLS-1$
			tfZip.setText(""); //$NON-NLS-1$
			cbVip.setSelected(false);
			setDefaultCustomerPicture();
		}
	}

	private void setDefaultCustomerPicture() {
		try {
			InputStream stream = getClass().getResourceAsStream("/images/generic-profile-pic-v2.png"); //$NON-NLS-1$
			byte[] picture2 = IOUtils.toByteArray(stream);
			IOUtils.closeQuietly(stream);
			lblPicture.setIcon(new ImageIcon(picture2));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean updateModel() throws IllegalModelStateException {
		String phoneString = tfPhone.getText();
		String name = tfName.getText();
		String email = tfEmail.getText();
		
		if(StringUtils.isEmpty(phoneString) && StringUtils.isEmpty(name) && StringUtils.isEmpty(email)) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("CustomerForm.60")); //$NON-NLS-1$
			return false;
		}
		
		Customer customer = (Customer) getBean();
		
		if(customer == null) {
			customer = new Customer();
			setBean(customer, false);
		}
		
		customer.setName(tfName.getText());
		customer.setDob(tfDoB.getText());
		customer.setAddress(tfAddress.getText());
		customer.setCity(tfCity.getText());
		customer.setCountry(tfCountry.getText());
		customer.setCreditLimit(PosGuiUtil.parseDouble(tfCreditLimit));
		customer.setEmail(tfEmail.getText());
		customer.setLoyaltyNo(tfLoyaltyNo.getText());
		customer.setLoyaltyPoint(tfLoyaltyPoint.getInteger());
		customer.setTelephoneNo(tfPhone.getText());
		customer.setState(tfZip.getText());
		customer.setVip(cbVip.isSelected());
		
		if(image != null) {
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
	public String getDisplayText() {
		if(editMode) {
			return Messages.getString("CustomerForm.62"); //$NON-NLS-1$
		}
		return Messages.getString("CustomerForm.63"); //$NON-NLS-1$
	}
}
