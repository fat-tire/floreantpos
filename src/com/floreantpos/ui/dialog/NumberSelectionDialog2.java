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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.swing.NumericKeypad;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;

public class NumberSelectionDialog2 extends OkCancelOptionDialog implements ActionListener {
	private int defaultValue;

	private JTextField tfNumber;

	private boolean floatingPoint;
	private PosButton posButton_1;
	private boolean clearPreviousNumber = true;

	public NumberSelectionDialog2() {
		super();
		init();
	}

	private void init() {
		//setResizable(false);

		JPanel contentPane = getContentPanel();

		MigLayout layout = new MigLayout("inset 0", "[grow,fill]", "[grow,fill]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		contentPane.setLayout(layout);

		tfNumber = new JTextField();
		tfNumber.setText(String.valueOf(defaultValue));
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, PosUIManager.getNumberFieldFontSize()));
		//tfNumber.setEditable(false);
		tfNumber.setFocusable(true);
		tfNumber.requestFocus();
		tfNumber.setBackground(Color.WHITE);
		//tfNumber.setHorizontalAlignment(JTextField.RIGHT);
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

	private void doClearAll() {
		tfNumber.setText(String.valueOf(defaultValue));
	}

	private void doClear() {
		String s = tfNumber.getText();
		if (s.length() > 1) {
			s = s.substring(0, s.length() - 1);
		}
		else {
			s = String.valueOf(defaultValue);
		}
		tfNumber.setText(s);
	}

	private void doInsertNumber(String number) {

		if (clearPreviousNumber) {
			tfNumber.setText("0"); //$NON-NLS-1$
			clearPreviousNumber = false;
		}

		String s = tfNumber.getText();
		double d = 0;

		try {
			d = Double.parseDouble(s);
		} catch (Exception x) {
		}

		if (d == 0 && !s.contains(".")) {
			tfNumber.setText(number);
			return;
		}

		s = s + number;
		if (!validate(s)) {
			POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
			return;
		}
		tfNumber.setText(s);
	}

	private void doInsertDot() {
		//if (isFloatingPoint() && tfNumber.getText().indexOf('.') < 0) {
		String string = tfNumber.getText() + "."; //$NON-NLS-1$
		if (!validate(string)) {
			POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
			return;
		}
		tfNumber.setText(string);
		//}
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (actionCommand.equals(POSConstants.CLEAR_ALL)) {
			doClearAll();
		}
		else if (actionCommand.equals(POSConstants.CLEAR)) {
			doClear();
		}
		else if (actionCommand.equals(".")) { //$NON-NLS-1$
			doInsertDot();
		}
		else {
			doInsertNumber(actionCommand);
		}
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
			tfNumber.setText("0"); //$NON-NLS-1$
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
