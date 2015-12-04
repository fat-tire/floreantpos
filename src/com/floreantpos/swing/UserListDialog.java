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
