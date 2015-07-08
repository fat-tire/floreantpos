package com.floreantpos.actions;

import java.io.File;

import com.floreantpos.main.Application;
import com.floreantpos.model.UserPermission;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class DrawerKickAction extends PosAction {

	public DrawerKickAction() {
		super("NO SALE", UserPermission.DRAWER_PULL); //$NON-NLS-1$
	}

	@Override
	public void execute() {
		try {
			File file = new File(Application.getInstance().getLocation(), "drawer-kick.bat"); //$NON-NLS-1$
			if (file.exists()) {
				Runtime.getRuntime().exec(file.getAbsolutePath());
			}
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(),e.getMessage(), e);
		}
	}

}
