package com.floreantpos.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.text.Document;

import org.apache.commons.lang.StringUtils;

public class FocusedTextField extends JTextField implements FocusListener {

	public FocusedTextField() {
		init();
	}

//	public FocusedTextField(boolean numeric) {
//		this.numeric = numeric;
//
//		init();
//	}

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
		installFocusHandler();
	}
	
	public boolean isEmpty() {
		return StringUtils.isEmpty(getText());
	}

	private void installFocusHandler() {
		addFocusListener(this);
	}

	public void focusGained(FocusEvent e) {
//		setBackground(Color.YELLOW);
		
		selectAll();
	}

	public void focusLost(FocusEvent e) {
//		setBackground(Color.WHITE);
	}

}
