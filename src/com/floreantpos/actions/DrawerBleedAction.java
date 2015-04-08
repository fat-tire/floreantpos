package com.floreantpos.actions;

import com.floreantpos.POSConstants;
import com.floreantpos.model.UserPermission;
import com.floreantpos.ui.dialog.CashDropDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class DrawerBleedAction extends PosAction {

	public DrawerBleedAction() {
		super(POSConstants.DRAWER_BLEED, UserPermission.DRAWER_PULL); //$NON-NLS-1$
	}

	@Override
	public void execute() {
		try {
			CashDropDialog dialog = new CashDropDialog();
			dialog.initDate();
			dialog.open();
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
		}
	}

}
