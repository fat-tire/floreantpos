package com.floreantpos.model.util;

public class TicketSummary {
	private int totalTicket;
	private double totalPrice;
	
	public TicketSummary() {
		super();
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getTotalTicket() {
		return totalTicket;
	}

	public void setTotalTicket(int totalTicket) {
		this.totalTicket = totalTicket;
	}

}
