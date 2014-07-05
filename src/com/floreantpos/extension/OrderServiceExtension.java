package com.floreantpos.extension;

import net.xeoh.plugins.base.Plugin;

import com.floreantpos.util.TicketAlreadyExistsException;

public interface OrderServiceExtension extends Plugin {
	String getName();
	String getDescription();
	
	void createNewTicket(String ticketType) throws TicketAlreadyExistsException;
}
