package com.mdss.pos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.hibernate.exception.ConstraintViolationException;

import com.mdss.pos.main.Application;
import com.mdss.pos.model.User;
import com.mdss.pos.model.dao.UserDAO;
import com.mdss.pos.swing.MessageDialog;
import com.mdss.pos.swing.TransparentPanel;
import com.mdss.pos.ui.PosTableRenderer;
import com.mdss.pos.ui.dialog.BeanEditorDialog;
import com.mdss.pos.ui.dialog.ConfirmDeleteDialog;
import com.mdss.pos.ui.forms.UserForm;

public class UserExplorer extends TransparentPanel {
	
	private JTable table;
	private UserTableModel tableModel;
	
	public UserExplorer() {
		List<User> users = UserDAO.getInstance().findAll();
		
		tableModel = new UserTableModel(users);
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		
		setLayout(new BorderLayout(5,5));
		add(new JScrollPane(table));
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Integer userWithMaxId = UserDAO.getInstance().findUserWithMaxId();
					
					UserForm editor = new UserForm();
					if(userWithMaxId != null) {
						editor.setId(new Integer(userWithMaxId.intValue() + 1));
					}
					BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;
					User user = (User) editor.getBean();
					tableModel.addItem(user);
				} catch (Exception x) {
					x.printStackTrace();
					MessageDialog.showError("An error has occured, please restart the application", x);
				}
			}
			
		});
		JButton copyButton = new JButton("Copy");
		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					User user = (User) tableModel.getRowData(index);
					
					User user2 = new User();
					user2.setUserId(user.getUserId());
					user2.setNewUserType(user.getNewUserType());
					user2.setFirstName(user.getFirstName());
					user2.setLastName(user.getLastName());
					user2.setPassword(user.getPassword());
					user2.setSsn(user.getSsn());
					
					UserForm editor = new UserForm();
					editor.setEditMode(false);
					editor.setBean(user2);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;

					User newUser = (User) editor.getBean();
					tableModel.addItem(newUser);
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

					User user = (User) tableModel.getRowData(index);
					UserForm editor = new UserForm();
					editor.setEditMode(true);
					editor.setBean(user);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;

					tableModel.updateItem(index);
				} catch (Throwable x) {
					MessageDialog.showError("An error has occured, please restart the application", x);
				}
			}
			
		});
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if (index < 0)
					return;
				
				User user = (User) tableModel.getRowData(index);
				if(user == null) {
					return;
				}
				
				try {
					if (ConfirmDeleteDialog.showMessage(UserExplorer.this, "Sure Want to Delete?", "Delete") == ConfirmDeleteDialog.YES) {
						UserDAO.getInstance().delete(user);
						tableModel.deleteItem(index);
					}
				} catch(ConstraintViolationException x) {
					String message = "User " + user.getFirstName() + " " + user.getLastName() + " (" + user.getNewUserType() + ") cannot be deleted because it is being used.";
					MessageDialog.showError(message, x);
				} catch (Exception x) {
					MessageDialog.showError("An error has occured, you may need restart the application", x);
				}
			}
			
		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(copyButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}
	
	class UserTableModel extends ListTableModel {
		
		UserTableModel(List list){
			super(new String[] {"ID", "First Name", "Last Name", "Type"}, list);
		}
		

		public Object getValueAt(int rowIndex, int columnIndex) {
			User user = (User) rows.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return String.valueOf(user.getUserId());
					
				case 1:
					return user.getFirstName();
					
				case 2:
					return user.getLastName();
					
				case 3:
					return user.getNewUserType();
			}
			return null;
		}
	}
}
