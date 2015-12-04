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
package com.floreantpos.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PasswordEntryDialog;

public abstract class PosAction extends AbstractAction {
	private boolean visible = true;
	protected UserPermission requiredPermission;
	
	public PosAction() {
		
	}

	public PosAction(String name) {
		super(name);
	}

	public PosAction(Icon icon) {
		super(null, icon);
	}

	public PosAction(String name, Icon icon) {
		super(name, icon);
	}

	public PosAction(String name, UserPermission requiredPermission) {
		super(name);

		this.requiredPermission = requiredPermission;
	}
	
	public PosAction(Icon icon, UserPermission requiredPermission) {
		super(null, icon);
		
		this.requiredPermission = requiredPermission;
	}

	public UserPermission getRequiredPermission() {
		return requiredPermission;
	}

	public void setRequiredPermission(UserPermission requiredPermission) {
		this.requiredPermission = requiredPermission;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		User user = Application.getCurrentUser();

		if (requiredPermission == null) {
			execute();
			return;
		}

		if (!user.hasPermission(requiredPermission)) {
			String password = PasswordEntryDialog.show(Application.getPosWindow(), Messages.getString("PosAction.0")); //$NON-NLS-1$
			if(StringUtils.isEmpty(password)) {
				return;
			}
			
			User user2 = UserDAO.getInstance().findUserBySecretKey(password);
			if(user2 == null) {
				POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("PosAction.1")); //$NON-NLS-1$
			}
			else {
				if(!user2.hasPermission(requiredPermission)) {
					POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("PosAction.2")); //$NON-NLS-1$
				}
				else {
					execute();
				}
			}
			
			//POSMessageDialog.showError(Application.getPosWindow(), "You do not have permission to execute this action");
			return;
		}

		execute();
	}

	public abstract void execute();

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	//	public boolean isAllowAdministrator() {
	//		return allowAdministrator;
	//	}
	//
	//	public void setAllowAdministrator(boolean allowAdministrator) {
	//		this.allowAdministrator = allowAdministrator;
	//	}
}
