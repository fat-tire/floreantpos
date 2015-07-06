package com.floreantpos.actions;

import com.floreantpos.extension.TicketImportPlugin;
import com.floreantpos.main.Application;
import com.floreantpos.model.UserPermission;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class ShowOnlineTicketManagementAction extends PosAction {

	private TicketImportPlugin ticketImportPlugin;

	public ShowOnlineTicketManagementAction() {
		super("ONLINE TICKET STAT", UserPermission.DRAWER_PULL); //$NON-NLS-1$
		ticketImportPlugin = Application.getPluginManager().getPlugin(TicketImportPlugin.class);
		setVisible(ticketImportPlugin != null);
	}

	@Override
	public void execute() {
		try {
			if (ticketImportPlugin != null) {
				ticketImportPlugin.startService();
			}
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

}
