package com.floreantpos.test;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class PrintServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("Number of print services: " + printServices.length);

        for (PrintService printer : printServices)
            System.out.println("Printer: " + printer.getName()); 
	}

}
