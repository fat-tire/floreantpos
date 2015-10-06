package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.model.OrderType;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;

public class OrderTypeSelectionDialog extends POSDialog {
	private OrderType selectedOrderType;

	public OrderTypeSelectionDialog() throws HeadlessException {
		setTitle(Messages.getString("OrderTypeSelectionDialog.0")); //$NON-NLS-1$
		setResizable(false);
		setLayout(new BorderLayout(5, 5));

		JPanel orderTypePanel = new JPanel(new GridLayout(1, 0, 10, 10));
		orderTypePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		OrderType[] values = OrderType.values();
		for (final OrderType orderType : values) {
			if (orderType == OrderType.BAR_TAB) {
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

			setSize(700, 250);
		}
	}

	public OrderType getSelectedOrderType() {
		return selectedOrderType;
	}
}
