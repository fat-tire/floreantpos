package com.floreantpos.extension;


public interface TicketImportPlugin extends FloreantPlugin {
	
	void startService();
	void stopService();
	void importTicket();
}
