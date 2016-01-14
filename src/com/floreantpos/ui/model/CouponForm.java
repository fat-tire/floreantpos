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
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.Messages;
import com.floreantpos.model.Discount;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.DiscountDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.ItemCheckBoxList;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.ItemNumberSelectionDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class CouponForm extends BeanEditor implements ItemListener {
	private JPanel contentPane;
	private JPanel itemPanel;
	private JTextField tfCouponName;
	private JTextField tfBarcode;
	private JComboBox cbQualificationType;
	private JComboBox cbCouponType;
	private JFormattedTextField tfCouponValue;
	private JCheckBox chkEnabled;
	private JCheckBox chkAutoApply;
	private JCheckBox chkNeverExpire;
	private JXDatePicker dpExperation;

	private JPanel itemSearchPanel;
	private JTextField txtSearchItem;

	private JScrollPane itemScrollPane;

	private ItemCheckBoxList cbListItems;

	public CouponForm() {
		this(new Discount());
	}

	public CouponForm(Discount coupon) {
		initializeComponent();

		tfCouponName.setDocument(new FixedLengthDocument(30));
		cbCouponType.setModel(new DefaultComboBoxModel(Discount.COUPON_TYPE_NAMES));

		cbQualificationType.setModel(new DefaultComboBoxModel(Discount.COUPON_QUALIFICATION_NAMES));
		cbQualificationType.addItemListener(this);

		setBean(coupon);
	}

	private void initializeComponent() {
		setLayout(new BorderLayout(10, 10));
		setPreferredSize(new Dimension(650, 400));

		contentPane = new JPanel();
		contentPane.setLayout(new MigLayout());
		contentPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
		contentPane.setPreferredSize(new Dimension(400, 0));

		final JLabel label1 = new JLabel(Messages.getString("CouponForm.0") + ":");
		final JLabel label2 = new JLabel(Messages.getString("CouponForm.9") + ":");
		final JLabel label3 = new JLabel(Messages.getString("CouponForm.11") + ":");
		final JLabel label4 = new JLabel(Messages.getString("CouponForm.13") + ":");
		final JLabel label6 = new JLabel("Coupon Barcode");
		final JLabel label5 = new JLabel("Discount on :");

		tfCouponName = new JTextField(20);
		tfBarcode = new JTextField();
		cbCouponType = new JComboBox();
		cbQualificationType = new JComboBox();
		dpExperation = new JXDatePicker();
		tfCouponValue = new JFormattedTextField();
		chkEnabled = new JCheckBox("Enabled");
		chkAutoApply = new JCheckBox("Auto Apply");
		chkNeverExpire = new JCheckBox(Messages.getString("CouponForm.16"));

		contentPane.add(label1);
		contentPane.add(tfCouponName, "grow, wrap");
		contentPane.add(label2);
		contentPane.add(dpExperation, "grow, wrap");
		contentPane.add(label3);
		contentPane.add(cbCouponType, "grow, wrap");
		contentPane.add(label6);
		contentPane.add(tfBarcode, "grow, wrap");
		contentPane.add(label5);
		contentPane.add(cbQualificationType, "grow, wrap");
		contentPane.add(label4);
		contentPane.add(tfCouponValue, "grow, wrap");
		contentPane.add(new JLabel(""));
		contentPane.add(chkEnabled, "wrap");
		contentPane.add(new JLabel(""));
		contentPane.add(chkAutoApply, "wrap");
		contentPane.add(new JLabel(""));
		contentPane.add(chkNeverExpire, "wrap");

		createItemSearchPanel();

		itemPanel = new JPanel(new BorderLayout(10, 10));
		itemPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));

		cbListItems = new ItemCheckBoxList();
		List<MenuItem> menuItems = MenuItemDAO.getInstance().findAll();
		cbListItems.setModel(menuItems);

		itemPanel.add(itemSearchPanel, BorderLayout.NORTH);
		itemScrollPane = new JScrollPane(cbListItems);
		itemPanel.add(itemScrollPane, BorderLayout.CENTER);

		add(contentPane, BorderLayout.WEST);
		add(itemPanel, BorderLayout.CENTER);

	}

	private void createItemSearchPanel() {
		itemSearchPanel = new JPanel();
		itemSearchPanel.setLayout(new BorderLayout(5, 5));

		PosButton btnSearch = new PosButton("...");
		btnSearch.setPreferredSize(new Dimension(60, 40));

		txtSearchItem = new JTextField();

		txtSearchItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (txtSearchItem.getText().equals("")) {
					POSMessageDialog.showMessage("Please enter item number or barcode ");
					return;
				}

				if (!addMenuItemByBarcode(txtSearchItem.getText())) {
					addMenuItemByItemId(txtSearchItem.getText());
				}
				txtSearchItem.setText("");
			}
		});

		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ItemNumberSelectionDialog dialog = new ItemNumberSelectionDialog(null);
				dialog.setTitle("Search item");
				dialog.setSize(600, 400);
				dialog.open();
				if (dialog.isCanceled()) {
					return;
				}

				txtSearchItem.requestFocus();

				if (!addMenuItemByBarcode(dialog.getValue())) {
					if (!addMenuItemByItemId(dialog.getValue())) {
						POSMessageDialog.showError(null, "Item not found");
					}
				}
			}
		});
		itemSearchPanel.add(txtSearchItem);
		itemSearchPanel.add(btnSearch, BorderLayout.EAST);
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if (event.getItem() == Discount.COUPON_QUALIFICATION_NAMES[0]) {
			List<MenuItem> menuItems = MenuItemDAO.getInstance().findAll();
			cbListItems.setModel(menuItems);
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
			cbListItems.setModel(new ArrayList());
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
		if (coupon == null)
			return;

		tfCouponName.setText(coupon.getName());
		tfCouponValue.setValue(Double.valueOf(coupon.getValue()));
		cbCouponType.setSelectedIndex(coupon.getType());
		cbQualificationType.setSelectedIndex(coupon.getQUALIFICATION_TYPE());
		dpExperation.setDate(coupon.getExpiryDate());
		tfBarcode.setText(coupon.getBarcode());
		chkEnabled.setSelected(coupon.isEnabled());
		chkAutoApply.setSelected(coupon.isAutoApply());
		chkNeverExpire.setSelected(coupon.isNeverExpire());

		if (coupon.getQUALIFICATION_TYPE() == Discount.QUALIFICATION_TYPE_ITEM) {
			cbListItems.selectItems(coupon.getMenuItems());
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
		couponValue = ((Double) tfCouponValue.getValue()).doubleValue();
		int couponType = cbCouponType.getSelectedIndex();
		Date expiryDate = dpExperation.getDate();
		boolean enabled = chkEnabled.isSelected();
		boolean autoApply = chkAutoApply.isSelected();
		boolean neverExpire = chkNeverExpire.isSelected();
		int qualificationType = cbQualificationType.getSelectedIndex();

		if (name == null || name.trim().equals("")) { //$NON-NLS-1$
			MessageDialog.showError(Messages.getString("CouponForm.1")); //$NON-NLS-1$
			return false;
		}
		if (couponType != Discount.FREE_AMOUNT && couponValue <= 0) {
			MessageDialog.showError(Messages.getString("CouponForm.2")); //$NON-NLS-1$
			return false;
		}

		Discount coupon = (Discount) getBean();
		coupon.setName(name);
		coupon.setValue(couponValue);
		coupon.setExpiryDate(expiryDate);
		coupon.setBarcode(barcode);
		coupon.setType(couponType);
		coupon.setQUALIFICATION_TYPE(qualificationType);
		coupon.setEnabled(enabled);
		coupon.setAutoApply(autoApply);
		coupon.setNeverExpire(neverExpire);

		if (qualificationType == Discount.QUALIFICATION_TYPE_ITEM) {
			coupon.setMenuItems(cbListItems.getCheckedValues());
		}
		/*else if (qualificationType == Discount.QUALIFICATION_TYPE_GROUP) {
			coupon.setMenuGroups(cbListItems.getCheckedValues());
		}
		else if (qualificationType == Discount.QUALIFICATION_TYPE_CATEGORY) {
			coupon.setMenuCategories(cbListItems.getCheckedValues());
		}*/

		return true;
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
