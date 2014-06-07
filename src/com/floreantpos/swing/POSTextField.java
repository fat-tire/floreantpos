package com.floreantpos.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class POSTextField extends JTextField implements FocusListener {

	public POSTextField() {
		super(10);
		addFocusListener(this);
	}

	public POSTextField(String text) {
		super(text);
		addFocusListener(this);
	}

	public POSTextField(int columns) {
		super(columns);
		addFocusListener(this);
	}

	public POSTextField(String text, int columns) {
		super(text, columns);
		addFocusListener(this);
	}

	public POSTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
		addFocusListener(this);
	}

	public void focusGained(FocusEvent e) {
		selectAll();
	}

	public void focusLost(FocusEvent e) {
	}

}
