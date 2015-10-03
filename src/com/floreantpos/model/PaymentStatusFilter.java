package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;

public enum PaymentStatusFilter {
	OPEN, PAID, CLOSED;

	public static PaymentStatusFilter fromString(String s) {
		if (StringUtils.isEmpty(s)) {
			return OPEN;
		}

		try {
			PaymentStatusFilter filter = valueOf(s);
			return filter;
		} catch (Exception e) {
			return OPEN;
		}
	}

	public String toString() {
		return name().replaceAll("_", " "); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
