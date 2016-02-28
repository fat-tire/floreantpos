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
package com.floreantpos.ui.model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.model.Discount;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.DiscountDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.ItemCheckBoxList;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.ItemSelectionDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class CouponForm extends BeanEditor implements ItemListener {
	private JPanel contentPane;
	private JPanel itemPanel;
	private FixedLengthTextField tfCouponName;
	private FixedLengthTextField tfBarcode;
	private JComboBox cbQualificationType;
	private JComboBox cbCouponType;
	private DoubleTextField tfCouponValue;
	private JCheckBox chkEnabled;
	private JCheckBox chkModifiable;
	private JCheckBox chkAutoApply;
	private JCheckBox chkNeverExpire;
	private JXDatePicker dpExperation;
	private JLabel lblMinimum;

	private DoubleTextField tfMinimumQua;

	private JPanel itemSearchPanel;

	private JTextField txtSearchItem;

	private JScrollPane itemScrollPane;

	private ItemCheckBoxList cbListItems;
	private ItemCheckBoxList addedListItems;

	private String uuid;

	public CouponForm() {
		this(new Discount());
	}

	public CouponForm(Discount coupon) {
		initializeComponent();

		cbCouponType.setModel(new DefaultComboBoxModel(Discount.COUPON_TYPE_NAMES));

		cbQualificationType.setModel(new DefaultComboBoxModel(Discount.COUPON_QUALIFICATION_NAMES));
		cbQualificationType.addItemListener(this);
		cbCouponType.addItemListener(this);

		setBean(coupon);
	}

	private void initializeComponent() {
		setLayout(new BorderLayout(10, 10));

		contentPane = new JPanel();
		contentPane.setLayout(new MigLayout());
		contentPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
		contentPane.setPreferredSize(new Dimension(400, 0));

		JLabel label1 = new JLabel(Messages.getString("CouponForm.0") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		JLabel label2 = new JLabel(Messages.getString("CouponForm.9") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		JLabel label3 = new JLabel(Messages.getString("CouponForm.11") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		JLabel label4 = new JLabel(Messages.getString("CouponForm.13") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		JLabel label6 = new JLabel(Messages.getString("CouponForm.12")); //$NON-NLS-1$
		JLabel label5 = new JLabel(Messages.getString("CouponForm.7")); //$NON-NLS-1$
		lblMinimum = new JLabel(Messages.getString("CouponForm.5")); //$NON-NLS-1$

		tfCouponName = new FixedLengthTextField(120);
		tfBarcode = new FixedLengthTextField(120);
		cbCouponType = new JComboBox();
		cbQualificationType = new JComboBox();
		dpExperation = new JXDatePicker();
		tfCouponValue = new DoubleTextField();
		tfMinimumQua = new DoubleTextField();
		chkEnabled = new JCheckBox(POSConstants.ENABLED); //$NON-NLS-1$
		chkModifiable = new JCheckBox("Modifiable Amount"); //$NON-NLS-1$
		chkAutoApply = new JCheckBox(Messages.getString("CouponForm.6")); //$NON-NLS-1$
		chkNeverExpire = new JCheckBox(Messages.getString("CouponForm.16")); //$NON-NLS-1$

		contentPane.add(label1);
		contentPane.add(tfCouponName, "grow, wrap"); //$NON-NLS-1$
		contentPane.add(label2);
		contentPane.add(dpExperation, "grow, wrap"); //$NON-NLS-1$
		contentPane.add(label3);
		contentPane.add(cbCouponType, "grow, wrap"); //$NON-NLS-1$
		contentPane.add(label6);
		contentPane.add(tfBarcode, "grow, wrap"); //$NON-NLS-1$
		contentPane.add(label5);
		contentPane.add(cbQualificationType, "grow, wrap"); //$NON-NLS-1$
		contentPane.add(lblMinimum);
		contentPane.add(tfMinimumQua, "grow, wrap"); //$NON-NLS-1$
		contentPane.add(label4);
		contentPane.add(tfCouponValue, "grow, wrap"); //$NON-NLS-1$
		contentPane.add(new JLabel("")); //$NON-NLS-1$
		contentPane.add(chkEnabled, "wrap"); //$NON-NLS-1$
		contentPane.add(new JLabel("")); //$NON-NLS-1$
		contentPane.add(chkAutoApply, "wrap"); //$NON-NLS-1$
		contentPane.add(new JLabel("")); //$NON-NLS-1$
		contentPane.add(chkNeverExpire, "wrap"); //$NON-NLS-1$
		contentPane.add(new JLabel("")); //$NON-NLS-1$
		contentPane.add(chkModifiable, "wrap"); //$NON-NLS-1$

		createItemSearchPanel();

		itemPanel = new JPanel(new BorderLayout(10, 10));
		itemPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));

		cbListItems = new ItemCheckBoxList();
		List<MenuItem> menuItems = MenuItemDAO.getInstance().findAll();
		cbListItems.setModel(menuItems);

		addedListItems = new ItemCheckBoxList();
		addedListItems.setModel(cbListItems.getCheckedValues());

		itemPanel.add(itemSearchPanel, BorderLayout.NORTH);
		itemScrollPane = new JScrollPane(addedListItems);

		itemPanel.add(itemScrollPane, BorderLayout.CENTER);

		add(contentPane, BorderLayout.WEST);
		add(itemPanel, BorderLayout.CENTER);

		setPreferredSize(new Dimension(700, 350));
	}

	private void createItemSearchPanel() {
		itemSearchPanel = new JPanel();
		itemSearchPanel.setLayout(new BorderLayout(5, 5));

		PosButton btnSearch = new PosButton(POSConstants.ADD); //$NON-NLS-1$
		btnSearch.setPreferredSize(new Dimension(60, 40));

		txtSearchItem = new JTextField();

		txtSearchItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (txtSearchItem.getText().equals("")) { //$NON-NLS-1$
					POSMessageDialog.showMessage(Messages.getString("CouponForm.8")); //$NON-NLS-1$
					return;
				}

				if (!addMenuItemByBarcode(txtSearchItem.getText())) {
					addMenuItemByItemId(txtSearchItem.getText());
				}
				txtSearchItem.setText(""); //$NON-NLS-1$
			}
		});

		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ItemSelectionDialog dialog = new ItemSelectionDialog();
				dialog.setModel(cbListItems.getModel());
				dialog.open();
				if (dialog.isCanceled()) {
					return;
				}
				cbListItems.setModel(dialog.getModel());
				addedListItems.setModel(cbListItems.getCheckedValues());
				addedListItems.selectItems(cbListItems.getCheckedValues());
				txtSearchItem.requestFocus();

			}
		});
		itemSearchPanel.add(txtSearchItem);
		itemSearchPanel.add(btnSearch, BorderLayout.EAST);
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if (event.getItem() == Discount.COUPON_QUALIFICATION_NAMES[0]) {
			List<MenuItem> menuItems = MenuItemDAO.getInstance().findAll();
			itemPanel.setVisible(true);
			cbListItems.setModel(menuItems);
			addedListItems.setModel(cbListItems.getCheckedValues());
		}
		else if (event.getItem() == Discount.COUPON_TYPE_NAMES[Discount.DISCOUNT_TYPE_AMOUNT]) {
			chkModifiable.setVisible(true);
		}
		else if (event.getItem() == Discount.COUPON_TYPE_NAMES[Discount.DISCOUNT_TYPE_PERCENTAGE]) {
			chkModifiable.setVisible(false);
		}
		/*else if (event.getItem() == Discount.COUPON_QUALIFICATION_NAMES[1]) {
			List<MenuGroup> menuGroups = MenuGroupDAO.getInstance().findAll();
			cbListItems.setModel(menuGroups);
		}
		else if (event.getItem() == Discount.COUPON_QUALIFICATION_NAMES[2]) {
			List<MenuCategory> menuCategories = MenuCategoryDAO.getInstance().findAll();
			cbListItems.setModel(menuCategories);
		}*/
		else {
			itemPanel.setVisible(false);
		}
	}

	private boolean addMenuItemByBarcode(String barcode) {

		MenuItemDAO dao = new MenuItemDAO();

		MenuItem menuItem = dao.getMenuItemByBarcode(barcode);

		if (menuItem == null) {
			return false;
		}

		//add to list
		return true;
	}

	private boolean addMenuItemByItemId(String id) {

		Integer itemId = Integer.parseInt(id);

		MenuItem menuItem = MenuItemDAO.getInstance().get(itemId);
		if (menuItem == null) {
			return false;
		}
		cbListItems.setSelected(menuItem);
		addedListItems.setModel(cbListItems.getCheckedValues());
		addedListItems.selectItems(cbListItems.getCheckedValues());
		return true;
	}

	@Override
	public boolean save() {
		try {

			if (!updateModel())
				return false;

			Discount coupon = (Discount) getBean();
			DiscountDAO.getInstance().saveOrUpdate(coupon);

		} catch (Exception e) {
			MessageDialog.showError(com.floreantpos.POSConstants.SAVE_ERROR, e);
			return false;
		}
		return true;
	}

	@Override
	protected void updateView() {
		Discount coupon = (Discount) getBean();
		if (coupon.getId() == null) {
			chkEnabled.setSelected(true); 
			tfMinimumQua.setText("0"); 
			cbCouponType.setSelectedIndex(Discount.DISCOUNT_TYPE_PERCENTAGE); 
			return;
		}

		uuid = coupon.getUUID();

		tfCouponName.setText(coupon.getName());
		tfMinimumQua.setText(coupon.getMinimunBuy().toString());
		tfCouponValue.setText(String.valueOf(coupon.getValue()));
		cbCouponType.setSelectedIndex(coupon.getType());
		cbQualificationType.setSelectedIndex(coupon.getQualificationType());
		dpExperation.setDate(coupon.getExpiryDate());
		tfBarcode.setText(coupon.getBarcode());
		chkEnabled.setSelected(coupon.isEnabled());
		chkModifiable.setSelected(coupon.isModifiable());
		chkAutoApply.setSelected(coupon.isAutoApply());
		chkNeverExpire.setSelected(coupon.isNeverExpire());

		if (coupon.getQualificationType() == Discount.QUALIFICATION_TYPE_ITEM) {
			cbListItems.selectItems(coupon.getMenuItems());
			addedListItems.setModel(cbListItems.getCheckedValues());
			addedListItems.selectItems(cbListItems.getCheckedValues());
		}
		/*else if (coupon.getQUALIFICATION_TYPE() == Discount.QUALIFICATION_TYPE_GROUP) {
			cbListItems.selectItems(coupon.getMenuGroups());
		}
		else if (coupon.getQUALIFICATION_TYPE() == Discount.QUALIFICATION_TYPE_CATEGORY) {
			cbListItems.selectItems(coupon.getMenuCategories());
		}*/

	}

	@Override
	protected boolean updateModel() {
		String name = tfCouponName.getText();
		String barcode = tfBarcode.getText();
		double couponValue = 0;
		couponValue = (Double) tfCouponValue.getDouble();
		int couponMinimumQua = Integer.parseInt(tfMinimumQua.getText());
		int couponType = cbCouponType.getSelectedIndex();
		Date expiryDate = dpExperation.getDate();
		boolean enabled = chkEnabled.isSelected();
		boolean modifiable = chkModifiable.isSelected();
		boolean autoApply = chkAutoApply.isSelected();
		boolean neverExpire = chkNeverExpire.isSelected();
		int qualificationType = cbQualificationType.getSelectedIndex();

		if (name == null || name.trim().equals("")) { //$NON-NLS-1$
			POSMessageDialog.showError(null, Messages.getString("CouponForm.1")); //$NON-NLS-1$
			return false;
		}
		if (couponValue <= 0) {
			POSMessageDialog.showError(null, Messages.getString("CouponForm.2")); //$NON-NLS-1$
			return false;
		}
		if (qualificationType == Discount.QUALIFICATION_TYPE_ITEM && couponValueOverflow()) {
			POSMessageDialog.showError(null, Messages.getString("CouponForm.10")); //$NON-NLS-1$
			return false;
		}

		Discount coupon = (Discount) getBean();
		coupon.setName(name);
		coupon.setMinimunBuy(couponMinimumQua);
		coupon.setValue(couponValue);
		coupon.setExpiryDate(expiryDate);
		coupon.setBarcode(barcode);
		coupon.setType(couponType);
		coupon.setQualificationType(qualificationType);
		coupon.setEnabled(enabled);
		coupon.setModifiable(modifiable);
		coupon.setAutoApply(autoApply);
		coupon.setNeverExpire(neverExpire);

		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
		}
		coupon.setUUID(uuid);

		if (qualificationType == Discount.QUALIFICATION_TYPE_ITEM) {
			if (addedListItems.getCheckedValues().size() > 0) {
				coupon.setMenuItems(addedListItems.getCheckedValues());
				coupon.setApplyToAll(false);
			}
			else {
				coupon.setApplyToAll(true);
			}
		}
		/*else if (qualificationType == Discount.QUALIFICATION_TYPE_GROUP) {
			coupon.setMenuGroups(cbListItems.getCheckedValues());
		}
		else if (qualificationType == Discount.QUALIFICATION_TYPE_CATEGORY) {
			coupon.setMenuCategories(cbListItems.getCheckedValues());
		}*/

		return true;
	}

	private boolean couponValueOverflow() {
		List<MenuItem> menuItems = addedListItems.getCheckedValues();
		double couponValue = Double.parseDouble(tfCouponValue.getText());
		if (cbCouponType.getSelectedIndex() == Discount.DISCOUNT_TYPE_PERCENTAGE) {
			couponValue = couponValue / 100;
		}
		if (Integer.parseInt(tfMinimumQua.getText()) > 0) {
			int minimumQua = Integer.parseInt(tfMinimumQua.getText());
			for (MenuItem menuItem : menuItems) {
				if (couponValue > (menuItem.getPrice() * minimumQua)) {
					return true;
				}
			}
		}
		else {
			for (MenuItem menuItem : menuItems) {
				if (couponValue > menuItem.getPrice()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getDisplayText() {
		Discount coupon = (Discount) getBean();
		if (coupon.getId() == null) {
			return Messages.getString("CouponForm.3"); //$NON-NLS-1$
		}
		return Messages.getString("CouponForm.4"); //$NON-NLS-1$
	}

}
