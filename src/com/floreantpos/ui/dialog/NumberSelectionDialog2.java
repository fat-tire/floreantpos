package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;

public class NumberSelectionDialog2 extends POSDialog implements ActionListener {
	private int defaultValue;
	
	private TitlePanel titlePanel;
	private JTextField tfNumber;

	private boolean floatingPoint;

	public NumberSelectionDialog2() {
		setResizable(false);
		
		Container contentPane = getContentPane();

		MigLayout layout = new MigLayout("fillx", "[60][60][60]", "");
		contentPane.setLayout(layout);

		titlePanel = new TitlePanel();
		contentPane.add(titlePanel, "span, grow, wrap, height 60");

		tfNumber = new JTextField();
		tfNumber.setText(String.valueOf(defaultValue));
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
		tfNumber.setEditable(false);
		tfNumber.setBackground(Color.WHITE);
		tfNumber.setHorizontalAlignment(JTextField.RIGHT);
		contentPane.add(tfNumber, "span 2, grow");

		PosButton posButton = new PosButton(POSConstants.CLEAR_ALL);
		posButton.addActionListener(this);
		contentPane.add(posButton, "grow,shrink,wrap, height 55");

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { ".", "0", "CLEAR" } };
		String[][] iconNames = new String[][] { { "7_32.png", "8_32.png", "9_32.png" }, { "4_32.png", "5_32.png", "6_32.png" }, { "1_32.png", "2_32.png", "3_32.png" },
				{ "dot_32.png", "0_32.png", "clear_32.png" } };

		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				posButton = new PosButton();
				ImageIcon icon = IconFactory.getIcon(iconNames[i][j]);
				String buttonText = String.valueOf(numbers[i][j]);

				if (icon == null) {
					posButton.setText(buttonText);
				}
				else {
					posButton.setIcon(icon);
					if (POSConstants.CLEAR.equals(buttonText)) {
						posButton.setText(buttonText);
					}
				}

				posButton.setActionCommand(buttonText);
				posButton.addActionListener(this);
				String constraints = "grow, height 55";
				if (j == numbers[i].length - 1) {
					constraints += ", wrap";
				}
				contentPane.add(posButton, constraints);
			}
		}
		contentPane.add(new JSeparator(), "newline, grow, span, gaptop 20");

		posButton = new PosButton(POSConstants.OK);
		posButton.addActionListener(this);
		contentPane.add(posButton, "skip 1, grow, height 55");

		posButton = new PosButton(POSConstants.CANCEL);
		posButton.addActionListener(this);
		contentPane.add(posButton, "grow, height 55");

	}
	
	private void doOk() {
		if (!validate(tfNumber.getText())) {
			POSMessageDialog.showError(POSConstants.INVALID_NUMBER);
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
		String s = tfNumber.getText();
		if (s.equals("0")) {
			tfNumber.setText(number);
			return;
		}

		s = s + number;
		if (!validate(s)) {
			POSMessageDialog.showError(POSConstants.INVALID_NUMBER);
			return;
		}
		tfNumber.setText(s);
	}
	
	private void doInsertDot() {
		if (isFloatingPoint() && tfNumber.getText().indexOf('.') < 0) {
			String string = tfNumber.getText() + ".";
			if (!validate(string)) {
				POSMessageDialog.showError(POSConstants.INVALID_NUMBER);
				return;
			}
			tfNumber.setText(string);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		if(POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		}
		else if(POSConstants.OK.equalsIgnoreCase(actionCommand)) {
			doOk();
		}
		else if (actionCommand.equals(POSConstants.CLEAR_ALL)) {
			doClearAll();
		}
		else if (actionCommand.equals(POSConstants.CLEAR)) {
			doClear();
		}
		else if (actionCommand.equals(".")) {
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
		titlePanel.setTitle(title);
		
		super.setTitle(title);
	}

	public double getValue() {
		return Double.parseDouble(tfNumber.getText());
	}

	public void setValue(double value) {
		if (isFloatingPoint()) {
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

}
