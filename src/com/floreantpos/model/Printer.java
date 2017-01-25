/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.model;

public class Printer {
	private VirtualPrinter virtualPrinter;
	private String deviceName;
	private boolean defaultPrinter;
	private String type;

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
		if (deviceName == null) {
			return "No Print";
		}
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
		if (!(obj instanceof Printer)) {
			return false;
		}

		Printer that = (Printer) obj;

		return this.virtualPrinter.equals(that.virtualPrinter);
	}

	@Override
	public String toString() {
		return virtualPrinter.toString();
	}

	public String getDisplayName() {
		return virtualPrinter.toString() + " -    " + getDeviceName(); //$NON-NLS-1$
	}

	public String getType() {
		type = VirtualPrinter.PRINTER_TYPE_NAMES[virtualPrinter.getType()];
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
