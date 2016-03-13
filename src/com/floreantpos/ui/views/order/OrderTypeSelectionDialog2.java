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
package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.Ticket;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;

public class OrderTypeSelectionDialog2 extends POSDialog {
	Ticket ticket;
	private String selectedOrderType;
	private PosButton btnForHere;
	private PosButton btnToGo;

	public OrderTypeSelectionDialog2(Ticket ticket) throws HeadlessException {
		this.ticket = ticket;

		initializeComponent();
	}

	private void initializeComponent() {
		setTitle(Messages.getString("OrderTypeSelectionDialog.0")); //$NON-NLS-1$
		setResizable(false);
		setLayout(new BorderLayout(5, 5));

		JPanel orderTypePanel = new JPanel(new GridLayout(1, 0, 10, 10));
		orderTypePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		btnForHere = new PosButton(OrderType.FOR_HERE);
		btnForHere.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedOrderType = OrderType.FOR_HERE;
				setCanceled(false);
				dispose();
			}
		});
		orderTypePanel.add(btnForHere);

		btnToGo = new PosButton(OrderType.TO_GO);
		btnToGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedOrderType = OrderType.TO_GO;
				setCanceled(false);
				dispose();
			}
		});
		orderTypePanel.add(btnToGo);

		PosButton btnCancel = new PosButton(POSConstants.CANCEL_BUTTON_TEXT);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});

		JPanel actionPanel = new JPanel(new MigLayout("fill")); //$NON-NLS-1$
		actionPanel.add(btnCancel, "growx, span"); //$NON-NLS-1$

		add(orderTypePanel);
		add(actionPanel, BorderLayout.SOUTH);

		setSize(400, 250);
	}

	public String getSelectedOrderType() {
		return selectedOrderType;
	}
}
