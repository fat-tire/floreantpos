package com.floreantpos.swing;



public class FixedLengthTextField extends FocusedTextField {
	private FixedLengthDocument fixedLengthDocument;
	
	public FixedLengthTextField() {
		this(20);
	}
	
	public FixedLengthTextField(int length) {
		super(10);
		
		fixedLengthDocument = new FixedLengthDocument(length);
		setDocument(fixedLengthDocument);
	}

	public int getLength() {
		return fixedLengthDocument.getLength();
	}

	public void setLength(int length) {
		fixedLengthDocument.setMaximumLength(length);
	}

}
