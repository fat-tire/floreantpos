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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.payment.SettleTicketDialog;
import com.floreantpos.util.POSUtil;

public class SettleTicketAction extends AbstractAction {

	private int ticketId;
	private User currentUser;

	public SettleTicketAction(int ticketId) {
		this.ticketId = ticketId;
	}

	public SettleTicketAction(int ticketId, User currentUser) {
		this.ticketId = ticketId;
		this.currentUser = currentUser;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public boolean execute() {
		final Ticket ticket = TicketDAO.getInstance().loadFullTicket(ticketId);

		if (ticket.isPaid()) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("SettleTicketAction.0")); //$NON-NLS-1$
			return false;
		}
		User currentUser2 = (currentUser == null) ? Application.getCurrentUser() : currentUser;
		final SettleTicketDialog settleTicketDialog = new SettleTicketDialog(ticket, currentUser2);
		settleTicketDialog.setSize(Application.getPosWindow().getSize());
		settleTicketDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		settleTicketDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				doSettleBartab(ticket, settleTicketDialog);
			}
		});
		settleTicketDialog.openFullScreen();
		return !settleTicketDialog.isCanceled();
	}

	private void doSettleBartab(final Ticket ticket, final SettleTicketDialog settleTicketDialog) {
		try {
			OrderType orderType = ticket.getOrderType();
			if (orderType.isBarTab()) {
				PosTransaction bartabTransaction = ticket.getBartabTransaction();
				if (bartabTransaction != null && !bartabTransaction.isCaptured() && !bartabTransaction.isVoided()) {
					settleTicketDialog.settleBartab();
				}
			}
		} catch (Exception e2) {
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), e2.getMessage(), e2);
			settleTicketDialog.setCanceled(true);
			settleTicketDialog.dispose();
		}
	}
}
