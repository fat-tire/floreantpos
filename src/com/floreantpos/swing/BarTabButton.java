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

import java.awt.Color;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PasswordEntryDialog;

public class BarTabButton extends PosButton {
	private User user;
	private Ticket ticket;

	public BarTabButton(Ticket ticket) {
		this.ticket = ticket;
		if (ticket.getId() != null)
			setText(ticket.getId() + "");

		update();
	}

	public int getId() {
		return ticket.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BarTabButton)) {
			return false;
		}

		BarTabButton that = (BarTabButton) obj;

		return this.ticket.equals(that.ticket);
	}

	@Override
	public int hashCode() {
		return this.ticket.hashCode();
	}

	@Override
	public String toString() {
		return ticket.toString();
	}

	public void update() {
		setEnabled(true);
		setBackground(Color.white);
		setForeground(Color.black);
	}

	public void setUser(User user) {
		if (user != null) {
			this.user = user;
		}
	}

	public User getUser() {
		return user;
	}

	public boolean hasUserAccess() {

		if (user == null) {
			return false;
		}
		User currentUser = Application.getCurrentUser();

		int currentUserId = currentUser.getUserId();
		int ticketUserId = user.getUserId();

		if (currentUserId == ticketUserId) {
			return true;
		}

		if (currentUser.hasPermission(UserPermission.PERFORM_MANAGER_TASK) || currentUser.hasPermission(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
			return true;
		}

		String password = PasswordEntryDialog.show(Application.getPosWindow(), Messages.getString("PosAction.0")); //$NON-NLS-1$
		if (StringUtils.isEmpty(password)) {
			return false;
		}

		int inputUserId = UserDAO.getInstance().findUserBySecretKey(password).getAutoId();
		if (inputUserId != user.getAutoId()) {
			POSMessageDialog.showError(Application.getPosWindow(), "Incorrect password"); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Ticket getTicket() {
		return ticket;
	}
}
