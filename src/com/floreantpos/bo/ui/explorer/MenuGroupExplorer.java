package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuGroupForm;

public class MenuGroupExplorer extends TransparentPanel {
	private List<MenuGroup> groupList;

	private JXTable table;
	private GroupExplorerTableModel tableModel;

	public MenuGroupExplorer() {
		MenuGroupDAO dao = new MenuGroupDAO();
		groupList = dao.findAll();

		tableModel = new GroupExplorerTableModel();
		table = new JXTable(tableModel);
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

					index = table.convertRowIndexToModel(index);
					
					MenuGroup category = groupList.get(index);

					MenuGroupForm editor = new MenuGroupForm(category);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
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
					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;
					MenuGroup foodGroup = (MenuGroup) editor.getBean();
					tableModel.addGroup(foodGroup);
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
					
					if (ConfirmDeleteDialog.showMessage(MenuGroupExplorer.this, POSConstants.CONFIRM_DELETE, POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
						MenuGroup category = groupList.get(index);
						MenuGroupDAO foodGroupDAO = new MenuGroupDAO();
						foodGroupDAO.delete(category);
						tableModel.deleteGroup(category, index);
					}
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

	class GroupExplorerTableModel extends AbstractTableModel {
		String[] columnNames = { POSConstants.ID, POSConstants.NAME, POSConstants.TRANSLATED_NAME,
				POSConstants.VISIBLE, POSConstants.MENU_CATEGORY, POSConstants.SORT_ORDER, POSConstants.BUTTON_COLOR };

		public int getRowCount() {
			if (groupList == null) {
				return 0;
			}
			return groupList.size();
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
			if (groupList == null)
				return ""; //$NON-NLS-1$

			MenuGroup group = groupList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return String.valueOf(group.getId());

				case 1:
					return group.getName();
					
				case 2:
					return group.getTranslatedName();

				case 3:
					return Boolean.valueOf(group.isVisible());

				case 4:
					return group.getParent().getDisplayName();
					
				case 5:
					return group.getSortOrder();
					
				case 6:
					if(group.getButtonColor() != null) {
						return new Color(group.getButtonColor());
					}
					
					return null;
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
