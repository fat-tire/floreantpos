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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import jssc.SerialPortException;
import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.SerialPortUtil;

public class AutomatedWeightInputDialog extends OkCancelOptionDialog implements ActionListener {
	private double defaultValue;

	private static DoubleTextField tfNumber;
	private boolean clearPreviousNumber = true;

	private static final String UNIT_KG = "KG";
	private static final String UNIT_LB = "LB";
	private static final String UNIT_OZ = "OZ";
	private static final String UNIT_G = "G";

	private POSToggleButton btnLB;
	private POSToggleButton btnOZ;
	private POSToggleButton btnKG;
	private POSToggleButton btnG;
	private PosButton btnRefresh;
	private JTextField tfUnitC;

	private ButtonGroup group;

	public AutomatedWeightInputDialog(String title) {
		super(title);
		init();
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

		tfUnitC = new JTextField();
		tfUnitC.setText("KG");
		tfUnitC.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
		tfUnitC.setFocusable(false);
		tfUnitC.setEnabled(false);
		tfUnitC.setHorizontalAlignment(SwingConstants.RIGHT);
		tfUnitC.setBackground(Color.WHITE);
		tfUnitC.setForeground(Color.LIGHT_GRAY);

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
		createRightPanel();
	}

	private void createRightPanel() {
		JPanel rightPanel = new JPanel(new MigLayout("fill, inset 0 10 0 0", "sg, fill", ""));

		Dimension size = PosUIManager.getSize_w100_h70();

		group = new ButtonGroup();

		btnRefresh = new PosButton("REFRESH");
		btnLB = new POSToggleButton(UNIT_LB);
		btnOZ = new POSToggleButton(UNIT_OZ);
		btnKG = new POSToggleButton(UNIT_KG);
		btnG = new POSToggleButton(UNIT_G);

		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readWeight();
			}
		});
		btnLB.addActionListener(this);
		btnOZ.addActionListener(this);
		btnKG.addActionListener(this);
		btnKG.setSelected(true);
		btnG.addActionListener(this);

		group.add(btnLB);
		group.add(btnOZ);
		group.add(btnKG);
		group.add(btnG);

		rightPanel.add(btnRefresh, "w " + size.width + "!,h " + 40 + "!,wrap");
		rightPanel.add(btnLB, "w " + size.width + "!, h " + size.height + "!,wrap");
		rightPanel.add(btnOZ, "w " + size.width + "!, h " + size.height + "!,wrap");
		rightPanel.add(btnKG, "w " + size.width + "!, h " + size.height + "!,wrap");
		rightPanel.add(btnG, "w " + size.width + "!, h " + size.height + "!,wrap");
		getContentPanel().add(rightPanel, BorderLayout.EAST);
	}

	public void readWeight() {
		try {

			String weightString = SerialPortUtil.readWeight(TerminalConfig.getScalePort());
			updateScaleView(weightString);

		} catch (Exception ex) {
			POSMessageDialog.showError(ex.getMessage());
		}
	}

	protected void updateScaleView(String value) throws SerialPortException {
		value = value.replaceAll("\n", "");
		String patternFormat = "(\\d*\\.?\\d*)(lb|oz|g|kg)";
		Pattern pattern = Pattern.compile(patternFormat, Pattern.CASE_INSENSITIVE);
		Matcher m = pattern.matcher(value);

		if (m.find()) {
			tfNumber.setText(m.group(1));
			String unitName = m.group(2).toUpperCase();

			if (unitName.equals(UNIT_KG)) {
				btnKG.setSelected(true);
				tfUnitC.setText(UNIT_KG);
			}
			else if (unitName.equals(UNIT_LB)) {
				btnLB.setSelected(true);
				tfUnitC.setText(UNIT_LB);
			}
			else if (unitName.equals(UNIT_G)) {
				btnG.setSelected(true);
				tfUnitC.setText(UNIT_G);
			}
			else if (unitName.equals(UNIT_OZ)) {
				btnOZ.setSelected(true);
				tfUnitC.setText(UNIT_OZ);
			}
		}
		else {
			tfNumber.setText(String.valueOf(0));
			group.clearSelection();
		}
	}

	private double convert(String unitTo, double inputValue) {
		String unitFrom = tfUnitC.getText();

		if (unitFrom.equals(UNIT_KG)) {
			if (unitTo.equals(UNIT_LB)) {
				return NumberUtil.roundToTwoDigit(2.20 * inputValue);
			}
			else if (unitTo.equals(UNIT_OZ)) {
				return NumberUtil.roundToTwoDigit(35.27 * inputValue);
			}
			else if (unitTo.equals(UNIT_G)) {
				return NumberUtil.roundToTwoDigit(1000 * inputValue);
			}
		}
		else if (unitFrom.equals(UNIT_LB)) {
			if (unitTo.equals(UNIT_KG)) {
				return NumberUtil.roundToTwoDigit(0.45 * inputValue);
			}
			else if (unitTo.equals(UNIT_OZ)) {
				return NumberUtil.roundToTwoDigit(16 * inputValue);
			}
			else if (unitTo.equals(UNIT_G)) {
				return NumberUtil.roundToTwoDigit(453.59 * inputValue);
			}
		}
		else if (unitFrom.equals(UNIT_G)) {
			if (unitTo.equals(UNIT_KG)) {
				return NumberUtil.roundToTwoDigit(0.001 * inputValue);
			}
			else if (unitTo.equals(UNIT_OZ)) {
				return NumberUtil.roundToTwoDigit(0.035 * inputValue);
			}
			else if (unitTo.equals(UNIT_LB)) {
				return NumberUtil.roundToTwoDigit(0.00220462 * inputValue);
			}
		}
		else if (unitFrom.equals(UNIT_OZ)) {
			if (unitTo.equals(UNIT_KG)) {
				return NumberUtil.roundToTwoDigit(0.0283495 * inputValue);
			}
			else if (unitTo.equals(UNIT_G)) {
				return NumberUtil.roundToTwoDigit(28.3495 * inputValue);
			}
			else if (unitTo.equals(UNIT_LB)) {
				return NumberUtil.roundToTwoDigit(0.0625 * inputValue);
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
		else if (actionCommand.equals(UNIT_LB)) {
			tfNumber.setText(String.valueOf(convert(UNIT_LB, value)));
			tfUnitC.setText(UNIT_LB);
		}
		else if (actionCommand.equals(UNIT_OZ)) {
			tfNumber.setText(String.valueOf(convert(UNIT_OZ, value)));
			tfUnitC.setText(UNIT_OZ);
		}
		else if (actionCommand.equals(UNIT_G)) {
			tfNumber.setText(String.valueOf(convert(UNIT_G, value)));
			tfUnitC.setText(UNIT_G);
		}
		else if (actionCommand.equals(UNIT_KG)) {
			tfNumber.setText(String.valueOf(convert(UNIT_KG, value)));
			tfUnitC.setText(UNIT_KG);
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

	public void setValue(double value) {
		tfNumber.setText(String.valueOf(value));
	}

	public static double takeDoubleInput(String title, double initialAmount) {
		AutomatedWeightInputDialog dialog = new AutomatedWeightInputDialog("Please enter item weight or quantity");
		dialog.setTitlePaneText(title);
		dialog.setValue(initialAmount);
		if (TerminalConfig.isActiveScaleDisplay()) {
			dialog.readWeight();
		}
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return -1;
		}

		return dialog.getValue();
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
