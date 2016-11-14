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

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.explorer.CurrencyDialog;

public class CurrencyExplorerAction extends AbstractAction {

	public CurrencyExplorerAction() {
		super(Messages.getString("CurrencyExplorerAction.0")); //$NON-NLS-1$
	}

	public CurrencyExplorerAction(String name) {
		super(name);
	}

	public CurrencyExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		//CurrencyExplorer explorer = new CurrencyExplorer();

		//explorer.setBorder(new EmptyBorder(20, 10, 20, 10));
		CurrencyDialog dialog = new CurrencyDialog();
		dialog.setTitle(Messages.getString("CurrencyExplorerAction.1")); //$NON-NLS-1$
		//dialog.add(explorer);
		dialog.setSize(800, 600);
		dialog.open();
	}

}
