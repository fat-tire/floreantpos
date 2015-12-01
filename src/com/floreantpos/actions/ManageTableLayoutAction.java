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

import com.floreantpos.POSConstants;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.model.UserPermission;

public class ManageTableLayoutAction extends PosAction {
	FloorLayoutPlugin floorLayoutPlugin;
	
	public ManageTableLayoutAction() {
		super(POSConstants.TABLE_MANAGE_BUTTON_TEXT, UserPermission.MANAGE_TABLE_LAYOUT); //$NON-NLS-1$
		
		floorLayoutPlugin = (FloorLayoutPlugin) ExtensionManager.getPlugin(FloorLayoutPlugin.class);
		if(floorLayoutPlugin == null) {
			setVisible(false);
		}
	}

	@Override
	public void execute() {
		if(floorLayoutPlugin != null) {
			floorLayoutPlugin.openTicketsAndTablesDisplay();
		}
	}
}
