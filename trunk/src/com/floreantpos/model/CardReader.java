package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;

public enum CardReader {
	SWIPE(Messages.getString("CardReader.0")), //$NON-NLS-1$
	MANUAL(Messages.getString("CardReader.1")), //$NON-NLS-1$
	EXTERNAL_TERMINAL(Messages.getString("CardReader.2")); //$NON-NLS-1$
	
	private String type;
	
	private CardReader(String typeString) {
		this.type = typeString;
	}
	
	public String getType() {
		return type;
	}
	
	public static CardReader fromString(String name) {
		if(StringUtils.isEmpty(name)) {
			return null;
		}
		
		return CardReader.valueOf(name);
	}
	
	@Override
	public String toString() {
		return type;
	}
}
