package com.floreantpos.customer;

import java.util.List;

import javax.swing.JPanel;

import com.floreantpos.PosLog;
import com.floreantpos.extension.OrderServiceFactory;
import com.floreantpos.model.Customer;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.util.TicketAlreadyExistsException;

public abstract class CustomerSelector extends JPanel {
	protected OrderType orderType;
	protected Ticket ticket;

	private boolean createNewTicket = true;
	private Customer customer;
	private String callerId;

	public CustomerSelector() {
	}

	public void customerSelected(OrderType orderType, List<ShopTable> selectedTables) {
		try {
			OrderServiceFactory.getOrderService().createNewTicket(orderType, selectedTables, null);
		} catch (TicketAlreadyExistsException e) {
			PosLog.error(getClass(), e);
		}
	}

	public abstract void redererCustomers();

	public abstract Customer getSelectedCustomer();

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

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCallerId(String callerId) {
		this.callerId=callerId; 
	}
	
	public String getCallerId() {
		return callerId;
	}
}