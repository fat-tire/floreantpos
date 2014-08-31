package com.floreantpos.model.inventory;

public enum InOutEnum {
	IN(1),
	OUT(-1),
	UNCHANGED(0);
	
	private int type;
	
	private InOutEnum(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static InOutEnum fromInt(int type) {
		InOutEnum[] values = values();
		
		for (InOutEnum inOutEnum : values) {
			if(inOutEnum.type == type) {
				return inOutEnum;
			}
		}
		
		return UNCHANGED;
	}
	
	@Override
	public String toString() {
		return name();
	}
}
