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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;

public class BasicWeightInputDialog extends OkCancelOptionDialog implements ActionListener {
	private int defaultValue;

	private JTextField tfNumber;

	private boolean floatingPoint;
	private PosButton btnCancel;
	private boolean clearPreviousNumber = true;

	public BasicWeightInputDialog() {
		super();
		init();
	}

	private void init() {
		//setResizable(false);
		JPanel contentPane = getContentPanel();

		Dimension size = PosUIManager.getSize_w100_h70();

		MigLayout layout = new MigLayout("", "sg", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		contentPane.setLayout(layout);

		tfNumber = new JTextField();
		tfNumber.setText(String.valueOf(defaultValue));
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, PosUIManager.getNumberFieldFontSize()));
		tfNumber.setFocusable(true);
		tfNumber.requestFocus();
		tfNumber.setBackground(Color.WHITE);

		contentPane.add(tfNumber, "span, grow"); //$NON-NLS-1$

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { ".", "0", "CLEAR ALL" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
		String[][] iconNames = new String[][] { { "7.png", "8.png", "9.png" }, { "4.png", "5.png", "6.png" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				{ "1.png", "2.png", "3.png" }, { "dot.png", "0.png", "" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

		int height = PosUIManager.getSize(55);
		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				PosButton posButton = new PosButton();
				posButton.setFocusable(false);
				ImageIcon icon = IconFactory.getIcon("/ui_icons/", iconNames[i][j]); //$NON-NLS-1$
				String buttonText = String.valueOf(numbers[i][j]);

				if (icon == null) {
					posButton.setText(buttonText);
				}
				else {
					posButton.setIcon(icon);
					if (POSConstants.CLEAR_ALL.equals(buttonText)) {
						posButton.setText("CLEAR ALL");
					}
				}

				posButton.setActionCommand(buttonText);
				posButton.addActionListener(this);
				String constraints = "grow,w " + size.width + "!,h " + size.height + "!"; //$NON-NLS-1$  
				if (j == numbers[i].length - 1) {
					constraints += ",wrap"; //$NON-NLS-1$
				}
				contentPane.add(posButton, constraints);
			}
		}
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

		if (POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		}
		else if (POSConstants.OK.equalsIgnoreCase(actionCommand)) {
			doOk();
		}
		else if (actionCommand.equals(POSConstants.CLEAR_ALL)) {
			doClearAll();
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
		super.setTitle(title);
		super.setTitlePaneText(title);
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
		BasicWeightInputDialog dialog2 = new BasicWeightInputDialog();
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
		BasicWeightInputDialog dialog = new BasicWeightInputDialog();
		dialog.setTitle(title);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return -1;
		}

		return (int) dialog.getValue();
	}

	public static double takeDoubleInput(String title, String dialogTitle, double initialAmount) {
		BasicWeightInputDialog dialog = new BasicWeightInputDialog();
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
		BasicWeightInputDialog dialog = new BasicWeightInputDialog();
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
		BasicWeightInputDialog dialog2 = new BasicWeightInputDialog();
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
