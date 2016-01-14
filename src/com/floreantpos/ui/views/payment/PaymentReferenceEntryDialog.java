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

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.CustomPayment;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class PaymentReferenceEntryDialog extends POSDialog {
	private JLabel lblRef;
	private FixedLengthTextField txtRef;
	private String paymentRef;
	private CustomPayment payment;

	public PaymentReferenceEntryDialog(CustomPayment payment) {
		super(BackOfficeWindow.getInstance(), true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		this.payment = payment;
		setTitle(Messages.getString("PaymentReferenceEntryDialog.0")); //$NON-NLS-1$

		createUI();
	}

	private void createUI() {

		lblRef = new JLabel(Messages.getString("PaymentReferenceEntryDialog.1") + payment.getRefNumberFieldName()); //$NON-NLS-1$
		txtRef = new FixedLengthTextField(120);

		setPreferredSize(new Dimension(900, 500));

		JPanel customTypePanel = new JPanel(new MigLayout("", "grow", "grow")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		customTypePanel.add(lblRef);
		customTypePanel.add(txtRef);

		QwertyKeyPad qwertyKeyPad = new QwertyKeyPad();

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		centerPanel.add(customTypePanel, BorderLayout.NORTH);
		centerPanel.add(qwertyKeyPad, BorderLayout.CENTER);

		getContentPane().add(centerPanel, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);

		PosButton btnSubmit = new PosButton();
		panel_2.add(btnSubmit);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitCard();
			}
		});

		btnSubmit.setText(Messages.getString("PaymentReferenceEntryDialog.5")); //$NON-NLS-1$

		PosButton btnCancel = new PosButton();
		panel_2.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});

		btnCancel.setText(Messages.getString("PaymentReferenceEntryDialog.2")); //$NON-NLS-1$

		JSeparator separator = new JSeparator();
		panel_1.add(separator, BorderLayout.NORTH);

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("PaymentReferenceEntryDialog.6")); //$NON-NLS-1$
		getContentPane().add(titlePanel, BorderLayout.NORTH);

	}

	protected void submitCard() {
		String ref = txtRef.getText();
		if (ref.equals("")) { //$NON-NLS-1$
			POSMessageDialog.showMessage(Messages.getString("PaymentReferenceEntryDialog.8")); //$NON-NLS-1$
			return;
		}
		setPaymentRef(ref);
		setCanceled(false);
		dispose();
	}

	/**
	 * @return the paymentMethod
	 */
	public String getPaymentRef() {
		return paymentRef;
	}

	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}
}
