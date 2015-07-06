package com.floreantpos.actions;

import javax.swing.JDialog;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.UserPermission;
import com.floreantpos.ui.dialog.DrawerPullReportDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class DrawerPullAction extends PosAction {

	public DrawerPullAction() {
		super(POSConstants.DRAWER_PULL_BUTTON_TEXT, UserPermission.DRAWER_PULL); //$NON-NLS-1$
	}

	@Override
	public void execute() {
		try {
			DrawerPullReportDialog dialog = new DrawerPullReportDialog();
			dialog.setTitle(com.floreantpos.POSConstants.DRAWER_PULL_BUTTON_TEXT);
			dialog.initialize();
			dialog.setSize(470, 500);
			dialog.setResizable(false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.open();
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(),e.getMessage(), e);
		}
	}

}
