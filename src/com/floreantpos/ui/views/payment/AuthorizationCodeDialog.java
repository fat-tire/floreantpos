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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.PaymentType;
import com.floreantpos.swing.FocusedTextField;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;

import net.miginfocom.swing.MigLayout;

public class AuthorizationCodeDialog extends POSDialog implements CardInputProcessor {
	private CardInputListener cardInputListener;
	private FocusedTextField tfAuthorizationCode;
	private POSToggleButton btnVisaCard;
	private POSToggleButton btnMasterCard;
	private POSToggleButton btnAmericanExpress;
	private POSToggleButton btnDiscoverCard;
	private POSToggleButton btnDebitVisaCard;
	private POSToggleButton btnDebitMasterCard;

	public AuthorizationCodeDialog(CardInputListener cardInputListener) {
		super(Application.getPosWindow(), true);
		this.cardInputListener = cardInputListener;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		createUI();
		btnVisaCard.setSelected(true);
	}

	private void createUI() {

		setPreferredSize(new Dimension(PosUIManager.getSize(1000), PosUIManager.getSize(600)));
		btnVisaCard = new POSToggleButton();
		btnVisaCard.setIcon(IconFactory.getIcon("/ui_icons/", "" + "visa_card.png")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		btnMasterCard = new POSToggleButton(""); //$NON-NLS-1$
		btnMasterCard.setIcon(IconFactory.getIcon("/ui_icons/", "" + "master_card.png")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		btnAmericanExpress = new POSToggleButton();
		btnAmericanExpress.setIcon(IconFactory.getIcon("/ui_icons/", "" + "am_ex_card.png")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		btnDiscoverCard = new POSToggleButton();
		btnDiscoverCard.setIcon(IconFactory.getIcon("/ui_icons/", "" + "discover_card.png")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		ButtonGroup group = new ButtonGroup();
		group.add(btnVisaCard);
		group.add(btnMasterCard);
		group.add(btnAmericanExpress);
		group.add(btnDiscoverCard);

		JPanel creditCardPanel = new JPanel(new GridLayout(1, 0, 10, 10));

		creditCardPanel.add(btnVisaCard);
		creditCardPanel.add(btnMasterCard);
		creditCardPanel.add(btnAmericanExpress);
		creditCardPanel.add(btnDiscoverCard);

		creditCardPanel.setBorder(new CompoundBorder(new TitledBorder(Messages.getString("PaymentTypeSelectionDialog.4")), new EmptyBorder(10, 10, 10, 10))); //$NON-NLS-1$

		JPanel debitCardPanel = new JPanel(new GridLayout(1, 0, 10, 10));

		btnDebitVisaCard = new POSToggleButton();
		btnDebitVisaCard.setIcon(IconFactory.getIcon("/ui_icons/", "" + "visa_card.png")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		btnDebitMasterCard = new POSToggleButton();
		btnDebitMasterCard.setIcon(IconFactory.getIcon("/ui_icons/", "" + "master_card.png")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		group.add(btnDebitVisaCard);
		group.add(btnDebitMasterCard);

		debitCardPanel.add(btnDebitVisaCard);
		debitCardPanel.add(btnDebitMasterCard);

		debitCardPanel.setBorder(new CompoundBorder(new TitledBorder(Messages.getString("PaymentTypeSelectionDialog.6")), new EmptyBorder(10, 10, 10, 10))); //$NON-NLS-1$

		JPanel panel = new JPanel();

		JPanel centralPanel = new JPanel(new BorderLayout());
		centralPanel.add(panel, BorderLayout.CENTER);

		JPanel cardPanel = new JPanel(new MigLayout("fill, ins 2", "sg, fill", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		cardPanel.add(debitCardPanel);
		cardPanel.add(creditCardPanel);

		centralPanel.add(cardPanel, BorderLayout.NORTH);

		getContentPane().add(centralPanel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[][grow]", "[50][grow]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblAuthorizationCode = new JLabel(Messages.getString("AuthorizationCodeDialog.3")); //$NON-NLS-1$
		panel.add(lblAuthorizationCode, "cell 0 0,alignx trailing"); //$NON-NLS-1$

		tfAuthorizationCode = new FocusedTextField();
		tfAuthorizationCode.setColumns(12);
		panel.add(tfAuthorizationCode, "cell 1 0,growx"); //$NON-NLS-1$

		QwertyKeyPad qwertyKeyPad = new QwertyKeyPad();
		panel.add(qwertyKeyPad, "cell 0 1 2 1,grow"); //$NON-NLS-1$

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);

		PosButton btnSubmit = new PosButton();
		panel_2.add(btnSubmit);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(false);
				dispose();
				cardInputListener.cardInputted(AuthorizationCodeDialog.this, getPaymentType());
			}
		});
		btnSubmit.setText(Messages.getString("AuthorizationCodeDialog.7")); //$NON-NLS-1$

		PosButton btnCancel = new PosButton();
		panel_2.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		btnCancel.setText(Messages.getString("AuthorizationCodeDialog.8")); //$NON-NLS-1$

		JSeparator separator = new JSeparator();
		panel_1.add(separator, BorderLayout.NORTH);

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("AuthorizationCodeDialog.9")); //$NON-NLS-1$
		getContentPane().add(titlePanel, BorderLayout.NORTH);
	}

	private PaymentType getPaymentType() {
		if (btnVisaCard.isSelected()) {
			return PaymentType.CREDIT_VISA;
		}
		else if (btnMasterCard.isSelected()) {
			return PaymentType.CREDIT_MASTER_CARD;
		}
		else if (btnAmericanExpress.isSelected()) {
			return PaymentType.CREDIT_AMEX;
		}
		else if (btnDiscoverCard.isSelected()) {
			return PaymentType.CREDIT_DISCOVERY;
		}
		else if (btnDebitMasterCard.isSelected()) {
			return PaymentType.DEBIT_MASTER_CARD;
		}
		else if (btnDebitVisaCard.isSelected()) {
			return PaymentType.DEBIT_VISA;
		}
		else {
			return PaymentType.CREDIT_VISA;
		}
	}

	public String getAuthorizationCode() {
		return tfAuthorizationCode.getText();
	}

}
