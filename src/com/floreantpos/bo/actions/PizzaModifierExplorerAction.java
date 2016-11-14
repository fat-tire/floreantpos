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

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.PizzaModifierExplorer;

public class PizzaModifierExplorerAction extends AbstractAction {

	public PizzaModifierExplorerAction() {
		super(Messages.getString("PizzaModifierExplorerAction.0")); //$NON-NLS-1$
	}

	public PizzaModifierExplorerAction(String name) {
		super(name);
	}

	public PizzaModifierExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane;
		PizzaModifierExplorer modifier;
		tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(Messages.getString("PizzaModifierExplorerAction.1")); //$NON-NLS-1$
		if (index == -1) {
			modifier = new PizzaModifierExplorer();
			tabbedPane.addTab(Messages.getString("PizzaModifierExplorerAction.1"), modifier); //$NON-NLS-1$
		}
		else {
			modifier = (PizzaModifierExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(modifier);
	}

}
