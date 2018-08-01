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

import java.util.Iterator;
import java.util.List;

import com.floreantpos.model.base.BaseVirtualPrinter;

public class VirtualPrinter extends BaseVirtualPrinter {
	private static final long serialVersionUID = 1L;

	public final static int REPORT = 0;
	public final static int RECEIPT = 1;
	public final static int KITCHEN = 2;
	public final static int PACKING = 3;
	public final static int KITCHEN_DISPLAY = 4;

	public final static String[] PRINTER_TYPE_NAMES = { "Report receipt", "Receipt", "Kitchen", "Packing", "KDS" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public VirtualPrinter() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public VirtualPrinter(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public VirtualPrinter(java.lang.Integer id, java.lang.String name) {

		super(id, name);
	}

	/*[CONSTRUCTOR MARKER END]*/

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VirtualPrinter)) {
			return false;
		}

		VirtualPrinter other = (VirtualPrinter) obj;

		return this.name.equalsIgnoreCase(other.name);
	}

	public String getDisplayName() {
		return PRINTER_TYPE_NAMES[getType()];
	}

	@Override
	public String toString() {
		String name = getName();

		List<String> typeNames = getOrderTypeNames();
		if (typeNames != null && typeNames.size() > 0) {
			name += " ("; //$NON-NLS-1$

			for (Iterator iterator = typeNames.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				name += string;
				if (iterator.hasNext()) {
					name += ", "; //$NON-NLS-1$
				}
			}
			name += ")"; //$NON-NLS-1$
		}

		return name;
	}
}