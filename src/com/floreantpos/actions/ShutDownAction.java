package com.floreantpos.actions;

import javax.swing.Action;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.UserPermission;

public class ShutDownAction extends PosAction {

	public ShutDownAction() {
		super(POSConstants.CAPITAL_SHUTDOWN, UserPermission.SHUT_DOWN);
	}
	
	public ShutDownAction(boolean showText, boolean showIcon) {
		if (showText) {
			putValue(Action.NAME, Messages.getString("Shutdown")); //$NON-NLS-1$
		}
		if (showIcon) {
			putValue(Action.SMALL_ICON, IconFactory.getIcon("shut_down.png")); //$NON-NLS-1$
		}
		
		setRequiredPermission(UserPermission.SHUT_DOWN);
	}
	
	@Override
	public void execute() {
		Application.getInstance().shutdownPOS();
	}

}
