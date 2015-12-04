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
package com.floreantpos.print;

public enum PrinterType {
	OS_PRINTER("printer"), //$NON-NLS-1$
	JAVAPOS("javapos"); //$NON-NLS-1$
	
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
