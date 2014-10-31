package com.floreantpos;

import com.floreantpos.model.Ticket;

public interface ITicketList {
	Ticket getSelectedTicket();
	void updateTicketList();
}
