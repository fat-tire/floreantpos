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
		setLayout(new MigLayout("", "[][grow][][grow][]", "[19px][][][][][grow][grow]"));
		
		JLabel lblName = new JLabel("Name");
		add(lblName, "cell 0 0,alignx trailing,aligny center");
		
		tfName = new FixedLengthTextField(60);
		tfName.setLength(60);
		add(tfName, "cell 1 0,growx,aligny top");
		
		lblDob = new JLabel("DoB (MM-DD-YYYY)");
		add(lblDob, "cell 2 0,alignx trailing");
		
		tfDoB = new FixedLengthTextField();
		tfDoB.setLength(16);
		add(tfDoB, "cell 3 0,growx");
		
		lblPicture = new JLabel("");
		lblPicture.setPreferredSize(new Dimension(120, 120));
		lblPicture.setIconTextGap(0);
		lblPicture.setHorizontalAlignment(SwingConstants.CENTER);
		lblPicture.setBorder(new TitledBorder(null, "Picture", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(lblPicture, "cell 4 0 1 5,grow");
		
		JLabel lblPhone = new JLabel("Phone");
		add(lblPhone, "cell 0 1,alignx trailing");
		
		tfPhone = new FixedLengthTextField(30);
		tfPhone.setLength(30);
		add(tfPhone, "cell 1 1,growx");
		
		JLabel lblEmail = new JLabel("E-mail");
		add(lblEmail, "cell 2 1,alignx trailing");
		
		tfEmail = new FixedLengthTextField(40);
		tfEmail.setLength(40);
		add(tfEmail, "flowx,cell 3 1,growx");
		
		JLabel lblAddress = new JLabel("Address");
		add(lblAddress, "cell 0 2,alignx trailing");
		
		tfAddress = new FixedLengthTextField(120);
		tfAddress.setLength(120);
		add(tfAddress, "cell 1 2,growx");
		
		JLabel lblZip = new JLabel("Zip code");
		add(lblZip, "flowx,cell 2 2,alignx trailing");
		
		tfZip = new FixedLengthTextField(30);
		tfZip.setLength(30);
		add(tfZip, "cell 3 2,growx");
		
		JLabel lblCitytown = new JLabel("City");
		add(lblCitytown, "cell 0 3,alignx trailing");
		
		tfCity = new FixedLengthTextField(30);
		tfCity.setLength(30);
		add(tfCity, "flowx,cell 1 3,growx");
		
		JLabel lblCountry = new JLabel("Country");
		add(lblCountry, "cell 2 3,alignx trailing");
		
		tfCountry = new FixedLengthTextField(30);
		tfCountry.setText("USA");
		tfCountry.setLength(30);
		add(tfCountry, "cell 3 3,growx");
		
		JLabel lblLoyaltyNo = new JLabel("Loyalty No");
		add(lblLoyaltyNo, "cell 0 4,alignx trailing");
		
		tfLoyaltyNo = new FixedLengthTextField(30);
		tfLoyaltyNo.setLength(30);
		add(tfLoyaltyNo, "cell 1 4");
		
		lblLoyaltyPoint = new JLabel("Loyalty Point");
		add(lblLoyaltyPoint, "cell 2 4,alignx trailing");
		
		tfLoyaltyPoint = new IntegerTextField();
		tfLoyaltyPoint.setColumns(10);
		add(tfLoyaltyPoint, "cell 3 4");
		
		JLabel lblCreditLimit = new JLabel("Credit Limit ($)");
		add(lblCreditLimit, "cell 0 5,alignx trailing,aligny top");
		
		tfCreditLimit = new DoubleTextField();
		tfCreditLimit.setText("500.00");
		tfCreditLimit.setColumns(10);
		add(tfCreditLimit, "cell 1 5,aligny top");
		
		cbVip = new JCheckBox("VIP");
		cbVip.setFocusable(false);
		add(cbVip, "cell 3 5,alignx leading,aligny top");
		
		panel_1 = new JPanel();
		add(panel_1, "cell 4 5,growx,aligny top");
		
		btnSelectImage = new PosSmallButton();
		btnSelectImage.setText("SELECT");
		panel_1.add(btnSelectImage);
		
		btnClearImage = new PosSmallButton();
		btnClearImage.setText("CLEAR");
		panel_1.add(btnClearImage);
		
		panel = new JPanel();
		add(panel, "cell 0 6 5 1,grow");
		
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
			BOMessageDialog.showError(this, "It seems this Customer is modified by some other person or terminal. Save failed.");
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
			tfName.setText("");
			tfDoB.setText("");
			tfAddress.setText("");
			tfCity.setText("");
			tfCountry.setText("");
			tfCreditLimit.setText("");
			tfEmail.setText("");
			tfLoyaltyNo.setText("");
			tfLoyaltyPoint.setText("");
			tfPhone.setText("");
			tfZip.setText("");
			cbVip.setSelected(false);
			setDefaultCustomerPicture();
		}
	}

	private void setDefaultCustomerPicture() {
		try {
			InputStream stream = getClass().getResourceAsStream("/images/generic-profile-pic-v2.png");
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
			POSMessageDialog.showError(Application.getPosWindow(), "Please provide either name or phone or email");
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
				ImageIO.write(image, "jpg", baos);
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
			return "Edit customer";
		}
		return "Create customer";
	}
}
