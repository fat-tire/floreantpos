package com.floreantpos.extension;

import net.xeoh.plugins.base.Plugin;

import com.floreantpos.model.Ticket;
import com.floreantpos.util.TicketAlreadyExistsException;

public interface OrderServiceExtension extends Plugin {
	String getName();
	String getDescription();
	
	void init();
	void createNewTicket(String ticketType) throws TicketAlreadyExistsException;
	void setCustomerToTicket(Ticket ticket);
	void setDeliveryDate(Ticket ticket);
}
