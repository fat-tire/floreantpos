package com.floreantpos.actions;

import com.floreantpos.POSConstants;
import com.floreantpos.ui.dialog.PayoutDialog;

public class PayoutAction extends PosAction {

	public PayoutAction() {
		super(POSConstants.PAYOUT_BUTTON_TEXT); //$NON-NLS-1$
	}

	@Override
	public void execute() {
		PayoutDialog dialog = new PayoutDialog();
		dialog.open();
	}

}
