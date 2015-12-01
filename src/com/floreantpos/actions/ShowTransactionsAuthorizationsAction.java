package com.floreantpos.actions;

import javax.swing.JDialog;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.UserPermission;
import com.floreantpos.ui.views.payment.AuthorizableTicketBrowser;

public class ShowTransactionsAuthorizationsAction extends PosAction {

	public ShowTransactionsAuthorizationsAction() {
		super(POSConstants.AUTHORIZE_BUTTON_TEXT, UserPermission.AUTHORIZE_TICKETS); //$NON-NLS-1$
	}

	@Override
	public void execute() {
		AuthorizableTicketBrowser dialog = new AuthorizableTicketBrowser(Application.getPosWindow());
    	dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    	dialog.setSize(800, 600);
    	dialog.setLocationRelativeTo(Application.getPosWindow());
    	dialog.setVisible(true);
	}

}
