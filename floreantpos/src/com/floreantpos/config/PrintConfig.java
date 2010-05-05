package com.floreantpos.config;

import com.floreantpos.print.PrinterType;

public class PrintConfig {
	public final static String P_RECEIPT_PRINTER_TYPE =  "RECEIPT_PRINTER_TYPE";
	public final static String P_OS_PRINTER_FOR_RECEIPT = "RECEIPT_OS_PRINTER_NAME";
	public final static String P_JAVAPOS_PRINTER_FOR_RECEIPT = "RECEIPT_JAVAPOS_PRINTER_NAME";
	public final static String P_CASH_DRAWER_NAME = "CASH_DRAWER_NAME";
	public final static String P_KITCHEN_PRINTER_TYPE = "KITCHEN_PRINTER_TYPE";
	public final static String P_OS_PRINTER_FOR_KITCHEN = "KITCHEN_OS_PRINTER_NAME";
	public final static String P_JAVAPOS_PRINTER_FOR_KITCHEN = "KITCHEN_JAVAPOS_PRINTER_NAME";
	public final static String P_PRINT_RECEIPT_WHEN_SETTELED = "PRINT_RECEIPT_WHEN_SETTELED";
	public final static String P_PRINT_RECEIPT_WHEN_PAID = "PRINT_RECEIPT_WHEN_PAID";
	public final static String P_PRINT_KITCHEN_WHEN_SETTELED = "PRINT_KITCHEN_WHEN_SETTELED";
	public final static String P_PRINT_KITCHEN_WHEN_PAID = "PRINT_KITCHEN_WHEN_PAID";

	//	public static boolean printReceiptInOsPrinter() {
	//		PrinterType printerType = PrinterType.fromString(ApplicationConfig.getString(P_RECEIPT_PRINTER_TYPE, PrinterType.OS_PRINTER.getName()));
	//		return printerType == PrinterType.OS_PRINTER;
	//	}
	//	
	//	public static boolean printKitchenInOsPrinter() {
	//		PrinterType printerType = PrinterType.fromString(ApplicationConfig.getString(P_RECEIPT_PRINTER_TYPE, PrinterType.OS_PRINTER.getName()));
	//		return printerType == PrinterType.OS_PRINTER;
	//	}

	public static String getOsReceiptPrinterName() {
		return ApplicationConfig.getString(P_OS_PRINTER_FOR_RECEIPT, "POSPrinter");
	}

	public static String getOsKitchenPrinterName() {
		return ApplicationConfig.getString(P_OS_PRINTER_FOR_KITCHEN, "KitchenPrinter");
	}

	public static PrinterType getReceiptPrinterType() {
		return PrinterType.fromString(ApplicationConfig.getString(P_RECEIPT_PRINTER_TYPE, PrinterType.OS_PRINTER.getName()));
	}

	public static PrinterType getKitchenPrinterType() {
		return PrinterType.fromString(ApplicationConfig.getString(P_KITCHEN_PRINTER_TYPE, PrinterType.OS_PRINTER.getName()));
	}

	public static String getJavaPosReceiptPrinterName() {
		return ApplicationConfig.getString(P_JAVAPOS_PRINTER_FOR_RECEIPT, "POSPrinter");
	}

	public static String getJavaPosKitchenPrinterName() {
		return ApplicationConfig.getString(P_JAVAPOS_PRINTER_FOR_KITCHEN, "KitchenPrinter");
	}

	public static String getCashDrawerName() {
		return ApplicationConfig.getString(P_CASH_DRAWER_NAME, "CashDrawer");
	}

	public static boolean isPrintReceiptWhenSetteled() {
		return ApplicationConfig.getBoolean(P_PRINT_RECEIPT_WHEN_SETTELED, true);
	}

	public static boolean isPrintReceiptWhenPaid() {
		return ApplicationConfig.getBoolean(P_PRINT_RECEIPT_WHEN_PAID, false);
	}

	public static boolean isPrintKitchenWhenSetteled() {
		return ApplicationConfig.getBoolean(P_PRINT_KITCHEN_WHEN_SETTELED, false);
	}

	public static boolean isPrintKitchenWhenPaid() {
		return ApplicationConfig.getBoolean(P_PRINT_RECEIPT_WHEN_PAID, false);
	}
}
