/**
 * 
 */
package com.floreantpos.swing;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FixedLengthDocument extends PlainDocument {

	/**
	 * 
	 */
	private int length;

	/**
	 * @param field
	 */
	public FixedLengthDocument(int length) {
		this.length = length;
	}

	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		int currentLength = super.getLength() + str.length();
		
		if(currentLength > this.length) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		
		super.insertString(offs, str, a);
	}

	public int getMaximumLength() {
		return length;
	}

	public void setMaximumLength(int length) {
		this.length = length;
	}
	
}