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
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.model.CustomPayment;
import com.floreantpos.model.dao.CustomPaymentDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;

public class CustomPaymentSelectionDialog extends POSDialog {

	private String paymentName;
	private String paymentRef;
	private String paymentFieldName;

	public CustomPaymentSelectionDialog() {

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		createUI();
	}

	private void createUI() {

		setPreferredSize(new Dimension(675, 529));

		JPanel customTypePanel = new JPanel(new MigLayout("wrap 5,center", "", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		PosScrollPane pane = new PosScrollPane(customTypePanel, PosScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, PosScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		List<CustomPayment> custompPayments = CustomPaymentDAO.getInstance().findAll();

		for (Iterator iter = custompPayments.iterator(); iter.hasNext();) {
			CustomPayment customPayment = (CustomPayment) iter.next();

			PosButton button = new PosButton(customPayment.getName());
			button.setPreferredSize(new Dimension(120, 80));
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					dispose();

					String paymentName = e.getActionCommand();
					setPaymentName(paymentName);

					CustomPayment payment = CustomPaymentDAO.getInstance().getByName(paymentName);
					setPaymentFieldName(payment.getRefNumberFieldName());

					if (payment.isRequiredRefNumber()) {

						PaymentReferenceEntryDialog dialog = new PaymentReferenceEntryDialog(payment);
						dialog.pack();
						dialog.open();

						if (dialog.isCanceled())
							return;
						setPaymentRef(dialog.getPaymentRef());
					}
					setCanceled(false);
				}
			});
			customTypePanel.add(button);
		}

		getContentPane().add(pane, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);

		PosButton btnCancel = new PosButton();
		panel_2.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		btnCancel.setText(Messages.getString("CustomPaymentSelectionDialog.3")); //$NON-NLS-1$

		JSeparator separator = new JSeparator();
		panel_1.add(separator, BorderLayout.NORTH);

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("CustomPaymentSelectionDialog.4")); //$NON-NLS-1$
		getContentPane().add(titlePanel, BorderLayout.NORTH);
	}

	/**
	 * @return the paymentName
	 */
	public String getPaymentName() {
		return paymentName;
	}

	/**
	 * @param paymentName the paymentName to set
	 */
	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
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

	/**
	 * @return the paymentFieldName
	 */
	public String getPaymentFieldName() {
		return paymentFieldName;
	}

	/**
	 * @param paymentFieldName the paymentFieldName to set
	 */
	public void setPaymentFieldName(String paymentFieldName) {
		this.paymentFieldName = paymentFieldName;
	}
}
