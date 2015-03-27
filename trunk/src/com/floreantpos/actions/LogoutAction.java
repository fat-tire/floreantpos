package com.floreantpos.actions;

import java.awt.Window;

import javax.swing.Action;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.demo.KitchenDisplayWindow;
import com.floreantpos.main.Application;

public class LogoutAction extends PosAction {

	public LogoutAction() {
		super(Messages.getString("Logout")); //$NON-NLS-1$
	}

	public LogoutAction(boolean showText, boolean showIcon) {
		if (showText) {
			putValue(Action.NAME, Messages.getString("Logout")); //$NON-NLS-1$
		}
		if (showIcon) {
			putValue(Action.SMALL_ICON, IconFactory.getIcon("logout.png")); //$NON-NLS-1$
		}
	}

	@Override
	public void execute() {
		BackOfficeWindow.getInstance().dispose();
		Window[] windows = Window.getWindows();
		for (Window window : windows) {
			if (window instanceof KitchenDisplayWindow) {
				window.dispose();
			}
		}
		Application.getInstance().doLogout();
	}

}
