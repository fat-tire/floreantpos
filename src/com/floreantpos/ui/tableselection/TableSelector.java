package com.floreantpos.ui.tableselection;

import java.util.List;

import javax.swing.JPanel;

import com.floreantpos.PosLog;
import com.floreantpos.extension.OrderServiceFactory;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.util.TicketAlreadyExistsException;

public abstract class TableSelector extends JPanel {
	protected OrderType orderType;
	protected Ticket ticket;

	private boolean createNewTicket = true;

	public TableSelector() {
	}

	public void tablesSelected(OrderType orderType, List<ShopTable> selectedTables) {
		try {
			OrderServiceFactory.getOrderService().createNewTicket(orderType, selectedTables,null);
		} catch (TicketAlreadyExistsException e) {
			PosLog.error(getClass(), e);
		}
	}

	public abstract void redererTables();
	public abstract List<ShopTable> getSelectedTables();
	public abstract void updateView(boolean update);

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public boolean isCreateNewTicket() {
		return createNewTicket;
	}

	public void setCreateNewTicket(boolean createNewTicket) {
		this.createNewTicket = createNewTicket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Ticket getTicket() {
		return ticket;
	}

}