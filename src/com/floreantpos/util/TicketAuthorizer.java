package com.floreantpos.util;

import com.floreantpos.ui.TicketListView;

public interface TicketAuthorizer {
	TicketListView getTicketListView();
	void updateTicketList();
}
