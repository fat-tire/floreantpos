package com.floreantpos.swing;



public class IntegerTextField extends FocusedTextField {

	public IntegerTextField() {
		setDocument(new DoubleDocument());
	}
	
	public int getInteger() {
		return Integer.parseInt(getText());
	}
}
