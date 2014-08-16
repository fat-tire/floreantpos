package com.floreantpos.swing;

public class IntegerTextField extends FocusedTextField {

	public IntegerTextField() {
		setDocument(new IntegerDocument());
	}

	public IntegerTextField(int columns) {
		super(columns);
		setDocument(new IntegerDocument());
	}

	public int getInteger() {
		try {
			return Integer.parseInt(getText());
		} catch (Exception e) {
			return 0;
		}
	}
}
