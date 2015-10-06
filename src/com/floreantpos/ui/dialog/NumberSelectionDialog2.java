package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
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
	private PosButton posButton_1;

	public NumberSelectionDialog2() {
		init();
	}
	
	public NumberSelectionDialog2(Frame parent) {
		super(parent, true);
		init();
	}

	private void init() {
		setResizable(false);

		Container contentPane = getContentPane();

		MigLayout layout = new MigLayout("fillx", "[60px,fill][60px,fill][60px,fill]", "[][][][][]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		contentPane.setLayout(layout);

		titlePanel = new TitlePanel();
		contentPane.add(titlePanel, "spanx ,growy,height 60,wrap"); //$NON-NLS-1$

		tfNumber = new JTextField();
		tfNumber.setText(String.valueOf(defaultValue));
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
		//tfNumber.setEditable(false);
		tfNumber.setFocusable(true);
		tfNumber.requestFocus();
		tfNumber.setBackground(Color.WHITE);
		//tfNumber.setHorizontalAlignment(JTextField.RIGHT);
		contentPane.add(tfNumber, "span 2, grow"); //$NON-NLS-1$

		PosButton posButton = new PosButton(POSConstants.CLEAR_ALL);
		posButton.setFocusable(false);
		posButton.setMinimumSize(new Dimension(25, 23));
		posButton.addActionListener(this);
		contentPane.add(posButton, "growy,height 55,wrap"); //$NON-NLS-1$

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { ".", "0", "CLEAR" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
		String[][] iconNames = new String[][] { { "7.png", "8.png", "9.png" }, { "4.png", "5.png", "6.png" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				{ "1.png", "2.png", "3.png" }, { "dot.png", "0.png", "clear.png" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				posButton = new PosButton();
				posButton.setFocusable(false);
				ImageIcon icon = IconFactory.getIcon("/ui_icons/", iconNames[i][j]); //$NON-NLS-1$
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
				String constraints = "grow, height 55"; //$NON-NLS-1$
				if (j == numbers[i].length - 1) {
					constraints += ", wrap"; //$NON-NLS-1$
				}
				contentPane.add(posButton, constraints);
			}
		}
		contentPane.add(new JSeparator(), "newline,spanx ,growy,gapy 20"); //$NON-NLS-1$

		posButton = new PosButton(POSConstants.OK);
		posButton.setFocusable(false);
		posButton.addActionListener(this);
		contentPane.add(posButton, "skip 1,grow"); //$NON-NLS-1$

		posButton_1 = new PosButton(POSConstants.CANCEL);
		posButton_1.setFocusable(false);
		posButton_1.addActionListener(this);
		contentPane.add(posButton_1, "grow"); //$NON-NLS-1$
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
		double d = 0;

		try {
			d = Double.parseDouble(s);
		} catch (Exception x) {
		}

		if (d == 0) {
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
		titlePanel.setTitle(title);

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
