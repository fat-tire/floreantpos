package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;

public enum OrderType {
	DINE_IN, TAKE_OUT, PICKUP, HOME_DELIVERY, DRIVE_THRU, BAR_TAB;
	
	private OrderTypeProperties properties;
	
	public String toString() {
		if(properties != null && StringUtils.isNotEmpty(properties.getAlias())) {
			return properties.getAlias();
		}
		
		String string = Messages.getString(name());
		if(StringUtils.isEmpty(string)) {
			return name().replaceAll("_", " "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return string;
	}

	public OrderTypeProperties getProperties() {
		return properties;
	}

	public void setProperties(OrderTypeProperties properties) {
		this.properties = properties;
	};
}
