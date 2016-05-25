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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.model.DeliveryConfiguration;
import com.floreantpos.model.dao.DeliveryConfigurationDAO;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.util.NumberUtil;

public class LengthInputDialog extends OkCancelOptionDialog implements ActionListener {
	private double defaultValue;

	private static DoubleTextField tfNumber;
	private boolean clearPreviousNumber = true;

	private static final String UNIT_KM = "KM";
	private static final String UNIT_MILE = "MILE";

	private JComboBox tfUnitC;

	public LengthInputDialog(String title) {
		super(title);
		init();
		DeliveryConfiguration deliveryConfig = DeliveryConfigurationDAO.getInstance().get(1);
		if (deliveryConfig != null) {
			tfUnitC.setSelectedItem(deliveryConfig.getUnitName());
		}
	}

	private void init() {
		setResizable(false);
		JPanel leftPanel = new JPanel();

		MigLayout layout = new MigLayout("fill,inset 0", "sg, fill", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		leftPanel.setLayout(layout);

		Dimension size = PosUIManager.getSize_w100_h70();

		tfNumber = new DoubleTextField();
		tfNumber.setText(String.valueOf(defaultValue));
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
		tfNumber.setFocusable(true);
		tfNumber.requestFocus();
		tfNumber.setBackground(Color.WHITE);

		Vector units = new Vector();
		units.add(UNIT_KM);
		units.add(UNIT_MILE);

		tfUnitC = new JComboBox(units);
		tfUnitC.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
		tfUnitC.setEnabled(false);

		leftPanel.add(tfNumber, "span 2, grow"); //$NON-NLS-1$
		leftPanel.add(tfUnitC, "span, grow"); //$NON-NLS-1$

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { ".", "0", "CLEAR ALL" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
		String[][] iconNames = new String[][] { { "7.png", "8.png", "9.png" }, { "4.png", "5.png", "6.png" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				{ "1.png", "2.png", "3.png" }, { "dot.png", "0.png", "" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

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
				String constraints = "w " + size.width + "!,h " + size.height + "!,grow"; //$NON-NLS-1$  
				if (j == numbers[i].length - 1) {
					constraints += ",wrap"; //$NON-NLS-1$
				}
				leftPanel.add(posButton, constraints);
			}
		}
		getContentPanel().add(leftPanel, BorderLayout.CENTER);
	}

	private double convert(String unitTo, double inputValue) {
		String unitFrom = tfUnitC.getSelectedItem().toString();

		if (unitFrom.equals(UNIT_KM)) {
			if (unitTo.equals(UNIT_MILE)) {
				return NumberUtil.roundToTwoDigit(0.621371 * inputValue);
			}
		}
		else if (unitFrom.equals(UNIT_MILE)) {
			if (unitTo.equals(UNIT_KM)) {
				return NumberUtil.roundToTwoDigit(1.609344 * inputValue);
			}
		}
		return inputValue;
	}

	private void doClearAll() {
		tfNumber.setText(String.valueOf(0.0));
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
		String string = tfNumber.getText() + "."; //$NON-NLS-1$
		if (!validate(string)) {
			POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
			return;
		}
		tfNumber.setText(string);
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		double value = tfNumber.getDouble();

		if (actionCommand.equals(POSConstants.CLEAR_ALL)) {
			doClearAll();
		}
		else if (actionCommand.equals(".")) { //$NON-NLS-1$
			doInsertDot();
		}
		else if (actionCommand.equals(UNIT_MILE)) {
			tfNumber.setText(String.valueOf(convert(UNIT_MILE, value)));
		}
		else if (actionCommand.equals(UNIT_KM)) {
			tfNumber.setText(String.valueOf(convert(UNIT_KM, value)));
		}
		else {
			doInsertNumber(actionCommand);
		}
	}

	private boolean validate(String str) {
		try {
			Double.parseDouble(str);
		} catch (Exception x) {
			return false;
		}
		return true;
	}

	public double getValue() {
		return Double.parseDouble(tfNumber.getText());
	}

	public double getDefaultUnitValue() {
		return convert(UNIT_KM, Double.parseDouble(tfNumber.getText()));
	}

	public void setValue(double value) {
		tfNumber.setText(String.valueOf(value));
	}

	public static double takeDoubleInput(String title, double initialAmount) {
		LengthInputDialog dialog = new LengthInputDialog("Please enter length.");
		dialog.setTitlePaneText(title);
		dialog.setValue(initialAmount);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return -1;
		}
		return dialog.getDefaultUnitValue();
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
}
