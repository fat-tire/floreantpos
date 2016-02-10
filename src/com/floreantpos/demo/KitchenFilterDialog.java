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
/*
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.demo;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;

/**
 * 
 * @author MShahriar
 */
public class KitchenFilterDialog extends POSDialog {

	public KitchenFilterDialog() {
		initializeComponent();
		setResizable(true);
	}

	private void initializeComponent() {
		setTitle("Select Printer and Order Type"); //$NON-NLS-1$
		setLayout(new BorderLayout());

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Select Printer and Order Type");//$NON-NLS-1$
		add(titlePanel, BorderLayout.NORTH);

		JPanel buttonActionPanel = new JPanel(new MigLayout("fill")); //$NON-NLS-1$

		PosButton btnCancel = new PosButton(POSConstants.CANCEL.toUpperCase());
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setCanceled(true);
				dispose();
			}
		});

		buttonActionPanel.add(btnCancel, "grow, span"); //$NON-NLS-1$

		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		footerPanel.add(new JSeparator(), BorderLayout.NORTH);
		footerPanel.add(buttonActionPanel);

		add(footerPanel, BorderLayout.SOUTH);

		setSize(550, 450);
	}
}
