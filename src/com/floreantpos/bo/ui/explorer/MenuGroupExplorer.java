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
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.CustomCellRenderer;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ComboItemSelectionDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.model.MenuCategoryForm;
import com.floreantpos.ui.model.MenuGroupForm;
import com.floreantpos.util.POSUtil;

public class MenuGroupExplorer extends TransparentPanel {

	private JXTable table;
	private BeanTableModel<MenuGroup> tableModel;

	public MenuGroupExplorer() {
		tableModel = new BeanTableModel<MenuGroup>(MenuGroup.class);
		tableModel.addColumn(POSConstants.ID.toUpperCase(), "id"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.NAME.toUpperCase(), "name"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TRANSLATED_NAME.toUpperCase(), "translatedName"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.VISIBLE.toUpperCase(), "visible"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.MENU_CATEGORY.toUpperCase(), "parent"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.SORT_ORDER.toUpperCase(), "sortOrder"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.BUTTON_COLOR.toUpperCase(), "buttonColor"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TEXT_COLOR.toUpperCase(), "textColor"); //$NON-NLS-1$

		tableModel.addRows(MenuGroupDAO.getInstance().findAll());

		table = new JXTable(tableModel);
		table.setDefaultRenderer(Object.class, new CustomCellRenderer());
		table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if (value instanceof Color) {
					JLabel lblColor = new JLabel(Messages.getString("MenuGroupExplorer.1"), JLabel.CENTER); //$NON-NLS-1$
					lblColor.setForeground((Color) value);
					return lblColor;
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		createButtonPanel();
	}

	private void createButtonPanel() {
		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();
		JButton editButton = explorerButton.getEditButton();
		JButton addButton = explorerButton.getAddButton();
		JButton deleteButton = explorerButton.getDeleteButton();

		JButton btnChangeCategory = new JButton("Change Menu Category");

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					index = table.convertRowIndexToModel(index);

					MenuGroup menuGroup = tableModel.getRow(index);

					MenuGroupForm editor = new MenuGroupForm(menuGroup);
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();
					if (dialog.isCanceled())
						return;
					table.repaint();
				} catch (Exception x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MenuGroupForm editor = new MenuGroupForm();
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();
					if (dialog.isCanceled())
						return;
					MenuGroup foodGroup = (MenuGroup) editor.getBean();
					tableModel.addRow(foodGroup);
				} catch (Exception x) {
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
					MenuGroup group = tableModel.getRow(index);

					if (POSMessageDialog.showYesNoQuestionDialog(MenuGroupExplorer.this, POSConstants.CONFIRM_DELETE, POSConstants.DELETE) != JOptionPane.YES_OPTION) {
						return;
					}

					MenuItemDAO menuItemDao = new MenuItemDAO();
					List<MenuItem> menuItems = menuItemDao.findByParent(null, group, true);

					if (menuItems.size() > 0) {
						if (POSMessageDialog.showYesNoQuestionDialog(MenuGroupExplorer.this, Messages.getString("MenuGroupExplorer.0"), POSConstants.DELETE) != JOptionPane.YES_OPTION) { //$NON-NLS-1$
							return;
						}
						menuItemDao.releaseParent(menuItems);
					}
					MenuGroupDAO foodGroupDAO = new MenuGroupDAO();
					foodGroupDAO.delete(group);

					tableModel.removeRow(index);
				} catch (Exception x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		btnChangeCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int[] rows = table.getSelectedRows();
					if (rows.length < 1)
						return;

					MenuCategory category = getSelectedMenuCategory(null);
					if (category == null)
						return;

					List<MenuGroup> menuGroups = new ArrayList<>();
					for (int i = 0; i < rows.length; i++) {
						int index = table.convertRowIndexToModel(rows[i]);
						MenuGroup menuGroup = tableModel.getRow(index);
						menuGroup.setParent(category);
						menuGroups.add(menuGroup);
					}
					MenuGroupDAO.getInstance().saveAll(menuGroups);
					tableModel.fireTableDataChanged();
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}
		});

		TransparentPanel panel = new TransparentPanel();

		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(btnChangeCategory);
		add(panel, BorderLayout.SOUTH);
	}

	protected MenuCategory getSelectedMenuCategory(MenuCategory defaultValue) {
		List<MenuCategory> menuCategorys = MenuCategoryDAO.getInstance().findAll();
		ComboItemSelectionDialog dialog = new ComboItemSelectionDialog("SELECT GROUP", "Menu Category", menuCategorys, false);
		dialog.setSelectedItem(defaultValue);
		dialog.setVisibleNewButton(true);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled())
			return null;

		if (dialog.isNewItem()) {
			MenuCategory foodCategory = new MenuCategory();
			MenuCategoryForm editor;
			try {
				editor = new MenuCategoryForm(foodCategory);
				BeanEditorDialog editorDialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
				editorDialog.open();
				if (editorDialog.isCanceled())
					return null;
			} catch (Exception e) {
			}
			return getSelectedMenuCategory(foodCategory);
		}
		return (MenuCategory) dialog.getSelectedItem();
	}
}
