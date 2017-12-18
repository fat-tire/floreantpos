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
/*
 * ModifierEditor.java
 *
 * Created on August 4, 2006, 12:03 AM
 */

package com.floreantpos.ui.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.Messages;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.ModifierGroup;
import com.floreantpos.model.ModifierMultiplierPrice;
import com.floreantpos.model.Multiplier;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.model.dao.ModifierDAO;
import com.floreantpos.model.dao.ModifierGroupDAO;
import com.floreantpos.model.dao.MultiplierDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author  MShahriar
 */
public class MenuModifierForm extends BeanEditor {

	private MenuModifier modifier;
	private PriceByOrderType priceTableModel;

	private JCheckBox chkPrintToKitchen;
	private JComboBox cbModifierGroup;
	private JComboBox cbTaxes;

	private JFormattedTextField tfName;
	private FixedLengthTextField tfTranslatedName;
	private DoubleTextField tfNormalPrice;
	private DoubleTextField tfExtraPrice;
	private IntegerTextField tfSortOrder;

	private JButton btnButtonColor;
	private JButton btnTextColor;

	private JTable priceTable;
	private JTabbedPane jTabbedPane1;

	private Map<String, MultiplierPricePanel> itemMap = new HashMap<>();
	private JCheckBox chkUseFixedPrice;

	public MenuModifierForm() throws Exception {
		this(new MenuModifier());
	}

	public MenuModifierForm(MenuModifier modifier) throws Exception {
		this.modifier = modifier;

		checkRegularMultiplier();
		initComponents();

		ModifierGroupDAO modifierGroupDAO = new ModifierGroupDAO();
		List<ModifierGroup> groups = modifierGroupDAO.findAll();
		cbModifierGroup.setModel(new DefaultComboBoxModel(new Vector<ModifierGroup>(groups)));
		priceTable.setModel(priceTableModel = new PriceByOrderType(modifier.getProperties()));

		TaxDAO taxDAO = new TaxDAO();
		List<Tax> taxes = taxDAO.findAll();
		cbTaxes.setModel(new ComboBoxModel(taxes));

		add(jTabbedPane1);

		setBean(modifier);
	}

