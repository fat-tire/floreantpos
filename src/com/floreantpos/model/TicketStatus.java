package com.floreantpos.model;

public enum TicketStatus {
	PAID, HOLD, PAID_AND_HOLD, UNKNOWN;
	
	public String toString() {
		switch (this) {
			case PAID_AND_HOLD:
				return "PAID & HOLD";

			default:
				return name();
		}
	};
}
