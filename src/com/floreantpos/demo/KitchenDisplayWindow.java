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
package com.floreantpos.demo;

import java.awt.Toolkit;

import javax.swing.JFrame;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;

public class KitchenDisplayWindow extends JFrame {

	KitchenDisplayView view = new KitchenDisplayView(false);

	public KitchenDisplayWindow() {
		setTitle(Messages.getString("KitchenDisplayWindow.0")); //$NON-NLS-1$
		setIconImage(Application.getApplicationIcon().getImage());

		add(view);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);

		view.setVisible(b);
	}

	@Override
	public void dispose() {
		view.cleanup();

		super.dispose();
	}
}
