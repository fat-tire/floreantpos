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

import javax.swing.JOptionPane;

import com.floreantpos.ITicketList;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.services.TicketService;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.VoidTicketDialog;
import com.floreantpos.util.POSUtil;

public class VoidTicketAction extends PosAction {

	private final ITicketList ticketList;

	public VoidTicketAction(ITicketList ticketList) {
		super(POSConstants.VOID_TICKET_BUTTON_TEXT, UserPermission.VOID_TICKET); //$NON-NLS-1$
		this.ticketList = ticketList;
	}

	@Override
	public void execute() {
		try {
			Ticket ticket = ticketList.getSelectedTicket();

			if (ticket == null) {
				int ticketId = NumberSelectionDialog2.takeIntInput(Messages.getString("VoidTicketAction.0")); //$NON-NLS-1$
				if (ticketId == -1)
					return;

				ticket = TicketService.getTicket(ticketId);
			}
			if (ticket.isVoided()) {
				if (POSMessageDialog.showYesNoQuestionDialog(POSUtil.getFocusedWindow(), "This ticket is already voided. \nDo you want to re-void?", "Confirm") != JOptionPane.YES_OPTION) {
					return;
				}
			}
			Ticket ticketToVoid = TicketDAO.getInstance().loadFullTicket(ticket.getId());

			VoidTicketDialog voidTicketDialog = new VoidTicketDialog();
			voidTicketDialog.setTicket(ticketToVoid);
			voidTicketDialog.open();

			if (!voidTicketDialog.isCanceled()) {
				ticketList.updateTicketList();
			}
		} catch (PosException e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

}
