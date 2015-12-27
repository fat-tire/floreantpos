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
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.ScrollableFlowPanel;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

/**
 * 
 * @author MShahriar
 */
public class TicketSelectionDialog extends POSDialog {

	JPanel buttonsPanel = new JPanel();

	private Map<Integer, TicketButton> ticketButtonMap = new HashMap<Integer, TicketSelectionDialog.TicketButton>();
	private DefaultListModel<TicketButton> addedTicketListModel = new DefaultListModel<TicketButton>();

	public TicketSelectionDialog() {
		initializeComponent();
		initialize();
	}

	private void initializeComponent() {
		setTitle(Messages.getString("TicketSelectionDialog.0")); //$NON-NLS-1$
		setLayout(new BorderLayout());

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("TicketSelectionDialog.0"));//$NON-NLS-1$

		buttonsPanel.setLayout(new MigLayout("fill")); //$NON-NLS-1$

		ScrollableFlowPanel scrollablePanel = new ScrollableFlowPanel(FlowLayout.LEADING);
		scrollablePanel.add(buttonsPanel);

		JPanel actionButtonPanel = new JPanel();
		actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		PosButton btnOk = new PosButton(Messages.getString("TicketSelectionDialog.3")); //$NON-NLS-1$
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinishTicketSelection();
			}
		});
		btnOk.setPreferredSize(new Dimension(100, TerminalConfig.getTouchScreenButtonHeight()));

		PosButton btnCancel = new PosButton(POSConstants.CANCEL.toUpperCase());
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addedTicketListModel.removeAllElements();
				setCanceled(true);
				dispose();
			}
		});
		btnCancel.setPreferredSize(new Dimension(100, TerminalConfig.getTouchScreenButtonHeight()));

		actionButtonPanel.add(btnOk);
		actionButtonPanel.add(btnCancel);

		add(titlePanel, BorderLayout.NORTH);
		add(new JScrollPane(scrollablePanel), BorderLayout.CENTER);
		add(actionButtonPanel, BorderLayout.SOUTH);
		setSize(600, 600);

	}

	public void initialize() {

		ticketButtonMap.clear();

		TicketDAO dao = new TicketDAO();
		try {
			List<Ticket> tickets = dao.findOpenTickets();
			for (Ticket ticket : tickets) {
				TicketButton ticketButton = new TicketButton(ticket);
				Integer key = ticket.getId();
				ticketButtonMap.put(key, ticketButton);
				buttonsPanel.add(ticketButton, "split 2,align center"); //$NON-NLS-1$
			}

		} catch (PosException e) {
			e.printStackTrace();
		}
	}

	protected void doFinishTicketSelection() {
		if (addedTicketListModel.isEmpty()) {
			POSMessageDialog.showMessage(Messages.getString("TicketSelectionDialog.5")); //$NON-NLS-1$
			return;
		}
		setCanceled(false);
		dispose();
	}

	public List<Ticket> getSelectedTickets() {
		Enumeration<TicketButton> elements = addedTicketListModel.elements();
		List<Ticket> tickets = new ArrayList<Ticket>();

		while (elements.hasMoreElements()) {
			TicketButton ticketButton = (TicketButton) elements.nextElement();
			tickets.add(ticketButton.getTicket());
		}

		return tickets;
	}

	private class TicketButton extends POSToggleButton implements ActionListener {
		private static final int BUTTON_SIZE = 80;
		Ticket ticket;

		TicketButton(Ticket ticket) {
			this.ticket = ticket;
			setFocusable(true);
			setFocusPainted(true);
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
			setFont(getFont().deriveFont(18.0f));

			setText("<html><body><center><u>" + ticket.getId() + "</u><br>" + ticket.getDueAmount() + "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			if (isSelected() && !addedTicketListModel.contains(this)) {
				addedTicketListModel.addElement(this);
			}
			else {
				addedTicketListModel.removeElement(this);
			}
		}

		Ticket getTicket() {
			return ticket;
		}
	}
}
