package com.floreantpos.ui.views.order.actions;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;

public interface TicketEditListener {
	void itemAdded(Ticket ticket, TicketItem item);
	void itemRemoved(TicketItem item);
}
