package com.floreantpos.model;

import com.floreantpos.Messages;

public enum TicketStatus {
	PAID, HOLD, PAID_AND_HOLD, UNKNOWN;
	
	public String toString() {
		switch (this) {
			case PAID_AND_HOLD:
				return Messages.getString("TicketStatus.0"); //$NON-NLS-1$

			default:
				return name();
		}
	};
}
