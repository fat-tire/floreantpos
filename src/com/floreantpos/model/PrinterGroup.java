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

import com.floreantpos.model.base.BasePrinterGroup;



public class PrinterGroup extends BasePrinterGroup {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public PrinterGroup () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public PrinterGroup (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public PrinterGroup (
		java.lang.Integer id,
		java.lang.String name) {

		super (
			id,
			name);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		String name = getName();
		
		List<String> list = getPrinterNames();
		if(list != null && list.size() > 0) {
			name += " ("; //$NON-NLS-1$
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				name += string;
				
				if(iterator.hasNext()) {
					name += ", "; //$NON-NLS-1$
				}
			}
			name += ")"; //$NON-NLS-1$
			
			if(isDefault) {
				name+="   -  Default";  //$NON-NLS-1$
			}
		}
		
		return name;
	}
}