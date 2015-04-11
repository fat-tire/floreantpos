package com.floreantpos.model;

public enum InventoryTransactionType {
	IN(1),
	OUT(-1),
	UNCHANGED(0);
	
	private int type;
	
	private InventoryTransactionType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static InventoryTransactionType fromInt(int type) {
		InventoryTransactionType[] values = values();
		
		for (InventoryTransactionType inOutEnum : values) {
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
