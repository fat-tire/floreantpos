package com.floreantpos.swing;


public class DoubleTextField extends FocusedTextField {

	public DoubleTextField() {
		setDocument(new IntegerDocument());
	}
	
	public double getDouble() {
		return Double.parseDouble(getText());
	}
}
