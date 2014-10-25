package com.floreantpos.actions;

import javax.swing.JDialog;

import com.floreantpos.main.Application;
import com.floreantpos.model.UserPermission;
import com.floreantpos.ui.dialog.TicketAuthorizationDialog;

public class AuthorizeTicketAction extends PosAction {

	public AuthorizeTicketAction() {
		super("AUTHORIZE", UserPermission.AUTHORIZE_TICKETS);
	}
	
	@Override
	public void execute() {
		TicketAuthorizationDialog dialog = new TicketAuthorizationDialog(Application.getPosWindow());
    	dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    	dialog.setSize(800, 600);
    	dialog.setLocationRelativeTo(Application.getPosWindow());
    	dialog.setVisible(true);
	}

}
