package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuGroupForm;

public class GroupExplorer extends TransparentPanel {
	private List<MenuGroup> groupList;

	private JTable table;
	private GroupExplorerTableModel tableModel;

	public GroupExplorer() {
		MenuGroupDAO dao = new MenuGroupDAO();
		groupList = dao.findAll();

		tableModel = new GroupExplorerTableModel();
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();
		JButton editButton = explorerButton.getEditButton();
		JButton addButton = explorerButton.getAddButton();
		JButton deleteButton = explorerButton.getDeleteButton();

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					MenuGroup category = groupList.get(index);

					MenuGroupForm editor = new MenuGroupForm(category);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;
					table.repaint();
				} catch (Exception x) {
					MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MenuGroupForm editor = new MenuGroupForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;
					MenuGroup foodGroup = (MenuGroup) editor.getBean();
					tableModel.addGroup(foodGroup);
				} catch (Exception x) {
					MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;
					if (ConfirmDeleteDialog.showMessage(GroupExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
						MenuGroup category = groupList.get(index);
						MenuGroupDAO foodGroupDAO = new MenuGroupDAO();
						foodGroupDAO.delete(category);
						tableModel.deleteGroup(category, index);
					}
				} catch (Exception x) {
					MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();

		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}

	class GroupExplorerTableModel extends AbstractTableModel {
		String[] columnNames = { com.floreantpos.POSConstants.ID, com.floreantpos.POSConstants.NAME, com.floreantpos.POSConstants.VISIBLE, com.floreantpos.POSConstants.MENU_CATEGORY };

		public int getRowCount() {
			if (groupList == null) {
				return 0;
			}
			return groupList.size();
		}

		public int getColumnCount() {
			return 4;
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
			if (groupList == null)
				return "";

			MenuGroup category = groupList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return String.valueOf(category.getId());

				case 1:
					return category.getName();

				case 2:
					return Boolean.valueOf(category.isVisible());

				case 3:
					return category.getParent().getName();
			}
			return null;
		}

		public void addGroup(MenuGroup category) {
			int size = groupList.size();
			groupList.add(category);
			fireTableRowsInserted(size, size);

		}

		public void deleteGroup(MenuGroup category, int index) {
			groupList.remove(category);
			fireTableRowsDeleted(index, index);
		}
	}
}
