package com.floreantpos.model;

import com.floreantpos.model.base.BasePrinterConfiguration;



public class PrinterConfiguration extends BasePrinterConfiguration {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public PrinterConfiguration () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public PrinterConfiguration (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public final static Integer ID = Integer.valueOf(1);
	
	@Override
	public String getReceiptPrinterName() {
		if(super.getReceiptPrinterName() == null) {
			return "PosPrinter";
		}
		return super.getReceiptPrinterName();
	}
	
	@Override
	public String getKitchenPrinterName() {
		if(super.getKitchenPrinterName() == null) {
			return "KitchenPrinter";
		}
		return super.getKitchenPrinterName();
	}
}