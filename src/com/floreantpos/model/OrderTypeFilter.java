package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;


public enum OrderTypeFilter {
	ALL, DINE_IN, TAKE_OUT, PICKUP, HOME_DELIVERY, DRIVE_THRU, BAR_TAB;
	
	public static OrderTypeFilter fromString(String s) {
		if(StringUtils.isEmpty(s)) {
			return ALL;
		}
		
		OrderTypeFilter filter = valueOf(s);
		if(filter == null) {
			return ALL;
		}
		
		return filter;
	}

	public String toString() {
		return name().replaceAll("_", " ");
	}
}
