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
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;

public class OrderTypeSelectionDialog extends POSDialog {
	private OrderType selectedOrderType;

	public OrderTypeSelectionDialog() throws HeadlessException {
		setTitle(Messages.getString("OrderTypeSelectionDialog.0")); //$NON-NLS-1$
		setResizable(false);
		setLayout(new BorderLayout(5, 5));

		JPanel orderTypePanel = new JPanel(new GridLayout(1, 0, 5, 5));
		orderTypePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		List<OrderType> values = Application.getInstance().getOrderTypes();
		for (final OrderType orderType : values) {
			if (!orderType.isBarTab()) {
				continue;
			}

			PosButton button = new PosButton(orderType.toString());
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectedOrderType = orderType;
					setCanceled(false);
					dispose();
				}
			});
			orderTypePanel.add(button);
		}
		PosButton btnCancel = new PosButton(POSConstants.CANCEL_BUTTON_TEXT);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});

		JPanel actionPanel = new JPanel();
		actionPanel.add(btnCancel);

		add(orderTypePanel);
		add(actionPanel, BorderLayout.SOUTH);

		setSize(450, 300);
	}

	public OrderType getSelectedOrderType() {
		return selectedOrderType;
	}
}
