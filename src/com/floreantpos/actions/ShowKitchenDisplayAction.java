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
import com.floreantpos.demo.KitchenDisplayView;
import com.floreantpos.ui.views.order.RootView;

public class ShowKitchenDisplayAction extends PosAction {

	public ShowKitchenDisplayAction() {
		super(POSConstants.KITCHEN_DISPLAY_BUTTON_TEXT); //$NON-NLS-1$
	}

	@Override
	public void execute() {
		/*Window[] windows = Window.getWindows();
		for (Window window : windows) {
			if(window instanceof KitchenDisplayWindow) {
				window.setVisible(true);
				window.toFront();
				return;
			}
		}
		
		KitchenDisplayWindow window = new KitchenDisplayWindow();
		window.setVisible(true);*/
		RootView.getInstance().showView(KitchenDisplayView.getInstance());
		RootView.getInstance().getHeaderPanel().setVisible(false); 
	}

}
