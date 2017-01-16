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
import com.floreantpos.ui.dialog.OkCancelOptionDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class UserListDialog extends OkCancelOptionDialog {
	BeanTableModel<User> tableModel;
	JTable userListTable;
	private User selectedUser;

	public UserListDialog() {
		super(Application.getPosWindow(), true);
		setTitle(Messages.getString("UserListDialog.0")); //$NON-NLS-1$
		setTitlePaneText(Messages.getString("UserListDialog.0")); //$NON-NLS-1$

		JPanel contentPane = getContentPanel();
		contentPane.setLayout(new BorderLayout(5, 5));
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		tableModel = new BeanTableModel<User>(User.class);
		tableModel.addColumn("Name", "fullName"); //$NON-NLS-1$ //$NON-NLS-2$

		userListTable = new JTable(tableModel);
		userListTable.setRowHeight(PosUIManager.getSize(60));
		userListTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.add(new JScrollPane(userListTable));

		List<User> userList = UserDAO.getInstance().findAll();
		tableModel.addRows(userList);
		if (userList != null && !userList.isEmpty()) {
			userListTable.getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	@Override
	public void doOk() {
		User user = tableModel.getRows().get(userListTable.getSelectedRow());
		if (user == null) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("UserListDialog.4")); //$NON-NLS-1$
			return;
		}
		if (!user.isClockedIn()) {
			POSMessageDialog.showError("Can't assign drawer. Selected user is not clocked in.");
			return;
//			User loginUser = Application.getCurrentUser();
//			String userString = user.getFullName() + " is";
//			if (loginUser.getUserId().intValue() == user.getUserId().intValue()) {
//				userString = "You are";
//			}
//			int option = POSMessageDialog.showYesNoQuestionDialog(Application.getPosWindow(), userString
//					+ " not clocked in. User needs to be clocked in for drawer assignment.", Messages.getString("Application.44"), "CLOCK IN", "CANCEL"); //$NON-NLS-1$ //$NON-NLS-2$
//			if (option == JOptionPane.YES_OPTION) {
//				User inputUser = PasswordEntryDialog.getUser(Application.getPosWindow(), Messages.getString("LoginView.1"), Messages.getString("LoginView.2")); //$NON-NLS-1$ //$NON-NLS-2$
//				if (inputUser == null)
//					return;
//
//				if (user.getUserId().intValue() != inputUser.getUserId().intValue()) {
//					POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Wrong password.");
//					return;
//				}
//				Shift currentShift = ShiftUtil.getCurrentShift();
//				Calendar currentTime = Calendar.getInstance();
//				user.doClockIn(Application.getInstance().getTerminal(), currentShift, currentTime);
//			}
//			else
//				canceled = true;

		}
		selectedUser = user;
		setCanceled(false);
		dispose();
	}
}
