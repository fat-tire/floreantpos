package com.floreantpos.extension;

import net.xeoh.plugins.base.Plugin;

public interface TicketImportPlugin extends Plugin {
	
	void startService();
	void stopService();
	void importTicket();
}
