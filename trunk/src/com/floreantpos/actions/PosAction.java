package com.floreantpos.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.floreantpos.main.Application;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.ui.dialog.POSMessageDialog;

public abstract class PosAction extends AbstractAction {
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

		//		if(allowAdministrator && user.isAdministrator()) {
		//			execute();
		//		}

		if (requiredPermission == null) {
			execute();
			return;
		}

		if (!user.hasPermission(requiredPermission)) {
			POSMessageDialog.showError("You do not have permission to execute this action");
			return;
		}

		execute();
	}

	public abstract void execute();

	//	public boolean isAllowAdministrator() {
	//		return allowAdministrator;
	//	}
	//
	//	public void setAllowAdministrator(boolean allowAdministrator) {
	//		this.allowAdministrator = allowAdministrator;
	//	}
}
