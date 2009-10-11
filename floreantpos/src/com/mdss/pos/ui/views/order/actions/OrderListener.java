package com.mdss.pos.ui.views.order.actions;

import com.mdss.pos.model.Ticket;

public interface OrderListener {
	void payOrderSelected(Ticket ticket);
}
