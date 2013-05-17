package com.floreantpos.swing;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class FocusedTextField extends JTextField implements FocusListener {
	private boolean numeric;

	public FocusedTextField() {
		init();
	}

	public FocusedTextField(boolean numeric) {
		this.numeric = numeric;

		init();
	}

	public FocusedTextField(String text) {
		super(text);

		init();
	}

	public FocusedTextField(int columns) {
		super(columns);

		init();
	}

	public FocusedTextField(String text, int columns) {
		super(text, columns);

		init();
	}

	public FocusedTextField(Document doc, String text, int columns) {
		super(doc, text, columns);

		init();
	}

	private void init() {
		if (numeric) {
			setDocument(new DoubleDocument());
		}
		installFocusHandler();
	}

	private void installFocusHandler() {
		addFocusListener(this);
	}

	public void focusGained(FocusEvent e) {
		setBackground(Color.YELLOW);
	}

	public void focusLost(FocusEvent e) {
		setBackground(Color.WHITE);
	}

}
