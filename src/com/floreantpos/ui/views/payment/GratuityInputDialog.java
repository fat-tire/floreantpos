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
package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.NumericKeypad;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;

public class GratuityInputDialog extends POSDialog {
	private DoubleTextField doubleTextField;
	public GratuityInputDialog() {
		
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("GratuityInputDialog.0")); //$NON-NLS-1$
		getContentPane().add(titlePanel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[224px,grow,fill]", "[][grow,fill][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		doubleTextField = new DoubleTextField();
		doubleTextField.setHorizontalAlignment(SwingConstants.TRAILING);
		doubleTextField.setFocusCycleRoot(true);
		doubleTextField.setColumns(20);
		panel.add(doubleTextField, "cell 0 0,alignx left,height 40px,aligny top"); //$NON-NLS-1$
		
		NumericKeypad numericKeypad = new NumericKeypad();
		panel.add(numericKeypad, "cell 0 1"); //$NON-NLS-1$
		
		JSeparator separator = new JSeparator();
		panel.add(separator, "cell 0 2,growx,gapy 50px"); //$NON-NLS-1$
		
		PosButton psbtnOk = new PosButton();
		psbtnOk.setFocusable(false);
		psbtnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(false);
				dispose();
			}
		});
		psbtnOk.setText(Messages.getString("GratuityInputDialog.7")); //$NON-NLS-1$
		panel.add(psbtnOk, "flowx,cell 0 3"); //$NON-NLS-1$
		
		PosButton psbtnCancel = new PosButton();
		psbtnCancel.setFocusable(false);
		psbtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		psbtnCancel.setText(Messages.getString("GratuityInputDialog.9")); //$NON-NLS-1$
		panel.add(psbtnCancel, "cell 0 3"); //$NON-NLS-1$
	}

	public double getGratuityAmount() {
		return doubleTextField.getDouble();
	}
}
