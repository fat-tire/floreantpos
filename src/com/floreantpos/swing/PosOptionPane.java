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
package com.floreantpos.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.main.Application;
import com.floreantpos.ui.dialog.OkCancelOptionDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class PosOptionPane extends OkCancelOptionDialog {
	private JTextArea taInputText;
	private String inputText;
	private QwertyKeyPad qwertyKeyPad;

	private PosOptionPane() {
		super(POSUtil.getFocusedWindow());
		init();
	}

	private PosOptionPane(Window parent) {
		super(parent);
		init();
	}

	private void init() {
		setResizable(false);

		JPanel contentPane = getContentPanel();

		MigLayout layout = new MigLayout("inset 0"); //$NON-NLS-1$ 
		contentPane.setLayout(layout);

		Dimension size = PosUIManager.getSize(0, 100);

		taInputText = new JTextArea();
		taInputText.setFont(taInputText.getFont().deriveFont(Font.BOLD, PosUIManager.getFontSize(16)));
		taInputText.setFocusable(true);
		taInputText.requestFocus();
		taInputText.setLineWrap(true);
		taInputText.setBackground(Color.WHITE);
		taInputText.setPreferredSize(size);

		qwertyKeyPad = new QwertyKeyPad();

		JScrollPane scrollPane = new JScrollPane(taInputText);

		contentPane.add(scrollPane, "spanx, grow"); //$NON-NLS-1$
		contentPane.add(qwertyKeyPad, "spanx ,grow"); //$NON-NLS-1$
	}

	@Override
	public void doOk() {
		String s = taInputText.getText();
		if (s.isEmpty()) {
			POSMessageDialog.showError(Application.getPosWindow(), "Please enter value");//$NON-NLS-1$
			return;
		}
		setValue(taInputText.getText());
		setCanceled(false);
		dispose();
	}

	public String getValue() {
		return inputText;
	}

	public void setValue(String value) {
		this.inputText = value;
	}

	public static String showInputDialog(String title) {
		PosOptionPane dialog = new PosOptionPane();
		dialog.setTitlePaneText(title);
		dialog.setTitle(title);
		dialog.pack();
		dialog.open();
		if (dialog.isCanceled()) {
			return null;
		}
		return dialog.getValue();
	}

	public static String showInputDialog(Window parent, String title) {
		PosOptionPane dialog = new PosOptionPane(parent);
		dialog.setTitlePaneText(title);
		dialog.setTitle(title);
		dialog.pack();
		dialog.open();
		if (dialog.isCanceled()) {
			return null;
		}
		return dialog.getValue();
	}
}