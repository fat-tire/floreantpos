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
 * FoodItemEditor.java
 *
 * Created on August 2, 2006, 10:34 PM
 */

package com.floreantpos.ui.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.FileUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import com.floreantpos.Messages;
import com.floreantpos.PosLog;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.InventoryPlugin;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuItemShift;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PrinterGroup;
import com.floreantpos.model.TaxGroup;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.PrinterGroupDAO;
import com.floreantpos.model.dao.TaxGroupDAO;
import com.floreantpos.swing.CheckBoxList;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.DoubleDocument;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.IUpdatebleView;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.ShiftUtil;

/**
 *
 * @author  MShahriar
 */
public class MenuItemForm extends BeanEditor<MenuItem> implements ActionListener, ChangeListener {
	ShiftTableModel shiftTableModel;
	PriceByOrderTypeTableModel priceTableModel;
	private MenuItem menuItem;

	private javax.swing.JButton btnAddShift;
	private javax.swing.JButton btnNewPrice;
	private javax.swing.JButton btnUpdatePrice;
	private javax.swing.JButton btnDeletePrice;
	private javax.swing.JButton btnDeleteAll;
	private javax.swing.JButton btnDefaultValue;
	private javax.swing.JButton btnDeleteModifierGroup;
	private javax.swing.JButton btnDeleteShift;
	private javax.swing.JButton btnEditModifierGroup;
	private javax.swing.JButton btnNewGroup;
	private javax.swing.JButton btnNewModifierGroup;
	private javax.swing.JComboBox cbGroup;
	private javax.swing.JComboBox cbTaxGroup;
	private javax.swing.JCheckBox chkVisible;
	private javax.swing.JLabel lfname;
	private javax.swing.JLabel lDiscountRate;
	private javax.swing.JLabel lblPrice;
	private javax.swing.JLabel lgroup;
	private javax.swing.JLabel lPercentage;
	private javax.swing.JLabel lTax;
	private javax.swing.JLabel lblButtonColor;
	private javax.swing.JPanel tabGeneral;
	private javax.swing.JPanel tabModifier;
	private javax.swing.JPanel tabShift;
	private javax.swing.JPanel tabPrice;
	private javax.swing.JPanel tabButtonStyle;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JTabbedPane tabbedPane;
	private javax.swing.JTable shiftTable;
	private javax.swing.JTable priceTable;
	private javax.swing.JTable tableTicketItemModifierGroups;
	private DoubleTextField tfDiscountRate;
	private com.floreantpos.swing.FixedLengthTextField tfName;
	private DoubleTextField tfPrice;
	private JTextArea tfDescription;
	// End of variables declaration//GEN-END:variables
	private List<MenuItemModifierGroup> menuItemModifierGroups;
	private MenuItemMGListModel menuItemMGListModel;
	private JLabel lblImagePreview;
	private JButton btnClearImage;
	private JCheckBox cbShowTextWithImage;
	private DoubleTextField tfBuyPrice;
	private JLabel lblKitchenPrinter;
	private JComboBox<PrinterGroup> cbPrinterGroup;
	private JLabel lblBarcode;
	private FixedLengthTextField tfBarcode;
	private JLabel lblTextColor;
	private JLabel lblUnitName;
	private JButton btnButtonColor;
	private JButton btnTextColor;
	private JLabel lblTranslatedName;
	private FixedLengthTextField tfTranslatedName;
	private FixedLengthTextField tfUnitName;
	private IntegerTextField tfSortOrder;
	//private CheckBoxList terminalList;
	private CheckBoxList orderList;
	private JCheckBox cbFractionalUnit;
	private DoubleTextField tfStockCount;
	private JLabel lblStockCount;
	private JCheckBox cbDisableStockCount;
	private JLabel lblSortOrder;

	/** Creates new form FoodItemEditor */
	public MenuItemForm() throws Exception {
		this(new MenuItem());
	}

	public MenuItemForm(MenuItem menuItem) throws Exception {
		this.menuItem = menuItem;
		initComponents();

		MenuGroupDAO foodGroupDAO = new MenuGroupDAO();
		List<MenuGroup> foodGroups = foodGroupDAO.findAll();
		cbGroup.setModel(new ComboBoxModel(foodGroups));

		TaxGroupDAO taxDAO = new TaxGroupDAO();
		List<TaxGroup> taxeGroups = taxDAO.findAll();
		cbTaxGroup.setModel(new ComboBoxModel(taxeGroups));

		menuItemModifierGroups = menuItem.getMenuItemModiferGroups();
		shiftTable.setModel(shiftTableModel = new ShiftTableModel(menuItem.getShifts()));
		priceTable.setModel(priceTableModel = new PriceByOrderTypeTableModel(menuItem.getProperties()));

		setBean(menuItem);
	}

	protected void doSelectImageFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int option = fileChooser.showOpenDialog(POSUtil.getBackOfficeWindow());

		if (option == JFileChooser.APPROVE_OPTION) {
			File imageFile = fileChooser.getSelectedFile();
			try {
				byte[] itemImage = FileUtils.readFileToByteArray(imageFile);
				int imageSize = itemImage.length / 1024;

				if (imageSize > 20) {
					POSMessageDialog.showMessage(Messages.getString("MenuItemForm.0")); //$NON-NLS-1$
					itemImage = null;
					return;
				}

				ImageIcon imageIcon = new ImageIcon(new ImageIcon(itemImage).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
				lblImagePreview.setIcon(imageIcon);

				MenuItem menuItem = (MenuItem) getBean();
				menuItem.setImageData(itemImage);

			} catch (IOException e) {
				PosLog.error(getClass(), e);
			}
		}
	}

