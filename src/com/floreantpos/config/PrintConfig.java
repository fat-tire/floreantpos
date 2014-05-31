package com.floreantpos.config;


public class PrintConfig {
	public final static String P_RECEIPT_PRINTER_TYPE =  "RECEIPT_PRINTER_TYPE";
	public final static String P_OS_PRINTER_FOR_RECEIPT = "RECEIPT_OS_PRINTER_NAME";
	public final static String P_JAVAPOS_PRINTER_FOR_RECEIPT = "RECEIPT_JAVAPOS_PRINTER_NAME";
	public final static String P_CASH_DRAWER_NAME = "CASH_DRAWER_NAME";
	public final static String P_KITCHEN_PRINTER_TYPE = "KITCHEN_PRINTER_TYPE";
	public final static String P_OS_PRINTER_FOR_KITCHEN = "KITCHEN_OS_PRINTER_NAME";
	public final static String P_JAVAPOS_PRINTER_FOR_KITCHEN = "KITCHEN_JAVAPOS_PRINTER_NAME";
	public final static String P_PRINT_RECEIPT_ON_ORDER_FINISH = "PRINT_RECEIPT_ON_ORDER_FINISH";
	public final static String P_PRINT_RECEIPT_ON_ORDER_SETTLE = "PRINT_RECEIPT_ON_ORDER_SETTLE";
	public final static String P_PRINT_TO_KITCHEN_ON_ORDER_FINISH = "PRINT_TO_KITCHEN_ON_ORDER_FINISH";
	public final static String P_PRINT_TO_KITCHEN_ON_ORDER_SETTLE = "PRINT_TO_KITCHEN_ON_ORDER_SETTLE";

	public static String getOsReceiptPrinterName() {
		return ApplicationConfig.getString(P_OS_PRINTER_FOR_RECEIPT, "POSPrinter");
	}

	public static String getOsKitchenPrinterName() {
		return ApplicationConfig.getString(P_OS_PRINTER_FOR_KITCHEN, "KitchenPrinter");
	}

	public static boolean isPrintReceiptWhenSetteled() {
		return ApplicationConfig.getBoolean(P_PRINT_RECEIPT_ON_ORDER_FINISH, true);
	}

	public static boolean isPrintReceiptWhenPaid() {
		return ApplicationConfig.getBoolean(P_PRINT_RECEIPT_ON_ORDER_SETTLE, false);
	}

	public static boolean isPrintKitchenWhenSetteled() {
		return ApplicationConfig.getBoolean(P_PRINT_TO_KITCHEN_ON_ORDER_FINISH, false);
	}

	public static boolean isPrintKitchenWhenPaid() {
		return ApplicationConfig.getBoolean(P_PRINT_TO_KITCHEN_ON_ORDER_SETTLE, false);
	}
}
