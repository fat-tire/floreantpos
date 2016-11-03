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
package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.CustomCellRenderer;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.model.MenuItemForm;
import com.floreantpos.util.CurrencyUtil;
import com.floreantpos.util.POSUtil;

public class MenuItemExplorer extends TransparentPanel {

	private JXTable table;
	private BeanTableModel<MenuItem> tableModel;
	private JComboBox cbGroup;
	private JComboBox cbOrderTypes;
	private JTextField tfName;

	public MenuItemExplorer() {
		tableModel = new BeanTableModel<MenuItem>(MenuItem.class);
		tableModel.addColumn(POSConstants.ID.toUpperCase(), "id"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.NAME.toUpperCase(), "name"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TRANSLATED_NAME.toUpperCase(), "translatedName"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.PRICE.toUpperCase() + " (" + CurrencyUtil.getCurrencySymbol() + ")", "price"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		tableModel.addColumn(Messages.getString("MenuItemExplorer.16"), "stockAmount"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn(POSConstants.VISIBLE.toUpperCase(), "visible"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.FOOD_GROUP.toUpperCase(), "parent"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TAX.toUpperCase(), "tax"); //$NON-NLS-1$
		tableModel.addColumn(Messages.getString("MenuItemExplorer.21"), "sortOrder"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn(Messages.getString("MenuItemExplorer.23"), "buttonColor"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn(Messages.getString("MenuItemExplorer.25"), "textColor"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn(POSConstants.IMAGE.toUpperCase(), "imageData"); //$NON-NLS-1$

		List<MenuItem> findAll = MenuItemDAO.getInstance().getMenuItems();
		tableModel.addRows(findAll);
		table = new JXTable(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setDefaultRenderer(Object.class, new CustomCellRenderer());
		table.setRowHeight(60);

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		add(createButtonPanel(), BorderLayout.SOUTH);
		add(buildSearchForm(), BorderLayout.NORTH);
		resizeColumnWidth(table);
	}

	private JPanel buildSearchForm() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[][]15[][]15[][]15[]", "[]5[]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblOrderType = new JLabel(Messages.getString("MenuItemExplorer.4")); //$NON-NLS-1$
		cbOrderTypes = new JComboBox();

		cbOrderTypes.addItem(Messages.getString("MenuItemExplorer.5")); //$NON-NLS-1$

		List<OrderType> orderTypes = Application.getInstance().getOrderTypes();
		for (OrderType orderType : orderTypes) {
			cbOrderTypes.addItem(orderType);
		}

		JLabel lblName = new JLabel(Messages.getString("MenuItemExplorer.0")); //$NON-NLS-1$
		JLabel lblGroup = new JLabel(Messages.getString("MenuItemExplorer.1")); //$NON-NLS-1$
		tfName = new JTextField(15);

		try {

			List<MenuGroup> menuGroupList = MenuGroupDAO.getInstance().findAll();

			cbGroup = new JComboBox();

			cbGroup.addItem(Messages.getString("MenuItemExplorer.2")); //$NON-NLS-1$
			for (MenuGroup s : menuGroupList) {
				cbGroup.addItem(s);
			}

			JButton searchBttn = new JButton(Messages.getString("MenuItemExplorer.3")); //$NON-NLS-1$

			panel.add(lblName, "align label"); //$NON-NLS-1$
			panel.add(tfName);
			panel.add(lblGroup);
			panel.add(cbGroup);
			panel.add(lblOrderType);
			panel.add(cbOrderTypes);
			panel.add(searchBttn);

			Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder title = BorderFactory.createTitledBorder(loweredetched, "Search"); //$NON-NLS-1$
			title.setTitleJustification(TitledBorder.LEFT);
			panel.setBorder(title);

			searchBttn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					searchItem();
				}
			});

			tfName.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					searchItem();
				}
			});

		} catch (Throwable x) {
			BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
		}

