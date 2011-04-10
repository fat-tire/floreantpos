package com.floreantpos.ui.views.order.actions;

import com.floreantpos.model.Ticket;

public interface OrderListener {
	void payOrderSelected(Ticket ticket);
}
