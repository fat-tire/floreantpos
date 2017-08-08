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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
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
import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PizzaCrust;
import com.floreantpos.model.PizzaPrice;
import com.floreantpos.model.PrinterGroup;
import com.floreantpos.model.Tax;
import com.floreantpos.model.TaxGroup;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuItemSizeDAO;
import com.floreantpos.model.dao.PizzaCrustDAO;
import com.floreantpos.model.dao.PrinterGroupDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.TaxGroupDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.CheckBoxList;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.IUpdatebleView;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.PosUIManager;
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
public class PizzaItemForm extends BeanEditor<MenuItem> implements ActionListener, ChangeListener {

	private JTabbedPane tabbedPane;
	private JTable shiftTable;
	private JTable priceTable;
	private JTable tableTicketItemModifierGroups;

	private FixedLengthTextField tfName;
	private FixedLengthTextField tfTranslatedName;
	private javax.swing.JComboBox cbGroup;
	private FixedLengthTextField tfBarcode;
	private DoubleTextField tfStockCount;
	private JCheckBox chkVisible;
	private JCheckBox cbDisableStockCount;
	private IntegerTextField tfDefaultSellPortion;
	private JComboBox<PrinterGroup> cbPrinterGroup;
	private JComboBox cbTaxGroup;
	private CheckBoxList orderList;
	private JTextArea tfDescription;

	private List<MenuItemModifierGroup> menuItemModifierGroups;
	private MenuItemMGListModel menuItemMGListModel;
	private JLabel lblImagePreview;
	private JButton btnClearImage;
	private JCheckBox cbShowTextWithImage;
	private JLabel lblKitchenPrinter;

	private JButton btnButtonColor;
	private JButton btnTextColor;
	private IntegerTextField tfSortOrder;

	private ShiftTableModel shiftTableModel;
	private BeanTableModel<PizzaPrice> priceTableModel;
	private MenuItem menuItem;

	public PizzaItemForm() throws Exception {
		this(new MenuItem());
	}

	public PizzaItemForm(MenuItem menuItem) throws Exception {
		this.menuItem = menuItem;
		initComponents();
		initData();
	}

	private void initData() {
		MenuGroupDAO foodGroupDAO = new MenuGroupDAO();
		List<MenuGroup> foodGroups = foodGroupDAO.findAll();
		cbGroup.setModel(new ComboBoxModel(foodGroups));

		TaxGroupDAO taxDAO = new TaxGroupDAO();
		List<TaxGroup> taxGroups = taxDAO.findAll();
		cbTaxGroup.setModel(new ComboBoxModel(taxGroups));

		menuItemModifierGroups = menuItem.getMenuItemModiferGroups();
		shiftTable.setModel(shiftTableModel = new ShiftTableModel(menuItem.getShifts()));

		priceTableModel = new BeanTableModel<PizzaPrice>(PizzaPrice.class) {
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				if (columnIndex == 2) {
					return true;
				}
				return false;
			}

			@Override
			public void setValueAt(Object value, int rowIndex, int columnIndex) {
				if (columnIndex == 2) {
					PizzaPrice price = priceTableModel.getRow(rowIndex);
					price.setPrice((double) value);
				}
			}
		};
		priceTableModel.addColumn("SIZE", "size");
		priceTableModel.addColumn("CRUST", "crust");
		priceTableModel.addColumn("PRICE", "price");

		List<PizzaPrice> pizzaPriceList = menuItem.getPizzaPriceList();

		if (pizzaPriceList == null || pizzaPriceList.isEmpty())
			priceTableModel.addRows(generatedPossiblePizzaItemSizeAndPriceList());
		else
			priceTableModel.addRows(pizzaPriceList);

		priceTable.setModel(priceTableModel);
		setBean(menuItem);

		priceTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					editEvent();
				}
			}
		});
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		JLabel lblButtonColor = new JLabel(Messages.getString("MenuItemForm.19")); //$NON-NLS-1$
		tabbedPane = new javax.swing.JTabbedPane();

		JPanel tabGeneral = new javax.swing.JPanel();

		JLabel lblName = new JLabel();
		lblName.setHorizontalAlignment(SwingConstants.TRAILING);

		tfName = new com.floreantpos.swing.FixedLengthTextField(20);
		tfDescription = new JTextArea(new FixedLengthDocument(120));

		JLabel lTax = new javax.swing.JLabel();
		lTax.setHorizontalAlignment(SwingConstants.TRAILING);

		cbTaxGroup = new javax.swing.JComboBox();
		JButton btnNewTax = new javax.swing.JButton();

		JPanel tabShift = new javax.swing.JPanel();
		JPanel tabPrice = new javax.swing.JPanel();

		JPanel tabButtonStyle = new javax.swing.JPanel();
		JButton btnDeleteShift = new javax.swing.JButton();
		JButton btnAddShift = new javax.swing.JButton();

		JButton btnNewPrice = new javax.swing.JButton();
		JButton btnUpdatePrice = new javax.swing.JButton();
		JButton btnDeletePrice = new javax.swing.JButton();
		JButton btnDeleteAll = new javax.swing.JButton();
		JButton btnDefaultValue = new javax.swing.JButton();
		JButton btnAutoGenerate = new javax.swing.JButton();

		JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
		JScrollPane priceTabScrollPane = new javax.swing.JScrollPane();

		shiftTable = new JTable();

		priceTable = new JTable();
		priceTable.setRowHeight(PosUIManager.getSize(priceTable.getRowHeight()));
		priceTable.setCellSelectionEnabled(true);
		priceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		priceTable.setSurrendersFocusOnKeystroke(true);
		cbPrinterGroup = new JComboBox<PrinterGroup>(new DefaultComboBoxModel<PrinterGroup>(PrinterGroupDAO.getInstance().findAll()
				.toArray(new PrinterGroup[0])));
		cbPrinterGroup.setPreferredSize(new Dimension(226, 0));

		tfDefaultSellPortion = new IntegerTextField(10);
		tfTranslatedName = new FixedLengthTextField(20);
		tfTranslatedName.setLength(120);
		lblKitchenPrinter = new JLabel(Messages.getString("MenuItemForm.27")); //$NON-NLS-1$
		lblName.setText(Messages.getString("LABEL_NAME")); //$NON-NLS-1$
		tfName.setLength(120);
		JLabel lblTranslatedName = new JLabel(Messages.getString("MenuItemForm.lblTranslatedName.text")); //$NON-NLS-1$
		tfSortOrder = new IntegerTextField(20);
		tfSortOrder.setText(""); //$NON-NLS-1$
		cbTaxGroup.setPreferredSize(new Dimension(198, 0));
		btnButtonColor = new JButton(); //$NON-NLS-1$
		btnButtonColor.setPreferredSize(new Dimension(228, 40));
		JLabel lblTextColor = new JLabel(Messages.getString("MenuItemForm.lblTextColor.text")); //$NON-NLS-1$
		btnTextColor = new JButton(Messages.getString("MenuItemForm.SAMPLE_TEXT")); //$NON-NLS-1$
		cbShowTextWithImage = new JCheckBox(Messages.getString("MenuItemForm.40")); //$NON-NLS-1$
		cbShowTextWithImage.setActionCommand(Messages.getString("MenuItemForm.41")); //$NON-NLS-1$
		lTax.setText(Messages.getString("LABEL_TAX")); //$NON-NLS-1$
		btnNewTax.setText("...");
		lTax.setText(Messages.getString("LABEL_TAX")); //$NON-NLS-1$

		btnNewTax.setText("..."); //$NON-NLS-1$
		btnNewTax.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnNewTaxdoCreateNewTax(evt);
			}
		});

		tabbedPane.addTab(com.floreantpos.POSConstants.GENERAL, tabGeneral);
		tabbedPane.setPreferredSize(new Dimension(750, 470));

		tabbedPane.addTab(com.floreantpos.POSConstants.MODIFIER_GROUPS, getModifierGroupTab());

		btnAddShift.addActionListener(this);
		btnDeleteShift.addActionListener(this);

		tabGeneral.setLayout(new MigLayout("insets 20", "[][]20px[][]", "[][][][][][][][][][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		tabGeneral.add(lblName, "cell 0 1 ,right"); //$NON-NLS-1$
		tabGeneral.add(tfName, "cell 1 1,grow"); //$NON-NLS-1$

		tabGeneral.add(lblTranslatedName, "cell 0 2,right"); //$NON-NLS-1$
		tabGeneral.add(tfTranslatedName, "cell 1 2,grow");

		JLabel lgroup = new javax.swing.JLabel();
		lgroup.setHorizontalAlignment(SwingConstants.TRAILING);
		lgroup.setText(Messages.getString("LABEL_GROUP")); //$NON-NLS-1$

		tabGeneral.add(lgroup, "cell 0 3,alignx right"); //$NON-NLS-1$
		JLabel lblBarcode = new JLabel(Messages.getString("MenuItemForm.lblBarcode.text")); //$NON-NLS-1$

		tabGeneral.add(lblBarcode, "cell 0 4,alignx right"); //$NON-NLS-1$
		tfBarcode = new FixedLengthTextField(20);
		tabGeneral.add(tfBarcode, "cell 1 4,grow"); //$NON-NLS-1$
		JLabel lblStockCount = new JLabel(Messages.getString("MenuItemForm.17")); //$NON-NLS-1$

		tabGeneral.add(lblStockCount, "cell 0 5,alignx right"); //$NON-NLS-1$
		tfStockCount = new DoubleTextField(1);
		tabGeneral.add(tfStockCount, "cell 1 5,grow"); //$NON-NLS-1$
		chkVisible = new javax.swing.JCheckBox();

		tabGeneral.add(new JLabel("Default sell portion (%)"), "cell 0 6");
		tabGeneral.add(tfDefaultSellPortion, "cell 1 6,grow");

		chkVisible.setText(com.floreantpos.POSConstants.VISIBLE);
		chkVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkVisible.setMargin(new java.awt.Insets(0, 0, 0, 0));

		tabGeneral.add(chkVisible, "cell 1 7");
		tabGeneral.add(lblKitchenPrinter, "cell 2 1,right"); //$NON-NLS-1$
		tabGeneral.add(cbPrinterGroup, "cell 3 1,grow"); //$NON-NLS-1$

		tabGeneral.add(lTax, "cell 2 2,right"); //$NON-NLS-1$
		tabGeneral.add(cbTaxGroup, "cell 3 2"); //$NON-NLS-1$
		tabGeneral.add(btnNewTax, "cell 3 2,grow"); //$NON-NLS-1$

		cbGroup = new javax.swing.JComboBox();
		cbGroup.setPreferredSize(new Dimension(198, 0));

		tabGeneral.add(cbGroup, "flowx,cell 1 3"); //$NON-NLS-1$
		JButton btnNewGroup = new javax.swing.JButton();

		btnNewGroup.setText("..."); //$NON-NLS-1$
		btnNewGroup.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doCreateNewGroup(evt);
			}
		});
		tabGeneral.add(btnNewGroup, "cell 1 3"); //$NON-NLS-1$

		tabGeneral.add(new JLabel(Messages.getString("MenuItemForm.25")), "cell 2 3,right"); //$NON-NLS-1$ //$NON-NLS-2$
		orderList = new CheckBoxList();

		List<OrderType> orderTypes = Application.getInstance().getOrderTypes();
		orderList.setModel(orderTypes);

		JScrollPane orderCheckBoxList = new JScrollPane(orderList);
		orderCheckBoxList.setPreferredSize(new Dimension(228, 100));
		tabGeneral.add(orderCheckBoxList, "cell 3 3 3 3"); //$NON-NLS-1$
		cbDisableStockCount = new JCheckBox(Messages.getString("MenuItemForm.18")); //$NON-NLS-1$
		tabGeneral.add(cbDisableStockCount, "cell 1 8"); //$NON-NLS-1$

		tabGeneral.add(new JLabel(Messages.getString("MenuItemForm.29")), "cell 2 6,alignx right"); //$NON-NLS-1$ //$NON-NLS-2$
		JScrollPane scrlDescription = new JScrollPane(tfDescription, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrlDescription.setPreferredSize(new Dimension(228, 70));
		tfDescription.setLineWrap(true);
		tabGeneral.add(scrlDescription, "cell 3 6 3 3"); //$NON-NLS-1$

		add(tabbedPane);
		//TODO: 
		addRecepieExtension();

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

		btnAutoGenerate.setText("Auto Generate"); //$NON-NLS-1$
		btnAutoGenerate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				autoGeneratePizzaItemSizeAndPrice();
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
		priceTabScrollPane.setViewportView(priceTable);

		tabPrice.setLayout(new BorderLayout());
		tabPrice.add(priceTabScrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		buttonPanel.add(btnNewPrice);
		buttonPanel.add(btnUpdatePrice);
		buttonPanel.add(btnDeletePrice);
		buttonPanel.add(btnAutoGenerate);

		tabPrice.add(buttonPanel, BorderLayout.SOUTH);
		tabGeneral.add(tabPrice, "cell 0 10,grow,span");
		tabbedPane.addChangeListener(this);

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
				Color color = JColorChooser.showDialog(POSUtil.getBackOfficeWindow(), Messages.getString("MenuItemForm.42"), btnButtonColor.getBackground()); //$NON-NLS-1$
				btnButtonColor.setBackground(color);
				btnTextColor.setBackground(color);
			}
		});

		btnTextColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(POSUtil.getBackOfficeWindow(), Messages.getString("MenuItemForm.43"), btnTextColor.getForeground()); //$NON-NLS-1$
				btnTextColor.setForeground(color);
			}
		});

		tabbedPane.addTab(Messages.getString("MenuItemForm.26"), tabButtonStyle); //$NON-NLS-1$
	}

	private void autoGeneratePizzaItemSizeAndPrice() {
		List<PizzaPrice> pizzaPriceList = generatedPossiblePizzaItemSizeAndPriceList();
		filterDuplicateItemSizesAndPrices(pizzaPriceList);
		priceTableModel.addRows(pizzaPriceList);
		priceTable.repaint();
	}

	private JPanel getModifierGroupTab() {
		JPanel tabModifierGroup = new JPanel(new MigLayout("fill"));

		JButton btnNewModifierGroup = new javax.swing.JButton();
		JButton btnEditModifierGroup = new javax.swing.JButton();
		JButton btnDeleteModifierGroup = new javax.swing.JButton();

		tableTicketItemModifierGroups = new javax.swing.JTable();

		JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
		jScrollPane1.setViewportView(tableTicketItemModifierGroups);

		tabModifierGroup.add(jScrollPane1, "span,grow");
		tabModifierGroup.add(btnNewModifierGroup, "left,split 3");
		tabModifierGroup.add(btnEditModifierGroup);
		tabModifierGroup.add(btnDeleteModifierGroup);

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
		return tabModifierGroup;
	}

	private void doSelectImageFile() {
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

	private void btnNewTaxdoCreateNewTax(java.awt.event.ActionEvent evt) {
		BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), new TaxForm());
		dialog.open();
	}

	private void btnNewModifierGroupActionPerformed(java.awt.event.ActionEvent evt) {
	}

	private void doCreateNewGroup(java.awt.event.ActionEvent evt) {
		MenuGroupForm editor = new MenuGroupForm();
		BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
		dialog.open();

		if (!dialog.isCanceled()) {
			MenuGroup foodGroup = (MenuGroup) editor.getBean();
			ComboBoxModel model = (ComboBoxModel) cbGroup.getModel();
			model.addElement(foodGroup);
			model.setSelectedItem(foodGroup);
		}
	}

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
		tfStockCount.setText(String.valueOf(menuItem.getStockAmount()));
		chkVisible.setSelected(menuItem.isVisible());
		cbShowTextWithImage.setSelected(menuItem.isShowImageOnly());
		cbDisableStockCount.setSelected(menuItem.isDisableWhenStockAmountIsZero());
		ImageIcon menuItemImage = menuItem.getImage();
		if (menuItemImage != null) {
			lblImagePreview.setIcon(menuItemImage);
		}
		if (menuItem.getId() == null)
			tfDefaultSellPortion.setText(String.valueOf(100));
		else
			tfDefaultSellPortion.setText(String.valueOf(menuItem.getDefaultSellPortion()));

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

	}

	@Override
	protected boolean updateModel() {
		String itemName = tfName.getText();
		if (POSUtil.isBlankOrNull(itemName)) {
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), com.floreantpos.POSConstants.NAME_REQUIRED);
			return false;
		}

		MenuItem menuItem = getBean();
		menuItem.setName(itemName);
		menuItem.setDescription(tfDescription.getText());
		menuItem.setBarcode(tfBarcode.getText());
		menuItem.setParent((MenuGroup) cbGroup.getSelectedItem());
		menuItem.setTaxGroup((TaxGroup) cbTaxGroup.getSelectedItem());
		menuItem.setStockAmount(Double.parseDouble(tfStockCount.getText()));
		menuItem.setVisible(chkVisible.isSelected());
		menuItem.setShowImageOnly(cbShowTextWithImage.isSelected());
		menuItem.setDisableWhenStockAmountIsZero(cbDisableStockCount.isSelected());
		menuItem.setDefaultSellPortion(tfDefaultSellPortion.getInteger());

		menuItem.setTranslatedName(tfTranslatedName.getText());
		menuItem.setSortOrder(tfSortOrder.getInteger());

		menuItem.setButtonColorCode(btnButtonColor.getBackground().getRGB());
		menuItem.setTextColorCode(btnTextColor.getForeground().getRGB());

		if (orderList.getCheckedValues().isEmpty()) {
			menuItem.setOrderTypeList(null);
		}
		else {
			menuItem.setOrderTypeList(orderList.getCheckedValues());
		}
		menuItem.setMenuItemModiferGroups(menuItemModifierGroups);
		menuItem.setShifts(shiftTableModel.getShifts());

		List<PizzaPrice> pizzaPriceList = priceTableModel.getRows();
		if (menuItem.getPizzaPriceList() != null) {
			menuItem.getPizzaPriceList().clear();
		}
		for (PizzaPrice pizzaPrice : pizzaPriceList) {
			menuItem.addTopizzaPriceList(pizzaPrice);
		}

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
		menuItem.setPizzaType(true);

		return true;
	}

	public String getDisplayText() {
		MenuItem foodItem = (MenuItem) getBean();
		if (foodItem.getId() == null) {
			return "New pizza item";
		}
		return "Edit pizza item";
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
		List<PizzaPrice> pizzaPriceList = priceTableModel.getRows();
		PizzaItemPriceDialog dialog = new PizzaItemPriceDialog(this.getParentFrame(), null, pizzaPriceList);
		dialog.setTitle("Add New Price");
		dialog.setSize(PosUIManager.getSize(350, 220));
		dialog.open();
		if (dialog.isCanceled()) {
			return;
		}
		PizzaPrice pizzaPrice = dialog.getPizzaPrice();
		priceTableModel.addRow(pizzaPrice);
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

		priceTableModel.removeRow(selectedRow);
	}

	private void deleteAll() {

		int option = POSMessageDialog.showYesNoQuestionDialog(this.getParentFrame(),
				Messages.getString("MenuItemForm.36"), Messages.getString("MenuItemForm.37")); //$NON-NLS-1$ //$NON-NLS-2$
		if (option != JOptionPane.YES_OPTION) {
			return;
		}
		priceTableModel.removeAll();
	}

	private void updatePrice() {
		editEvent();
	}

	private void editEvent() {
		List<PizzaPrice> pizzaPriceList = priceTableModel.getRows();
		int selectedRow = priceTable.getSelectedRow();
		if (selectedRow == -1) {
			POSMessageDialog.showMessage(this.getParentFrame(), Messages.getString("MenuItemForm.38")); //$NON-NLS-1$
			return;
		}
		PizzaPrice pizzaPrice = priceTableModel.getRow(selectedRow);
		PizzaItemPriceDialog pizzaItemPriceDialog = new PizzaItemPriceDialog(this.getParentFrame(), pizzaPrice, pizzaPriceList);
		pizzaItemPriceDialog.setTitle("Edit Pizza Price");
		pizzaItemPriceDialog.setSize(PosUIManager.getSize(350, 220));
		pizzaItemPriceDialog.open();

		if (pizzaItemPriceDialog.isCanceled()) {
			return;
		}
		priceTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
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

	private void filterDuplicateItemSizesAndPrices(List<PizzaPrice> pizzaPriceList) {
		List<PizzaPrice> existedPizzaPriceValueList = priceTableModel.getRows();

		if (existedPizzaPriceValueList != null) {
			for (Iterator iterator = existedPizzaPriceValueList.iterator(); iterator.hasNext();) {
				PizzaPrice existingPizzaPrice = (PizzaPrice) iterator.next();

				for (Iterator iterator2 = pizzaPriceList.iterator(); iterator2.hasNext();) {
					PizzaPrice pizzaPrice = (PizzaPrice) iterator2.next();
					if ((existingPizzaPrice.getSize().equals(pizzaPrice.getSize()) && existingPizzaPrice.getCrust().equals(pizzaPrice.getCrust()))) {
						iterator2.remove();
					}
				}
			}
		}
	}

	private List<PizzaPrice> generatedPossiblePizzaItemSizeAndPriceList() {
		List<MenuItemSize> menuItemSizeList = MenuItemSizeDAO.getInstance().findAll();
		List<PizzaCrust> crustList = PizzaCrustDAO.getInstance().findAll();
		List<PizzaPrice> pizzaPriceList = new ArrayList<PizzaPrice>();

		for (int i = 0; i < menuItemSizeList.size(); ++i) {
			for (int j = 0; j < crustList.size(); ++j) {
				PizzaPrice pizzaPrice = new PizzaPrice();
				pizzaPrice.setSize(menuItemSizeList.get(i));
				pizzaPrice.setCrust(crustList.get(j));
				pizzaPrice.setPrice(0.0);

				pizzaPriceList.add(pizzaPrice);
			}
		}
		return pizzaPriceList;
	}
}
