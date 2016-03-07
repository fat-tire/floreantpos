package com.floreantpos.ui.tableselection;

import java.util.List;

import javax.swing.JPanel;

import com.floreantpos.extension.OrderServiceFactory;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.ShopTable;
import com.floreantpos.util.TicketAlreadyExistsException;

public abstract class TableSelector extends JPanel {
	protected OrderType orderType;
	
	private boolean createNewTicket = true;

	public TableSelector() {
	}

	public void tablesSelected(OrderType orderType, List<ShopTable> selectedTables) {
		try {
			OrderServiceFactory.getOrderService().createNewTicket(orderType, selectedTables);
		} catch (TicketAlreadyExistsException e) {
			e.printStackTrace();
		}
	}

	public abstract void redererTables();
	public abstract List<ShopTable> getSelectedTables();

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

}