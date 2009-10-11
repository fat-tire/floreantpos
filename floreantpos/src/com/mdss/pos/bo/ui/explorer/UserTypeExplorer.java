package com.mdss.pos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.mdss.pos.main.Application;
import com.mdss.pos.model.UserType;
import com.mdss.pos.model.dao.UserTypeDAO;
import com.mdss.pos.swing.MessageDialog;
import com.mdss.pos.swing.TransparentPanel;
import com.mdss.pos.ui.PosTableRenderer;
import com.mdss.pos.ui.dialog.BeanEditorDialog;
import com.mdss.pos.ui.dialog.ConfirmDeleteDialog;
import com.mdss.pos.ui.forms.UserTypeForm;

public class UserTypeExplorer extends TransparentPanel {
	private List<UserType> typeList;
	
	private JTable table;

	private UserTypeExplorerTableModel tableModel;
	
	public UserTypeExplorer() {
		UserTypeDAO dao = new UserTypeDAO();
		typeList = dao.findAll();
		
		tableModel = new UserTypeExplorerTableModel();
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		
		setLayout(new BorderLayout(5,5));
		add(new JScrollPane(table));
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UserTypeForm editor = new UserTypeForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;
					UserType type = (UserType) editor.getBean();
					tableModel.addType(type);
				} catch (Exception x) {
					MessageDialog.showError("An error has occured, please restart the application", x);
				}
			}
			
		});
		
		JButton editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					UserType type = typeList.get(index);

					UserTypeForm editor = new UserTypeForm();
					editor.setUserType(type);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
					MessageDialog.showError("An error has occured, please restart the application", x);
				}
			}
			
		});
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					if (ConfirmDeleteDialog.showMessage(UserTypeExplorer.this, "Sure Want to Delete?", "Delete") == ConfirmDeleteDialog.YES) {
						UserType category = typeList.get(index);
						UserTypeDAO dao = new UserTypeDAO();
						dao.delete(category);
						tableModel.deleteCategory(category, index);
					}
				} catch (Exception x) {
					MessageDialog.showError("An error has occured, please restart the application", x);
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
		String[] columnNames = {"Type Name", "Permissions"};
		
		public int getRowCount() {
			if(typeList == null) {
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
			if(typeList == null)
				return "";
			
			UserType userType = typeList.get(rowIndex);
			
			switch(columnIndex) {
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
