package com.floreantpos.ui.views.order.modifier;

import com.floreantpos.model.MenuItem;
import com.floreantpos.model.TicketItem;

public class ModifierSelectionModel {
	private TicketItem ticketItem;
	private MenuItem menuItem;

	public ModifierSelectionModel() {
		super();
	}

	public ModifierSelectionModel(TicketItem ticketItem, MenuItem menuItem) {
		super();
		this.ticketItem = ticketItem;
		this.menuItem = menuItem;
	}

	public TicketItem getTicketItem() {
		return ticketItem;
	}

	public void setTicketItem(TicketItem ticketItem) {
		this.ticketItem = ticketItem;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

}
