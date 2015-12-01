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
