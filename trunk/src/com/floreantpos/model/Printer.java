package com.floreantpos.model;

public class Printer {
	private VirtualPrinter virtualPrinter;
	private String deviceName;
	private boolean defaultPrinter;

	public Printer() {
		super();
	}

	public Printer(VirtualPrinter virtualPrinter, String deviceName) {
		super();
		this.virtualPrinter = virtualPrinter;
		this.deviceName = deviceName;
	}

	public Printer(VirtualPrinter virtualPrinter, String deviceName, boolean defaultPrinter) {
		super();
		this.virtualPrinter = virtualPrinter;
		this.deviceName = deviceName;
		this.defaultPrinter = defaultPrinter;
	}

	public VirtualPrinter getVirtualPrinter() {
		return virtualPrinter;
	}

	public void setVirtualPrinter(VirtualPrinter virtualPrinter) {
		this.virtualPrinter = virtualPrinter;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public boolean isDefaultPrinter() {
		return defaultPrinter;
	}

	public void setDefaultPrinter(boolean defaultPrinter) {
		this.defaultPrinter = defaultPrinter;
	}
	
	@Override
	public int hashCode() {
		return this.virtualPrinter.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Printer)) {
			return false;
		}
		
		Printer that = (Printer) obj;
		
		return this.virtualPrinter.equals(that.virtualPrinter);
	}
	
	@Override
	public String toString() {
		return virtualPrinter.getName();
	}
}
