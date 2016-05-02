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
/*
 * NumberSelectionView.java
 *
 * Created on August 25, 2006, 7:56 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSMessageDialog;

/**
 *
 * @author  MShahriar
 */
public class NumberSelectionView extends TransparentPanel implements ActionListener {
	private TitledBorder titledBorder;

	private boolean decimalAllowed;
	private JTextField tfNumber;

	public NumberSelectionView() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout(5, 5));

		tfNumber = new JTextField();
		tfNumber.setText("0"); //$NON-NLS-1$
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, PosUIManager.getNumberFieldFontSize()));
		tfNumber.setEditable(false);
		tfNumber.setBackground(Color.WHITE);
		tfNumber.setHorizontalAlignment(JTextField.RIGHT);

		JPanel northPanel = new JPanel(new BorderLayout(5, 5));
		northPanel.add(tfNumber, BorderLayout.CENTER);

		/*PosButton btnClearAll = new PosButton();
		btnClearAll.setText(com.floreantpos.POSConstants.CLEAR_ALL);
		btnClearAll.setActionCommand(com.floreantpos.POSConstants.CLEAR_ALL);
		btnClearAll.setPreferredSize(new Dimension(PosUIManager.getSize(90, 50)));
		btnClearAll.addActionListener(this);
		northPanel.add(btnClearAll, BorderLayout.EAST);*/

		add(northPanel, BorderLayout.NORTH);

		String[][] numbers = { { "7", "8", "9" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				{ "4", "5", "6" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				{ "1", "2", "3" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				{ ".", "0", com.floreantpos.POSConstants.CLEAR_ALL} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		};
		String[][] iconNames = new String[][] { { "7.png", "8.png", "9.png" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				{ "4.png", "5.png", "6.png" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				{ "1.png", "2.png", "3.png" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				{ "dot.png", "0.png", "clear.png" } //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		};

		JPanel centerPanel = new JPanel(new GridLayout(4, 3, 5, 5));
		Dimension preferredSize = PosUIManager.getSize(100, 70);

		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				PosButton posButton = new PosButton();
				ImageIcon icon = IconFactory.getIcon("/ui_icons/", iconNames[i][j]); //$NON-NLS-1$
				String buttonText = String.valueOf(numbers[i][j]);

				if (icon == null) {
					posButton.setText(buttonText);
				}
				else {
					posButton.setIcon(icon);
					if (com.floreantpos.POSConstants.CLEAR_ALL.equals(buttonText)) {
						posButton.setText(buttonText);
					}
				}

				posButton.setActionCommand(buttonText);
				posButton.setPreferredSize(preferredSize);
				posButton.addActionListener(this);
				centerPanel.add(posButton);
			}
		}
		add(centerPanel, BorderLayout.CENTER);

		titledBorder = new TitledBorder(""); //$NON-NLS-1$
		titledBorder.setTitleJustification(TitledBorder.CENTER);

		setBorder(titledBorder);
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals(com.floreantpos.POSConstants.CLEAR_ALL)) {
			tfNumber.setText("0"); //$NON-NLS-1$
		}
		else if (actionCommand.equals(com.floreantpos.POSConstants.CLEAR)) {
			String s = tfNumber.getText();
			if (s.length() > 1) {
				s = s.substring(0, s.length() - 1);
			}
			else {
				s = "0"; //$NON-NLS-1$
			}
			tfNumber.setText(s);
		}
		else if (actionCommand.equals(".")) { //$NON-NLS-1$
			if (isDecimalAllowed() && tfNumber.getText().indexOf('.') < 0) {
				String string = tfNumber.getText() + "."; //$NON-NLS-1$
				if (!validate(string)) {
					POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("NumberSelectionView.1")); //$NON-NLS-1$
					return;
				}
				tfNumber.setText(string);
			}
		}
		else {
			String s = tfNumber.getText();
			if (s.equals("0")) { //$NON-NLS-1$
				tfNumber.setText(actionCommand);
				return;
			}

			s = s + actionCommand;
			if (!validate(s)) {
				POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("NumberSelectionView.0")); //$NON-NLS-1$
				return;
			}
			tfNumber.setText(s);
		}

	}

	private boolean validate(String str) {
		if (isDecimalAllowed()) {
			try {
				Double.parseDouble(str);
			} catch (Exception x) {
				return false;
			}
		}
		else {
			try {
				Integer.parseInt(str);
			} catch (Exception x) {
				return false;
			}
		}
		return true;
	}

	public void setTitle(String title) {
		titledBorder.setTitle(title);
	}

	public double getValue() {
		return Double.parseDouble(tfNumber.getText());
	}

	public String getText() {
		return tfNumber.getText();
	}

	public void setValue(double value) {
		if (isDecimalAllowed()) {
			tfNumber.setText(String.valueOf(value));
		}
		else {
			tfNumber.setText(String.valueOf((int) value));
		}
	}

	public boolean isDecimalAllowed() {
		return decimalAllowed;
	}

	public void setDecimalAllowed(boolean decimalAllowed) {
		this.decimalAllowed = decimalAllowed;
	}
}
