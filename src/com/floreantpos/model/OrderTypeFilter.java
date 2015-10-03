package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;


public enum OrderTypeFilter {
	ALL, DINE_IN, TAKE_OUT, PICKUP, HOME_DELIVERY, DRIVE_THRU, BAR_TAB;
	
	public static OrderTypeFilter fromString(String s) {
		if(StringUtils.isEmpty(s)) {
			return ALL;
		}
		
		try {
			OrderTypeFilter filter = valueOf(s);

			return filter;
		} catch (Exception e) {
			return ALL;
		}
	}

	public String toString() {
		return name().replaceAll("_", " "); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
