package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;


public enum PaymentStatusFilter {
	ALL, PAID, UN_PAID;
	
	public static PaymentStatusFilter fromString(String s) {
		if(StringUtils.isEmpty(s)) {
			return ALL;
		}
		
		PaymentStatusFilter filter = valueOf(s);
		if(filter == null) {
			return ALL;
		}
		
		return filter;
	}

	public String toString() {
		return name().replaceAll("_", " ");
	}
}
