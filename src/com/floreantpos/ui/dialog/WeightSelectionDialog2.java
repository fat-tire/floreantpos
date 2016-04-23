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
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import jssc.SerialPortException;
import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.SerialPortUtil;

public class WeightSelectionDialog2 extends POSDialog implements ActionListener {
	private double defaultValue;

	private TitlePanel titlePanel;
	private static DoubleTextField tfNumber;

	private boolean floatingPoint;
	private PosButton btnCancel;
	private boolean clearPreviousNumber = true;

	private static final String UNIT_KG = "KG";
	private static final String UNIT_LB = "LB";
	private static final String UNIT_OZ = "OZ";
	private static final String UNIT_G = "G";

	private POSToggleButton btnLB;
	private POSToggleButton btnOZ;
	private POSToggleButton btnKG;
	private POSToggleButton btnG;
	private PosButton btnDefault;
	private PosButton btnRefresh;
	private JTextField tfUnitC;

	private ButtonGroup group;

	public WeightSelectionDialog2() {
		init();
		if (TerminalConfig.isActiveScaleDisplay()) {
			readWeight();
		}
	}

	public WeightSelectionDialog2(Frame parent) {
		super(parent, true);
		init();
		if (TerminalConfig.isActiveScaleDisplay()) {
			readWeight();
		}
	}

	private void init() {
		setResizable(false);
		JPanel contentPanel = new JPanel();

		JLabel lblProductName = new JLabel();
		getContentPane().add(lblProductName, BorderLayout.NORTH);

		MigLayout layout = new MigLayout("", "sg", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		contentPanel.setLayout(layout);

		int height = 60;
		titlePanel = new TitlePanel();
		contentPanel.add(titlePanel, "spanx ,growy,height " + height + ",wrap"); //$NON-NLS-1$

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

		contentPanel.add(tfNumber, "span 2, grow"); //$NON-NLS-1$
		contentPanel.add(tfUnitC, "span, grow"); //$NON-NLS-1$

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
				String constraints = "grow,height " + 55; //$NON-NLS-1$  
				if (j == numbers[i].length - 1) {
					constraints += ",wrap"; //$NON-NLS-1$
				}
				contentPanel.add(posButton, constraints);
			}
		}
		contentPanel.add(new JSeparator(), "span,grow,gaptop 5"); //$NON-NLS-1$

		PosButton btnOk = new PosButton(POSConstants.OK);
		btnOk.setFocusable(false);
		btnOk.addActionListener(this);

		btnCancel = new PosButton(POSConstants.CANCEL);
		btnCancel.setFocusable(false);
		btnCancel.addActionListener(this);

		JPanel buttonPanel = new JPanel(new MigLayout("fill, inset 0", "sg fill", ""));
		buttonPanel.add(btnOk, "grow");
		buttonPanel.add(btnCancel, "grow");

		contentPanel.add(buttonPanel, "span,grow"); //$NON-NLS-1$
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		createRightPanel();
	}

	private void createRightPanel() {
		JPanel rightPanel = new JPanel(new MigLayout("filly", "sg", ""));
		int size = 60;

		group = new ButtonGroup();

		btnRefresh = new PosButton("REFRESH");
		btnLB = new POSToggleButton(UNIT_LB);
		btnOZ = new POSToggleButton(UNIT_OZ);
		btnKG = new POSToggleButton(UNIT_KG);
		btnG = new POSToggleButton(UNIT_G);
		btnDefault = new PosButton("DEFAULT");

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
		btnDefault.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				group.clearSelection();
				tfNumber.setText(String.valueOf(defaultValue));
			}
		});

		group.add(btnLB);
		group.add(btnOZ);
		group.add(btnKG);
		group.add(btnG);
		//group.add(btnDefault);

		rightPanel.add(new JLabel(""), "h " + 55 + "!, grow, wrap");
		rightPanel.add(btnRefresh, "h " + 40 + "!, grow, wrap");
		rightPanel.add(btnLB, "h " + size + "!, grow, wrap");
		rightPanel.add(btnOZ, "h " + size + "!, grow, wrap");
		rightPanel.add(btnKG, "h " + size + "!, grow, wrap");
		rightPanel.add(btnG, "h " + size + "!, grow, wrap");
		rightPanel.add(btnDefault, "h " + size + "!, grow, wrap");
		getContentPane().add(rightPanel, BorderLayout.EAST);
	}

	public void readWeight() {
		try {
			
			String weightString = SerialPortUtil.readWeight(TerminalConfig.getScalePort());
			updateScaleView(weightString);
			
		} catch (Exception ex) {
			POSMessageDialog.showError(Application.getPosWindow(), POSConstants.ERROR_MESSAGE, ex);
		}
	}

	protected void updateScaleView(String value) throws SerialPortException {
		value = value.replaceAll("\n", "");
		String patternFormat = "([\\d.]+)\\s+(lb|oz|g|kg)";
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

	private void doOk() {
		if (!validate(tfNumber.getText())) {
			POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
			return;
		}
		setCanceled(false);
		dispose();
	}

	private void doCancel() {
		setCanceled(true);
		dispose();
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
		titlePanel.setTitle(title);

		super.setTitle("Please enter item weight or quantity.");
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
		defaultValue = value;
	}

	public boolean isFloatingPoint() {
		return floatingPoint;
	}

	public void setFloatingPoint(boolean decimalAllowed) {
		this.floatingPoint = decimalAllowed;
	}

	public static void main(String[] args) {
		WeightSelectionDialog2 dialog2 = new WeightSelectionDialog2();
		dialog2.pack();
		dialog2.setVisible(true);
	}

	public double getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
		tfNumber.setText(String.valueOf(defaultValue));
	}

	public static int takeIntInput(String title) {
		WeightSelectionDialog2 dialog = new WeightSelectionDialog2();
		dialog.setTitle(title);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return -1;
		}

		return (int) dialog.getValue();
	}

	public static double takeDoubleInput(String title, String dialogTitle, double initialAmount) {
		WeightSelectionDialog2 dialog = new WeightSelectionDialog2();
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
		WeightSelectionDialog2 dialog = new WeightSelectionDialog2();
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
		WeightSelectionDialog2 dialog2 = new WeightSelectionDialog2();
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
