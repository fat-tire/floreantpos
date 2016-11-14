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
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.model.MenuCategoryForm;
import com.floreantpos.util.POSUtil;

public class MenuCategoryExplorer extends TransparentPanel {

	private JXTable table;
	private BeanTableModel<MenuCategory> tableModel;

	public MenuCategoryExplorer() {
		tableModel = new BeanTableModel<MenuCategory>(MenuCategory.class);
		tableModel.addColumn(POSConstants.ID.toUpperCase(), "id"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.NAME.toUpperCase(), "name"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TRANSLATED_NAME.toUpperCase(), "translatedName"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.BEVERAGE.toUpperCase(), "beverage"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.VISIBLE.toUpperCase(), "visible"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.SORT_ORDER.toUpperCase(), "sortOrder"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.BUTTON_COLOR.toUpperCase(), "buttonColor"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TEXT_COLOR.toUpperCase(), "textColor"); //$NON-NLS-1$

		tableModel.addRows(MenuCategoryDAO.getInstance().findAll());

		table = new JXTable(tableModel);
		table.setDefaultRenderer(Object.class, new CustomCellRenderer());
		table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if (value instanceof Color) {
					JLabel lblColor = new JLabel(Messages.getString("MenuCategoryExplorer.1"), JLabel.CENTER); //$NON-NLS-1$
					lblColor.setForeground((Color) value);
					return lblColor;
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		addButtonPanel();
	}

	private void addButtonPanel() {
		JButton addButton = new JButton(POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					MenuCategoryForm editor = new MenuCategoryForm();
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();

					if (dialog.isCanceled())
						return;

					MenuCategory foodCategory = (MenuCategory) editor.getBean();
					tableModel.addRow(foodCategory);

				} catch (Exception x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		JButton editButton = new JButton(POSConstants.EDIT);
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					index = table.convertRowIndexToModel(index);
					MenuCategory category = tableModel.getRow(index);

					MenuCategoryForm editor = new MenuCategoryForm(category);
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
		JButton deleteButton = new JButton(POSConstants.DELETE);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					index = table.convertRowIndexToModel(index);
					MenuCategory category = tableModel.getRow(index);

					if (POSMessageDialog.showYesNoQuestionDialog(MenuCategoryExplorer.this, POSConstants.CONFIRM_DELETE, POSConstants.DELETE) != JOptionPane.YES_OPTION) {
						return;
					}

					MenuGroupDAO menuGroupDao = new MenuGroupDAO();
					List<MenuGroup> menuGroups = menuGroupDao.findByParent(category);

					if (menuGroups.size() > 0) {
						if (POSMessageDialog.showYesNoQuestionDialog(MenuCategoryExplorer.this,
								Messages.getString("MenuCategoryExplorer.0"), POSConstants.DELETE) != JOptionPane.YES_OPTION) { //$NON-NLS-1$
							return;
						}
						menuGroupDao.releaseParent(menuGroups);
					}

					MenuCategoryDAO dao = new MenuCategoryDAO();
					dao.delete(category);

					tableModel.removeRow(index);
				} catch (Exception x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}
}