		return panel;
	}

	private void searchItem() {
		Object selectedType = cbOrderTypes.getSelectedItem();
		String txName = tfName.getText();
		Object selectedGroup = cbGroup.getSelectedItem();

		List<MenuItem> similarItem = null;

		if (selectedGroup instanceof MenuGroup) {
			similarItem = MenuItemDAO.getInstance().getMenuItems(txName, (MenuGroup) selectedGroup, selectedType);
		}
		else {
			similarItem = MenuItemDAO.getInstance().getMenuItems(txName, null, selectedType);
		}

		tableModel.removeAll();
		tableModel.addRows(similarItem);
	}

	private TransparentPanel createButtonPanel() {
		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();
		JButton editButton = explorerButton.getEditButton();
		JButton addButton = explorerButton.getAddButton();
		JButton deleteButton = explorerButton.getDeleteButton();

		JButton updateStockAmount = new JButton(Messages.getString("MenuItemExplorer.6")); //$NON-NLS-1$

		updateStockAmount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {

					int index = table.getSelectedRow();

					if (index < 0) {
						POSMessageDialog.showMessage(MenuItemExplorer.this, Messages.getString("MenuItemExplorer.7")); //$NON-NLS-1$
						return;
					}

					MenuItem menuItem = tableModel.getRow(index);

					String amountString = JOptionPane.showInputDialog(MenuItemExplorer.this,
							Messages.getString("MenuItemExplorer.8"), menuItem.getStockAmount()); //$NON-NLS-1$

					if (amountString == null || amountString.equals("")) { //$NON-NLS-1$
						return;
					}

					double stockAmount = Double.parseDouble(amountString);

					if (stockAmount < 0) {
						POSMessageDialog.showError(MenuItemExplorer.this, Messages.getString("MenuItemExplorer.10")); //$NON-NLS-1$
						return;
					}

					menuItem.setStockAmount(stockAmount);
					MenuItemDAO.getInstance().saveOrUpdate(menuItem);
					table.repaint();

				} catch (NumberFormatException e1) {
					POSMessageDialog.showError(MenuItemExplorer.this, Messages.getString("MenuItemExplorer.11")); //$NON-NLS-1$
					return;
				} catch (Exception e2) {
					BOMessageDialog.showError(MenuItemExplorer.this, POSConstants.ERROR_MESSAGE, e2);
					return;
				}
			}
		});

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					index = table.convertRowIndexToModel(index);

					MenuItem menuItem = tableModel.getRow(index);
					menuItem = MenuItemDAO.getInstance().initialize(menuItem);

					tableModel.setRow(index, menuItem);

					MenuItemForm editor = new MenuItemForm(menuItem);
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MenuItem menuItem = new MenuItem();

					Object group = cbGroup.getSelectedItem();

					if (group instanceof MenuGroup) {
						menuItem.setParent((MenuGroup) group);
					}

					Object selectedType = cbOrderTypes.getSelectedItem();

					if (selectedType instanceof OrderType) {

						List types = new ArrayList();

						types.add(((OrderType) selectedType).getName());

						menuItem.setOrderTypes(types);
					}
					MenuItemForm editor = new MenuItemForm(menuItem);

					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();

					if (dialog.isCanceled())
						return;

					MenuItem foodItem = (MenuItem) editor.getBean();

					tableModel.addRow(foodItem);
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					index = table.convertRowIndexToModel(index);

					if (POSMessageDialog.showYesNoQuestionDialog(MenuItemExplorer.this, POSConstants.CONFIRM_DELETE, POSConstants.DELETE) != JOptionPane.YES_OPTION) {
						return;
					}
					MenuItem item = tableModel.getRow(index);

					MenuItemDAO foodItemDAO = new MenuItemDAO();
					if (item.getDiscounts().size() > 0) {
						foodItemDAO.releaseParentAndDelete(item);
					}
					else {
						foodItemDAO.delete(item);
					}

					tableModel.removeRow(index);
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(updateStockAmount);
		panel.add(deleteButton);
		return panel;
	}

	public void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			columnModel.getColumn(column).setPreferredWidth((Integer) getColumnWidth().get(column));
		}
	}

	private List getColumnWidth() {
		List<Integer> columnWidth = new ArrayList();
		columnWidth.add(50);
		columnWidth.add(200);
		columnWidth.add(200);
		columnWidth.add(70);
		columnWidth.add(50);
		columnWidth.add(50);
		columnWidth.add(140);
		columnWidth.add(70);
		columnWidth.add(70);
		columnWidth.add(100);
		columnWidth.add(100);
		columnWidth.add(200);

		return columnWidth;
	}
}
