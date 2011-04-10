package com.floreantpos.model.util;

public class TransactionSummary {
	private int totalNumber;
	private double subtotalAmount;
	private double totalAmount;
	private double totalTax;
	private double totalDiscount;
	private double gratuityAmount;
	
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

	public double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(double totalTax) {
		this.totalTax = totalTax;
	}

	public double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public double getGratuityAmount() {
		return gratuityAmount;
	}

	public void setGratuityAmount(double gratuityAmount) {
		this.gratuityAmount = gratuityAmount;
	}

	public double getSubtotalAmount() {
		return subtotalAmount;
	}

	public void setSubtotalAmount(double subtotalAmount) {
		this.subtotalAmount = subtotalAmount;
	}

}
