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
package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.PosException;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.GratuityViewer2;

public class ViewGratuitiesAction extends AbstractAction {

	public ViewGratuitiesAction() {
		super(com.floreantpos.POSConstants.GRATUITY_ADMINISTRATION);
	}

	public ViewGratuitiesAction(String name) {
		super(name);
	}

	public ViewGratuitiesAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		
		try {
			GratuityViewer2 explorer = null;
			JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
			int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.GRATUITY_ADMINISTRATION);
			if (index == -1) {
				explorer = new GratuityViewer2();
				tabbedPane.addTab(com.floreantpos.POSConstants.GRATUITY_ADMINISTRATION, explorer);
			}
			else {
				explorer = (GratuityViewer2) tabbedPane.getComponentAt(index);
			}
			tabbedPane.setSelectedComponent(explorer);
			
		} catch(PosException x) {
			BOMessageDialog.showError(backOfficeWindow, x.getMessage(), x);
		} catch (Exception ex) {
			BOMessageDialog.showError(backOfficeWindow, com.floreantpos.POSConstants.ERROR_MESSAGE, ex);
		}
	}

}
