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
package com.floreantpos.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.views.order.TicketSelectionDialog;
import com.floreantpos.ui.views.payment.GroupSettleTicketDialog;
import com.floreantpos.util.POSUtil;

public class GroupSettleTicketAction extends AbstractAction {

	private List<Ticket> tickets;

	public GroupSettleTicketAction() {
	}

	public GroupSettleTicketAction(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public boolean execute() {
		if (!POSUtil.checkDrawerAssignment()) {
			return false;
		}

		if (tickets == null || tickets.isEmpty()) {
			TicketSelectionDialog ticketSelectionDialog = new TicketSelectionDialog();
			ticketSelectionDialog.open();

			if (ticketSelectionDialog.isCanceled()) {
				return false;
			}

			List<Ticket> selectedTickets = ticketSelectionDialog.getSelectedTickets();
			if (selectedTickets == null) {
				return false;
			}

			tickets = new ArrayList<Ticket>();

			for (int i = 0; i < selectedTickets.size(); i++) {
				Ticket ticket = selectedTickets.get(i);

				Ticket fullTicket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
				if (fullTicket.getOrderType().isBarTab())
					continue;
				tickets.add(fullTicket);
			}
		}
		GroupSettleTicketDialog posDialog = new GroupSettleTicketDialog(tickets);
		posDialog.setSize(Application.getPosWindow().getSize());
		posDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		posDialog.openUndecoratedFullScreen();
		return true;
	}

}