	protected void doClearImage() {
		MenuItem menuItem = (MenuItem) getBean();
		menuItem.setImageData(null);
		lblImagePreview.setIcon(null);
	}

	public void addRecepieExtension() {
		InventoryPlugin plugin = (InventoryPlugin) ExtensionManager.getPlugin(InventoryPlugin.class);
		if (plugin == null) {
			return;
		}

		plugin.addRecepieView(tabbedPane);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		lblStockCount = new JLabel(Messages.getString("MenuItemForm.17")); //$NON-NLS-1$
		tfStockCount = new DoubleTextField(1);
		cbDisableStockCount = new JCheckBox(Messages.getString("MenuItemForm.18")); //$NON-NLS-1$
		lblButtonColor = new JLabel(Messages.getString("MenuItemForm.19")); //$NON-NLS-1$
		tabbedPane = new javax.swing.JTabbedPane();
		tabGeneral = new javax.swing.JPanel();
		lfname = new javax.swing.JLabel();
		lfname.setHorizontalAlignment(SwingConstants.TRAILING);
		tfName = new com.floreantpos.swing.FixedLengthTextField(20);
		lgroup = new javax.swing.JLabel();
		lgroup.setHorizontalAlignment(SwingConstants.TRAILING);
		cbGroup = new javax.swing.JComboBox();
		cbGroup.setPreferredSize(new Dimension(198, 0));
		btnNewGroup = new javax.swing.JButton();
		lblPrice = new javax.swing.JLabel();
		lblPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		tfPrice = new DoubleTextField(20);
		tfPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		tfDescription = new JTextArea(new FixedLengthDocument(255));

		//tfDescription.setDocument(;
		tfUnitName = new FixedLengthTextField(20);
		lTax = new javax.swing.JLabel();
		lTax.setHorizontalAlignment(SwingConstants.TRAILING);
		cbTaxGroup = new javax.swing.JComboBox();
		lDiscountRate = new javax.swing.JLabel();
		lDiscountRate.setHorizontalAlignment(SwingConstants.TRAILING);
		lPercentage = new javax.swing.JLabel();
		tfDiscountRate = new DoubleTextField(18);
		tfDiscountRate.setHorizontalAlignment(SwingConstants.TRAILING);
		chkVisible = new javax.swing.JCheckBox();
		tabModifier = new javax.swing.JPanel();
		btnNewModifierGroup = new javax.swing.JButton();
		btnDeleteModifierGroup = new javax.swing.JButton();
		btnEditModifierGroup = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		tableTicketItemModifierGroups = new javax.swing.JTable();
		tabShift = new javax.swing.JPanel();
		tabPrice = new javax.swing.JPanel();
		tabButtonStyle = new javax.swing.JPanel();
		btnDeleteShift = new javax.swing.JButton();
		btnAddShift = new javax.swing.JButton();
		btnNewPrice = new javax.swing.JButton();
		btnUpdatePrice = new javax.swing.JButton();
		btnDeletePrice = new javax.swing.JButton();
		btnDeleteAll = new javax.swing.JButton();
		btnDefaultValue = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		jScrollPane3 = new javax.swing.JScrollPane();
		shiftTable = new javax.swing.JTable();
		priceTable = new javax.swing.JTable();
		cbPrinterGroup = new JComboBox<PrinterGroup>(new DefaultComboBoxModel<PrinterGroup>(PrinterGroupDAO.getInstance().findAll()
				.toArray(new PrinterGroup[0])));
		cbPrinterGroup.setPreferredSize(new Dimension(226, 0));

		tfTranslatedName = new FixedLengthTextField(20);
		tfTranslatedName.setLength(120);

		lblUnitName = new JLabel(Messages.getString("MenuItemForm.23")); //$NON-NLS-1$
		lblKitchenPrinter = new JLabel(Messages.getString("MenuItemForm.27")); //$NON-NLS-1$
		lgroup.setText(Messages.getString("LABEL_GROUP")); //$NON-NLS-1$
		lfname.setText(Messages.getString("LABEL_NAME")); //$NON-NLS-1$
		tfName.setLength(120);
		lblTranslatedName = new JLabel(Messages.getString("MenuItemForm.lblTranslatedName.text")); //$NON-NLS-1$
		tfBarcode = new FixedLengthTextField(20);
		tfSortOrder = new IntegerTextField(20);
		lblSortOrder = new JLabel(Messages.getString("MenuItemForm.lblSortOrder.text")); //$NON-NLS-1$
		tfSortOrder.setText(""); //$NON-NLS-1$
		lblBarcode = new JLabel(Messages.getString("MenuItemForm.lblBarcode.text")); //$NON-NLS-1$
		cbTaxGroup.setPreferredSize(new Dimension(198, 0));
		///lblButtonColor = new JLabel(Messages.getString("MenuItemForm.lblButtonColor.text")); //$NON-NLS-1$
		btnButtonColor = new JButton(); //$NON-NLS-1$
		btnButtonColor.setPreferredSize(new Dimension(228, 40));
		lblTextColor = new JLabel(Messages.getString("MenuItemForm.lblTextColor.text")); //$NON-NLS-1$
		btnTextColor = new JButton(Messages.getString("MenuItemForm.SAMPLE_TEXT")); //$NON-NLS-1$
		//	btnTextColor.setPreferredSize(new Dimension(228, 40));
		cbShowTextWithImage = new JCheckBox(Messages.getString("MenuItemForm.40")); //$NON-NLS-1$
		cbShowTextWithImage.setActionCommand(Messages.getString("MenuItemForm.41")); //$NON-NLS-1$
		lTax.setText(Messages.getString("LABEL_TAX")); //$NON-NLS-1$
		cbFractionalUnit = new JCheckBox(Messages.getString("MenuItemForm.24")); //$NON-NLS-1$

		btnNewGroup.setText("..."); //$NON-NLS-1$
		btnNewGroup.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doCreateNewGroup(evt);
			}
		});

		if (Application.getInstance().isPriceIncludesTax()) {
			lblPrice.setText(Messages.getString("LABEL_SALES_PRICE_INCLUDING_TAX")); //$NON-NLS-1$
		}
		else {
			lblPrice.setText(Messages.getString("LABEL_SALES_PRICE_EXCLUDING_TAX")); //$NON-NLS-1$
		}

		tfPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		lTax.setText(Messages.getString("LABEL_TAX")); //$NON-NLS-1$

		lDiscountRate.setText(com.floreantpos.POSConstants.DISCOUNT_RATE + ":"); //$NON-NLS-1$

		lPercentage.setText("%"); //$NON-NLS-1$

		chkVisible.setText(com.floreantpos.POSConstants.VISIBLE);
		chkVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkVisible.setMargin(new java.awt.Insets(0, 0, 0, 0));
		tabbedPane.addTab(com.floreantpos.POSConstants.GENERAL, tabGeneral);
		tabbedPane.setPreferredSize(new Dimension(750, 470));

		btnNewModifierGroup.setText(com.floreantpos.POSConstants.ADD);
		btnNewModifierGroup.setActionCommand("AddModifierGroup"); //$NON-NLS-1$
		btnNewModifierGroup.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnNewModifierGroupActionPerformed(evt);
			}
		});

		btnDeleteModifierGroup.setText(com.floreantpos.POSConstants.DELETE);
		btnDeleteModifierGroup.setActionCommand("DeleteModifierGroup"); //$NON-NLS-1$

		btnEditModifierGroup.setText(com.floreantpos.POSConstants.EDIT);
		btnEditModifierGroup.setActionCommand("EditModifierGroup"); //$NON-NLS-1$

		menuItemMGListModel = new MenuItemMGListModel();
		tableTicketItemModifierGroups.setModel(menuItemMGListModel);

		btnNewModifierGroup.addActionListener(this);
		btnEditModifierGroup.addActionListener(this);
		btnDeleteModifierGroup.addActionListener(this);
		btnAddShift.addActionListener(this);
		btnDeleteShift.addActionListener(this);

		tfDiscountRate.setDocument(new DoubleDocument());

		tabGeneral.setLayout(new MigLayout("insets 20", "[][]20px[][]", "[][][][][][][][][][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		/*JLabel lblImage = new JLabel(Messages.getString("MenuItemForm.28")); //$NON-NLS-1$
		lblImage.setHorizontalAlignment(SwingConstants.TRAILING);
		tabGeneral.add(lblImage, "cell 0 0,right"); //$NON-NLS-1$

		lblImagePreview = new JLabel(""); //$NON-NLS-1$
		lblImagePreview.setHorizontalAlignment(JLabel.CENTER);
		lblImagePreview.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		lblImagePreview.setPreferredSize(new Dimension(100, 100));
		tabGeneral.add(lblImagePreview, "cell 1 0"); //$NON-NLS-1$

		JButton btnSelectImage = new JButton("..."); //$NON-NLS-1$
		btnSelectImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSelectImageFile();
			}
		});
		tabGeneral.add(btnSelectImage, "cell 1 0"); //$NON-NLS-1$

		btnClearImage = new JButton(Messages.getString("MenuItemForm.34")); //$NON-NLS-1$
		btnClearImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doClearImage();
			}
		});
		tabGeneral.add(btnClearImage, "cell 1 0"); //$NON-NLS-1$
		*/

		tabGeneral.add(lfname, "cell 0 1 ,right"); //$NON-NLS-1$
		tabGeneral.add(tfName, "cell 1 1,grow"); //$NON-NLS-1$

		tabGeneral.add(lblTranslatedName, "cell 0 2,right"); //$NON-NLS-1$
		tabGeneral.add(tfTranslatedName, "cell 1 2,grow"); //$NON-NLS-1$

		/*tabGeneral.add(new JLabel("Description"), "cell 0 3,right");
		JScrollPane scrlDescription = new JScrollPane(tfDescription, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabGeneral.add(scrlDescription, "cell 1 3");*/

		tabGeneral.add(lblUnitName, "cell 0 3,right"); //$NON-NLS-1$
		tabGeneral.add(tfUnitName, "cell 1 3,grow"); //$NON-NLS-1$
		JLabel lblBuyPrice = new JLabel(Messages.getString("LABEL_BUY_PRICE")); //$NON-NLS-1$

		tabGeneral.add(lblBuyPrice, "cell 0 4,alignx right"); //$NON-NLS-1$
		tfBuyPrice = new DoubleTextField(20);
		tfBuyPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		tabGeneral.add(tfBuyPrice, "cell 1 4,grow"); //$NON-NLS-1$

		tabGeneral.add(lblPrice, "cell 0 5,alignx right"); //$NON-NLS-1$
		tabGeneral.add(tfPrice, "cell 1 5,grow"); //$NON-NLS-1$

		tabGeneral.add(lgroup, "cell 0 6,alignx right"); //$NON-NLS-1$
		tabGeneral.add(cbGroup, "cell 1 6"); //$NON-NLS-1$
		tabGeneral.add(btnNewGroup, "cell 1 6"); //$NON-NLS-1$

		tabGeneral.add(lblBarcode, "cell 0 7,alignx right"); //$NON-NLS-1$
		tabGeneral.add(tfBarcode, "cell 1 7,grow"); //$NON-NLS-1$

		tabGeneral.add(lblSortOrder, "cell 0 8,alignx right"); //$NON-NLS-1$
		tabGeneral.add(tfSortOrder, "cell 1 8,grow"); //$NON-NLS-1$

		tabGeneral.add(lblStockCount, "cell 0 9,alignx right"); //$NON-NLS-1$
		tabGeneral.add(tfStockCount, "cell 1 9,grow"); //$NON-NLS-1$

		//tabGeneral.add(cbShowTextWithImage, "cell 1 8"); //$NON-NLS-1$
		tabGeneral.add(chkVisible, "cell 1 10"); //$NON-NLS-1$
		tabGeneral.add(cbFractionalUnit, "cell 1 11"); //$NON-NLS-1$
		tabGeneral.add(cbDisableStockCount, "cell 1 12"); //$NON-NLS-1$

		// right side

		tabGeneral.add(lblKitchenPrinter, "cell 2 1,right"); //$NON-NLS-1$
		tabGeneral.add(cbPrinterGroup, "cell 3 1,grow"); //$NON-NLS-1$

		tabGeneral.add(lTax, "cell 2 2,right"); //$NON-NLS-1$
		tabGeneral.add(cbTaxGroup, "cell 3 2"); //$NON-NLS-1$

		/*tabGeneral.add(lblButtonColor, "cell 2 3,right"); //$NON-NLS-1$
		tabGeneral.add(btnButtonColor, "cell 3 3,grow"); //$NON-NLS-1$

		tabGeneral.add(lblTextColor, "cell 2 4,right"); //$NON-NLS-1$
		tabGeneral.add(btnTextColor, "cell 3 4 3 5"); //$NON-NLS-1$
		btnTextColor.setPreferredSize(new Dimension(228, 50));*/

		tabGeneral.add(new JLabel(Messages.getString("MenuItemForm.25")), "cell 2 3,,aligny top,alignx right"); //$NON-NLS-1$ //$NON-NLS-2$
		orderList = new CheckBoxList();

		List<OrderType> orderTypes = Application.getInstance().getOrderTypes();
		orderList.setModel(orderTypes);
		//		List<String> orderListM = new ArrayList();
		//		orderListM.add(OrderType.DINE_IN.toString());
		//		orderListM.add(OrderType.BAR_TAB.toString());
		//		orderListM.add(OrderType.DRIVE_THRU.toString());
		//		orderListM.add(OrderType.HOME_DELIVERY.toString());
		//		orderListM.add(OrderType.PICKUP.toString());
		//		orderListM.add(OrderType.RETAIL.toString());
		//		orderListM.add(OrderType.TAKE_OUT.toString());

		//		orderList.setModel(orderListM);

		JScrollPane orderCheckBoxList = new JScrollPane(orderList);
		orderCheckBoxList.setPreferredSize(new Dimension(228, 100));
		tabGeneral.add(orderCheckBoxList, "cell 3 3 3 4"); //$NON-NLS-1$
		tfDescription.setWrapStyleWord(true);
		tfDescription.setLineWrap(true);

		tabGeneral.add(new JLabel(Messages.getString("MenuItemForm.29")), "cell 2 7,aligny top,alignx right"); //$NON-NLS-1$ //$NON-NLS-2$
		JScrollPane scrlDescription = new JScrollPane(tfDescription, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrlDescription.setPreferredSize(new Dimension(228, 90));
		tabGeneral.add(scrlDescription, "cell 3 7 3 4"); //$NON-NLS-1$

		add(tabbedPane);
		//TODO: 
		addRecepieExtension();

		/*btnButtonColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MenuItemForm.this, Messages.getString("MenuItemForm.42"), btnButtonColor.getBackground()); //$NON-NLS-1$
				btnButtonColor.setBackground(color);
				btnTextColor.setBackground(color);
			}
		});

		btnTextColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MenuItemForm.this, Messages.getString("MenuItemForm.43"), btnTextColor.getForeground()); //$NON-NLS-1$
				btnTextColor.setForeground(color);
			}
		});*/

		jScrollPane1.setViewportView(tableTicketItemModifierGroups);

		GroupLayout jPanel2Layout = new GroupLayout(tabModifier);
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING).addGroup(
				jPanel2Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(btnDeleteModifierGroup).addComponent(btnEditModifierGroup)
										.addComponent(btnNewModifierGroup)).addContainerGap()));
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING).addGroup(
				jPanel2Layout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								jPanel2Layout
										.createParallelGroup(Alignment.LEADING)
										.addGroup(
												jPanel2Layout.createSequentialGroup().addComponent(btnNewModifierGroup)
														.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnEditModifierGroup)
														.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnDeleteModifierGroup))
										.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)).addContainerGap()));
		tabModifier.setLayout(jPanel2Layout);

		tabbedPane.addTab(com.floreantpos.POSConstants.MODIFIER_GROUPS, tabModifier);

		btnDeleteShift.setText(com.floreantpos.POSConstants.DELETE_SHIFT);

		btnAddShift.setText(com.floreantpos.POSConstants.ADD_SHIFT);

		shiftTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null },
				{ null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		jScrollPane2.setViewportView(shiftTable);

		org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(tabShift);
		tabShift.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel3Layout
						.createSequentialGroup()
						.addContainerGap(76, Short.MAX_VALUE)
						.add(jPanel3Layout
								.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
								.add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 670,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(org.jdesktop.layout.GroupLayout.TRAILING,
										jPanel3Layout.createSequentialGroup().add(btnAddShift).add(5, 5, 5).add(btnDeleteShift))).addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel3Layout.createSequentialGroup()
						.add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 345, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(btnAddShift).add(btnDeleteShift))
						.addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		tabbedPane.addTab(com.floreantpos.POSConstants.SHIFTS, tabShift);

		//

		btnNewPrice.setText(Messages.getString("MenuItemForm.9")); //$NON-NLS-1$
		btnNewPrice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewPrice();
			}
		});
		btnUpdatePrice.setText(Messages.getString("MenuItemForm.13")); //$NON-NLS-1$
		btnUpdatePrice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updatePrice();
			}
		});
		btnDeletePrice.setText(Messages.getString("MenuItemForm.14")); //$NON-NLS-1$
		btnDeletePrice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deletePrice();
			}
		});
		btnDeleteAll.setText(Messages.getString("MenuItemForm.15")); //$NON-NLS-1$
		btnDeleteAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteAll();
			}
		});

		btnDefaultValue.setText(Messages.getString("MenuItemForm.7")); //$NON-NLS-1$
		/*btnDefaultValue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setDefaultValue();
			}
		});*/
		priceTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null },
				{ null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		jScrollPane3.setViewportView(priceTable);

		tabPrice.setLayout(new BorderLayout());
		tabPrice.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tabPrice.add(jScrollPane3, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		buttonPanel.add(btnNewPrice);
		buttonPanel.add(btnUpdatePrice);
		//buttonPanel.add(btnDefaultValue);
		buttonPanel.add(btnDeletePrice);

		//	buttonPanel.add(btnDeleteAll);

		tabPrice.add(buttonPanel, BorderLayout.SOUTH);
		tabbedPane.addTab(Messages.getString("MenuItemForm.16"), tabPrice); //$NON-NLS-1$

		//

		tabbedPane.addChangeListener(this);
		//

		tabButtonStyle.setLayout(new MigLayout("insets 10", "[][]100[][][][]", "[][][center][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblImage = new JLabel(Messages.getString("MenuItemForm.28")); //$NON-NLS-1$
		lblImage.setHorizontalAlignment(SwingConstants.TRAILING);
		tabButtonStyle.add(lblImage, "cell 0 0,right"); //$NON-NLS-1$

		lblImagePreview = new JLabel(""); //$NON-NLS-1$
		lblImagePreview.setHorizontalAlignment(JLabel.CENTER);
		lblImagePreview.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		lblImagePreview.setPreferredSize(new Dimension(100, 100));
		tabButtonStyle.add(lblImagePreview, "cell 1 0"); //$NON-NLS-1$

		JButton btnSelectImage = new JButton("..."); //$NON-NLS-1$
		btnClearImage = new JButton(Messages.getString("MenuItemForm.34")); //$NON-NLS-1$
		tabButtonStyle.add(btnClearImage, "cell  1 0"); //$NON-NLS-1$
		tabButtonStyle.add(btnSelectImage, "cell 1 0"); //$NON-NLS-1$

		tabButtonStyle.add(lblButtonColor, "cell 0 2,right"); //$NON-NLS-1$
		tabButtonStyle.add(btnButtonColor, "cell 1 2,grow"); //$NON-NLS-1$
		tabButtonStyle.add(lblTextColor, "cell 0 3,right"); //$NON-NLS-1$
		tabButtonStyle.add(btnTextColor, "cell 1 3"); //$NON-NLS-1$
		tabButtonStyle.add(cbShowTextWithImage, "cell 1 4"); //$NON-NLS-1$

		/*	tabButtonStyle.add(lblImagePreview, "cell 3 0 3 4"); //$NON-NLS-1$
			tabButtonStyle.add(btnClearImage, "cell 3 0,gaptop 40"); //$NON-NLS-1$
			tabButtonStyle.add(btnSelectImage, "cell 3 0,gaptop 40"); //$NON-NLS-1$
			tabButtonStyle.add(lblImage, "cell 2 0,gaptop 40"); //$NON-NLS-1$
			tabButtonStyle.add(lblButtonColor, "cell 0 0,right"); //$NON-NLS-1$
			tabButtonStyle.add(btnButtonColor, "cell 1 0,grow"); //$NON-NLS-1$
			tabButtonStyle.add(lblTextColor, "cell 0 1,right"); //$NON-NLS-1$
			tabButtonStyle.add(btnTextColor, "cell 1 1"); //$NON-NLS-1$
		*/
		btnTextColor.setPreferredSize(new Dimension(228, 50));

		btnSelectImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSelectImageFile();
			}
		});

		btnClearImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doClearImage();
			}
		});

		btnButtonColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MenuItemForm.this, Messages.getString("MenuItemForm.42"), btnButtonColor.getBackground()); //$NON-NLS-1$
				btnButtonColor.setBackground(color);
				btnTextColor.setBackground(color);
			}
		});

		btnTextColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MenuItemForm.this, Messages.getString("MenuItemForm.43"), btnTextColor.getForeground()); //$NON-NLS-1$
				btnTextColor.setForeground(color);
			}
		});

		tabbedPane.addTab(Messages.getString("MenuItemForm.26"), tabButtonStyle); //$NON-NLS-1$

	}// </editor-fold>//GEN-END:initComponents

	private void btnNewModifierGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewModifierGroupActionPerformed

	}//GEN-LAST:event_btnNewModifierGroupActionPerformed

	private void doCreateNewGroup(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doCreateNewGroup
		MenuGroupForm editor = new MenuGroupForm();
		BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
		dialog.open();

		if (!dialog.isCanceled()) {
			MenuGroup foodGroup = (MenuGroup) editor.getBean();
			ComboBoxModel model = (ComboBoxModel) cbGroup.getModel();
			model.addElement(foodGroup);
			model.setSelectedItem(foodGroup);
		}
	}//GEN-LAST:event_doCreateNewGroup

	private void addMenuItemModifierGroup() {
		try {
			MenuItemModifierGroupForm form = new MenuItemModifierGroupForm();
			BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), form);
			dialog.open();
			if (!dialog.isCanceled()) {
				MenuItemModifierGroup modifier = (MenuItemModifierGroup) form.getBean();
				//modifier.setParentMenuItem((MenuItem) this.getBean());

				if (menuItemModifierGroups != null) {
					for (MenuItemModifierGroup modifierGroup : menuItemModifierGroups) {
						if (modifierGroup.getModifierGroup().equals(modifier.getModifierGroup())) {
							POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("MenuItemForm.48")); //$NON-NLS-1$
							return;
						}
					}
				}

				menuItemMGListModel.add(modifier);
			}
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

	private void editMenuItemModifierGroup() {
		try {
			int index = tableTicketItemModifierGroups.getSelectedRow();
			if (index < 0)
				return;

			MenuItemModifierGroup menuItemModifierGroup = menuItemMGListModel.get(index);

			MenuItemModifierGroupForm form = new MenuItemModifierGroupForm(menuItemModifierGroup);
			BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), form);
			dialog.open();
			if (!dialog.isCanceled()) {
				//menuItemModifierGroup.setParentMenuItem((MenuItem) this.getBean());
				menuItemMGListModel.fireTableDataChanged();
			}
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

	private void deleteMenuItemModifierGroup() {
		try {
			int index = tableTicketItemModifierGroups.getSelectedRow();
			if (index < 0)
				return;

			if (ConfirmDeleteDialog.showMessage(this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.CONFIRM) == ConfirmDeleteDialog.YES) {
				menuItemMGListModel.remove(index);
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

			MenuItem menuItem = (MenuItem) getBean();
			MenuItemDAO menuItemDAO = new MenuItemDAO();
			menuItemDAO.saveOrUpdate(menuItem);
		} catch (Exception e) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, e);
			return false;
		}
		return true;
	}

	@Override
	protected void updateView() {
		MenuItem menuItem = getBean();

		if (menuItem.getId() != null && !Hibernate.isInitialized(menuItem.getMenuItemModiferGroups())) {
			//initialize food item modifer groups.
			MenuItemDAO dao = new MenuItemDAO();
			Session session = dao.getSession();
			menuItem = (MenuItem) session.merge(menuItem);
			Hibernate.initialize(menuItem.getMenuItemModiferGroups());
			session.close();
		}

		//	terminalList.selectItems(menuItem.getTerminals());
		orderList.selectItems(menuItem.getOrderTypeList());
		tfName.setText(menuItem.getName());
		tfDescription.setText(menuItem.getDescription());
		tfTranslatedName.setText(menuItem.getTranslatedName());
		tfBarcode.setText(menuItem.getBarcode());
		tfBuyPrice.setText(String.valueOf(menuItem.getBuyPrice()));
		tfPrice.setText(String.valueOf(menuItem.getPrice()));
		tfUnitName.setText(menuItem.getUnitName());
		tfDiscountRate.setText(String.valueOf(menuItem.getDiscountRate()));
		tfStockCount.setText(String.valueOf(menuItem.getStockAmount()));
		chkVisible.setSelected(menuItem.isVisible());
		cbShowTextWithImage.setSelected(menuItem.isShowImageOnly());
		cbDisableStockCount.setSelected(menuItem.isDisableWhenStockAmountIsZero());
		ImageIcon menuItemImage = menuItem.getImage();
		if (menuItemImage != null) {
			lblImagePreview.setIcon(menuItemImage);
		}

		cbGroup.setSelectedItem(menuItem.getParent());
		cbTaxGroup.setSelectedItem(menuItem.getTaxGroup());

		cbPrinterGroup.setSelectedItem(menuItem.getPrinterGroup());

		if (menuItem.getSortOrder() != null) {
			tfSortOrder.setText(menuItem.getSortOrder().toString());
		}

		Color buttonColor = menuItem.getButtonColor();
		if (buttonColor != null) {
			btnButtonColor.setBackground(buttonColor);
			btnTextColor.setBackground(buttonColor);
		}

		if (menuItem.getTextColor() != null) {
			btnTextColor.setForeground(menuItem.getTextColor());
		}
		cbFractionalUnit.setSelected(menuItem.isFractionalUnit());
	}

	@Override
	protected boolean updateModel() {
		String itemName = tfName.getText();
		if (POSUtil.isBlankOrNull(itemName)) {
			MessageDialog.showError(com.floreantpos.POSConstants.NAME_REQUIRED);
			return false;
		}

		MenuItem menuItem = getBean();
		menuItem.setName(itemName);
		menuItem.setDescription(tfDescription.getText());
		menuItem.setBarcode(tfBarcode.getText());
		menuItem.setParent((MenuGroup) cbGroup.getSelectedItem());
		menuItem.setBuyPrice(tfBuyPrice.getDouble());
		menuItem.setPrice(Double.valueOf(tfPrice.getText()));
		menuItem.setUnitName(tfUnitName.getText());
		menuItem.setTaxGroup((TaxGroup) cbTaxGroup.getSelectedItem());
		menuItem.setStockAmount(Double.parseDouble(tfStockCount.getText()));
		menuItem.setVisible(chkVisible.isSelected());
		menuItem.setShowImageOnly(cbShowTextWithImage.isSelected());
		menuItem.setFractionalUnit(cbFractionalUnit.isSelected());
		menuItem.setDisableWhenStockAmountIsZero(cbDisableStockCount.isSelected());

		menuItem.setTranslatedName(tfTranslatedName.getText());
		menuItem.setSortOrder(tfSortOrder.getInteger());

		menuItem.setButtonColorCode(btnButtonColor.getBackground().getRGB());
		menuItem.setTextColorCode(btnTextColor.getForeground().getRGB());

		/*	if (!terminalList.getCheckedValues().isEmpty()) {
				menuItem.setTerminals(terminalList.getCheckedValues());
			}*/

		if (orderList.getCheckedValues().isEmpty()) {
			menuItem.setOrderTypeList(null);
		}
		else {
			menuItem.setOrderTypeList(orderList.getCheckedValues());
		}
		try {
			menuItem.setDiscountRate(Double.parseDouble(tfDiscountRate.getText()));
		} catch (Exception x) {
		}
		menuItem.setMenuItemModiferGroups(menuItemModifierGroups);
		menuItem.setShifts(shiftTableModel.getShifts());

		int tabCount = tabbedPane.getTabCount();
		for (int i = 0; i < tabCount; i++) {
			Component componentAt = tabbedPane.getComponent(i);
			if (componentAt instanceof IUpdatebleView) {
				IUpdatebleView view = (IUpdatebleView) componentAt;
				if (!view.updateModel(menuItem)) {
					return false;
				}
			}
		}

		menuItem.setPrinterGroup((PrinterGroup) cbPrinterGroup.getSelectedItem());

		return true;
	}

	public String getDisplayText() {
		MenuItem foodItem = (MenuItem) getBean();
		if (foodItem.getId() == null) {
			return com.floreantpos.POSConstants.NEW_MENU_ITEM;
		}
		return com.floreantpos.POSConstants.EDIT_MENU_ITEM;
	}

	class MenuItemMGListModel extends AbstractTableModel {
		String[] cn = { com.floreantpos.POSConstants.GROUP_NAME, com.floreantpos.POSConstants.MIN_QUANTITY, com.floreantpos.POSConstants.MAX_QUANTITY };

		MenuItemMGListModel() {
		}

		public MenuItemModifierGroup get(int index) {
			return menuItemModifierGroups.get(index);
		}

		public void add(MenuItemModifierGroup group) {
			if (menuItemModifierGroups == null) {
				menuItemModifierGroups = new ArrayList<MenuItemModifierGroup>();
			}
			menuItemModifierGroups.add(group);
			fireTableDataChanged();
		}

		public void remove(int index) {
			if (menuItemModifierGroups == null) {
				return;
			}
			menuItemModifierGroups.remove(index);
			fireTableDataChanged();
		}

		public void remove(MenuItemModifierGroup group) {
			if (menuItemModifierGroups == null) {
				return;
			}
			menuItemModifierGroups.remove(group);
			fireTableDataChanged();
		}

		public int getRowCount() {
			if (menuItemModifierGroups == null)
				return 0;

			return menuItemModifierGroups.size();

		}

		public int getColumnCount() {
			return cn.length;
		}

		@Override
		public String getColumnName(int column) {
			return cn[column];
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItemModifierGroup menuItemModifierGroup = menuItemModifierGroups.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return menuItemModifierGroup.getModifierGroup().getName();

				case 1:
					return Integer.valueOf(menuItemModifierGroup.getMinQuantity());

				case 2:
					return Integer.valueOf(menuItemModifierGroup.getMaxQuantity());
			}
			return null;
		}
	}

	class ShiftTableModel extends AbstractTableModel {
		List<MenuItemShift> shifts;
		String[] cn = { com.floreantpos.POSConstants.START_TIME, com.floreantpos.POSConstants.END_TIME, com.floreantpos.POSConstants.PRICE };
		Calendar calendar = Calendar.getInstance();

		ShiftTableModel(List<MenuItemShift> shifts) {
			if (shifts == null) {
				this.shifts = new ArrayList<MenuItemShift>();
			}
			else {
				this.shifts = new ArrayList<MenuItemShift>(shifts);
			}
		}

		public MenuItemShift get(int index) {
			return shifts.get(index);
		}

		public void add(MenuItemShift group) {
			if (shifts == null) {
				shifts = new ArrayList<MenuItemShift>();
			}
			shifts.add(group);
			fireTableDataChanged();
		}

		public void remove(int index) {
			if (shifts == null) {
				return;
			}
			shifts.remove(index);
			fireTableDataChanged();
		}

		public void remove(MenuItemShift group) {
			if (shifts == null) {
				return;
			}
			shifts.remove(group);
			fireTableDataChanged();
		}

		public int getRowCount() {
			if (shifts == null)
				return 0;

			return shifts.size();

		}

		public int getColumnCount() {
			return cn.length;
		}

		@Override
		public String getColumnName(int column) {
			return cn[column];
		}

		public List<MenuItemShift> getShifts() {
			return shifts;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItemShift shift = shifts.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return ShiftUtil.buildShiftTimeRepresentation(shift.getShift().getStartTime());

				case 1:
					return ShiftUtil.buildShiftTimeRepresentation(shift.getShift().getEndTime());

				case 2:
					return String.valueOf(shift.getShiftPrice());
			}
			return null;
		}
	}

	//

	class PriceByOrderTypeTableModel extends AbstractTableModel {
		List<String> propertiesKey = new ArrayList<String>();

		String[] cn = { "ORDER TYPE", "PRICE", "TAX" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		PriceByOrderTypeTableModel(Map<String, String> properties) {

			if (properties != null && !properties.isEmpty()) {
				List<String> keys = new ArrayList(properties.keySet());
				setPropertiesToTable(keys);
			}
		}

		private void setPropertiesToTable(List<String> keys) {
			propertiesKey.clear();

			for (int i = 0; i < keys.size(); i++) {
				if (keys.get(i).contains("_PRICE")) { //$NON-NLS-1$
					this.propertiesKey.add(keys.get(i));
				}
			}
		}

		public String get(int index) {
			return propertiesKey.get(index);
		}

		public void add(MenuItem menuItem) {
			setPropertiesToTable(new ArrayList(menuItem.getProperties().keySet()));
			fireTableDataChanged();
		}

		/*public void setDefaultValue() {
			int selectedRow = priceTable.getSelectedRow();
			if (selectedRow == -1) {
				POSMessageDialog.showMessage(Messages.getString("MenuItemForm.8")); //$NON-NLS-1$
				return;
			}
			String modifiedKey = priceTableModel.propertiesKey.get(selectedRow);
			modifiedKey = modifiedKey.replaceAll("_PRICE", ""); //$NON-NLS-1$ //$NON-NLS-2$
			modifiedKey = modifiedKey.replaceAll("_", " ");//$NON-NLS-1$ //$NON-NLS-2$
			menuItem.setPriceByOrderType(modifiedKey, menuItem.getPrice());
			if (menuItem.getTax() != null) {
				menuItem.setTaxByOrderType(modifiedKey, menuItem.getTax().getRate());
			}
			else {
				menuItem.setTaxByOrderType(modifiedKey, 0.0);
			}

			MenuItemDAO.getInstance().saveOrUpdate(menuItem);
			add(menuItem);
			fireTableDataChanged();
		}*/

		public void remove(int index) {
			if (propertiesKey == null) {
				return;
			}
			String typeProperty = propertiesKey.get(index);
			String taxProperty = typeProperty.replaceAll("_PRICE", "_TAX"); //$NON-NLS-1$ //$NON-NLS-2$

			menuItem.removeProperty(typeProperty, taxProperty);
			MenuItemDAO.getInstance().saveOrUpdate(menuItem);

			propertiesKey.remove(index);
			fireTableDataChanged();
		}

		public void removeAll() {
			menuItem.getProperties().clear();
			MenuItemDAO.getInstance().saveOrUpdate(menuItem);
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
					return menuItem.getStringWithOutUnderScore(key, "_PRICE"); //$NON-NLS-1$
				case 1:
					return menuItem.getProperty(key);
				case 2:
					return menuItem.getProperty(menuItem.replaceString(key, "_PRICE", "_TAX")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			return null;
		}
	}

	//
	private void addShift() {
		MenuItemShiftDialog dialog = new MenuItemShiftDialog(this.getParentFrame());
		dialog.setSize(350, 220);
		dialog.open();

		if (!dialog.isCanceled()) {
			MenuItemShift menuItemShift = dialog.getMenuItemShift();
			shiftTableModel.add(menuItemShift);
		}
	}

	private void addNewPrice() {

		MenuItemPriceByOrderTypeDialog dialog = new MenuItemPriceByOrderTypeDialog(this.getParentFrame(), menuItem);
		dialog.setSize(350, 220);
		dialog.open();
		if (!dialog.isCanceled()) {
			priceTableModel.add(dialog.getMenuItem());
		}
	}

	private void deleteShift() {
		int selectedRow = shiftTable.getSelectedRow();
		if (selectedRow >= 0) {
			shiftTableModel.remove(selectedRow);
		}
	}

	private void deletePrice() {
		int selectedRow = priceTable.getSelectedRow();
		if (selectedRow == -1) {
			POSMessageDialog.showMessage(this.getParentFrame(), Messages.getString("MenuItemForm.32")); //$NON-NLS-1$
			return;
		}
		int option = POSMessageDialog.showYesNoQuestionDialog(this.getParentFrame(),
				Messages.getString("MenuItemForm.33"), Messages.getString("MenuItemForm.35")); //$NON-NLS-1$ //$NON-NLS-2$
		if (option != JOptionPane.YES_OPTION) {
			return;
		}

		priceTableModel.remove(selectedRow);
	}

	private void deleteAll() {

		int option = POSMessageDialog.showYesNoQuestionDialog(this.getParentFrame(),
				Messages.getString("MenuItemForm.36"), Messages.getString("MenuItemForm.37")); //$NON-NLS-1$ //$NON-NLS-2$
		if (option != JOptionPane.YES_OPTION) {
			return;
		}
		priceTableModel.removeAll();
	}

	/*private void setDefaultValue() {
		priceTableModel.setDefaultValue();
	}*/

	private void updatePrice() {
		int selectedRow = priceTable.getSelectedRow();
		if (selectedRow == -1) {
			POSMessageDialog.showMessage(this.getParentFrame(), Messages.getString("MenuItemForm.38")); //$NON-NLS-1$
			return;
		}
		priceTableModel.propertiesKey.get(selectedRow);
		MenuItemPriceByOrderTypeDialog dialog = new MenuItemPriceByOrderTypeDialog(this.getParentFrame(), menuItem,
				priceTableModel.propertiesKey.get(selectedRow));
		dialog.setSize(350, 220);
		dialog.open();
		if (!dialog.isCanceled()) {
			priceTableModel.add(dialog.getMenuItem());
		}
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("AddModifierGroup")) { //$NON-NLS-1$
			addMenuItemModifierGroup();
		}
		else if (actionCommand.equals("EditModifierGroup")) { //$NON-NLS-1$
			editMenuItemModifierGroup();
		}
		else if (actionCommand.equals("DeleteModifierGroup")) { //$NON-NLS-1$
			deleteMenuItemModifierGroup();
		}
		else if (actionCommand.equals(com.floreantpos.POSConstants.ADD_SHIFT)) {
			addShift();
		}
		else if (actionCommand.equals(com.floreantpos.POSConstants.DELETE_SHIFT)) {
			deleteShift();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Component selectedComponent = tabbedPane.getSelectedComponent();
		if (!(selectedComponent instanceof IUpdatebleView)) {
			return;
		}

		IUpdatebleView view = (IUpdatebleView) selectedComponent;

		MenuItem menuItem = (MenuItem) getBean();
		view.initView(menuItem);
	}
}
