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
		}
		
		return name;
	}
}