package com.floreantpos.model.util;

public class TransactionSummary {
	private int totalNumber;
	private double totalAmount;
	private double changeAmount;
	
	public TransactionSummary() {
		super();
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalPrice) {
		this.totalAmount = totalPrice;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalTicket) {
		this.totalNumber = totalTicket;
	}

	public double getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(double changeAmount) {
		this.changeAmount = changeAmount;
	}
}
