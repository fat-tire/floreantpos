package com.floreantpos.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.floreantpos.extension.TicketImportPlugin;
import com.floreantpos.main.Application;

public class TicketImportAction extends AbstractAction {

	public TicketImportAction() {
		super("ONLINE TICKETS");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TicketImportPlugin ticketImportPlugin = Application.getPluginManager().getPlugin(TicketImportPlugin.class);
		if(ticketImportPlugin != null) {
			ticketImportPlugin.startService();
		}
	}
}
