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
package com.floreantpos.ui.tableselection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.BarTabButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.ScrollableFlowPanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.util.CurrencyUtil;
import com.jidesoft.swing.JideScrollPane;

public class BarTabSelectionView extends JPanel {

	private Map<Ticket, BarTabButton> tableButtonMap = new HashMap<Ticket, BarTabButton>();
	private ScrollableFlowPanel buttonsPanel;
	private OrderType orderType;

	public BarTabSelectionView() {
		init();
	}

	private void init() {
		setLayout(new BorderLayout(10,10));

		buttonsPanel = new ScrollableFlowPanel(FlowLayout.CENTER);
		TitledBorder titledBorder1 = BorderFactory.createTitledBorder(null, "Bar Tab Tickets", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);

		JPanel rightPanel = new JPanel(new java.awt.BorderLayout(5, 5));
		rightPanel.setBorder(new CompoundBorder(titledBorder1, new EmptyBorder(2, 2, 2, 2)));

		JideScrollPane scrollPane = new JideScrollPane(buttonsPanel, JideScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JideScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setPreferredSize(PosUIManager.getSize(60, 0));

		rightPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
		add(rightPanel, java.awt.BorderLayout.CENTER);
	}

	private void rendererBarTickets() {
		List<Ticket> openTickets = TicketDAO.getInstance().findOpenTicketsByOrderType(orderType);
		for (Ticket ticket : openTickets) {
			BarTabButton barTabButton = new BarTabButton(ticket);
			barTabButton.setPreferredSize(PosUIManager.getSize(180, 160));
			barTabButton.setFont(new Font(barTabButton.getFont().getName(), Font.BOLD, 24));
			barTabButton.setText(barTabButton.getText());
			barTabButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editTab(e);
				}
			});
			barTabButton.update();
			buttonsPanel.add(barTabButton);
			tableButtonMap.put(ticket, barTabButton);
			String customerName = barTabButton.getTicket().getProperty(Ticket.CUSTOMER_NAME);
			if (customerName == null) {
				customerName = "Guest";
			}
			barTabButton.setText("<html><center>" + customerName + "<br><h4 style='margin:0px;'>" + ticket.getOwner().getFirstName() + "<br>Chk#" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ ticket.getId() + "</h4>" + CurrencyUtil.getCurrencySymbol() + ticket.getTotalAmount() + "<br><small style='margin:0px;'>Due: " //$NON-NLS-1$
					+ CurrencyUtil.getCurrencySymbol() + ticket.getDueAmount() + "</small></center></html>");
			if (!ticket.getOwner().getUserId().toString().equals(Application.getCurrentUser().getUserId().toString())) {
				barTabButton.setBackground(new Color(139, 0, 139));
				barTabButton.setForeground(Color.WHITE);
			}
			barTabButton.setTicket(ticket);
			barTabButton.setUser(ticket.getOwner());
		}
	}

	private boolean editTab(ActionEvent e) {
		BarTabButton button = (BarTabButton) e.getSource();
		if (!button.hasUserAccess()) {
			return false;
		}
		editTicket(button.getTicket());
		return true;
	}

	private void closeDialog(boolean canceled) {
		Window windowAncestor = SwingUtilities.getWindowAncestor(BarTabSelectionView.this);
		if (windowAncestor instanceof POSDialog) {
			((POSDialog) windowAncestor).setCanceled(false);
			windowAncestor.dispose();
		}
	}

	private boolean editTicket(Ticket ticket) {
		if (ticket == null || RootView.getInstance().isMaintenanceMode()) {
			return false;

		}
		closeDialog(false);

		Ticket ticketToEdit = TicketDAO.getInstance().loadFullTicket(ticket.getId());

		OrderView.getInstance().setCurrentTicket(ticketToEdit);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
		OrderView.getInstance().getTicketView().getTxtSearchItem().requestFocus();
		return true;
	}

	public void updateView(OrderType orderType) {
		this.orderType = orderType;
		buttonsPanel.getContentPane().removeAll();
		tableButtonMap.clear();
		rendererBarTickets();
		buttonsPanel.getContentPane().revalidate();
		buttonsPanel.getContentPane().repaint();
	}
}