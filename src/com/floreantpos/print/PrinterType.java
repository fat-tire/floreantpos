package com.floreantpos.print;

public enum PrinterType {
	OS_PRINTER("printer"),
	JAVAPOS("javapos");
	
	private final String name;
	
	PrinterType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static PrinterType fromString(String name) {
		PrinterType[] values = values();
		for (int i = 0; i < values.length; i++) {
			PrinterType printerType = values[i];
			if(printerType.getName().equals(name)) {
				return printerType;
			}
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
