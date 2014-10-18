package com.floreantpos.model.util;

public class TransactionSummary {
	private int count;
	private double amount;
	private double tipsAmount;
	private double changeAmount;
	
	public TransactionSummary() {
		super();
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double totalPrice) {
		this.amount = totalPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int totalTicket) {
		this.count = totalTicket;
	}

	public double getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(double changeAmount) {
		this.changeAmount = changeAmount;
	}

	public double getTipsAmount() {
		return tipsAmount;
	}

	public void setTipsAmount(double tipsAmount) {
		this.tipsAmount = tipsAmount;
	}
}
