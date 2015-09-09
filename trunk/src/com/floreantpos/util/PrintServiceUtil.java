package com.floreantpos.util;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class PrintServiceUtil {
	/**
	 * Get {@link javax.print.PrintService} for specified printer name.
	 * 
	 * @param printerName
	 * @return
	 */
	public static PrintService getPrintServiceForPrinter(final String printerName) {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

		for (int i = 0; i < printServices.length; i++) {
			PrintService printService = printServices[i];
			if (printService.getName().equals(printerName)) {
				return printService;
			}
		}
		
		return PrintServiceLookup.lookupDefaultPrintService();
	}
}
