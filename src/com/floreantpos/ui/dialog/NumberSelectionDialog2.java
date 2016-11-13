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
package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.swing.NumericKeypad;
import com.floreantpos.swing.PosUIManager;

public class NumberSelectionDialog2 extends OkCancelOptionDialog {

	private int defaultValue;
	private JTextField tfNumber;
	private boolean floatingPoint;

	public NumberSelectionDialog2() {
		super(Application.getPosWindow());
		init();
	}

	private void init() {
		JPanel contentPane = getContentPanel();
		contentPane.setLayout(new MigLayout("inset 0", "[grow,fill]", "[grow,fill]"));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		tfNumber = new JTextField();
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, PosUIManager.getNumberFieldFontSize()));
		tfNumber.setFocusable(true);
		tfNumber.requestFocus();
		tfNumber.setBackground(Color.WHITE);
		contentPane.add(tfNumber, "cell 0 0,alignx left,height 40px,aligny top"); //$NON-NLS-1$

		NumericKeypad numericKeypad = new NumericKeypad();
		contentPane.add(numericKeypad, "cell 0 1"); //$NON-NLS-1$
	}

	@Override
	public void doOk() {
		if (!validate(tfNumber.getText())) {
			POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
			return;
		}
		setCanceled(false);
		dispose();
	}

	private boolean validate(String str) {
		if (isFloatingPoint()) {
			try {
				Double.parseDouble(str);
			} catch (Exception x) {
				return false;
			}
		}
		else {
			try {
				Integer.parseInt(str);
			} catch (Exception x) {
				return false;
			}
		}
		return true;
	}

	public void setTitle(String title) {
		super.setTitlePaneText(title);
		super.setTitle(title);
	}

	public void setDialogTitle(String title) {
		super.setTitle(title);
	}

	public double getValue() {
		return Double.parseDouble(tfNumber.getText());
	}

	public void setValue(double value) {
		if (value == 0) {
			tfNumber.setText(""); //$NON-NLS-1$
		}
		else if (isFloatingPoint()) {
			tfNumber.setText(String.valueOf(value));
		}
		else {
			tfNumber.setText(String.valueOf((int) value));
		}
	}

	public boolean isFloatingPoint() {
		return floatingPoint;
	}

	public void setFloatingPoint(boolean decimalAllowed) {
		this.floatingPoint = decimalAllowed;
	}

	public static void main(String[] args) {
		NumberSelectionDialog2 dialog2 = new NumberSelectionDialog2();
		dialog2.pack();
		dialog2.setVisible(true);
	}

	public int getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
		tfNumber.setText(String.valueOf(defaultValue));
	}

	public static int takeIntInput(String title) {
		NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
		dialog.setTitle(title);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return -1;
		}

		return (int) dialog.getValue();
	}

	public static double takeDoubleInput(String title, String dialogTitle, double initialAmount) {
		NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
		dialog.setFloatingPoint(true);
		dialog.setValue(initialAmount);
		dialog.setTitle(title);
		dialog.setDialogTitle(dialogTitle);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return Double.NaN;
		}

		return dialog.getValue();
	}

	public static double takeDoubleInput(String title, double initialAmount) {
		NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
		dialog.setFloatingPoint(true);
		dialog.setTitle(title);
		dialog.setValue(initialAmount);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return -1;
		}

		return dialog.getValue();
	}

	public static double show(Component parent, String title, double initialAmount) {
		NumberSelectionDialog2 dialog2 = new NumberSelectionDialog2();
		dialog2.setFloatingPoint(true);
		dialog2.setTitle(title);
		dialog2.pack();
		dialog2.setLocationRelativeTo(parent);
		dialog2.setValue(initialAmount);
		dialog2.setVisible(true);

		if (dialog2.isCanceled()) {
			return Double.NaN;
		}

		return dialog2.getValue();
	}
}
