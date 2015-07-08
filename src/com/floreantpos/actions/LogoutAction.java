package com.floreantpos.actions;

import java.awt.Window;

import javax.swing.Action;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.main.PosWindow;

public class LogoutAction extends PosAction {

	public LogoutAction() {
		super(Messages.getString("Logout")); //$NON-NLS-1$
	}

	public LogoutAction(boolean showText, boolean showIcon) {
		if (showText) {
			putValue(Action.NAME, Messages.getString("Logout")); //$NON-NLS-1$
		}
		if (showIcon) {
			putValue(Action.SMALL_ICON, IconFactory.getIcon("/ui_icons/", "logout.png")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public void execute() {
		Window[] windows = Window.getWindows();
		for (Window window : windows) {
			if (!(window instanceof PosWindow)) {
				window.setVisible(false);
				window.dispose();
			}
		}
		Application.getInstance().doLogout();
	}

}
