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
}
