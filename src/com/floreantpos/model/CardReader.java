package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;

public enum CardReader {
	SWIPE("Swipe"),
	MANUAL("Manual"),
	EXTERNAL_TERMINAL("External Terminal");
	
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
