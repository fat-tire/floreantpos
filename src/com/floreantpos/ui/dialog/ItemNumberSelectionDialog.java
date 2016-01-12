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
package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.TitlePanel;

public class ItemNumberSelectionDialog extends POSDialog implements ActionListener {
	private TitlePanel titlePanel;
	private JTextField tfNumber;
	private String value;
	private PosButton btnCancel;
	private PosButton btnOk;
	private QwertyKeyPad qwertyKeyPad;

	public ItemNumberSelectionDialog() {
		init();
	}

	public ItemNumberSelectionDialog(Frame parent) {
		super(parent, true);
		init();
	}

	private void init() {
		setResizable(false);
		
		Container contentPane = getContentPane();

		MigLayout layout = new MigLayout("fillx", "[60px,fill,grow][60px,fill,grow][60px,fill,grow]", "[][][][][]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		contentPane.setLayout(layout);
		titlePanel = new TitlePanel();
		titlePanel.setTitle("Search item");
		contentPane.add(titlePanel, "spanx ,grow,height 60,wrap"); //$NON-NLS-1$

		tfNumber = new JTextField();
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
		tfNumber.setFocusable(true);
		tfNumber.requestFocus();
		tfNumber.setBackground(Color.WHITE);
		
		tfNumber.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				doOk();
			}
		});
		
		contentPane.add(tfNumber, "spanx, grow"); //$NON-NLS-1$

		qwertyKeyPad = new QwertyKeyPad();

		contentPane.add(qwertyKeyPad, "spanx ,grow");

		btnOk = new PosButton(POSConstants.OK);
		btnOk.setFocusable(false);
		btnOk.addActionListener(this);

		btnCancel = new PosButton(POSConstants.CANCEL);
		btnCancel.setFocusable(false);
		btnCancel.addActionListener(this);

		JPanel footerPanel = new JPanel(new MigLayout("fill, ins 2", "sg, fill", ""));
		footerPanel.add(btnOk);
		footerPanel.add(btnCancel);

		contentPane.add(footerPanel, "spanx ,grow");
	}
	
	
	

	private void doOk() {
		String s = tfNumber.getText();
		if (s.equals("0") || s.equals("")) {
			POSMessageDialog.showError(Application.getPosWindow(), "Please enter barcode or item no.");
			return;
		}
		setValue(tfNumber.getText());
		setCanceled(false);
		dispose();
	}

	private void doCancel() {
		setCanceled(true);
		dispose();
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		}
		else if (POSConstants.OK.equalsIgnoreCase(actionCommand)) {
			doOk();
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
