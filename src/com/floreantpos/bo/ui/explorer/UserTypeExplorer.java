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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.UserType;
import com.floreantpos.model.dao.UserTypeDAO;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.forms.UserTypeForm;
import com.floreantpos.util.POSUtil;

public class UserTypeExplorer extends TransparentPanel {
	private List<UserType> typeList;

	private JTable table;

	private UserTypeExplorerTableModel tableModel;

	public UserTypeExplorer() {
		UserTypeDAO dao = new UserTypeDAO();
		typeList = dao.findAll();

		tableModel = new UserTypeExplorerTableModel();
		table = new JTable(tableModel);
		table.setRowHeight(PosUIManager.getSize(20));
		table.setDefaultRenderer(Object.class, new PosTableRenderer());

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		JButton addButton = new JButton(com.floreantpos.POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UserTypeForm editor = new UserTypeForm();
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.setPreferredSize(PosUIManager.getSize(500, 450));
					dialog.open();
					if (dialog.isCanceled())
						return;
					UserType type = (UserType) editor.getBean();
					tableModel.addType(type);
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		JButton editButton = new JButton(com.floreantpos.POSConstants.EDIT);
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					UserType type = typeList.get(index);

					UserTypeForm editor = new UserTypeForm();
					editor.setUserType(type);
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.setPreferredSize(PosUIManager.getSize(500, 450));
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		JButton deleteButton = new JButton(com.floreantpos.POSConstants.DELETE);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					if (ConfirmDeleteDialog
							.showMessage(UserTypeExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
						UserType category = typeList.get(index);
						UserTypeDAO dao = new UserTypeDAO();
						dao.delete(category);
						tableModel.deleteCategory(category, index);
					}
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}

	class UserTypeExplorerTableModel extends AbstractTableModel {
		String[] columnNames = { com.floreantpos.POSConstants.TYPE_NAME, com.floreantpos.POSConstants.PERMISSIONS };

		public int getRowCount() {
			if (typeList == null) {
				return 0;
			}
			return typeList.size();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (typeList == null)
				return ""; //$NON-NLS-1$

			UserType userType = typeList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return userType.getName();

				case 1:
					return userType.getPermissions();
			}
			return null;
		}

		public void addType(UserType type) {
			int size = typeList.size();
			typeList.add(type);
			fireTableRowsInserted(size, size);
		}

		public void deleteCategory(UserType type, int index) {
			typeList.remove(type);
			fireTableRowsDeleted(index, index);
		}
	}
}
