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
package com.floreantpos.demo;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.KitchenTicket.KitchenTicketStatus;
import com.floreantpos.model.KitchenTicketItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.KitchenTicketItemDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class KitchenTicketStatusSelector extends POSDialog implements ActionListener {
	private PosButton btnVoid = new PosButton(KitchenTicketStatus.VOID.name());
	private PosButton btnReady = new PosButton(Messages.getString("KitchenTicketStatusSelector.2")); //$NON-NLS-1$

	private KitchenTicket kitchenTicket;
	private KitchenTicketItem ticketItem;

	public KitchenTicketStatusSelector(Frame parent) {
		super(parent, true);
		initComponent();
	}

	public KitchenTicketStatusSelector(Frame parent, KitchenTicket kitchenTicket) {
		super(parent, true);
		this.kitchenTicket = kitchenTicket;
		initComponent();
	}

	private void initComponent() {
		setTitle(Messages.getString("KitchenTicketStatusSelector.0")); //$NON-NLS-1$
		setIconImage(Application.getApplicationIcon().getImage());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("KitchenTicketStatusSelector.1")); //$NON-NLS-1$
		add(titlePanel, BorderLayout.NORTH);

		JPanel panel = new JPanel(new GridLayout(1, 0, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		//panel.add(btnVoid);
		panel.add(btnReady);

		add(panel);

		btnReady.setActionCommand(KitchenTicketStatus.DONE.name());
		btnVoid.addActionListener(this);
		btnReady.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			KitchenTicketStatus status = KitchenTicketStatus.valueOf(e.getActionCommand());
			ticketItem.setStatus(status.name());

			int itemCount = ticketItem.getQuantity();

			Ticket ticket = TicketDAO.getInstance().load(kitchenTicket.getTicketId());

			for (TicketItem item : ticket.getTicketItems()) {
				if (ticketItem.getMenuItemCode() != null && ticketItem.getMenuItemCode().equals(item.getItemCode())) {
					if (item.getStatus() != null && item.getStatus().equals(Ticket.STATUS_READY)) {
						continue;
					}
					if (itemCount == 0) {
						break;
					}
					if (status.equals(KitchenTicketStatus.DONE)) {
						item.setStatus(Ticket.STATUS_READY);
					}
					else {
						item.setStatus(Ticket.STATUS_VOID);
					}
					itemCount -= item.getItemCount();
				}
			}
			Transaction tx = null;
			Session session = null;

			try {
				session = KitchenTicketItemDAO.getInstance().createNewSession();
				tx = session.beginTransaction();
				session.saveOrUpdate(ticket);
				session.saveOrUpdate(ticketItem);
				tx.commit();

			} catch (Exception ex) {
				tx.rollback();
			} finally {
				session.close();
			}
			dispose();
		} catch (Exception e2) {
			POSMessageDialog.showError(this, e2.getMessage(), e2);
		}
	}

	public KitchenTicketItem getTicketItem() {
		return ticketItem;
	}

	public void setTicketItem(KitchenTicketItem ticketItem) {
		this.ticketItem = ticketItem;
	}
}
