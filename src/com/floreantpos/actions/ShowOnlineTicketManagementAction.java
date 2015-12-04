/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.actions;

import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.TicketImportPlugin;
import com.floreantpos.main.Application;
import com.floreantpos.model.UserPermission;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class ShowOnlineTicketManagementAction extends PosAction {

	private TicketImportPlugin ticketImportPlugin;

	public ShowOnlineTicketManagementAction() {
		super("ONLINE TICKET STAT", UserPermission.DRAWER_PULL); //$NON-NLS-1$
		ticketImportPlugin = (TicketImportPlugin) ExtensionManager.getPlugin(TicketImportPlugin.class);
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
