/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.floreantpos.Messages;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.TransparentPanel;

public class NoteView extends JPanel implements ActionListener, ChangeListener {
	Font buttonFont = getFont().deriveFont(Font.BOLD, PosUIManager.getFontSize(24));

	String[] s1 = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
	String[] s2 = { "q", "w", "e", "r", "t", "y", "u", "i", "o", "p" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
	String[] s3 = { "a", "s", "d", "f", "g", "h", "j", "k", "l", ";" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
	String[] s4 = { "z", "x", "c", "v", "b", "n", "m", "-", ",", "." }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$

	JTextArea note = new JTextArea();

	private ArrayList<PosButton> buttons = new ArrayList<PosButton>();
	Dimension size = PosUIManager.getSize(60, 60);

	public NoteView() {
		setLayout(new BorderLayout(5, 5));

		note.setWrapStyleWord(true);
		note.setLineWrap(true);
		note.setDocument(new FixedLengthDocument(255));

		TransparentPanel northPanel = new TransparentPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(note);
		northPanel.setPreferredSize(new Dimension(100, 60));
		northPanel.add(scrollPane);
		add(northPanel, BorderLayout.NORTH);

		TransparentPanel centerPanel = new TransparentPanel(new GridLayout(0, 1, 2, 2));
		centerPanel.add(addButtonsToPanel(s1));
		centerPanel.add(addButtonsToPanel(s2));
		centerPanel.add(addButtonsToPanel(s3));
		centerPanel.add(addButtonsToPanel(s4));
		add(centerPanel, BorderLayout.CENTER);

		JPanel eastPanel = new JPanel(new GridLayout(0, 1, 2, 2));
		PosButton button = new PosButton();
		button.setText(Messages.getString("NoteView.40")); //$NON-NLS-1$
		button.addActionListener(this);
		eastPanel.add(button);

		POSToggleButton toggleButton = new POSToggleButton();
		toggleButton.setText(Messages.getString("NoteView.41")); //$NON-NLS-1$
		toggleButton.addChangeListener(this);
		eastPanel.add(toggleButton);

		button = new PosButton();
		button.setText(com.floreantpos.POSConstants.CLEAR);
		button.addActionListener(this);
		eastPanel.add(button);

		button = new PosButton();
		button.setText(com.floreantpos.POSConstants.CLEAR_ALL);
		button.addActionListener(this);
		eastPanel.add(button);

		eastPanel.setPreferredSize(PosUIManager.getSize(90, 70));
		add(eastPanel, BorderLayout.EAST);
	}

	private TransparentPanel addButtonsToPanel(String[] buttonText) {
		TransparentPanel panel = new TransparentPanel(new GridLayout(0, s1.length, 2, 2));
		for (int i = 0; i < buttonText.length; i++) {
			String s = buttonText[i];
			PosButton button = new PosButton();
			button.setText(s);
			button.setPreferredSize(size);
			button.addActionListener(this);
			button.setFont(buttonFont);
			buttons.add(button);
			panel.add(button);
		}
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		note.requestFocus();

		String s = e.getActionCommand();

		if (s.equals(com.floreantpos.POSConstants.CLEAR)) {
			String str = note.getText();
			if (str.length() > 0) {
				str = str.substring(0, str.length() - 1);
			}
			note.setText(str);
		}
		else if (s.equals(com.floreantpos.POSConstants.CLEAR_ALL)) {
			note.setText(""); //$NON-NLS-1$
		}
		else if (s.equals(Messages.getString("NoteView.43"))) { //$NON-NLS-1$
			String str = note.getText();
			if (str == null) {
				str = ""; //$NON-NLS-1$
			}
			note.setText(str + " "); //$NON-NLS-1$
		}
		else {
			String str = note.getText();
			if (str == null) {
				str = ""; //$NON-NLS-1$
			}
			note.setText(str + s);
		}
	}

	public void stateChanged(ChangeEvent e) {
		JToggleButton b = (JToggleButton) e.getSource();

		if (b.isSelected()) {
			for (PosButton button : buttons) {
				button.setText(button.getText().toUpperCase());
			}
		}
		else {
			for (PosButton button : buttons) {
				button.setText(button.getText().toLowerCase());
			}
		}
	}

	public String getNote() {
		return note.getText();
	}

	public void setNoteLength(int length) {
		note.setDocument(new FixedLengthDocument(length));
	}
}
