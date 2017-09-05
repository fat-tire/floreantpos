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

package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.ScrollableFlowPanel;
import com.floreantpos.ui.dialog.OkCancelOptionDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

/**
 * 
 * @author MShahriar
 */
public class TicketSelectionDialog extends OkCancelOptionDialog {

	private ScrollableFlowPanel buttonsPanel;
	private List<Ticket> addedTicketListModel = new ArrayList<Ticket>();

	public TicketSelectionDialog() {
		super(Application.getPosWindow(), Messages.getString("TicketSelectionDialog.0"));
		initComponent();
		initData();
	}

	public TicketSelectionDialog(List<Ticket> tickets) {
		initComponent();
		rendererTickets(tickets);
		setResizable(true);
	}

	private void initComponent() {
		setOkButtonText(Messages.getString("TicketSelectionDialog.3"));//$NON-NLS-1$
		buttonsPanel = new ScrollableFlowPanel(FlowLayout.LEADING);

		JScrollPane scrollPane = new PosScrollPane(buttonsPanel, PosScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, PosScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(80, 0));
		scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), scrollPane.getBorder()));

		getContentPanel().add(scrollPane, BorderLayout.CENTER);
		setSize(1024, 600);
	}

	private void initData() {
		TicketDAO dao = new TicketDAO();
		try {
			List<Ticket> tickets = dao.getTicketsWithSpecificFields(Ticket.PROP_ID, Ticket.PROP_DUE_AMOUNT, Ticket.PROP_CREATE_DATE);
			Dimension size = PosUIManager.getSize(115, 80);
			for (Ticket ticket : tickets) {
				if (ticket.getDueAmount() <= 0) {
					continue;
				}
				TicketButton btnTicket = new TicketButton(ticket);
				buttonsPanel.add(btnTicket);
				btnTicket.setPreferredSize(size);
			}
		} catch (PosException e) {
			POSMessageDialog.showError(TicketSelectionDialog.this, e.getLocalizedMessage(), e);
		}
	}

	private void rendererTickets(List<Ticket> tickets) {
		try {
			for (Ticket ticket : tickets) {
				if (ticket.getDueAmount() <= 0) {
					continue;
				}
				buttonsPanel.add(new TicketButton(ticket));
			}

		} catch (PosException e) {
			POSMessageDialog.showError(TicketSelectionDialog.this, e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void doOk() {
		if (addedTicketListModel.isEmpty()) {
			POSMessageDialog.showMessage(Messages.getString("TicketSelectionDialog.5")); //$NON-NLS-1$
			return;
		}
		setCanceled(false);
		dispose();
	}

	public void doCancel() {
		addedTicketListModel.clear();
		setCanceled(true);
		dispose();
	}

	public List<Ticket> getSelectedTickets() {
		return addedTicketListModel;
	}

	private class TicketButton extends POSToggleButton implements ActionListener {
		private Ticket ticket;

		TicketButton(Ticket ticket) {
			this.ticket = ticket;
			setFont(getFont().deriveFont(Font.BOLD, PosUIManager.getFontSize(18)));
			setText("<html><body><center>#" + ticket.getId() + "<br>" + POSConstants.DUE + ":" + ticket.getDueAmount() + "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			if (isSelected()) {
				addedTicketListModel.add(this.ticket);
			}
			else {
				addedTicketListModel.remove(this.ticket);
			}
		}
	}
}
