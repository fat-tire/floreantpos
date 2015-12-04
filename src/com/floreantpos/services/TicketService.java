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
package com.floreantpos.services;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;

public class TicketService {
	public static Ticket getTicket(int ticketId) {
		TicketDAO dao = new TicketDAO();
		Ticket ticket = dao.get(Integer.valueOf(ticketId));

		if (ticket == null) {
			throw new PosException(POSConstants.NO_TICKET_WITH_ID + " " + ticketId + " " + POSConstants.FOUND); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return ticket;
	}
}
