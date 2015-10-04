package com.floreantpos.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class UserListDialog extends POSDialog {
	BeanTableModel<User> tableModel;
	JTable userListTable;
	
	public UserListDialog() {
		super(Application.getPosWindow(), true);
		setTitle(Messages.getString("UserListDialog.0")); //$NON-NLS-1$
		
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout(5, 5));
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		tableModel = new BeanTableModel<User>(User.class);
		tableModel.addColumn("Name", "fullName"); //$NON-NLS-1$ //$NON-NLS-2$
		
		userListTable = new JTable(tableModel);
		userListTable.setRowHeight(60);
		userListTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.add(new JScrollPane(userListTable));
		
		PosButton btnSelct = new PosButton(Messages.getString("UserListDialog.3")); //$NON-NLS-1$
		btnSelct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = userListTable.getSelectedRow();
				if(selectedRow == -1) {
					POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("UserListDialog.4")); //$NON-NLS-1$
					return;
				}
				
				setCanceled(false);
				dispose();
			}
		});
		
		PosButton btnCancel = new PosButton(Messages.getString("UserListDialog.5")); //$NON-NLS-1$
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(btnSelct);
		buttonPanel.add(btnCancel);
		
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		List<User> users = UserDAO.getInstance().findAllActive();
		tableModel.addRows(users);
	}
	
	public User getSelectedUser() {
		return tableModel.getRows().get(userListTable.getSelectedRow());
	}
}
