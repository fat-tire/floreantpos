package com.floreantpos.swing;

public class DoubleTextField extends FocusedTextField {

	public DoubleTextField() {
		setDocument(new DoubleDocument());
	}

	public DoubleTextField(int columns) {
		super(columns);
		setDocument(new DoubleDocument());
	}

	public double getDouble() {
		try {
			return Double.parseDouble(getText());
		} catch (Exception e) {
			return Double.NaN;
		}
	}
}
