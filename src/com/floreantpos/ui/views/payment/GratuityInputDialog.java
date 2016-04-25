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

import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.NumericKeypad;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.ui.dialog.OkCancelOptionDialog;
import com.floreantpos.util.POSUtil;

public class GratuityInputDialog extends OkCancelOptionDialog {
	private DoubleTextField doubleTextField;

	public GratuityInputDialog() {
		super(POSUtil.getFocusedWindow());
		setTitlePaneText(Messages.getString("GratuityInputDialog.0")); //$NON-NLS-1$

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("inset 0", "[grow,fill]", "[grow,fill]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		doubleTextField = new DoubleTextField();
		doubleTextField.setHorizontalAlignment(SwingConstants.TRAILING);
		doubleTextField.setFocusCycleRoot(true);
		doubleTextField.setFont(doubleTextField.getFont().deriveFont(Font.BOLD, PosUIManager.getNumberFieldFontSize()));
		//doubleTextField.setColumns(20);
		panel.add(doubleTextField, "cell 0 0,alignx left,height 40px,aligny top"); //$NON-NLS-1$

		NumericKeypad numericKeypad = new NumericKeypad();
		panel.add(numericKeypad, "cell 0 1"); //$NON-NLS-1$

		getContentPanel().add(panel);
	}

	public void doOk() {
		setCanceled(false);
		dispose();
	}

	public double getGratuityAmount() {
		return doubleTextField.getDouble();
	}
}
