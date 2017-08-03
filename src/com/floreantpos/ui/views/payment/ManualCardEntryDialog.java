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
package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.floreantpos.Messages;
import com.floreantpos.config.CardConfig;
import com.floreantpos.model.PaymentType;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;

import net.miginfocom.swing.MigLayout;

public class ManualCardEntryDialog extends POSDialog implements CardInputProcessor {
	private CardInputListener cardInputListener;
	private POSTextField tfCardNumber;
	private POSTextField tfExpMonth;
	private POSTextField tfExpYear;
	private PaymentType paymentType;

	public ManualCardEntryDialog(CardInputListener cardInputListener) {
		this.cardInputListener = cardInputListener;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		createUI();

	}

	private void createUI() {
		setPreferredSize(new Dimension(PosUIManager.getSize(900), PosUIManager.getSize(500)));
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][grow]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblCardNumber = new JLabel(Messages.getString("ManualCardEntryDialog.3")); //$NON-NLS-1$
		panel.add(lblCardNumber, "cell 0 0,alignx trailing"); //$NON-NLS-1$

		tfCardNumber = new POSTextField();
		tfCardNumber.setColumns(20);
		panel.add(tfCardNumber, "cell 1 0"); //$NON-NLS-1$

		JLabel lblExpieryMonth = new JLabel(Messages.getString("ManualCardEntryDialog.6")); //$NON-NLS-1$
		panel.add(lblExpieryMonth, "cell 0 1,alignx trailing"); //$NON-NLS-1$

		tfExpMonth = new POSTextField();
		tfExpMonth.setColumns(4);
		panel.add(tfExpMonth, "cell 1 1"); //$NON-NLS-1$

		JLabel lblExpieryYear = new JLabel(Messages.getString("ManualCardEntryDialog.9")); //$NON-NLS-1$
		panel.add(lblExpieryYear, "cell 0 2,alignx trailing"); //$NON-NLS-1$

		tfExpYear = new POSTextField();
		tfExpYear.setColumns(4);
		panel.add(tfExpYear, "cell 1 2"); //$NON-NLS-1$

		QwertyKeyPad qwertyKeyPad = new QwertyKeyPad();
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 5));
		centerPanel.add(panel, BorderLayout.NORTH);
		centerPanel.add(qwertyKeyPad, BorderLayout.CENTER);

		getContentPane().add(centerPanel, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel(new MigLayout("al center")); //$NON-NLS-1$
		panel_1.add(panel_2);

		PosButton btnSwipeCard = new PosButton();
		panel_2.add(btnSwipeCard, "grow");
		btnSwipeCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSwipeCardDialog();
			}
		});
		btnSwipeCard.setText(Messages.getString("ManualCardEntryDialog.13")); //$NON-NLS-1$

		PosButton btnEnterAuthorizationCode = new PosButton();
		panel_2.add(btnEnterAuthorizationCode, "grow");
		btnEnterAuthorizationCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openAuthorizationCodeEntryDialog();
			}
		});
		btnEnterAuthorizationCode.setText(Messages.getString("ManualCardEntryDialog.14")); //$NON-NLS-1$

		PosButton btnSubmit = new PosButton();
		panel_2.add(btnSubmit, "grow");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitCard();
			}
		});
		btnSubmit.setText(Messages.getString("ManualCardEntryDialog.15")); //$NON-NLS-1$

		PosButton btnCancel = new PosButton();
		panel_2.add(btnCancel, "grow");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		btnCancel.setText(Messages.getString("ManualCardEntryDialog.16")); //$NON-NLS-1$

		JSeparator separator = new JSeparator();
		panel_1.add(separator, BorderLayout.NORTH);

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("ManualCardEntryDialog.17")); //$NON-NLS-1$
		getContentPane().add(titlePanel, BorderLayout.NORTH);

		if (!CardConfig.isSwipeCardSupported()) {
			btnSwipeCard.setEnabled(false);
		}
		if (!CardConfig.isExtTerminalSupported()) {
			btnEnterAuthorizationCode.setEnabled(false);
		}
	}

	protected void openAuthorizationCodeEntryDialog() {
		setCanceled(true);
		dispose();

		AuthorizationCodeDialog dialog = new AuthorizationCodeDialog(cardInputListener);
		dialog.pack();
		dialog.open();
	}

	protected void submitCard() {
		setCanceled(false);
		dispose();
		cardInputListener.cardInputted(this, null);
	}

	private void openSwipeCardDialog() {
		setCanceled(true);
		dispose();

		SwipeCardDialog swipeCardDialog = new SwipeCardDialog(cardInputListener);
		swipeCardDialog.pack();
		swipeCardDialog.open();
	}

	public String getCardNumber() {
		return tfCardNumber.getText();
	}

	public String getExpMonth() {
		return tfExpMonth.getText();
	}

	public String getExpYear() {
		return tfExpYear.getText();
	}

}