	private void initComponents() {
		setLayout(new BorderLayout(0, 0));

		jTabbedPane1 = new javax.swing.JTabbedPane();

		tfName = new javax.swing.JFormattedTextField();
		tfTranslatedName = new FixedLengthTextField();
		cbModifierGroup = new javax.swing.JComboBox();
		tfNormalPrice = new DoubleTextField();
		tfExtraPrice = new DoubleTextField();
		tfSortOrder = new IntegerTextField();
		cbTaxes = new javax.swing.JComboBox();
		JButton btnNewTax = new javax.swing.JButton();
		chkPrintToKitchen = new javax.swing.JCheckBox();
		chkUseFixedPrice = new JCheckBox("Use fixed price");

		JButton btnNewPrice = new JButton();
		JButton btnUpdatePrice = new JButton();
		JButton btnDeletePrice = new JButton();
		JButton btnDefaultValue = new JButton();
		JButton btnDeleteAll = new JButton();

		JPanel tabPrice = new javax.swing.JPanel();
		JScrollPane jScrollPane3 = new javax.swing.JScrollPane();
		priceTable = new javax.swing.JTable();

		JLabel lblName = new javax.swing.JLabel(com.floreantpos.POSConstants.NAME + ":");
		JLabel lblTranslatedName = new JLabel(Messages.getString("MenuModifierForm.0")); //$NON-NLS-1$
		JLabel lblModifierGroup = new javax.swing.JLabel(com.floreantpos.POSConstants.GROUP + ":");
		JLabel lblPrice = new javax.swing.JLabel("Price" + ":");
		JLabel lblExtraPrice = new javax.swing.JLabel(com.floreantpos.POSConstants.EXTRA_PRICE + ":");
		JLabel lblSortOrder = new JLabel(Messages.getString("MenuModifierForm.15")); //$NON-NLS-1$
		JLabel lblTaxRate = new javax.swing.JLabel(com.floreantpos.POSConstants.TAX_RATE + ":");
		JLabel lblPercentage = new javax.swing.JLabel();

		tfExtraPrice.setText("0"); //$NON-NLS-1$
		lblPercentage.setText("%"); //$NON-NLS-1$
		tfNormalPrice.setText("0"); //$NON-NLS-1$

		btnNewTax.setText("..."); //$NON-NLS-1$
		btnNewTax.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnNewTaxActionPerformed(evt);
			}
		});

		chkPrintToKitchen.setText(com.floreantpos.POSConstants.PRINT_TO_KITCHEN);
		chkPrintToKitchen.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkPrintToKitchen.setMargin(new java.awt.Insets(0, 0, 0, 0));

		JPanel generalTabPanel = new JPanel(new BorderLayout());
		jTabbedPane1.addTab(com.floreantpos.POSConstants.GENERAL, generalTabPanel);

		TransparentPanel lelfInputPanel = new TransparentPanel();
		lelfInputPanel.setLayout(new MigLayout("wrap 2,hidemode 3", "[90px][grow]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		lelfInputPanel.add(lblName, "alignx left,aligny center"); //$NON-NLS-1$
		lelfInputPanel.add(tfName, "growx,aligny top"); //$NON-NLS-1$

		lelfInputPanel.add(lblTranslatedName, "alignx left,aligny center"); //$NON-NLS-1$
		lelfInputPanel.add(tfTranslatedName, "growx"); //$NON-NLS-1$

		lelfInputPanel.add(lblModifierGroup, "alignx left,aligny center"); //$NON-NLS-1$
		lelfInputPanel.add(cbModifierGroup, "growx,aligny top"); //$NON-NLS-1$

		//	lelfInputPanel.add(lblPrice, "alignx left,aligny center"); //$NON-NLS-1$
		//	lelfInputPanel.add(tfNormalPrice, "growx,aligny top"); //$NON-NLS-1$

		//lelfInputPanel.add(lblExtraPrice, "alignx left,aligny center"); //$NON-NLS-1$
		//lelfInputPanel.add(tfExtraPrice, "growx,aligny top"); //$NON-NLS-1$

		JPanel rightInputPanel = new JPanel(new MigLayout("wrap 2", "[86px][grow]"));

		rightInputPanel.add(lblTaxRate, "alignx left,aligny center,split 2"); //$NON-NLS-1$
		rightInputPanel.add(lblPercentage, "alignx left,aligny center"); //$NON-NLS-1$
		rightInputPanel.add(cbTaxes, "growx,aligny top,split 2"); //$NON-NLS-1$
		rightInputPanel.add(btnNewTax, "alignx left,aligny top"); //$NON-NLS-1$

		rightInputPanel.add(lblSortOrder, "alignx left,aligny center"); //$NON-NLS-1$
		rightInputPanel.add(tfSortOrder, "growx,aligny top"); //$NON-NLS-1$

		rightInputPanel.add(chkPrintToKitchen, "skip 1,alignx left,aligny top"); //$NON-NLS-1$

		generalTabPanel.add(lelfInputPanel);
		generalTabPanel.add(rightInputPanel, BorderLayout.EAST);

		JLabel lblButtonColor = new JLabel(Messages.getString("MenuModifierForm.1")); //$NON-NLS-1$
		btnButtonColor = new JButton(""); //$NON-NLS-1$
		btnButtonColor.setPreferredSize(new Dimension(140, 40));

		JLabel lblTextColor = new JLabel(Messages.getString("MenuModifierForm.27")); //$NON-NLS-1$
		btnTextColor = new JButton(Messages.getString("MenuModifierForm.29")); //$NON-NLS-1$
		btnTextColor.setPreferredSize(new Dimension(140, 40));

		JPanel tabButtonStyle = new JPanel(new MigLayout("hidemode 3,wrap 2"));
		tabButtonStyle.add(lblButtonColor); //$NON-NLS-1$
		tabButtonStyle.add(btnButtonColor); //$NON-NLS-1$
		tabButtonStyle.add(lblTextColor); //$NON-NLS-1$
		tabButtonStyle.add(btnTextColor); //$NON-NLS-1$

		jTabbedPane1.addTab("Button Style", tabButtonStyle); //$NON-NLS-1$

		btnButtonColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MenuModifierForm.this, Messages.getString("MenuModifierForm.39"), btnButtonColor.getBackground()); //$NON-NLS-1$
				btnButtonColor.setBackground(color);
				btnTextColor.setBackground(color);
			}
		});

		btnTextColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MenuModifierForm.this, Messages.getString("MenuModifierForm.40"), btnTextColor.getForeground()); //$NON-NLS-1$
				btnTextColor.setForeground(color);
			}
		});

		btnNewPrice.setText(Messages.getString("MenuModifierForm.2")); //$NON-NLS-1$
		btnNewPrice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewPrice();
			}
		});
		btnUpdatePrice.setText(Messages.getString("MenuModifierForm.3")); //$NON-NLS-1$
		btnUpdatePrice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updatePrice();
			}
		});
		btnDeletePrice.setText(Messages.getString("MenuModifierForm.4")); //$NON-NLS-1$
		btnDeletePrice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deletePrice();
			}
		});
		btnDeleteAll.setText(Messages.getString("MenuModifierForm.5")); //$NON-NLS-1$
		btnDeleteAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteAll();
			}
		});

		btnDefaultValue.setText(Messages.getString("MenuModifierForm.8")); //$NON-NLS-1$
		btnDefaultValue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setDefaultValue();
			}
		});
		priceTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		jScrollPane3.setViewportView(priceTable);

		tabPrice.setLayout(new BorderLayout());
		tabPrice.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tabPrice.add(jScrollPane3, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		buttonPanel.add(btnNewPrice);
		buttonPanel.add(btnUpdatePrice);
		//buttonPanel.add(btnDefaultValue);
		buttonPanel.add(btnDeletePrice);
		//buttonPanel.add(btnDeleteAll);

		JPanel multiplierPanel = new JPanel(new MigLayout("fillx,wrap 1"));
		List<Multiplier> multipliers = MultiplierDAO.getInstance().findAll();

		if (multipliers != null) {
			for (Multiplier multiplier : multipliers) {
				MultiplierPricePanel multiplierPricePanel = new MultiplierPricePanel(multiplier);
				multiplierPanel.add(multiplierPricePanel, "grow");
				itemMap.put(multiplier.getName(), multiplierPricePanel);
			}
		}
		//lelfInputPanel.add(chkSelectAll, "newline,gapleft 10,skip 1,split 2,grow");
		//lelfInputPanel.add(chkUseFixedPrice, "newline,skip 1,grow");

		//lelfInputPanel.add(btnCalculateMultilierPrice, "gapright 10,w 80!");

		JScrollPane scrollPane = new JScrollPane(multiplierPanel);
		scrollPane.setBorder(new TitledBorder("Multiplier price"));
		lelfInputPanel.add(scrollPane, "newline,skip 1,grow");

		tabPrice.add(buttonPanel, BorderLayout.SOUTH);
		if (TerminalConfig.isMultipleOrderSupported()) {
			//jTabbedPane1.addTab(Messages.getString("MenuModifierForm.6"), tabPrice); //$NON-NLS-1$
		}
	}

	private void checkRegularMultiplier() {
		Multiplier multiplier = MultiplierDAO.getInstance().get(Multiplier.REGULAR);
		if (multiplier != null && multiplier.isMain()) {
			return;
		}
		if (multiplier == null) {
			multiplier = new Multiplier(Multiplier.REGULAR);
			multiplier.setRate(0.0);
			multiplier.setSortOrder(0);
			multiplier.setTicketPrefix("");
			multiplier.setDefaultMultiplier(true);
			multiplier.setMain(true);
			MultiplierDAO.getInstance().save(multiplier);
		}
		else {
			multiplier.setMain(true);
			MultiplierDAO.getInstance().update(multiplier);
		}
	}

	private void btnNewTaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewTaxActionPerformed
		try {
			TaxForm editor = new TaxForm();
			BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
			dialog.open();
			if (!dialog.isCanceled()) {
				Tax tax = (Tax) editor.getBean();
				ComboBoxModel model = (ComboBoxModel) cbTaxes.getModel();
				model.addElement(tax);
				model.setSelectedItem(tax);
			}
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

	@Override
	public boolean save() {
		try {
			if (!updateModel())
				return false;

			MenuModifier modifier = (MenuModifier) getBean();
			ModifierDAO dao = new ModifierDAO();
			dao.saveOrUpdate(modifier);
		} catch (Exception e) {
			MessageDialog.showError(com.floreantpos.POSConstants.SAVE_ERROR, e);
			return false;
		}
		return true;
	}

	@Override
	protected void updateView() {
		MenuModifier modifier = (MenuModifier) getBean();

		if (modifier == null) {
			tfName.setText(""); //$NON-NLS-1$
			tfNormalPrice.setText("0"); //$NON-NLS-1$
			tfExtraPrice.setText("0"); //$NON-NLS-1$
			return;
		}

		tfName.setText(modifier.getName());
		tfTranslatedName.setText(modifier.getTranslatedName());
		tfNormalPrice.setText(String.valueOf(modifier.getPrice()));
		tfExtraPrice.setText(String.valueOf(modifier.getExtraPrice()));
		cbModifierGroup.setSelectedItem(modifier.getModifierGroup());
		chkPrintToKitchen.setSelected(modifier.isShouldPrintToKitchen());
		chkUseFixedPrice.setSelected(modifier.isFixedPrice());

		if (modifier.getSortOrder() != null) {
			tfSortOrder.setText(modifier.getSortOrder().toString());
		}

		if (modifier.getButtonColor() != null) {
			Color color = new Color(modifier.getButtonColor());
			btnButtonColor.setBackground(color);
			btnTextColor.setBackground(color);
		}

		if (modifier.getTextColor() != null) {
			Color color = new Color(modifier.getTextColor());
			btnTextColor.setForeground(color);
		}

		if (modifier.getTax() != null) {
			cbTaxes.setSelectedItem(modifier.getTax());
		}
		List<ModifierMultiplierPrice> multiplierPriceList = modifier.getMultiplierPriceList();
		if (multiplierPriceList != null) {
			for (ModifierMultiplierPrice multiplierPrice : multiplierPriceList) {
				MultiplierPricePanel pricePanel = itemMap.get(multiplierPrice.getMultiplier().getName());
				if (pricePanel == null)
					continue;
				pricePanel.setModifierMultiplierPrice(multiplierPrice);
			}
		}
		itemMap.get(Multiplier.REGULAR).tfAditionalPrice.setText(String.valueOf(modifier.getPrice()));
	}

	@Override
	protected boolean updateModel() {
		MenuModifier modifier = (MenuModifier) getBean();

		String name = tfName.getText();
		if (POSUtil.isBlankOrNull(name)) {
			MessageDialog.showError(Messages.getString("MenuModifierForm.44")); //$NON-NLS-1$
			return false;
		}

		modifier.setName(name);
		modifier.setExtraPrice(tfExtraPrice.getDouble());
		modifier.setTax((Tax) cbTaxes.getSelectedItem());
		modifier.setModifierGroup((ModifierGroup) cbModifierGroup.getSelectedItem());
		modifier.setShouldPrintToKitchen(Boolean.valueOf(chkPrintToKitchen.isSelected()));

		modifier.setTranslatedName(tfTranslatedName.getText());
		modifier.setButtonColor(btnButtonColor.getBackground().getRGB());
		modifier.setTextColor(btnTextColor.getForeground().getRGB());
		modifier.setSortOrder(tfSortOrder.getInteger());
		modifier.setFixedPrice(chkUseFixedPrice.isSelected());

		List<ModifierMultiplierPrice> mulplierPriceList = new ArrayList<>();
		for (MultiplierPricePanel panel : itemMap.values()) {
			if (panel.isSelected()) {
				ModifierMultiplierPrice multiplierPrice = panel.getMultiplierPrice();
				if (multiplierPrice == null) {
					multiplierPrice = new ModifierMultiplierPrice();
					multiplierPrice.setMultiplier(panel.getMultiplier());
					multiplierPrice.setModifier(modifier);
				}
				multiplierPrice.setPrice(panel.getPrice());
				mulplierPriceList.add(multiplierPrice);
			}
		}
		modifier.setPrice(itemMap.get(Multiplier.REGULAR).getPrice());
		modifier.setMultiplierPriceList(mulplierPriceList);
		return true;
	}

	class PriceByOrderType extends AbstractTableModel {
		List<String> propertiesKey = new ArrayList<String>();

		String[] cn = { "MODIFIER", "ORDER TYPE", "PRICE", "TAX", "EXTRA PRICE", "EXTRA TAX" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		PriceByOrderType(Map<String, String> properties) {

			if (properties != null) {
				List<String> keys = new ArrayList(properties.keySet());
				setPropertiesToTable(keys);
			}
		}

		private void setPropertiesToTable(List<String> keys) {
			propertiesKey.clear();

			for (int i = 0; i < keys.size(); i++) {
				if (keys.get(i).contains("_PRICE") && !keys.get(i).contains("EXTRA_PRICE")) { //$NON-NLS-1$
					this.propertiesKey.add(keys.get(i));
				}
			}
		}

		public String get(int index) {
			return propertiesKey.get(index);
		}

		public void add(MenuModifier modifier) {
			setPropertiesToTable(new ArrayList(modifier.getProperties().keySet()));
			fireTableDataChanged();
		}

		public void setDefaultValue() {
			int selectedRow = priceTable.getSelectedRow();
			if (selectedRow == -1) {
				POSMessageDialog.showMessage(Messages.getString("MenuModifierForm.9")); //$NON-NLS-1$
				return;
			}
			String modifiedKey = priceTableModel.propertiesKey.get(selectedRow);
			modifiedKey = modifiedKey.replaceAll("_PRICE", ""); //$NON-NLS-1$ //$NON-NLS-2$
			modifiedKey = modifiedKey.replaceAll("_", " ");//$NON-NLS-1$ //$NON-NLS-2$
			modifier.setPriceByOrderType(modifiedKey, modifier.getPrice());
			if (modifier.getTax() != null) {
				modifier.setTaxByOrderType(modifiedKey, modifier.getTax().getRate());
			}
			else {
				modifier.setTaxByOrderType(modifiedKey, 0.0);
			}
			MenuModifierDAO.getInstance().saveOrUpdate(modifier);
			add(modifier);
			fireTableDataChanged();
		}

		public void remove(int index) {
			if (propertiesKey == null) {
				return;
			}
			String typeProperty = propertiesKey.get(index);
			String taxProperty = typeProperty.replaceAll("_PRICE", "_TAX"); //$NON-NLS-1$ //$NON-NLS-2$

			modifier.removeProperty(typeProperty, taxProperty);
			MenuModifierDAO.getInstance().saveOrUpdate(modifier);

			propertiesKey.remove(index);
			fireTableDataChanged();
		}

		public void removeAll() {
			modifier.getProperties().clear();
			MenuModifierDAO.getInstance().saveOrUpdate(modifier);
			propertiesKey.clear();
			fireTableDataChanged();
		}

		public int getRowCount() {
			if (propertiesKey == null)
				return 0;

			return propertiesKey.size();
		}

		public int getColumnCount() {
			return cn.length;
		}

		@Override
		public String getColumnName(int column) {
			return cn[column];
		}

		public List<String> getProperties() {
			return propertiesKey;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			String key = String.valueOf(propertiesKey.get(rowIndex));
			switch (columnIndex) {
				case 0:
					return modifier.getName();
				case 1:
					key = key.replaceAll("_PRICE", ""); //$NON-NLS-1$ //$NON-NLS-2$
					key = key.replaceAll("_", " "); //$NON-NLS-1$ //$NON-NLS-2$
					return key;
				case 2:
					return modifier.getProperty(key);
				case 3:
					key = key.replaceAll("_PRICE", "_TAX"); //$NON-NLS-1$ //$NON-NLS-2$
					return modifier.getProperty(key);
				case 4:
					key = key.replaceAll("_PRICE", "_EXTRA_PRICE"); //$NON-NLS-1$ //$NON-NLS-2$
					return modifier.getProperty(key);
				case 5:
					key = key.replaceAll("_PRICE", "_EXTRA_TAX"); //$NON-NLS-1$ //$NON-NLS-2$
					return modifier.getProperty(key);
			}
			return null;
		}
	}

	public String getDisplayText() {
		MenuModifier modifier = (MenuModifier) getBean();
		if (modifier.getId() == null) {
			return Messages.getString("MenuModifierForm.45"); //$NON-NLS-1$
		}
		return Messages.getString("MenuModifierForm.46"); //$NON-NLS-1$
	}

	private void addNewPrice() {

		ModifierPriceByOrderTypeDialog dialog = new ModifierPriceByOrderTypeDialog(this.getParentFrame(), modifier);
		dialog.setSize(350, 220);
		dialog.open();
		if (!dialog.isCanceled()) {
			priceTableModel.add(dialog.getMenuModifier());
		}
	}

	private void deletePrice() {
		int selectedRow = priceTable.getSelectedRow();
		if (selectedRow == -1) {
			POSMessageDialog.showMessage(this.getParentFrame(), Messages.getString("MenuModifierForm.7")); //$NON-NLS-1$
			return;
		}
		int option = POSMessageDialog.showYesNoQuestionDialog(this.getParentFrame(), Messages.getString("MenuModifierForm.21"), Messages.getString("MenuModifierForm.22")); //$NON-NLS-1$ //$NON-NLS-2$
		if (option != JOptionPane.YES_OPTION) {
			return;
		}

		priceTableModel.remove(selectedRow);
	}

	private void deleteAll() {

		int option = POSMessageDialog.showYesNoQuestionDialog(this.getParentFrame(), Messages.getString("MenuModifierForm.23"), Messages.getString("MenuModifierForm.24")); //$NON-NLS-1$ //$NON-NLS-2$
		if (option != JOptionPane.YES_OPTION) {
			return;
		}
		priceTableModel.removeAll();
	}

	private void setDefaultValue() {
		priceTableModel.setDefaultValue();
	}

	private void updatePrice() {
		int selectedRow = priceTable.getSelectedRow();
		if (selectedRow == -1) {
			POSMessageDialog.showMessage(this.getParentFrame(), Messages.getString("MenuModifierForm.25")); //$NON-NLS-1$
			return;
		}

		priceTableModel.propertiesKey.get(selectedRow);
		ModifierPriceByOrderTypeDialog dialog = new ModifierPriceByOrderTypeDialog(this.getParentFrame(), modifier, String.valueOf(priceTableModel.propertiesKey.get(selectedRow)));
		dialog.setSize(350, 220);
		dialog.open();
		if (!dialog.isCanceled()) {
			priceTableModel.add(dialog.getMenuModifier());
		}
	}

	protected void doCalculateMultiplierPrice() {
		MultiplierPricePanel regularPricePanel = itemMap.get(Multiplier.REGULAR);
		if (regularPricePanel == null)
			return;
		for (MultiplierPricePanel panel : itemMap.values()) {
			panel.calculatePrice(regularPricePanel.tfAditionalPrice.getDoubleOrZero());
		}
	}

	private class MultiplierPricePanel extends JPanel {
		ModifierMultiplierPrice multiplierPrice;
		Multiplier multiplier;
		DoubleTextField tfAditionalPrice;

		public MultiplierPricePanel(Multiplier multiplier) {
			this.multiplier = multiplier;
			setLayout(new MigLayout("inset 0,fillx", "[100px][grow][100px]", ""));
			//chkEnable = new JCheckBox(multiplier.getName());
			tfAditionalPrice = new DoubleTextField(multiplier.isMain() ? 6 : 9);
			tfAditionalPrice.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel lblName = new JLabel(multiplier.getName());
			if (multiplier.isMain()) {
				lblName.setFont(new Font(null, Font.BOLD, tfName.getFont().getSize()));
			}
			add(lblName);
			add(new JLabel(multiplier.isMain() ? "Price" : "Additional price", JLabel.TRAILING), "grow, gapright 10px");
			add(tfAditionalPrice, "split 2,grow");
			if (multiplier.isMain()) {
				JButton btnCalculateMultilierPrice = new JButton("Calc");
				btnCalculateMultilierPrice.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						doCalculateMultiplierPrice();
					}
				});
				add(btnCalculateMultilierPrice);
			}
		}

		public void calculatePrice(double regPrice) {
			if (multiplier.isMain())
				return;
			tfAditionalPrice.setText(String.valueOf(regPrice * multiplier.getRate() / 100));
		}

		public Double getPrice() {
			return tfAditionalPrice.getDoubleOrZero();
		}

		public Multiplier getMultiplier() {
			return multiplier;
		}

		public boolean isSelected() {
			Double value = tfAditionalPrice.getDouble();
			return !value.isNaN();
		}

		private void update() {
			if (multiplierPrice == null)
				return;
			tfAditionalPrice.setText(String.valueOf(multiplierPrice.getPrice()));
		}

		public void setModifierMultiplierPrice(ModifierMultiplierPrice price) {
			this.multiplierPrice = price;
			update();
		}

		public ModifierMultiplierPrice getMultiplierPrice() {
			return multiplierPrice;
		}

	}
}
