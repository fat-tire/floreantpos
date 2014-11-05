package com.floreantpos.model;

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
	public PrinterGroup (java.lang.Integer autoId) {
		super(autoId);
	}

	/**
	 * Constructor for required fields
	 */
	public PrinterGroup (
		java.lang.Integer autoId,
		java.lang.String name) {

		super (
			autoId,
			name);
	}

/*[CONSTRUCTOR MARKER END]*/

	public static final String GROUP_RECEIPT_PRINTER = "Receipt Printer";
	public static final String GROUP_KITCHEN_PRINTER = "Kitchen Printer";
	
	@Override
	public String toString() {
		return getName();
	}
}