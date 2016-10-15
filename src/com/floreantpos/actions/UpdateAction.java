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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.floreantpos.main.Application;
import com.floreantpos.services.PosWebService;
import com.floreantpos.ui.dialog.UpdateDialog;
import com.oro.licensor.TerminalUtil;

public class UpdateAction extends AbstractAction {

	public UpdateAction() {
		super("Update"); //$NON-NLS-1$
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		openUpdateDialog();
	}

	private void openUpdateDialog() {
		PosWebService service = new PosWebService();
		try {
			boolean up_to_date = false;
			String versionInfo = service.getAvailableNewVersions(TerminalUtil.getSystemUID(), Application.VERSION.substring(0, 3));
			String[] availableNewVersions = null;
			if (versionInfo != null) {
				if (versionInfo.equals("UP_TO_DATE")) {
					up_to_date = true;
				}
				else if (versionInfo.startsWith("[")) {
					versionInfo = versionInfo.replace("[", "").replace(",]", "");
					availableNewVersions = versionInfo.split(",");
				}
			}
			UpdateDialog dialog = new UpdateDialog(availableNewVersions, up_to_date, true);
			dialog.setTitle("Update");
			dialog.pack();
			dialog.open();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
